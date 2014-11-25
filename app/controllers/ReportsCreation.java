package controllers;

import static play.data.Form.form;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
import views.html.reports.reportscreation;

import com.avaje.ebean.Page;

/**
 * Manage reports.
 */
@Security.Authenticated(Secured.class)
public class ReportsCreation extends AbstractController {
  
    /**
     * Display the report.
     */
    public static Result index() {
    	return redirect(routes.ReportsCreation.targets(0, Const.CREATED_AT, Const.DESC, "", "", 
    			Utils.getCurrentDate(), "", "", "", Const.EITHER));
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
        String startDate = form.get(Const.FIELD_CRAWL_START_DATE);
        Logger.info("startDate: " + startDate);
        String endDate = form.get(Const.FIELD_CRAWL_END_DATE);
        String crawlFrequency = "";
        Logger.info("field crawl frequency: " + form.get(Const.FIELD_CRAWL_FREQUENCY));
        String crawlFrequency_name = form.get(Const.FIELD_CRAWL_FREQUENCY);
        if (crawlFrequency_name != null && !crawlFrequency_name.toLowerCase().equals(Const.NONE)) {
        	crawlFrequency = crawlFrequency_name;
        }
    	String npld = Const.NONE;
    	Logger.info("npld: " + form.get(Const.FILTER_NPLD));
    	String npld_name = form.get(Const.FILTER_NPLD);
        if (npld_name != null && !npld_name.toLowerCase().equals(Const.NONE)) {
        	npld = npld_name;
        }
    	String tld = Const.EITHER;
    	String tld_name = form.get(Const.FILTER_TLD);
    	Logger.info("tld: " + form.get(Const.FILTER_TLD));
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
    	    	Page<Target> page = Target.pageReportsCreation(pageNo, 10, sort, order, curator, organisation, 
    					startDate, endDate, npld, crawlFrequency, tld);    	    	
    			int rowCount = page.getTotalRowCount();
    	    	Page<Target> pageAll = Target.pageReportsCreation(pageNo, rowCount, sort, order, curator, organisation, 
    					startDate, endDate, npld, crawlFrequency, tld); 
    			exportTargets.addAll(pageAll.getList());
				Logger.info("export report creation size: " + exportTargets.size());
    			export(exportTargets, Const.EXPORT_TARGETS_REPORT_CREATION);
    	    	return redirect(routes.ReportsCreation.targets(pageNo, sort, order, curator, organisation, 
    	    			startDate, endDate, npld, crawlFrequency, tld));
    		}
    		else if (Const.SEARCH.equals(action)) {
    			Logger.info("searching " + pageNo + " " + sort + " " + order + ", curator: " + curator + 
    					", organisation: " + organisation + ", startDate: " + startDate + ", endDate: " + endDate + 
    					", npld: " + npld + ", crawlFrequency: " + crawlFrequency + ", tld: " + tld);
    	    	return redirect(routes.ReportsCreation.targets(pageNo, sort, order, curator, organisation, 
    	    			startDate, endDate, npld, crawlFrequency, tld));
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
    public static Result targets(int pageNo, String sortBy, String order, String curator,
    		String organisation, String startDate, String endDate, String npld, String crawlFrequency, String tld) {
    	Logger.info("ReportsCreation.targets()");
    	
        return ok(
        	reportscreation.render(
        			"ReportsCreation", 
        			User.findByEmail(request().username()), 
        			Target.pageReportsCreation(pageNo, 10, sortBy, order, curator, organisation, 
        					startDate, endDate, npld, crawlFrequency, tld), 
        			sortBy, 
        			order,
                	curator, 
                	organisation, 
                	startDate, 
                	endDate,
                	npld, 
                	crawlFrequency, 
                	tld)
        	);
    }
	    
}

