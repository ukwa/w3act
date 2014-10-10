package uk.bl.api;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import models.Document;
import play.Logger;

public class CrawlData {
	
	private static String logFileName = "crawl.log";
	
	public static List<Document> renameThisMethodLaterAccordingly() {
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
	
}
