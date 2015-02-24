package controllers;

import com.ning.http.client.Realm.AuthScheme;

import play.Logger;
import play.Play;
import play.libs.WS;
import play.libs.F.Function;
import play.libs.F.Promise;
import play.libs.WS.Response;
import play.libs.WS.WSRequestHolder;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;

@Security.Authenticated(SecuredController.class)
public class WaybackController extends Controller {

    public static Promise<Result> wayback(String url) {
    	String wayBackUrl = Play.application().configuration().getString("application.wayback.url");
    	final String wayback = wayBackUrl + "/" + url;
    	
    	WSRequestHolder holder = WS.url(wayback);
//    	holder.setAuth("kinman.li@bl.uk", "password", AuthScheme.BASIC);
    	// pass on credentials
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
}
