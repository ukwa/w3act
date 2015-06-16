package controllers;

import static play.data.Form.form;

import java.util.ArrayList;
import java.util.List;

import models.Collection;
import models.User;

import org.apache.commons.lang3.StringUtils;

import play.Logger;
import play.Play;
import play.data.DynamicForm;
import play.data.Form;
import play.data.validation.ValidationError;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Result;
import play.mvc.Results;
import play.mvc.Security;
import play.mvc.With;
import uk.bl.Const;
import uk.bl.exception.ActException;
import views.html.collections.edit;
import views.html.collections.list;
import views.html.collections.view;
import views.html.collections.newForm;

import com.avaje.ebean.Page;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


/**
 * @author kli
 *
 */
/**
 * @author kli
 *
 */
public class CollectionController extends AbstractController {

	/**
	 * Display the Collections.
	 */
	@Security.Authenticated(SecuredController.class)
	public static Result index() {
		return GO_HOME;
	}

	public static Result GO_HOME = redirect(routes.CollectionController.list(0, "title", "asc", ""));

	/**
	 * Display the paginated list of collections.
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
	@Security.Authenticated(SecuredController.class)
	public static Result list(int pageNo, String sortBy, String order, String filter) {
		
		JsonNode node = getCollectionsDataByFilter(filter);
		
		Page<Collection> pages = Collection.pager(pageNo, 10, sortBy, order, filter);
		
		return ok(list.render("Collections", User.findByEmail(request().username()), filter, pages, sortBy, order, node));
	}
	
	/**
	 * This method enables searching for given URL and redirection in order to
	 * add new entry if required.
	 * 
	 * @return
	 */
	@Security.Authenticated(SecuredController.class)
	public static Result search() {
    	DynamicForm requestData = form().bindFromRequest();
    	String action = requestData.get("action");
    	String query = requestData.get(Const.URL);

    	if (StringUtils.isBlank(query)) {
			Logger.debug("Collection name is empty. Please write name in search window.");
			flash("message", "Please enter a name in the search window");
	        return redirect(routes.CollectionController.list(0, "title", "asc", ""));
    	}
    	
    	int pageNo = Integer.parseInt(requestData.get(Const.PAGE_NO));
    	String sort = requestData.get(Const.SORT_BY);
    	String order = requestData.get(Const.ORDER);


    	if (StringUtils.isEmpty(action)) {
    		return badRequest("You must provide a valid action");
    	} else {
    		if (action.equals("search")) {
    	    	return redirect(routes.CollectionController.list(pageNo, sort, order, query));
		    } else {
		      return badRequest("This action is not allowed");
		    }
    	}
	}

	@Security.Authenticated(SecuredController.class)
	@BodyParser.Of(BodyParser.Json.class)
    public static Result filterByJson(String name) {
    	JsonNode jsonData = null;
        if (name != null) {
	        List<Collection> dCollections = Collection.filterByCollectionName(name);
	        jsonData = Json.toJson(dCollections);
        }
        return ok(jsonData);
    }
	
	@Security.Authenticated(SecuredController.class)
	@BodyParser.Of(BodyParser.Json.class)
    public static Result getByJson() {
    	JsonNode jsonData = getCollectionsData();
        return ok(jsonData);
    }
	
	@Security.Authenticated(SecuredController.class)
	public static Result view(Long id) {
		User user = User.findByEmail(request().username());
		Collection collection = Collection.findById(id);
		if( collection != null ) {
			Logger.debug("id::::" + id+ " parent:::: " + collection.parent);
			if (request().accepts("text/html")) {
				Logger.info("Rendering collection: "+collection);
				return ok(view.render(collection, user));
			} else {
				return ok(Json.toJson(collection));
			}
		} else {
    		return notFound("There is no Collection with ID "+id);
		}
	}
	
	@Security.Authenticated(SecuredController.class)
	public static Result viewAsJson(Long id) {
		Collection collection = Collection.findById(id);
		if( collection!= null ) {
			Logger.debug("" + id+ " " + collection.parent);
			return ok(Json.toJson(collection));
		} else {
    		return notFound("There is no Collection with ID "+id);
		}
	}
	
	@Security.Authenticated(SecuredController.class)
    public static Result viewAct(String url) {
		User user = User.findByEmail(request().username());
		Collection collection = Collection.findByUrl(url);
        return ok(view.render(collection, user));
    }
    
