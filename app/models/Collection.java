package models;

import java.lang.reflect.Field;
import java.util.*;

import javax.persistence.*;

import com.avaje.ebean.ExpressionList;
import com.avaje.ebean.Page;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import play.Logger;
import play.cache.Cached;
import play.db.ebean.*;
import scala.NotImplementedError;
import uk.bl.Const;

/**
 * Collection entity managed by Ebean
 */
@Entity
@DiscriminatorValue("collections")
public class Collection extends Taxonomy {

	/** */
	private static final long serialVersionUID = 3043585612371074777L;
	
	@JsonIgnore
    @ManyToOne
	@JoinColumn(name = "parent_id")
	public Collection parent;
	
    @OneToMany(mappedBy="parent")
	public List<Collection> children;
	
    @JsonIgnore
    @ManyToMany
	@JoinTable(name = "collection_target", joinColumns = { @JoinColumn(name = "collection_id", referencedColumnName="id") },
			inverseJoinColumns = { @JoinColumn(name = "target_id", referencedColumnName="id") }) 
	public List<Target> targets;

    @JsonIgnore
    @ManyToMany
	@JoinTable(name = "collection_instance", joinColumns = { @JoinColumn(name = "collection_id", referencedColumnName="id") },
		inverseJoinColumns = { @JoinColumn(name = "instance_id", referencedColumnName="id") }) 
	public List<Instance> instances;

    
	public static Model.Finder<Long,Collection> find = new Model.Finder<Long, Collection>(Long.class, Collection.class);
    
    /**
     * Retrieve all collections.
     */
    public static List<Collection> findAllCollections() {
        return find.orderBy("name asc").findList();
    }
    
    /**
     * Retrieve an object by Id (nid).
     * @param nid
     * @return object 
     */
    public static Collection findById(Long id) {
    	return find.where().eq(Const.ID, id).findUnique();
    }

	public static String findNameById(Long id) {
		return find.where().eq(Const.ID, id).findUnique().name;
	}
    
    public static Collection findByName(String name) {
    	return find.where().eq(Const.NAME, name).findUnique();
    }
    
    /**
     * This method returns all collections related alphabetically sorted.
     * @return user list
     */
    public static List<Collection> findAllSorted() {
    	List<Collection> res = new ArrayList<Collection>();
    	Page<Collection> page = pager(0, find.all().size(), Const.NAME, Const.ASC, "");
    	res = page.getList();
        return res;
    }
    
    /**
     * Create a new dcollection.
     */
    public static Collection create(String title) {
        Collection dcollection = new Collection();
        dcollection.name = title;
        dcollection.save();
        return dcollection;
    }
    
