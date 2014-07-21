package controllers;

import static play.data.Form.form;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Iterator;
import java.util.List;

import models.Organisation;
import models.Role;
import models.Target;
import models.User;

import org.apache.commons.lang3.StringUtils;

import play.Logger;
import play.data.DynamicForm;
import play.data.Form;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Result;
import play.mvc.Security;
import uk.bl.Const;
import uk.bl.api.PasswordHash;
import uk.bl.api.Utils;
import uk.bl.api.WhoIsThread;
import views.html.whois.index;
import views.html.infomessage;

import com.avaje.ebean.Ebean;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * Manage passwords.
 */
@Security.Authenticated(Secured.class)
public class CheckWhoIs extends AbstractController {
   
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
    public static Result index() {
        return ok(index.render(User.findByEmail(request().username()))); 
    }
    
	/**
	 * This method prepares Collection form for sending info message
	 * about errors 
	 * @return edit page with form and info message
	 */
	public static Result info() {
      	return ok(
  			index.render(User.findByEmail(request().username()))
        );
    }
        
    /**
     * This method saves changed password in the same object. The "version" field in the User object
     * contains the timestamp of the change. 
     * @return
     */
    public static Result check() {
//    	Result res = null;
        String check = getFormParam("check");
        if (check != null) {
        	Logger.info("CheckWhoIs controller check() ");
        	Logger.info("target number to check: " + getFormParam(Const.NUMBER));
        	int number = 50;
            if (getFormParam(Const.NUMBER) != null 
            		&& getFormParam(Const.NUMBER).length() > 0
            		&& Utils.isNumeric(getFormParam(Const.NUMBER))) {
            	number = Integer.valueOf(getFormParam(Const.NUMBER));
            }
//        	new WhoIsThread("WhoIs check").start();
        	Target target = new Target();
        	WhoIsThread notifier = new WhoIsThread(target, number);
    		Thread notifierThread = new Thread(notifier, "notifierWhoIsThread");
    		notifierThread.start();
//        	res = ok(infomessage.render("You have successfully checked the current status of WhoIs service."));
        	return ok(infomessage.render("You have started a check for the current status of the WhoIs service. Please refresh the 'Archivist Tasks > Check WhoIs' page using F5 in order to see changes."));
//  			flash("message", "You have started a check for the current status of the WhoIs service. Please refresh this page using F5 in order to see changes.");
//  			return index();
        } 
        return index();
    }
}

