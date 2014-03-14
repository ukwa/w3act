package controllers;

import static play.data.Form.form;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import models.DCollection;
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
import views.html.reports.reportsqa;

import com.avaje.ebean.ExpressionList;

/**
 * Manage reports.
 */
@Security.Authenticated(Secured.class)
public class ReportsQa extends AbstractController {
  
    /**
     * Display the report.
     */
    public static Result index() {
        List<Target> resListQaed = processFilterReports("", "", "", "", "");
        List<Target> resListAwaitingQa = processFilterReports("", "", "", "", "");
        List<Target> resListWithQaIssues = processFilterReports("", "", "", "", "");
        List<Target> resListWithNoQaIssues = processFilterReports("", "", "", "", "");
        List<Target> resListFailedInstances = processFilterReports("", "", "", "", "");
        List<Target> resListPassedInstances = processFilterReports("", "", "", "", "");
        List<Target> resListWithQaIssuesResolved = processFilterReports("", "", "", "", "");
        return ok(
                reportsqa.render(
                    "Reports", User.find.byId(request().username()), resListQaed, resListAwaitingQa,
                    resListWithQaIssues, resListWithNoQaIssues, resListFailedInstances, resListPassedInstances,
                    resListWithQaIssuesResolved, "", "", "", "", ""
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
    	String action = form.get(Const.ACTION);
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
    	String collection_name = form.get(Const.FIELD_SUGGESTED_COLLECTIONS);
    	String collection = "";
    	if (collection_name != null && !collection_name.toLowerCase().equals(Const.NONE)) {
    		try {
    			collection = DCollection.findByTitle(collection_name).url;
    		} catch (Exception e) {
    			Logger.info("Can't find collection for title: " + collection_name + ". " + e);
    		}
    	} 
        String start_date = form.get(Const.FIELD_CRAWL_START_DATE);
        Logger.info("start_date: " + start_date);
        String end_date = form.get(Const.FIELD_CRAWL_END_DATE);
        
        List<Target> resListQaed = processFilterReports(curator, organisation, collection, start_date, end_date);
        List<Target> resListAwaitingQa = processFilterReports(curator, organisation, collection, start_date, end_date);
        List<Target> resListWithQaIssues = processFilterReports(curator, organisation, collection, start_date, end_date);
        List<Target> resListWithNoQaIssues = processFilterReports(curator, organisation, collection, start_date, end_date);
        List<Target> resListFailedInstances = processFilterReports(curator, organisation, collection, start_date, end_date);
        List<Target> resListPassedInstances = processFilterReports(curator, organisation, collection, start_date, end_date);
        List<Target> resListWithQaIssuesResolved = processFilterReports(curator, organisation, collection, start_date, end_date);
        
    	if (StringUtils.isEmpty(action)) {
    		return badRequest("You must provide a valid action");
    	} else {
    		if (Const.EXPORT.equals(action)) {
				Logger.info("export resListQaed size: " + resListQaed.size());
    			export(resListQaed, Const.EXPORT_TARGETS_WITH_QAED_INSTANCES);
				Logger.info("export resListAwaitingQa size: " + resListAwaitingQa.size());
    			export(resListAwaitingQa, Const.EXPORT_TARGETS_WITH_AWAITING_QA);
				Logger.info("export resListWithQaIssues size: " + resListWithQaIssues.size());
    			export(resListWithQaIssues, Const.EXPORT_TARGETS_WITH_QA_ISSUES);
				Logger.info("export resListWithNoQaIssues size: " + resListWithNoQaIssues.size());
    			export(resListWithNoQaIssues, Const.EXPORT_TARGETS_WITH_NO_QA_ISSUES);
				Logger.info("export resListFailedInstances size: " + resListFailedInstances.size());
    			export(resListFailedInstances, Const.EXPORT_TARGETS_WITH_FAILED_INSTANCES);
				Logger.info("export resListPassedInstances size: " + resListPassedInstances.size());
    			export(resListPassedInstances, Const.EXPORT_TARGETS_WITH_PASSED_INSTANCES);
				Logger.info("export resListWithQaIssuesResolved size: " + resListWithQaIssuesResolved.size());
    			export(resListWithQaIssuesResolved, Const.EXPORT_TARGETS_WITH_QA_ISSUES_RESOLVED);
    			return ok(
                		reportsqa.render(
                            "ReportsQa", User.find.byId(request().username()), resListQaed, resListAwaitingQa,
                            resListWithQaIssues, resListWithNoQaIssues, resListFailedInstances, resListPassedInstances,
                            resListWithQaIssuesResolved, curator, organisation, start_date, end_date, collection
                        )
                    );
    		}
    		else if (Const.SEARCH.equals(action)) {
    			return ok(
                		reportsqa.render(
                            "ReportsQa", User.find.byId(request().username()), resListQaed, resListAwaitingQa,
                            resListWithQaIssues, resListWithNoQaIssues, resListFailedInstances, resListPassedInstances,
                            resListWithQaIssuesResolved, curator, organisation, start_date, end_date, collection
                        )
                    );
		    } else {
		    	return badRequest("This action is not allowed");
		    }
    	}
    }	   
    
        
    /**
     * This method exports selected targets to CSV file.
     * @param list of Target objects
     * @param file name
     * @return
     */
    public static void export(List<Target> targetList, String fileName) {
    	Logger.info("export() targetList size: " + targetList.size());

        StringWriter sw = new StringWriter();
	    sw.append("Target title");
		sw.append(Const.CSV_SEPARATOR);
	    sw.append("Target URL");
		sw.append(Const.CSV_SEPARATOR);
	    sw.append("Date requested");
		sw.append(Const.CSV_SEPARATOR);
        sw.append(Const.CSV_LINE_END);
 	    
 	    if (targetList != null && targetList.size() > 0) {
 	    	Iterator<Target> itr = targetList.iterator();
 	    	while (itr.hasNext()) {
 	    		Target target = itr.next();
	    		sw.append(target.title);
		 	    sw.append(Const.CSV_SEPARATOR);
	    		sw.append(target.field_url);
		 	    sw.append(Const.CSV_SEPARATOR);
	    		sw.append(target.field_license);
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
     * @param collection The collection URL
     * @return
     */
    public static List<Target> processFilterReports(String curator, String organisation, 
//    		String status, 
    		String collection, String start_date, String end_date) {
    	boolean isProcessed = false;
    	ExpressionList<Target> exp = Target.find.where();
    	List<Target> res = new ArrayList<Target>();
//    	if (status != null && !status.toLowerCase().equals(Const.NONE) && status.length() > 0) {
//    		Logger.info("status: " + status);
//    		exp = exp.eq(Const.STATUS, status);
//    		isProcessed = true;
//    	} 
    	if (curator != null && !curator.toLowerCase().equals(Const.NONE) && curator.length() > 0) {
    		Logger.info("curator: " + curator);
    		exp = exp.eq(Const.CREATOR_USER, curator);
    		isProcessed = true;
    	} 
    	if (collection != null && !collection.equals(Const.NONE)) {
    		exp = exp.icontains(Const.FIELD_SUGGESTED_COLLECTIONS, collection);
    	} 
    	if (start_date != null && start_date.length() > 0) {
    		Logger.info("start_date: " + start_date);
    		exp = exp.ge(Const.LAST_UPDATE, start_date);
    		isProcessed = true;
    	} 
    	if (end_date != null && end_date.length() > 0) {
    		Logger.info("end_date: " + end_date);
    		exp = exp.le(Const.LAST_UPDATE, end_date);
    		isProcessed = true;
    	} 
    	res = exp.query().findList();
    	Logger.info("Expression list size: " + res.size() + ", isProcessed: " + isProcessed);

//        if (!isProcessed) {
//    		res = models.Target.findAll();
//    	}
        return res;
    }
              
}

