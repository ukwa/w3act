package controllers;

import com.avaje.ebean.Ebean;
import com.thesecretserver.LoginCredentials;
import com.thesecretserver.PasswordManager;

import models.FlashMessage;
import models.User;
import models.WatchedTarget;
import play.Logger;
import play.Play;
import play.data.Form;
import play.libs.Json;
import play.mvc.Result;
import play.mvc.Security;
import views.html.logincredentials.edit;

@Security.Authenticated(SecuredController.class)
public class LoginCredentialsController extends AbstractController {
	
	private static PasswordManager passwordManager;
	
	static {
		String secretServerUser = Play.application().configuration().getString("secret_server_user");
		String secretServerPassword = Play.application().configuration().getString("secret_server_password");
		passwordManager = new PasswordManager(secretServerUser, secretServerPassword, "", "AD");
	}
	
	public static Result edit(Long id) {
		WatchedTarget watchedTarget = WatchedTarget.find.byId(id);
		Form<WatchedTarget> watchedTargetForm = Form.form(WatchedTarget.class).fill(watchedTarget);
		
		return ok(edit.render(watchedTargetForm,
				User.findByEmail(request().username())));
	}
	
	public static Result save(Long id) {
		Form<WatchedTarget> watchedTargetForm = Form.form(WatchedTarget.class).bindFromRequest();
		WatchedTarget watchedTarget = watchedTargetForm.get();
		String username = watchedTargetForm.field("username").value();
		String password = watchedTargetForm.field("password").value();
		if(!watchedTarget.loginPageUrl.isEmpty() && !username.isEmpty() && !password.isEmpty()) {
			LoginCredentials loginCredentials = new LoginCredentials(watchedTarget.loginPageUrl, username, password);
			try {
				watchedTarget.secretId = passwordManager.addLoginCredentials(WatchedTarget.find.byId(id).target.title, loginCredentials);
				new FlashMessage(FlashMessage.Type.SUCCESS, "The credentials were successfully saved to the Secret Server.").send();
			} catch(Exception e) {
				String msg = "Can't store username and password because there is no connection to the Secret Server.";
				new FlashMessage(FlashMessage.Type.ERROR, msg).send();
				Logger.error(msg, e);
			}
		} else if (username.isEmpty() && password.isEmpty()) {
			FlashMessage.updateSuccess.send();
		}
		Ebean.update(watchedTarget);
		return redirect(routes.LoginCredentialsController.edit(id));
	}
	
	public static Result getSecret(Integer id) {
		try {
			LoginCredentials loginCredentials = passwordManager.getLoginCredentials(id);
			return ok(Json.toJson(loginCredentials));
		} catch (Exception e) {
			Logger.error("can't get secret", e);
			return internalServerError(e.getMessage());
		}
	}
}
