package controllers;

import models.Target;
import models.User;
import play.Logger;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.html.targets.*;

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
		Logger.info("TargetEdit.edit() url: " + url);
		Target target = Target.findByUrl(url);
		Logger.info("TargetEdit.edit() target name: " + target.title + ", url: " + url + ", username: " + request().username());
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
    
    /**
     * This method shows selected revision of a Target by given ID.
     * @param nid
     * @return
     */
    public static Result viewrevision(Long nid) {
        return ok(
                targetview.render(
                        Target.findById(nid), User.find.byId(request().username())
                )
            );
    }
    
}

