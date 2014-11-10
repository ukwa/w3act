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
import uk.bl.api.Utils;
import views.html.reports.reportsqa;

import com.avaje.ebean.Page;

/**
 * Manage reports.
 */
@Security.Authenticated(Secured.class)
public class ReportsQa extends AbstractController {
  
    /**
     * Display the report.
     */
    public static Result index() {
    	return redirect(routes.ReportsQa.targets(0, "title", "asc", "qaed", "", "", Utils.getCurrentDate(), "", ""));
    }

    public static Result switchReportQaTab(String status) {
    	Logger.info("switchReportQaTab() status: " + status);
    	return redirect(routes.ReportsQa.targets(0, "title", "asc", status, "", "", Utils.getCurrentDate(), "", ""));
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
    	
    	int pageNo = Integer.parseInt(form.get(Const.PAGE_NO));
    	String sort = form.get(Const.SORT_BY);
    	String order = form.get(Const.ORDER);
    	String status = form.get(Const.STATUS);
    	Logger.info("load status: " + status);

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
    			collection = Collection.findByTitle(collection_name).url;
    		} catch (Exception e) {
    			Logger.info("Can't find collection for title: " + collection_name + ". " + e);
    		}
    	} 
        String startDate = form.get(Const.FIELD_CRAWL_START_DATE);
        Logger.info("startDate: " + startDate);
        String endDate = form.get(Const.FIELD_CRAWL_END_DATE);
        
    	if (StringUtils.isEmpty(action)) {
    		return badRequest("You must provide a valid action");
    	} else {
    		if (Const.EXPORT.equals(action)) {
    			List<Target> exportTargets = new ArrayList<Target>();
    	    	Page<Target> page = Target.pageReportsQa(pageNo, 10, sort, order, status, curator, organisation, 
    					startDate, endDate, collection);    	    	
    			int rowCount = page.getTotalRowCount();
    	    	Page<Target> pageAll = Target.pageReportsQa(pageNo, rowCount, sort, order, status, curator, organisation, 
    					startDate, endDate, collection); 
    			exportTargets.addAll(pageAll.getList());
				Logger.info("export report QA size: " + exportTargets.size() + ", status: " + status);
    			export(exportTargets, Const.EXPORT_TARGETS_REPORTS_QA);
    	    	return redirect(routes.ReportsQa.targets(pageNo, sort, order, status, curator, organisation, 
    	    			startDate, endDate, collection));
    		}
    		else if (Const.SEARCH.equals(action)) {
    			Logger.info("searching " + pageNo + " " + sort + " " + order + ", status: " + status +
    					", curator: " + curator + ", organisation: " + organisation + ", startDate: " + startDate +
    					", endDate: " + endDate + ", collection: " + collection);
    	    	return redirect(routes.ReportsQa.targets(pageNo, sort, order, status, curator, organisation, 
    	    			startDate, endDate, collection));
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
	    		sw.append(target.fieldUrl);
		 	    sw.append(Const.CSV_SEPARATOR);
	    		sw.append(target.updatedAt);
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
    public static Result targets(int pageNo, String sortBy, String order, String status, String curator,
    		String organisation, String startDate, String endDate, String collection) {
    	Logger.info("ReportsQa.targets()");
    	
        return ok(
        	reportsqa.render(
        			"ReportsQa", 
        			User.findByEmail(request().username()), 
        			Target.pageReportsQa(pageNo, 10, sortBy, order, status, curator, organisation, 
        					startDate, endDate, collection), 
        			sortBy, 
        			order,
        			status,
                	curator, 
                	organisation, 
                	startDate, 
                	endDate, 
                	collection)
        	);
    }
	    
}

