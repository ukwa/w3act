package controllers;


import java.util.List;

import models.LookupEntry;
import models.Target;
import models.User;
import play.Logger;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Result;
import play.mvc.Security;
import uk.bl.Const;
import uk.bl.api.Utils;
import uk.bl.exception.WhoisException;
import uk.bl.scope.Scope;
import uk.bl.scope.WhoIsData;
import views.html.whois.index;
import views.html.whois.results;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.SqlRow;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * Manage passwords.
 */
@Security.Authenticated(Secured.class)
public class CheckWhoIs extends AbstractController {
   
    /**
     * Add new user entry.
     * @param user
     * @return
     */
    @BodyParser.Of(BodyParser.Json.class)
    public static Result filterByJson(String name) {
        JsonNode jsonData = null;
        if (name != null) {
	        List<User> users = User.filterByName(name);
	        jsonData = Json.toJson(users);
        }
        return ok(jsonData);
    }
    
    /**
     * Display the user edit panel for this URL.
     */
    public static Result index() {
		Integer ukRegistrantCount = Target.findWhoIsCount(true);
		Integer nonUKRegistrantCount = Target.findWhoIsCount(false); 
    	WhoIsData whoIsData = new WhoIsData(null, null, ukRegistrantCount, nonUKRegistrantCount, 0);
        return ok(index.render(User.findByEmail(request().username()), whoIsData)); 
    }
    
    /**
     * This method saves changed password in the same object. The "version" field in the User object
     * contains the timestamp of the change. 
     * @return
     * @throws WhoisException 
     */
    public static Result check(int total) throws WhoisException {
//    	Result res = null;
    	Logger.info("CheckWhoIs controller check() " + total);
        String check = getFormParam("check");
        if (check != null || total > 0) {
        	Logger.info("target number to check: " + getFormParam(Const.NUMBER));
//        	int number = 50;
            if (getFormParam(Const.NUMBER) != null 
            		&& getFormParam(Const.NUMBER).length() > 0
            		&& Utils.isNumeric(getFormParam(Const.NUMBER))) {
            	total = Integer.valueOf(getFormParam(Const.NUMBER));
            }
//        	new WhoIsThread("WhoIs check").start();
//        	Target target = new Target();
//        	WhoIsThread notifier = new WhoIsThread(target, number);
//    		Thread notifierThread = new Thread(notifier, "notifierWhoIsThread");
//    		notifierThread.start();
    		

//    		Visual feedback should include:
//		    most recent domain checked (and when)
//		    the domain least recently checked (and when)
    		
//    		List of all targets sorted by date of last WhoIs check
//    		showing the least recently checked first.
        	
//	        res = find.where().eq(Const.ACTIVE, true).orderBy(Const.LAST_UPDATE + " " + Const.DESC).setMaxRows(number).findList();
			
//    		StringBuilder lookupSql = new StringBuilder("select l.name as lookup_name, t.field_url, t.last_update as target_date, l.last_update as lookup_date, (l.last_update::timestamp - t.last_update::timestamp) as diff from target t, lookup_entry l where t.field_url = l.name order by diff desc");
//    		
//    		if (total > 0) {
//    			lookupSql.append(" limit ").append(total);
//    		}
//    		lookupSql.append(";");
    		
//    		List<SqlRow> rows = Ebean.createSqlQuery(lookupSql.toString()).findList();
    		
//    		for (SqlRow r : rows) {
//    			Logger.info(r.getString("lookup_name") + " " +  r.getTimestamp("target_date") + " "  + r.getTimestamp("lookup_date"));
//    		}
//        	res = ok(infomessage.render("You have successfully checked the current status of WhoIs service."));

			WhoIsData whoIsData = Scope.checkWhois(total);

			// need to match up with lookupentry and get the last_update
        	return ok(
        			results.render(User.findByEmail(request().username()), whoIsData)
        	);
//  			flash("message", "You have started a check for the current status of the WhoIs service. Please refresh this page using F5 in order to see changes.");
//  			return index();
        } 
        return index();
    }    

}

