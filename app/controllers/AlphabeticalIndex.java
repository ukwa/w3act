package controllers;

import play.mvc.*;


import models.*;
import uk.bl.Const;
import views.html.*;

/**
 * Manage targets.
 */
@Security.Authenticated(Secured.class)
public class AlphabeticalIndex extends AbstractController {
  
    /**
     * Display the targets.
     */
    public static Result index() {
        return ok(
            alphabeticalindex.render(
                "AlphabeticalIndex", User.find.byId(request().username()), models.Target.findInvolving(), User.findAll()
            )
        );
    }

    public static Result filterUrl() {
        String url = getFormParam(Const.URL);
        return ok(
                alphabeticalindex.render(
                    "AlphabeticalIndex", User.find.byId(request().username()), models.Target.filterUrl(url), User.findAll()
                )
            );
    }

}

