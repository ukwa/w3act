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

@Entity
public class LookupEntry extends Model
{
    /**
	 * 
	 */
	private static final long serialVersionUID = -2250699575468302989L;

	@Id 
    public Long id;

    /**
     * This field with prefix "act-" builds an unique identifier in W3ACT database.
     */
    @Column(columnDefinition = "TEXT")
    public String url;

    /**
     * This field contains stored lookup URLs.
     */
    @Column(columnDefinition = "TEXT")
    public String name;

    /**
     * The type of lookup can be e.g. WHOIS, GEOIP ...
     */
    @Column(columnDefinition = "TEXT")
    public String ttype;
    
    /**
     * Value is true or false.
     */
    public Boolean scopevalue;
    
    @Version
    public Timestamp lastUpdate;

    public static final Model.Finder<Long, LookupEntry> find = new Model.Finder<Long, LookupEntry>(Long.class, LookupEntry.class);

    public String getName()
    {
        return name;
    }

    public static LookupEntry findByName(String name)
    {
        return find.where()
                   .eq("name",
                       name)
                   .findUnique();
    }
    
    /**
     * Retrieve a lookup entry by URL.
     * @param url
     * @return lookup entry name
     */
    public static LookupEntry findByUrl(String url) {
    	LookupEntry res = new LookupEntry();
    	if (url != null && url.length() > 0 && !url.equals(Const.NONE)) {
    		res = find.where().eq(Const.URL, url).findUnique();
    	} else {
    		res.name = Const.NONE;
    	}
    	return res;
    }

    /**
     * Retrieve a lookup entry value by URL.
     * @param url
     * @return lookup entry value (true or false)
     */
    public static boolean getValueByUrl(String url) {
    	boolean res = false;
    	LookupEntry resLookupEntry = findByUrl(url);    	
    	if (resLookupEntry != null && resLookupEntry.scopevalue != null) {
    		res = resLookupEntry.scopevalue;
    	}
    	return res;
    }

	/**
	 * This method filters lookup entry by name and returns a list 
	 * of filtered LookupEntry objects.
	 * @param name
	 * @return
	 */
	public static List<LookupEntry> filterByName(String name) {
		List<LookupEntry> res = new ArrayList<LookupEntry>();
        ExpressionList<LookupEntry> ll = find.where().icontains(Const.NAME, name);
    	res = ll.findList();
		return res;
	}
        
   /**
     * Retrieve all lookup entry objects.
     */
    public static List<LookupEntry> findAll() {
        return find.all();
    }
    
    public String toString() {
        return "LookupEntry(" + url + ")" + ", id:" + id + ", scopevalue: " + scopevalue;
    }
    
}