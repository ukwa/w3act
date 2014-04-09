package models;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Version;

import play.db.ebean.Model;
import uk.bl.Const;

import com.avaje.ebean.ExpressionList;
import com.avaje.ebean.Page;

/**
 * This class allows archivist to manage open flags.
 */
@Entity
public class Flag extends Model
{

	/**
	 * file id
	 */
	private static final long serialVersionUID = -2257699575463702989L;

	@Id 
    public Long id;

    /**
     * This field with prefix "act-" builds an unique identifier in W3ACT database.
     */
    @Column(columnDefinition = "TEXT")
    public String url;
	
    /**
     * The name of the refusal.
     */
    @Column(columnDefinition = "TEXT")
    public String name;
    
    /**
     * Allows the addition of further notes regarding flag description.
     */
    @Column(columnDefinition = "TEXT")
    public String description;

    @Version
    public Timestamp lastUpdate;

    public static final Model.Finder<Long, Flag> find = new Model.Finder<Long, Flag>(Long.class, Flag.class);

    public String getName()
    {
        return name;
    }

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
	public static List<Flag> filterByName(String name) {
		List<Flag> res = new ArrayList<Flag>();
        ExpressionList<Flag> ll = find.where().icontains(Const.NAME, name);
    	res = ll.findList();
		return res;
	}
        
    /**
     * Retrieve all flags.
     */
    public static List<Flag> findAll() {
        return find.all();
    }
    
    public String toString() {
        return "Flag(" + name + ")" + ", id:" + id;
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
    public static Page<Flag> page(int page, int pageSize, String sortBy, String order, String filter) {

        return find.where().icontains("name", filter)
        		.orderBy(sortBy + " " + order)
        		.findPagingList(pageSize)
        		.setFetchAhead(false)
        		.getPage(page);
    }    

}