package controllers;

import models.FastSubject;
import models.User;
import play.mvc.Result;
import play.mvc.Security;
import views.html.status.status;

import com.thesecretserver.PasswordManager;

@Security.Authenticated(SecuredController.class)
public class StatusController extends AbstractController {
	
	public static Result status() {
		return ok(status.render(User.findByEmail(request().username())));
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
	
	public static String secretServerVersion() {
		try {
			return PasswordManager.versionGet();
		} catch (Exception e) {
			return null;
		}
	}
}

