package controllers;

import static play.data.Form.form;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import models.CommunicationLog;
import models.ContactPerson;
import models.CrawlPermission;
import models.DCollection;
import models.MailTemplate;
import models.Target;
import models.User;

import org.apache.commons.lang3.StringUtils;

import play.Logger;
import play.data.DynamicForm;
import play.data.Form;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Result;
import play.mvc.Security;
import uk.bl.Const;
import uk.bl.api.Utils;
import uk.bl.scope.EmailHelper;
import views.html.crawlpermissions.edit;
import views.html.communicationlogs.logs;
import views.html.crawlpermissions.crawlpermissionpreview;
import views.html.licence.licences;
import views.html.mailtemplates.mailtemplates;
import views.html.refusals.refusals;
import views.html.infomessage;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.ExpressionList;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * Manage permissions.
 */
@Security.Authenticated(Secured.class)
public class CrawlPermissions extends AbstractController {
  
    /**
     * Display the permission.
     */
    public static Result index() {
        List<CrawlPermission> resList = processFilterCrawlPermissions("", Const.DEFAULT_CRAWL_PERMISSION_STATUS, "");
        return ok(
        		views.html.crawlpermissions.list.render(
                    "CrawlPermissions", User.find.byId(request().username()), resList, "", Const.DEFAULT_CRAWL_PERMISSION_STATUS
                )
            );
    }

    /**
     * Display the permission edit panel for this URL.
     */
    public static Result edit(String url) {
		Logger.info("permission url: " + url);
		CrawlPermission permission = CrawlPermission.findByUrl(url);
		Logger.info("permission name: " + permission.name + ", url: " + url);
		Form<CrawlPermission> permissionFormNew = Form.form(CrawlPermission.class);
		permissionFormNew = permissionFormNew.fill(permission);
      	return ok(
	              edit.render(permissionFormNew, User.find.byId(request().username()))
	            );
    }
    
    public static Result view(String url) {
        return ok(
        		views.html.crawlpermissions.view.render(
                		models.CrawlPermission.findByUrl(url), User.find.byId(request().username())
                )
            );
    }
    
    /**
     * This method shows crawl permissions associated with given target. It is called from
     * target edit page.
     * @param targetUrl
     * @return
     */
    public static Result showCrawlPermissions(String targetUrl) {
    	Result res = null;
    	String target = "";
    	String status = "";
    	if (targetUrl != null && targetUrl.length() > 0) {
    		target = Target.findByUrl(targetUrl).field_url;
    		status = Target.findByUrl(targetUrl).qa_status;
    	}
    	Logger.info("showCrawlPermissions: " + targetUrl + ", target: " + target);
        List<CrawlPermission> resList = processFilterCrawlPermissions("", status, target);
    	Logger.info("showCrawlPermissions count: " + resList.size());
        res = ok(
        		views.html.crawlpermissions.list.render(
                    "CrawlPermissions", User.find.byId(request().username()), resList, "", status
                )
            );
        return res;    	
    }
    
    /**
     * This method enables searching for given URL and redirection in order to add new entry
     * if required.
     * @return
     */
    public static Result search() {
    	DynamicForm form = form().bindFromRequest();
    	String action = form.get("action");
    	String name = form.get(Const.NAME);
        String status = form.get(Const.STATUS);
        
        if (status == null) {
        	status = Const.DEFAULT_CRAWL_PERMISSION_STATUS;
        }

        List<CrawlPermission> resList = processFilterCrawlPermissions(name, status, "");

        if (StringUtils.isBlank(name)) {
			Logger.info("Organisation name is empty. Please write name in search window.");
			flash("message", "Please enter a name in the search window");
			return ok(
					views.html.crawlpermissions.list.render(
                        "CrawlPermissions", User.find.byId(request().username()), resList, "", status
                    )
                );
		}

    	if (StringUtils.isEmpty(action)) {
    		return badRequest("You must provide a valid action");
    	} else {
    		if (Const.ADDENTRY.equals(action)) {
//        		return redirect(routes.CrawlPermissions.create(name));
            	CrawlPermission permission = new CrawlPermission();
            	permission.name = name;
                permission.id = Target.createId();
                permission.url = Const.ACT_URL + permission.id;
                permission.creatorUser = User.find.byId(request().username()).url;
                permission.status = Const.CrawlPermissionStatus.QUEUED.name();
                permission.template = Const.MailTemplateType.PERMISSION_REQUEST.name();
                permission.thirdPartyContent = false;
                permission.agree = false;
                permission.publish = true;
        		Logger.info("add entry with url: " + permission.url + ", and name: " + permission.name);
        		Form<CrawlPermission> permissionFormNew = Form.form(CrawlPermission.class);
        		permissionFormNew = permissionFormNew.fill(permission);
              	return ok(
        	              edit.render(permissionFormNew, User.find.byId(request().username()))
        	            );
    		} 
    		else if (Const.SEARCH.equals(action)) {
    			return ok(
    					views.html.crawlpermissions.list.render(
                            "CrawlPermissions", User.find.byId(request().username()), resList, name, status
                        )
                    );
		    } else {
		      return badRequest("This action is not allowed");
		    }
    	}
    }	   
    
