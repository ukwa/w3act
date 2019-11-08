package controllers;

import java.util.*;

import models.Collection;
import models.Subject;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import play.Logger;
import play.libs.Json;
import play.mvc.Controller;


class NaryTreeNode {
	public long key;
	public String title;
	public String url;
	public boolean select;
	//public int targetCount;
	public List<NaryTreeNode> children;

	public NaryTreeNode() {}

	public NaryTreeNode(long _val, String _name, String _url,  boolean _select, List<NaryTreeNode> _children) {
		key = _val;
		title = _name;
		url = _url;
		select = _select;
		children = _children;
	}

	public void BSF(){

	}

	public NaryTreeNode searchNode(NaryTreeNode start_from, NaryTreeNode searchNode){//root (where to start from), element (key or title)
		//search for Title or key (aka id)
		return null;
	}

	// specify the Level where to start from
	// L0:                         root
	//            /     /     /     |     \     \     \
	// L1        24    13    96    63     15    79    74
	//         Art.. Busin. Edu  Gov     Med   Scie   Soc
	//          /
	//
	//
	public NaryTreeNode insertNode(NaryTreeNode start_from, NaryTreeNode parent, NaryTreeNode newNode){//root (where to start from), element (key or title)
		//search with
		searchNode(start_from, parent);

		return null;
	}
};

public class AbstractController extends Controller {

	private static final String cacheName = "play";
	private static final CacheManager cacheManager = CacheManager.getInstance();

	//----------------------------------------------------------------------------------
	// In-memory data structure for Subjects and Collections.
	//----------------------------------------------------------------------------------
	private static Stack<NaryTreeNode> stackOfObjectsLayers;
	private static Stack<NaryTreeNode> stackOfCollectionsLayers;

	public static Stack<NaryTreeNode> getStackOfObjectsLayers() {
		if (stackOfObjectsLayers==null)
			stackOfObjectsLayers = new Stack<>();
		return stackOfObjectsLayers;
	}
	public static Stack<NaryTreeNode> getStackOfCollectionsLayers() {
		if (stackOfCollectionsLayers==null)
			stackOfCollectionsLayers = new Stack<>();
		return stackOfCollectionsLayers;
	}
    //----------------------------------------------------------------------------------

	public static CacheManager getCacheManager() {
		return cacheManager;
	}

	public static Cache getCache() {
		Cache cache = getCacheManager().getCache(cacheName);
		return cache;
	}

	protected static String getQueryParam(String name) {
                String[] value = request().queryString().get(name);
                if (value == null)
                        return null;
                
                if (value.length == 0)
                        return null;
                
                return value[0];
        }
        
        protected static int getQueryParamAsInt(String name, int defaultValue) {
                String[] param = request().queryString().get(name);
                if (param == null)
                        return defaultValue;
                
                if (param.length < 1)
                        return defaultValue;
                
                try {
                        return Integer.parseInt(param[0]);
                } catch (Throwable t) {
                        return defaultValue;
                }
        }
        
        protected static double getQueryParamAsDouble(String name, double defaultValue) {
                String[] param = request().queryString().get(name);
                if (param == null)
                        return defaultValue;
                
                if (param.length < 1)
                        return defaultValue;
                
                try {
                        return Double.parseDouble(param[0]);
                } catch (Throwable t) {
                        return defaultValue;
                }
        }
        
        protected static String getFormParam(String name) {
            Map<String, String[]> formParams = request().body().asFormUrlEncoded();
            if (formParams == null)
                    return null;
            String[] values = formParams.get(name);
            if (values == null)
                    return null;
            
            if (values.length < 1)
                    return null;
            
            return values[0];
    }
    
        protected static String[] getFormParams(String name) {
            Map<String, String[]> formParams = request().body().asFormUrlEncoded();
            if (formParams == null)
                    return null;
            String[] values = formParams.get(name);
            if (values == null)
                    return null;
            
            if (values.length < 1)
                    return null;
            
            return values;
    }

