package models;

import java.lang.reflect.Field;
import java.util.*;

import javax.persistence.*;

import com.avaje.ebean.Expr;
import com.avaje.ebean.ExpressionList;
import com.avaje.ebean.Page;

import play.Logger;
import play.data.validation.Constraints.Required;
import play.db.ebean.*;
import uk.bl.Const;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * DCollection entity managed by Ebean
 */
@Entity
@Table(name = "collection")
public class Collection extends ActModel {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3043585612371074777L;

	//bi-directional many-to-many association to Target
    @ManyToMany
	@JoinTable(name = Const.COLLECTION_TARGET, joinColumns = { @JoinColumn(name = "collection_id", referencedColumnName="id") },
		inverseJoinColumns = { @JoinColumn(name = "target_id", referencedColumnName="id") }) 
    private List<Target> targets = new ArrayList<Target>();
 
    //bi-directional many-to-many association to Instance
    @ManyToMany
	@JoinTable(name = Const.COLLECTION_INSTANCE, joinColumns = { @JoinColumn(name = "collection_id", referencedColumnName="id") },
		inverseJoinColumns = { @JoinColumn(name = "instance_id", referencedColumnName="id") }) 
    private List<Instance> instances = new ArrayList<Instance>();
    
    @JsonIgnore
    @Column(columnDefinition = "text") 
    public String value;
    
    @JsonIgnore
    @Column(columnDefinition = "text")
    public String summary;
    
    @JsonIgnore
    public String format;
    
    @JsonIgnore
    public Long vid;
    
    @JsonIgnore
    public Boolean isNew;
    
    @JsonIgnore
    public String type;
    
    @Required
    public String title;
    
    @JsonIgnore
    public String language;
    
    @JsonIgnore
    public String editUrl;
    
    @JsonIgnore
    public Long status;
    
    @JsonIgnore
    public Long promote;
    
    @JsonIgnore
    public Long sticky;
    
    @JsonIgnore
    public String author;
    
    @JsonIgnore
    public String log;
    
    @JsonIgnore
    public Long comment;
    
    @JsonIgnore
    public Long commentCount;
    
    @JsonIgnore
    public Long commentCountNew;
    
    @JsonIgnore
    public String revision;
    
    @JsonIgnore
    public Long feedNid;    
    
    @Column(columnDefinition = "text") 
    public String fieldOwner;
    
    @Column(columnDefinition = "text") 
    public String fieldDates;
    
    /**
     * 'true' if collection should be made visible in the UI, default 'false'
     */
    @JsonIgnore
    public Boolean publish;
    
    // lists
    @JsonIgnore
    @Column(columnDefinition = "text")
    public String fieldTargets; 
    
    @JsonIgnore
    @Column(columnDefinition = "text")
    public String fieldSubCollections;
    
    @JsonIgnore
    @Column(columnDefinition = "text") 
    public String fieldInstances;
    
    // additional fields from taxonomy
    @JsonIgnore
    public Long weight;
    
    @JsonIgnore
    public Long nodeCount;
    
    @JsonIgnore
    @Column(columnDefinition = "text") 
    public String vocabulary;
    
    // lists
    @JsonIgnore
    @Column(columnDefinition = "text")  
    public String parent;
    
    @JsonIgnore
    @Column(columnDefinition = "text")  
    public String parentsAll;


    /**
     * Constructor
     * @param title
     */
    public Collection(String title) {
        this.title = title;
        this.publish = false;
    }

    public Collection() {
    	super();
        this.publish = false;
    }
    
    // -- Queries
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public static Model.Finder<Long,Collection> find = new Model.Finder(Long.class, Collection.class);
    
    public List<Target> getTargets() {
    	return this.targets;
    }
    
    public void setTargets(List<Target> targets) {
    	this.targets = targets;
    }    
    
    public List<Instance> getInstances() {
    	return this.instances;
    }
    
    public void setInstances(List<Instance> instances) {
    	this.instances = instances;
    }        
    
    /**
     * Retrieve dcollections 
     */
    public static List<Collection> findInvolving() {
        return find.all();
    }
    
    /**
     * Retrieve all collections.
     */
    public static List<Collection> findAll() {
        return find.all();
    }
    
    /**
     * Retrieve an object by Id (nid).
     * @param nid
     * @return object 
     */
    public static Collection findById(Long id) {
    	Collection res = find.where().eq(Const.ID, id).findUnique();
    	return res;
    }          
    
    /**
     * This method returns all collections related alphabetically sorted.
     * @return user list
     */
    public static List<Collection> findAllSorted() {
    	List<Collection> res = new ArrayList<Collection>();
    	Page<Collection> page = page(0, find.all().size(), Const.TITLE, Const.ASC, "");
    	res = page.getList();
        return res;
    }
    
    /**
     * Create a new dcollection.
     */
    public static Collection create(String title) {
        Collection dcollection = new Collection(title);
        dcollection.save();
        return dcollection;
    }
    
    /**
     * Rename a dcollection
     */
    public static String rename(Long dcollectionId, String newName) {
        Collection dcollection = (Collection) find.ref(dcollectionId);
        dcollection.title = newName;
        dcollection.update();
        return newName;
    }
        
