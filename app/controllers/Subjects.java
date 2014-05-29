package controllers;

import static play.data.Form.form;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import models.DCollection;
import models.Organisation;
import models.Taxonomy;
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
import views.html.subjects.*;

import com.avaje.ebean.Ebean;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Security.Authenticated(Secured.class)
public class Subjects extends AbstractController {

	/**
	 * Display the subjects.
	 */
	public static Result index() {
		Logger.info("Subjects.index()");
		return GO_HOME;
	}

	public static Result GO_HOME = redirect(routes.Subjects.list(0, "name",
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
		JsonNode node = getSubjectsData(filter);
//		Logger.info("LookUp.list() " + node);
		
		return ok(list.render("Subjects",
				User.find.byId(request().username()), filter,
				Taxonomy.page(pageNo, 10, sortBy, order, filter), sortBy,
				order, node));
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
	        		routes.Subjects.list(0, "name", "asc", "")
	        );
    	}
    	
    	int pageNo = Integer.parseInt(form.get(Const.PAGE_NO));
    	String sort = form.get(Const.SORT_BY);
    	String order = form.get(Const.ORDER);


    	if (StringUtils.isEmpty(action)) {
    		return badRequest("You must provide a valid action");
    	} else {
    		if (Const.ADDENTRY.equals(action)) {
//        		return redirect(routes.Subjects.create(query));
    	        Logger.info("create subject()");
    	    	Taxonomy subject = new Taxonomy();
    	    	subject.name = query;
    	        subject.tid = Target.createId();
    	        subject.url = Const.ACT_URL + subject.tid;
    			Logger.info("add subject with url: " + subject.url + ", and name: " + subject.name);
    			JsonNode node = getSubjectsTree(subject.url);
    			Form<Taxonomy> subjectForm = Form.form(Taxonomy.class);
    			subjectForm = subjectForm.fill(subject);
    	        return ok(edit.render(subjectForm, User.find.byId(request().username()), node));    			
    		} 
    		else if (Const.SEARCH.equals(action)) {
    	    	return redirect(routes.Subjects.list(pageNo, sort, order, query));
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
	    
	  
    public static Result view(String url) {
        return ok(
                view.render(
                        Taxonomy.findByUrl(url), User.find.byId(request().username())
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
    	Taxonomy subject = new Taxonomy();
    	subject.name = name;
        subject.tid = Target.createId();
        subject.url = Const.ACT_URL + subject.tid;
		Logger.info("add subject with url: " + subject.url + ", and name: " + subject.name);
		JsonNode node = getSubjectsTree(subject.url);
		Form<Taxonomy> subjectForm = Form.form(Taxonomy.class);
		subjectForm = subjectForm.fill(subject);
        return ok(edit.render(subjectForm, User.find.byId(request().username()), node));
    }
    
    /**
     * Display the subject edit panel for this URL.
     */
    public static Result edit(String url) {
		Logger.info("subject url: " + url);
		Taxonomy subject = Taxonomy.findByUrl(url);
		Logger.info("subject name: " + subject.name + ", url: " + url);
		JsonNode node = getSubjectsTree(subject.url);
		Form<Taxonomy> subjectForm = Form.form(Taxonomy.class);
		subjectForm = subjectForm.fill(subject);
        return ok(edit.render(subjectForm, User.find.byId(request().username()), node));
    }

	/**
	 * This method prepares Subject form for sending info message
	 * about errors 
	 * @return edit page with form and info message
	 */
	public static Result info() {
    	Taxonomy subject = new Taxonomy();
    	subject.tid = Long.valueOf(getFormParam(Const.TID));
    	subject.url = getFormParam(Const.URL);
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
    		subject.parent = Utils.removeDuplicatesFromList(getFormParam(Const.TREE_KEYS));
    		Logger.debug("subject parent: " + subject.parent);
        	if (!getFormParam(Const.TREE_KEYS).toLowerCase().contains(Const.NONE)) {
        	    subject.ttype = Const.SUBSUBJECT;
        	}
        }
        if (getFormParam(Const.DESCRIPTION) != null) {
        	subject.description = getFormParam(Const.DESCRIPTION);
        }
		JsonNode node = getSubjectsTree(subject.url);
		Form<Taxonomy> subjectFormNew = Form.form(Taxonomy.class);
		subjectFormNew = subjectFormNew.fill(subject);
      	return ok(
	              edit.render(subjectFormNew, User.find.byId(request().username()), node)
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
        String save = getFormParam(Const.SAVE);
        String delete = getFormParam(Const.DELETE);
//        Logger.info("save: " + save);
        if (save != null) {
        	Logger.info("input data for saving subject tid: " + getFormParam(Const.TID) + ", url: " + getFormParam(Const.URL) + 
        			", name: " + getFormParam(Const.NAME) + ", parent: " + getFormParam(Const.TREE_KEYS));
        	
        	Form<Taxonomy> subjectForm = Form.form(Taxonomy.class).bindFromRequest();
            if(subjectForm.hasErrors()) {
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
	  			return info();
            }
        	
        	Taxonomy subject = null;
            boolean isExisting = true;
            try {
                try {
                	subject = Taxonomy.findByUrlExt(getFormParam(Const.URL));
                } catch (Exception e) {
                	Logger.info("is not existing exception");
                	isExisting = false;
                	subject = new Taxonomy();
                	subject.tid = Long.valueOf(getFormParam(Const.TID));
                	subject.url = getFormParam(Const.URL);
                }
                if (subject == null) {
                	Logger.info("is not existing");
                	isExisting = false;
                	subject = new Taxonomy();
                	subject.tid = Long.valueOf(getFormParam(Const.TID));
                	subject.url = getFormParam(Const.URL);
                }
                
                subject.name = getFormParam(Const.NAME);
                subject.publish = Utils.getNormalizeBooleanString(getFormParam(Const.PUBLISH));
        	    if (getFormParam(Const.TTYPE) != null) {
        	    	subject.ttype = getFormParam(Const.TTYPE);
        	    }
//        	    subject.ttype = Const.SUBJECT;
//        	    if (getFormParam(Const.PARENT) != null) {
//                	if (!getFormParam(Const.PARENT).toLowerCase().contains(Const.NONE)) {
//                		subject.parent = getFormParam(Const.PARENT);
//                	    subject.ttype = Const.SUBSUBJECT;
//                	}
//        	    }
                if (getFormParam(Const.TREE_KEYS) != null) {
            		if (StringUtils.isNotEmpty(getFormParam(Const.TREE_KEYS)) && getFormParam(Const.TREE_KEYS).contains(Const.COMMA)) {
                    	Logger.info("Please select only one parent.");
        	  			flash("message", "Please select only one parent.");
        	  			return info();
                    }
            		if (StringUtils.isNotEmpty(subject.parent) && subject.parent.equals(subject.url)) {
                    	Logger.info("It is not possible to assign a node to itself as a parent. Please select one parent.");
        	  			flash("message", "It is not possible to assign a node to itself as a parent. Please select one parent.");
        	  			return info();
                    }
            		String parentUrl = Utils.removeDuplicatesFromList(getFormParam(Const.TREE_KEYS));
            		if (parentUrl != null) {
            			if (!parentUrl.toLowerCase().equals(Const.NONE)) {
            				subject.parent = Taxonomy.findByUrlExt(parentUrl).name;
                        	if (!getFormParam(Const.TREE_KEYS).toLowerCase().contains(Const.NONE)) {
        	            	    subject.ttype = Const.SUBSUBJECT;
        	            	}
            			} else {
            				subject.parent = Const.NONE;
    	            	    subject.ttype = Const.SUBJECT;
            			}
            		}
//            		subject.parent = Utils.removeDuplicatesFromList(getFormParam(Const.TREE_KEYS));
            		Logger.debug("subject parent: " + subject.parent);
                }
                if (getFormParam(Const.DESCRIPTION) != null) {
                	subject.description = getFormParam(Const.DESCRIPTION);
                }
            } catch (Exception e) {
            	Logger.info("Subject not exists exception: " + e.getMessage());
            }
            
        	if (!isExisting) {
               	Ebean.save(subject);
    	        Logger.info("save subject: " + subject.toString());
        	} else {
           		Logger.info("update subject: " + subject.toString());
               	Ebean.update(subject);
        	}
	        res = redirect(routes.Subjects.edit(subject.url));
        } 
        if (delete != null) {
        	String url = getFormParam(Const.URL);
        	Logger.info("deleting: " + url);
        	Taxonomy subject = Taxonomy.findByUrl(url);
        	Ebean.delete(subject);
	        res = redirect(routes.Subjects.index()); 
        }
        return res;
    }
	    
    /**
     * This method computes a tree of subjects in JSON format. 
     * @param subjectUrl This is an identifier for current selected object
     * @return tree structure
     */
    private static JsonNode getSubjectsData(String url) {    	
    	List<Taxonomy> subjects = Taxonomy.findListByTypeSorted(Const.SUBJECT);
    	List<ObjectNode> result = getSubjectTreeElements(subjects, url, true);
    	Logger.info("subjects main level size: " + subjects.size());
    	JsonNode jsonData = Json.toJson(result);
//    	Logger.info("getSubjectsData() jsonData: " + jsonData);
        return jsonData;
    }
    
    /**
   	 * This method calculates first order subjects.
     * @param subjectList The list of all subjects
     * @param subjectUrl This is an identifier for current selected object
     * @param parent This parameter is used to differentiate between root and children nodes
     * @return subject object in JSON form
     */
    public static List<ObjectNode> getSubjectTreeElements(List<Taxonomy> subjectList, String subjectUrl, boolean parent) { 
		List<ObjectNode> result = new ArrayList<ObjectNode>();
		JsonNodeFactory nodeFactory = new JsonNodeFactory(false);

		if (subjectList.size() > 0) {
	    	Iterator<Taxonomy> itr = subjectList.iterator();
	    	while (itr.hasNext()) {
	    		Taxonomy subject = itr.next();
//	    		Logger.info("getSubjectTreeElements() subject name: " + subject.name + 
//	    				", subjectUrl: " + subjectUrl + ", parent: " + parent + ", subject.parent: " + subject.parent);
	    		if (subjectUrl.isEmpty() 
	    				|| (StringUtils.isNotEmpty(subjectUrl) && StringUtils.containsIgnoreCase(subject.name, subjectUrl))) {	    		
//		    		if ((parent && subject.parent.length() == 0) || !parent) {
			    	if ((parent && subject.parent.length() == 0) || !parent || (parent && subject.parent.equals(Const.NONE))) {
						ObjectNode child = nodeFactory.objectNode();
						child.put("title", subject.name);
						child.put("url", String.valueOf(routes.Subjects.view(subject.url)));
				    	if (StringUtils.isNotEmpty(subject.url) && subject.url.equalsIgnoreCase(subjectUrl)) {
				    		child.put("select", true);
				    	}
						child.put("key", "\"" + subject.url + "\"");
				    	List<Taxonomy> childSubjects = Taxonomy.findSubSubjectsList(subject.name);
				    	if (childSubjects.size() > 0) {
				    		child.put("children", Json.toJson(getSubjectTreeElements(childSubjects, subjectUrl, false)));
				    	}
						result.add(child);
		    		}
	    		}
	    	}
    	}
//    	Logger.info("getSubjectTreeElements() res: " + result);
    	return result;
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
	    		if (StringUtils.isNotEmpty(subject.parent)) {
	    			url = subject.parent;
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
	    		if ((parent && subject.parent.length() == 0) || !parent || subject.parent.equals(Const.NONE_VALUE)) {
		    		if (firstTime) {
		    			firstTime = false;
		    		} else {
		    			sb.append(", ");
		    		}
//	    			Logger.debug("added");
					sb.append("{\"title\": \"" + subject.name + "\"," + checkSelection(subject.name, url) + 
							" \"key\": \"" + subject.url + "\"" + 
							getSubjectChildren(subject.url, url) + "}");
	    		}
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
