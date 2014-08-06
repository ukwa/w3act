package models;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;
import uk.bl.Const;

import com.avaje.ebean.ExpressionList;
import com.avaje.ebean.Page;

/**
 * This class allows archivist to manage open tags.
 */
@Entity
@Table(name = "tag")
public class Tag extends Model
{

	/**
	 * file id
	 */
	private static final long serialVersionUID = -2257699575463702989L;

	@Id 
    public Long id;

    @ManyToOne
    public Target fk_target;
	
    @ManyToOne
    public Target fk_instance;
	
    /**
     * This field with prefix "act-" builds an unique identifier in W3ACT database.
     */
    @Column(columnDefinition = "TEXT")
    public String url;
	
    /**
     * The name of the tag.
     */
    @Required
    @Column(columnDefinition = "TEXT")
    public String name;
    
    /**
     * Allows the addition of further notes regarding tag description.
     */
    @Column(columnDefinition = "TEXT")
    public String description;

    @Version
    public Timestamp lastUpdate;

    public static final Model.Finder<Long, Tag> find = new Model.Finder<Long, Tag>(Long.class, Tag.class);

    public Tag() {
		super();
	}

	public String getName()
    {
        return name;
    }

    public static Tag findByName(String name)
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
	public static List<Tag> filterByName(String name) {
		List<Tag> res = new ArrayList<Tag>();
        ExpressionList<Tag> ll = find.where().icontains(Const.NAME, name);
    	res = ll.findList();
		return res;
	}
        
    /**
     * Retrieve all tags.
     */
    public static List<Tag> findAll() {
        return find.all();
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
    public static Page<Tag> page(int page, int pageSize, String sortBy, String order, String filter) {

        return find.where().icontains("name", filter)
        		.orderBy(sortBy + " " + order)
        		.findPagingList(pageSize)
        		.setFetchAhead(false)
        		.getPage(page);
    }    

}