import java.util.List;

import play.Application;
import play.GlobalSettings;
import play.Logger;
import uk.bl.Const;
import uk.bl.api.JsonUtils;
import uk.bl.db.DataImport;
import play.mvc.*;
import play.mvc.Http.*;
import play.libs.F.*;
import static play.mvc.Results.*;

public class Global extends GlobalSettings {
    
    public void onStart(Application app) {
    	// should run in background and return view
    	Boolean dataImport = play.Play.application().configuration().getBoolean("application.data.import");
    	Logger.info("dataImport: " + dataImport);
//        List<Object> allInstances = JsonUtils.getDrupalData(Const.NodeType.INSTANCE);

    	if (dataImport) {
    		DataImport.INSTANCE.insert();
    	}
    }
    
    public Promise<Result> onError(RequestHeader request, Throwable t) {
        return Promise.<Result>pure(internalServerError(
            views.html.errorPage.render(t)
        ));
    }
    
    public Promise<Result> onHandlerNotFound(RequestHeader request) {
        return Promise.<Result>pure(notFound(
            views.html.notFoundPage.render(request.uri())
        ));
    }
    
    public Promise<Result> onBadRequest(RequestHeader request, String error) {
        return Promise.<Result>pure(badRequest("Please don't try to hack the URI!"));
    }
}