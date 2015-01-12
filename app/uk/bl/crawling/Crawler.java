package uk.bl.crawling;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import models.Document;
import models.WatchedTarget;
import play.Logger;

public class Crawler {
	private Set<String> knownSites;
	private List<Document> foundDocuments;
	private Integer maxDocuments;
	
	private static boolean crawlWayback = false;
	private static String waybackUrl = "http://www.webarchive.org.uk/wayback/archive/";
	
	public List<Document> crawlForDocuments(WatchedTarget watchedTarget, Integer maxDocuments) {
		knownSites = new HashSet<>();
		foundDocuments = new ArrayList<>();
		this.maxDocuments = maxDocuments;
		
		String seedUrl = crawlWayback ?
				waybackReplayUrl(watchedTarget.target.field_url, "20140522210454") :
				watchedTarget.target.field_url;
		knownSites.add(seedUrl);
		Set<Link> fringe = new HashSet<>();
		fringe.add(new Link(null, seedUrl));
		breathFirstSearch(watchedTarget, fringe, 1);
		
		return foundDocuments;
	}
	
	private void breathFirstSearch (WatchedTarget watchedTarget, Set<Link> fringe, int linkDepth) {
		if (linkDepth < -1 || (linkDepth == -1 && !foundDocuments.isEmpty())) return;
		Set<Link> children = new HashSet<>();
		for (Link link : fringe) {
			try {
				if (linkDepth >= 0 || urlMatchesScheme(link.target, watchedTarget.documentUrlScheme)) {
					Connection connection = Jsoup.connect(link.target);
					
					connection.request().method(Method.GET);
					connection.ignoreContentType(true);
					connection.execute();
					
					Response response = connection.response();
					
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
											document.filename = URLDecoder.decode(hrefUrl.substring(hrefUrl.lastIndexOf('/')+1), "UTF-8");
											document.title = document.filename.substring(0, document.filename.indexOf('.'));
											document.watchedTarget = watchedTarget;
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
						String contentDisposition = response.header("Content-Disposition").replace("\"", "");
						if (contentDisposition != null && contentDisposition.endsWith(".pdf")) {
							Document document = new Document();
							document.landingPageUrl = crawlWayback ?
									urlFromWayback(link.source) : link.source;
							document.documentUrl = pageUrl;
							document.filename = contentDisposition.substring(contentDisposition.lastIndexOf('=')+1);
							document.title = document.filename.substring(0, document.filename.indexOf('.'));
							document.watchedTarget = watchedTarget;
							foundDocuments.add(document);
							if (maxDocuments != null && foundDocuments.size() >= maxDocuments) return;
							Logger.debug("hidden pdf found: " + document.filename + " (url: " + pageUrl + ")");
						}
					}
				}
			} catch (IOException e) {
				Logger.info("Can't get content of url: " + link.target);
			}
		}
		
		breathFirstSearch(watchedTarget, children, linkDepth - 1);
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
