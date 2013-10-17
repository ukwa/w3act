package controllers;

import play.*;
import play.mvc.*;
import play.data.*;
import static play.data.Form.*;

import java.util.*;

import models.*;

import views.html.*;

/**
 * Manage curators.
 */
@Security.Authenticated(Secured.class)
public class Curators extends Controller {
  
    /**
     * Display the Curators.
     */
    public static Result index() {
        return ok(
            curators.render(
                "Curators", User.find.byId(request().username()), models.User.findAll()
            )
        );
    }

}