    /**
     * This method applyies filters to the list of crawl permissions.
     * @param filterUrl The search string
     * @param status The status of the permission workflow
     * @param target The domain name (URL)
     * @return
     */
    public static List<CrawlPermission> processFilterCrawlPermissions(String filterUrl, String status, String target) {
//    	Logger.info("process filter filterUrl: " + filterUrl + ", status: " + status);
    	boolean isProcessed = false;
    	ExpressionList<CrawlPermission> exp = CrawlPermission.find.where();
    	List<CrawlPermission> res = new ArrayList<CrawlPermission>();
    	if (filterUrl != null && !filterUrl.equals(Const.NONE) && filterUrl.length() > 0) {
    		Logger.info("name: " + filterUrl);
    		exp = exp.contains(Const.NAME, filterUrl);
    		isProcessed = true;
    	}
    	if (status != null && !status.toLowerCase().equals(Const.NONE) && status.length() > 0) {
    		Logger.info("status: " + status);
    		exp = exp.eq(Const.STATUS, status);
    		isProcessed = true;
    	} 
    	if (target != null && !target.toLowerCase().equals(Const.NONE) && target.length() > 0) {
    		Logger.info("target: " + target);
    		exp = exp.eq(Const.TARGET, target);
    		isProcessed = true;
    	} 
    	res = exp.query().findList();
    	Logger.info("Expression list size: " + res.size() + ", isProcessed: " + isProcessed);

        if (!isProcessed) {
    		res = models.CrawlPermission.findAll();
    	}
        return res;
    }
        
    /**
     * Add new permission entry.
     * @param permission title
     * @return
     */
    public static Result create(String name) {
    	CrawlPermission permission = new CrawlPermission();
    	permission.name = name;
        permission.id = Target.createId();
        permission.url = Const.ACT_URL + permission.id;
        permission.creatorUser = User.find.byId(request().username()).url;
        permission.status = Const.CrawlPermissionStatus.QUEUED.name();
        permission.template = Const.MailTemplateType.PERMISSION_REQUEST.name();
        permission.thirdPartyContent = false;
        permission.agree = false;
        permission.publish = true;
		Logger.info("add entry with url: " + permission.url + ", and name: " + permission.name);
		Form<CrawlPermission> permissionFormNew = Form.form(CrawlPermission.class);
		permissionFormNew = permissionFormNew.fill(permission);
      	return ok(
	              edit.render(permissionFormNew, User.find.byId(request().username()))
	            );
    }
      
    /**
     * Create new crawl permission request for particular target.
     * @param permission title
     * @param target
     * @return
     */
    public static Result licenceRequestForTarget(String name, String target) {
    	CrawlPermission permission = new CrawlPermission();
    	permission.name = name;
        permission.id = Utils.createId();
        permission.url = Const.ACT_URL + permission.id;
        Logger.debug("licenceRequestForTarget url: " + permission.url);
        permission.creatorUser = User.find.byId(request().username()).url;
        Logger.debug("licenceRequestForTarget user: " + permission.creatorUser);
        permission.status = Const.CrawlPermissionStatus.QUEUED.name();
        permission.template = Const.MailTemplateType.PERMISSION_REQUEST.name();
        permission.target = target;
		Logger.info("add entry with url: " + permission.url + ", name: " + permission.name + ", and target: " + permission.target);
		Form<CrawlPermission> permissionFormNew = Form.form(CrawlPermission.class);
		permissionFormNew = permissionFormNew.fill(permission);
      	return ok(
	              edit.render(permissionFormNew, User.find.byId(request().username()))
	            );
    }
      
