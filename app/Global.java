import java.util.concurrent.TimeUnit;

import akka.actor.ActorRef;
import akka.actor.Props;
import play.Application;
import play.GlobalSettings;
import play.Logger;
import scala.concurrent.duration.Duration;
import uk.bl.db.DataImport;
import uk.bl.crawling.CrawlActor;
import play.mvc.*;
import play.mvc.Http.*;
import play.libs.Akka;
import play.libs.F.*;
import static play.mvc.Results.*;

public class Global extends GlobalSettings {
    
    public void onStart(Application app) {
    	// should run in background and return view
    	Boolean dataImport = play.Play.application().configuration().getBoolean("application.data.import");
    	Boolean omitInstances = play.Play.application().configuration().getBoolean("application.data.omit_instances");
    	Logger.info("dataImport: " + dataImport);
//        List<Object> allInstances = JsonUtils.getDrupalData(Const.NodeType.INSTANCE);

    	if (dataImport != null && dataImport) {
    		if (omitInstances == null) omitInstances = false;
    		DataImport.INSTANCE.insert(!omitInstances);
    	}
    	
    	ActorRef crawlActor = Akka.system().actorOf(Props.create(CrawlActor.class));
		Akka.system().scheduler().schedule(
				Duration.create(0, TimeUnit.MILLISECONDS), //Initial delay 0 milliseconds
				Duration.create(24, TimeUnit.HOURS),     //Frequency 24 hours
				crawlActor,
				new CrawlActor.StartMessage(),
				Akka.system().dispatcher(),
				null
    	);
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