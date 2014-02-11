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
public class WebSites extends AbstractController {
  
    /**
     * Display the targets.
     */
    public static Result index() {
        return ok(
            websites.render(
                "WebSites", User.find.byId(request().username()), models.Target.findInvolving(), User.findAll()
            )
        );
    }

    /**
     * This method is used for filtering where "url" is an input field from form.
     * @return
     */
    public static Result filterUrl() {
        String url = getFormParam(Const.URL);
        return ok(
                websites.render(
                    "WebSites", User.find.byId(request().username()), models.Target.filterUrl(url), User.findAll()
                )
            );
    }
	
}

