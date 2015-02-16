package controllers;

import static play.data.Form.form;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import models.CommunicationLog;
import models.ContactPerson;
import models.CrawlPermission;
import models.MailTemplate;
import models.Target;
import models.User;

import org.apache.commons.lang3.StringUtils;

import play.Logger;
import play.data.DynamicForm;
import play.data.Form;
import play.data.validation.ValidationError;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Result;
import play.mvc.Security;
import uk.bl.Const;
import uk.bl.Const.CrawlPermissionStatus;
import uk.bl.api.Utils;
import uk.bl.exception.ActException;
import uk.bl.scope.EmailHelper;
import views.html.crawlpermissions.newForm;
import views.html.crawlpermissions.edit;
import views.html.crawlpermissions.view;
import views.html.crawlpermissions.crawlpermissionpreview;
import views.html.crawlpermissions.list;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.ExpressionList;
import com.avaje.ebean.Page;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * Manage permissions.
 */
@Security.Authenticated(SecuredController.class)
public class CrawlPermissionController extends AbstractController {
    final static Form<CrawlPermission> crawlPermissionForm = new Form<CrawlPermission>(CrawlPermission.class);

    /**
     * Display the crawl permissions.
     */
    public static Result index() {
    	Logger.debug("CrawlPermissions.index()");
        return GO_HOME;
    }
    
    public static Result GO_HOME = redirect(
        routes.CrawlPermissionController.list(0, Const.NAME, Const.ASC, "", Const.DEFAULT_CRAWL_PERMISSION_STATUS, Const.SELECT_ALL)
    );
    
    /**
     * Display the paginated list of crawl permissions.
     *
     * @param page Current page number (starts from 0)
     * @param sortBy Column to be sorted
     * @param order Sort order (either asc or desc)
     * @param filter Filter applied on target urls
     */
    public static Result list(int pageNo, String sortBy, String order, String filter, String status, String sel) {
    	Logger.debug("CrawlPermissions.list() " + filter + " - " + sel);
    	
    	Page<CrawlPermission> pages = CrawlPermission.page(pageNo, 20, sortBy, order, filter, status);

    	CrawlPermissionStatus[] crawlPermissionStatuses = Const.CrawlPermissionStatus.values();
    	
    	List<MailTemplate> templates = MailTemplate.findByType(MailTemplate.TemplateType.PERMISSION_REQUEST.name());
    	
    	Logger.debug("template: " + templates);

        return ok(
        	list.render(
        			"CrawlPermissions", 
        			User.findByEmail(request().username()), 
        			filter, 
        			pages, 
        			sortBy, 
        			order,
        			status,
        			sel,
        			crawlPermissionStatuses, templates)
        	);
    }
    
    
    public static Result newForm(Long targetId) {
    	User user = User.findByEmail(request().username());
    	Map<String,String> crawlPermissionStatuses = CrawlPermissionStatus.options();
    	
		CrawlPermission crawlPermission = new CrawlPermission();
    	if (targetId != -1) {
    		Target target = Target.findById(targetId);
    		crawlPermission.name = target.title;
//    		target.formUrl = target.fieldUrl();
    		crawlPermission.target = target;
//    		Logger.debug("target.formUrl:" + target.formUrl);    	
    	}
		
        crawlPermission.status = Const.CrawlPermissionStatus.QUEUED.name();
        crawlPermission.user = user;
        crawlPermission.token = UUID.randomUUID().toString();

		Form<CrawlPermission> filledForm = Form.form(CrawlPermission.class);
		filledForm = filledForm.fill(crawlPermission);
		
        return ok(newForm.render(filledForm, user, crawlPermissionStatuses, targetId, null));
        
        
    }

    public static Result edit(Long id) {
    	User user = User.findByEmail(request().username());
		CrawlPermission crawlPermission = CrawlPermission.findById(id);
		crawlPermission.target.formUrl = crawlPermission.target.fieldUrl();
		Form<CrawlPermission> crawlPermissionForm = Form.form(CrawlPermission.class);
		crawlPermissionForm = crawlPermissionForm.fill(crawlPermission);
    	Map<String,String> crawlPermissionStatuses = CrawlPermissionStatus.options();
      	return ok(edit.render(crawlPermissionForm, user, id, crawlPermissionStatuses, null));
    }
    
    public static Result view(Long id) {
    	User user = User.findByEmail(request().username());
    	CrawlPermission crawlPermission = CrawlPermission.findById(id);
    	Logger.debug("contactPerson: " + crawlPermission.contactPerson);
    	Logger.debug("user: " + crawlPermission.user);
    	Logger.debug("license: " + crawlPermission.license);
    	Logger.debug("mailTemplate: " + crawlPermission.mailTemplate);
        return ok(view.render(crawlPermission, user));
    }
    
