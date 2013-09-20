package controllers;

import play.*;
import play.mvc.*;
import play.data.*;
import static play.data.Form.*;

import java.util.*;

import models.*;

import views.html.*;

/**
 * Manage dcollections.
 */
@Security.Authenticated(Secured.class)
public class DCollections extends Controller {
  
    /**
     * Display the dcollections.
     */
    public static Result index() {
        return ok(
            dcollectionsview.render(
                "Collections", User.find.byId(request().username()), models.DCollection.findInvolving(request().username())
            )
        );
    }

    // -- DCollections

    /**
     * Add a dcollection.
     */
    public static Result add() {
        DCollection newDCollection = DCollection.create(
            "New dcollection", 
            form().bindFromRequest().get("group"),
            request().username()
        );
        return ok();
    }
    
    /**
     * Rename a dcollection.
     */
    public static Result rename(Long dcollection) {
        if(Secured.isMemberOf(dcollection)) {
            return ok(
                DCollection.rename(
                    dcollection, 
                    form().bindFromRequest().get("name")
                )
            );
        } else {
            return forbidden();
        }
    }
    
    /**
     * Delete a dcollection.
     */
    public static Result delete(Long dcollection) {
        if(Secured.isMemberOf(dcollection)) {
            DCollection.find.ref(dcollection).delete();
            return ok();
        } else {
            return forbidden();
        }
    }

    // -- DCollection groups

    /**
     * Add a new dcollection group.
     */
    public static Result addGroup() {
        return ok(
            //group.render("New group", new ArrayList<DCollection>())
        );
    }
  
    /**
     * Delete a dcollection group.
     */
    public static Result deleteGroup(String group) {
        DCollection.deleteInFolder(group);
        return ok();
    }
  
    /**
     * Rename a dcollection group.
     */
    public static Result renameGroup(String group) {
        return ok(
            DCollection.renameFolder(group, form().bindFromRequest().get("name"))
        );
    }
  
    // -- Members
  
    /**
     * Add a dcollection member.
     */
    public static Result addUser(Long dcollection) {
        if(Secured.isMemberOf(dcollection)) {
            DCollection.addMember(
                dcollection,
                form().bindFromRequest().get("user")
            );
            return ok();
        } else {
            return forbidden();
        }
    }
  
    /**
     * Remove a dcollection member.
     */
    public static Result removeUser(Long dcollection) {
        if(Secured.isMemberOf(dcollection)) {
            DCollection.removeMember(
                dcollection,
                form().bindFromRequest().get("user")
            );
            return ok();
        } else {
            return forbidden();
        }
    }
  
}

