package controllers;

import static play.data.Form.form;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;

import models.CrawlPermission;
import models.License.LicenseStatus;
import models.Organisation;
import models.Role;
import models.Target;
import models.User;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;

import play.Logger;
import play.data.DynamicForm;
import play.mvc.Result;
import play.mvc.Security;
import uk.bl.Const;
import uk.bl.Const.CrawlFrequency;
import uk.bl.Const.CrawlPermissionStatus;
import uk.bl.Const.NpldType;
import uk.bl.Const.RequestType;
import uk.bl.Const.ScopeType;
import uk.bl.exception.ActException;
import views.html.reports.*;

import com.avaje.ebean.Page;

/**
 * Manage reports.
 */
@Security.Authenticated(SecuredController.class)
public class ReportController extends AbstractController {
  
    /**
     * Display the report.
     * @throws ActException 
     */
    public static Result index() throws ActException {
		return redirect(routes.ReportController.processFilterReports(
				0,-1L, -1L,
				CrawlPermissionStatus.PENDING.name(), //default filter
				"",
				"","","",""));
	};

    /**
     * This method enables searching for given URL and redirection in order to add new entry
     * if required.
	 * Helper for Targets -> Reports search/filter form
     * @return
     * @throws ActException 
     */
    public static Result search() throws ActException {
		Logger.debug("search() ---- STEP NO 1 ");

		DynamicForm requestData = form().bindFromRequest();
		String crawlPermissionsStatus22 = form().bindFromRequest().get("crawlPermissionsStatus22");
		Logger.debug("search() ---- requestData22,  crawlPermissionsStatus22 = " + crawlPermissionsStatus22);

    	String action = requestData.get("action");

    	Logger.debug("action: " + action);

		String curator = requestData.get("curator");

    	String organisation = requestData.get("organisation");
    	Long curatorId = null;
    	if (StringUtils.isNotBlank(curator)) {
    		curatorId = Long.parseLong(curator);
    	}
    	Long organisationId = null;
    	if (StringUtils.isNotBlank(organisation)) {
    		organisationId = Long.parseLong(organisation);
    	}

    	String request = requestData.get("request");
		String requestType = requestData.get("requestType");

		Logger.debug("request ---- = " + request);
		Logger.debug("requestType ---- = " + requestType);

//    	if (request_name != null && !request_name.toLowerCase().equals(Const.NONE) 
//    			&& !request_name.toLowerCase().equals(Const.ALL)) {
//   			request = request_name;
//    	} 
        String requestedFromDate = requestData.get("startDate");
        String requestedToDate = requestData.get("endDate");
        String grantedFromDate = requestData.get("grantedFromDate");
        String grantedToDate = requestData.get("grantedToDate");
    	Logger.debug("Parameters: " + curatorId + ", " + organisationId + ", " + crawlPermissionsStatus22 + ", " + requestedFromDate + ", " + requestedToDate + ", " + grantedFromDate + ", " + grantedToDate);

        List<User> users = User.findAll();
        List<Organisation> organisations = Organisation.findAllSorted();
        RequestType[] requestTypes = Const.RequestType.values();

        User user = User.findByEmail(request().username());

		//Form data
		int pageNo = Integer.parseInt(requestData.get("p"));
		String sort = requestData.get("s");
		String order = requestData.get("o");
		//Long curatorId = Long.parseLong(requestData.get("curator"));
		//Long organisationId = Long.parseLong(requestData.get("organisation"));

		String startDate = requestData.get("startDate");
		Logger.debug("startDate: " + startDate);

//		String endDate = requestData.get("endDate");

		// default
		String searchByCrawlPermissionsStatus = (crawlPermissionsStatus22 != null && !crawlPermissionsStatus22.trim().isEmpty())?crawlPermissionsStatus22:CrawlPermissionStatus.PENDING.name();
		if (action.equals("searchRequested")){
			searchByCrawlPermissionsStatus = CrawlPermissionStatus.PENDING.name();
		} else if (action.equals("searchGranted")){
			searchByCrawlPermissionsStatus = CrawlPermissionStatus.GRANTED.name();
		} else if (action.equals("searchRefused")){
			searchByCrawlPermissionsStatus = CrawlPermissionStatus.REFUSED.name();
		}
		else {
			searchByCrawlPermissionsStatus = crawlPermissionsStatus22;
		}
		Logger.debug("crawlPermissionsStatus, aka searchType ---- = " + searchByCrawlPermissionsStatus);


		Page<Target> pages = Target.pageReports(pageNo, curatorId, organisationId,
				searchByCrawlPermissionsStatus, "",
				"", "", "", "");

		Logger.debug("crawlPermissionsStatus after ---- = " + searchByCrawlPermissionsStatus);
		Logger.debug("search() --- crawlPermissionsStatus, requestData22,  crawlPermissionsStatus22 = " + crawlPermissionsStatus22);

		if (StringUtils.isEmpty(action)) {
    		return badRequest("You must provide a valid action");
    	}
		else {
    		if (action.equals("search")||action.equals("searchRequested")||action.equals("searchGranted")||action.equals("searchRefused")) {
				Logger.debug("action : search+");
				Logger.debug("returning 1, searchByCrawlPermissionsStatus = " + searchByCrawlPermissionsStatus);
				return ok(
						reports.render(
								"Reports", user,
								pages,
								sort, order,
								searchByCrawlPermissionsStatus,
								curatorId, organisationId,
								"", "", "", "", "",
								users, organisations, requestTypes
						)
				);
		    }
			else if (action.equals("export")) {
				String status = searchByCrawlPermissionsStatus;//requestData.get("status");
    			Logger.debug("in export: " + status);

				if (StringUtils.isNotEmpty(status)) {
					Logger.debug("export requested size: " + pages.getList().size());//  resListRequest.size());

					List<Target> pagesFull = Target.pageReportsFull(pageNo, curatorId, organisationId,
							searchByCrawlPermissionsStatus, "",
							"", "", "", "");

					String file = export(pagesFull);
					response().setContentType("text/csv; charset=utf-8");
					response().setHeader("Content-disposition", "attachment; filename=\"" + Const.EXPORT_REQUESTED_LICENCE_FILE + "\"");
					return ok(file);
				}
				Logger.debug("returning in export");

		    }
	    	return badRequest("This action is not allowed");
    	}
    }
    
//    public static void exportLicenses(String name) {
//		return ok(
//        		reports.render(
//                    "Reports", User.findByEmail(request().username()), resList, resListGranted,
//                    resListRefused, curatorId, organisationId, startDate, endDate, request, users, organisations, requestTypes
//                )
//            );
//    }
        
