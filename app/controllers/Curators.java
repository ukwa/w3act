package controllers;

import static play.data.Form.form;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Iterator;
import java.util.List;

import models.DCollection;
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
import views.html.collections.edit;
import views.html.users.list;
import views.html.users.useredit;
import views.html.users.userview;

import com.avaje.ebean.Ebean;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * Manage curators.
 */
@Security.Authenticated(Secured.class)
public class Curators extends AbstractController {
  
    /**
     * Display the Curators.
     */
    public static Result index() {
    	Logger.info("Curators.index()");
        return GO_HOME;
    }
    
    public static Result GO_HOME = redirect(
            routes.Curators.list(0, "name", "asc", "")
        );
    
    /**
     * Display the paginated list of Curators.
     *
     * @param page Current page number (starts from 0)
     * @param sortBy Column to be sorted
     * @param order Sort order (either asc or desc)
     * @param filter Filter applied on target urls
     */
    public static Result list(int pageNo, String sortBy, String order, String filter) {
    	Logger.info("Curators.list() " + filter);
        return ok(
        	list.render(
        			"Curators", 
        			User.find.byId(request().username()), 
        			filter, 
        			User.page(pageNo, 10, sortBy, order, filter), 
        			sortBy, 
        			order)
        	);
    }
    
    /**
     * Searching
     */
    public static Result search() {
    	
    	DynamicForm form = form().bindFromRequest();
    	String action = form.get("action");
    	String query = form.get(Const.NAME);
		Logger.info("query: " + query);
		Logger.info("action: " + action);
		
    	if (StringUtils.isBlank(query)) {
			Logger.info("Curator's name is empty. Please write name in search window.");
			flash("message", "Please enter a name in the search window");
	        return redirect(
	        		routes.Curators.list(0, "name", "asc", "")
	        );
    	}

    	int pageNo = getQueryParamAsInt(Const.PAGE_NO, 0);
    	String sort = getQueryParam(Const.SORT_BY);
    	String order = getQueryParam(Const.ORDER);

    	if (StringUtils.isEmpty(action)) {
    		return badRequest("You must provide a valid action");
    	} else {
    		if (Const.ADDENTRY.equals(action)) {
    	    	User user = new User();
    	    	user.name = query;
    	        user.uid = Utils.createId();
    	        user.url = Const.ACT_URL + user.uid;
    	        user.roles = Const.DEFAULT_ROLE;
    	        user.email = user.name + "@bl.uk";
    	        Logger.info("add curator entry with url: " + user.url + ", and name: " + user.name);
    			Form<User> userForm = Form.form(User.class);
    			userForm = userForm.fill(user);
    	        return ok(useredit.render(userForm, User.find.byId(request().username())));    			
    		} 
    		else if (Const.SEARCH.equals(action)) {
    	    	return redirect(routes.Curators.list(pageNo, sort, order, query));
		    } else {
		      return badRequest("This action is not allowed");
		    }
    	}
    }
    
    /**
     * Add an organisation.
     */
    public static Result create(String title) {

    	User user = new User();
    	user.name = title;
        user.uid = Target.createId();
        user.url = Const.ACT_URL + user.uid;
        user.roles = Const.DEFAULT_ROLE;
        user.email = user.name + "@bl.uk";
        Logger.info("add curator with url: " + user.url + ", and name: " + user.name);
		Form<User> userForm = Form.form(User.class);
		userForm = userForm.fill(user);
        return ok(useredit.render(userForm, User.find.byId(request().username())));    			
    }
    
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
     * Display the user view panel for this URL.
     */
    public static Result view(String url) {
		Logger.info("view user url: " + url);
		User user = User.findByUrl(url);
		Logger.info("user name: " + user.name + ", url: " + url);
        return ok(
                userview.render(
                        User.findByUrl(url), User.find.byId(request().username())
                )
            );
    }
    
    /**
     * Display the user edit panel for this URL.
     */
    public static Result edit(String url) {
		Logger.info("user url: " + url);
		User user = User.findByUrl(url);
		Logger.info("user name: " + user.name + ", url: " + url);
		Form<User> userForm = Form.form(User.class);
		userForm = userForm.fill(user);
        return ok(useredit.render(userForm, User.find.byId(request().username()))); 
    }
    
    public static Result sites(String url) {
        return redirect(routes.Targets.userTargets(0, "title", "asc", "", url, "", ""));
    }
    
