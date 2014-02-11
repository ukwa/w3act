package controllers;

import play.mvc.*;
import static play.data.Form.*;

import models.*;

/**
 * Manage dcollections.
 */
@Deprecated
@Security.Authenticated(Secured.class)
public class DCollections extends AbstractController {
  
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

