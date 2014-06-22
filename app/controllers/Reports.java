package controllers;

import static play.data.Form.form;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import models.ContactPerson;
import models.CrawlPermission;
import models.Organisation;
import models.Target;
import models.User;

import org.apache.commons.lang3.StringUtils;

import play.Logger;
import play.data.DynamicForm;
import play.mvc.Result;
import play.mvc.Security;
import uk.bl.Const;
import uk.bl.api.Utils;
import views.html.reports.*;

import com.avaje.ebean.ExpressionList;

/**
 * Manage reports.
 */
@Security.Authenticated(Secured.class)
public class Reports extends AbstractController {
  
    /**
     * Display the report.
     */
    public static Result index() {
        List<Target> resList = 
//                List<CrawlPermission> resList = 
        		processFilterReports("", "", Const.CrawlPermissionStatus.PENDING.name(), "", "", "");
        List<Target> resListGranted = 
//                List<CrawlPermission> resListGranted = 
        		processFilterReports("", "", Const.CrawlPermissionStatus.GRANTED.name(), "", "", "");
        List<Target> resListRefused = 
//                List<CrawlPermission> resListRefused = 
        		processFilterReports("", "", Const.CrawlPermissionStatus.REFUSED.name(), "", "", "");
        return ok(
                reports.render(
                    "Reports", User.findByEmail(request().username()), resList, 
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
        String start_date = form.get(Const.FIELD_CRAWL_START_DATE);
        Logger.info("start_date: " + start_date);
        String end_date = form.get(Const.FIELD_CRAWL_END_DATE);
        
        List<Target> resList = 
//                List<CrawlPermission> resList = 
        		processFilterReports(curator, organisation, Const.CrawlPermissionStatus.PENDING.name(), 
        				request, start_date, end_date);
        List<Target> resListGranted = 
//                List<CrawlPermission> resListGranted = 
        		processFilterReports(curator, organisation, Const.CrawlPermissionStatus.GRANTED.name(), 
        				request, start_date, end_date);
        List<Target> resListRefused = 
//                List<CrawlPermission> resListRefused = 
        		processFilterReports(curator, organisation, Const.CrawlPermissionStatus.REFUSED.name(), 
        				request, start_date, end_date);

    	if (StringUtils.isEmpty(action)) {
    		return badRequest("You must provide a valid action");
    	} else {
    		if (Const.EXPORT.equals(action)) {
				Logger.info("export requested size: " + resList.size());
    			export(resList, Const.EXPORT_REQUESTED_LICENCE_FILE);
				Logger.info("export granted size: " + resListGranted.size());
    			export(resListGranted, Const.EXPORT_GRANTED_LICENCE_FILE);
				Logger.info("export refused size: " + resListRefused.size());
    			export(resListRefused, Const.EXPORT_REFUSED_LICENCE_FILE);
    			return ok(
                		reports.render(
                            "Reports", User.findByEmail(request().username()), resList, resListGranted,
                            resListRefused, curator, organisation, start_date, end_date, request
                        )
                    );
    		}
    		else if (Const.SEARCH.equals(action)) {
    			return ok(
                		reports.render(
                            "Reports", User.findByEmail(request().username()), resList, resListGranted,
                            resListRefused, curator, organisation, start_date, end_date, request
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
     * @param file name
     * @return
     */
    public static void export(List<Target> permissionList, String fileName) {
//        public static void export(List<CrawlPermission> permissionList, String fileName) {
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
// 	    	Iterator<CrawlPermission> itr = permissionList.iterator();
 	    	Iterator<Target> itr = permissionList.iterator();
 	    	while (itr.hasNext()) {
 	    		Target target = itr.next();
// 	    		CrawlPermission permission = itr.next();
	    		sw.append(target.title);
		 	    sw.append(Const.CSV_SEPARATOR);
	    		sw.append(target.field_url);
		 	    sw.append(Const.CSV_SEPARATOR);
	    		sw.append(target.created);
		 	    sw.append(Const.CSV_SEPARATOR);
	 	 	    sw.append(Const.CSV_LINE_END);
 	    	}
 	    }
    	Utils.generateCsvFile(fileName, sw.toString());
    }
            	
    /**
     * This method applies filters to the list of crawl reports.
     * @param curator The curator URL
     * @param organisation The organisation URL
     * @param status The status of the report workflow
     * @param request The request type (first request/folloup/all)
     * @return
     */
    public static List<Target> processFilterReports(String curator, String organisation, 
    		String status, String request, String startDate, String endDate) {
    	boolean isProcessed = false;
    	ExpressionList<Target> exp = Target.find.where();
    	List<Target> res = new ArrayList<Target>();
    	
   		exp = exp.eq(Const.ACTIVE, true);
    	if (curator != null && !curator.equals(Const.NONE)) {
//    		Logger.info("curatorUrl: " + curatorUrl);
    		exp = exp.icontains(Const.AUTHOR, curator);
    	}
    	if (organisation != null && !organisation.equals(Const.NONE)) {
//    		Logger.info("organisationUrl: " + organisationUrl);
    		exp = exp.icontains(Const.FIELD_NOMINATING_ORGANISATION, organisation);
    	} 
    	if (startDate != null && startDate.length() > 0) {
    		Logger.info("startDate: " + startDate);
        	String startDateUnix = Utils.getUnixDateStringFromDateExt(startDate);
        	Logger.info("startDateUnix: " + startDateUnix);
    		exp = exp.ge(Const.CREATED, startDateUnix);
    	} 
    	if (endDate != null && endDate.length() > 0) {
    		Logger.info("endDate: " + endDate);
        	String endDateUnix = Utils.getUnixDateStringFromDate(endDate);
        	Logger.info("endDateUnix: " + endDateUnix);
    		exp = exp.le(Const.CREATED, endDateUnix);
    	} 
    	if (status != null && !status.toLowerCase().equals(Const.NONE) && status.length() > 0) {
//    		Logger.info("qa status: " + status);
    		exp = exp.eq(Const.QA_STATUS, status);
    		isProcessed = true;
    	} 
    	res = exp.query().findList();
    	Logger.info("processFilterReports() Expression list size: " + res.size() + ", isProcessed: " + isProcessed);
    	if (request != null && !request.toLowerCase().equals(Const.ALL) && request.length() > 0) {
    		Logger.info("request: " + request);
	        List<Target> resByRequest = new ArrayList<Target>();
	        Iterator<Target> resIter = res.iterator();
	        while (resIter.hasNext()) {
	        	Target target = resIter.next();
	        	if (target.field_url != null && target.field_url.length() > 0) {
	        		List<CrawlPermission> permissionList = CrawlPermission.filterByTarget(target.field_url);
	            	if (permissionList != null && permissionList.size() > 0) {
	            		CrawlPermission permission = permissionList.get(0);
	            		Logger.info("permission: " + permission);
	            		Logger.info("permission requestFollowup: " + permission.requestFollowup + ", request: " + request);
	            		try {
		            		if (permission.requestFollowup && request.equals(Const.RequestTypes.FOLLOW_UP.name())
		            				|| !permission.requestFollowup && request.equals(Const.RequestTypes.FIRST_REQUEST.name())) {
		            			resByRequest.add(target);
		            		}
	            		} catch (Exception e) {
		            		if (request.equals(Const.RequestTypes.FIRST_REQUEST.name())) {
		            			resByRequest.add(target);
		            		}
	            		}
	            	}
	        	}
	        }
	        return resByRequest;
    	} 
        return res;
    }
              
    /**
     * Display the report.
     */
    public static Result summary() {
    	return redirect(
                routes.ReportsQa.index()
    	        );
    }

    public static Result openLicences() {
    	return redirect(
                routes.Reports.index()
    	        );
    }

    public static Result recordCreation() {
    	return redirect(
                routes.ReportsCreation.index()
    	        );
    }

    public static Result qa() {
    	return redirect(
                routes.ReportsQa.index()
    	        );
    }

}