	/**
	 * This method prepares CrawlPermission form for sending info message
	 * about errors 
	 * @return edit page with form and info message
	 */
	public static Result info() {
       	CrawlPermission permission = new CrawlPermission();
       	permission.id = Long.valueOf(getFormParam(Const.ID));
       	permission.url = getFormParam(Const.URL);
        permission.name = getFormParam(Const.NAME);
	    if (getFormParam(Const.DESCRIPTION) != null) {
	    	permission.description = getFormParam(Const.DESCRIPTION);
	    }
	    if (getFormParam(Const.TARGET) != null) {
	    	permission.target = getFormParam(Const.TARGET);
	    }
		permission.contactPerson = Const.NONE;
        if (getFormParam(Const.CONTACT_PERSON) != null) {
    		/**
    		 * Save or update contact person
    		 */
        	ContactPerson person = null;
            boolean isContactPersonExisting = true;
            try {
                try {
                	person = ContactPerson.findByName(getFormParam(Const.CONTACT_PERSON));
                } catch (Exception e) {
                	Logger.info("contact person is not existing exception");
                	isContactPersonExisting = false;
                	person = new ContactPerson();
                	person.id = Utils.createId();
                	person.url = Const.ACT_URL + person.id;
                }
                if (person == null) {
                	Logger.info("contact person is not existing");
                	isContactPersonExisting = false;
                	person = new ContactPerson();
                	person.id = Utils.createId();
                	person.url = Const.ACT_URL + person.id;
                }
                
        	    if (getFormParam(Const.CONTACT_PERSON) != null) {
        	    	person.name = getFormParam(Const.CONTACT_PERSON);
        	    }
        	    if (getFormParam(Const.POSITION) != null) {
        	    	person.position = getFormParam(Const.POSITION);
        	    }
        	    if (getFormParam(Const.EMAIL) != null) {
        	    	person.email = getFormParam(Const.EMAIL);
        	    }
        	    if (getFormParam(Const.CONTACT_ORGANISATION) != null) {
        	    	person.contactOrganisation = getFormParam(Const.CONTACT_ORGANISATION);
        	    }
        	    if (getFormParam(Const.PHONE) != null) {
        	    	person.phone = getFormParam(Const.PHONE);
        	    }
        	    if (getFormParam(Const.POSTAL_ADDRESS) != null) {
        	    	person.postalAddress = getFormParam(Const.POSTAL_ADDRESS);
        	    }
            } catch (Exception e) {
            	Logger.info("ContactPerson not existing exception");
            }
            
            permission.contactPerson = person.url;
        	if (!isContactPersonExisting) {
               	Ebean.save(person);
    	        Logger.info("save contact person: " + person.toString());
        	} else {
           		Logger.info("update contact person: " + person.toString());
               	Ebean.update(person);
        	}
        }
	    
	    Logger.info("creator user: " + getFormParam(Const.CREATOR_USER));
	    if (getFormParam(Const.CREATOR_USER) != null) {
	    	permission.creatorUser = User.findByName(getFormParam(Const.CREATOR_USER)).url;
	    }
	    if (getFormParam(Const.TEMPLATE) != null) {
	    	permission.template = getFormParam(Const.TEMPLATE);
	    }
	    if (getFormParam(Const.STATUS) != null) {
	    	permission.status = getFormParam(Const.STATUS);
	    }
	    if (getFormParam(Const.REQUEST_FOLLOW_UP) != null) {
	    	permission.requestFollowup = Utils.getNormalizeBooleanString(getFormParam(Const.REQUEST_FOLLOW_UP));
	    }
		Form<CrawlPermission> permissionFormNew = Form.form(CrawlPermission.class);
		permissionFormNew = permissionFormNew.fill(permission);
      	return ok(
	              edit.render(permissionFormNew, User.find.byId(request().username()))
	            );
    }
    
