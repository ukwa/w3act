package controllers;

import play.*;
import play.mvc.*;
import play.data.*;
import static play.data.Form.*;

import java.util.*;

import models.*;

import views.html.*;

/**
 * Manage sites.
 */
@Security.Authenticated(Secured.class)
public class Targets extends Controller {
  
    /**
     * Display the sites.
     */
    public static Result index() {
        return ok(
            targets.render(
                "Target", User.find.byId(request().username()), models.Target.findInvolving(request().username())
            )
        );
    }

    // -- Sites

    /**
     * Add a site.
     */
    public static Result add() {
        Target newSite = Target.create(
            "New target", 
            form().bindFromRequest().get("group"),
            request().username()
        );
        return null;
    }
    
    /**
     * Rename a site.
     */
    public static Result rename(Long site) {
        if(Secured.isMemberOf(site)) {
            return ok(
                Target.rename(
                    site, 
                    form().bindFromRequest().get("name")
                )
            );
        } else {
            return forbidden();
        }
    }
    
    /**
     * Delete a site.
     */
    public static Result delete(Long site) {
        if(Secured.isMemberOf(site)) {
            Target.find.ref(site).delete();
            return ok();
        } else {
            return forbidden();
        }
    }

    // -- Site groups

    /**
     * Add a new site group.
     */
    public static Result addGroup() {
        return ok();
    }
  
    /**
     * Delete a site group.
     */
    public static Result deleteGroup(String group) {
        Target.deleteInFolder(group);
        return ok();
    }
  
    /**
     * Rename a site group.
     */
    public static Result renameGroup(String group) {
        return ok(
            Target.renameFolder(group, form().bindFromRequest().get("name"))
        );
    }
  
    // -- Members
  
    /**
     * Add a site member.
     */
    public static Result addUser(Long site) {
        if(Secured.isMemberOf(site)) {
            Target.addMember(
                site,
                form().bindFromRequest().get("user")
            );
            return ok();
        } else {
            return forbidden();
        }
    }
  
    /**
     * Remove a site member.
     */
    public static Result removeUser(Long site) {
        if(Secured.isMemberOf(site)) {
            Target.removeMember(
                site,
                form().bindFromRequest().get("user")
            );
            return ok();
        } else {
            return forbidden();
        }
    }
  
}

