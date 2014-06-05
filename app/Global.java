import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import models.Taxonomy;
import play.Application;
import play.GlobalSettings;
import uk.bl.Const;
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
    
    static class InitialData {
        
	    /**
	     * This method removes from taxonomy list old subject taxonomies.
	     * @param taxonomyList
	     * @return
	     */
	    public static List<Taxonomy> cleanUpTaxonomies(List<Object> taxonomyList) {
	    	List<Taxonomy> res = new ArrayList<Taxonomy>();
            Iterator<Object> taxonomyItr = taxonomyList.iterator();
            while (taxonomyItr.hasNext()) {
            	Taxonomy taxonomy = (Taxonomy) taxonomyItr.next();
            	if (!(taxonomy.ttype.equals(Const.SUBJECT) && (taxonomy.parent == null || taxonomy.parent.length() == 0)) 
            			&& !(taxonomy.ttype.equals(Const.SUBSUBJECT) && taxonomy.parent.contains(Const.ACT_URL))) {
            		res.add(taxonomy);
            	}
            }
            return res;
	    }
    }
    
}