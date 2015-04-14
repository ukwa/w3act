package uk.bl.crawling;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.avaje.ebean.Ebean;

import controllers.Documents;
import controllers.TargetController;
import controllers.WatchedTargets;
import models.Document;
import models.WatchedTarget;
import akka.actor.UntypedActor;
import play.Logger;
import play.libs.Akka;
import play.libs.F.Function0;
import play.libs.F.Promise;
import scala.concurrent.duration.Duration;

public class CrawlActor extends UntypedActor {
	
	public static class CrawlMessage {}
	public static class ConvertMessage {}
    
	private class CrawlFunction implements Function0<List<Document>> {
		
		private WatchedTarget watchedTarget;
		
		public CrawlFunction(WatchedTarget watchedTarget) {
			this.watchedTarget = watchedTarget;
		}
		
		public List<Document> apply() {
			Logger.info("Crawling " + watchedTarget.target.fieldUrls.get(0).url);
			List<String> newerCrawlTimes = Crawler.getNewerCrawlTimes(watchedTarget);
			Logger.debug("got " + newerCrawlTimes.size() + " new crawl dates");
			for (String crawlTime : newerCrawlTimes)
				crawlDocuments(watchedTarget, true, crawlTime, 2, null);
			Logger.info("Finished crawling " + watchedTarget.target.fieldUrls.get(0).url);
			return null;
		}

	}
	
	public static List<Document> crawlDocuments(WatchedTarget watchedTarget,
			boolean crawlWayback, String crawlTime, int depth, Integer maxDocuments) {
		Logger.debug("crawlDocuments of " + watchedTarget.target.fieldUrls.get(0).url + " (date: " + crawlTime + ")");
		List<Document> documentList = (new Crawler(crawlWayback)).crawlForDocuments(watchedTarget, crawlTime, depth, maxDocuments);
		List<Document> newDocumentList = Documents.filterNew(documentList);
		if (documentList.isEmpty()) {
			TargetController.raiseFlag(watchedTarget.target, "No Documents Found");
		} else {
			if (crawlWayback &&
					(watchedTarget.waybackTimestamp == null ||
					crawlTime.compareTo(watchedTarget.waybackTimestamp) > 0)) {
				WatchedTargets.setWaybackTimestamp(watchedTarget, crawlTime);
			}			
			Ebean.save(newDocumentList);
		}		
		return newDocumentList;
	}
	
	public static void crawlAndConvertDocuments(WatchedTarget watchedTarget,
			boolean crawlWayback, String crawlTime, int depth, Integer maxDocuments) {
		convertDocuments(crawlDocuments(watchedTarget, crawlWayback, crawlTime, depth, maxDocuments));
	}
	
	public static void convertDocuments(List<Document> newDocumentList) {
		String ctphFile = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ".txt";
		for (Document document : newDocumentList) {
			try {
				convertPdfToHtml(document, ctphFile);
				Documents.addHashes(document);
			} catch (IOException e) {
				Logger.error(e.getMessage());
			}
		}
		try {
			compareHashes(ctphFile);
			Documents.addDuplicateAlert();
		} catch (IOException e) {
			Logger.error(e.getMessage());
		}		
	}

	public static void convertDocuments() {
		List<Document> newDocumentList = Document.find.where().eq("sha256hash", null).findList();
		convertDocuments(newDocumentList);
	}

	private static void convertPdfToHtml(Document document, String ctphFile) throws IOException {
		ProcessBuilder builder = new ProcessBuilder("/bin/bash", "-c",
				"cd conf/converter && ./convertPdfToHtml.sh '" + document.documentUrl +
				"' '" + document.id + "' " + ctphFile);
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
	
	private static void compareHashes(String ctphFile) throws IOException {
		ProcessBuilder builder = new ProcessBuilder("/bin/bash", "-c",
				"cd conf/converter && ./compareHashes.sh " + ctphFile);
		builder.start();
	}
	
	public void onReceive(Object message) throws Exception {
		if (message instanceof CrawlMessage) {
			Logger.info("Starting crawl");
			List<WatchedTarget> watchedTargets = WatchedTarget.find.all();
			for (WatchedTarget watchedTarget : watchedTargets) {
				Promise.promise(new CrawlFunction(watchedTarget));
			}
			Akka.system().scheduler().scheduleOnce(
					Duration.create(1, TimeUnit.HOURS),
					getSelf(),
					new CrawlActor.ConvertMessage(),
					Akka.system().dispatcher(),
					null
			);
		} else if (message instanceof ConvertMessage) {
			Logger.info("Convert documents");
			convertDocuments();
		}
	}
}