package controllers;

import static play.data.Form.form;

import java.io.StringWriter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.ExpressionList;
import com.avaje.ebean.Page;
import com.fasterxml.jackson.databind.JsonNode;

import models.CrawlPermission;
import models.Organisation;
import models.Target;
import models.User;
import models.ContactPerson;
import models.MailTemplate;
import models.CommunicationLog;
import play.Logger;
import play.data.DynamicForm;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Result;
import play.mvc.Security;
import uk.bl.Const;
import uk.bl.api.Utils;
import uk.bl.scope.EmailHelper;
import views.html.contactpersons.*;
import views.html.mailtemplates.*;
import views.html.reports.*;
import views.html.licence.*;
import views.html.refusals.*;
import views.html.communicationlogs.*;

import java.util.*;

import org.apache.commons.lang3.StringUtils;

/**
 * Manage reports.
 */
@Security.Authenticated(Secured.class)
public class Reports extends AbstractController {
  
    /**
     * Display the report.
     */
    public static Result index() {
        List<CrawlPermission> resList = 
        		processFilterReports("", "", Const.CrawlPermissionStatus.PENDING.name(), "");
        List<CrawlPermission> resListGranted = 
        		processFilterReports("", "", Const.CrawlPermissionStatus.GRANTED.name(), "");
        List<CrawlPermission> resListRefused = 
        		processFilterReports("", "", Const.CrawlPermissionStatus.REFUSED.name(), "");
        return ok(
                reports.render(
                    "Reports", User.find.byId(request().username()), resList, 
                    resListGranted, resListRefused, "", "", "", "", ""
                )
            );
    }

    /**
     * This method enables searching for given URL and redirection in order to add new entry
     * if required.
     * @return
     */
    public static Result search() {
    	DynamicForm form = form().bindFromRequest();
    	String action = form.get("action");
    	Logger.info("action: " + action);
    	String curator_name = form.get(Const.AUTHOR);
    	String curator = "";
    	if (curator_name != null && !curator_name.toLowerCase().equals(Const.NONE)) {
    		try {
    			curator = User.findByName(curator_name).url;
    		} catch (Exception e) {
    			Logger.info("Can't find curator for name: " + curator_name + ". " + e);
    		}
    	} 
    	String organisation_name = form.get(Const.FIELD_NOMINATING_ORGANISATION);
    	String organisation = "";
    	if (organisation_name != null && !organisation_name.toLowerCase().equals(Const.NONE) 
    			&& !organisation_name.equals(Const.ALL_AGENCIES)) {
    		try {
    			organisation = Organisation.findByTitle(organisation_name).url;
    		} catch (Exception e) {
    			Logger.info("Can't find organisation for title: " + organisation_name + ". " + e);
    		}
    	} 
    	String request_name = form.get(Const.REQUEST);
    	String request = "";
    	if (request_name != null && !request_name.toLowerCase().equals(Const.NONE) 
    			&& !request_name.toLowerCase().equals(Const.ALL)) {
   			request = request_name;
    	} 

        List<CrawlPermission> resList = 
        		processFilterReports(curator, organisation, Const.CrawlPermissionStatus.PENDING.name(), request);
        List<CrawlPermission> resListGranted = 
        		processFilterReports(curator, organisation, Const.CrawlPermissionStatus.GRANTED.name(), request);
        List<CrawlPermission> resListRefused = 
        		processFilterReports(curator, organisation, Const.CrawlPermissionStatus.REFUSED.name(), request);

    	if (StringUtils.isEmpty(action)) {
    		return badRequest("You must provide a valid action");
    	} else {
    		if (Const.EXPORT.equals(action)) {
				Logger.info("export size: " + resList.size());
    			export(resList);
    			return ok(
                		reports.render(
                            "Reports", User.find.byId(request().username()), resList, resListGranted,
                            resListRefused, curator, organisation, "", "", request
                        )
                    );
    		}
    		else if (Const.SEARCH.equals(action)) {
    			return ok(
                		reports.render(
                            "Reports", User.find.byId(request().username()), resList, resListGranted,
                            resListRefused, curator, organisation, "", "", request
                        )
                    );
		    } else {
		    	return badRequest("This action is not allowed");
		    }
    	}
    }	   
    
        
    /**
     * This method exports selected crawl permissions to CSV file.
     * @param list of Target objects
     * @return
     */
    public static void export(List<CrawlPermission> permissionList) {
    	Logger.info("export() permissionList size: " + permissionList.size());

        StringWriter sw = new StringWriter();
	    sw.append("Target title");
		sw.append(Const.CSV_SEPARATOR);
	    sw.append("Target URL");
		sw.append(Const.CSV_SEPARATOR);
	    sw.append("Date requested");
		sw.append(Const.CSV_SEPARATOR);
        sw.append(Const.CSV_LINE_END);
 	    
 	    if (permissionList != null && permissionList.size() > 0) {
 	    	Iterator<CrawlPermission> itr = permissionList.iterator();
 	    	while (itr.hasNext()) {
 	    		CrawlPermission permission = itr.next();
	    		sw.append(Target.findByUrl(permission.target).title);
		 	    sw.append(Const.CSV_SEPARATOR);
	    		sw.append(permission.target);
		 	    sw.append(Const.CSV_SEPARATOR);
	    		sw.append(permission.licenseDate);
		 	    sw.append(Const.CSV_SEPARATOR);
	 	 	    sw.append(Const.CSV_LINE_END);
 	    	}
 	    }
    	Utils.generateCsvFile(Const.EXPORT_LICENCE_FILE, sw.toString());
    }
            	
