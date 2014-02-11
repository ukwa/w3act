package controllers;

import play.*;
import play.mvc.*;
import play.data.*;
import static play.data.Form.*;

import java.util.*;

import models.*;
import uk.bl.Const;
import views.html.*;

/**
 * Manage targets.
 */
@Security.Authenticated(Secured.class)
public class UrlSearch extends AbstractController {
  
    /**
     * Display the targets.
     */
    public static Result index() {
        return ok(
            urlsearch.render(
                "UrlSearch", User.find.byId(request().username()), models.Target.findInvolving(), User.findAll()
            )
        );
    }

    public static Result filterUrl() {
        String url = getFormParam(Const.URL);
        return ok(
            urlsearch.render(
                "UrlSearch", User.find.byId(request().username()), models.Target.filterUrl(url), User.findAll()
            )
        );
    }
	
    public static Result addNewTarget() {
        return ok(
            urlsearch.render(
                    "AddTarget", User.find.byId(request().username()), models.Target.findInvolving(), User.findAll()
            )
        );
    }
	
}

