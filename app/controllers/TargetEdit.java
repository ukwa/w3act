package controllers;

import models.Target;
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
public class TargetEdit extends Controller {
  
    /**
     * Display the target.
     */
    public static Result index() {
        return ok(
        );
    }

    /**
     * Display the target edit panel for this URL.
     */
    public static Result edit(String url) {
		Logger.info("target url: " + url);
		Target target = Target.findByUrl(url);
		Logger.info("target name: " + target.title + ", url: " + url);
        return ok(
                targetedit.render(
                        Target.findByUrl(url), User.find.byId(request().username())
                )
            );
    }
    
    public static Result view(String url) {
        return ok(
                targetview.render(
                        Target.findByUrl(url), User.find.byId(request().username())
                )
            );
    }
    
}

