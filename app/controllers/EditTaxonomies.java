package controllers;

import play.*;
import play.mvc.*;
import play.data.*;
import static play.data.Form.*;

import java.util.*;

import models.*;

import views.html.*;

/**
 * Manage targets.
 */
@Security.Authenticated(Secured.class)
public class EditTaxonomies extends Controller {
  
    /**
     * Display the targets.
     */
    public static Result index() {
        return ok(
            taxonomies.render(
                "EditTaxonomies", User.find.byId(request().username()), models.Taxonomy.findInvolving(), User.findAll()
            )
        );
    }

    /**
     * Add a target.
     */
    public static Result add() {
        Target newTarget = Target.create(
            "New target", 
            "url"
        );
        return null;
    }
    
    /**
     * Rename a target.
     */
    public static Result rename(Long target) {
        return ok(
            Target.rename(
                target, 
                form().bindFromRequest().get("title")
            )
        );
    }
    
    /**
     * Delete a target.
     */
    public static Result delete(Long target) {
        Target.find.ref(target).delete();
        return ok();
    }
    
}

