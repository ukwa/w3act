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
@SuppressWarnings("serial")
@Entity
@Table(name = "dcollection")
public class DCollection extends Model {

    @Id @JsonIgnore
    @Column(name="ID")
    public Long nid;

    //bi-directional many-to-many association to Target
    @ManyToMany
	@JoinTable(name = Const.COLLECTION_TARGET, joinColumns = { @JoinColumn(name = "id_collection", referencedColumnName="ID") },
		inverseJoinColumns = { @JoinColumn(name = "id_target", referencedColumnName="ID") }) 
    private List<Target> targets = new ArrayList<Target>();
 
    public List<Target> getTargets() {
    	return this.targets;
    }
    
    public void setTargets(List<Target> targets) {
    	this.targets = targets;
    }    
    
    //bi-directional many-to-many association to Instance
    @ManyToMany
	@JoinTable(name = Const.COLLECTION_INSTANCE, joinColumns = { @JoinColumn(name = "id_collection", referencedColumnName="ID") },
		inverseJoinColumns = { @JoinColumn(name = "id_instance", referencedColumnName="ID") }) 
    private List<Instance> instances = new ArrayList<Instance>();
    
    public List<Instance> getInstances() {
    	return this.instances;
    }
    
    public void setInstances(List<Instance> instances) {
    	this.instances = instances;
    }    
    
    @Column(columnDefinition = "TEXT") @JsonIgnore
    public String value;
    @Column(columnDefinition = "TEXT") @JsonIgnore
    public String summary; 
    @JsonIgnore
    public String format;
    @JsonIgnore
    public Long vid;
    @JsonIgnore
    public Boolean is_new;
    @JsonIgnore
    public String type;
    @Required
    public String title;
    @JsonIgnore
    public String language;
    public String url;
    @JsonIgnore
    public String edit_url;
    @JsonIgnore
    public Long status;
    @JsonIgnore
    public Long promote;
    @JsonIgnore
    public Long sticky;
    @JsonIgnore
    public String created;
    @JsonIgnore
    public String changed;
    @JsonIgnore
    public String author;
    @JsonIgnore
    public String log;
    @JsonIgnore
    public Long comment;
    @JsonIgnore
    public Long comment_count;
    @JsonIgnore
    public Long comment_count_new;
    @JsonIgnore
    public String revision;
    @JsonIgnore
    public Long feed_nid;    
    @Column(columnDefinition = "TEXT") 
    public String field_owner;
    @Column(columnDefinition = "TEXT") 
    public String field_dates;
    /**
     * 'true' if collection should be made visible in the UI, default 'false'
     */
    @JsonIgnore
    public Boolean publish;
    // lists
    @Column(columnDefinition = "TEXT") @JsonIgnore
    public String field_targets; 
    @Column(columnDefinition = "TEXT") @JsonIgnore
    public String field_sub_collections; 
    @Column(columnDefinition = "TEXT") @JsonIgnore
    public String field_instances; 
    // additional fields from taxonomy
    @JsonIgnore
    public Long weight;
    @JsonIgnore
    public Long node_count;
    @Column(columnDefinition = "TEXT") @JsonIgnore
    public String vocabulary;
    // lists
    @Column(columnDefinition = "TEXT")  @JsonIgnore
    public String parent;
    @Column(columnDefinition = "TEXT")  @JsonIgnore
    public String parents_all;


    /**
     * Constructor
     * @param title
     */
    public DCollection(String title) {
        this.title = title;
        this.publish = false;
    }

    public DCollection() {
    	super();
        this.publish = false;
    }
    
    // -- Queries
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public static Model.Finder<Long,DCollection> find = new Model.Finder(Long.class, DCollection.class);
    
    /**
     * Retrieve dcollections 
     */
    public static List<DCollection> findInvolving() {
        return find.all();
    }
    
    /**
     * Retrieve all collections.
     */
    public static List<DCollection> findAll() {
        return find.all();
    }
    
    /**
     * Retrieve an object by Id (nid).
     * @param nid
     * @return object 
     */
    public static DCollection findById(Long nid) {
    	DCollection res = find.where().eq(Const.NID, nid).findUnique();
    	return res;
    }          
    
    /**
     * This method returns all collections related alphabetically sorted.
     * @return user list
     */
    public static List<DCollection> findAllSorted() {
    	List<DCollection> res = new ArrayList<DCollection>();
    	Page<DCollection> page = page(0, find.all().size(), Const.TITLE, Const.ASC, "");
    	res = page.getList();
        return res;
    }
    
    /**
     * Create a new dcollection.
     */
    public static DCollection create(String title) {
        DCollection dcollection = new DCollection(title);
        dcollection.save();
        return dcollection;
    }
    