	@Security.Authenticated(SecuredController.class)
    public static Result newForm() {
    	User user = User.findByEmail(request().username());
		JsonNode node = getCollectionsData();
		Form<Collection> collectionForm = Form.form(Collection.class);
		Collection collection = new Collection();
		collectionForm = collectionForm.fill(collection);
        return ok(newForm.render(collectionForm, user, node));
    	
    }

	@Security.Authenticated(SecuredController.class)
    public static Result edit(Long id) {
    	User user = User.findByEmail(request().username());
		Collection collection = Collection.findById(id);
		if( collection.description == null ) collection.description = "";
		collection.description = collection.description.replace("<p>", "").replace("</p>", "").replace("<br />", "\n").replace("<br>", "\n");
		List<Collection> thisCollection = new ArrayList<Collection>();
		thisCollection.add((Collection)collection.parent);
		JsonNode node = getCollectionsData(thisCollection);
		Form<Collection> collectionForm = Form.form(Collection.class);
		collectionForm = collectionForm.fill(collection);
		Logger.debug("id: " + collectionForm.get().id);
        return ok(edit.render(collectionForm, user, id, node));
    }

	@Security.Authenticated(SecuredController.class)
    public static Result info(Form<Collection> form, Long id) {
    	Logger.debug("info");
    	User user = User.findByEmail(request().username());
		List<Collection> thisCollection = new ArrayList<Collection>();
		Collection collection = form.get();
		thisCollection.add(collection);
		JsonNode node = getCollectionsData(thisCollection);
		return badRequest(edit.render(form, user, id, node));
    }
    
	@Security.Authenticated(SecuredController.class)
	public static Result newInfo(Form<Collection> form) {
		User user = User.findByEmail(request().username());
		JsonNode node = getCollectionsData();
        return badRequest(newForm.render(form, user, node));
	}
	
	@Security.Authenticated(SecuredController.class)
    public static Result save() {
    	
    	DynamicForm requestData = form().bindFromRequest();
    	String action = requestData.get("action");

    	Logger.debug("action: " + action);
    	
        if (StringUtils.isNotEmpty(action)) {
        	if (action.equals("save")) {
		        Form<Collection> filledForm = form(Collection.class).bindFromRequest();
		        if(filledForm.hasErrors()) {
	        		Logger.debug("errors: " + filledForm.errors());
		            return newInfo(filledForm);
		        }
		        
	            String collectionSelect = requestData.get("collectionSelect").replace("\"", "");
	            Logger.debug("collectionSelect: " + collectionSelect);
	            if (StringUtils.isNotEmpty(collectionSelect)) {
	                String[] collections = collectionSelect.split(", ");
	                if (collections.length == 1) {
	                	Long collectionId = Long.valueOf(collections[0]);
		            	Collection collection = Collection.findById(collectionId);
	                	filledForm.get().parent = collection;
	                	Logger.debug("looking good");
	                }
	                else if (collections.length > 1) {
	                	Logger.debug("Please select only one parent.");
	    	  			flash("message", "Please select only one parent.");
	    	  			return newInfo(filledForm);
	                }
	            }		        
		        
		        filledForm.get().save();
		        flash("message", "Collection " + filledForm.get().name + " has been created");
		        return redirect(routes.CollectionController.view(filledForm.get().id));
        	}
        }
        return null;    	
    }
    