	/**
	 * This method prepares Collection form for sending info message
	 * about errors 
	 * @return edit page with form and info message
	 */
	public static Result info() {
   	    User user = new User();
   	    user.uid = Long.valueOf(getFormParam(Const.UID));
   	    user.url = getFormParam(Const.URL);
	    user.name = getFormParam(Const.NAME);
    	user.email = user.name + "@";
	    if (getFormParam(Const.EMAIL) != null) {
	    	user.email = getFormParam(Const.EMAIL);
	    }
        if (getFormParam(Const.ORGANISATION) != null) {
        	if (!getFormParam(Const.ORGANISATION).toLowerCase().contains(Const.NONE)) {
//        		Logger.info("organisation: " + getFormParam(Const.ORGANISATION));
        		user.field_affiliation = Organisation.findByTitle(getFormParam(Const.ORGANISATION)).url;
        	} else {
        		user.field_affiliation = Const.NONE;
        	}
        }
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
        	user.revision = getFormParam(Const.REVISION);
        }
		Form<User> userFormNew = Form.form(User.class);
		userFormNew = userFormNew.fill(user);
      	return ok(
	              useredit.render(userFormNew, User.find.byId(request().username()))
	            );
    }
        
    /**
     * This method saves changes on given curator in the same object
     * completed by revision comment. The "version" field in the User object
     * contains the timestamp of the change. 
     * @return
     */
    public static Result save() {
    	Result res = null;
        String save = getFormParam("save");
        String delete = getFormParam("delete");
//        Logger.info("save: " + save);
        if (save != null) {
        	Logger.info("save updated user uid: " + getFormParam(Const.UID) + ", url: " + getFormParam(Const.URL) + 
        			", name: " + getFormParam(Const.NAME) + ", roles: " + getFormParam(Const.ROLES) +
        			", revision: " + getFormParam(Const.REVISION) + ", email: " + getFormParam(Const.EMAIL) +
        			", organisation: " + getFormParam(Const.ORGANISATION));
        	Form<User> userForm = Form.form(User.class).bindFromRequest();
            if(userForm.hasErrors()) {
            	String missingFields = "";
            	for (String key : userForm.errors().keySet()) {
            	    Logger.debug("key: " +  key);
            	    key = Utils.showMissingField(key);
            	    if (missingFields.length() == 0) {
            	    	missingFields = key;
            	    } else {
            	    	missingFields = missingFields + Const.COMMA + " " + key;
            	    }
            	}
            	Logger.info("form errors size: " + userForm.errors().size() + ", " + missingFields);
	  			flash("message", "Please fill out all the required fields, marked with a red star." + 
	  					"Missing fields are " + missingFields);
	  			return info();
            }
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
                	user.revision = getFormParam(Const.REVISION);
                }
            } catch (Exception e) {
            	Logger.info("User not existing exception");
            }
            
        	if (!isExisting) {
                if (getFormParam(Const.PASSWORD) == null || getFormParam(Const.PASSWORD).length() == 0) {
                	Logger.info("The password field is empty.");
    	  			flash("message", "The password field is empty.");
    	  			return info();
                } else {
        	    	user.password = getFormParam(Const.PASSWORD);
			    	try {
						user.password = PasswordHash.createHash(user.password);
					} catch (NoSuchAlgorithmException e) {
						Logger.info("change password - no algorithm error: " + e);
					} catch (InvalidKeySpecException e) {
						Logger.info("change password - key specification error: " + e);
					}
        	    }
               	Ebean.save(user);
    	        Logger.info("save user: " + user.toString());
        	} else {
                if (!(getFormParam(Const.PASSWORD) == null || getFormParam(Const.PASSWORD).length() == 0
                		|| getFormParam(Const.OLD_PASSWORD) == null || getFormParam(Const.OLD_PASSWORD).length() == 0)) {
            		String oldInputPassword = getFormParam(Const.OLD_PASSWORD);
            		user.password = getFormParam(Const.PASSWORD);
			    	try {
	                	String userDbPassword = User.findByUid(user.uid).password;
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
           		User oldUser = User.findById(user.uid);
           		if (!oldUser.email.equals(user.email)) {
           			flash("message", "Email field is a key and should not be changed. Original: " 
           					+ oldUser.email + ", new value: " + user.email);
           			return info();
           		}
               	Ebean.update(user);
        	}
	        res = redirect(routes.Curators.edit(user.url));
        } 
        if (delete != null) {
        	User user = User.findByUrl(getFormParam(Const.URL));
        	Ebean.delete(user);
	        res = redirect(routes.Curators.index()); 
        }
        return res;
    }
}

