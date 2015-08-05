package models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import play.Logger;
import play.db.ebean.Model;
import uk.bl.Const;

import com.avaje.ebean.ExpressionList;
import com.avaje.ebean.Page;

/**
 * This class allows archivist to manage open tags.
 */
@Entity
@DiscriminatorValue("tags")
public class Tag extends Taxonomy {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2257699575463702989L;
	
    @ManyToMany
	@JoinTable(name = "tag_target", joinColumns = { @JoinColumn(name = "tag_id", referencedColumnName="id") },
		inverseJoinColumns = { @JoinColumn(name = "target_id", referencedColumnName="id") }) 
    public List<Target> targets;

    @ManyToMany
	@JoinTable(name = "tag_instance", joinColumns = { @JoinColumn(name = "tag_id", referencedColumnName="id") },
		inverseJoinColumns = { @JoinColumn(name = "instance_id", referencedColumnName="id") }) 
    public List<Instance> instances;
    
    public static final Model.Finder<Long, Tag> find = new Model.Finder<Long, Tag>(Long.class, Tag.class);

    public static Tag findTagByName(String name) {
    	return find.where().eq("name", name).findUnique();
    }

    /**
     * Retrieve an object by Id (id).
     * @param nid
     * @return object 
     */
    public static Tag findTagById(Long id) {
    	Tag res = find.where().eq(Const.ID, id).findUnique();
    	return res;
    }          
        
    /**
     * Retrieve a permission refusal by URL.
     * @param url
     * @return permission refusal name
     */
    public static Tag findTagByUrl(String url) {
    	Tag res = new Tag();
    	if (url != null && url.length() > 0 && !url.equals(Const.NONE)) {
    		res = find.where().eq(Const.URL, url).findUnique();
    	} else {
    		res.name = Const.NONE;
    	}
    	return res;
    }
    
	/**
	 * This method filters permission refusals by name and returns a list 
	 * of filtered Tag objects.
	 * @param name
	 * @return
	 */
	public static List<Tag> filterByTagName(String name) {
		List<Tag> res = new ArrayList<Tag>();
        ExpressionList<Tag> ll = find.where().icontains(Const.NAME, name);
    	res = ll.findList();
		return res;
	}
        
    /**
     * Retrieve all tags.
     */
    public static List<Tag> findAllTags() {
        return find.all();
    }
    
	/**
	 * This method retrieves selected tags from target object.
	 * @param targetUrl
	 * @return
	 */
	public static List<Tag> convertUrlsToTagObjects(String urls) {
		List<Tag> res = new ArrayList<Tag>();
   		if (urls != null && urls.length() > 0 && !urls.toLowerCase().contains(Const.NONE)) {
	    	String[] parts = urls.split(Const.COMMA + " ");
	    	for (String part: parts) {
		    	Logger.debug("+++ Tag convertUrlsToObjects() part: " + part);
	    		Tag tag = findTagByUrl(part);
	    		if (tag != null && tag.name != null && tag.name.length() > 0) {
			    	Logger.debug("tag name: " + tag.name);
	    			res.add(tag);
	    		}
	    	}
    	}
		return res;
	}       
	
	public static Map<String,String> options() {
        LinkedHashMap<String,String> options = new LinkedHashMap<String,String>();
        for(Tag t: Tag.findAllTags()) {
            options.put(t.id.toString(), t.name);
        }
        return options;		
	}
	
    /**
     * Return a page of User
     *
     * @param page Page to display
     * @param pageSize Number of Tags per page
     * @param sortBy User property used for sorting
     * @param order Sort order (either or asc or desc)
     * @param filter Filter applied on the name column
     */
    public static Page<Tag> pager(int page, int pageSize, String sortBy, String order, String filter) {

        return find.where().icontains("name", filter)
        		.orderBy(sortBy + " " + order)
        		.findPagingList(pageSize)
        		.setFetchAhead(false)
        		.getPage(page);
    }    

    public static Map<String, Boolean> toOptions(List<Tag> myTags) {
    	if (myTags == null) myTags = new ArrayList<Tag>();
        Map<String, Boolean> tagsMap = new HashMap<String, Boolean>();
        for (Tag tag : Tag.findAllTags()) {
        	tagsMap.put(tag.name, (myTags != null && myTags.contains(tag)));
        }
        return tagsMap;
    }
}