    /**
     * Rename a dcollection
     */
    public static String rename(Long dcollectionId, String newName) {
        Collection dcollection = (Collection) find.ref(dcollectionId);
        dcollection.name = newName;
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
			Logger.debug(e.getMessage());
		} catch (IllegalAccessException e) {
			Logger.debug(e.getMessage());
		} catch (SecurityException e) {
			Logger.debug(e.getMessage());
		} catch (NoSuchFieldException e) {
			Logger.debug(e.getMessage());
		} catch (Exception e) {
			Logger.debug(e.getMessage());
		}
    	return res;
    }
	
	/**
	 * 
	 * @return
	 */
	@JsonProperty
	@Transient
	public List<Long> getTargetIds() {
		List<Long> target_ids = new ArrayList<Long>();
		for( Target t : targets ) {
			target_ids.add( t.id );
		}
		return target_ids;
	}
	
	@JsonProperty
	@Transient
	public void setTargetIds(List<Long> targetIds) {
		List<Target> newTargets = new ArrayList<Target>();
		for( Long tid : targetIds) {
			newTargets.add(Target.findById(tid));
		}
		this.targets = newTargets;
	}
	    
    /**
     * Retrieve a collection object by URL.
     * @param url
     * @return collection
     */
    public static Collection findByUrl(String url) {
    	return find.where().eq(Const.URL, url).findUnique();
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
     * @param name
     * @return collection object
     */
    public static Collection findByTitle(String name) {
    	Collection res = new Collection();
    	if (name != null && name.length() > 0) {
    		res = find.where().eq(Const.NAME, name).findUnique();
    	} else {
    		res.name = Const.NONE;
    	}
    	return res;
    }
    
    /**
     * Retrieve a collection by title.
     * @param name
     * @return collection object
     */
    public static Collection findByTitleExt(String name) {
    	Collection res = new Collection();
    	if (name != null && name.length() > 0) {
//    		Logger.debug("p1: " + name);
    		if (name.contains(Const.COMMA)) {
    			name = name.replace(Const.COMMA, Const.COMMA + " "); // in database entry with comma has additional space after comma
    		}
    		res = find.where().eq(Const.NAME, name).findUnique();
    	} else {
    		res.name = Const.NONE;
    	}
    	return res;
    }
    
	/**
	 * This method filters collections by title and returns a list of filtered Collection objects.
	 * @param title
	 * @return
	 */
	public static List<Collection> filterByCollectionName(String name) {
		List<Collection> res = new ArrayList<Collection>();
        ExpressionList<Collection> ll = find.where().icontains(Const.NAME, name);
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
		    		String name = findByUrl(part).name;
		    		res = res + name + Const.LIST_DELIMITER;
		        }
	    	} else {
	    		res = urls;    	
	    	}
    	}
    	return res;
    }          
	
	/**
	 * This method presents collection list for view page.
	 * @param list
	 * @return presentation string
	 */
	public static String getCollectionsAsString(List<Collection> list) {
    	String res = "";
//		Logger.debug("getCollectionsAsString() list size: " + list.size());
		Iterator<Collection> itr = list.iterator();
		boolean firstTime = true;
		while (itr.hasNext()) {
			Collection collection = itr.next();
			if (firstTime) {
//				Logger.debug("add first collection.name: " + collection.name);
				res = collection.name;
				firstTime = false;
			} else {
//				Logger.debug("add collection.name: " + collection.name);
				res = res + Const.COMMA + " " + collection.name;
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
//		Logger.debug("getSelectedCollections() targetUrl: " + targetUrl);
//		List<Collection> res = new ArrayList<Collection>();
//    	if (targetUrl != null && targetUrl.length() > 0) {
//    		Target target = Target.findByUrl(targetUrl);
//    		if (target.fieldCollectionCategories != null) {
////    			Logger.debug("getSelectedCollections() field_collection_categories: " + target.field_collection_categories);
//		    	String[] parts = target.fieldCollectionCategories.split(Const.COMMA + " ");
//		    	for (String part: parts) {
////		    		Logger.debug("part: " + part);
//		    		Collection collection = findByUrl(part);
//		    		if (collection != null && StringUtils.isNotEmpty(collection.name)) {
////			    		Logger.debug("collection title: " + collection.name);
//		    			res.add(collection);
//		    		}
//		    	}
//    		}
//    	}
//		return res;
		throw new NotImplementedError();
	}

    @Cached(key = "CollectionsData")
    public static List<Collection> getFirstLevelCollections() {
	       //Logger.debug("getFirstLevelCollections list size: " + rootCollections.size());
	       return find.fetch("targets").where().isNull("parent").order().asc("name").findList();
	} 
	
	public static List<Collection> findChildrenByParentId(Long parentId) {
		return find.where().eq("t0.parent_id", parentId).order().asc("name").findList();
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
	public static Page<Collection> pager(int page, int pageSize, String sortBy, String order, String filter) {

        return find.where().contains("title", filter)
        		.orderBy(sortBy + " " + order)
        		.findPagingList(pageSize)
        		.setFetchAhead(false)
        		.getPage(page);
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Collection other = (Collection) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "Collection [targets=" + targets + ", children=" + children
				+ ", ttype=" + ttype + ", name=" + name + ", parent=" + parent
			    + ", description="
						+ description + ", startDate="
						+ startDate + ", endDate=" + endDate
						+ ", publish=" + publish + "]";
	}

	/**
	 * This method retrieves collections with parents - child level collections.
	 * @return
	 */
	public static List<Collection> getChildLevelCollections(String url) {
		return find.where().eq(Const.PARENT, url).findList();
	}
	
	public static List<Collection> getCollectionCategoriesByTargetId(Long id) {
		return find.fetch("target").where().eq("target.id", id).isNull("parent").findList();
	}

}

