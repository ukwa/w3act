package controllers;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import models.FastSubject;
import models.User;
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
		try {
			ProcessBuilder builder = new ProcessBuilder("/bin/bash", "-c", "pdf2htmlEX -v");
			Process p = builder.start();
			if (p.waitFor() == 0) {
				return true;
			}
		} catch (Exception e) {}
		return false;
	}
	
	public static boolean isSsdeepAvailable() {
		try {
			ProcessBuilder builder = new ProcessBuilder("/bin/bash", "-c", "ssdeep -V");
			Process p = builder.start();
			if (p.waitFor() == 0) {
				return true;
			}
		} catch (Exception e) {}
		return false;
	}
	
	public static boolean isWaybackResponding() {
		return isSiteResponding(WaybackController.getWaybackEndpoint());
	}
	
	public static boolean isPiiResponding() {
		
		String arkRequest = Play.application().configuration().getString("pii_url");
		if(arkRequest != null){
		String statusRequest = arkRequest.substring(0, arkRequest.lastIndexOf('/') + 1) + "status";
		return isSiteResponding(statusRequest);
		}else
			return false;
	}
	
	public static boolean isSiteResponding(String url) {
		try {
			URL obj = new URL(url);
			HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
			connection.setRequestMethod("HEAD");
			return connection.getResponseCode() == 200;
		} catch (IOException e) {
			return false;
		}
	}
	
	public static String secretServerVersion() {
		try {
			return PasswordManager.versionGet();
		} catch (Exception e) {
			return null;
		}
	}
}

