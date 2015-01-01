package controllers;

import static play.data.Form.form;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import models.Collection;
import models.Subject;
import models.Target;
import models.Taxonomy;
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
import uk.bl.api.Utils;
import views.html.subjects.*;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Page;
import com.fasterxml.jackson.databind.JsonNode;

@Security.Authenticated(SecuredController.class)
public class SubjectController extends AbstractController {
	
    final static Form<Subject> subjectForm = new Form<Subject>(Subject.class);

	/**
	 * Display the subjects.
	 */
	public static Result index() {
		Logger.debug("Subjects.index()");
		return GO_HOME;
	}

	public static Result GO_HOME = redirect(routes.SubjectController.list(0, "name",
			"asc", ""));

	/**
	 * Display the paginated list of subjects.
	 * 
	 * @param page
	 *            Current page number (starts from 0)
	 * @param sortBy
	 *            Column to be sorted
	 * @param order
	 *            Sort order (either asc or desc)
	 * @param filter
	 *            Filter applied on target urls
	 */
	public static Result list(int pageNo, String sortBy, String order,
			String filter) {
		JsonNode node = getSubjectsDataByFilter(filter);
		
		Page<Subject> pages = Subject.pager(pageNo, 10, sortBy, order, filter);
		
		return ok(list.render("Subjects", User.findByEmail(request().username()), filter, pages, sortBy, order, node));
	}
	
	/**
	 * This method enables searching for given URL and redirection in order to
	 * add new entry if required.
	 * 
	 * @return
	 */
	public static Result search() {
    	DynamicForm form = form().bindFromRequest();
    	String action = form.get("action");
    	String query = form.get(Const.URL);

		Logger.debug("action: " + action);
    	Logger.debug("subjects search() query: " + query);
    	
    	if (StringUtils.isBlank(query)) {
			Logger.debug("Subject name is empty. Please write name in search window.");
			flash("message", "Please enter a name in the search window");
	        return redirect(
	        		routes.SubjectController.list(0, "name", "asc", "")
	        );
    	}
    	
    	int pageNo = Integer.parseInt(form.get(Const.PAGE_NO));
    	String sort = form.get(Const.SORT_BY);
    	String order = form.get(Const.ORDER);


    	if (StringUtils.isEmpty(action)) {
    		return badRequest("You must provide a valid action");
    	} else {
    		if (Const.SEARCH.equals(action)) {
    	    	return redirect(routes.SubjectController.list(pageNo, sort, order, query));
		    } else {
		      return badRequest("This action is not allowed");
		    }
    	}
	}

	@BodyParser.Of(BodyParser.Json.class)
    public static Result filterByJson(String name) {
    	JsonNode jsonData = null;
        if (name != null) {
	        List<Taxonomy> subjects = Taxonomy.filterSubjectsByName(name);
	        jsonData = Json.toJson(subjects);
        }
        return ok(jsonData);
    }
	    
	  
    public static Result view(Long id) {
    	User user = User.findByEmail(request().username());
        return ok(view.render(Subject.findById(id), user));
    }
    
    public static Result newForm() {
    	User user = User.findByEmail(request().username());
		JsonNode node = getSubjectsData();
		Form<Subject> subjectForm = Form.form(Subject.class);
		Subject subject = new Subject();
		subjectForm = subjectForm.fill(subject);
        return ok(newForm.render(subjectForm, user, node));
    	
    }

    public static Result edit(Long id) {
    	User user = User.findByEmail(request().username());
		Subject subject = Subject.findById(id);
		List<Subject> thisSubject = new ArrayList<Subject>();
		thisSubject.add((Subject)subject.parent);
		JsonNode node = getSubjectsData(thisSubject);
		Form<Subject> subjectForm = Form.form(Subject.class);
		subjectForm = subjectForm.fill(subject);
		Logger.debug("id: " + subjectForm.get().id);
        return ok(edit.render(subjectForm, user, id, node));        
        
    }

    public static Result info(Form<Subject> form, Long id) {
    	Logger.debug("info");
    	User user = User.findByEmail(request().username());
		List<Subject> thisSubject = new ArrayList<Subject>();
		Subject subject = form.get();
		thisSubject.add(subject);
		JsonNode node = getSubjectsData(thisSubject);
		return badRequest(edit.render(form, user, id, node));
    }
    
	public static Result newInfo(Form<Subject> form) {
		User user = User.findByEmail(request().username());
		JsonNode node = getSubjectsData();
        return badRequest(newForm.render(form, user, node));
	}


