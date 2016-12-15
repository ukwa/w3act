package controllers;

import com.avaje.ebean.Ebean;
import com.thesecretserver.LoginCredentials;
import com.thesecretserver.PasswordManager;

import models.FlashMessage;
import models.User;
import models.Target;
import play.Logger;
import play.Play;
import play.data.DynamicForm;
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
		Target target = Target.find.byId(id);
		Form<Target> TargetForm = Form.form(Target.class).fill(target);
		
		return ok(edit.render(TargetForm,
				User.findByEmail(request().username())));
	}
	
	public static Result save(Long id) {
		DynamicForm requestData = Form.form().bindFromRequest();
		Target target = Target.find.byId(id);
		target.loginPageUrl = requestData.field("loginPageUrl").value();
		target.logoutUrl    = requestData.field("logoutUrl").value();
		String username = requestData.field("username").value();
		String password = requestData.field("password").value();
		if(!target.loginPageUrl.isEmpty() && !username.isEmpty() && !password.isEmpty()) {
			LoginCredentials loginCredentials = new LoginCredentials(target.loginPageUrl, username, password);
			try {
				target.secretId = passwordManager.addLoginCredentials(Target.find.byId(id).title, loginCredentials);
				new FlashMessage(FlashMessage.Type.SUCCESS, "The credentials were successfully saved to the Secret Server.").send();
			} catch(Exception e) {
				String msg = "Can't store username and password because there is no connection to the Secret Server.";
				new FlashMessage(FlashMessage.Type.ERROR, msg).send();
				Logger.error(msg, e);
			}
		} else if (username.isEmpty() && password.isEmpty()) {
			FlashMessage.updateSuccess.send();
		}
		Ebean.update(target);
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
