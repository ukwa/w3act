package uk.bl.crawling;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.avaje.ebean.Ebean;

import models.Document;
import models.WatchedTarget;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import play.libs.F.*;
import play.libs.F.Promise;

public class CrawlActor extends UntypedActor {
	LoggingAdapter log = Logging.getLogger(getContext().system(), this);
	
	public static class StartMessage {}
    
	private class CrawlFunction implements Function0<List<Document>> {
		
		WatchedTarget watchedTarget;
		
		public CrawlFunction(WatchedTarget watchedTarget) {
			this.watchedTarget = watchedTarget;
		}
		
		public List<Document> apply() {
			log.info("Crawling " + watchedTarget.target.field_url);
			crawlAndConvertDocuments(watchedTarget, null);
			log.info("Finished crawling " + watchedTarget.target.field_url);
			return null;
		}

	}
	
	public static List<Document> crawlAndConvertDocuments(WatchedTarget watchedTarget, Integer maxDocuments) {
		List<Document> documentList = (new Crawler()).crawlForDocuments(watchedTarget, maxDocuments);
		List<Document> newDocumentList = new ArrayList<>();
		
		for (Document document : documentList)
			if (Document.find.where().eq("document_url", document.documentUrl).findRowCount() == 0)
				newDocumentList.add(document);
		
		Ebean.save(newDocumentList);
		
		for (Document document : newDocumentList) {
			try {
				convertPdfToHtml(document);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return newDocumentList;
	}
	
	private static void convertPdfToHtml(Document document) throws IOException {
		ProcessBuilder builder = new ProcessBuilder(
 			"/bin/bash", "-c", "cd converter && ./convertPdfToHtml.sh " + document.documentUrl + " '" + document.title + "'");
		builder.redirectErrorStream(true);
		Process p = builder.start();
		/*BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
		String line;
		while (true) {
			line = r.readLine();
 			if (line == null) { break; }
				System.out.println(line);
		}*/
	}
	
	public void onReceive(Object message) throws Exception {
		if (message instanceof StartMessage) {
			log.info("Starting Crawl");
			List<WatchedTarget> watchedTargets = WatchedTarget.find.all();
			for (WatchedTarget watchedTarget : watchedTargets) {
				Promise.promise(new CrawlFunction(watchedTarget));
			}
		}
	}
}