/**
 * 
 */
package controllers;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import play.Logger;
import play.Play;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import play.utils.UriEncoding;

/**
 * @author andy
 *
 */
@Security.Authenticated(SecuredController.class)
public class WSProxy extends Controller {
	
	// Build up URL and copy over query parameters:		
	public static Result passthrough(String url) throws ClientProtocolException, IOException {
		final String encoding = Play.application().configuration().getString("application.web_encoding", StandardCharsets.UTF_8.name());
		final String allowedUrlPattern = Play.application().configuration().getString("wsproxy.allowedUrlPattern", ".*");

		// Build up the wayback query:
		String urlBuilder = UriEncoding.decodePath(url, encoding);
		String q = ctx()._requestHeader().rawQueryString();
		if( q != null && q.length() > 0 ) {
			Logger.info("Passing through raw Query String: "+q);
			urlBuilder += "?"+q;
		}
		final String nurl = urlBuilder;		
		Logger.info("Proxying "+nurl);

		// Ensure that the proxied web address is allowed
		final Matcher allowedUrlMatcher = Pattern.compile(allowedUrlPattern).matcher(nurl);

		if(allowedUrlMatcher.matches()) {
			// Set up the GET:
			CloseableHttpClient httpclient = HttpClientBuilder.create()
					.disableRedirectHandling()
					.build();
			HttpGet httpGet = new HttpGet(nurl);

			CloseableHttpResponse response = null;

			try {
				response = httpclient.execute(httpGet);
			}
			catch(IOException e) {
				Logger.error("Error reading response", e);
				throw e;
			}

			// If this looks like a redirect, return that:
			if(response.getFirstHeader(LOCATION) != null) {
				String location = response.getFirstHeader(LOCATION).getValue();
				response.close();
				Logger.info("Got LOCATION: " + location);
				// Issue the redirect directly...
				return redirect(location);
			}

			// Pass the content type over:
			String contentType = response.getFirstHeader(CONTENT_TYPE).getValue();
			Logger.debug("Response content type: " + contentType);
			HttpEntity entity = response.getEntity();
			return status(response.getStatusLine().getStatusCode(), entity.getContent()).as(contentType);
		}
		else {
			Logger.info(String.format("Proxying URL '%s' is not permitted by the current application configuration", nurl));
			return status(FORBIDDEN);
		}
	}
	
}
