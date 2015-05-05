package uk.bl.crawling;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import models.Document;
import models.WatchedTarget;
import play.Logger;
import play.Play;
import play.libs.F.Function;
import play.libs.F.Promise;
import play.libs.XPath;
import play.libs.ws.WS;
import play.libs.ws.WSRequestHolder;
import play.libs.ws.WSResponse;

public class Crawler {
	private Set<String> knownSites;
	private List<Document> foundDocuments;
	private Integer maxDocuments;
	private String crawlTime;
	
	private boolean crawlWayback = false;
	private static String waybackUrl = Play.application().configuration().getString("wayback_url");
	
	private Map<String, MetadataExtractor> metadataExtractors;
	
	public Crawler(boolean crawlWayback) {
		this.crawlWayback = crawlWayback;
		metadataExtractors = new HashMap<>();
		metadataExtractors.put("www.ifs.org.uk", new MetadataExtractor("*[itemtype=http://schema.org/CreativeWork] *[itemprop=name]",
				"*[itemtype=http://schema.org/CreativeWork] *[itemprop=datePublished]",
				"*[itemtype=http://schema.org/CreativeWork] *[itemprop=author]"));
		metadataExtractors.put("www.gov.uk", new MetadataExtractor("h1", null, null));
	}
	
	private static class CaptureRequestFunction implements Function<WSResponse, List<String>> {
		private String waybackTimestamp;
		
		public CaptureRequestFunction(String waybackTimestamp) {
			this.waybackTimestamp = waybackTimestamp;
		}
		public List<String> apply(WSResponse response) {
			List<String> timestamps = new ArrayList<>();
			try {
				org.w3c.dom.Document xml = response.asXml();
				if (xml != null) {
					NodeList nodes = XPath.selectNodes("/wayback/results/result/capturedate", xml);
					for (int i=0; i < nodes.getLength(); i++) {
						Node node = nodes.item(i);
						if (waybackTimestamp == null || node.getTextContent().compareTo(waybackTimestamp) > 0)
							timestamps.add(node.getTextContent());
					}
				}
			} catch (Exception e) {
				Logger.error("Can't get timestamps via the Wayback API: " + e.getMessage());
			}
			return timestamps;
		}
	}
	
	public static List<String> getNewerCrawlTimes(WatchedTarget watchedTarget) {
		WSRequestHolder holder = WS.url(waybackUrl + "xmlquery")
				.setQueryParameter("type", "urlquery")
				.setQueryParameter("url", watchedTarget.target.fieldUrls.get(0).url);
		if (watchedTarget.waybackTimestamp != null)
			holder.setQueryParameter("startdate", watchedTarget.waybackTimestamp);
		
		Promise<List<String>> timestampPromise = holder.get().map(
				new CaptureRequestFunction(watchedTarget.waybackTimestamp));
		return timestampPromise.get(5000);
	}
	
	public List<Document> crawlForDocuments(WatchedTarget watchedTarget, String crawlTime, int depth, Integer maxDocuments) {
		Logger.debug("crawlForDocuments");
		knownSites = new HashSet<>();
		foundDocuments = new ArrayList<>();
		this.crawlTime = crawlTime;
		this.maxDocuments = maxDocuments;
		
		String seedUrl = crawlWayback ?
				waybackReplayUrl(watchedTarget.target.fieldUrls.get(0).url, crawlTime) :
				watchedTarget.target.fieldUrls.get(0).url;
		knownSites.add(seedUrl);
		Set<Link> fringe = new HashSet<>();
		fringe.add(new Link(null, seedUrl));
		breathFirstSearch(watchedTarget, fringe, depth);
		
		return foundDocuments;
	}
	