	@Security.Authenticated(SecuredController.class)
    public static Result update(Long id) {
    	DynamicForm requestData = form().bindFromRequest();
        Form<Collection> filledForm = form(Collection.class).bindFromRequest();
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
		        
	            String collectionSelect = requestData.get("collectionSelect").replace("\"", "");
	            Logger.debug("collectionSelect: " + collectionSelect);
	            if (StringUtils.isNotEmpty(collectionSelect)) {
	                String[] collections = collectionSelect.split(", ");
	                if (collections.length == 1) {
	                	Long collectionId = Long.valueOf(collections[0]);
	                	if (collectionId.longValue() == id.longValue()) {
	                		Logger.debug("same id");
	        	            ValidationError e = new ValidationError("collectionSelect", "It is not possible to assign a node to itself as a parent. Please select one parent.");
	        	            filledForm.reject(e);
	        	  			return info(filledForm, id);
	                	} else {
			            	Collection collection = Collection.findById(collectionId);
		                	filledForm.get().parent = collection;
		                	Logger.debug("looking good");
	                	}
	                }
	                else if (collections.length > 1) {
	                	Logger.debug("Please select only one parent.");
	    	  			flash("message", "Please select only one parent.");
	    	  			return info(filledForm, id);
	                }
	            }
	            
	            // Check if the 'publish' field is empty, which corresponds to 'false':
	            if( filledForm.get().publish == null ) {
	            	filledForm.get().publish = false;
	            }
		        
		        filledForm.get().update(id);
		        flash("message", "Collection " + filledForm.get().name + " has been updated");
		        return redirect(routes.CollectionController.view(filledForm.get().id));
        	} else if (action.equals("delete")) {
        		Collection collection = Collection.findById(id);
		        flash("message", "Collection " + filledForm.get().name + " has been deleted");
            	collection.delete();
            	
            	/**
            	 * Check whether children exist
            	 */
//                if (Collection.hasChildren(collection.url)) {
//                	Logger.debug("This collection has children nodes. Please re-assign children to other nodes first.");
//    	  			flash("message", "This collection has children nodes. Please re-assign children to other nodes first.");
//    	  			return info(collectionForm);
//                } 

        		return redirect(routes.CollectionController.index()); 
        	}
        }
        return null;
    }
	    
	@Security.Authenticated(SecuredController.class)
    public static Result sites(Long id) {
        return redirect(routes.TargetController.collectionTargets(0, "title", "asc", "", id));
    }
	
	
    /**
     * @param id
     * @return
     * 
     * curl -v -H "Content-Type: application/json" -X PUT -d '{"name": "kinman li"}' -u kinman.li@bl.uk:password http://localhost:9000/actdev/api/collections/1
     * @throws ActException
     */
    @With(SecuredAction.class)
    @BodyParser.Of(BodyParser.Json.class)
    public static Result collectionUpdate(Long id) throws ActException {
//    	JsonNode node = request().body().asJson();
//    	ObjectMapper objectMapper = new ObjectMapper();
//		objectMapper.setSerializationInclusion(Include.NON_DEFAULT);
    	Collection collection = Collection.findById(id);
		String url = Play.application().configuration().getString("server_name") + Play.application().configuration().getString("application.context") + "/collections/" + collection.id;
		Logger.debug("location: " + url);
		response().setHeader(LOCATION, url);
		Logger.debug("response 200 updated");
	    return ok(response().getHeaders().get(LOCATION));
    }
    
    /**
     * 
     *  curl -v -H "Content-Type: application/json" -X POST -d '{"name": "Test Collection"}' -u wa-sysadm@bl.uk:sysAdmin http://localhost:9000/act/api/collections
     *  
     * @return
     */
    @With(SecuredAction.class)
    @BodyParser.Of(BodyParser.Json.class)
    public static Result collectionCreate() {
    	JsonNode node = request().body().asJson();

        
        try {
	    	if(node == null) {
	    		return badRequest("Expecting Json data");
	    	} else {
				ObjectMapper objectMapper = new ObjectMapper();
				objectMapper.setSerializationInclusion(Include.NON_DEFAULT);
				Collection cl = objectMapper.readValue(node.toString(), Collection.class);
				if ( cl.id != null ) {
					return badRequest("No ID should be passed when creating a new Collection!");
				}
				/*
				// Refuse if there's an existing one at the same point in the tree:
				for ( Collection c : Collection.findAllCollections() ) {
					if( c.name.equals(cl.name) && c.parent.equals(cl.parent)) {
						return badRequest("A collection with the name '"+cl.name+"' and parent '"+cl.parent+"' already exists! " );
					}
				}
				*/
				cl.save();
	    	}
        } catch (IllegalArgumentException e) {
    		return badRequest(" Collection invalid: " + e);
        } catch (Exception e) {
        	Logger.error("error: " + e);
            return Results.internalServerError(e.getMessage());
        }        
    	Logger.debug("response 200 updated");
    	return ok("OK");
    }
    
	@Security.Authenticated(SecuredController.class)
    public static Result getCollectionTargets(Long id) {
		Collection collection = Collection.findById(id);
		Logger.debug("targets: " + collection.targets.size());
		
		JsonNode jsonNode = Json.toJson(collection.targets);
		return ok(jsonNode);
    }
}