    /**
     * This method saves new object or changes on given Permission in the same object
     * completed by revision comment. The "version" field in the Permission object
     * contains the timestamp of the change. 
     * @return
     */
    public static Result save() {
    	Result res = null;
        String save = getFormParam(Const.SAVE);
        String delete = getFormParam(Const.DELETE);
//        Logger.info("save: " + save);
        if (save != null) {
        	Logger.info("input data for saving of the crawl permission id: " + getFormParam(Const.ID) + 
        			", url: " + getFormParam(Const.URL) + 
        			", name: " + getFormParam(Const.NAME));
        	CrawlPermission permission = null;
            boolean isExisting = true;
            try {
            	Form<CrawlPermission> permissionForm = Form.form(CrawlPermission.class).bindFromRequest();
                if(permissionForm.hasErrors()) {
                	String missingFields = "";
                	for (String key : permissionForm.errors().keySet()) {
                	    Logger.debug("key: " +  key);
                	    key = Utils.showMissingField(key);
                	    if (missingFields.length() == 0) {
                	    	missingFields = key;
                	    } else {
                	    	missingFields = missingFields + Const.COMMA + " " + key;
                	    }
                	}
                	Logger.info("form errors size: " + permissionForm.errors().size() + ", " + missingFields);
    	  			flash("message", "Please fill out all the required fields, marked with a red star." + 
    	  					"Missing fields are " + missingFields);
    	  			return info();
                }
            	
            	if (StringUtils.isBlank(getFormParam(Const.EMAIL))) {
            		Logger.info("email: " + getFormParam(Const.EMAIL));
            		Logger.info("Please fill out all the required fields, marked with a red star.");
    	  			flash("message", "Please fill out all the required fields, marked with a red star." + 
    	  					"Missing field is email");
    	  			return info();
            	}    	 	
            	
                try {
                	permission = CrawlPermission.findByUrl(getFormParam(Const.URL));
                } catch (Exception e) {
                	Logger.info("is not existing exception");
                	isExisting = false;
                	permission = new CrawlPermission();
                	permission.id = Long.valueOf(getFormParam(Const.ID));
                	permission.url = getFormParam(Const.URL);
                }
                if (permission == null) {
                	Logger.info("is not existing");
                	isExisting = false;
                	permission = new CrawlPermission();
                	permission.id = Long.valueOf(getFormParam(Const.ID));
                	permission.url = getFormParam(Const.URL);
                }
                
                permission.name = getFormParam(Const.NAME);
        	    if (getFormParam(Const.DESCRIPTION) != null) {
        	    	permission.description = getFormParam(Const.DESCRIPTION);
        	    }
        	    if (getFormParam(Const.TARGET) != null) {
        	    	permission.target = getFormParam(Const.TARGET);
        	    }
        		permission.contactPerson = Const.NONE;
                if (getFormParam(Const.CONTACT_PERSON) != null) {
//                	if (!getFormParam(Const.CONTACT_PERSON).toLowerCase().contains(Const.NONE)) {
                		/**
                		 * Save or update contact person
                		 */
                    	ContactPerson person = null;
                        boolean isContactPersonExisting = true;
                        try {
                            try {
                            	person = ContactPerson.findByName(getFormParam(Const.CONTACT_PERSON));
                            } catch (Exception e) {
                            	Logger.info("contact person is not existing exception");
                            	isContactPersonExisting = false;
                            	person = new ContactPerson();
                            	person.id = Utils.createId();
                            	person.url = Const.ACT_URL + person.id;
                            }
                            if (person == null) {
                            	Logger.info("contact person is not existing");
                            	isContactPersonExisting = false;
                            	person = new ContactPerson();
                            	person.id = Utils.createId();
                            	person.url = Const.ACT_URL + person.id;
                            }
                            
                    	    if (getFormParam(Const.CONTACT_PERSON) != null) {
                    	    	person.name = getFormParam(Const.CONTACT_PERSON);
                    	    }
                    	    if (getFormParam(Const.POSITION) != null) {
                    	    	person.position = getFormParam(Const.POSITION);
                    	    }
                    	    if (getFormParam(Const.EMAIL) != null) {
                    	    	person.email = getFormParam(Const.EMAIL);
                    	    }
                    	    if (getFormParam(Const.CONTACT_ORGANISATION) != null) {
                    	    	person.contactOrganisation = getFormParam(Const.CONTACT_ORGANISATION);
                    	    }
                    	    if (getFormParam(Const.PHONE) != null) {
                    	    	person.phone = getFormParam(Const.PHONE);
                    	    }
                    	    if (getFormParam(Const.POSTAL_ADDRESS) != null) {
                    	    	person.postalAddress = getFormParam(Const.POSTAL_ADDRESS);
                    	    }
                        } catch (Exception e) {
                        	Logger.info("ContactPerson not existing exception");
                        }
                        
                        permission.contactPerson = person.url;
                    	if (!isContactPersonExisting) {
                           	Ebean.save(person);
                	        Logger.info("save contact person: " + person.toString());
                    	} else {
                       		Logger.info("update contact person: " + person.toString());
                           	Ebean.update(person);
                    	}
//                	}
                }
        	    
        	    Logger.info("creator user: " + getFormParam(Const.CREATOR_USER));
//        	    Logger.info("creator user url: " + User.findByName(getFormParam(Const.CREATOR_USER)).url);
        	    if (getFormParam(Const.CREATOR_USER) != null) {
        	    	permission.creatorUser = User.findByName(getFormParam(Const.CREATOR_USER)).url;
        	    }
        	    if (getFormParam(Const.TEMPLATE) != null) {
        	    	permission.template = getFormParam(Const.TEMPLATE);
        	    }
        	    if (getFormParam(Const.STATUS) != null) {
        	    	permission.status = getFormParam(Const.STATUS);
        	    }
        	    if (getFormParam(Const.REQUEST_FOLLOW_UP) != null) {
        	    	permission.requestFollowup = Utils.getNormalizeBooleanString(getFormParam(Const.REQUEST_FOLLOW_UP));
        	    }

            } catch (Exception e) {
            	Logger.info("CrawlPermission not existing exception");
            }
            
        	if (!isExisting) {
                permission.thirdPartyContent = false;
                permission.agree = false;
                permission.publish = true;
            	permission.licenseDate = LicenceController.getCurrentDate();
               	Ebean.save(permission);
    	        Logger.info("save crawl permission: " + permission.toString());
    	        CommunicationLog log = CommunicationLog.logHistory(Const.PERMISSION + " " + permission.status, permission.url, permission.creatorUser, Const.SAVE);
    	        Ebean.save(log);
    	        updateAllByTarget(permission.url, permission.target, permission.status);
    	        Targets.updateQaStatus(permission.target, permission.status);
        	} else {
           		Logger.info("update crawl permission: " + permission.toString());
               	Ebean.update(permission);
               	CommunicationLog log = CommunicationLog.logHistory(Const.PERMISSION + " " + permission.status, permission.url, permission.creatorUser, Const.UPDATE);
    	        Ebean.save(log);
    	        updateAllByTarget(permission.url, permission.target, permission.status);
    	        Targets.updateQaStatus(permission.target, permission.status);
        	}
	        return redirect(routes.CrawlPermissions.edit(permission.url));
        } 
        if (delete != null) {
        	CrawlPermission permission = CrawlPermission.findByUrl(getFormParam(Const.URL));
        	Ebean.delete(permission);
	        res = redirect(routes.CrawlPermissions.index()); 
        }
    	res = redirect(routes.CrawlPermissions.index()); 
        return res;
    }	   