	protected static JsonNode getCollectionsDataByIds(List<Long> myCollectionIds) {
		return Json.toJson(getCollectionTreeElementsByIdsStack(Collection.getFirstLevelCollections(), myCollectionIds));//jsonData;
	}

	protected static JsonNode getSingleCollectionDataById(Long myCollectionId, List<Long> selectedCollectionIds) {
		List<Collection> collections = Collection.findChildrenByParentId(myCollectionId);
		List<ObjectNode> result = getCollectionTreeElementsByIds(collections, null, true, selectedCollectionIds);
		JsonNode jsonData = Json.toJson(result);
		return jsonData;
	}

    protected static List<ObjectNode> getCollectionTreeElementsByIds(List<Collection> collections, String filter, boolean parent, List<Long> myCollectionIds) {
		List<ObjectNode> result = new ArrayList<ObjectNode>();
		JsonNodeFactory nodeFactory = new JsonNodeFactory(false);

        for (Collection collection : collections) {
			ObjectNode child = nodeFactory.objectNode();
			child.put("title", collection.name + ", id = " + collection.id + " (" + collection.targets.size() + ")");
			child.put("url", String.valueOf(routes.CollectionController.view(collection.id)));

			//if selected in filter
			if (myCollectionIds != null && myCollectionIds.contains(collection.id)) {
				child.put("select", true);
			}

			child.put("key", "\"" + collection.id + "\"");

	    	List<Collection> children = Collection.findChildrenByParentId(collection.id);

	    	if (children.size() > 0) {
	    		child.put("children", Json.toJson(getCollectionTreeElementsByIds(children, filter, false, myCollectionIds)));
	    	}
			result.add(child);
    	}
    	return result;
    }

    protected static JsonNode getCollectionsData() {
    	return getCollectionsDataByFilter(null);
    }

    protected static JsonNode getCollectionsData(List<Collection> myCollections) {
    	List<Collection> collections = Collection.getFirstLevelCollections();
    	List<ObjectNode> result = getCollectionTreeElements(collections, null, true, myCollections);
    	JsonNode jsonData = Json.toJson(result);
        return jsonData;
    }

    /**
     * This method computes a tree of collections in JSON format. 
     * @param collectionUrl This is an identifier for current selected object
     * @return tree structure
     */
    protected static JsonNode getCollectionsDataByFilter(String filter) {
    	List<Collection> collections = Collection.getFirstLevelCollections();
    	List<ObjectNode> result = getCollectionTreeElements(collections, filter, true);
    	JsonNode jsonData = Json.toJson(result);
        return jsonData;
    }
    
    protected static List<ObjectNode> getCollectionTreeElements(List<Collection> collections, String filter, boolean parent) {
    	return getCollectionTreeElements(collections, filter, parent, null);
    }
    
    /**
   	 * This method calculates first order collections.
     * @param collections The list of all collections
     * @param filter This is an identifier for current selected object
     * @param parent This parameter is used to differentiate between root and children nodes
     * @return collection object in JSON form
     */
    protected static List<ObjectNode> getCollectionTreeElements(List<Collection> collections, String filter, boolean parent, List<Collection> myCollections) {
		List<ObjectNode> result = new ArrayList<ObjectNode>();
		JsonNodeFactory nodeFactory = new JsonNodeFactory(false);

    	for (Collection collection : collections) {
			ObjectNode child = nodeFactory.objectNode();
			child.put("title", collection.name + " (" + collection.targets.size() + ")");
			child.put("url", String.valueOf(routes.CollectionController.view(collection.id)));
			if (myCollections != null && myCollections.contains(collection)) {
				child.put("select", true);
			}

			child.put("key", collection.id );
	    	List<Collection> children = Collection.findChildrenByParentId(collection.id);
//	    	Logger.debug("collection: " + collection.name + " - " + collection.children.size());
//    	    	Logger.debug("children: " + children.size());
	    	if (children.size() > 0) {
	    		child.put("children", Json.toJson(getCollectionTreeElements(children, filter, false, myCollections)));
	    	}
			result.add(child);
    	}
    	return result;
    }

