package controllers;

import static play.data.Form.form;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import models.Subject;
import models.Target;
import models.Taxonomy;
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
		Logger.info("Subjects.index()");
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

		Logger.info("action: " + action);
    	Logger.info("subjects search() query: " + query);
    	
    	if (StringUtils.isBlank(query)) {
			Logger.info("Subject name is empty. Please write name in search window.");
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
    		if (Const.ADDENTRY.equals(action)) {
//        		return redirect(routes.SubjectController.create(query));
    	        Logger.info("create subject()");
    	    	Subject subject = new Subject();
    	    	subject.name = query;
//    	        subject.id = Target.createId();
//    	        subject.url = Const.ACT_URL + subject.tid;
    			Logger.info("add subject with url: " + subject.url + ", and name: " + subject.name);
    			JsonNode node = getSubjectsTree(subject.url);
    			Form<Subject> subjectForm = Form.form(Subject.class);
    			subjectForm = subjectForm.fill(subject);
    	        return ok(edit.render(subjectForm, User.findByEmail(request().username()), node));    			
    		} 
    		else if (Const.SEARCH.equals(action)) {
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
        return ok(
                view.render(
                        Subject.findById(id), User.findByEmail(request().username())
                )
            );
    }
    
    /**
     * Add new subject entry.
     * @param subject name
     * @return
     */
    public static Result create(String name) {
        Logger.info("create subject()");
    	Subject subject = new Subject();
    	subject.name = name;
    	// TODO: createId
//        subject.id = Target.createId();
//        subject.url = Const.ACT_URL + subject.id;
		Logger.info("add subject with url: " + subject.url + ", and name: " + subject.name);
		JsonNode node = getSubjectsTree(subject.url);
		Form<Subject> subjectForm = Form.form(Subject.class);
		subjectForm = subjectForm.fill(subject);
        return ok(edit.render(subjectForm, User.findByEmail(request().username()), node));
    }
    
    /**
     * Display the subject edit panel for this URL.
     */
    public static Result edit(Long id) {
    	User user = User.findByEmail(request().username());
		Subject subject = Subject.findById(id);
		List<Subject> thisSubject = new ArrayList<Subject>();
		thisSubject.add((Subject)subject.parent);
		JsonNode node = getSubjectsData(thisSubject);
		Form<Subject> subjectForm = Form.form(Subject.class);
		subjectForm = subjectForm.fill(subject);
		Logger.info("id: " + subjectForm.get().id);
        return ok(edit.render(subjectForm, user, node));        
        
    }

	/**
	 * This method prepares Subject form for sending info message
	 * about errors 
	 * @return edit page with form and info message
	 */
	public static Result info() {
    	Subject subject = new Subject();
    	// TODO: createId
//    	subject.id = Long.valueOf(getFormParam(Const.TID));
//    	subject.url = getFormParam(Const.URL);
    	subject.name = getFormParam(Const.NAME);
        subject.publish = Utils.getNormalizeBooleanString(getFormParam(Const.PUBLISH));
	    if (getFormParam(Const.TTYPE) != null) {
	    	subject.ttype = getFormParam(Const.TTYPE);
	    }
//	    subject.ttype = Const.SUBJECT;
//	    if (getFormParam(Const.PARENT) != null) {
//        	if (!getFormParam(Const.PARENT).toLowerCase().contains(Const.NONE)) {
//        		subject.parent = getFormParam(Const.PARENT);
//        	    subject.ttype = Const.SUBSUBJECT;
//        	}
//	    }
        if (getFormParam(Const.TREE_KEYS) != null) {
//    		subject.parent = Utils.removeDuplicatesFromList(getFormParam(Const.TREE_KEYS));
    		Logger.debug("subject parent: " + subject.parent);
        	if (!getFormParam(Const.TREE_KEYS).toLowerCase().contains(Const.NONE)) {
        	    subject.ttype = Const.SUBSUBJECT;
        	}
        }
        if (getFormParam(Const.DESCRIPTION) != null) {
        	subject.description = getFormParam(Const.DESCRIPTION);
        }
		JsonNode node = getSubjectsTree(subject.url);
		Form<Subject> subjectFormNew = Form.form(Subject.class);
		subjectFormNew = subjectFormNew.fill(subject);
      	return ok(
	              edit.render(subjectFormNew, User.findByEmail(request().username()), node)
	            );
    }
    
    /**
     * This method saves new object or changes on given Subject in the same object
     * completed by revision comment. The "version" field in the Subject object
     * contains the timestamp of the change. 
     * @return
     */
    public static Result save() {

    	Result res = null;
    	DynamicForm requestData = form().bindFromRequest();
    	String action = requestData.get("action");

        if (StringUtils.isNotEmpty(action)) {
    		Form<Subject> filledForm = subjectForm.bindFromRequest();

        	Long id = filledForm.get().id;

        	if (action.equals("save")) {
	            if(filledForm.hasErrors()) {
	            	String missingFields = "";
	            	for (String key : subjectForm.errors().keySet()) {
	            	    Logger.debug("key: " +  key);
	            	    key = Utils.showMissingField(key);
	            	    if (missingFields.length() == 0) {
	            	    	missingFields = key;
	            	    } else {
	            	    	missingFields = missingFields + Const.COMMA + " " + key;
	            	    }
	            	}
	            	Logger.info("form errors size: " + subjectForm.errors().size() + ", " + missingFields);
		  			flash("message", "Please fill out all the required fields, marked with a red star." + 
		  					" Missing fields are: " + missingFields);
		  			return info(filledForm);
	            }
	        	
	            Subject subjectFromDB = Subject.findById(id);
	            Subject subjectFromForm = filledForm.get();
	            
	            subjectFromDB.name = subjectFromForm.name;
	            subjectFromDB.publish = subjectFromForm.publish;
	            subjectFromDB.description = subjectFromForm.description;
	            subjectFromDB.revision = subjectFromForm.revision;
	            
	            String subjectSelect = requestData.get("subjectSelect").replace("\"", "");
	            Logger.info("subjectSelect: " + subjectSelect);
	            if (StringUtils.isNotEmpty(subjectSelect)) {
	                String[] subjects = subjectSelect.split(", ");
	                if (subjects.length == 1) {
	                	Long subjectId = Long.valueOf(subjects[0]);
	                	if (subjectId == id) {
	                		Logger.info("same id");
	        	  			flash("message", "It is not possible to assign a node to itself as a parent. Please select one parent.");
	        	  			return info(filledForm);
	                	} else {
			            	Subject subject = Subject.findById(subjectId);
		                	subjectFromDB.parent = subject;
		                	Logger.info("looking good");
	                	}
	                }
	                else if (subjects.length > 1) {
	                	Logger.info("Please select only one parent.");
	    	  			flash("message", "Please select only one parent.");
	    	  			return info(filledForm);
	                }
	            }
	            subjectFromDB.save();
	            
		        res = redirect(routes.SubjectController.view(subjectFromDB.id));
        	} else if (action.equals("delete")) {
	        	Subject subject = Subject.findById(id);
	        	subject.delete();
    	        res = redirect(routes.SubjectController.index()); 
        	}
        } 
        return res;
    }
    
    public static Result info(Form<Subject> form) {
    	Logger.info("info");
    	User user = User.findByEmail(request().username());
		List<Subject> thisCollection = new ArrayList<Subject>();
		Subject collection = form.get();
		thisCollection.add(collection);
		JsonNode node = getSubjectsData(thisCollection);
		return ok(edit.render(form, user, node));
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
    			Logger.info("New subject has no parent yet.");
    		}
    	}    	
    	sb.append(getSubjectTreeElementsExt(parentSubjects, url, true));
//    	Logger.info("collections main level size: " + suggestedCollections.size());
        jsonData = Json.toJson(Json.parse(sb.toString()));
//    	Logger.info("getCollections() json: " + jsonData.toString());
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
//    	Logger.info("getSubjectTreeElements() URL: " + url);
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
//	    	Logger.info("subjectList level size: " + subjectList.size());
	    	sb.append("]");
	    	res = sb.toString();
//	    	Logger.info("getSubjectTreeElements() res: " + res);
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
//    	Logger.info("checkSelection() 1: " + subjectUrl + ", 2: " + currentUrl);
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
//    	Logger.info("getChildren() target URL: " + targetUrl);
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
//	    	Logger.info("getChildren() res: " + res);
    	}
    	return res;
    }
    
}
