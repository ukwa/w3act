package uk.bl.api;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import models.Document;
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
	
	public static List<Document> crawlForDocuments(String url) {
		knownSites = new HashSet<>();
		foundDocuments = new ArrayList<>();
		
		knownSites.add(url);
		processPage(url, 1);
		
		return foundDocuments;
	}
	
	public static void processPage(String url, int linkDepth) {
		try {
	 
			org.jsoup.nodes.Document doc = Jsoup.connect(url).get();
			
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
					} else if(linkDepth > 0 && domainIsEqual(url,targetUrl)) {
						knownSites.add(targetUrl);
						System.out.println(targetUrl + " (via " + url + ")");
						processPage(targetUrl, linkDepth - 1);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static boolean domainIsEqual(String url, String targetUrl) {
		if (targetUrl.split("/").length <= 2) return false;
		return url.split("/")[2].equals(targetUrl.split("/")[2]);
	}
	
}