    public static Result save() {
    	
    	DynamicForm requestData = form().bindFromRequest();
    	String action = requestData.get("action");

    	Logger.debug("action: " + action);
    	
        if (StringUtils.isNotEmpty(action)) {
        	if (action.equals("save")) {
		        Form<Subject> filledForm = form(Subject.class).bindFromRequest();
		        if(filledForm.hasErrors()) {
	        		Logger.debug("errors: " + filledForm.errors());
		            return newInfo(filledForm);
		        }
		        
	            String subjectSelect = requestData.get("subjectSelect").replace("\"", "");
	            Logger.debug("subjectSelect: " + subjectSelect);
	            if (StringUtils.isNotEmpty(subjectSelect)) {
	                String[] subjects = subjectSelect.split(", ");
	                if (subjects.length == 1) {
	                	Long subjectId = Long.valueOf(subjects[0]);
		            	Subject subject = Subject.findById(subjectId);
	                	filledForm.get().parent = subject;
	                	Logger.debug("looking good");
	                }
	                else if (subjects.length > 1) {
	                	Logger.debug("Please select only one parent.");
	    	  			flash("message", "Please select only one parent.");
	    	  			return newInfo(filledForm);
	                }
	            }		        
		        
		        filledForm.get().save();
		        flash("message", "Subject " + filledForm.get().name + " has been created");
		        return redirect(routes.SubjectController.view(filledForm.get().id));
        	}
        }
        return null;    	
    }
    
    public static Result update(Long id) {
    	DynamicForm requestData = form().bindFromRequest();
        Form<Subject> filledForm = form(Subject.class).bindFromRequest();
    	Logger.debug("hasGlobalErrors: " + filledForm.hasGlobalErrors());
    	Logger.debug("hasErrors: " + filledForm.hasErrors());

    	String action = requestData.get("action");

    	Logger.debug("action: " + action);
    	
        if (StringUtils.isNotEmpty(action)) {
        	if (action.equals("save")) {    
		        if (filledForm.hasErrors()) {
		        	Logger.debug("hasErrors: " + filledForm.errors());
		            return info(filledForm, id);
		        }
		        
	            String subjectSelect = requestData.get("subjectSelect").replace("\"", "");
	            Logger.debug("subjectSelect: " + subjectSelect);
	            if (StringUtils.isNotEmpty(subjectSelect)) {
	                String[] subjects = subjectSelect.split(", ");
	                if (subjects.length == 1) {
	                	Long subjectId = Long.valueOf(subjects[0]);
	                	if (subjectId.longValue() == id.longValue()) {
	                		Logger.debug("same id");
	        	            ValidationError e = new ValidationError("subjectSelect", "It is not possible to assign a node to itself as a parent. Please select one parent.");
	        	            filledForm.reject(e);
	        	  			return info(filledForm, id);
	                	} else {
			            	Subject subject = Subject.findById(subjectId);
		                	filledForm.get().parent = subject;
		                	Logger.debug("looking good");
	                	}
	                }
	                else if (subjects.length > 1) {
	                	Logger.debug("Please select only one parent.");
	    	  			flash("message", "Please select only one parent.");
	    	  			return info(filledForm, id);
	                }
	            }		        
		        
		        filledForm.get().update(id);
		        flash("message", "Collection " + filledForm.get().name + " has been updated");
		        return redirect(routes.SubjectController.view(filledForm.get().id));
        	} else if (action.equals("delete")) {
        		Subject subject = Subject.findById(id);
		        flash("message", "Subject " + filledForm.get().name + " has been deleted");
            	subject.delete();
        		return redirect(routes.SubjectController.index()); 
        	}
        }
        return null;
    }
    
    /**
     * This method demonstrates targets associated with given subject.
     * @param url The URL identifier for subject
     * @return
     */
    public static Result sites(Long id) {
        return redirect(routes.TargetController.subjectTargets(0, Const.TITLE, Const.ASC, "", id));
    }  
    

    

    /**
     * This method presents subjects in a tree form.
     * @param url
     * @return
     */
    private static JsonNode getSubjectsTree(String url) {
        JsonNode jsonData = null;
        final StringBuffer sb = new StringBuffer();
    	List<Taxonomy> parentSubjects = Taxonomy.findListByTypeSorted(Const.SUBJECT);
    	if (url != null && url.length() > 0) {
    		try {
	    		Taxonomy subject = Taxonomy.findByUrl(url);
	    		if (subject != null) {
//	    			url = subject.parent.id;
	    		}
    		} catch (Exception e) {
    			Logger.debug("New subject has no parent yet.");
    		}
    	}    	
    	sb.append(getSubjectTreeElementsExt(parentSubjects, url, true));
//    	Logger.debug("collections main level size: " + suggestedCollections.size());
        jsonData = Json.toJson(Json.parse(sb.toString()));
//    	Logger.debug("getCollections() json: " + jsonData.toString());
        return jsonData;
    }
    
