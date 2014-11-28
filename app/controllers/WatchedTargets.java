package controllers;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.avaje.ebean.Ebean;
import com.fasterxml.jackson.databind.JsonNode;

import models.User;
import models.WatchedTarget;
import play.Logger;
import play.data.DynamicForm;
import play.data.Form;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Result;
import play.mvc.Security;
import uk.bl.Const;
import uk.bl.crawling.CrawlActor;
import views.html.watchedtargets.list;

@Security.Authenticated(Secured.class)
public class WatchedTargets extends AbstractController {

    /**
     * Display the paginated list of Documents.
     *
     * @param page Current page number (starts from 0)
     * @param sortBy Column to be sorted
     * @param order Sort order (either asc or desc)
     * @param filter Filter applied on Documents
     */
    public static Result list(int pageNo, String sortBy, String order, String filter) {
    	Logger.info("WatchedTargets.list()");
    	
    	User user = User.findByEmail(request().username());
    	
    	if (WatchedTarget.find.findRowCount() == 0) {
	    	List<WatchedTarget> watchedTargetsTestData = Arrays.asList(
	    			new WatchedTarget(user, "ifs", "act-ifs",
	    					"http://www.ifs.org.uk/publications/re", "www.ifs.org.uk/uploads/publications"),
	    			new WatchedTarget(user, "thinknpc", "act-thinknpc",
	    					"http://www.thinknpc.org/publications/", "www.thinknpc.org"),
	    			new WatchedTarget(user, "ofsted", "act-ofsted",
	    					"http://www.ofsted.gov.uk/resources/surveys", "www.ofsted.gov.uk/sites/default/files/documents"),
	    			new WatchedTarget(user, "parliament", "act-parliament",
	    					"http://www.parliament.uk/business/committees/committees-a-z/commons-select/home-affairs-committee/publications/", "www.publications.parliament.uk"),
	    			new WatchedTarget(user, "gov", "act-gov",
	    					"https://www.gov.uk/government/publications", "www.gov.uk/government/uploads")
	    			);
	    	try {
		    	for (WatchedTarget watchedTarget : watchedTargetsTestData) {
		    		Ebean.save(watchedTarget.target);
		    	}
		    	Ebean.save(watchedTargetsTestData);
	    	} catch (Exception e) {
				Logger.error(e.getMessage());
			}
    	}
    	    	
        return ok(
        	list.render(
        			user,
        			filter,
        			WatchedTarget.page(user, pageNo, 10, sortBy, order, filter),
        			sortBy,
        			order)
        	);
    }
    
    public static Result search() {
    	DynamicForm form = Form.form().bindFromRequest();
    	String action = form.get("action");
    	String query = form.get(Const.QUERY);
		Logger.info("query: " + query);
		Logger.info("action: " + action);
    	
    	if (StringUtils.isBlank(query)) {
			Logger.info("Document name is empty. Please write name in search window.");
			flash("message", "Please enter a name in the search window");
	        return redirect(
	        		routes.WatchedTargets.list(0, "title", "asc", "")
	        );
    	}

    	int pageNo = getQueryParamAsInt(Const.PAGE_NO, 0);
    	String sort = getQueryParam(Const.SORT_BY);
    	String order = getQueryParam(Const.ORDER);

    	if (StringUtils.isEmpty(action)) {
    		return badRequest("You must provide a valid action");
    	} else {
    		if (Const.SEARCH.equals(action)) {
    	    	return redirect(routes.WatchedTargets.list(pageNo, sort, order, query));
		    } else {
		      return badRequest("This action is not allowed");
		    }
    	}
    }
    
    public static Result crawl(Long id) {
    	WatchedTarget watchedTarget = WatchedTarget.find.byId(id);
    	CrawlActor.crawlAndConvertDocuments(watchedTarget, 3);
    	return redirect(routes.Documents.list(id, false, 0, "title", "asc", ""));
    }
    
    @BodyParser.Of(BodyParser.Json.class)
    public static Result filterByJson(String title) {
        JsonNode jsonData = null;
        if (title != null) {
	        List<WatchedTarget> watchedTargets = WatchedTarget.find.where().join("target")
	        		.where().icontains("target.title", title).findList();
	        jsonData = Json.toJson(watchedTargets);
        }
        return ok(jsonData);
    }
}