	private void breathFirstSearch (WatchedTarget watchedTarget, Set<Link> fringe, int linkDepth) {
		Logger.debug("breathFirstSearch");
		if (linkDepth < -1 || (linkDepth == -1 && !foundDocuments.isEmpty())) return;
		Set<Link> children = new HashSet<>();
		for (Link link : fringe) {
			try {
				if (linkDepth >= 0 || urlMatchesScheme(link.target, watchedTarget.documentUrlScheme)) {
									
					Response response = getResponse(link.target);
					String pageUrl = crawlWayback ?
							urlFromWayback(link.target) : link.target;
					
					if (response.contentType().contains("html")) {
						if (linkDepth >= 0) {
							org.jsoup.nodes.Document doc = response.parse();
							
							for(Element element : doc.select("a[href]")) {
								String waybackHrefUrl = element.absUrl("href").replace(" ", "%20");
								String hrefUrl = crawlWayback ?
										urlFromWayback(waybackHrefUrl) : waybackHrefUrl;
								if (hrefUrl != null && !knownSites.contains(hrefUrl)) {
									if (hrefUrl.endsWith(".pdf")) {
										if (urlMatchesScheme(hrefUrl, watchedTarget.documentUrlScheme)) {
											knownSites.add(hrefUrl);
											Logger.debug("pdf found: " + hrefUrl + " (via " + link.target + ")");
											Document document = new Document();
											document.landingPageUrl = pageUrl;
											document.documentUrl = hrefUrl;
											document.waybackTimestamp = crawlTime;
											document.setStatus(Document.Status.NEW);
											document.filename = URLDecoder.decode(hrefUrl.substring(hrefUrl.lastIndexOf('/')+1), "UTF-8");
											document.title = document.filename.substring(0, document.filename.indexOf('.'));
											document.watchedTarget = watchedTarget;
											document.fastSubjects = watchedTarget.fastSubjects;
											extractMetadata(document);
											foundDocuments.add(document);
											if (maxDocuments != null && foundDocuments.size() >= maxDocuments) return;
										}
									} else if(domainIsEqual(pageUrl, hrefUrl)) {
										knownSites.add(hrefUrl);
										children.add(new Link(link.target, waybackHrefUrl));
									}
								}
							}
						}
					} else if (urlMatchesScheme(pageUrl, watchedTarget.documentUrlScheme)) {
						String contentDisposition = response.header("Content-Disposition");
						contentDisposition = contentDisposition == null ?
								"" : contentDisposition.replace("\"", "");
						String contentType = response.header("Content-Type");
						if (contentType.equals("application/pdf") || contentDisposition.endsWith(".pdf")) {
							Document document = new Document();
							document.landingPageUrl = crawlWayback ?
									urlFromWayback(link.source) : link.source;
							document.documentUrl = pageUrl;
							document.waybackTimestamp = crawlTime;
							document.setStatus(Document.Status.NEW);
							if (contentType.equals("application/pdf"))
								document.filename = URLDecoder.decode(pageUrl.substring(pageUrl.lastIndexOf('/')+1), "UTF-8");
							else
								document.filename = contentDisposition.substring(contentDisposition.lastIndexOf('=')+1);
							document.title = document.filename.substring(0, document.filename.indexOf('.'));
							document.watchedTarget = watchedTarget;
							document.fastSubjects = watchedTarget.fastSubjects;
							Logger.debug("hidden pdf found: " + document.filename + " (url: " + pageUrl + ")");
							foundDocuments.add(document);
							if (maxDocuments != null && foundDocuments.size() >= maxDocuments) return;
						}
					}
				}
			} catch (IOException e) {
				Logger.info("Can't get content of url: " + link.target);
				e.printStackTrace();
			}
		}
		
		breathFirstSearch(watchedTarget, children, linkDepth - 1);
	}
	
	public void extractMetadata(Document document) {
		
		try {
			String domain = new URI(document.landingPageUrl).getHost();
			if (metadataExtractors.containsKey(domain)) {
				MetadataExtractor metadataExtractor = metadataExtractors.get(domain);
				org.jsoup.nodes.Document doc = Jsoup.connect(document.landingPageUrl).get();
				metadataExtractor.extract(document, doc);
			}
		} catch (IOException | URISyntaxException e) {
			e.printStackTrace();
		}
	}

	private Response getResponse(String url) throws IOException {
		Logger.debug("getResponse: " + url);
		Connection connection = Jsoup.connect(url);
		
		connection.request().method(Method.GET);
		
		connection.ignoreContentType(true);
		connection.execute();
		
		return connection.response();
	}

	private String waybackReplayUrl(String url, String timestamp) {
		return waybackUrl + "replay?url=" + url + "&date=" + timestamp;
	}
	
	private String urlFromWayback(String waybackUrl) {
		Pattern urlPattern1 = Pattern.compile("^[^?]+\\?.*url=([^&]+).*$");
		Pattern urlPattern2 = Pattern.compile("^[^/]+//.*/([^/]+//.*)$");
		Matcher matcher1 = urlPattern1.matcher(waybackUrl);
		if (matcher1.matches())
			return matcher1.group(1);
		Matcher matcher2 = urlPattern2.matcher(waybackUrl);
		if (matcher2.matches())
			return matcher2.group(1);
		return null;
	}

	private boolean domainIsEqual(String url, String targetUrl) {
		if (targetUrl.split("/").length <= 2) return false;
		return url.split("/")[2].equals(targetUrl.split("/")[2]);
	}
	

	
	private boolean urlMatchesScheme(String url, String scheme) {
		String urlWithoutProtocol = url.substring(url.indexOf("//") + 2);
		return urlWithoutProtocol.startsWith(scheme);
	}
	
}
