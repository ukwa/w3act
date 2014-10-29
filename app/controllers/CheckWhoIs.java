package controllers;


import java.util.Date;
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
import uk.bl.api.WhoIsThread;
import uk.bl.exception.WhoisException;
import uk.bl.scope.Scope;
import views.html.whois.index;
import views.html.whois.results;
import views.html.infomessage;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Query;
import com.avaje.ebean.RawSql;
import com.avaje.ebean.RawSqlBuilder;
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
        return ok(index.render(User.findByEmail(request().username()))); 
    }
    
	/**
	 * This method prepares Collection form for sending info message
	 * about errors 
	 * @return edit page with form and info message
	 */
	public static Result info() {
      	return ok(
  			index.render(User.findByEmail(request().username()))
        );
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
    		
			Scope.checkWhoisThread(total);

//    		Visual feedback should include:
//		    most recent domain checked (and when)
//		    the domain least recently checked (and when)
    		
    		LookupEntry entry = LookupEntry.find.where().orderBy("lastUpdate desc").setMaxRows(1).findUnique();
    		String domainMostRecentChecked = entry.name;
    		
        	String sql = "select count(name), name from lookup_entry group by name order by count(name) asc limit 1";
        	SqlRow row =  Ebean.createSqlQuery(sql).findUnique();
    		
        	String leastName = row.getString("name");
        	
        	String domainLeastChecked = leastName;
        	
//    		LookupEntry domainLeastChecked = LookupEntry.findLeastChecked();
    		Logger.info("LookupEntry mostRecentChecked: " + domainMostRecentChecked);
    		Logger.info("LookupEntry domainLeastChecked: " + domainLeastChecked);

//        	res = ok(infomessage.render("You have successfully checked the current status of WhoIs service."));
        	return ok(
        			results.render(User.findByEmail(request().username()), domainMostRecentChecked, domainLeastChecked)
        	);
//  			flash("message", "You have started a check for the current status of the WhoIs service. Please refresh this page using F5 in order to see changes.");
//  			return index();
        } 
        return index();
    }    

}

