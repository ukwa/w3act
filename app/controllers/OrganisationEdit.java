package controllers;

import models.Organisation;
import models.User;
import play.mvc.Result;
import play.mvc.Security;
import views.html.organisations.organisationadmin;
import views.html.organisations.organisationsites;

/**
 * Manage targets.
 */
@Security.Authenticated(Secured.class)
public class OrganisationEdit extends AbstractController {
  
    /**
     * Display the organisation.
     */
    public static Result index() {
        return ok(
        );
    }

    public static Result sites(String url) {
        return ok(
                organisationsites.render(
                        Organisation.findByUrl(url), User.find.byId(request().username())
                )
            );
    }
    
    /**
     * Administer users
     * @param url
     * @return
     */
    public static Result admin(String url) {
        return ok(
                organisationadmin.render(
                        Organisation.findByUrl(url), User.find.byId(request().username())
                )
            );
    }
    
}

