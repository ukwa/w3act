package controllers;

import com.avaje.ebean.Ebean;
import com.thesecretserver.LoginCredentials;
import com.thesecretserver.PasswordManager;

import models.FlashMessage;
import models.User;
import models.WatchedTarget;
import play.Play;
import play.data.Form;
import play.mvc.Result;
import play.mvc.Security;
import views.html.logincredentials.edit;

@Security.Authenticated(SecuredController.class)
public class LoginCredentialsController extends AbstractController {
	
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
			String secretServerUser = Play.application().configuration().getString("secret_server_user");
			String secretServerPassword = Play.application().configuration().getString("secret_server_password");
			try {
				PasswordManager passwordManager = new PasswordManager(secretServerUser, secretServerPassword, "", "AD");
				watchedTarget.secretId = passwordManager.addLoginCredentials(WatchedTarget.find.byId(id).target.title, loginCredentials);
				FlashMessage.updateSuccess.send();
			} catch(Exception e) {
				new FlashMessage(FlashMessage.Type.ERROR,
						"Can't store username and password because there is no connection to the Secret Server."
					).send();
				e.printStackTrace();
			}
		}
		Ebean.update(watchedTarget);
		return redirect(routes.LoginCredentialsController.edit(id));
	}
}
