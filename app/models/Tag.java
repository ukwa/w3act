package models;

import java.util.ArrayList;
import java.util.List;

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
	@JoinTable(name = Const.TAG_TARGET, joinColumns = { @JoinColumn(name = "tag_id", referencedColumnName="id") },
		inverseJoinColumns = { @JoinColumn(name = "target_id", referencedColumnName="ID") }) 
    public List<Target> targets;

    @ManyToMany
	@JoinTable(name = Const.TAG_INSTANCE, joinColumns = { @JoinColumn(name = "tag_id", referencedColumnName="id") },
		inverseJoinColumns = { @JoinColumn(name = "instance_id", referencedColumnName="id") }) 
    public List<Instance> instances;
    
    public static final Model.Finder<Long, Tag> find = new Model.Finder<Long, Tag>(Long.class, Tag.class);

    public static Tag findByName(String name) {
    	return find.where().eq("name", name).findUnique();
    }

    /**
     * Retrieve an object by Id (id).
     * @param nid
     * @return object 
     */
    public static Tag findById(Long id) {
    	Tag res = find.where().eq(Const.ID, id).findUnique();
    	return res;
    }          
        
    /**
     * Retrieve a permission refusal by URL.
     * @param url
     * @return permission refusal name
     */
    public static Tag findByUrl(String url) {
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
		    	Logger.info("+++ Tag convertUrlsToObjects() part: " + part);
	    		Tag tag = findByUrl(part);
	    		if (tag != null && tag.name != null && tag.name.length() > 0) {
			    	Logger.info("tag name: " + tag.name);
	    			res.add(tag);
	    		}
	    	}
    	}
		return res;
	}       
	
    @Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		Tag other = (Tag) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
    	
    public String toString() {
        return "Tag(" + name + ")" + ", id:" + id;
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

}