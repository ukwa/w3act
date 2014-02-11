package controllers;

import models.Taxonomy;
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
public class TaxonomyEdit extends Controller {
  
    /**
     * Display the taxonomy.
     */
    public static Result index() {
        return ok(
        );
    }

    /**
     * Display the taxonomy edit panel for this URL.
     */
    public static Result edit(String url) {
		Logger.info("taxonomy url: " + url);
		Taxonomy taxonomy = Taxonomy.findByUrl(url);
		Logger.info("taxonomy name: " + taxonomy.name + ", url: " + url);
        return ok(
                taxonomyedit.render(
                        Taxonomy.findByUrl(url), User.find.byId(request().username())
                )
            );
    }
    
    public static Result view(String url) {
        return ok(
                taxonomyview.render(
                        Taxonomy.findByUrl(url), User.find.byId(request().username())
                )
            );
    }
    
}

