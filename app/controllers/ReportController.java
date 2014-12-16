package controllers;

import static play.data.Form.form;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
import uk.bl.Const.CrawlFrequency;
import uk.bl.Const.NpldType;
import uk.bl.Const.RequestType;
import uk.bl.api.Utils;
import views.html.reports.*;

import com.avaje.ebean.Expr;
import com.avaje.ebean.ExpressionList;
import com.avaje.ebean.Page;

/**
 * Manage reports.
 */
@Security.Authenticated(SecuredController.class)
public class ReportController extends AbstractController {
  
    /**
     * Display the report.
     */
    public static Result index() {
        List<Target> resList = processFilterReports(-1l, -1l, Const.CrawlPermissionStatus.PENDING.name(), "", "", "");
        List<Target> resListGranted = processFilterReports(-1l, -1l, Const.CrawlPermissionStatus.GRANTED.name(), "", "", "");
        List<Target> resListRefused = processFilterReports(-1l, -1l, Const.CrawlPermissionStatus.REFUSED.name(), "", "", "");
        User user = User.findByEmail(request().username());
        List<User> users = User.findAll();
        List<Organisation> organisations = Organisation.findAllSorted();
        RequestType[] requestTypes = Const.RequestType.values();
        
        return ok(
                reports.render(
                    "Reports", user, resList, resListGranted, resListRefused, -1l, -1l, "", "", "", users, organisations, requestTypes
                )
            );
    }

