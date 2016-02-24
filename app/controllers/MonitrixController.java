package controllers;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import models.User;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import play.Logger;
import play.Play;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import uk.bl.exception.ActException;

public class MonitrixController extends Controller {
	
	public static String getEndpoint() {
		return Play.application().configuration().getString("application.monitrix.url");
	}
	
	@Security.Authenticated(SecuredController.class)
	public static Result proxy(String url) throws ActException, ClientProtocolException, IOException {
		
		String wayBackUrl = getEndpoint();
		
		// Build up the wayback query:
		String waybackBuilder = wayBackUrl;
		if( StringUtils.isNotEmpty(url) ) {
			waybackBuilder = waybackBuilder + "/" + url;
		}
		String q = ctx()._requestHeader().rawQueryString();
		if( q != null && q.length() > 0 ) {
			Logger.info("Passing through raw Query String: "+q);
			waybackBuilder += "?"+q;
		}
		final String wayback = waybackBuilder;
		Logger.info("Using URL: "+wayback);

		// Build up URL and copy over query parameters:		
		CloseableHttpClient httpclient = HttpClientBuilder.create()
			    .disableRedirectHandling()
			    .build();
		//
		HttpGet httpGet = new HttpGet(wayback);
		CloseableHttpResponse response = httpclient.execute(httpGet);
		// If this looks like a redirect, return that:
		if ( response.getFirstHeader(LOCATION) != null ) {
			String location = response.getFirstHeader(LOCATION).getValue();
			response.close();
			Logger.info("Got LOCATION: "+location);
			// Issue the redirect directly...
			return redirect(location);
		}
		// Otherwise, return the body, copying over the headers:
		// Except this does not work, because doing this here overrides/breaks the Play frameworks response handling.
		//for( Header h : response.getAllHeaders() ) {
		//	response().setHeader(h.getName(), h.getValue());
		//}
		String contentType = response.getFirstHeader(CONTENT_TYPE).getValue();
		Logger.debug("Response content type: " + contentType);
		HttpEntity entity = response.getEntity();
		return status(response.getStatusLine().getStatusCode(), entity.getContent()).as(contentType);
	}
	
	public static Result root() throws ActException, ClientProtocolException, IOException {
		return proxy("");
	}

}