    public static Result templates() {
        return ok(
                mailtemplates.render(
                    "MailTemplates", User.find.byId(request().username()), models.MailTemplate.findAll(), "", ""
                )
            );
    }
    
    public static Result contactpersons() {
        return ok(
                views.html.contactpersons.list.render(
                    "ContactPersons", User.find.byId(request().username()), models.ContactPerson.findAll(), ""
                )
            );
    }
    
    public static Result licences() {
        return ok(
                licences.render(
                	"Licences", User.find.byId(request().username()), models.Taxonomy.findListByType("license"), ""
                )
            );
    }

    public static Result refusals() {
        return ok(
                refusals.render(
                	"Refusals", User.find.byId(request().username()), models.PermissionRefusal.findAll(), ""
                )
            );
    }

    public static Result communicationLogs() {
        return ok(
                logs.render(
                	"CommunicationLogs", User.find.byId(request().username()), models.CommunicationLog.findAll(), "", ""
                )
            );
    }

    /**
     * This method selects crawl permissions selected using checkboxes 
     * on the crawl permission overview page.
     * @return
     */
    public static String getAssignedPermissions() {
    	String assignedPermissions = "";
		try {		   	
		    List<CrawlPermission> permissionList = CrawlPermission.findAll();
		    Iterator<CrawlPermission> permissionItr = permissionList.iterator();
		    while (permissionItr.hasNext()) {
		    	CrawlPermission permission = permissionItr.next();
		        if (getFormParam(permission.name) != null) {
		    		Logger.info("getFormParam(permission.name): " + getFormParam(permission.name) + " " + permission.name);
		            boolean userFlag = Utils.getNormalizeBooleanString(getFormParam(permission.name));
		            if (userFlag) {
		            	if (assignedPermissions.length() == 0) {
		            		assignedPermissions = permission.name;
		            	} else {
		            		assignedPermissions = assignedPermissions + Const.COMMA + " " + permission.name;
		            	}
		            }
		        }
		    }
			Logger.info("assignedPermissions: " + assignedPermissions);
		} catch (Exception e) {
			Logger.info("send some exception" + e);
		}    
		return assignedPermissions;
    }
    
    /**
     * This method selects crawl permissions selected using checkboxes 
     * on the crawl permission overview page.
     * @return
     */
    public static List<CrawlPermission> getAssignedPermissionsList() {
    	List<CrawlPermission> assignedPermissionsList = new ArrayList<CrawlPermission>();
		try {		   	
		    List<CrawlPermission> permissionList = CrawlPermission.findAll();
		    Iterator<CrawlPermission> permissionItr = permissionList.iterator();
		    while (permissionItr.hasNext()) {
		    	CrawlPermission permission = permissionItr.next();
		        if (getFormParam(permission.name) != null) {
		    		Logger.info("getFormParam(permission.name): " + getFormParam(permission.name) + " " + permission.name);
		            boolean userFlag = Utils.getNormalizeBooleanString(getFormParam(permission.name));
		            if (userFlag) {
	            		assignedPermissionsList.add(CrawlPermission.findByName(permission.name));
		            }
		        }
		    }
			Logger.info("assignedPermissions: " + assignedPermissionsList);
		} catch (Exception e) {
			Logger.info("send some exception" + e);
		}    
		return assignedPermissionsList;
    }
    
    /**
     * This method aggregates 'to' emails.
     * @return
     */
    public static String evaluateToEmails() {
       	String assignedPermissions = "";
        List<CrawlPermission> permissionList = CrawlPermission.findAll();
        Iterator<CrawlPermission> permissionItr = permissionList.iterator();
        while (permissionItr.hasNext()) {
        	CrawlPermission permission = permissionItr.next();
            if (getFormParam(permission.name) != null) {
//        		Logger.info("getFormParam(permission.name): " + getFormParam(permission.name) + " " + permission.name);
                boolean userFlag = Utils.getNormalizeBooleanString(getFormParam(permission.name));
                if (userFlag) {
                	if (assignedPermissions.length() == 0) {
                		assignedPermissions = ContactPerson.findEmailsByUrls(permission.contactPerson, assignedPermissions);
                	} else {
                		assignedPermissions = assignedPermissions + Const.COMMA + " " + 
                				ContactPerson.findEmailsByUrls(permission.contactPerson, assignedPermissions);
                	}
                }
            }
        }
        assignedPermissions = assignedPermissions.replace(" ,", "");
		Logger.info("assignedPermissions: " + assignedPermissions);
        return assignedPermissions;
    }
    
