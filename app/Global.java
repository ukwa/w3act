import play.Application;
import play.GlobalSettings;
import uk.bl.db.DataImport;
import play.mvc.*;
import play.mvc.Http.*;
import play.libs.F.*;
import static play.mvc.Results.*;

public class Global extends GlobalSettings {
    
    public void onStart(Application app) {
    	// should run in background and return view
    	DataImport.INSTANCE.insert();
    }
    
    public Promise<SimpleResult> onError(RequestHeader request, Throwable t) {
        return Promise.<SimpleResult>pure(internalServerError(
            views.html.errorPage.render(t)
        ));
    }
    
    public Promise<SimpleResult> onHandlerNotFound(RequestHeader request) {
        return Promise.<SimpleResult>pure(notFound(
            views.html.notFoundPage.render(request.uri())
        ));
    }
    
    public Promise<SimpleResult> onBadRequest(RequestHeader request, String error) {
        return Promise.<SimpleResult>pure(badRequest("Please don't try to hack the URI!"));
    }
}