    /**
     * This method translates database view to the HTML view.
     * @return list of Strings
     */
	public List<String> get_field_list(String fieldName) {
    	List<String> res = new ArrayList<String>();
    	try {
    		res.add(Const.EMPTY);
			Field field = this.getClass().getField(fieldName); 
			String content = (String) field.get(this);
			res = Arrays.asList(content.split("\\s*,\\s*"));
		} catch (IllegalArgumentException e) {
			Logger.info(e.getMessage());
		} catch (IllegalAccessException e) {
			Logger.info(e.getMessage());
		} catch (SecurityException e) {
			Logger.info(e.getMessage());
		} catch (NoSuchFieldException e) {
			Logger.info(e.getMessage());
		} catch (Exception e) {
			Logger.info(e.getMessage());
		}
    	return res;
    }
    
    /**
     * Retrieve a collection object by URL.
     * @param url
     * @return collection
     */
    public static Collection findByUrl(String url) {
//    	Logger.info("collection findByUrl: " + url);
    	Collection res = new Collection();
    	if (url != null && url.length() > 0 && !url.equals(Const.NONE)) {
    		res = find.where().eq(Const.URL, url).findUnique();
    	} else {
    		res.title = Const.NONE;
    	}
    	return res;
    }

    /**
     * This method returns parent collections for given collection.
     * @param quera collection
     * @return parent collection list
     */
    public static List<Collection> getParents(Collection collection) {
		List<Collection> res = new ArrayList<Collection>();
		String parentStr = collection.parentsAll;
    	if (parentStr != null && parentStr.length() > 0) {
    		if (parentStr.contains(Const.COMMA)) {
    			List<String> resList = Arrays.asList(parentStr.split(Const.COMMA));
    			Iterator<String> itr = resList.iterator();
    			while (itr.hasNext()) {
        			String parentUrl = itr.next();
        	        Collection parentCollection = find.where().eq(Const.URL, parentUrl).findUnique();
        	        res.add(parentCollection);
    			}
    		} else {
    	        Collection parentCollection = find.where().eq(Const.URL, parentStr).findUnique();
    	        res.add(parentCollection);
    		}
    	}
		return res;
    }
    
    /**
     * This method returns true if given collection has children nodes.
     * @param collectionUrl The parent collection URL
     * @return true if parent collection has children
     */
    public static boolean hasChildren(String collectionUrl) {
    	boolean res = false;
    	List<Collection> collectionList = getChildLevelCollections(collectionUrl);
    	if (collectionList.size() > 0) {
    		res = true;
    	}
    	return res;
    }
    
    /**
     * This method returns parent collections for given collection.
     * @param collectionUrl The identifier URL of collection
     * @return parent collection list
     */
    public static List<Collection> getParentsByUrl(String collectionUrl) {
    	Collection collection = Collection.findByUrl(collectionUrl);
    	List<Collection> res = new ArrayList<Collection>();
    	if (collection != null) {
    		res = getParents(collection);
    	}
		return res;
    }
    
    /**
     * Retrieve a collection by title.
     * @param title
     * @return collection object
     */
    public static Collection findByTitle(String title) {
    	Collection res = new Collection();
    	if (title != null && title.length() > 0) {
    		res = find.where().eq(Const.TITLE, title).findUnique();
    	} else {
    		res.title = Const.NONE;
    	}
    	return res;
    }
    
    /**
     * Retrieve a collection by title.
     * @param title
     * @return collection object
     */
    public static Collection findByTitleExt(String title) {
    	Collection res = new Collection();
    	if (title != null && title.length() > 0) {
//    		Logger.info("p1: " + name);
    		if (title.contains(Const.COMMA)) {
    			title = title.replace(Const.COMMA, Const.COMMA + " "); // in database entry with comma has additional space after comma
    		}
    		res = find.where().eq(Const.TITLE, title).findUnique();
    	} else {
    		res.title = Const.NONE;
    	}
    	return res;
    }
    
	/**
	 * This method filters collections by title and returns a list of filtered Collection objects.
	 * @param title
	 * @return
	 */
	public static List<Collection> filterByName(String name) {
		List<Collection> res = new ArrayList<Collection>();
        ExpressionList<Collection> ll = find.where().icontains(Const.TITLE, name);
    	res = ll.findList();
		return res;
	}       
    
    /**
     * Retrieve the Collection names by URL list given as a string.
     * @param url
     * @return collection title list
     */
    public static String findTitlesByUrls(String urls) {
    	String res = "";
    	if (urls != null) {
	    	if (urls.contains(Const.LIST_DELIMITER)) {
		    	String[] parts = urls.split(Const.LIST_DELIMITER);
		    	for (String part: parts)
		        {
		    		String name = findByUrl(part).title;
		    		res = res + name + Const.LIST_DELIMITER;
		        }
	    	} else {
	    		res = urls;    	
	    	}
    	}
    	return res;
    }          
	