    /**
     * This method exports selected crawl permissions to CSV file.
     * @param list of Target objects
     * @param file name
     * @return
     */
    public static String export(List<Target> permissionList) {
    	Logger.debug("export() permissionList size: " + permissionList.size());
		StringBuilder sw = new StringBuilder();
	    sw.append("Target title");
		sw.append(Const.CSV_SEPARATOR);
	    sw.append("Target URL");
		sw.append(Const.CSV_SEPARATOR);
	    sw.append("Date requested");
		sw.append(Const.CSV_SEPARATOR);
        sw.append(Const.CSV_LINE_END);
 	    
 	    if (permissionList != null && permissionList.size() > 0) {
 	    	Iterator<Target> itr = permissionList.iterator();
 	    	while (itr.hasNext()) {
 	    		Target target = itr.next();
	    		sw.append(StringEscapeUtils.escapeCsv(target.title));
		 	    sw.append(Const.CSV_SEPARATOR);
	    		sw.append(StringEscapeUtils.escapeCsv(target.fieldUrl()));
		 	    sw.append(Const.CSV_SEPARATOR);
	    		sw.append(StringEscapeUtils.escapeCsv(target.createdAt.toString()));
		 	    sw.append(Const.CSV_SEPARATOR);
	 	 	    sw.append(Const.CSV_LINE_END);
 	    	}
 	    }
//    	File file = Utils.INSTANCE.generateCsvFile(fileName, sw.toString());
    	return sw.toString();
    }
            	
    /**
     * This method applies filters to the list of crawl reports.
     * @param curator The curator URL
     * @param organisation The organisation URL
     * @param status The status of the report workflow
     * @param request The request type (first request/folloup/all)
     * @return
     * @throws ActException 
     */

