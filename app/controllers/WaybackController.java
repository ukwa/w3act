package controllers;

import play.Logger;
import play.Play;
import play.libs.WS;
import play.libs.F.Function;
import play.libs.F.Promise;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;

@Security.Authenticated(SecuredController.class)
public class WaybackController extends Controller {

    public static Promise<Result> wayback(String url) {
    	String wayBackUrl = Play.application().configuration().getString("application.wayback.url");
    	final String wayback = wayBackUrl + "/*/" + url;
    	Logger.debug(wayback);
    	
        final Promise<Result> resultPromise = WS.url(wayback).get().map(
                new Function<WS.Response, Result>() {
                    public Result apply(WS.Response response) {
                        return ok(response.getBody()).as("text/html");
                    }
                }
        );
        return resultPromise;    	
    }
}