	/**
	 * This method retrieves collections without parents - first level collections.
	 * @return
	 */
	public static List<Collection> getFirstLevelCollections() {
		List<Collection> res = new ArrayList<Collection>();
        ExpressionList<Collection> ll = find.where()
        		.add(Expr.or(
        				Expr.eq(Const.PARENT, ""),
        				Expr.eq(Const.PARENT, Const.NONE_VALUE)
    	                )
    	        );
    	res = ll.findList();
    	Logger.info("getFirstLevelCollections list size: " + res.size());
		return res;
	}       
    
	/**
	 * This method retrieves collections with parents - child level collections.
	 * @return
	 */
	public static List<Collection> getChildLevelCollections(String url) {
		List<Collection> res = new ArrayList<Collection>();
        ExpressionList<Collection> ll = find.where().eq(Const.PARENT, url);
    	res = ll.findList();
		return res;
	}       
    
	/**
	 * This method presents collection list for view page.
	 * @param list
	 * @return presentation string
	 */
	public static String getCollectionsAsString(List<Collection> list) {
    	String res = "";
//		Logger.info("getCollectionsAsString() list size: " + list.size());
		Iterator<Collection> itr = list.iterator();
		boolean firstTime = true;
		while (itr.hasNext()) {
			Collection collection = itr.next();
			if (firstTime) {
//				Logger.info("add first collection.title: " + collection.title);
				res = collection.title;
				firstTime = false;
			} else {
//				Logger.info("add collection.title: " + collection.title);
				res = res + Const.COMMA + " " + collection.title;
			}
		}
		if (res.length() == 0) {
			res = Const.NONE;
		}
        return res;
	}
	
	/**
	 * This method retrieves selected suggested collections from target object.
	 * @param targetUrl
	 * @return
	 */
	public static List<Collection> getSelectedCollections(String targetUrl) {
//		Logger.info("getSelectedCollections() targetUrl: " + targetUrl);
		List<Collection> res = new ArrayList<Collection>();
    	if (targetUrl != null && targetUrl.length() > 0) {
    		Target target = Target.findByUrl(targetUrl);
    		if (target.fieldCollectionCategories != null) {
//    			Logger.info("getSelectedCollections() field_collection_categories: " + target.field_collection_categories);
		    	String[] parts = target.fieldCollectionCategories.split(Const.COMMA + " ");
		    	for (String part: parts) {
//		    		Logger.info("part: " + part);
		    		Collection collection = findByUrl(part);
		    		if (collection != null && collection.title != null && collection.title.length() > 0) {
//			    		Logger.info("collection title: " + collection.title);
		    			res.add(collection);
		    		}
		    	}
    		}
    	}
		return res;
	}       
    
	/**
	 * This method retrieves selected suggested collections from instance object.
	 * @param targetUrl
	 * @return
	 */
	public static List<Collection> getSelectedCollectionsByInstanceUrl(String instanceUrl) {
//		Logger.info("getSelectedCollections() instanceUrl: " + instanceUrl);
		List<Collection> res = new ArrayList<Collection>();
    	if (instanceUrl != null && instanceUrl.length() > 0) {
    		Instance instance = Instance.findByUrl(instanceUrl);
    		if (instance.fieldCollectionCategories != null) {
//    			Logger.info("getSelectedCollections() field_collection_categories: " + target.field_collection_categories);
		    	String[] parts = instance.fieldCollectionCategories.split(Const.COMMA + " ");
		    	for (String part: parts) {
//		    		Logger.info("part: " + part);
		    		Collection collection = findByUrl(part);
		    		if (collection != null && collection.title != null && collection.title.length() > 0) {
//			    		Logger.info("collection title: " + collection.title);
		    			res.add(collection);
		    		}
		    	}
    		}
    	}
		return res;
	}       
    
	/**
	 * This method retrieves selected collections from target object.
	 * @param targetUrl
	 * @return
	 */
	public static List<Collection> convertUrlsToObjects(String urls) {
		List<Collection> res = new ArrayList<Collection>();
   		if (urls != null && urls.length() > 0 && !urls.toLowerCase().contains(Const.NONE)) {
	    	String[] parts = urls.split(Const.COMMA + " ");
	    	for (String part: parts) {
//		    		Logger.info("part: " + part);
	    		Collection collection = findByUrl(part);
	    		if (collection != null && collection.title != null && collection.title.length() > 0) {
//			    	Logger.info("collection title: " + collection.title);
	    			res.add(collection);
	    		}
	    	}
    	}
		return res;
	}       
    	
    public String toString() {
        return "Collection(" + id + ") with title: " + title + ", field_targets: " + fieldTargets +
        		 ", field_instances: " + fieldInstances +", format: " + format + ", summary: " + summary + ", value: " + value;
    }

    /**
     * Return a page of Target
     *
     * @param page Page to display
     * @param pageSize Number of targets per page
     * @param sortBy Target property used for sorting
     * @param order Sort order (either or asc or desc)
     * @param filter Filter applied on the name column
     */
    public static Page<Collection> page(int page, int pageSize, String sortBy, String order, String filter) {

        return find.where().contains("title", filter)
        		.orderBy(sortBy + " " + order)
        		.findPagingList(pageSize)
        		.setFetchAhead(false)
        		.getPage(page);
    }
       
}