    /**
     * This method shows crawl permissions associated with given target. It is called from
     * target edit page.
     * @param targetUrl
     * @return
     */
    public static Result showCrawlPermissions(Long targetId) {
    	Result res = null;
    	String status = "";
		Target target = Target.findById(targetId);
		if (target.qaIssue != null) {
    		status = target.qaIssue.url;
		}
    	Logger.debug("showCrawlPermissions: " + targetId + ", target: " + target);
//        List<CrawlPermission> resList = processFilterCrawlPermissions("", status, target);
//    	Logger.debug("showCrawlPermissions count: " + resList.size());
    	res = redirect(routes.CrawlPermissionController.list(0, Const.NAME, Const.ASC, "", status, Const.SELECT_ALL));
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

//        List<CrawlPermission> resList = processFilterCrawlPermissions(name, status, "");

        if (StringUtils.isBlank(name)) {
			Logger.debug("Organisation name is empty. Please write name in search window.");
			flash("message", "Please enter a name in the search window");
	    	return redirect(routes.CrawlPermissionController.list(0, Const.NAME, Const.ASC, name, status, Const.SELECT_ALL));
		}

    	if (StringUtils.isEmpty(action)) {
    		return badRequest("You must provide a valid action");
    	} else {
    		if (action.equals("search")) {
    	    	return redirect(routes.CrawlPermissionController.list(0, Const.NAME, Const.ASC, name, status, Const.SELECT_ALL));
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
//    	Logger.debug("process filter filterUrl: " + filterUrl + ", status: " + status);
    	boolean isProcessed = false;
    	ExpressionList<CrawlPermission> exp = CrawlPermission.find.where();
    	List<CrawlPermission> res = new ArrayList<CrawlPermission>();
    	if (filterUrl != null && !filterUrl.equals(Const.NONE) && filterUrl.length() > 0) {
    		Logger.debug("name: " + filterUrl);
    		exp = exp.contains(Const.NAME, filterUrl);
    		isProcessed = true;
    	}
    	if (status != null && !status.toLowerCase().equals(Const.NONE) && status.length() > 0) {
    		Logger.debug("status: " + status);
    		exp = exp.eq(Const.STATUS, status);
    		isProcessed = true;
    	} 
    	if (target != null && !target.toLowerCase().equals(Const.NONE) && target.length() > 0) {
    		Logger.debug("target: " + target);
    		exp = exp.eq(Const.TARGET, target);
    		isProcessed = true;
    	} 
    	res = exp.query().findList();
    	Logger.debug("Expression list size: " + res.size() + ", isProcessed: " + isProcessed);

        if (!isProcessed) {
    		res = models.CrawlPermission.findAll();
    	}
        return res;
    }
        
    /**
     * Create new crawl permission request for particular target.
     * @param permission title
     * @param target
     * @return
     */
    public static Result licenceRequestForTarget(Long targetId) {
    	
    	
    	return redirect(routes.CrawlPermissionController.newForm(targetId));
    }
//    	CrawlPermission crawlPermission = new CrawlPermission();
//        User user = User.findByEmail(request().username());
//        Target target = Target.findById(targetId);
//        target.formUrl = target.fieldUrl();
//    	crawlPermission.name = target.title;
//        crawlPermission.user = user;
//        crawlPermission.status = Const.CrawlPermissionStatus.QUEUED.name();
//        Logger.debug("crawlPermission.mailTemplate.name: " + crawlPermission.mailTemplate.name);
//        MailTemplate mailTemplate = new MailTemplate();
//        mailTemplate.name = Const.MailTemplateType.PERMISSION_REQUEST.name();
//        crawlPermission.mailTemplate = mailTemplate;
//        crawlPermission.target = target;
//		ContactPerson contactPerson = new ContactPerson();
//		crawlPermission.contactPerson = contactPerson;
//		Logger.debug("add entry with url: " + crawlPermission.url + ", name: " + crawlPermission.name + ", and target: " + crawlPermission.target.title + ", " + user.email);
//		
//		Form<CrawlPermission> filledForm = crawlPermissionForm;
//		filledForm = filledForm.fill(crawlPermission);
//    	Map<String,String> crawlPermissionStatuses = CrawlPermissionStatus.options();
//    	
//    	Logger.debug("licenceRequestForTarget contactPerson: " + filledForm.get().contactPerson);
//    	Logger.debug("licenceRequestForTarget user: " + filledForm.get().user);
//      	return ok(
//	              newForm.render(filledForm, user, crawlPermissionStatuses, targetId)

    public static Form<CrawlPermission> processForm() {
    	return processForm(false);
    }
    
    public static Form<CrawlPermission> processForm(boolean requireContactPerson) {
       	CrawlPermission permission = CrawlPermission.create(Long.valueOf(getFormParam(Const.ID)), getFormParam(Const.URL), getFormParam(Const.NAME));
	    if (getFormParam(Const.DESCRIPTION) != null) {
	    	permission.description = getFormParam(Const.DESCRIPTION);
	    }
	    if (getFormParam(Const.TARGET) != null) {
	    	permission.target.title = getFormParam(Const.TARGET);
	    }
		permission.contactPerson.name = Const.NONE;
        if (getFormParam(Const.CONTACT_PERSON) != null) {
            permission.contactPerson.name = getFormParam(Const.CONTACT_PERSON);
            
            Logger.debug("contactPerson: " + permission.contactPerson + ", " + permission.description + ", " + permission.target.title);
            
            if (requireContactPerson) {
	            try {
	            	ContactPerson person = ContactPerson.findByName(getFormParam(Const.CONTACT_PERSON));
	            	setContactPerson(permission, person.url);
	            	Logger.debug("contact person: " + getFormParam(Const.CONTACT_PERSON) + ", " + person.url);
	            } catch (Exception e) {
	            	Logger.debug("contact person is not existing.");
	                if (getFormParam(Const.EMAIL) != null) {
	                    try {
	                    	List<ContactPerson> personList = ContactPerson.filterByEmail(getFormParam(Const.EMAIL));
	                    	if (personList.size() > 0) {
	                        	setContactPerson(permission, personList.get(0).url);
	                    	}
	                    } catch (Exception e2) {
	                    	Logger.debug("contact person is not found by email.");
	                    }
	                }	    
	            }
            }
        }	    
//	    Logger.debug("creator user: " + getFormParam(Const.CREATOR_USER) + " - " + ContactPerson.findByUrl(getFormParam(Const.CONTACT_PERSON)).email);
	    if (getFormParam(Const.CREATOR_USER) != null) {
	    	permission.user = User.findByName(getFormParam(Const.CREATOR_USER));
	    }
	    if (getFormParam(Const.TEMPLATE) != null) {
	    	permission.mailTemplate.url = getFormParam(Const.TEMPLATE);
	    }
	    if (getFormParam(Const.STATUS) != null) {
	    	permission.status = getFormParam(Const.STATUS);
	    }
	    if (getFormParam(Const.REQUEST_FOLLOW_UP) != null) {
	    	permission.requestFollowup = Utils.INSTANCE.getNormalizeBooleanString(getFormParam(Const.REQUEST_FOLLOW_UP));
	    }
		Form<CrawlPermission> permissionFormNew = Form.form(CrawlPermission.class);
		permissionFormNew = permissionFormNew.fill(permission);
		Logger.debug("email: " + getFormParam(Const.EMAIL));
		return permissionFormNew;
    }
    
	/**
	 * This method prepares CrawlPermission form for sending info message
	 * about errors 
	 * @return edit page with form and info message
	 */
    public static Result info(Form<CrawlPermission> form, Long id, String contactName) {
    	User user = User.findByEmail(request().username());
    	Map<String,String> crawlPermissionStatuses = CrawlPermissionStatus.options();
		Logger.debug("Info contactPerson: " + form.get().contactPerson.name);
		return badRequest(edit.render(form, user, id, crawlPermissionStatuses, contactName));
    }
    
	public static Result newInfo(Form<CrawlPermission> form, Long targetId, String contactName) {
    	Map<String,String> crawlPermissionStatuses = CrawlPermissionStatus.options();
		User user = User.findByEmail(request().username());
        return badRequest(newForm.render(form, user, crawlPermissionStatuses, targetId, contactName));
	}
	
    public static Result update(Long id) {
    	DynamicForm requestData = form().bindFromRequest();
    	String action = requestData.get("action");

    	Logger.debug("update() action: " + action);
        Form<CrawlPermission> filledForm = form(CrawlPermission.class).bindFromRequest();
    	
        if (StringUtils.isNotEmpty(action)) {

        	if (action.equals("save")) {    
        		
            	Logger.debug("hasGlobalErrors: " + filledForm.hasGlobalErrors());
            	Logger.debug("hasErrors: " + filledForm.hasErrors());

		        if (filledForm.hasErrors()) {
		        	Logger.debug("hasErrors: " + filledForm.errors());
		            return info(filledForm, id, null);
		        }
		        
		        String formUrl = requestData.get("target.formUrl");

		    	String contactPersonName = filledForm.get().contactPerson.name;
		    	String contactPersonEmail = filledForm.get().contactPerson.email;
	
		        if (StringUtils.isBlank(formUrl)) {
		            ValidationError ve = new ValidationError("target.formUrl", "URL is required");
		            filledForm.reject(ve);
		            return info(filledForm, id, null);
		        }
		        
		        if (StringUtils.isBlank(contactPersonEmail)) {
		            ValidationError ve = new ValidationError("contactPerson.email", "Email is required");
		            filledForm.reject(ve);
		            return info(filledForm, id, null);
		        }
		        
		    	ContactPerson existingContact = ContactPerson.findByEmail(contactPersonEmail);
		    	
		    	if (existingContact != null) {
			    	Logger.debug("validateForm contactPersonName: " + contactPersonName + "/" + existingContact.name);
			    	Logger.debug("validateForm contactPersonEmail: " + contactPersonEmail + "/" + existingContact.email);
		    		if (StringUtils.isNotEmpty(existingContact.name) && StringUtils.isNotEmpty(existingContact.email) && 
		    			StringUtils.isNotBlank(contactPersonName) && StringUtils.isNotBlank(contactPersonEmail) 
		    			&& existingContact.email.equals(contactPersonEmail) && !existingContact.name.equals(contactPersonName)) {
		    			// matching emails but names don't match
		    	    	Logger.debug("validateForm validation error");

			    		String msg = "A contact person with email '" + contactPersonEmail + 
			  					"' is already in the Contact Persons list, but with the Name '" + existingContact.name + 
			  					"' which is different from the given name '" + contactPersonName + 
			  					"'. Please review the revised details below and click Save, or enter an alternative contact email address.";
			            filledForm.get().contactPerson = existingContact;
			            Logger.debug("refill: " + filledForm.get().contactPerson.name);
			            ValidationError e = new ValidationError("email", msg);
			            filledForm.reject(e);
			            return info(filledForm, id, filledForm.get().contactPerson.name);
		    		}
		        	filledForm.get().contactPerson = existingContact;
		    	} else {
		        	filledForm.get().contactPerson.save();
		    	}
		    	
               	CommunicationLog log = CommunicationLog.logHistory(Const.PERMISSION + " " + filledForm.get().status, filledForm.get(), filledForm.get().user, Const.UPDATE);
               	log.save();

    	        updateAllByTarget(filledForm.get().id, filledForm.get().target.id, filledForm.get().status);
    	        
    	        filledForm.get().target.licenseStatus = filledForm.get().status;
    	        filledForm.get().target.update();
//    	        TargetController.updateQaStatus(filledForm.get().target.title, filledForm.get().status);
    	        
		        filledForm.get().update(id);
		        flash("message", "Crawl Permission " + filledForm.get().name + " has been updated");
		        return redirect(routes.CrawlPermissionController.view(filledForm.get().id));
        	} else if (action.equals("delete")) {
        		CrawlPermission crawlPermission = CrawlPermission.findById(id);
		        flash("message", "Crawl Permission " + filledForm.get().name + " has been deleted");
            	crawlPermission.delete();
            	
        		return redirect(routes.CrawlPermissionController.index()); 
        	}
        }
        return null;
    }
    
    public static Result save() {
        Form<CrawlPermission> filledForm = form(CrawlPermission.class).bindFromRequest();
    	DynamicForm requestData = form().bindFromRequest();

    	String target = requestData.get("target.id");
    	Long targetId = -1L;
    	if (StringUtils.isNotBlank(target)) {
    		targetId = Long.valueOf(target);
    	}
    	
        if(filledForm.hasErrors()) {
    		Logger.debug("errors: " + filledForm.errors());
            return newInfo(filledForm, targetId, null);
        }
        
        String formUrl = requestData.get("target.formUrl");

    	String contactPersonName = filledForm.get().contactPerson.name;
    	String contactPersonEmail = filledForm.get().contactPerson.email;
    	
        if (StringUtils.isBlank(formUrl)) {
            ValidationError ve = new ValidationError("target.formUrl", "URL is required");
            filledForm.reject(ve);
            return newInfo(filledForm, targetId, null);
        }
        
        if (StringUtils.isBlank(contactPersonEmail)) {
            ValidationError ve = new ValidationError("contactPerson.email", "Email is required");
            filledForm.reject(ve);
            return newInfo(filledForm, targetId, null);
        }
        
    	ContactPerson existingContact = ContactPerson.findByEmail(contactPersonEmail);
    	
    	if (existingContact != null) {
	    	Logger.debug("validateForm contactPersonName: " + contactPersonName + "/" + existingContact.name);
	    	Logger.debug("validateForm contactPersonEmail: " + contactPersonEmail + "/" + existingContact.email);
    		if (StringUtils.isNotEmpty(existingContact.name) && StringUtils.isNotEmpty(existingContact.email) && 
    			StringUtils.isNotBlank(contactPersonName) && StringUtils.isNotBlank(contactPersonEmail) 
    			&& existingContact.email.equals(contactPersonEmail) && !existingContact.name.equals(contactPersonName)) {
    			// matching emails but names don't match
    	    	Logger.debug("validateForm validation error");

	    		String msg = "A contact person with email '" + contactPersonEmail + 
	  					"' is already in the Contact Persons list, but with the Name '" + existingContact.name + 
	  					"' which is different from the given name '" + contactPersonName + 
	  					"'. Please review the revised details below and click Save, or enter an alternative contact email address.";
	            filledForm.get().contactPerson = existingContact;
	            Logger.debug("refill: " + filledForm.get().contactPerson.name);
	            ValidationError e = new ValidationError("email", msg);
	            filledForm.reject(e);
	            return newInfo(filledForm, targetId, filledForm.get().contactPerson.name);
    		}
        	filledForm.get().contactPerson = existingContact;
    	} else {
        	filledForm.get().contactPerson.save();
    	}
		
        Logger.debug("save user: " + filledForm.get().user);
        Logger.debug("save contactUser: " + filledForm.get().contactPerson);

        filledForm.get().thirdPartyContent = Boolean.FALSE;
        filledForm.get().agree = Boolean.FALSE;
        filledForm.get().publish = Boolean.TRUE;
        
       	CommunicationLog log = CommunicationLog.logHistory(Const.PERMISSION + " " + filledForm.get().status, filledForm.get(), filledForm.get().user, Const.SAVE);
       	log.save();
//
        updateAllByTarget(filledForm.get().id, filledForm.get().target.id, filledForm.get().status);
        filledForm.get().target.licenseStatus = filledForm.get().status;    	        
        filledForm.get().target.update();

        filledForm.get().contactPerson.save();
//		queued etc // no need for this as you can get it using target.crawlpermissions
//        TargetController.updateQaStatus(filledForm.get().target.title, filledForm.get().status);
        Logger.debug("save user: " + filledForm.get().user);
        filledForm.get().save();
        
        return redirect(routes.CrawlPermissionController.view(filledForm.get().id));
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
		    		Logger.debug("getFormParam(permission.name): " + getFormParam(permission.name) + " " + permission.name);
		            boolean userFlag = Utils.INSTANCE.getNormalizeBooleanString(getFormParam(permission.name));
		            if (userFlag) {
		            	if (assignedPermissions.length() == 0) {
		            		assignedPermissions = permission.name;
		            	} else {
		            		assignedPermissions = assignedPermissions + Const.COMMA + " " + permission.name;
		            	}
		            }
		        }
		    }
			Logger.debug("assignedPermissions: " + assignedPermissions);
		} catch (Exception e) {
			Logger.debug("send some exception" + e);
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
		    		Logger.debug("getFormParam(permission.name): " + getFormParam(permission.name) + " " + permission.name);
		            boolean userFlag = Utils.INSTANCE.getNormalizeBooleanString(getFormParam(permission.name));
		            if (userFlag) {
	            		assignedPermissionsList.add(CrawlPermission.findByName(permission.name));
		            }
		        }
		    }
			Logger.debug("assignedPermissions: " + assignedPermissionsList);
		} catch (Exception e) {
			Logger.debug("send some exception" + e);
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
//        		Logger.debug("getFormParam(permission.name): " + getFormParam(permission.name) + " " + permission.name);
                boolean userFlag = Utils.INSTANCE.getNormalizeBooleanString(getFormParam(permission.name));
                if (userFlag) {
                	if (assignedPermissions.length() == 0) {
                		assignedPermissions = ContactPerson.findEmailsByUrls(permission.contactPerson.url, assignedPermissions);
                	} else {
                		assignedPermissions = assignedPermissions + Const.COMMA + " " + 
                				ContactPerson.findEmailsByUrls(permission.contactPerson.url, assignedPermissions);
                	}
                }
            }
        }
        assignedPermissions = assignedPermissions.replace(" ,", "");
		Logger.debug("assignedPermissions: " + assignedPermissions);
        return assignedPermissions;
    }
    
    /**
     * This method rejects selected crawl permissions and changes their status to 'EMAIL_REJECTED'.
     * @return
     */
    public static void rejectSelectedCrawlPermissions(List<CrawlPermission> permissionList) {
    	for (CrawlPermission permission : permissionList) {
        	permission.status = Const.CrawlPermissionStatus.EMAIL_REJECTED.name();
        	Logger.debug("new permission staus: " + permission.status);
           	Ebean.update(permission);                	
	        CommunicationLog log = CommunicationLog.logHistory(Const.PERMISSION + " " + permission.status, permission, permission.user, Const.UPDATE);
	        Ebean.save(log);
        	Logger.debug("updated permission name: " + permission.name + ", staus: " + permission.status);
	        updateAllByTarget(permission.id, permission.target.id, permission.status);
//        	        TargetController.updateQaStatus(permission.target.title, permission.status);
        }
    }
    
    /**
     * This method updates all crawl permissions associated for given target
     * when a permission for a target is 'Granted', all others are set to 'Superseded'.
     * @param url The identificaiton URL of a current crawl permission object
     * @param target The domain name (URL)
     * @param status The status of the permission workflow
     */
    public static void updateAllByTarget(Long crawlPermissionId, Long targetId, String status) {
    	if (status.equals(Const.CrawlPermissionStatus.GRANTED.name())) {
	    	ExpressionList<CrawlPermission> exp = CrawlPermission.find.where().eq("target.id", targetId);
	    	if (crawlPermissionId != null) {
	    		exp = exp.ne("id", crawlPermissionId);
	    	}
	    	List<CrawlPermission> permissionList = exp.query().findList();
	    	
	    	for (CrawlPermission permission : permissionList) {
		    	permission.status = Const.CrawlPermissionStatus.SUPERSEDED.name();
		    	Logger.debug("updateAllByTarget() permission: " + permission.name + " to SUPERSEDED");
               	permission.save();  
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
    	Logger.debug("Expression list size: " + permissionList.size());
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
			if (startPos == 0) {
				startPos = licenseUrl.indexOf("https://") + "https://".length();
			}

			int endPos = licenseUrl.substring(startPos).indexOf(Const.SLASH_DELIMITER) + startPos;
			if (StringUtils.isNotEmpty(serverName)) {
				res = serverName + licenseUrl.substring(endPos);
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
    public static boolean setPendingSelectedCrawlPermissions(String template, List<CrawlPermission> permissionList) throws ActException {
    	
    	boolean res = true;
        
        Logger.debug("template: " + template);
        for (CrawlPermission permission : permissionList) {
        	Logger.debug("mail to contact person: " + permission.contactPerson.name.replace(Const.LIST_DELIMITER,"") + ".");
        	Logger.debug("mail template: " + template);
        	ContactPerson contactPerson = permission.contactPerson;
    		String email = contactPerson.email;
//                	String[] toMailAddresses = Utils.getMailArray(email);
    		String messageBody = Const.NONE_VALUE;
        	String messageSubject = Const.NONE_VALUE;
    		if (!template.equals(Const.NONE_VALUE)) {
            	MailTemplate mailTemplate = MailTemplate.findByName(template);
            	messageSubject = mailTemplate.subject;
//	                	messageBody = mailTemplate.text;
            	messageBody = mailTemplate.readTemplate();
            	String[] placeHolderArray = Utils.INSTANCE.getMailArray(mailTemplate.placeHolders);
        		Logger.debug("setPendingSelectedCrawlPermissions permission.target: " + permission.target.title);
        		
        		String licenseUrl = routes.LicenseController.form(permission.token).absoluteURL(request()).toString();
        		Logger.debug("setPendingSelectedCrawlPermissions current: " + licenseUrl);
        		
        		licenseUrl = injectServerName(licenseUrl);
        		Logger.debug("setPendingSelectedCrawlPermissions new: " + licenseUrl);
            	messageBody = CrawlPermission.
                	replaceTwoStringsInText(
                			messageBody
    						, Const.PLACE_HOLDER_DELIMITER + placeHolderArray[0] + Const.PLACE_HOLDER_DELIMITER
    						, Const.PLACE_HOLDER_DELIMITER + placeHolderArray[1] + Const.PLACE_HOLDER_DELIMITER
    						, permission.target.title
    						, licenseUrl);
    		} else {
    			Logger.debug("selected 'None' template type");
    		}
    		Logger.debug("email: " + email + ", " + messageSubject + ", " + messageBody);
        	if (StringUtils.isNotBlank(email)) {
                EmailHelper.sendMessage(email, messageSubject, messageBody);                	
//                    EmailHelper.sendMessage(toMailAddresses, messageSubject, messageBody);                	
            	permission.status = Const.CrawlPermissionStatus.PENDING.name();
            	Logger.debug("new permission staus: " + permission.status);
               	Ebean.update(permission);   
    	        CommunicationLog log = CommunicationLog.logHistory(Const.PERMISSION + " " + permission.status, permission, permission.user, Const.UPDATE);
    	        Ebean.save(log);
            	Logger.debug("updated permission name: " + permission.name + ", staus: " + permission.status);
    	        updateAllByTarget(permission.id, permission.target.id, permission.status);
//	        	        TargetController.updateQaStatus(permission.target.title, permission.status);
        	} else {
        		throw new ActException("Missing contact email. Please check contact person");
//	                	Logger.debug("Missing contact email. Please check contact person");
//	        	        res = false;
//	        	        break;
        	}
        }
        return res;
    }
    
    /**
     * This method handles queued crawl permissions.
     */
    public static Result send() throws ActException {
		Logger.debug("send crawl permission");
    	Result res = ok();
        DynamicForm requestData = Form.form().bindFromRequest();
    	String action = requestData.get("action");

    	
    	if (StringUtils.isNotBlank(action)) {
		    String template = Const.DEFAULT_TEMPLATE;
		    String temp = requestData.get("template");
		    
	        if (StringUtils.isNotBlank(temp)) {
		    	template = temp;
		    }
	        
	        String status = requestData.get("statusValue");
	    	Logger.debug("status: " + status);

//	    	String toMails = evaluateToEmails();
//	    	Logger.debug("toMails: " + toMails);
	
		    Map<String, String[]> formParams = request().body().asFormUrlEncoded();
	        String[] permissionValues = formParams.get("permissionsList");
	
	        List<CrawlPermission> crawlPermissions = new ArrayList<CrawlPermission>();
	        
	        if (permissionValues != null && permissionValues.length > 0) {
		        for (String permissionValue : permissionValues) {
		        	if (StringUtils.isNotBlank(permissionValue)) {
		        		Long permissionId = Long.valueOf(permissionValue);
		        		CrawlPermission crawlPermission = CrawlPermission.findById(permissionId);
		        		crawlPermissions.add(crawlPermission);
		        	}
		        }
	        }
	    	Logger.debug("crawlPermissions: " + crawlPermissions);
	    	
//	    	if (action.equals("sendall")) {
//	        	Logger.debug("send all crawl permission requests");
//	            boolean sendingRes = setPendingSelectedCrawlPermissions(template, crawlPermissions);
//	            if (!sendingRes) {
//	    			flash("message", "Missing contact email. Please check contact person");
//	            }
//	        	res = redirect(routes.CrawlPermissionController.index()); 
//	        }
	        if (action.equals("sendsome")) {
	        	Logger.debug("send some crawl permission requests");
	        	boolean sendingRes = setPendingSelectedCrawlPermissions(template, crawlPermissions);//messageBody, messageSubject); 
	            if (!sendingRes) {
	    			flash("message", "Missing contact email. Please check contact person");
	            }
		        res = redirect(routes.CrawlPermissionController.index()); 
	        }
	        
	        if (action.equals("preview")) {
	        	Logger.debug("preview crawl permission requests");
	        	if (crawlPermissions.size() > 1) {
		        	Logger.debug("crawlPermissions.size(): " + crawlPermissions.size() + " - " + status);
	    			flash("message", "Please select only one email to preview");
		        	res = redirect(routes.CrawlPermissionController.list(
		        			0, Const.NAME, Const.ASC, "", status, Const.SELECT_ALL));
	    	    	return res;
	        	}
	        	
	        	CrawlPermission crawlPermission = null;
	        	
		        if (permissionValues != null && permissionValues.length > 0) {
		        	// should only be one after the above
		        	String permissionValue = permissionValues[0];
		        	if (StringUtils.isNotBlank(permissionValue)) {
		        		Long permissionId = Long.valueOf(permissionValue);
		        		crawlPermission = CrawlPermission.findById(permissionId);
		            	MailTemplate mailTemplate = MailTemplate.findByName(template);
		            	String toMails = crawlPermission.contactPerson.email;
		            	String messageSubject = mailTemplate.subject;
		            	String messageBody = mailTemplate.readTemplate();
		            	messageBody = mailTemplate.readTemplate();
		            	
		            	String[] placeHolderArray = Utils.INSTANCE.getMailArray(mailTemplate.placeHolders);
		        		String licenseUrl = routes.LicenseController.form(crawlPermission.token).absoluteURL(request()).toString();
		        		licenseUrl = injectServerName(licenseUrl);
		            	messageBody = CrawlPermission.
		                	replaceTwoStringsInText(
		                			messageBody
		    						, Const.PLACE_HOLDER_DELIMITER + placeHolderArray[0] + Const.PLACE_HOLDER_DELIMITER
		    						, Const.PLACE_HOLDER_DELIMITER + placeHolderArray[1] + Const.PLACE_HOLDER_DELIMITER
		    						, crawlPermission.target.title
		    						, licenseUrl);
		            	
				        res = ok(
				            crawlpermissionpreview.render(
					            	crawlPermission, User.findByEmail(request().username()), toMails, template, messageSubject, messageBody
				            )
				        );
			        }
		        }
	        }
	        
	        if (action.equals("reject")) {
	        	Logger.debug("reject crawl permission requests");
	        	rejectSelectedCrawlPermissions(crawlPermissions);        	
		        res = redirect(routes.CrawlPermissionController.index()); 
	        }
	        
	        if (action.equals("selectall")) {
	        	Logger.debug("select all listed in page crawl permissions");
	        	res = redirect(routes.CrawlPermissionController.list(
	        			0, Const.NAME, Const.ASC, "", Const.DEFAULT_CRAWL_PERMISSION_STATUS, Const.SELECT_ALL));
	        }
	        if (action.equals("deselectall")) {
	        	Logger.debug("deselect all listed in page crawl permissions");
	        	res = redirect(routes.CrawlPermissionController.list(
	        			0, Const.NAME, Const.ASC, "", Const.DEFAULT_CRAWL_PERMISSION_STATUS, Const.DESELECT_ALL));
	        }
    	}
        return res;
    }
    
    /**
     * This method sends email preview to the user.
     */
    public static Result sendPreview() {
		Logger.debug("send preview");
    	Result res = ok();
        String preview = getFormParam(Const.PREVIEW);
        if (preview != null) {
        	boolean sendingRes = true;
        	Logger.debug("mail to contact person:" + getFormParam(Const.EMAIL) + ".");
    		String email = request().username(); //getFormParam(Const.EMAIL);
        	String messageSubject = getFormParam(Const.SUBJECT);
        	String messageBody = getFormParam(Const.BODY);
        	if (email != null) {
                EmailHelper.sendMessage(email, messageSubject, messageBody);                	
        	} else {
            	Logger.debug("Missing contact email. Please check contact person");
            	sendingRes = false;
        	}
            if (!sendingRes) {
    			flash("message", "Missing contact email. Please check contact person");
            }
	        res = redirect(routes.CrawlPermissionController.index()); 
        }
        return res;
    }
    
    /**
     * This method checks if crawl permission for given target already exists.
     * @param target
     * @return true if exists false otherwise
     */
    public static boolean checkCrawlPermissionTarget(String target) {
    	Logger.debug("checkCrawlPermissionTarget target: " + target);
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
    	Logger.debug("crawlPermissionExist target: " + target);
    	boolean res = checkCrawlPermissionTarget(target);
    	Logger.debug("crawl permission exists res: " + res + ", target: " + target);
    	return ok(Json.toJson(res));
    }
    
    /**
     * This method is checking if crawl permission for given target already exists and returns result in JSON format.
     * @param target
     * @return JSON result
     */
    public static Result crawlPermissionExistAtHigherLevel(String target) {
    	Logger.debug("crawlPermissionExistAtHigherLevel target: " + target);
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
		    			Logger.debug("crawlPermissionExistAtHigherLevel error: " + e);
		    		}
		        }
	    	}
    	}
    	Logger.debug("crawl permission in higher level exists res: " + res + ", target: " + target);
    	return ok(Json.toJson(res));
    }
    
    /**
     * This method is checking if crawl permission for given target for higher level
     * would be possible and returns result in JSON format.
     * @param target
     * @return JSON result
     */
    public static Result checkForHigherLevelPrompt(String target) {
    	Logger.debug("checkForHigherLevelPrompt target: " + target);
    	boolean res = false;
    	if (target != null) {
    		if (target.contains(Const.SLASH_DELIMITER)) {
		    	String[] parts = target.split(Const.SLASH_DELIMITER);
		    	if (parts.length > 1) {
		    		res = true;
		    	}
	    	}
    	}
    	Logger.debug("crawl permission in higher level exists res: " + res + ", target: " + target);
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
	        if (permission.contactPerson == null) {
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
    
    public static CrawlPermission setContactPerson(CrawlPermission crawlPermission, String url) { 
    	crawlPermission.contactPerson.url = url;
    	return crawlPermission;
    }

}

