/**
 * 
 */
package controllers;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import play.Logger;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;

/**
 * @author andy
 *
 */
@Security.Authenticated(SecuredController.class)
public class WSProxy extends Controller {
	
	// Build up URL and copy over query parameters:		
	public static Result passthrough(String url) throws ClientProtocolException, IOException {
		
		// Build up the wayback query:
		String urlBuilder = url;
		String q = ctx()._requestHeader().rawQueryString();
		if( q != null && q.length() > 0 ) {
			Logger.info("Passing through raw Query String: "+q);
			urlBuilder += "?"+q;
		}
		final String nurl = urlBuilder;		
		Logger.info("Proxing "+nurl);
		
		// Set up the GET:
		CloseableHttpClient httpclient = HttpClientBuilder.create()
			    .disableRedirectHandling()
			    .build();
		HttpGet httpGet = new HttpGet(nurl);
		CloseableHttpResponse response = httpclient.execute(httpGet);
		
		// If this looks like a redirect, return that:
		if ( response.getFirstHeader(LOCATION) != null ) {
			String location = response.getFirstHeader(LOCATION).getValue();
			response.close();
			Logger.info("Got LOCATION: "+location);
			// Issue the redirect directly...
			return redirect(location);
		}

		// Pass the content type over:
		String contentType = response.getFirstHeader(CONTENT_TYPE).getValue();
		Logger.debug("Response content type: " + contentType);
		HttpEntity entity = response.getEntity();
		return status(response.getStatusLine().getStatusCode(), entity.getContent()).as(contentType);
	}
	
}
