package controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import akka.actor.ActorRef;
import akka.actor.Props;

import com.avaje.ebean.Ebean;
import com.fasterxml.jackson.databind.JsonNode;

import models.Document;
import models.User;
import models.WatchedTarget;
import play.Logger;
import play.data.DynamicForm;
import play.data.Form;
import play.libs.Akka;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Result;
import play.mvc.Security;
import scala.concurrent.duration.Duration;
import uk.bl.crawling.CrawlActor;
import uk.bl.crawling.Crawler;
import views.html.watchedtargets.list;

@Security.Authenticated(SecuredController.class)
public class WatchedTargets extends AbstractController {

    /**
     * Display the paginated list of Documents.
     *
     * @param page Current page number (starts from 0)
     * @param sortBy Column to be sorted
     * @param order Sort order (either asc or desc)
     * @param filter Filter applied on Documents
     */
    public static Result list(String userString, boolean children, int pageNo, String sortBy, String order, String filter) {
        return renderList(userString, children, pageNo, sortBy, order, filter, true);
    }
    
    public static Result overview(int pageNo, String sortBy, String order) {
    	return renderList("" + User.findByEmail(request().username()).id, true, pageNo, sortBy, order, "", false);
    }
    
    public static Result renderList(String userString, boolean children, int pageNo, String sortBy, String order, String filter, boolean filters) {
    	Logger.info("WatchedTargets.list()");
    	
    	Long userId = userString.isEmpty() || userString.equals("null") ?
				null : new Long(userString);
    	
        return ok(
        	list.render(
        			User.findByEmail(request().username()),
        			filterForm(userId, children),
        			filter,
        			WatchedTarget.page(userId, children, pageNo, 10, sortBy, order, filter),
        			sortBy,
        			order,
        			filters)
        	);
    }
    
    public static Result view(Long id) {
    	WatchedTarget watchedTarget = WatchedTarget.find.byId(id);
		return redirect(routes.WatchedTargets.list("" + watchedTarget.target.authorUser.id, true, 0, "title", "asc", watchedTarget.target.title));
	}
    
    public static Result crawl(Long id, boolean crawlWayback) {
    	WatchedTarget watchedTarget = WatchedTarget.find.byId(id);
    	if (crawlWayback) {
	    	List<String> newerCrawlTimes = Crawler.getNewerCrawlTimes(watchedTarget);
	    	for (String crawlTime : newerCrawlTimes) {
	    		Logger.debug("crawlTime: " + crawlTime);
	    		CrawlActor.crawlAndConvertDocuments(watchedTarget, crawlWayback, crawlTime, 3);
	    	}
    	} else {
    		CrawlActor.crawlAndConvertDocuments(watchedTarget, crawlWayback, null, 3);
    	}
    	
    	return redirect(routes.Documents.list("" + watchedTarget.target.authorUser.id, "" + id, "", "",
    			Document.Status.NEW.toString(), 0, "title", "asc", ""));
    }
    
    public static Result crawlAll() {
    	ActorRef crawlActor = Akka.system().actorOf(Props.create(CrawlActor.class));
    	Akka.system().scheduler().scheduleOnce(
				Duration.create(0, TimeUnit.MILLISECONDS),
				crawlActor,
				new CrawlActor.CrawlMessage(),
				Akka.system().dispatcher(),
				null
    	);
    	return redirect(routes.ApplicationController.home());
    }
    
    public static Result convert() {
    	ActorRef crawlActor = Akka.system().actorOf(Props.create(CrawlActor.class));
    	Akka.system().scheduler().scheduleOnce(
				Duration.create(0, TimeUnit.MILLISECONDS),
				crawlActor,
				new CrawlActor.ConvertMessage(),
				Akka.system().dispatcher(),
				null
    	);
    	return redirect(routes.ApplicationController.home());
    }
    
    public static void setWaybackTimestamp(WatchedTarget watchedTarget, String waybackTimestamp) {
    	watchedTarget.waybackTimestamp = waybackTimestamp;
    	Ebean.update(watchedTarget);
    }
    
    public static String prettyDateString(String waybackTimestamp) {
    	String year = waybackTimestamp.substring(0, 4);
    	String month = waybackTimestamp.substring(4, 6);
    	String day = waybackTimestamp.substring(6, 8);
    	String hour = waybackTimestamp.substring(8, 10);
    	String minute = waybackTimestamp.substring(10, 12);
    	return day + "-" + month + "-" + year + " " + hour + ":" + minute;
    }
    
    @BodyParser.Of(BodyParser.Json.class)
    public static Result filterByJson(String title) {
        JsonNode jsonData = null;
        if (title != null) {
	        List<WatchedTarget> watchedTargets = WatchedTarget.find.where().join("target")
	        		.where().icontains(WatchedTarget.SEARCH_FIELD, title).findList();
	        jsonData = Json.toJson(watchedTargets);
        }
        return ok(jsonData);
    }
    
    public static DynamicForm filterForm(Long userId, boolean children) {
    	Map<String,String> filterData = new HashMap<>();
    	filterData.put("user", "" + userId);
    	filterData.put("children", "" + children);
    	return Form.form().bind(filterData);
    }
}
