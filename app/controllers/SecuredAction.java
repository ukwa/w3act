package controllers;

import models.User;
import play.Logger;
import play.libs.F;
import play.libs.F.Promise;
import play.mvc.Action;
import play.mvc.Http.Context;
import play.mvc.Result;
import play.mvc.Http.HeaderNames;
import uk.bl.api.PasswordHash;
import uk.bl.api.Base64;

public class SecuredAction extends Action.Simple {

	@Override
	public Promise<Result> call(Context ctx) throws Throwable {
		
    	String[] authorization = ctx.request().headers().get(HeaderNames.AUTHORIZATION);
    	
    	if (authorization == null) {
    		return F.Promise.pure((Result) unauthorized("unauthorized"));
    	}
    	String auth = authorization[0].substring(6);
    	Logger.trace("SecuredAction auth: " + auth);
	    	
        final byte[] decodedAuth = Base64.decode(auth);
        final String[] credentials = new String(decodedAuth, "UTF-8").split(":");
        
        if (credentials == null || credentials.length != 2) {
    		return F.Promise.pure((Result) unauthorized("unauthorized"));
        }
        
        String email = credentials[0];
        String password = credentials[1];
        
		Logger.trace("SecuredAction credentials: "+email+" "+password);
		
		if( Logger.isTraceEnabled() ){
			for( User u : User.findAll() ) {
				Logger.info("U: "+u);
			}
		}
		
        User user = User.findByEmail(email.toLowerCase());
		String userPassword = null;
		if( user != null ) userPassword = user.password;
		// And is the PW okay?
		boolean result = false;
		if( userPassword != null ) {
			result = PasswordHash.validatePassword(password, userPassword);
		}
        
		Logger.trace("SecuredAction result: " + result);
		if (result) {
            ctx.request().setUsername(user.email);
			return delegate.call(ctx);
		}
		return F.Promise.pure((Result) unauthorized("unauthorized"));
	}
}
