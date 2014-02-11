package controllers;

import play.*;
import play.mvc.*;
import play.data.*;
import static play.data.Form.*;

import models.*;
import views.html.*;

public class Application extends Controller {
  
    // -- Authentication
    
    public static class Login {
        
        public String email;
        public String password;
        
        /**
         * We only store lowercase emails and transform user input to lowercase for this field.
         * @return null if authentication ok.
         */
        public String validate() {
            if(User.authenticate(email.toLowerCase(), password) == null) {
                return "Invalid user or password";
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
                routes.About.index()
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
            routes.Application.login()
        );
    }
  
    // -- Javascript routing
    
    public static Result javascriptRoutes() {
        response().setContentType("text/javascript");
        return ok(
            Routes.javascriptRouter("jsRoutes",
            
				controllers.routes.javascript.About.index(),
                controllers.routes.javascript.Collections.index(),
                controllers.routes.javascript.Targets.index(),
                controllers.routes.javascript.Organisations.index(),
                controllers.routes.javascript.Curators.index(),
                controllers.routes.javascript.Contact.index()
            )
        );
    }

}
