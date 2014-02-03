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
import views.html.targets.*;

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
        return GO_HOME;
    }

    public static Result GO_HOME = redirect(
        routes.Targets.list(0, "title", "asc", "")
    );
}

