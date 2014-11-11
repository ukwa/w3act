package controllers;

import play.mvc.*;

import models.*;

import views.html.*;

/**
 * Describe W3ACT project.
 */
@Security.Authenticated(Secured.class)
public class FindContent extends Controller {
  
    /**
     * Display the find content page.
     */
    public static Result index() {
		return ok(
            findcontent.render("FindContent", User.findByEmail(request().username()))
        );
    }
	
}

