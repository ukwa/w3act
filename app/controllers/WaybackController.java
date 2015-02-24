package controllers;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import org.apache.commons.lang3.StringUtils;

import models.User;

import com.ning.http.client.Realm.AuthScheme;

import play.Logger;
import play.Play;
import play.libs.F;
import play.libs.WS;
import play.libs.F.Function;
import play.libs.F.Promise;
import play.libs.WS.Response;
import play.libs.WS.WSRequestHolder;
import play.mvc.Controller;
import play.mvc.Http.Cookie;
import play.mvc.Result;
import play.mvc.Security;
import uk.bl.api.PasswordHash;
import uk.bl.exception.ActException;

public class WaybackController extends Controller {

    public static Promise<Result> wayback(String url) throws ActException {
    	String wayBackUrl = Play.application().configuration().getString("application.wayback.url");
    	final String wayback = wayBackUrl + "/" + url;
    	
    	String username = session("email");
    	String password = session("password");
    	
    	Logger.debug("username: " + username);
    	Logger.debug("password: " + password);
	    
    	String flashMessage = "";
    	if (StringUtils.isNotEmpty(username) && StringUtils.isNotEmpty(password)) {
			User user = User.findByEmail(username.toLowerCase());
			if (user == null) {
				flashMessage = "User not found " + username;
				return redirectToLogin(flashMessage);
			}
			if (user.roles != null && !user.roles.isEmpty() && user.hasRole("closed")) {
				flashMessage = "This user account has been closed. Please contact the British Library web archiving team " + username;
				return redirectToLogin(flashMessage);
			}
			String userPassword = User.findByEmail(username.toLowerCase()).password;
    		try {
				boolean exists = PasswordHash.validatePassword(password, userPassword);
				if (exists) {
			    	WSRequestHolder holder = WS.url(wayback);
		
			    	Promise<Response> responsePromise = holder.setFollowRedirects(true).get();
			    	
			        final Promise<Result> resultPromise = responsePromise.map(
			        		
		                new Function<WS.Response, Result>() {
		                	
		                    public Result apply(WS.Response response) {
		
		//http://crawler03.bl.uk:8080/wayback/20140611000704js_/http://bl.uk/scripts/jquery-1.5.1.min.js OK 200 https://www.webarchive.org.uk/act/login
		
		                    	Logger.debug(wayback + " (" + response.getStatusText() + " " + response.getStatus() + ") " + response.getUri());
		                    	
		                    	String contentType = response.getHeader(CONTENT_TYPE);
		                    	Logger.debug("content type: " + contentType);
		                        return ok(response.getBody()).as(contentType);
		                    }
		                    
		                }
			        );
			        return resultPromise;
				}
			} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
				flashMessage = e.getMessage();
				redirectToLogin(flashMessage);
			}

    	}
		flashMessage = "Please login";
		return redirectToLogin(flashMessage);
    }
    
    private static Promise<Result> redirectToLogin(final String message) {
        return F.Promise.promise(new F.Function0<Result>() {
            public Result apply() {
                flash("success", message);
                return redirect(routes.ApplicationController.index());
            }
        });
    }
    
}
