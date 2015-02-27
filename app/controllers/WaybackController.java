package controllers;

import play.Logger;
import play.Play;
import play.libs.ws.WS;
import play.libs.ws.WSResponse;
import play.libs.F.Function;
import play.libs.F.Promise;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;

@Security.Authenticated(SecuredController.class)
public class WaybackController extends Controller {

    public static Promise<Result> wayback(String url) {
    	String wayBackUrl = Play.application().configuration().getString("application.wayback.url");
    	final String wayback = wayBackUrl + "/" + url;
    	Logger.debug(wayback);
    	
        final Promise<Result> resultPromise = WS.url(wayback).get().map(
                new Function<WSResponse, Result>() {
                    public Result apply(WSResponse response) {
                    	String contentType = response.getHeader(CONTENT_TYPE);
                    	Logger.debug("content type: " + contentType);
                        return ok(response.getBody()).as(contentType);
                    }
                }
        );
        return resultPromise;    	
    }
}