    /**
     * This method rejects selected crawl permissions and changes their status to 'EMAIL_REJECTED'.
     * @return
     */
    public static void rejectSelectedCrawlPermissions() {
        List<CrawlPermission> permissionList = CrawlPermission.findAll();
        Iterator<CrawlPermission> permissionItr = permissionList.iterator();
        while (permissionItr.hasNext()) {
        	CrawlPermission permission = permissionItr.next();
            if (getFormParam(permission.name) != null) {
//        		Logger.info("getFormParam(permission.name): " + getFormParam(permission.name) + " " + permission.name);
                boolean userFlag = Utils.getNormalizeBooleanString(getFormParam(permission.name));
                if (userFlag) {
                	permission.status = Const.CrawlPermissionStatus.EMAIL_REJECTED.name();
                	permission.licenseDate = LicenceController.getCurrentDate();
                	Logger.info("new permission staus: " + permission.status);
                   	Ebean.update(permission);                	
        	        CommunicationLog log = CommunicationLog.logHistory(Const.PERMISSION + " " + permission.status, permission.url, permission.creatorUser, Const.UPDATE);
        	        Ebean.save(log);
                	Logger.info("updated permission name: " + permission.name + ", staus: " + permission.status);
        	        updateAllByTarget(permission.url, permission.target, permission.status);
        	        Targets.updateQaStatus(permission.target, permission.status);
                }
            }
        }
    }
    
    /**
     * This method updates all crawl permissions associated for given target
     * when a permission for a target is 'Granted', all others are set to 'Superseded'.
     * @param url The identificaiton URL of a current crawl permission object
     * @param target The domain name (URL)
     * @param status The status of the permission workflow
     */
    public static void updateAllByTarget(String url, String target, String status) {
    	if (status.equals(Const.CrawlPermissionStatus.GRANTED.name())) {
	    	ExpressionList<CrawlPermission> exp = CrawlPermission.find.where();
	    	List<CrawlPermission> permissionList = new ArrayList<CrawlPermission>();
//	    	if (status != null && !status.toLowerCase().equals(Const.NONE) && status.length() > 0) {
//	    		Logger.info("updateAllByTarget() status: " + status);
//	    		exp = exp.eq(Const.STATUS, status);
//	    	} 
	    	if (target != null && !target.toLowerCase().equals(Const.NONE) && target.length() > 0) {
	    		Logger.info("updateAllByTarget() target: " + target);
	    		exp = exp.eq(Const.TARGET, target);
	    	} 
	    	permissionList = exp.query().findList();
	    	Logger.info("updateAllByTarget() Expression list size: " + permissionList.size());
		    Iterator<CrawlPermission> permissionItr = permissionList.iterator();
		    while (permissionItr.hasNext()) {
		    	CrawlPermission permission = permissionItr.next();
		    	if (!url.equals(permission.url)) {
			    	permission.status = Const.CrawlPermissionStatus.SUPERSEDED.name();
			    	Logger.info("updateAllByTarget() permission: " + permission.name + " to SUPERSEDED");
	               	Ebean.update(permission);  
		    	}
		    }
    	}
    }
            
    /**
     * This method updates all crawl permissions associated for given target
     * with passed status.
     * @param target The domain name (URL)
     * @param status The status of the permission workflow
     */
    public static void updateAllByTargetStatusChange(String target, String status) {
    	ExpressionList<CrawlPermission> exp = CrawlPermission.find.where();
    	List<CrawlPermission> permissionList = new ArrayList<CrawlPermission>();
    	if (target != null && !target.toLowerCase().equals(Const.NONE) && target.length() > 0) {
    		Logger.debug("updateAllByTargetStatusChange() target: " + target);
    		exp = exp.eq(Const.TARGET, target);
    	} 
    	permissionList = exp.query().findList();
    	Logger.info("Expression list size: " + permissionList.size());
	    Iterator<CrawlPermission> permissionItr = permissionList.iterator();
	    while (permissionItr.hasNext()) {
	    	CrawlPermission permission = permissionItr.next();
	    	permission.status = status;
    		Logger.debug("updateAllByTargetStatusChange() update status to '" + status + 
    				"' for permission: " + permission.name);
            Ebean.update(permission);  
	    }
    }
            
