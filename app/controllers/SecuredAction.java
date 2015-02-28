package controllers;

import models.User;
import play.Logger;
import play.libs.F;
import play.libs.F.Promise;
import play.mvc.Action;
import play.mvc.Http.Context;
import play.mvc.SimpleResult;
import play.mvc.Http.HeaderNames;

import uk.bl.api.PasswordHash;
import uk.bl.api.Base64;

public class SecuredAction extends Action.Simple {

	@Override
	public Promise<SimpleResult> call(Context ctx) throws Throwable {
		
    	String[] authorization = ctx.request().headers().get(HeaderNames.AUTHORIZATION);
    	
    	String auth = authorization[0].substring(6);
    	Logger.debug("auth: " + auth);
    	
        final byte[] decodedAuth = Base64.decode(auth);
        final String[] credentials = new String(decodedAuth, "UTF-8").split(":");
        
        if (credentials == null || credentials.length != 2) {
    		return F.Promise.pure((SimpleResult) unauthorized("unauthorized"));
        }
        
        String email = credentials[0];
        String password = credentials[1];

        User user = User.findByEmail(email.toLowerCase());
		String userPassword = user.password;
		boolean result = PasswordHash.validatePassword(password, userPassword);
        
		Logger.debug("RESULT: " + result);
		if (result) {
            ctx.request().setUsername(user.email);
			return delegate.call(ctx);
		}
		return F.Promise.pure((SimpleResult) unauthorized("unauthorized"));
	}
}
