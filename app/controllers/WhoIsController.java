package controllers;


import java.util.List;

import models.Target;
import models.User;
import play.Logger;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Result;
import play.mvc.Security;
import uk.bl.Const;
import uk.bl.api.Utils;
import uk.bl.exception.WhoisException;
import uk.bl.scope.Scope;
import uk.bl.scope.WhoIsData;
import views.html.whois.index;
import views.html.whois.results;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Manage passwords.
 */
@Security.Authenticated(SecuredController.class)
public class WhoIsController extends AbstractController {
   
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
		Integer ukRegistrantCount = Target.findWhoIsCount(true);
		Integer nonUKRegistrantCount = Target.findWhoIsCount(false); 
    	WhoIsData whoIsData = new WhoIsData(null, null, ukRegistrantCount, nonUKRegistrantCount, 0);
        return ok(index.render(User.findByEmail(request().username()), whoIsData)); 
    }
    
    /**
     * This method saves changed password in the same object. The "version" field in the User object
     * contains the timestamp of the change. 
     * @return
     * @throws WhoisException 
     */
    public static Result check(int total) throws WhoisException {
    	Logger.info("CheckWhoIs controller check() " + total);
        String check = getFormParam("check");
        if (check != null || total > 0) {
        	Logger.info("target number to check: " + getFormParam(Const.NUMBER));
            if (getFormParam(Const.NUMBER) != null && getFormParam(Const.NUMBER).length() > 0 && Utils.isNumeric(getFormParam(Const.NUMBER))) {
            	total = Integer.valueOf(getFormParam(Const.NUMBER));
            }

			WhoIsData whoIsData = Scope.INSTANCE.checkWhois(total);
			User user = User.findByEmail(request().username());
			// need to match up with lookupentry and get the last_update
        	return ok(
        			results.render(user, whoIsData)
        	);
        } 
        return index();
    }    

}

