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
import views.html.*;

/**
 * Manage dcollections.
 */
@Security.Authenticated(Secured.class)
public class DCollections extends AbstractController {
  
    /**
     * Display the dcollections.
     */
    public static Result index() {
        return ok(
            dcollections.render(
                "Collections", User.find.byId(request().username()), models.DCollection.findInvolving(), ""
            )
        );
    }

    // -- DCollections

    /**
     * Add a dcollection.
     */
    public static Result add() {
        DCollection newDCollection = DCollection.create(
            "New dcollection"
        );
        return ok();
    }
    
    /**
     * Rename a dcollection.
     */
    public static Result rename(Long dcollection) {
        return ok(
            DCollection.rename(
                dcollection, 
                form().bindFromRequest().get("title")
            )
        );
    }
    
    /**
     * Delete a dcollection.
     */
    public static Result delete(Long dcollection) {
        DCollection.find.ref(dcollection).delete();
        return ok();
    }

    /**
     * This method saves new object or changes on given Collection in the same object
     * completed by revision comment. The "version" field in the Collection object
     * contains the timestamp of the change. 
     * @return
     */
    public static Result save() {
    	Result res = null;
        String save = getFormParam(Const.SAVE);
        String delete = getFormParam(Const.DELETE);
//        Logger.info("save: " + save);
        if (save != null) {
        	Logger.info("save collection nid: " + getFormParam(Const.NID) + ", url: " + getFormParam(Const.URL) + 
        			", title: " + getFormParam(Const.TITLE) + ", revision: " + getFormParam(Const.REVISION));
        	DCollection collection = null;
            boolean isExisting = true;
            try {
                try {
                	collection = DCollection.findByUrl(getFormParam(Const.URL));
                } catch (Exception e) {
                	Logger.info("is not existing exception");
                	isExisting = false;
                	collection = new DCollection();
                	collection.nid = Long.valueOf(getFormParam(Const.NID));
                	collection.url = getFormParam(Const.URL);
                }
                if (collection == null) {
                	Logger.info("is not existing");
                	isExisting = false;
                	collection = new DCollection();
                	collection.nid = Long.valueOf(getFormParam(Const.NID));
                	collection.url = getFormParam(Const.URL);
                }
                
                collection.title = getFormParam(Const.TITLE);
        	    if (getFormParam(Const.SUMMARY) != null) {
        	    	collection.summary = getFormParam(Const.SUMMARY);
        	    }
        	    if (collection.revision == null) {
        	    	collection.revision = "";
        	    }
                if (getFormParam(Const.REVISION) != null) {
                	collection.revision = collection.revision.concat(", " + getFormParam(Const.REVISION));
                }
            } catch (Exception e) {
            	Logger.info("User not existing exception");
            }
            
        	if (!isExisting) {
               	Ebean.save(collection);
    	        Logger.info("save collection: " + collection.toString());
        	} else {
           		Logger.info("update collection: " + collection.toString());
               	Ebean.update(collection);
        	}
	        res = redirect(routes.CollectionEdit.view(collection.url));
        } 
        if (delete != null) {
        	DCollection collection = DCollection.findByUrl(getFormParam(Const.URL));
        	Ebean.delete(collection);
	        res = redirect(routes.DCollections.index()); 
        }
        return res;
    }
	
    /**
     * This method adds link to passed collection in given User object if 
     * link does not already exists.
     * @param user
     * @param collection
     */
//    private static void addLink(User user, DCollection collection) {
////		Logger.info("flag true add link: " + user.name);
//    	if (!isOrganisationLink(user, collection)) {
//    		user.field_affiliation = collection.url;
//        	Ebean.update(user);
//    	}
//	}
    
    /**
     * This method removes link to passed collection from given User object if 
     * link exists.
     * @param user
     * @param collection
     */
//    private static void removeLink(User user, Organisation collection) {
////		Logger.info("flag false remove link: " + user.name);
//    	if (isOrganisationLink(user, collection)) {
//    		Logger.info("remove link: " + user.name);
//    		user.field_affiliation = "";
//        	Ebean.update(user);
//    	}
//	}
    
    /**
     * This method implements administration for users associated with particular collection.
     * @return
     */
//    public static Result admin() {
//    	Result res = null;
//        String save = getFormParam(Const.SAVE);
//        if (save != null) {
//        	DCollection collection = null;
//            boolean isExisting = true;
//            try {
//               	collection = DCollection.findByUrl(getFormParam(Const.URL));
//               	
//		        List<User> userList = User.findAll();
//		        Iterator<User> userItr = userList.iterator();
//		        while (userItr.hasNext()) {
//		        	User user = userItr.next();
//	                if (getFormParam(user.name) != null) {
////                		Logger.info("getFormParam(user.name): " + getFormParam(user.name) + " " + user.name);
//		                boolean userFlag = Utils.getNormalizeBooleanString(getFormParam(user.name));
//		                if (userFlag) {
//		                	addLink(user, collection); 
//		                } else {
//		                	removeLink(user, collection); 
//		                }
//	                } else {
//	                	removeLink(user, collection); 	                	
//	                }
//		        }
//            } catch (Exception e) {
//            	Logger.info("User not existing exception");
//            }
//	        res = redirect(routes.CollectionEdit.admin(collection.url));
//        } else {
//        	res = ok();
//        }
//        return res;
//    }
    
}