	public static JsonNode getSubjectsDataByIds(List<Long> mySubjectIds){
		return Json.toJson(getSubjectTreeElementsByIdsStack( Subject.getFirstLevelSubjects(), mySubjectIds));//jsonData;
	}

	protected static List<NaryTreeNode> getSubjectTreeElementsByIdsStack(List<Subject> subjects, List<Long> mySubjectIds) {
		getStackOfObjectsLayers().clear();
        for (Subject subject : subjects)
			getStackOfObjectsLayers().push(new NaryTreeNode(subject.id, subject.name,
                    String.valueOf(routes.SubjectController.view(subject.id)),
                    mySubjectIds.contains(subject.id)?true:false,
                    subjectHelper(subject.id, mySubjectIds) ));
        //searchSubject(74);
		return getStackOfObjectsLayers();
	}

    public static List<NaryTreeNode> subjectHelper(long subject_id, List<Long> mySubjectIds){
        List<NaryTreeNode> result = new ArrayList<>();

        List<Subject> children = Subject.findChildrenByParentId(subject_id);
        if (children.size() > 0) {
            children.forEach(c->
                    result.add(new NaryTreeNode(c.id, c.name, //+ " (" + c.targets.size() + ")",
                            String.valueOf(routes.SubjectController.view(c.id)),
                            mySubjectIds.contains(c.id)?true:false,
                            subjectHelper(c.id, mySubjectIds)))
            );
            return result;
        }
        else { //no children
            return null;
        }
    }

	//@Cached(key = "CollectionsTreeDS")
	protected static List<NaryTreeNode> getCollectionTreeElementsByIdsStack(List<Collection> collections, List<Long> myCollectionIds) {
		getStackOfCollectionsLayers().clear();
		collections.forEach(c->
			getStackOfCollectionsLayers().push(
				new NaryTreeNode(c.id, c.name,
						String.valueOf(routes.CollectionController.view(c.id)),
						myCollectionIds.contains(c.id)?true:false,
						collectionHelper(c.id, myCollectionIds) )));

		//searchCollections(690); //TODO: search action
		return getStackOfCollectionsLayers();
	}

	public static List<NaryTreeNode> collectionHelper(long collection_id, List<Long> myCollectionIds){
		List<NaryTreeNode> result = new ArrayList<>();
		List<Collection> children = Collection.findChildrenByParentId(collection_id);
		if (children.size() > 0) {
			children.forEach(c->
				result.add(new NaryTreeNode(c.id, c.name, //+ " (" + c.targets.size() + ")",
						String.valueOf(routes.CollectionController.view(c.id)),
						myCollectionIds.contains(c.id)?true:false,
                        collectionHelper(c.id, myCollectionIds)))
			);
			return result;
		}
		else { //no children
			return null;
		}
	}

	public static JsonNode getSubjectsData() {
		return getSubjectsData(null);
	}


	/**
     * In-memory structure Insertion
     *
     * */
    protected static JsonNode insertionSubject(NaryTreeNode root, NaryTreeNode parent, NaryTreeNode newNodeToInsert){
        Iterator value = getStackOfObjectsLayers().iterator();
        return null;
    }

	protected static JsonNode searchSubject(int _key){//root_id, ){

		//                      root (based on REAL SUBJECTS TREE)
		//                       |
		//      /     /     /    |     \     \     \
		//     24    13    96    63     15    79    74   (nodes with children?)
		//     |     |     |
		//     |     |
		//     |     |
		//     |     |
		//     |    12, 33, 53, ...
		//     |
		//   23, 29, 71, 89, 112, 113

		Iterator iterator = getStackOfObjectsLayers().iterator();
		while (iterator.hasNext()) { //stack of NaryTreeNode
			NaryTreeNode nodeObject = (NaryTreeNode)iterator.next();
			if (nodeObject.key == _key) {
				Logger.debug("++++++++++ --------- STACK traverse - FOUND NODE - Subject Tree - key, title =  : " + nodeObject.key + ", title = " + nodeObject.title);
			}
		}

		return null;
	}
	
