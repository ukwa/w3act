package uk.bl.api;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import models.Document;
import models.WatchedTarget;
import play.Logger;

public class CrawlData {
	
	private static String logFileName = "crawl.log";
	private static Set<String> knownSites;
	private static List<Document> foundDocuments;
	
	public static List<Document> retrieveDocuments() {
		Logger.info("parse log file ...");
		
		List<Document> documentList = new ArrayList<>();
		
		try (BufferedReader reader = new BufferedReader(new FileReader(logFileName))) {
			String line = null;
			while ((line = reader.readLine()) != null) {
				String[] logEntries = line.split(" +");
				String url = logEntries[3];
				if (url.endsWith(".pdf")) {
					Document document = new Document();
			    	document.landingPageUrl = logEntries[5];
			    	document.documentUrl = url;
			    	document.filename = url.substring(url.lastIndexOf('/')+1);
			    	document.title = document.filename.substring(0, document.filename.indexOf('.'));
			    	documentList.add(document);
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return documentList;
	}
	
	public static List<Document> crawlForDocuments(WatchedTarget watchedTarget) {
		knownSites = new HashSet<>();
		foundDocuments = new ArrayList<>();
		
		knownSites.add(watchedTarget.url);
		Set<Link> fringe = new HashSet<>();
		fringe.add(new Link(null, watchedTarget.url));
		breathFirstSearch(watchedTarget, fringe, 1);
		
		return foundDocuments;
	}
	
	/*public static void processPage(String url, int linkDepth) {
		try {
			Connection connection = Jsoup.connect(url);
			org.jsoup.nodes.Document doc = connection.get();
			Response response = connection.response();
			
			if (!response.contentType().contains("html"))
				System.out.println("contentType: " + response.contentType());
			
			Elements links = doc.select("a[href]");
			for(Element link: links){
				String targetUrl = link.absUrl("href");
				if (!knownSites.contains(targetUrl)) {
					if (targetUrl.endsWith(".pdf")) {
						knownSites.add(targetUrl);
						System.out.println("pdf: " + targetUrl + " (via " + url + ")");
						Document document = new Document();
						document.landingPageUrl = url;
						document.documentUrl = targetUrl;
						document.filename = targetUrl.substring(targetUrl.lastIndexOf('/')+1);
				    	document.title = document.filename.substring(0, document.filename.indexOf('.'));
						foundDocuments.add(document);
					} else if(linkDepth >= 0 && domainIsEqual(url,targetUrl)) {
						knownSites.add(targetUrl);
						System.out.println(targetUrl + " (via " + url + ")");
						processPage(targetUrl, linkDepth - 1);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}*/
	
	private static void breathFirstSearch (WatchedTarget watchedTarget, Set<Link> fringe, int linkDepth) {
		Set<Link> children = new HashSet<>();
		for (Link link : fringe) {
			try {
				Connection connection = Jsoup.connect(link.target);
				
				connection.request().method(Method.GET);
				connection.ignoreContentType(true);
				connection.execute();
				
				Response response = connection.response();
				
				if (response.contentType().contains("html")) {
					if (linkDepth >= 0) {
						org.jsoup.nodes.Document doc = response.parse();
						
						for(Element element : doc.select("a[href]")){
							String targetUrl = element.absUrl("href");
							if (!knownSites.contains(targetUrl)) {
								if (targetUrl.endsWith(".pdf")) {
									knownSites.add(targetUrl);
									System.out.println("pdf: " + targetUrl + " (via " + link.target + ")");
									Document document = new Document();
									document.landingPageUrl = link.target;
									document.documentUrl = targetUrl;
									document.filename = targetUrl.substring(targetUrl.lastIndexOf('/')+1);
									document.title = document.filename.substring(0, document.filename.indexOf('.'));
									foundDocuments.add(document);
								} else if(domainIsEqual(link.target,targetUrl) &&
										(linkDepth > 0 || watchedTarget.deepDocumentSearch)) {
									knownSites.add(targetUrl);
									//System.out.println(targetUrl + " (via " + link.target + ")");
									children.add(new Link(link.target, targetUrl));
								}
							}
						}
					}
				} else if (watchedTarget.deepDocumentSearch) {
					System.out.println("contentType: " + response.contentType());
					//System.out.println("contentDisposition: " + response.header("Content-Disposition"));
					String contentDisposition = response.header("Content-Disposition");
					if (contentDisposition != null && contentDisposition.endsWith(".pdf")) {
						Document document = new Document();
						document.landingPageUrl = link.source;
						document.documentUrl = link.target;
						document.filename = contentDisposition.substring(contentDisposition.lastIndexOf('=')+1);
						document.title = document.filename.substring(0, document.filename.indexOf('.'));
						foundDocuments.add(document);
						System.out.println("hidden pdf: " + document.filename + " (url: " + link.target + ")");
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (linkDepth > 0 || (watchedTarget.deepDocumentSearch && linkDepth == 0))
			breathFirstSearch(watchedTarget, children, linkDepth - 1);
	}

	private static boolean domainIsEqual(String url, String targetUrl) {
		if (targetUrl.split("/").length <= 2) return false;
		return url.split("/")[2].equals(targetUrl.split("/")[2]);
	}
	
}
