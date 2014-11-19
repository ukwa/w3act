package controllers;

import java.io.BufferedReader;
import java.io.FileReader;

import models.User;
import play.Logger;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import uk.bl.Const;
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
    	User user = User.findByEmail(request().username());
    	if (user != null) {
    		return ok(about.render("About", user));
    		
    	}
        return redirect("login");
    }
	
    /**
     * This method retrieves the last commit hash from Github.
     * @return last commit hash value
     */
    public static String getVersion() {
    	String res = "";
    	try {
    		BufferedReader br = new BufferedReader(new FileReader(Const.VERSION_FILE));
    		try {
    			StringBuilder sb = new StringBuilder();
    			String line = br.readLine();

    			while (line != null) {
    				sb.append(line);
    				sb.append('\n');
    				line = br.readLine();
    			}
    			res = sb.toString();
    		} finally {
    			br.close();
    		}
//    		row = Utils.buildWebRequestByUrl(Const.GITHUB, Const.LAST_COMMIT);
//        	Logger.info("row: " + row);
//	    	if (row != null && row.length() > 0) {
//		    	int start = row.indexOf(Const.LAST_COMMIT) + Const.LAST_COMMIT.length();
//		    	row = row.substring(start, start + 40);
//	    	}
    	} catch (Exception e) {
    		Logger.debug("Error occured by last commit hash calculation: " + e);
    	}
    	Logger.info("last commit hash: " + res);

    	return res;
    }
    
}

