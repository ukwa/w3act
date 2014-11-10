package controllers;

import java.util.Arrays;
import java.util.List;

import com.avaje.ebean.Ebean;

import models.Document;
import models.User;
import models.WatchedTarget;
import play.Logger;
import play.mvc.Result;
import play.mvc.Security;
import uk.bl.api.CrawlData;
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
    	
    	/*List<WatchedTarget> watchedTargetsTestData = Arrays.asList(
    			new WatchedTarget(user, "http://www.ifs.org.uk/publications/re", false),
    			new WatchedTarget(user, "http://www.thinknpc.org/publications/", true),
    			new WatchedTarget(user, "http://www.ofsted.gov.uk/resources/surveys", false),
    			new WatchedTarget(user, "http://www.parliament.uk/business/committees/committees-a-z/commons-select/home-affairs-committee/publications/", false),
    			new WatchedTarget(user, "https://www.gov.uk/government/publications", false)
    			);
    	Ebean.save(watchedTargetsTestData);*/
    	
    	List<WatchedTarget> watchedTargets = WatchedTarget.find.where().eq("id_creator", user.uid).findList();
    	
        return ok(
        	list.render(
        			user,
        			watchedTargets
        			)
        	);
    }
    
    public static Result crawl(Long id) {
    	WatchedTarget watchedTarget = WatchedTarget.find.byId(id);
    	List<Document> documentList = CrawlData.crawlForDocuments(watchedTarget);
    	Ebean.save(documentList);
    	return redirect(routes.Documents.list(id, 0, "title", "asc", ""));
    }

}
