package controllers;

import models.Target;
import models.User;
import play.Logger;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import uk.bl.Const;
import views.html.targetedit;
import views.html.targetview;

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
//		Logger.info("TargetEdit.edit() url: " + url);
		Target target = Target.findByUrl(url);
		Logger.info("TargetEdit.edit() target name: " + target.title + ", url: " + url);
        return ok(
                targetedit.render(
                        Target.findByUrl(url), User.find.byId(request().username())
                )
            );
    }
    
    /**
     * Add new entry.
     * @param target
     * @return
     */
    public static Result addEntry(String targettitle) {
//        public static Result addEntry(Target target) {
    	Target target = new Target();
    	target.title = targettitle;
        target.nid = Target.createId();
        target.url = Const.ACT_URL + target.nid;
        target.revision = Const.INITIAL_REVISION;
        target.active = true;
		Logger.info("add entry with target url: " + target.url);
		Logger.info("target name: " + target.title);
        return ok(
                targetedit.render(
                        target, User.find.byId(request().username())
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

