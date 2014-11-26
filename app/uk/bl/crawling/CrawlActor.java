package uk.bl.crawling;

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
        	List<Document> documentList = (new Crawler()).crawlForDocuments(watchedTarget);
        	List<Document> newDocumentList = new ArrayList<>();
        	
        	for (Document document : documentList)
        		if (Document.find.where().eq("document_url", document.documentUrl).findRowCount() == 0)
        			newDocumentList.add(document);
        	
        	Ebean.save(newDocumentList);
        	
        	log.info("Finished crawling " + watchedTarget.target.field_url);
        	
            return newDocumentList;
        }
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