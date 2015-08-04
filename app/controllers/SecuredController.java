package controllers;

import models.User;
import play.Logger;
import play.mvc.*;
import play.mvc.Http.*;

public class SecuredController extends Security.Authenticator {
    
    @Override
    public String getUsername(Context ctx) {
        return ctx.session().get("email");
    }
    
    @Override
    public Result onUnauthorized(Context ctx) {
    	// Store the original URL in the flash scope so it can be stored in the form:
    	String requestUrl = "GET".equals(ctx.request().method()) ? ctx.request().uri() : routes.ApplicationController.index().url();
    	Logger.info("onUnauthorised controller setting requestUrl = "+ requestUrl);
    	ctx.flash().put("url", requestUrl );
        return redirect(routes.ApplicationController.login());
    }
    
    // Access rights
    
    public static boolean isSysAdmin(String email) {
    	User user = User.findByEmail(email);
        return user.isSysAdmin();
    }

    public static boolean isArchivist(String email) {
    	User user = User.findByEmail(email);
        return user.isArchivist();
    }

}