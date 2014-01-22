package controllers;

import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;

import models.Target;
import models.User;
import play.Logger;
import play.libs.Json;
import play.mvc.BodyParser;
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
    	Logger.info("LookUp.index()");
        return ok(
            lookup.render(
                "LookUp", User.find.byId(request().username()), models.Target.findInvolving(), User.findAll(), ""
            )
        );
    }

    /**
     * Export selected targets to CSV file.
     */
//    public static Result export() {
//    	Logger.info("LookUp.export()");
////        String export = getFormParam(Const.EXPORT);
////        if (export != null) {
//       	return redirect(routes.Targets.index()); 
////        }
//    }
    
    /**
     * This method enables searching for given URL and redirection in order to add new entry
     * if required.
     * @return
     */
    public static Result filterUrl() {
    	Result res = null;
    	Logger.info("LookUp.filterUrl()");
        String addentry = getFormParam("addentry");
        String search = getFormParam("search");
        String url = getFormParam(Const.URL);
        Logger.info("addentry: " + addentry + ", search: " + search + ", url: " + url);
        if (addentry != null) {
        	if (url != null && url.length() > 0) {
        		res = redirect(routes.TargetEdit.addEntry(url));
        	} else {
        		Logger.info("Target title is empty. Please write title in search window.");
//                res = ok();
                res = ok(
        	            lookup.render(
        	                "LookUp", User.find.byId(request().username()), models.Target.filterUrl(url), User.findAll(), ""
                        )
                    );
        	}
        } else {
            res = ok(
	            lookup.render(
	                "LookUp", User.find.byId(request().username()), models.Target.filterUrl(url), User.findAll(), url
                )
            );
        }
        return res;
    }

    @BodyParser.Of(BodyParser.Json.class)
    public static Result filterByJson(String url) {
        JsonNode jsonData = null;
        if (url != null) {
	        List<Target> targets = Target.filterUrl(url);
	        jsonData = Json.toJson(targets);
        }
        return ok(jsonData);
    }
}

