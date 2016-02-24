package controllers;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import models.FieldUrl;
import models.Target;
import models.User;
import play.Logger;
import play.Play;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.html.monitrix.frequent;

@Security.Authenticated(SecuredController.class)
public class MonitrixController extends Controller {
	
	public static String getEndpoint() {
		return Play.application().configuration().getString("application.monitrix.url");
	}
	
    public static Result all() {
    	User user = User.findByEmail(request().username());
    	return ok(frequent.render(null, user, "*", getEndpoint()));
    }
    
    private static String escape(String in) {
    	Logger.debug("in: "+in);
    	String n = in.replaceAll("([\\:\\/])","\\\\$1");
    	Logger.debug("out: "+n);
    	return n;
    }
    
    public static Result forTarget(Long id) throws UnsupportedEncodingException {
    	Target target = Target.findById(id);
    	if( target != null) {
    		User user = User.findByEmail(request().username());
    		String query="";
    		for( FieldUrl furl : target.fieldUrls) {
    			if( query.length() > 0 ) {
    				query = query + " OR ";
    			}
    			// For precise match:
    			//query = query + "downloaded_uri.raw:\""+furl.url+"\"";
    			// For prefix:
    			query = query + "downloaded_uri.raw:/"+escape(furl.url)+".*/";
    		}
    		Logger.debug("query: "+query);
    		return ok(frequent.render(target, user, URLEncoder.encode(query,"UTF-8").replaceFirst(":", "%3D"), getEndpoint()));
    	} else {
    		return notFound("There is no Target with ID "+id);
    	}
    }
    
}

