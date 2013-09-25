package controllers;

import play.*;
import play.mvc.*;
import play.data.*;
import static play.data.Form.*;

import java.util.*;

import models.*;

import views.html.*;

/**
 * Manage targets.
 */
@Security.Authenticated(Secured.class)
public class Targets extends Controller {
  
    /**
     * Display the targets.
     */
    public static Result index() {
        return ok(
            targets.render(
                "Targets", User.find.byId(request().username()), models.Target.findInvolving(request().username())
            )
        );
    }

    // -- targets

    /**
     * Add a target.
     */
    public static Result add() {
        Target newTarget = Target.create(
            "New target", 
            form().bindFromRequest().get("group"),
            request().username()
        );
        return null;
    }
    
    /**
     * Rename a target.
     */
    public static Result rename(Long target) {
        if(Secured.isMemberOf(target)) {
            return ok(
                Target.rename(
                    target, 
                    form().bindFromRequest().get("name")
                )
            );
        } else {
            return forbidden();
        }
    }
    
    /**
     * Delete a target.
     */
    public static Result delete(Long target) {
        if(Secured.isMemberOf(target)) {
            Target.find.ref(target).delete();
            return ok();
        } else {
            return forbidden();
        }
    }

    // -- target groups

    /**
     * Add a new target group.
     */
    public static Result addGroup() {
        return ok();
    }
  
    /**
     * Delete a target group.
     */
    public static Result deleteGroup(String group) {
        Target.deleteInFolder(group);
        return ok();
    }
  
    /**
     * Rename a target group.
     */
    public static Result renameGroup(String group) {
        return ok(
            Target.renameFolder(group, form().bindFromRequest().get("name"))
        );
    }
  
    // -- Members
  
    /**
     * Add a target member.
     */
    public static Result addUser(Long target) {
        if(Secured.isMemberOf(target)) {
            Target.addMember(
                target,
                form().bindFromRequest().get("user")
            );
            return ok();
        } else {
            return forbidden();
        }
    }
  
    /**
     * Remove a target member.
     */
    public static Result removeUser(Long target) {
        if(Secured.isMemberOf(target)) {
            Target.removeMember(
                target,
                form().bindFromRequest().get("user")
            );
            return ok();
        } else {
            return forbidden();
        }
    }
  
}