	protected static JsonNode searchCollections(int _key){//root_id, ){

		//                             root (based on REAL SUBJECTS TREE)
		//                              |
		//      /   ...8...  /     /    |     \     \     \
		//     1    brexit  910    96    63     15    79    74   (nodes with children?)
		//     |            |      |
		//     |            |
		//     |            |
		//     |            |
		//     |            911, , ..., 914
		//     |
		//   23, 29, 71, 89, 112, 113

		int i=0;
		Iterator iterator = getStackOfCollectionsLayers().iterator();

		while (iterator.hasNext()) { //stack of NaryTreeNode
			i++;
			NaryTreeNode nodeObject = (NaryTreeNode)iterator.next();
			if (nodeObject.key == _key) {
				Logger.debug("++++++++++ --------- STACK traverse - FOUND NODE - Collection Tree - coll id key =  : " + nodeObject.key + ", title = " + nodeObject.title+" at i = " +i+", stack size = "+getStackOfCollectionsLayers().size());
			}
		}
		return null;
	}

	/**
	 * This method computes a tree of subjects in JSON format. 
	 * @param subjectUrl This is an identifier for current selected object
	 * @return tree structure
	 */
	protected static JsonNode getSubjectsData(List<Subject> mySubjects) {    	
		List<Subject> firstLevel = Subject.getFirstLevelSubjects();
		List<ObjectNode> result = getSubjectTreeElements(firstLevel, true, mySubjects);
		Logger.debug("subjects main level size: " + firstLevel.size());
		JsonNode jsonData = Json.toJson(result);
	    return jsonData;
	}
	
    protected static JsonNode getSubjectsDataByFilter(String filter) {
		List<Subject> firstLevel = Subject.getFirstLevelSubjects();
    	List<ObjectNode> result = getSubjectTreeElements(firstLevel, filter, true);
    	JsonNode jsonData = Json.toJson(result);
        return jsonData;
    }

    protected static List<ObjectNode> getSubjectTreeElements(List<Subject> subjects, String filter, boolean parent) {
    	return getSubjectTreeElements(subjects, filter, parent, null);
    }
    
	protected static List<ObjectNode> getSubjectTreeElements(List<Subject> firstLevel, boolean parent, List<Subject> mySubjects) { 
		return getSubjectTreeElements(firstLevel, null, parent, mySubjects);
	}
	
	/**
	 * This method calculates first order subjects.
	 * @param subjectList The list of all subjects
	 * @param filter This is an identifier for current selected object
	 * @param parent This parameter is used to differentiate between root and children nodes
	 * @return subject object in JSON form
	 */
	protected static List<ObjectNode> getSubjectTreeElements(List<Subject> subjects, String filter, boolean parent, List<Subject> mySubjects) { 
		List<ObjectNode> result = new ArrayList<ObjectNode>();
		JsonNodeFactory nodeFactory = new JsonNodeFactory(false);

		for (Subject subject : subjects) {
			ObjectNode child = nodeFactory.objectNode();
			child.put("title", subject.name);
			child.put("url", String.valueOf(routes.SubjectController.view(subject.id)));
			if (mySubjects != null && mySubjects.contains(subject)) {
				child.put("select", true);
			}
			child.put("key", subject.id);
	    	List<Subject> children = Subject.findChildrenByParentId(subject.id);
	    	if (children.size() > 0) {
	    		child.put("children", Json.toJson(getSubjectTreeElements(children, filter, false, mySubjects)));
	    	}
			result.add(child);
		}
		return result;
	}

}