    /**
     * This method applies filters to the list of crawl reports.
     * @param curator The curator URL
     * @param organisation The organisation URL
     * @param status The status of the report workflow
     * @param request The request type (first request/folloup/all)
     * @return
     */
    public static List<CrawlPermission> processFilterReports(String curator, String organisation, 
    		String status, String request) {
    	boolean isProcessed = false;
    	ExpressionList<CrawlPermission> exp = CrawlPermission.find.where();
    	List<CrawlPermission> res = new ArrayList<CrawlPermission>();
    	if (status != null && !status.toLowerCase().equals(Const.NONE) && status.length() > 0) {
    		Logger.info("status: " + status);
    		exp = exp.eq(Const.STATUS, status);
    		isProcessed = true;
    	} 
    	if (curator != null && !curator.toLowerCase().equals(Const.NONE) && curator.length() > 0) {
    		Logger.info("curator: " + curator);
    		exp = exp.eq(Const.CREATOR_USER, curator);
    		isProcessed = true;
    	} 
    	if (request != null && !request.toLowerCase().equals(Const.ALL) && request.length() > 0) {
    		Logger.info("request: " + request);
    		if (request.equals(Const.RequestTypes.FOLLOW_UP.name())) {
    			exp = exp.eq(Const.REQUEST_FOLLOW_UP, true);
    		} else {
    			exp = exp.eq(Const.REQUEST_FOLLOW_UP, null);
    		}
    		isProcessed = true;
    	} 
    	res = exp.query().findList();
    	Logger.info("Expression list size: " + res.size() + ", isProcessed: " + isProcessed);

        if (!isProcessed) {
    		res = models.CrawlPermission.filterByStatus(status);
    	}
        return res;
    }
              
    /**
     * Display the report.
     */
    public static Result summary() {
        List<CrawlPermission> resList = processFilterReports("", "", Const.CrawlPermissionStatus.PENDING.name(), "");
        List<CrawlPermission> resListGranted = processFilterReports("", "", Const.CrawlPermissionStatus.GRANTED.name(), "");
        List<CrawlPermission> resListRefused = processFilterReports("", "", Const.CrawlPermissionStatus.REFUSED.name(), "");
        return ok(
                reports.render(
                    "Reports", User.find.byId(request().username()), resList, resListGranted, resListRefused, "", "", "", "", ""
                )
            );
    }

    public static Result openLicences() {
        List<CrawlPermission> resList = processFilterReports("", "", Const.CrawlPermissionStatus.PENDING.name(), "");
        List<CrawlPermission> resListGranted = processFilterReports("", "", Const.CrawlPermissionStatus.GRANTED.name(), "");
        List<CrawlPermission> resListRefused = processFilterReports("", "", Const.CrawlPermissionStatus.REFUSED.name(), "");
        return ok(
                reports.render(
                    "Reports", User.find.byId(request().username()), resList, resListGranted, resListRefused, "", "", "", "", ""
                )
            );
    }

    public static Result recordCreation() {
        List<CrawlPermission> resList = processFilterReports("", "", Const.CrawlPermissionStatus.PENDING.name(), "");
        List<CrawlPermission> resListGranted = processFilterReports("", "", Const.CrawlPermissionStatus.GRANTED.name(), "");
        List<CrawlPermission> resListRefused = processFilterReports("", "", Const.CrawlPermissionStatus.REFUSED.name(), "");
        return ok(
                reports.render(
                    "Reports", User.find.byId(request().username()), resList, resListGranted, resListRefused, "", "", "", "", ""
                )
            );
    }

    public static Result qa() {
        List<CrawlPermission> resList = processFilterReports("", "", Const.CrawlPermissionStatus.PENDING.name(), "");
        List<CrawlPermission> resListGranted = processFilterReports("", "", Const.CrawlPermissionStatus.GRANTED.name(), "");
        List<CrawlPermission> resListRefused = processFilterReports("", "", Const.CrawlPermissionStatus.REFUSED.name(), "");
        return ok(
                reports.render(
                    "Reports", User.find.byId(request().username()), resList, resListGranted, resListRefused, "", "", "", "", ""
                )
            );
    }

}

