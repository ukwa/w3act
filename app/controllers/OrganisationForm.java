package controllers;

import play.mvc.*;

import models.*;

import views.html.organisations.*;

/**
 * Describe W3ACT project.
 */
@Security.Authenticated(Secured.class)
public class OrganisationForm extends Controller {
  
    /**
     * Display the About tab.
     */
    public static Result index() {
		return ok(
            organisationform.render("OrganisationForm", User.find.byId(request().username()))
        );
    }
	
}

