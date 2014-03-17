package controllers;

import models.User;
import play.Logger;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import uk.bl.Const;
import uk.bl.api.Utils;
import views.html.about;

/**
 * Describe W3ACT project.
 */
@Security.Authenticated(Secured.class)
public class About extends Controller {
  
    /**
     * Display the About tab.
     */
    public static Result index() {
		return ok(
            about.render("About", User.find.byId(request().username()))
        );
    }
	
    /**
     * This method retrieves the last commit hash from Github.
     * @return last commit hash value
     */
    public static String getLastCommitHash() {
    	String row = "";
    	try {
    		row = Utils.buildWebRequestByUrl(Const.GITHUB, Const.LAST_COMMIT);
        	Logger.info("row: " + row);
	    	if (row != null && row.length() > 0) {
		    	int start = row.indexOf(Const.LAST_COMMIT) + Const.LAST_COMMIT.length();
		    	row = row.substring(start, start + 40);
	    	}
    	} catch (Exception e) {
    		Logger.debug("Error occured by last commit hash calculation: " + e);
    	}
    	Logger.info("last commit hash: " + row);
    	return row;
    }
    
}

