package controllers;

import java.util.List;

import com.avaje.ebean.Ebean;
import com.fasterxml.jackson.databind.JsonNode;

import models.Permission;
import models.Target;
import models.User;
import play.Logger;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Result;
import play.mvc.Security;
import uk.bl.Const;
import views.html.*;

/**
 * Manage permissions.
 */
@Security.Authenticated(Secured.class)
public class PermissionEdit extends AbstractController {
  
    /**
     * Display the permission.
     */
    public static Result index() {
        return ok(
                permissions.render(
                    "Permissions", User.find.byId(request().username()), models.Permission.findAll(), ""
                )
            );
    }

    /**
     * Display the permission edit panel for this URL.
     */
    public static Result edit(String url) {
		Logger.info("permission url: " + url);
		Permission permission = Permission.findByUrl(url);
		Logger.info("permission name: " + permission.name + ", url: " + url);
        return ok(
                permissionedit.render(
                        Permission.findByUrl(url), User.find.byId(request().username())
                )
            );
    }
    
    public static Result view(String url) {
        return ok(
                permissionview.render(
                        Permission.findByUrl(url), User.find.byId(request().username())
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
    	Logger.info("PermissionEdit.filter()");
        String addentry = getFormParam(Const.ADDENTRY);
        String search = getFormParam(Const.SEARCH);
        String name = getFormParam(Const.NAME);
        Logger.info("addentry: " + addentry + ", search: " + search + ", name: " + name);
        if (addentry != null) {
        	if (name != null && name.length() > 0) {
        		res = redirect(routes.PermissionEdit.addEntry(name));
        	} else {
        		Logger.info("Permission name is empty. Please write name in search window.");
                res = ok(
                        permissions.render(
                            "Permissions", User.find.byId(request().username()), models.Permission.filterByName(name), ""
                        )
                    );
        	}
        } else {
            res = ok(
            		permissions.render(
                        "Permissions", User.find.byId(request().username()), models.Permission.filterByName(name), name
                    )
                );
        }
        return res;
    }	   
    
    /**
     * Add new permission entry.
     * @param permission title
     * @return
     */
    public static Result addEntry(String name) {
    	Permission permission = new Permission();
    	permission.name = name;
        permission.id = Target.createId();
        permission.url = Const.ACT_URL + permission.id;
		Logger.info("add entry with url: " + permission.url + ", and name: " + permission.name);
        return ok(
                permissionedit.render(
                      permission, User.find.byId(request().username())
                )
            );
    }
      
    /**
     * This method saves new object or changes on given Permission in the same object
     * completed by revision comment. The "version" field in the Permission object
     * contains the timestamp of the change. 
     * @return
     */
    public static Result save() {
    	Result res = null;
        String save = getFormParam(Const.SAVE);
        String delete = getFormParam(Const.DELETE);
//        Logger.info("save: " + save);
        if (save != null) {
        	Logger.info("save permission id: " + getFormParam(Const.ID) + ", url: " + getFormParam(Const.URL) + 
        			", name: " + getFormParam(Const.NAME) + ", revision: " + getFormParam(Const.REVISION));
        	Permission permission = null;
            boolean isExisting = true;
            try {
                try {
                	permission = Permission.findByUrl(getFormParam(Const.URL));
                } catch (Exception e) {
                	Logger.info("is not existing exception");
                	isExisting = false;
                	permission = new Permission();
                	permission.id = Long.valueOf(getFormParam(Const.ID));
                	permission.url = getFormParam(Const.URL);
                }
                if (permission == null) {
                	Logger.info("is not existing");
                	isExisting = false;
                	permission = new Permission();
                	permission.id = Long.valueOf(getFormParam(Const.ID));
                	permission.url = getFormParam(Const.URL);
                }
                
                permission.name = getFormParam(Const.NAME);
        	    if (getFormParam(Const.DESCRIPTION) != null) {
        	    	permission.description = getFormParam(Const.DESCRIPTION);
        	    }
        	    if (permission.revision == null) {
        	    	permission.revision = "";
        	    }
                if (getFormParam(Const.REVISION) != null) {
                	permission.revision = permission.revision.concat(", " + getFormParam(Const.REVISION));
                }
            } catch (Exception e) {
            	Logger.info("Permission not existing exception");
            }
            
        	if (!isExisting) {
               	Ebean.save(permission);
    	        Logger.info("save permission: " + permission.toString());
        	} else {
           		Logger.info("update permission: " + permission.toString());
               	Ebean.update(permission);
        	}
	        res = redirect(routes.PermissionEdit.view(permission.url));
        } 
        if (delete != null) {
        	Permission permission = Permission.findByUrl(getFormParam(Const.URL));
        	Ebean.delete(permission);
	        res = redirect(routes.PermissionEdit.index()); 
        }
        return res;
    }	   

    @BodyParser.Of(BodyParser.Json.class)
    public static Result filterByJson(String name) {
        JsonNode jsonData = null;
        if (name != null) {
	        List<Permission> permissions = Permission.filterByName(name);
	        jsonData = Json.toJson(permissions);
        }
        return ok(jsonData);
    }
}

