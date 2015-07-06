import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import akka.actor.ActorRef;
import akka.actor.Props;
import models.Role;
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
    	Boolean activeCrawling = play.Play.application().configuration().getBoolean("application.activeCrawling");
    	Logger.debug("dataImport: " + dataImport);
//        List<Object> allInstances = JsonUtils.getDrupalData(Const.NodeType.INSTANCE);

    	if (dataImport != null && dataImport) {
    		if (omitInstances == null) omitInstances = false;
    		DataImport.INSTANCE.insert(!omitInstances);
    	}
		Role closed = Role.findByName("closed");
		Logger.debug("closed found: " + closed);
		if (closed == null) {
			Role newClosed = new Role();
			newClosed.name = "closed";
			newClosed.save();
		}
    	
		if (activeCrawling != null && activeCrawling) {
	    	ActorRef crawlActor = Akka.system().actorOf(Props.create(CrawlActor.class));
			Akka.system().scheduler().schedule(
					Duration.create(millisecondsUntilMidnight(), TimeUnit.MILLISECONDS), //Initial delay
					Duration.create(24, TimeUnit.HOURS),     //Frequency 24 hours
					crawlActor,
					new CrawlActor.CrawlMessage(),
					Akka.system().dispatcher(),
					null
	    	);
		}
    	/*Boolean useAccounts = play.Play.application().configuration().getBoolean("use.accounts");
    	if (useAccounts) {
	    	DataImport.INSTANCE.importPermissions();
	    	DataImport.INSTANCE.importRoles();
			DataImport.INSTANCE.importAccounts();
    	}*/
    }
    
    public long millisecondsUntilMidnight() {
    	Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH, 1);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        long milliseconds = c.getTimeInMillis()-System.currentTimeMillis();
        Logger.debug("First crawl will start in " + milliseconds + "ms");
        return milliseconds;
    }
    
    @Override
    public Promise<Result> onError(RequestHeader request, Throwable t) {
        return Promise.<Result>pure(internalServerError(
            views.html.errorPage.render(t)
        ));
    }
    
    @Override
    public Promise<Result> onHandlerNotFound(RequestHeader request) {
        return Promise.<Result>pure(notFound(
            views.html.notFoundPage.render(request.uri())
        ));
    }
    
    @Override
    public Promise<Result> onBadRequest(RequestHeader request, String error) {
    	Logger.debug("error: " + error);
        return Promise.<Result>pure(badRequest(
        		views.html.errorPage.render(new Throwable("Bad Request"))
        ));
    }
}
