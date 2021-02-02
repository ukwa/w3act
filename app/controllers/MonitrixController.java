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
	
    public static String getURLPrefixQuery(String url) {
        return "?url_filter=" + escapeParam(url) + "*";
    }
    
    public static String getURLQuery(String url) {
        return "?url_filter=" + escapeParam(url) + "";
    }
    
    public static Result all() {
    	User user = User.findByEmail(request().username());
    	return ok(frequent.render(null, user, "*", getEndpoint()));
    }
    
    public static enum Query {
    	EXACT,
    	PREFIX
    };
    
    private static String escapeParam(String raw) {
        try {
            return URLEncoder.encode(raw, "UTF-8").replaceAll("\\+", "%20");
        } catch (UnsupportedEncodingException e) {
            return raw;
        }
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
    			query = query + getURLQuery(furl.url);
    		}
    		Logger.debug("query: "+query);
    		return ok(frequent.render(target, user, URLEncoder.encode(query,"UTF-8").replaceFirst(":", "%3D"), getEndpoint()));
    	} else {
    		return notFound("There is no Target with ID "+id);
    	}
    }
    
}

