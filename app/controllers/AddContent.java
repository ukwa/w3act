package controllers;

import play.mvc.*;

import models.*;

import views.html.*;

/**
 * Describe W3ACT project.
 */
@Security.Authenticated(Secured.class)
public class AddContent extends Controller {
  
    /**
     * Display the add content page.
     */
    public static Result index() {
		return ok(
            addcontent.render("AddContent", User.findByEmail(request().username()))
        );
    }
	
}

