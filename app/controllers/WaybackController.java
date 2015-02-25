package controllers;

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
import uk.bl.exception.ActException;

public class WaybackController extends Controller {

	@Security.Authenticated(SecuredController.class)
	public static Promise<Result> wayback(String url) throws ActException {
		String wayBackUrl = Play.application().configuration().getString("application.wayback.url");
		final String wayback = wayBackUrl + "/" + url;

		WSRequestHolder holder = WS.url(wayback).setFollowRedirects(false);

		Promise<Response> responsePromise = holder.get();

		final Promise<Result> resultPromise = responsePromise.map(

				new Function<WS.Response, Result>() {

					public Result apply(WS.Response response) {

						Logger.debug(wayback + " (" + response.getStatusText() + " " + response.getStatus() + ") " + response.getUri());

						String contentType = response.getHeader(CONTENT_TYPE);
						Logger.debug("content type: " + contentType);
						// TODO Copy all headers over?
						if ( response.getHeader(LOCATION) != null ) {
							Logger.debug("Copying over Location header: "+response.getHeader(LOCATION));
							response().setHeader(LOCATION, response.getHeader(LOCATION));
						}
						return status(response.getStatus(), response.getBody()).as(contentType);
					}

				}
				);
		return resultPromise;
	}

}
