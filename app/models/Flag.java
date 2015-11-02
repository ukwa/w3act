package models;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import play.db.ebean.Model;
import uk.bl.Const;

import com.avaje.ebean.ExpressionList;
import com.avaje.ebean.Page;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * This class allows archivist to manage open flags.
 */
@Entity
@DiscriminatorValue("flags")
public class Flag extends Taxonomy {

	/**
	 * file id
	 */
	private static final long serialVersionUID = -2257699575463702989L;

	@JsonIgnore
    @ManyToMany
	@JoinTable(name = "flag_target", joinColumns = { @JoinColumn(name = "flag_id", referencedColumnName="id") },
		inverseJoinColumns = { @JoinColumn(name = "target_id", referencedColumnName="id") }) 
    public List<Target> targets;

	@JsonIgnore
    @ManyToMany
	@JoinTable(name = "flag_instance", joinColumns = { @JoinColumn(name = "flag_id", referencedColumnName="id") },
		inverseJoinColumns = { @JoinColumn(name = "instance_id", referencedColumnName="id") }) 
    public List<Instance> instances;


    public static final Model.Finder<Long, Flag> find = new Model.Finder<Long, Flag>(Long.class, Flag.class);

    public static Flag findByName(String name)
    {
        return find.where()
                   .eq("name",
                       name)
                   .findUnique();
    }

    
    /**
     * Retrieve an object by Id (id).
     * @param nid
     * @return object 
     */
    public static Flag findById(Long id) {
    	Flag res = find.where().eq(Const.ID, id).findUnique();
    	return res;
    }          
    
    /**
     * Retrieve a permission refusal by URL.
     * @param url
     * @return permission refusal name
     */
    public static Flag findByUrl(String url) {
    	Flag res = new Flag();
    	if (url != null && url.length() > 0 && !url.equals(Const.NONE)) {
    		res = find.where().eq(Const.URL, url).findUnique();
    	} else {
    		res.name = Const.NONE;
    	}
    	return res;
    }
    
	/**
	 * This method filters flags by name and returns a list 
	 * of filtered Flag objects.
	 * @param name
	 * @return
	 */
	public static List<Flag> filterByFlagName(String name) {
		List<Flag> res = new ArrayList<Flag>();
        ExpressionList<Flag> ll = find.where().icontains(Const.NAME, name);
    	res = ll.findList();
		return res;
	}
        
    /**
     * Retrieve all flags.
     */
    public static List<Flag> findAllFlags() {
        return find.all();
    }
    
	/**
	 * This method retrieves selected flags from target object.
	 * @param targetUrl
	 * @return
	 */
	public static List<Flag> convertUrlsToFlagObjects(String urls) {
		List<Flag> res = new ArrayList<Flag>();
   		if (urls != null && urls.length() > 0 && !urls.toLowerCase().contains(Const.NONE)) {
	    	String[] parts = urls.split(Const.COMMA + " ");
	    	for (String part: parts) {
//		    		Logger.debug("part: " + part);
	    		Flag flag = findByName(part);
	    		if (flag != null && flag.name != null && flag.name.length() > 0) {
//			    	Logger.debug("flag name: " + flag.name);
	    			res.add(flag);
	    		}
	    	}
    	}
		return res;
	}
	
	public static Map<String,String> options() {
        LinkedHashMap<String,String> options = new LinkedHashMap<String,String>();
        for(Flag t: Flag.findAllFlags()) {
            options.put(t.id.toString(), t.name);
        }
        return options;		
	}
    	
	@Override
	public String toString() {
        return "Flag(" + name + ")" + ", id:" + id;
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
		Flag other = (Flag) obj;
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
	
    /**
     * Return a page of User
     *
     * @param page Flag to display
     * @param pageSize Number of Flags per page
     * @param sortBy User property used for sorting
     * @param order Sort order (either or asc or desc)
     * @param filter Filter applied on the name column
     */
    public static Page<Flag> pager(int page, int pageSize, String sortBy, String order, String filter) {

        return find.where().icontains("name", filter)
        		.orderBy(sortBy + " " + order)
        		.findPagingList(pageSize)
        		.setFetchAhead(false)
        		.getPage(page);
    }    

}