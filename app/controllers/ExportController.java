package controllers;

import static play.data.Form.form;

import java.util.Iterator;
import java.util.List;

import models.LookupEntry;
import models.User;

import org.apache.commons.lang3.StringUtils;

import play.Logger;
import play.data.DynamicForm;
import play.mvc.Result;
import play.mvc.Security;
import uk.bl.Const;
import uk.bl.api.Utils;
import views.html.settings.export;

/**
 * Manage targets.
 */
@Security.Authenticated(SecuredController.class)
public class ExportController extends AbstractController {
  
    /**
     * Display the export.
     */
    public static Result index() {
		return ok(
	            export.render("Export", User.findByEmail(request().username()))
	        );
    }
    
    /**
     * This method exports selected lookups to CSV file.
     * @param list of Target objects
     * @return
     */
    public static Result exportLookup(List<LookupEntry> lookupList) {
    	Logger.debug("export lookups size: " + lookupList.size());

    
    	StringBuilder sw = new StringBuilder();

        
		sw.append(Const.URL);
 	    sw.append(Const.CSV_SEPARATOR);
		sw.append(Const.VALUE);
		sw.append(Const.CSV_LINE_END);
 	    if (lookupList != null && lookupList.size() > 0) {
 	    	Iterator<LookupEntry> itr = lookupList.iterator();
 	    	while (itr.hasNext()) {
 	    		LookupEntry lookupEntry = (LookupEntry) itr.next();
 	    		if (lookupEntry.name != null) {
 	    			sw.append(lookupEntry.name);
 	    			sw.append(Const.CSV_SEPARATOR);
 	    			Logger.debug("add entry: " + lookupEntry.name + ", obj: "+ lookupEntry);
 	    			Logger.debug("add entry: " + lookupEntry.name + ", value: "+ lookupEntry.scopevalue.toString() + ", " + 
 	    					Utils.INSTANCE.getNormalizeBooleanString(String.valueOf(lookupEntry.scopevalue.booleanValue())));
 	    			if (lookupEntry.scopevalue) {
// 	 	    			if (lookupEntry.scopevalue.booleanValue()) {
 	    				sw.append(Const.YES);
 	    			} else {
 	    				sw.append(Const.NO);
 	    			}
 	    			sw.append(Const.CSV_LINE_END);
 	    		}
 	    	}
 	    }
		String csvData = sw.toString();
		response().setContentType("text/csv; charset=utf-8");
		response().setHeader("Content-disposition","attachment; filename=\"" + Const.EXPORT_LOOKUP_FILE + "\"");
		return ok(csvData);
    }
    
    /**
     * This method enables searching for given URL and redirection in order to add new entry
     * if required.
     * @return
     */
    public static Result search() {
    	
    	DynamicForm form = form().bindFromRequest();
    	String action = form.get(Const.ACTION);
    	Logger.debug("action: " + action);
    	if (StringUtils.isEmpty(action)) {
    		return badRequest("You must provide a valid action");
    	} else {
    		if (Const.EXPORT_LOOKUPS.equals(action)) {
    			return exportLookup(LookupEntry.findAll());    			
    		} 
    	}
    	return GO_EXPORT_HOME;
    }
            
    public static Result GO_EXPORT_HOME = redirect(
            routes.ExportController.index()
        );
    
}