    /**
     * This method injects necessary server name from configuration file.
     * It is defined in 'server_name' field.
     * @param licenseUrl
     * @return edited link
     */
    public static String injectServerName(String licenseUrl) {
    	String res = "";
		if (licenseUrl != null && licenseUrl.length() > 0 && licenseUrl.contains(Const.HTTP_PREFIX)) {
			String serverName = EmailHelper.getServerNameFromPropertyFile();
			int startPos = licenseUrl.indexOf(Const.HTTP_PREFIX) + Const.HTTP_PREFIX.length();
			int endPos = licenseUrl.substring(startPos).indexOf(Const.SLASH_DELIMITER) + startPos;
			if (serverName != null && serverName.length() > 0) {
				res = Const.HTTP_PREFIX + serverName + licenseUrl.substring(endPos);
			}
		}
        return res;
    }
    
    /**
     * This method sets status "PENDING" for selected crawl permissions.
     * If parameter all is true - do it for all queued permissions,
     * otherwise only selected by checkbox.
     * @param all Type of email sending (all, selected)
     * @param template The email template
     * @return true if sending successful, false otherwise
     */
    public static boolean setPendingSelectedCrawlPermissions(boolean all, String template) {
    	boolean res = true;
        List<CrawlPermission> permissionList = CrawlPermission.findAll();
        Iterator<CrawlPermission> permissionItr = permissionList.iterator();
        while (permissionItr.hasNext()) {
        	CrawlPermission permission = permissionItr.next();
            if (getFormParam(permission.name) != null) {
//        		Logger.info("getFormParam(permission.name): " + getFormParam(permission.name) + " " + permission.name);
                boolean userFlag = Utils.getNormalizeBooleanString(getFormParam(permission.name));
                if (userFlag || all) {
                	Logger.info("mail to contact person:" + permission.contactPerson.replace(Const.LIST_DELIMITER,"") + ".");
            		String email = ContactPerson.findByUrl(permission.contactPerson.replace(Const.LIST_DELIMITER,"")).email;
//                	String[] toMailAddresses = Utils.getMailArray(email);
                	MailTemplate mailTemplate = MailTemplate.findByName(template);
                	String messageSubject = mailTemplate.subject;
                	String messageBody = mailTemplate.readTemplate();
                	String[] placeHolderArray = Utils.getMailArray(mailTemplate.placeHolders);
            		Logger.info("setPendingSelectedCrawlPermissions permission.target: " + permission.target);
            		Logger.info("setPendingSelectedCrawlPermissions current: " + routes.LicenceController.form(permission.url).absoluteURL(request()).toString());
            		String licenseUrl = routes.LicenceController.form(permission.url).absoluteURL(request()).toString();
            		licenseUrl = injectServerName(licenseUrl);
            		Logger.info("setPendingSelectedCrawlPermissions new: " + licenseUrl);
                	messageBody = CrawlPermission.
	                	replaceTwoStringsInText(
	                			messageBody
	    						, Const.PLACE_HOLDER_DELIMITER + placeHolderArray[0] + Const.PLACE_HOLDER_DELIMITER
	    						, Const.PLACE_HOLDER_DELIMITER + placeHolderArray[1] + Const.PLACE_HOLDER_DELIMITER
	    						, permission.target
	    						, licenseUrl);
                	if (email != null) {
	                    EmailHelper.sendMessage(email, messageSubject, messageBody);                	
	//                    EmailHelper.sendMessage(toMailAddresses, messageSubject, messageBody);                	
	                	permission.status = Const.CrawlPermissionStatus.PENDING.name();
	                	Logger.info("new permission staus: " + permission.status);
	                   	Ebean.update(permission);   
	        	        CommunicationLog log = CommunicationLog.logHistory(Const.PERMISSION + " " + permission.status, permission.url, permission.creatorUser, Const.UPDATE);
	        	        Ebean.save(log);
	                	Logger.info("updated permission name: " + permission.name + ", staus: " + permission.status);
	        	        updateAllByTarget(permission.url, permission.target, permission.status);
	        	        Targets.updateQaStatus(permission.target, permission.status);
                	} else {
	                	Logger.info("Missing contact email. Please check contact person");
	        	        res = false;
	        	        break;
                	}
                }
            }
        }
        return res;
    }
    
