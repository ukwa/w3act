
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
    	Boolean dataImport = play.Play.application().configuration().getBoolean("application.data.import");
    	Logger.debug("dataImport: " + dataImport);
//        List<Object> allInstances = JsonUtils.getDrupalData(Const.NodeType.INSTANCE);

    	if (dataImport) {
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
        return Promise.<SimpleResult>pure(badRequest("Please don't try to hack the URI!"));
    }
}