	//List<Target> resListRequest = processFilterReports(null, null, Const.CrawlPermissionStatus.PENDING.name(),"","","","","");

	/**
	 * Process and Filter Reports
	 * @param pageNo
	 * @param curatorId
	 * @param organisationId
	 * @param crawlPermissionsStatus
	 * @param request
	 * @param requestedFromDate
	 * @param requestedToDate
	 * @param grantedFromDate
	 * @param grantedToDate
	 * @return
	 * @throws ActException
	 */
	public static Result processFilterReports(int pageNo, Long curatorId, Long organisationId,
    		String crawlPermissionsStatus, String request, String requestedFromDate, String requestedToDate, String grantedFromDate, String grantedToDate) throws ActException {
		User user = User.findByEmail(request().username());
		List<User> users = User.findAll();
		List<Organisation> organisations = Organisation.findAllSorted();
		RequestType[] requestTypes = Const.RequestType.values();

		Logger.debug("pageNo: " + pageNo);

		Logger.debug("curatorId: " + curatorId);
		Logger.debug("organisationId: " + organisationId);
		Logger.debug("crawlPermissionsStatus: " + crawlPermissionsStatus);
		Logger.debug("request: " + request);
		Logger.debug("requestedFromDate: " + requestedFromDate);
		Logger.debug("requestedToDate: " + requestedToDate);
		Logger.debug("grantedFromDate: " + grantedFromDate);
		Logger.debug("grantedToDate: " + grantedToDate);

		Page<Target> pages = Target.pageReports(pageNo, curatorId, organisationId,
				crawlPermissionsStatus, "",
				"", "", "", "");

        return ok(reports.render(
				"Reports", user,
				pages,
				"", "",
				crawlPermissionsStatus,
				curatorId, organisationId,
				"", "", "", "", "",
				users, organisations, requestTypes
		));
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
    		Long organisationId, String startDate, String endDate, String npld, String crawlFrequency, String tld) throws ActException {
    	Logger.debug("ReportsCreation.targets()");
    	
    	User user = User.findByEmail(request().username());

    	Page<Target> pages = Target.pageReportsCreation(pageNo, Const.PAGINATION_OFFSET, sortBy, order, curatorId, organisationId,
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
     * This method enables searching for given URL and redirection in order to add new entry
     * if required.
     * @return
     */
    public static Result searchCreation() throws ActException {
		DynamicForm requestData = form().bindFromRequest();
    	String action = requestData.get("action");
    	int pageNo = Integer.parseInt(requestData.get("p"));
    	String sort = requestData.get("s");
    	String order = requestData.get("o");
    	Long curatorId = Long.parseLong(requestData.get("curator"));
    	Long organisationId = Long.parseLong(requestData.get("organisation"));

    	String crawlFrequencyName = requestData.get("crawlFrequency");
    	
        String startDate = requestData.get("startDate");
        Logger.debug("startDate: " + startDate);

        String endDate = requestData.get("endDate");

    	String npld = requestData.get("npld");

    	String tld = "either";
    	String tld_name = requestData.get("tld");
    	Logger.debug("tld: " + requestData.get(Const.FILTER_TLD));
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
    		if (action.equals("export")) {
    			List<Target> exportTargets = new ArrayList<Target>();
    	    	Page<Target> page = Target.pageReportsCreation(pageNo, Const.PAGINATION_OFFSET, sort, order, curatorId, organisationId, startDate, endDate, npld, crawlFrequencyName, tld);
    	    	
    			int rowCount = page.getTotalRowCount();
    			
    	    	Page<Target> pageAll = Target.pageReportsCreation(pageNo, rowCount, sort, order, curatorId, organisationId, startDate, endDate, npld, crawlFrequencyName, tld);
    			exportTargets.addAll(pageAll.getList());
				Logger.debug("export report creation size: " + exportTargets.size());
    			String file = export(exportTargets);
    			response().setContentType("text/csv; charset=utf-8");
    			response().setHeader("Content-disposition","attachment; filename=\"" + Const.EXPORT_TARGETS_REPORT_CREATION + "\"");
    	        return ok(file);
    		} else if (action.equals("search")) {
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
    			"", "", "", "", "either"));
    }

    
    /* ----- CONSISTENCY CHECK CODE ---
    
    /**
     * Looks up Targets that are missing a Crawl Permission.
     * 
     * @return
     */
    private static List<Target> getTargetsWithoutCrawlPermissions() {
    	List<Target> nocp = new ArrayList<Target>();
    	for( Target t : Target.findAll() ) {
    		// Does this target have a license?
    		if( ( t.licenseStatus != null && ! "".equals(t.licenseStatus) 
    				&& ! LicenseStatus.NOT_INITIATED.equals(t.licenseStatus)) ||
    				( t.licenses != null && t.licenses.size() > 0 ) ) {
    			// Is there a crawl permission? i.e. latest is NULL or has is NOT_INITIATED:
    			CrawlPermission cp = t.getLatestCrawlPermission();
    			if( cp == null || CrawlPermissionStatus.NOT_INITIATED.equals(cp.status) ) {
    				nocp.add(t);
    			}
    		}
    	}
    	return nocp;
    }

    /**
     * Looks up Targets that have empty start dates:
     * 
     * @return
     */
    private static List<Target> getTargetsWithoutStartDate() {
    	List<Target> ts = new ArrayList<Target>();
    	for( Target t : Target.findAll() ) {
    		if( (! CrawlFrequency.DOMAINCRAWL.name().equals(t.crawlFrequency))
    				&& t.crawlStartDate == null ) {
    			ts.add(t);
    		}
    	}
    	return ts;
    }
    
    /* --- */
    
    /**
     * Looks up Targets that have empty start dates:
     * 
     * @return
     */
    private static List<Target> getTargetsWithoutRootScope() {
    	List<Target> ts = new ArrayList<Target>();
    	for( Target t : Target.findAll() ) {
    		if( ! ScopeType.root.name().equals(t.scope) ) {
    			ts.add(t);
    		}
    	}
    	return ts;
    }
    
    /**
     * Looks up Users those have multiple roles:
     * 
     * @return
     */
    private static List<User> getUsersWithMultipleRoles() {
    	List<User> user = new ArrayList<User>();
    	for( User u : User.findAll() ) {
    		if( u.roles.size() > 1) {
    			user.add(u);   			
    		}
    	}
    	return user;
    }


    /**
     * Performs some basic self-consistency checks on the targets etc.
     * 
     * @return A report showning where there are any problems.
     */
    public static Result consistencyChecks() {
        User user = User.findByEmail(request().username());
    	return ok(consistencyChecks.render(user, getTargetsWithoutCrawlPermissions(), getTargetsWithoutStartDate(), getTargetsWithoutRootScope(), getUsersWithMultipleRoles() ));
    }
    
    /**
     * Used to reset twitter entries where we have lost all records of crawl permissions.
     * 
     * @return
     */
    public static Result removeTwitterInconsistencies() {
    	List<Target> targets = getTargetsWithoutCrawlPermissions();
    	for( Target t : targets ) {
    	  if( t.fieldUrl().contains("twitter.com")) {
    		Logger.warn("Setting licenseStatus to null for "+t.title+"("+t.fieldUrl()+")...");
    		t.licenseStatus = null;
    		if( t.licenses != null ) {
    			t.licenses.clear();
    		}
    		t.update();
    	  } else {
      		Logger.debug("Leaving licenseStatus as "+t.licenseStatus+" for "+t.title+"("+t.fieldUrl()+")...");
    	  }
    	}
    	return redirect(routes.ReportController.consistencyChecks());
    }
    
    /**
     * Used to reset GRANTED entries where we have lost all records of crawl permissions.
     * 
     * @return
     */
    public static Result removeGrantedInconsistencies() {
    	List<Target> targets = getTargetsWithoutCrawlPermissions();
    	for( Target t : targets ) {
    	  if( t.isGranted() ) {
    		Logger.warn("Setting licenseStatus to null for "+t.title+"("+t.fieldUrl()+")...");
    		t.licenseStatus = null;
    		if( t.licenses != null ) {
    			t.licenses.clear();
    		}
    		t.update();
    	  } else {
      		Logger.debug("Leaving licenseStatus as "+t.licenseStatus+" for "+t.title+"("+t.fieldUrl()+")...");
    	  }
    	}
    	return redirect(routes.ReportController.consistencyChecks());
    }
    
    /**
     * Used to reset QUEUED entries where we have lost all records of crawl permissions.
     * 
     * @return
     */
    public static Result removeQueuedInconsistencies() {
    	List<Target> targets = getTargetsWithoutCrawlPermissions();
    	for( Target t : targets ) {
    	  if( t.isQueued() ) {
    		Logger.warn("Setting licenseStatus to null for "+t.title+"("+t.fieldUrl()+")...");
    		t.licenseStatus = null;
    		if( t.licenses != null ) {
    			t.licenses.clear();
    		}
    		t.update();
    	  } else {
      		Logger.debug("Leaving licenseStatus as "+t.licenseStatus+" for "+t.title+"("+t.fieldUrl()+")...");
    	  }
    	}
    	return redirect(routes.ReportController.consistencyChecks());
    }
    
    /**
     * 
     * @param id
     * @return
     */
    public static Result resetThisLicenseToNull( Long id ) {
    	if( id > 0 ) {
    		Target t = Target.findById(id);
    		if( t != null ) {
        		Logger.warn("Setting licenseStatus to null for "+t.title+"("+t.fieldUrl()+")...");
        		t.licenseStatus = null;
        		Logger.warn("Setting licenses to empty for "+t.title+"("+t.fieldUrl()+")...");
        		if( t.licenses != null ) {
        			t.licenses.clear();
        		}
        		t.update();
    		}
    	}
    	return redirect(routes.ReportController.consistencyChecks());
    }
    
    /**
     * Used to reset 'resource' and 'plus1' scopes to 'root':
     * 
     * @return
     */
    public static Result resetBadScopes() {
    	List<Target> targets = getTargetsWithoutRootScope();
    	for( Target t : targets ) {
    	  if( ScopeType.resource.name().equals(t.scope) || 
    		  ScopeType.plus1.name().equals(t.scope) ) {
    		Logger.warn("Setting Scope to root for "+t.title+"("+t.fieldUrl()+"), was "+t.scope);
    		t.scope = ScopeType.root.name();
    		t.update();
    		Logger.info("> is now "+t.scope);
    	  } else {
      		Logger.debug("Leaving Scope as "+t.scope+" for "+t.title+"("+t.fieldUrl()+")...");
    	  }
    	}
    	return redirect(routes.ReportController.consistencyChecks());
    }
    
    
    /**
     * 
     * @return
     */
    public static Result resetEmptyStartDates() {
    	List<Target> targets = getTargetsWithoutStartDate();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		sdf.setTimeZone(TimeZone.getTimeZone("UCT"));
    	for( Target t : targets ) {
    		try {
				t.crawlStartDate = sdf.parse("2013-04-06");
			} catch (Exception e) {
				throw( new RuntimeException(e));
			}
    		t.update();
    		Logger.info("> is now "+t.crawlStartDate);
    	}
    	return redirect(routes.ReportController.consistencyChecks());
    }
    
    /**
     * 
     * @return
     */
    public static Result removeUnwantedRoles() {
    	List<User> users = getUsersWithMultipleRoles();	
    	
    	if(users.size() !=0 && users != null){
    	for( User u : users ) {
    		
        	int severity = Role.getRoleSeverity(u.roles);
        	Logger.info("previous size::::::::::::::::::::::::::  "+u.roles.size());
        	u.roles.clear();
        	
            if(severity == 0)
        		u.roles.add(Role.findByName("sys_admin"));
            if(severity == 1)
            	u.roles.add(Role.findByName("archivist"));
            if(severity == 2)
            	u.roles.add(Role.findByName("expert_user"));
            if(severity == 3)
            	u.roles.add(Role.findByName("user"));
            if(severity == 4)
            	u.roles.add(Role.findByName("viewer"));
    	    if(severity == 5)
          	u.roles.add(Role.findByName("closed"));
            
            Logger.info("present size::::::::::::::::::::::::::  "+u.roles.size()+"  Role:::: "+u.roles.get(0).name);
            
        	}
    	
    	}
 
    	return redirect(routes.ReportController.consistencyChecks());
    }
    
}

