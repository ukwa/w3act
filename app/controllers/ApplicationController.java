package controllers;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import org.apache.commons.lang3.StringUtils;

import play.*;
import play.mvc.*;
import play.data.*;
import static play.data.Form.*;
import models.*;
import uk.bl.api.PasswordHash;
import views.html.*;

public class ApplicationController extends Controller {
  
    // -- Authentication
    
    public static class Login {
        
        public String email;
        public String password;
        
        /**
         * We only store lowercase emails and transform user input to lowercase for this field.
         * @return null if authentication ok.
         */
        public String validate() {
        	boolean res = false;
        	try {
        		if (StringUtils.isBlank(email)) {
        			return "Please enter an email";
        		}
        		if (StringUtils.isBlank(password)) {
        			return "Please enter a password";
        		}
//				Logger.info("validate() inserted password: " + password);
				String inputPassword = password;
//				Logger.info("validate() db hash for email: " + email.toLowerCase());
				if (User.findByEmail(email.toLowerCase()) == null) {
					return "Invalid email";
				}
				String userPassword = User.findByEmail(email.toLowerCase()).password;
        		res = PasswordHash.validatePassword(inputPassword, userPassword);
			} catch (NoSuchAlgorithmException e) {
				Logger.info("validate() no algorithm error: " + e);
			} catch (InvalidKeySpecException e) {
				Logger.info("validate() key specification error: " + e);
			}
        	Logger.info("res: " + res);
            if(!res || User.authenticate(email.toLowerCase(), User.findByEmail(email.toLowerCase()).password) == null) {
                return "Password not recognised";
            }
            return null;
        }
        
    }

    /**
     * Login page.
     */
    public static Result login() {
        return ok(
            login.render(form(Login.class))
        );
    }
    
    /**
     * Handle login form submission.
     * We only store lowercase emails and transform user input to lowercase for this field.
     */
    public static Result authenticate() {
        Form<Login> loginForm = form(Login.class).bindFromRequest();
        if(loginForm.hasErrors()) {
            return badRequest(login.render(loginForm));
        } else {
            session("email", loginForm.get().email.toLowerCase());
            return redirect(
                routes.AboutController.index()
            );
        }
    }

    /**
     * Logout and clean the session.
     */
    public static Result logout() {
        session().clear();
        flash("success", "You've been logged out");
        return redirect(
            routes.ApplicationController.login()
        );
    }
  
    // -- Javascript routing
    
    public static Result javascriptRoutes() {
        response().setContentType("text/javascript");
        return ok(
            Routes.javascriptRouter("jsRoutes",
            
				controllers.routes.javascript.AboutController.index(),
                controllers.routes.javascript.CollectionController.index(),
                controllers.routes.javascript.TargetController.index(),
                controllers.routes.javascript.OrganisationController.index(),
                controllers.routes.javascript.UserController.index(),
                controllers.routes.javascript.Contact.index()
            )
        );
    }

}

