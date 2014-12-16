package controllers;

import static play.data.Form.form;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import models.Collection;
import models.Organisation;
import models.Target;
import models.User;

import org.apache.commons.lang3.StringUtils;

import play.Logger;
import play.data.DynamicForm;
import play.mvc.Result;
import play.mvc.Security;
import uk.bl.Const;
import uk.bl.Const.ReportQaStatusType;
import uk.bl.api.Utils;
import views.html.reports.reportsqa;

import com.avaje.ebean.Page;

/**
 * Manage reports.
 */
@Security.Authenticated(SecuredController.class)
public class ReportQaController extends AbstractController {
  
    /**
     * Display the report.
     */
    public static Result index() {
    	return redirect(routes.ReportQaController.targets(0, "title", "asc", "qaed", -1L, -1L, Utils.getCurrentDate(), "", -1L));
    }

    public static Result switchReportQaTab(String status) {
    	Logger.info("switchReportQaTab() status: " + status);
    	return redirect(routes.ReportQaController.targets(0, "title", "asc", status, -1L, -1L, Utils.getCurrentDate(), "", -1L));
    }

    /**
     * This method enables searching for given URL and redirection in order to add new entry
     * if required.
     * @return
     */
    public static Result search() {

    	DynamicForm requestData = form().bindFromRequest();
    	String action = requestData.get("action");

    	int pageNo = Integer.parseInt(requestData.get("p"));
    	String sort = requestData.get("s");
    	String order = requestData.get("o");
    	int pageSize = Integer.parseInt(requestData.get("pageSize"));
    	Long curatorId = Long.parseLong(requestData.get("curator"));
    	Long organisationId = Long.parseLong(requestData.get("organisation"));
    	Long collectionId = Long.parseLong(requestData.get("collection"));

    	String status = requestData.get("status");


        String startDate = requestData.get("startDate");
        Logger.info("startDate: " + startDate);
        String endDate = requestData.get("endDate");
        
    	if (StringUtils.isEmpty(action)) {
    		return badRequest("You must provide a valid action");
    	} else {
    		if (Const.EXPORT.equals(action)) {
    			List<Target> exportTargets = new ArrayList<Target>();
    	    	Page<Target> page = Target.pageReportsQa(pageNo, 10, sort, order, status, curatorId, organisationId, 
    					startDate, endDate, collectionId);    	    	
    			int rowCount = page.getTotalRowCount();
    	    	Page<Target> pageAll = Target.pageReportsQa(pageNo, rowCount, sort, order, status, curatorId, organisationId, 
    					startDate, endDate, collectionId); 
    			exportTargets.addAll(pageAll.getList());
				Logger.info("export report QA size: " + exportTargets.size() + ", status: " + status);
    			export(exportTargets, Const.EXPORT_TARGETS_REPORTS_QA);
    	    	return redirect(routes.ReportQaController.targets(pageNo, sort, order, status, curatorId, organisationId, 
    	    			startDate, endDate, collectionId));
    		}
    		else if (Const.SEARCH.equals(action)) {
    	    	return redirect(routes.ReportQaController.targets(pageNo, sort, order, status, curatorId, organisationId, 
    	    			startDate, endDate, collectionId));
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
	    		sw.append(target.fieldUrl());
		 	    sw.append(Const.CSV_SEPARATOR);
	    		sw.append(target.updatedAt.toString());
		 	    sw.append(Const.CSV_SEPARATOR);
	 	 	    sw.append(Const.CSV_LINE_END);
 	    	}
 	    }
    	Utils.generateCsvFile(fileName, sw.toString());
    }
           
    /**
     * Display the paginated list of targets.
     *
     * @param page Current page number (starts from 0)
     * @param sortBy Column to be sorted
     * @param order Sort order (either asc or desc)
     * @param status The type of report QA e.g. awaiting QA, with no QA issues...
     * @param curator Author of the target
     * @param organisation The author's organisation
     * @param startDate The start date for filtering
     * @param endDate The end date for filtering
     * @param collection The associated collection
     */
    public static Result targets(int pageNo, String sortBy, String order, String status, Long curatorId,
    		Long organisationId, String startDate, String endDate, Long collectionId) {
    	Logger.info("ReportsQa.targets()");
    	
    	User user = User.findByEmail(request().username());
    	Page<Target> pages = Target.pageReportsQa(pageNo, 10, sortBy, order, status, curatorId, organisationId, startDate, endDate, collectionId);
    	
        List<User> users = User.findAll();
        List<Organisation> organisations = Organisation.findAllSorted();
        List<Collection> collections = Collection.findAllCollections();
        ReportQaStatusType[] reportQaStatusTypes = ReportQaStatusType.values();

        return ok(
        	reportsqa.render(
        			"ReportsQa", 
        			user, 
        			pages, 
        			sortBy, 
        			order,
        			status,
        			curatorId, 
                	organisationId, 
                	startDate, 
                	endDate, 
                	collectionId,
                	users,
                	organisations,
                	collections,
                	reportQaStatusTypes)
        	);
    }
	    
}

