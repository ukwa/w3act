package controllers;

import java.util.Iterator;
import java.util.List;

import com.avaje.ebean.Ebean;

import models.Organisation;
import models.Role;
import models.Target;
import models.User;
import play.Logger;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import uk.bl.Const;
import uk.bl.api.Utils;
import views.html.*;

/**
 * Manage targets.
 */
@Security.Authenticated(Secured.class)
public class RoleEdit extends AbstractController {
  
    /**
     * Display the role.
     */
    public static Result index() {
        return ok(
                roles.render(
                    "Roles", User.find.byId(request().username()), models.Role.findAll(), ""
                )
            );
    }

    /**
     * Display the role edit panel for this URL.
     */
    public static Result edit(String url) {
		Logger.info("role url: " + url);
		Role role = Role.findByUrl(url);
		Logger.info("role name: " + role.name + ", url: " + url);
        return ok(
                roleedit.render(
                        Role.findByUrl(url), User.find.byId(request().username())
                )
            );
    }
    
    public static Result view(String url) {
        return ok(
                roleview.render(
                        Role.findByUrl(url), User.find.byId(request().username())
                )
            );
    }
    
    /**
     * Administer roles
     * @param url
     * @return
     */
    public static Result admin(String url) {
        return ok(
                roleadmin.render(
                        Role.findByUrl(url), User.find.byId(request().username())
                )
            );
    }
    
    /**
     * This method enables searching for given URL and redirection in order to add new entry
     * if required.
     * @return
     */
    public static Result filter() {
    	Result res = null;
    	Logger.info("RoleEdit.filter()");
        String addentry = getFormParam(Const.ADDENTRY);
        String search = getFormParam(Const.SEARCH);
        String name = getFormParam(Const.NAME);
        Logger.info("addentry: " + addentry + ", search: " + search + ", name: " + name);
        if (addentry != null) {
        	if (name != null && name.length() > 0) {
        		res = redirect(routes.RoleEdit.addEntry(name));
        	} else {
        		Logger.info("Role name is empty. Please write name in search window.");
                res = ok(
                        roles.render(
                            "Roles", User.find.byId(request().username()), models.Role.filterByName(name), ""
                        )
                    );
        	}
        } else {
            res = ok(
            		roles.render(
                        "Roles", User.find.byId(request().username()), models.Role.filterByName(name), name
                    )
                );
        }
        return res;
    }	   
    
    /**
     * Add new role entry.
     * @param role title
     * @return
     */
    public static Result addEntry(String name) {
    	Role role = new Role();
    	role.name = name;
        role.id = Target.createId();
        role.url = Const.ACT_URL + role.id;
		Logger.info("add entry with url: " + role.url + ", and name: " + role.name);
        return ok(
                roleedit.render(
                      role, User.find.byId(request().username())
                )
            );
    }
      
    /**
     * This method saves new object or changes on given Organisation in the same object
     * completed by revision comment. The "version" field in the Organisation object
     * contains the timestamp of the change. 
     * @return
     */
    public static Result save() {
    	Result res = null;
        String save = getFormParam(Const.SAVE);
        String delete = getFormParam(Const.DELETE);
//        Logger.info("save: " + save);
        if (save != null) {
        	Logger.info("save organisation nid: " + getFormParam(Const.NID) + ", url: " + getFormParam(Const.URL) + 
        			", title: " + getFormParam(Const.TITLE) + ", revision: " + getFormParam(Const.REVISION) + 
        			", abbreviation: " + getFormParam(Const.FIELD_ABBREVIATION));
        	Role role = null;
            boolean isExisting = true;
            try {
                try {
                	role = Role.findByUrl(getFormParam(Const.URL));
                } catch (Exception e) {
                	Logger.info("is not existing exception");
                	isExisting = false;
                	role = new Role();
                	role.id = Long.valueOf(getFormParam(Const.NID));
                	role.url = getFormParam(Const.URL);
                }
                if (role == null) {
                	Logger.info("is not existing");
                	isExisting = false;
                	role = new Role();
                	role.id = Long.valueOf(getFormParam(Const.NID));
                	role.url = getFormParam(Const.URL);
                }
                
                role.name = getFormParam(Const.NAME);
        	    if (getFormParam(Const.PERMISSIONS) != null) {
        	    	role.permissions = getFormParam(Const.PERMISSIONS);
        	    }
        	    if (getFormParam(Const.DESCRIPTION) != null) {
        	    	role.description = getFormParam(Const.DESCRIPTION);
        	    }
        	    if (role.revision == null) {
        	    	role.revision = "";
        	    }
                if (getFormParam(Const.REVISION) != null) {
                	role.revision = role.revision.concat(", " + getFormParam(Const.REVISION));
                }
            } catch (Exception e) {
            	Logger.info("Role not existing exception");
            }
            
        	if (!isExisting) {
               	Ebean.save(role);
    	        Logger.info("save role: " + role.toString());
        	} else {
           		Logger.info("update role: " + role.toString());
               	Ebean.update(role);
        	}
	        res = redirect(routes.RoleEdit.view(role.url));
        } 
        if (delete != null) {
        	Role role = Role.findByUrl(getFormParam(Const.URL));
        	Ebean.delete(role);
	        res = redirect(routes.RoleEdit.index()); 
        }
        return res;
    }	   
    
    /**
     * This method implements administration for roles associated with particular organisation.
     * @return
     */
    public static Result adminrole() {
    	Result res = null;
        String save = getFormParam(Const.SAVE);
        if (save != null) {
        	Role role = null;
            boolean isExisting = true;
            try {
               	role = Role.findByUrl(getFormParam(Const.URL));
               	
//		        List<User> userList = User.findAll();
//		        Iterator<User> userItr = userList.iterator();
//		        while (userItr.hasNext()) {
//		        	User user = userItr.next();
//	                if (getFormParam(user.name) != null) {
////                		Logger.info("getFormParam(user.name): " + getFormParam(user.name) + " " + user.name);
//		                boolean userFlag = Utils.getNormalizeBooleanString(getFormParam(user.name));
//		                if (userFlag) {
//		                	addLink(user, role); 
//		                } else {
//		                	removeLink(user, organisation); 
//		                }
//	                } else {
//	                	removeLink(user, organisation); 	                	
//	                }
//		        }
            } catch (Exception e) {
            	Logger.info("User not existing exception");
            }
	        res = redirect(routes.RoleEdit.admin(role.url));
        } else {
        	res = ok();
        }
        return res;
    }
	    
    
}

