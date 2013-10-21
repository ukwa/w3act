package controllers;

import models.Organisation;
import models.User;
import play.Logger;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.html.*;

/**
 * Manage targets.
 */
@Security.Authenticated(Secured.class)
public class OrganisationEdit extends Controller {
  
    /**
     * Display the organisation.
     */
    public static Result index() {
        return ok(
        );
    }

    /**
     * Display the organisation edit panel for this URL.
     */
    public static Result edit(String url) {
		Logger.info("organisation url: " + url);
		Organisation organisation = Organisation.findByUrl(url);
		Logger.info("organisation title: " + organisation.title + ", url: " + url);
        return ok(
                organisationedit.render(
                        Organisation.findByUrl(url), User.find.byId(request().username())
                )
            );
    }
    
    public static Result view(String url) {
        return ok(
                organisationview.render(
                        Organisation.findByUrl(url), User.find.byId(request().username())
                )
            );
    }
    
}

