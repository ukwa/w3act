package controllers;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import models.FastSubject;
import models.User;
import models.Document;
import play.Logger;
import play.Play;
import play.mvc.Result;
import play.mvc.Security;
import uk.bl.db.DataImport;
import views.html.status.status;

import com.thesecretserver.PasswordManager;

@Security.Authenticated(SecuredController.class)
public class StatusController extends AbstractController {
	
	public static Result status() {
		return ok(status.render(User.findByEmail(request().username())));
	}
	
	public static Result addFastDefaultSubjects() {
		DataImport.importFastSubjects();
		return redirect(routes.StatusController.status());
	}
	
	public static boolean areThereFastSubjects() {
		return FastSubject.find.findRowCount() > 0;
	}
	
	public static boolean isPdf2htmlEXAvailable() {
		return isSiteResponding(getPdf2HtmlEndpoint());
	}
	
	public static boolean isWaybackResponding() {
		return isSiteResponding(getWaybackEndpoint());
	}
	
	public static boolean isPiiResponding() {
		return isSiteResponding(getPIIEndpoint());
	}
	
	public static boolean isOfficalPiiInstance() {
		String arkRequest = getPIIEndpoint();
		if(arkRequest != null){
		String statusRequest = arkRequest.substring(0, arkRequest.lastIndexOf('/') + 1) + "status";
		return isSiteResponding(statusRequest);
		}else
			return false;
	}
	
	public static String secretServerVersion() {
		try {
			return PasswordManager.versionGet();
		} catch (Exception e) {
			return null;
		}
	}
	
	public static String getWaybackEndpoint() {
		return WaybackController.getWaybackEndpoint();
	}
	
	public static String getPIIEndpoint() {
		return Play.application().configuration().getString("pii_url");
	}
	
	public static String getSecretServerEndpoint() {
		URL url = PasswordManager.getSecretServerEndpoint();
		if( url == null ) return "";
		return url.toString();
	}
	
	public static String getPdf2HtmlEndpoint() {
		String url = Document.getPdf2HtmlEndpoint();
		url = url.replace("convert?url=", "");
		return url;
	}

	private static boolean isSiteResponding(String url) {
		try {
			URL obj = new URL(url);
			HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
			connection.setRequestMethod("HEAD");
			return connection.getResponseCode() == 200;
		} catch (IOException e) {
			return false;
		}
	}
	
}

