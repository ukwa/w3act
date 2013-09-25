package controllers;

import play.*;
import play.mvc.*;
import play.data.*;
import static play.data.Form.*;

import java.util.*;

import models.*;

import views.html.*;

/**
 * Manage dcollections.
 */
@Security.Authenticated(Secured.class)
public class DCollections extends Controller {
  
    /**
     * Display the dcollections.
     */
    public static Result index() {
        return ok(
            dcollections.render(
                "Collections", User.find.byId(request().username()), models.DCollection.findInvolving()
            )
        );
    }

    // -- DCollections

    /**
     * Add a dcollection.
     */
    public static Result add() {
        DCollection newDCollection = DCollection.create(
            "New dcollection"
        );
        return ok();
    }
    
    /**
     * Rename a dcollection.
     */
    public static Result rename(Long dcollection) {
        return ok(
            DCollection.rename(
                dcollection, 
                form().bindFromRequest().get("title")
            )
        );
    }
    
    /**
     * Delete a dcollection.
     */
    public static Result delete(Long dcollection) {
        DCollection.find.ref(dcollection).delete();
        return ok();
    }

}

