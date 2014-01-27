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

/**
 * This class supports the management of licenses.
 */
@Entity
public class License extends Model
{

	/**
	 * file id
	 */
	private static final long serialVersionUID = -2157694575433302989L;

	@Id 
    public Long id;

    /**
     * This field with prefix "act-" builds an unique identifier in W3ACT database.
     */
    @Column(columnDefinition = "TEXT")
    public String url;
	
    /**
     * The name of the e-mail.
     */
    @Column(columnDefinition = "TEXT")
    public String name;
       
    /**
     * License type.
     */
    @Column(columnDefinition = "TEXT")
    public String type;
    
    /**
     * Either text as a string or name of the associated text file.
     */
    @Column(columnDefinition = "TEXT")
    public String text;

    @Version
    public Timestamp lastUpdate;

    public static final Model.Finder<Long, License> find = new Model.Finder<Long, License>(Long.class, License.class);

    public String getName()
    {
        return name;
    }

    public static License findByName(String name)
    {
        return find.where()
                   .eq("name",
                       name)
                   .findUnique();
    }
    
    /**
     * Retrieve a mail template by URL.
     * @param url
     * @return mail template name
     */
    public static License findByUrl(String url) {
    	License res = new License();
    	if (url != null && url.length() > 0 && !url.equals(Const.NONE)) {
    		res = find.where().eq(Const.URL, url).findUnique();
    	} else {
    		res.name = Const.NONE;
    	}
    	return res;
    }
    
    /**
	 * This method filters templates by name and returns a list 
	 * of filtered Template objects.
	 * @param name
	 * @return
	 */
	public static List<License> filterByName(String name) {
		List<License> res = new ArrayList<License>();
        ExpressionList<License> ll = find.where().icontains(Const.NAME, name);
    	res = ll.findList();
		return res;
	}
        
    /**
     * Retrieve all templates.
     */
    public static List<License> findAll() {
        return find.all();
    }
    
    /**
     * This method returns a list of all status values for crawl permission record.
     * @return
     */
    public static List<String> getAllTypes() {
    	List<String> res = new ArrayList<String>();
	    Const.MailTemplateType[] resArray = Const.MailTemplateType.values();
	    for (int i=0; i < resArray.length; i++) {
		    res.add(resArray[i].name());
	    }
	    return res;
    }    
    
    public String toString() {
        return "License(" + name + ")" + ", id:" + id;
    }    

}