    /**
     * This method handles queued crawl permissions.
     */
    public static Result send() {
		Logger.info("send crawl permission");
    	Result res = ok();
        String send = getFormParam(Const.SEND);
        String sendall = getFormParam(Const.SEND_ALL);
        String sendsome = getFormParam(Const.SEND_SOME);
        String preview = getFormParam(Const.PREVIEW);
        String reject = getFormParam(Const.REJECT);
        Logger.info("send: " + send + ", sendall: " + sendall + ", sendsome: " + sendsome + ", preview: " + preview + ", reject: " + reject);
	    String template = Const.DEFAULT_TEMPLATE;
        if (getFormParam(Const.TEMPLATE) != null) {
	    	template = getFormParam(Const.TEMPLATE);
	    }
    	String toMails = evaluateToEmails();
    	Logger.info("toMails: " + toMails);

    	if (sendall != null) {
        	Logger.info("send all crawl permission requests");
            boolean sendingRes = setPendingSelectedCrawlPermissions(true, template);
            if (!sendingRes) {
    			flash("message", "Missing contact email. Please check contact person");
            }
        	res = redirect(routes.CrawlPermissions.index()); 
        }
        if (sendsome != null) {
        	Logger.info("send some crawl permission requests");
        	boolean sendingRes = setPendingSelectedCrawlPermissions(false, template);//messageBody, messageSubject); 
            if (!sendingRes) {
    			flash("message", "Missing contact email. Please check contact person");
            }
	        res = redirect(routes.CrawlPermissions.index()); 
        }
        if (preview != null) {
        	Logger.info("preview crawl permission requests");        	
	        res = ok(
	            crawlpermissionpreview.render(
		            	getAssignedPermissionsList().get(0), User.find.byId(request().username()), toMails, template
	            )
	        );
        }
        if (reject != null) {
        	Logger.info("reject crawl permission requests");
        	rejectSelectedCrawlPermissions();        	
	        res = redirect(routes.CrawlPermissions.index()); 
        }
        return res;
    }
    
    /**
     * This method checks if crawl permission for given target already exists.
     * @param target
     * @return true if exists false otherwise
     */
    public static boolean checkCrawlPermissionTarget(String target) {
    	Logger.info("checkCrawlPermissionTarget target: " + target);
    	boolean res = false;
    	List<CrawlPermission> list = CrawlPermission.filterByTarget(target);
        if (list != null && list.size() > 0) {
        	res = true;
        }
        return res;
    }
    
    /**
     * This method is checking if crawl permission for given target already exists and returns result in JSON format.
     * @param target
     * @return JSON result
     */
    public static Result crawlPermissionExist(String target) {
    	Logger.info("crawlPermissionExist target: " + target);
    	boolean res = checkCrawlPermissionTarget(target);
    	Logger.info("crawl permission exists res: " + res + ", target: " + target);
    	return ok(Json.toJson(res));
    }
    
    /**
     * This method is checking if crawl permission for given target already exists and returns result in JSON format.
     * @param target
     * @return JSON result
     */
    public static Result crawlPermissionExistAtHigherLevel(String target) {
    	Logger.info("crawlPermissionExistAtHigherLevel target: " + target);
    	boolean res = false;
    	String path = "";
    	if (target != null) {
    		if (target.contains(Const.SLASH_DELIMITER)) {
		    	String[] parts = target.split(Const.SLASH_DELIMITER);
		    	for (String part: parts)
		        {
		    		try {
		    			res = checkCrawlPermissionTarget(path + part);
		    			if (res) {
		    				break;
		    			} else {
		    				path = path + part + Const.SLASH_DELIMITER;
		    			}
		    		} catch (Exception e) {
		    			Logger.info("crawlPermissionExistAtHigherLevel error: " + e);
		    		}
		        }
	    	}
    	}
    	Logger.info("crawl permission in higher level exists res: " + res + ", target: " + target);
    	return ok(Json.toJson(res));
    }
    
    /**
     * This method is checking if crawl permission for given target for higher level
     * would be possible and returns result in JSON format.
     * @param target
     * @return JSON result
     */
    public static Result checkForHigherLevelPrompt(String target) {
    	Logger.info("checkForHigherLevelPrompt target: " + target);
    	boolean res = false;
    	if (target != null) {
    		if (target.contains(Const.SLASH_DELIMITER)) {
		    	String[] parts = target.split(Const.SLASH_DELIMITER);
		    	if (parts.length > 1) {
		    		res = true;
		    	}
	    	}
    	}
    	Logger.info("crawl permission in higher level exists res: " + res + ", target: " + target);
    	return ok(Json.toJson(res));
    }
    
    /**
     * This method checks if permission contains a contact person. It is
     * necessary in order to send permission request to this person.
     * Otherwise send buttons are disabled.
     * @param list
     * @return check result
     */
    public static boolean haveContactPerson(List<CrawlPermission> permissionList) {
    	boolean res = true;
	    Iterator<CrawlPermission> permissionItr = permissionList.iterator();
	    while (permissionItr.hasNext()) {
	    	CrawlPermission permission = permissionItr.next();
	        if (permission.contactPerson == null || permission.contactPerson.length() == 0) {
	        	res = false;
	        }
	    }
    	return res;
    }
    
    @BodyParser.Of(BodyParser.Json.class)
    public static Result filterByJson(String name) {
        JsonNode jsonData = null;
        if (name != null) {
	        List<CrawlPermission> permissions = CrawlPermission.filterByName(name);
	        jsonData = Json.toJson(permissions);
        }
        return ok(jsonData);
    }
}

