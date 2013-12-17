package controllers;

import play.*;
import play.mvc.*;
import play.data.*;
import static play.data.Form.*;

import java.util.*;

import com.avaje.ebean.Ebean;

import models.*;
import uk.bl.Const;
import uk.bl.api.Utils;
import uk.bl.scope.Scope;
import views.html.*;

/**
 * Manage curators.
 */
@Security.Authenticated(Secured.class)
public class Curators extends AbstractController {
  
    /**
     * Display the Curators.
     */
    public static Result index() {
        return ok(
            curators.render(
                "Curators", User.find.byId(request().username()), models.User.findAll(), ""
            )
        );
    }

    /**
     * This method saves changes on given curator in the same object
     * completed by revision comment. The "version" field in the User object
     * contains the timestamp of the change. 
     * @return
     */
    public static Result saveUser() {
    	Result res = null;
        String save = getFormParam("save");
        String delete = getFormParam("delete");
//        Logger.info("save: " + save);
        if (save != null) {
        	Logger.info("save updated user uid: " + getFormParam(Const.UID) + ", url: " + getFormParam(Const.URL) + 
        			", name: " + getFormParam(Const.NAME) + ", roles: " + getFormParam(Const.ROLES) +
        			", revision: " + getFormParam(Const.REVISION) + ", email: " + getFormParam(Const.EMAIL) +
        			", organisation: " + getFormParam(Const.ORGANISATION));
        	User user = null;
            boolean isExisting = true;
            try {
                try {
            	    user = User.findByUrl(getFormParam(Const.URL));
                } catch (Exception e) {
                	Logger.info("is not existing exception");
                	isExisting = false;
                	user = new User();
            	    user.uid = Long.valueOf(getFormParam(Const.UID));
            	    user.url = getFormParam(Const.URL);
                }
                if (user == null) {
                	Logger.info("is not existing");
                	isExisting = false;
                	user = new User();
            	    user.uid = Long.valueOf(getFormParam(Const.UID));
            	    user.url = getFormParam(Const.URL);
                }
                
        	    user.name = getFormParam(Const.NAME);
    	    	user.email = user.name + "@";
        	    if (getFormParam(Const.EMAIL) != null) {
        	    	user.email = getFormParam(Const.EMAIL);
        	    }
        	    if (getFormParam(Const.PASSWORD) != null) {
        	    	user.password = getFormParam(Const.PASSWORD);
        	    }
                if (getFormParam(Const.ORGANISATION) != null) {
                	if (!getFormParam(Const.ORGANISATION).toLowerCase().contains(Const.NONE)) {
//                		Logger.info("organisation: " + getFormParam(Const.ORGANISATION));
                		user.field_affiliation = Organisation.findByTitle(getFormParam(Const.ORGANISATION)).url;
                	} else {
                		user.field_affiliation = Const.NONE;
                	}
                }
//                if (getFormParam(Const.ROLES) != null) {
//                	user.roles = getFormParam(Const.ROLES);
//                }
                String roleStr = "";
		        List<Role> roleList = Role.findAll();
		        Iterator<Role> roleItr = roleList.iterator();
		        while (roleItr.hasNext()) {
		        	Role role = roleItr.next();
	                if (getFormParam(role.name) != null) {
		                boolean roleFlag = Utils.getNormalizeBooleanString(getFormParam(role.name));
		                if (roleFlag) {
		                	if (roleStr.length() == 0) {
		                		roleStr = role.name;
		                	} else {
		                		roleStr = roleStr + ", " + role.name;
		                	}
		                }
	                }
		        }
		        if (roleStr.length() == 0) {
		        	user.roles = Const.DEFAULT_ROLE;
		        } else {
		        	user.roles = roleStr;
		        }
		        Logger.info("roleStr: "+ roleStr + ", user.roles: " + user.roles);
                if (getFormParam(Const.REVISION) != null) {
                	user.revision = user.revision.concat(", " + getFormParam(Const.REVISION));
                }
            } catch (Exception e) {
            	Logger.info("User not existing exception");
            }
            
        	if (!isExisting) {
               	Ebean.save(user);
    	        Logger.info("save user: " + user.toString());
        	} else {
           		Logger.info("update user: " + user.toString());
               	Ebean.update(user);
        	}
	        res = redirect(routes.UserEdit.view(user.url));
        } 
        if (delete != null) {
        	User user = User.findByUrl(getFormParam(Const.URL));
        	Ebean.delete(user);
	        res = redirect(routes.Curators.index()); 
        }
        return res;
    }
	
}

