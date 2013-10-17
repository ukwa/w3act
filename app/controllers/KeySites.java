package controllers;

import play.*;
import play.mvc.*;
import play.data.*;
import static play.data.Form.*;

import java.util.*;

import models.*;
import uk.bl.Const;
import views.html.*;

/**
 * Manage targets.
 */
@Security.Authenticated(Secured.class)
public class KeySites extends AbstractController {
  
    /**
     * Display the targets.
     */
    public static Result index() {
        return ok(
            keysites.render(
                "KeySites", User.find.byId(request().username()), models.Target.findInvolving(), User.findAll()
            )
        );
    }

    public static Result filterUrl() {
    	Result res;
        String url = getFormParam(Const.URL);
//        Target.fi
//        target.summary = getFormParam(Const.SUMMARY);
//        UUID id = UUID.randomUUID();
//        Logger.info("id: " + id.toString());
//        target.nid = id.getMostSignificantBits();
//        String save = getFormParam("save");
//        String preview = getFormParam("preview");
////        Logger.info("save: " + save + ", preview: " + preview);
//        if (save != null) {
//	        target.save();
//	        Logger.info("add article: " + target.toString());
//	        res = redirect(routes.KeySites.index());
//        } else {
//        	previewObj = target;
//	    res = redirect(routes.KeySites.index());
//        }
        return ok(
                keysites.render(
                    "KeySites", User.find.byId(request().username()), models.Target.filterUrl(url), User.findAll()
                )
            );
//        return res;
    }
	
}

