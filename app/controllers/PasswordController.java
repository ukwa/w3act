package controllers;


import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;

import models.User;

import play.Logger;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Result;
import play.mvc.Security;
import uk.bl.Const;
import uk.bl.api.PasswordHash;
import views.html.passwords.edit;
import views.html.infomessage;

import com.avaje.ebean.Ebean;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * Manage passwords.
 */
@Security.Authenticated(SecuredController.class)
public class PasswordController extends AbstractController {
   
    /**
     * Add new user entry.
     * @param user
     * @return
     */
    @BodyParser.Of(BodyParser.Json.class)
    public static Result filterByJson(String name) {
        JsonNode jsonData = null;
        if (name != null) {
	        List<User> users = User.filterByName(name);
	        jsonData = Json.toJson(users);
        }
        return ok(jsonData);
    }
    
    /**
     * Display the user edit panel for this URL.
     */
    public static Result edit() {
        return ok(edit.render(User.findByEmail(request().username()))); 
    }
    
	/**
	 * This method prepares Collection form for sending info message
	 * about errors 
	 * @return edit page with form and info message
	 */
	public static Result info() {
      	return ok(
  			edit.render(User.findByEmail(request().username()))
        );
    }
        
    /**
     * This method saves changed password in the same object. The "version" field in the User object
     * contains the timestamp of the change. 
     * @return
     */
    public static Result save() {
    	Result res = null;
        String save = getFormParam("save");
        if (save != null) {
        	Logger.info("input data for changed password for user uid: " + 
        			getFormParam(Const.UID) + ", url: " + getFormParam(Const.URL));
//        	Form<User> userForm = Form.form(User.class).bindFromRequest();
        	User user = null;
            boolean isExisting = true;
            try {
                try {
            	    user = User.findByUrl(getFormParam(Const.URL));
                } catch (Exception e) {
                	Logger.info("User is not existing in database exception: " + e.getMessage());
                	isExisting = false;
                	return ok(infomessage.render("User is not existing in database exception: " + e.getMessage()));
                }
                if (user == null) {
                	Logger.info("User is not existing in database - resulting oject for given URL '" + 
                			getFormParam(Const.URL) + "' is null.");
                	isExisting = false;
                	return ok(infomessage.render("User is not existing in database - resulting oject for given URL '" + 
                			getFormParam(Const.URL) + "' is null."));
                }                
            } catch (Exception e) {
            	Logger.info("User not existing exception");
            	return ok(infomessage.render("User is not existing in database exception: " + e.getMessage()));
            }
            
        	if (isExisting) {
                if (getFormParam(Const.PASSWORD) == null || getFormParam(Const.PASSWORD).length() == 0) {
                	Logger.info("The password field is empty.");
    	  			flash("message", "The password field is empty.");
    	  			return info();
                } 
                if (getFormParam(Const.PASSWORD_VALIDATION) == null || getFormParam(Const.PASSWORD_VALIDATION).length() == 0) {
                	Logger.info("The password validation field is empty.");
    	  			flash("message", "The password validation field is empty.");
    	  			return info();
                } 
                if (!getFormParam(Const.PASSWORD).equals(getFormParam(Const.PASSWORD_VALIDATION))) {
                	Logger.info("The value of the password field does not match to the value of the password validation field.");
    	  			flash("message", "The value of the password field does not match to the value of the password validation field.");
    	  			return info();
                }
                
                /**
                 * Change password
                 */                
                if (!(getFormParam(Const.PASSWORD) == null || getFormParam(Const.PASSWORD).length() == 0
                		|| getFormParam(Const.PASSWORD_VALIDATION) == null || getFormParam(Const.PASSWORD_VALIDATION).length() == 0
                		|| getFormParam(Const.OLD_PASSWORD) == null || getFormParam(Const.OLD_PASSWORD).length() == 0)) {
            		String oldInputPassword = getFormParam(Const.OLD_PASSWORD);
            		user.password = getFormParam(Const.PASSWORD);
			    	try {
	                	String userDbPassword = User.findByUid(user.id).password;
	            		boolean isValidOldPassword = PasswordHash.validatePassword(oldInputPassword, userDbPassword);
	            		if (!isValidOldPassword) {
	                    	Logger.info("The old password is not correct.");
	        	  			flash("message", "The old password is not correct.");
	        	  			return info();	            		
	        	  		} else {
	        	  			user.password = PasswordHash.createHash(user.password);
	        	  		}
					} catch (NoSuchAlgorithmException e) {
						Logger.info("change password - no algorithm error: " + e);
					} catch (InvalidKeySpecException e) {
						Logger.info("change password - key specification error: " + e);
					}
        	    }
           		Logger.info("update user: " + user.toString());
               	Ebean.update(user);
        	}
        	res = ok(infomessage.render("You have successfully updated your password."));
        } 
        return res;
    }
}

