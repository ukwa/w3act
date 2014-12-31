package controllers;


import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import models.User;
import play.Logger;
import play.data.DynamicForm;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Result;
import play.mvc.Security;
import uk.bl.api.PasswordHash;
import views.html.passwords.edit;
import views.html.infomessage;

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
    	Logger.info("edit");
    	User user = User.findByEmail(request().username());
        return ok(edit.render(user)); 
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
    	Logger.info("saving");
    	
    	DynamicForm request = DynamicForm.form().bindFromRequest();

    	String oldPassword = request.get("oldpassword");
    	String password = request.get("password");
    	String passwordValidation = request.get("password_validation");

    	Long id = Long.valueOf(request.get("id"));

    	Logger.info(oldPassword + " " + password + " " + passwordValidation);
    	
    	User user = User.findById(id);

    	if (user != null) {
            if (StringUtils.isEmpty(password)) {
            	Logger.info("The password field is empty.");
	  			flash("message", "The password field is empty.");
	  			return info();
            } 
            if (StringUtils.isEmpty(passwordValidation)) {
            	Logger.info("The password validation field is empty.");
	  			flash("message", "The password validation field is empty.");
	  			return info();
            } 
            if (!password.equals(passwordValidation)) {
            	Logger.info("The value of the password field does not match to the value of the password validation field.");
	  			flash("message", "The value of the password field does not match to the value of the password validation field.");
	  			return info();
            }

            /**
             * Change password
             */                
            if (StringUtils.isNotBlank(password) || StringUtils.isNotBlank(passwordValidation) || StringUtils.isNotBlank(oldPassword)) {
		    	try {
                	String userDbPassword = user.password;
            		boolean isValidOldPassword = PasswordHash.validatePassword(oldPassword, userDbPassword);
            		if (!isValidOldPassword) {
                    	Logger.info("The old password is not correct.");
        	  			flash("message", "The old password is not correct.");
        	  			return info();	            		
        	  		} else {
        	  			user.password = PasswordHash.createHash(password);
        	  		}
				} catch (NoSuchAlgorithmException e) {
					Logger.info("change password - no algorithm error: " + e);
				} catch (InvalidKeySpecException e) {
					Logger.info("change password - key specification error: " + e);
				}
    	    }
       		Logger.info("update user: " + user.toString());
           	user.save();
    	}
    	res = ok(infomessage.render("You have successfully updated your password."));
        return res;
    }
}

