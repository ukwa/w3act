package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;
import uk.bl.Const;

import com.avaje.ebean.ExpressionList;

/**
 * This class allows archivist to log if crawl permission is refused 
 * (usually outside ACT), and why.
 */
@Entity
@Table(name = "PermissionRefusal")
public class PermissionRefusal extends ActModel {

	/**
	 * file id
	 */
	private static final long serialVersionUID = -2257699575463302989L;

    /**
     * The name of the refusal.
     */
    @Required
    @Column(columnDefinition = "text")
    public String name;
    
    /**
     * The date of refusal in format (dd/mm/yyyy).
     */
    @Column(columnDefinition = "text")
    public String date;
    
    /**
     * Refusal type: 3rd party content, Impracticality, Internal reasons, 
     * Legalistic form, No reason, Privacy, Other.
     */
    @Column(columnDefinition = "text")
    public String ttype;
    
    /**
     * Allows the addition of further notes regarding refusal reason.
     */
    @Column(columnDefinition = "text")
    public String reason;

    public static final Model.Finder<Long, PermissionRefusal> find = new Model.Finder<Long, PermissionRefusal>(Long.class, PermissionRefusal.class);

    public String getName()
    {
        return name;
    }

    public static PermissionRefusal findByName(String name)
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
    public static PermissionRefusal findById(Long id) {
    	PermissionRefusal res = find.where().eq(Const.ID, id).findUnique();
    	return res;
    }          
        
    /**
     * Retrieve a permission refusal by URL.
     * @param url
     * @return permission refusal name
     */
    public static PermissionRefusal findByUrl(String url) {
    	PermissionRefusal res = new PermissionRefusal();
    	if (url != null && url.length() > 0 && !url.equals(Const.NONE)) {
    		res = find.where().eq(Const.URL, url).findUnique();
    	} else {
    		res.name = Const.NONE;
    	}
    	return res;
    }
    
	/**
	 * This method filters permission refusals by name and returns a list 
	 * of filtered PermissionRefusal objects.
	 * @param name
	 * @return
	 */
	public static List<PermissionRefusal> filterByName(String name) {
		List<PermissionRefusal> res = new ArrayList<PermissionRefusal>();
        ExpressionList<PermissionRefusal> ll = find.where().icontains(Const.NAME, name);
    	res = ll.findList();
		return res;
	}
        
    /**
     * Retrieve all permission refusals.
     */
    public static List<PermissionRefusal> findAll() {
        return find.all();
    }
    
    /**
     * This method returns a list of all refusal type values for crawl permission record.
     * @return
     */
    public static List<String> getAllTypes() {
    	List<String> res = new ArrayList<String>();
	    Const.RefusalType[] resArray = Const.RefusalType.values();
	    for (int i=0; i < resArray.length; i++) {
		    res.add(resArray[i].name());
	    }
	    return res;
    }         
    
    public String toString() {
        return "PermissionRefusal(" + name + ")" + ", id:" + id;
    }    

}