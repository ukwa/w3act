
import play.Application;
import play.GlobalSettings;
import play.Logger;
import uk.bl.db.DataImport;
import play.mvc.*;
import play.mvc.Http.*;
import play.libs.F.*;
import static play.mvc.Results.*;

public class Global extends GlobalSettings {
    
    public void onStart(Application app) {
    	// should run in background and return view
    	Boolean useAccounts = play.Play.application().configuration().getBoolean("use.accounts");
    	if (useAccounts) {
	    	DataImport.INSTANCE.insert();
    	}
    }
    
    @Override
    public Promise<SimpleResult> onError(RequestHeader request, Throwable t) {
        return Promise.<SimpleResult>pure(internalServerError(
            views.html.errorPage.render(t)
        ));
    }
    
    @Override
    public Promise<SimpleResult> onHandlerNotFound(RequestHeader request) {
        return Promise.<SimpleResult>pure(notFound(
            views.html.notFoundPage.render(request.uri())
        ));
    }
    
    @Override
    public Promise<SimpleResult> onBadRequest(RequestHeader request, String error) {
    	Logger.debug("error: " + error);
        return Promise.<SimpleResult>pure(badRequest(
        		views.html.errorPage.render(new Throwable("Bad Request"))
        ));
    }
}