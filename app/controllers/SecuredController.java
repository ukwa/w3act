package controllers;

import play.mvc.*;
import play.mvc.Http.*;

public class SecuredController extends Security.Authenticator {
    
    @Override
    public String getUsername(Context ctx) {
        return ctx.session().get("email");
    }
    
    @Override
    public Result onUnauthorized(Context ctx) {
    	ctx.flash().put("url", "GET".equals(ctx.request().method()) ? ctx.request().uri() : "/");
        return redirect(routes.ApplicationController.login());
    }
    
    // Access rights
    
    public static boolean isMemberOf(Long project) {
        return true;
    }
    
    public static boolean isOwnerOf(Long task) {
        return true;
    }
    
}