    /**
     * Rename a dcollection
     */
    public static String rename(Long dcollectionId, String newName) {
        DCollection dcollection = (DCollection) find.ref(dcollectionId);
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
    public static DCollection findByUrl(String url) {
//    	Logger.info("collection findByUrl: " + url);
    	DCollection res = new DCollection();
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
    public static List<DCollection> getParents(DCollection collection) {
		List<DCollection> res = new ArrayList<DCollection>();
		String parentStr = collection.parents_all;
    	if (parentStr != null && parentStr.length() > 0) {
    		if (parentStr.contains(Const.COMMA)) {
    			List<String> resList = Arrays.asList(parentStr.split(Const.COMMA));
    			Iterator<String> itr = resList.iterator();
    			while (itr.hasNext()) {
        			String parentUrl = itr.next();
        	        DCollection parentCollection = find.where().eq(Const.URL, parentUrl).findUnique();
        	        res.add(parentCollection);
    			}
    		} else {
    	        DCollection parentCollection = find.where().eq(Const.URL, parentStr).findUnique();
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
    	List<DCollection> collectionList = getChildLevelCollections(collectionUrl);
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
    public static List<DCollection> getParentsByUrl(String collectionUrl) {
    	DCollection collection = DCollection.findByUrl(collectionUrl);
    	List<DCollection> res = new ArrayList<DCollection>();
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
    public static DCollection findByTitle(String title) {
    	DCollection res = new DCollection();
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
    public static DCollection findByTitleExt(String title) {
    	DCollection res = new DCollection();
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
	public static List<DCollection> filterByName(String name) {
		List<DCollection> res = new ArrayList<DCollection>();
        ExpressionList<DCollection> ll = find.where().icontains(Const.TITLE, name);
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
	public static List<DCollection> getFirstLevelCollections() {
		List<DCollection> res = new ArrayList<DCollection>();
        ExpressionList<DCollection> ll = find.where()
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
	public static List<DCollection> getChildLevelCollections(String url) {
		List<DCollection> res = new ArrayList<DCollection>();
        ExpressionList<DCollection> ll = find.where().eq(Const.PARENT, url);
    	res = ll.findList();
		return res;
	}       
    
	/**
	 * This method presents collection list for view page.
	 * @param list
	 * @return presentation string
	 */
	public static String getCollectionsAsString(List<DCollection> list) {
    	String res = "";
//		Logger.info("getCollectionsAsString() list size: " + list.size());
		Iterator<DCollection> itr = list.iterator();
		boolean firstTime = true;
		while (itr.hasNext()) {
			DCollection collection = itr.next();
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
	public static List<DCollection> getSelectedCollections(String targetUrl) {
//		Logger.info("getSelectedCollections() targetUrl: " + targetUrl);
		List<DCollection> res = new ArrayList<DCollection>();
    	if (targetUrl != null && targetUrl.length() > 0) {
    		Target target = Target.findByUrl(targetUrl);
    		if (target.field_collection_categories != null) {
//    			Logger.info("getSelectedCollections() field_collection_categories: " + target.field_collection_categories);
		    	String[] parts = target.field_collection_categories.split(Const.COMMA + " ");
		    	for (String part: parts) {
//		    		Logger.info("part: " + part);
		    		DCollection collection = findByUrl(part);
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
	public static List<DCollection> getSelectedCollectionsByInstanceUrl(String instanceUrl) {
//		Logger.info("getSelectedCollections() instanceUrl: " + instanceUrl);
		List<DCollection> res = new ArrayList<DCollection>();
    	if (instanceUrl != null && instanceUrl.length() > 0) {
    		Instance instance = Instance.findByUrl(instanceUrl);
    		if (instance.field_collection_categories != null) {
//    			Logger.info("getSelectedCollections() field_collection_categories: " + target.field_collection_categories);
		    	String[] parts = instance.field_collection_categories.split(Const.COMMA + " ");
		    	for (String part: parts) {
//		    		Logger.info("part: " + part);
		    		DCollection collection = findByUrl(part);
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
	public static List<DCollection> convertUrlsToObjects(String urls) {
		List<DCollection> res = new ArrayList<DCollection>();
   		if (urls != null && urls.length() > 0 && !urls.toLowerCase().contains(Const.NONE)) {
	    	String[] parts = urls.split(Const.COMMA + " ");
	    	for (String part: parts) {
//		    		Logger.info("part: " + part);
	    		DCollection collection = findByUrl(part);
	    		if (collection != null && collection.title != null && collection.title.length() > 0) {
//			    	Logger.info("collection title: " + collection.title);
	    			res.add(collection);
	    		}
	    	}
    	}
		return res;
	}       
    	
    public String toString() {
        return "DCollection(" + nid + ") with title: " + title + ", field_targets: " + field_targets +
        		 ", field_instances: " + field_instances +", format: " + format + ", summary: " + summary + ", value: " + value;
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
    public static Page<DCollection> page(int page, int pageSize, String sortBy, String order, String filter) {

        return find.where().contains("title", filter)
        		.orderBy(sortBy + " " + order)
        		.findPagingList(pageSize)
        		.setFetchAhead(false)
        		.getPage(page);
    }
       
}

