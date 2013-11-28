package controllers;

import models.DCollection;
import models.User;
import play.Logger;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.html.*;

/**
 * Manage collections.
 */
@Security.Authenticated(Secured.class)
public class CollectionEdit extends Controller {
  
    /**
     * Display the collection.
     */
    public static Result index() {
        return ok(
        );
    }

    /**
     * Display the collection edit panel for this URL.
     */
    public static Result edit(String url) {
		Logger.info("collection url: " + url);
		DCollection collection = DCollection.findByUrl(url);
		Logger.info("collection title: " + collection.title + ", url: " + url);
        return ok(
                collectionedit.render(
                        DCollection.findByUrl(url), User.find.byId(request().username())
                )
            );
    }
    
    public static Result view(String url) {
        return ok(
                collectionview.render(
                        DCollection.findByUrl(url), User.find.byId(request().username())
                )
            );
    }
    
}

