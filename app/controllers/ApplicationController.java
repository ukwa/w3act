package controllers;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import org.apache.commons.lang3.StringUtils;

import com.avaje.ebean.Ebean;
import play.*;
import play.mvc.*;
import play.data.*;
import static play.data.Form.*;
import models.*;
import uk.bl.api.PasswordHash;
import uk.bl.api.Utils;
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
//				Logger.debug("validate() inserted password: " + password);
				String inputPassword = password;
//				Logger.debug("validate() db hash for email: " + email.toLowerCase());
				User user = User.findByEmail(email.toLowerCase());
				if (user == null) {
					return "Invalid email";
				}
				if (user.roles != null && !user.roles.isEmpty() && user.hasRole("closed")) {
					return "This user account has been closed. Please contact the British Library web archiving team";
				}
				String userPassword = User.findByEmail(email.toLowerCase()).password;
				Logger.debug("userPassword: " + userPassword + " - " + inputPassword);
        		res = PasswordHash.validatePassword(inputPassword, userPassword);
			} catch (NoSuchAlgorithmException e) {
				Logger.debug("validate() no algorithm error: " + e);
			} catch (InvalidKeySpecException e) {
				Logger.debug("validate() key specification error: " + e);
			}
            if(!res || User.authenticate(email.toLowerCase(), User.findByEmail(email.toLowerCase()).password) == null) {
                return "Password not recognised";
            }
        	Logger.debug("res: " + res);
            return null;
        }
        
    }

    /**
     * Login page.
     */
    public static Result login() {
    	// If user is already logged in, redirect to the homepage:
    	String email = session().get("email");
    	User user = User.findByEmail(email);
    	if (user != null) {
            return redirect( routes.ApplicationController.index() );    		
    	}
		// Redirect to login page (embedding the flash scope url to redirect to afterwards):
        return ok(
            login.render(form(Login.class))
        );
    }
    	
    /**
     * Handle login form submission.
     * We only store lowercase emails and transform user input to lowercase for this field.
     */
    public static Result authenticate() {
		Logger.debug("Authenticate");

		// Grab the url to redirect to after login:
    	DynamicForm requestData = Form.form().bindFromRequest();
    	String url = "";
    	if( requestData.data().containsKey("redirectToUrl")) {
    		url = requestData.get("redirectToUrl");
    	}
		// Parse the login:
        Form<Login> loginForm = form(Login.class).bindFromRequest();
        if(loginForm.hasErrors()) {
        	flash().put("url", url);
            return badRequest(login.render(loginForm));
        } else {
            session("email", loginForm.get().email.toLowerCase());
            Logger.debug("The redirectToUrl is: " + url);
            User user = User.findByEmail(session().get("email"));
            if (user != null) { 
            user.lastLogin = Utils.INSTANCE.getCurrentTimeStamp();
    		Ebean.update(user);
            }
            if( StringUtils.isBlank(url) ) url = routes.ApplicationController.home().url();

            Logger.debug("Final Url is: " + url);

			return redirect( url );
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
  
    /**
     * Display the About tab.
     */
	@Security.Authenticated(SecuredController.class)
    public static Result index() {
		String email = session().get("email");
		User user = User.findByEmail(email);
		return ok(about.render("About", user));
    }
    
    public static String getVersion() {
    	if( Play.isProd() ) {
        	return ApplicationController.class.getPackage().getImplementationVersion();
    	} else {
    		return Play.application().configuration().getString("application.version");
    	}
    }
    
    public static Result addContent() {
		return ok(
            addcontent.render("AddContent", User.findByEmail(request().username()))
        );
    }
    
    public static Result findContent() {
		return ok(
            findcontent.render("FindContent", User.findByEmail(request().username()))
        );
    }

    // -- Javascript routing
    
    public static Result javascriptRoutes() {
        response().setContentType("text/javascript");
        return ok(
            Routes.javascriptRouter("jsRoutes",
            	controllers.routes.javascript.ApplicationController.index(),
                controllers.routes.javascript.CollectionController.index(),
                controllers.routes.javascript.InstanceController.index(),
                controllers.routes.javascript.TargetController.index(),
                controllers.routes.javascript.OrganisationController.index(),
                controllers.routes.javascript.UserController.index(),
                controllers.routes.javascript.ContactController.index(),
				controllers.routes.javascript.TargetController.allSubjectsIDsAsJson(),
				controllers.routes.javascript.TargetController.allCollectionsIDsAsJson(),
				controllers.routes.javascript.CollectionController.allCollectionAreasAsJson()
			)
        );
    }
    
	@Security.Authenticated(SecuredController.class)
    public static Result home() {
		//String email = session().get("email");
		//User user = User.findByEmail(email);
		//if (user.ddhaptUser && getDDHAPTStatus())
		//	return redirect(routes.WatchedTargets.overview(0, "target.title", "asc"));
		//else
		return redirect(routes.ApplicationController.index());
	}
	
	public static boolean getDDHAPTStatus(){
		
		Boolean ddhaptStatus = Play.application().configuration().getBoolean("enableDDHAPT");		
		return ddhaptStatus;		
	}
	
	//Redirect urls with trailing "/" to the url they intended
    public static Result untrail(String path) {
		
	 // We've already removed the trailing "/", so if there's still one (or more) there, 
	 // there's a danger of too many redirects, so let's just 404 in case somebody's playing silly browsers
		Logger.debug("+++++++++  Url path is: " + path);


		if (path.charAt(path.length() -1 ) == '/')
		{
			Logger.debug("+++++++++ Case:  Sorry, that page does not exist. path = " + path);

			return notFound("Sorry, that page does not exist.");
		}
	 else {
			Logger.debug("+++++++++ Case:  HTTP 301, path = " + path);

			return movedPermanently(Play.application().configuration().getString("application.context")  + "/" + path); //HTTP 301
	 }
    }
	

}

