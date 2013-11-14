package controllers;

import java.util.UUID;

import com.avaje.ebean.Ebean;

import models.Organisation;
import models.Target;
import models.User;
import play.Logger;
import play.mvc.Result;
import play.mvc.Security;
import uk.bl.Const;
import views.html.*;

/**
 * Look up the URL of interest. This gives a status check, and allows the user to decide 
 * whether they need to create an entry for this item. This page allows the user to look 
 * up a URL in a way that is compatible with a bookmarklet or other RESTful services.
 *
 * From here, they can find out:
 *
 *   If the URI, or a closely related URI (e.g. same domain), already has an entry in ACT.
 *   If the URI has been crawled (hook into Monitrix).
 *   If the URI is available from any of the Wayback instances (i.e. hooks to Wayback API).
 *   If the URI falls in scope for crawling, or whether additional permissions will be needed.
 *   If the URI is held in other archives, a la http://www.webarchive.org.uk/mementos/search/http://www.bbc.co.uk/news/
 *
 */
@Security.Authenticated(Secured.class)
public class LookUp extends AbstractController {
  
    /**
     * Display the targets.
     */
    public static Result index() {
        return ok(
            lookup.render(
                "LookUp", User.find.byId(request().username()), models.Target.findInvolving(), User.findAll(), ""
            )
        );
    }

    /**
     * This method enables searching for given URL and redirection in order to add new entry
     * if required.
     * @return
     */
    public static Result filterUrl() {
    	Result res = null;
        String addentry = getFormParam("addentry");
        String search = getFormParam("search");
        String url = getFormParam(Const.URL);
//        Logger.info("addentry: " + addentry + ", search: " + search + ", url: " + url);
        if (addentry != null) {
//	        res = redirect(routes.WebSite.addEntry(url)); 
//        	Target target = new Target();
//        	target.title = url;
//            UUID id = UUID.randomUUID();
//            Logger.info("id: " + id.toString());
//            target.nid = id.getMostSignificantBits();
//            target.url = Const.ACT_URL + target.nid;
//	        res = redirect(routes.TargetEdit.addEntry(target)); 
	        res = redirect(routes.TargetEdit.addEntry(url));
        } else {
            res = ok(
	            lookup.render(
	                "LookUp", User.find.byId(request().username()), models.Target.filterUrl(url), User.findAll(), url
                )
            );
        }
        return res;
    }
	
//    public static Result addNewTarget() {
//        return ok(
//            lookup.render(
//                    "AddTarget", User.find.byId(request().username()), models.Target.findInvolving(), User.findAll()
//            )
//        );
//    }
	
}