    /**
     * This method enables searching for given URL and redirection in order to add new entry
     * if required.
     * @return
     */
    public static Result search() {
    	DynamicForm requestData = form().bindFromRequest();
    	String action = requestData.get("action");

    	Long curatorId = Long.parseLong(requestData.get("curator"));
    	Long organisationId = Long.parseLong(requestData.get("organisation"));
    	
    	String request = requestData.get("request");
//    	if (request_name != null && !request_name.toLowerCase().equals(Const.NONE) 
//    			&& !request_name.toLowerCase().equals(Const.ALL)) {
//   			request = request_name;
//    	} 
        String startDate = requestData.get("startDate");
        String endDate = requestData.get("endDate");
        
        List<Target> resList = processFilterReports(curatorId, organisationId, Const.CrawlPermissionStatus.PENDING.name(), request, startDate, endDate);
        List<Target> resListGranted = processFilterReports(curatorId, organisationId, Const.CrawlPermissionStatus.GRANTED.name(), request, startDate, endDate);
        List<Target> resListRefused = processFilterReports(curatorId, organisationId, Const.CrawlPermissionStatus.REFUSED.name(), request, startDate, endDate);

        List<User> users = User.findAll();
        List<Organisation> organisations = Organisation.findAllSorted();
        RequestType[] requestTypes = Const.RequestType.values();

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
                            resListRefused, curatorId, organisationId, startDate, endDate, request, users, organisations, requestTypes
                        )
                    );
    		}
    		else if (Const.SEARCH.equals(action)) {
    			return ok(
                		reports.render(
                            "Reports", User.findByEmail(request().username()), resList, resListGranted,
                            resListRefused, curatorId, organisationId, startDate, endDate, request, users, organisations, requestTypes
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
	    		sw.append(target.fieldUrl());
		 	    sw.append(Const.CSV_SEPARATOR);
	    		sw.append(target.createdAt + "");
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
    public static List<Target> processFilterReports(Long curatorId, Long organisationId, 
    		String crawlPermissionsStatus, String request, String startDate, String endDate) {
//    	boolean isProcessed = false;
		ExpressionList<Target> exp = Target.find.fetch("crawlPermissions").where();
		exp = exp.eq("active", true);
		
		if (curatorId != -1) {
			exp = exp.eq("authorUser.id", curatorId);
		}
		if (organisationId != -1) {
			exp = exp.eq("organisation.id", organisationId);
		}
		if (StringUtils.isNotEmpty(crawlPermissionsStatus)) {
			exp = exp.eq("crawlPermissions.status", crawlPermissionsStatus);
		}

//    	if (startDate != null && startDate.length() > 0) {
//    		Logger.info("startDate: " + startDate);
//        	String startDateUnix = Utils.getUnixDateStringFromDateExt(startDate);
//        	Logger.info("startDateUnix: " + startDateUnix);
//        	// TODO: UNIX DATE
//    		exp = exp.ge(Const.CREATED_AT, startDateUnix);
//    	} 
//    	if (endDate != null && endDate.length() > 0) {
//    		Logger.info("endDate: " + endDate);
//        	String endDateUnix = Utils.getUnixDateStringFromDate(endDate);
//        	Logger.info("endDateUnix: " + endDateUnix);
//        	// TODO: UNIX DATE
//    		exp = exp.le(Const.CREATED_AT, endDateUnix);
//    	} 
//
    	List<Target> res = exp.query().findList();
    	
//    	Logger.info("processFilterReports() Expression list size: " + res.size() + ", isProcessed: " + isProcessed);
//    	if (request != null && !request.toLowerCase().equals(Const.ALL) && request.length() > 0) {
//    		Logger.info("request: " + request);
//	        List<Target> resByRequest = new ArrayList<Target>();
//	        Iterator<Target> resIter = res.iterator();
//	        while (resIter.hasNext()) {
//	        	Target target = resIter.next();
//	        	if (target.fieldUrl() != null && target.fieldUrl().length() > 0) {
//	        		List<CrawlPermission> permissionList = CrawlPermission.filterByTarget(target.fieldUrl());
//	            	if (permissionList != null && permissionList.size() > 0) {
//	            		CrawlPermission permission = permissionList.get(0);
//	            		Logger.info("permission: " + permission);
//	            		Logger.info("permission requestFollowup: " + permission.requestFollowup + ", request: " + request);
//	            		try {
//		            		if (permission.requestFollowup && request.equals(Const.RequestTypes.FOLLOW_UP.name())
//		            				|| !permission.requestFollowup && request.equals(Const.RequestTypes.FIRST_REQUEST.name())) {
//		            			resByRequest.add(target);
//		            		}
//	            		} catch (Exception e) {
//		            		if (request.equals(Const.RequestTypes.FIRST_REQUEST.name())) {
//		            			resByRequest.add(target);
//		            		}
//	            		}
//	            	}
//	        	}
//	        }
//	        return resByRequest;
//    	} 
        return res;
    }
              
    /**
     * Display the report.
     */
    public static Result summary() {
    	return redirect(
                routes.ReportQaController.index()
    	        );
    }

    public static Result openLicences() {
    	return redirect(
                routes.ReportController.index()
    	        );
    }

    public static Result recordCreation() {
    	return redirect(
                routes.ReportController.indexCreation()
    	        );
    }

    public static Result qa() {
    	return redirect(
                routes.ReportQaController.index()
    	        );
    }

    /**
     * Display the paginated list of targets.
     *
     * @param page Current page number (starts from 0)
     * @param sortBy Column to be sorted
     * @param order Sort order (either asc or desc)
     * @param curator Author of the target
     * @param organisation The author's organisation
     * @param startDate The start date for filtering
     * @param endDate The end date for filtering
     * @param npld The selection of NPLD scope rule for filtering
     * @param crawlFrequency The crawl frequency value for filtering
     * @param tld The top level domain setting for filtering
     */
    public static Result targets(int pageNo, String sortBy, String order, Long curatorId,
    		Long organisationId, String startDate, String endDate, String npld, String crawlFrequency, String tld) {
    	Logger.info("ReportsCreation.targets()");
    	
    	User user = User.findByEmail(request().username());
    	Page<Target> pages = Target.pageReportsCreation(pageNo, 10, sortBy, order, curatorId, organisationId, 
				startDate, endDate, npld, crawlFrequency, tld);
    	

		List<User> users = User.findAll();
		List<Organisation> organisations = Organisation.findAll();
		CrawlFrequency[] crawlFrequencies = CrawlFrequency.values();
        NpldType[] nplds = NpldType.values();

        return ok(
        	reportscreation.render(
        			"ReportsCreation", 
        			user, 
        			pages, 
        			sortBy, 
        			order,
        			curatorId, 
                	organisationId, 
                	startDate, 
                	endDate,
                	npld, 
                	crawlFrequency, 
                	tld,
                	users,
                	organisations,
                	crawlFrequencies,
                	nplds)
        	);
    }
    
    /**
     * This method exports selected targets to CSV file.
     * @param list of Target objects
     * @param file name
     * @return
     */
    public static void exportCreation(List<Target> targetList, String fileName) {
    	Logger.info("export() targetList size: " + targetList.size());

        StringWriter sw = new StringWriter();
	    sw.append("Target title");
		sw.append(Const.CSV_SEPARATOR);
	    sw.append("Target URL");
		sw.append(Const.CSV_SEPARATOR);
	    sw.append("Date created");
		sw.append(Const.CSV_SEPARATOR);
        sw.append(Const.CSV_LINE_END);
 	    
 	    if (targetList != null && targetList.size() > 0) {
 	    	Iterator<Target> itr = targetList.iterator();
 	    	while (itr.hasNext()) {
 	    		Target target = itr.next();
	    		sw.append(target.title);
		 	    sw.append(Const.CSV_SEPARATOR);
	    		sw.append(target.fieldUrl());
		 	    sw.append(Const.CSV_SEPARATOR);
	    		sw.append(target.createdAt + "");
		 	    sw.append(Const.CSV_SEPARATOR);
	 	 	    sw.append(Const.CSV_LINE_END);
 	    	}
 	    }
    	Utils.generateCsvFile(fileName, sw.toString());
    }
    
    /**
     * This method enables searching for given URL and redirection in order to add new entry
     * if required.
     * @return
     */
    public static Result searchCreation() {
    	DynamicForm requestData = form().bindFromRequest();

    	String action = requestData.get("action");
    	String filter = requestData.get("filter");

    	
    	int pageNo = Integer.parseInt(requestData.get("p"));
    	String sort = requestData.get("s");
    	String order = requestData.get("o");
    	int pageSize = Integer.parseInt(requestData.get("pageSize"));
    	Long curatorId = Long.parseLong(requestData.get("curator"));
    	Long organisationId = Long.parseLong(requestData.get("organisation"));

    	String crawlFrequencyName = requestData.get("crawlFrequency");
    	
    	
    	
    	
        String startDate = requestData.get("startDate");
        Logger.info("startDate: " + startDate);
        String endDate = requestData.get("endDate");

    	String npld = requestData.get("npld");

    	String tld = "either";
    	String tld_name = requestData.get("tld");
    	Logger.info("tld: " + requestData.get(Const.FILTER_TLD));
        if (tld_name != null && !tld_name.toLowerCase().equals(Const.NONE)) {
        	long idx = Long.valueOf(tld_name);
        	if (idx == 1) {
        		tld = Const.YES;
        	}
        	if (idx == 2) {
        		tld = Const.NO;
        	}
        }
        
    	if (StringUtils.isEmpty(action)) {
    		return badRequest("You must provide a valid action");
    	} else {
    		if (Const.EXPORT.equals(action)) {
    			List<Target> exportTargets = new ArrayList<Target>();
    	    	Page<Target> page = Target.pageReportsCreation(pageNo, 10, sort, order, curatorId, organisationId, 
    					startDate, endDate, npld, crawlFrequencyName, tld);    	    	
    			int rowCount = page.getTotalRowCount();
    	    	Page<Target> pageAll = Target.pageReportsCreation(pageNo, rowCount, sort, order, curatorId, organisationId, 
    					startDate, endDate, npld, crawlFrequencyName, tld); 
    			exportTargets.addAll(pageAll.getList());
				Logger.info("export report creation size: " + exportTargets.size());
    			export(exportTargets, Const.EXPORT_TARGETS_REPORT_CREATION);
    	    	return redirect(routes.ReportController.targets(pageNo, sort, order, curatorId, organisationId, 
    	    			startDate, endDate, npld, crawlFrequencyName, tld));
    		}
    		else if (Const.SEARCH.equals(action)) {

    	    	return redirect(routes.ReportController.targets(pageNo, sort, order, curatorId, organisationId, 
    	    			startDate, endDate, npld, crawlFrequencyName, tld));
		    } else {
		    	return badRequest("This action is not allowed");
		    }
    	}
    }	
    
    /**
     * Display the report.
     */
    public static Result indexCreation() {
    	return redirect(routes.ReportController.targets(0, "createdAt", "desc", -1l, -1l, 
    			Utils.getCurrentDate(), "", "", "", "either"));
    }

}

