package controllers;

import play.*;
import play.mvc.*;
import play.data.*;
import static play.data.Form.*;

import java.util.*;

import models.*;

import views.html.*;

/**
 * Manage organisations.
 */
@Security.Authenticated(Secured.class)
public class Organisations extends Controller {
  
    /**
     * Display the organisations.
     */
    public static Result index() {
        return ok(
            organisations.render(
                "Organisations", User.find.byId(request().username()), models.Organisation.findInvolving()
            )
        );
    }

    // -- Organisations

    /**
     * Add a organisation.
     */
    public static Result add() {
        Organisation newOrganisation = Organisation.create(
            "New organisation"
        );
        return ok();
    }
    
    /**
     * Rename a organisation.
     */
    public static Result rename(Long organisation) {
        return ok(
            Organisation.rename(
                organisation, 
                form().bindFromRequest().get("title")
            )
        );
    }
    
    /**
     * Delete a organisation.
     */
    public static Result delete(Long organisation) {
        Organisation.find.ref(organisation).delete();
        return ok();
    }

}