    /**
   	 * This method calculates first order subjects.
     * @param subjectList The list of all subjects
     * @param url This is an identifier for current subject object
     * @param parent This parameter is used to differentiate between root and children nodes
     * @return collection object in JSON form
     */
    public static String getSubjectTreeElementsExt(List<Taxonomy> subjectList, String url, boolean parent) { 
//    	Logger.debug("getSubjectTreeElements() URL: " + url);
    	String res = "";
    	if (subjectList.size() > 0) {
	        final StringBuffer sb = new StringBuffer();
	        sb.append("[");
	        if (parent) {
	        	sb.append("{\"title\": \"" + "None" + "\"," + checkNone(url) + 
	        			" \"key\": \"" + "None" + "\"" + "}, ");
	        }
	    	Iterator<Taxonomy> itr = subjectList.iterator();
	    	boolean firstTime = true;
	    	while (itr.hasNext()) {
	    		Taxonomy subject = itr.next();
//    			Logger.debug("add subject: " + subject.name + ", with url: " + subject.url +
//    					", parent:" + subject.parent + ", parent size: " + subject.parent.length());
//	    		if ((parent && subject.parent == null) || !parent || subject.parent == null) {
//		    		if (firstTime) {
//		    			firstTime = false;
//		    		} else {
//		    			sb.append(", ");
//		    		}
////	    			Logger.debug("added");
//					sb.append("{\"title\": \"" + subject.name + "\"," + checkSelection(subject.name, url) + 
//							" \"key\": \"" + subject.url + "\"" + 
//							getSubjectChildren(subject.url, url) + "}");
//	    		}
	    	}
//	    	Logger.debug("subjectList level size: " + subjectList.size());
	    	sb.append("]");
	    	res = sb.toString();
//	    	Logger.debug("getSubjectTreeElements() res: " + res);
    	}
    	return res;
    }
        
    /**
     * Check if none value is selected
     * @param url This is an identifier for current subject object
     * @return
     */
    public static String checkNone(String url) {
    	String res = "";
		if (StringUtils.isNotEmpty(url) && url.toLowerCase().equals(Const.NONE.toLowerCase())) {
			res = "\"select\": true ,";
		}
    	return res;
    }
    
    /**
     * Mark subjects that are stored in target object as selected
     * @param subjectUrl The subject identifier
     * @param currentUrl This is an identifier for current subject object
     * @return
     */
    public static String checkSelection(String subjectUrl, String currentUrl) {
    	String res = "";
		if (currentUrl.contains(Const.COMMA)) {
			currentUrl = currentUrl.replace(Const.COMMA, Const.COMMA + " "); // in database entry with comma has additional space after comma
		}
//    	Logger.debug("checkSelection() 1: " + subjectUrl + ", 2: " + currentUrl);
    	if (currentUrl != null && currentUrl.length() > 0) {
    		if (currentUrl.equals(subjectUrl) || currentUrl.equals(subjectUrl)) {
    			res = "\"select\": true ,";
    		}
    	}
    	return res;
    }
    
    /**
     * This method calculates subject children - objects that have parents.
     * @param url The identifier for parent 
     * @param currentUrl This is an identifier for current subject object
     * @return child subject in JSON form
     */
    public static String getSubjectChildren(String url, String currentUrl) {
//    	Logger.debug("getChildren() target URL: " + targetUrl);
    	String res = "";
        final StringBuffer sb = new StringBuffer();
    	sb.append(", \"children\":");
//    	List<Taxonomy> childSubjectList = Taxonomy.findListByType(Const.SUBSUBJECT);
//    	List<Taxonomy> childSubjectList = Taxonomy.getChildLevelSubjects(url);
    	Taxonomy subject = Taxonomy.findByUrl(url);
    	List<Taxonomy> childSubjectList = Taxonomy.findSubSubjectsList(subject.name);
//    	List<Taxonomy> childSubjectList = Taxonomy.getChildLevelSubjects(subject.name);
    	if (childSubjectList.size() > 0) {
	    	sb.append(getSubjectTreeElementsExt(childSubjectList, currentUrl, false));
	    	res = sb.toString();
//	    	Logger.debug("getChildren() res: " + res);
    	}
    	return res;
    }
    
}
