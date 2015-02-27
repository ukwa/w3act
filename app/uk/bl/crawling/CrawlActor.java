package uk.bl.crawling;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.avaje.ebean.Ebean;

import controllers.Documents;
import controllers.TargetController;
import controllers.WatchedTargets;
import models.Document;
import models.WatchedTarget;
import akka.actor.UntypedActor;
import play.Logger;
import play.libs.F.Function0;
import play.libs.F.Promise;

public class CrawlActor extends UntypedActor {
	
	public static class StartMessage {}
    
	private class CrawlFunction implements Function0<List<Document>> {
		
		private WatchedTarget watchedTarget;
		private String crawlTime;
		
		public CrawlFunction(WatchedTarget watchedTarget, String crawlTime) {
			this.watchedTarget = watchedTarget;
			this.crawlTime = crawlTime;
		}
		
		public List<Document> apply() {
			Logger.info("Crawling " + watchedTarget.target.fieldUrls.get(0).url);
			crawlAndConvertDocuments(watchedTarget, true, crawlTime, null);
			Logger.info("Finished crawling " + watchedTarget.target.fieldUrls.get(0).url);
			return null;
		}

	}
	
	public static List<Document> crawlAndConvertDocuments(WatchedTarget watchedTarget,
			boolean crawlWayback, String crawlTime, Integer maxDocuments) {
		List<Document> documentList = (new Crawler(crawlWayback)).crawlForDocuments(watchedTarget, crawlTime, maxDocuments);
		List<Document> newDocumentList = new ArrayList<>();
		if (documentList.isEmpty()) {
			TargetController.raiseFlag(watchedTarget.target, "No Documents Found");
		} else {
			if (crawlWayback &&
					(watchedTarget.waybackTimestamp == null ||
					crawlTime.compareTo(watchedTarget.waybackTimestamp) > 0)) {
				WatchedTargets.setWaybackTimestamp(watchedTarget, crawlTime);
			}
			for (Document document : documentList)
				if (Document.find.where().eq("document_url", document.documentUrl).findRowCount() == 0)
					newDocumentList.add(document);
			
			Ebean.save(newDocumentList);
			
			for (Document document : newDocumentList) {
				try {
					convertPdfToHtml(document);
					Documents.addHash(document);
				} catch (IOException e) {
					Logger.error(e.getMessage());
				}
			}
		}
		
		return newDocumentList;
	}

	private static void convertPdfToHtml(Document document) throws IOException {
		ProcessBuilder builder = new ProcessBuilder(
 			"/bin/bash", "-c", "cd conf/converter && ./convertPdfToHtml.sh '" + document.documentUrl + "' '" + document.id + "'");
		builder.redirectErrorStream(true);
		Process p = builder.start();
		BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
		String line;
		while (true) {
			line = r.readLine();
 			if (line == null) { break; }
			Logger.debug(line);
		}
	}
	
	public void onReceive(Object message) throws Exception {
		if (message instanceof StartMessage) {
			Logger.info("Starting Crawl");
			List<WatchedTarget> watchedTargets = WatchedTarget.find.all();
			for (WatchedTarget watchedTarget : watchedTargets) {
				List<String> newerCrawlTimes = Crawler.getNewerCrawlTimes(watchedTarget);
				for (String crawlTime : newerCrawlTimes)
					Promise.promise(new CrawlFunction(watchedTarget, crawlTime));
			}
		}
	}
}