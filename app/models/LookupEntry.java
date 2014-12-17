package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import play.Logger;
import play.db.ebean.Model;
import uk.bl.Const;

import com.avaje.ebean.ExpressionList;

@Entity
@Table(name = "lookup_entry")
public class LookupEntry extends ActModel {
    /**
	 * 
	 */
	private static final long serialVersionUID = -2250699575468302989L;

    /**
     * This field contains stored lookup URLs.
     */
    @Column(columnDefinition = "text")
    public String name;

    /**
     * The type of lookup can be e.g. WHOIS, GEOIP ...
     */
    @Column(columnDefinition = "text")
    public String ttype;
    
    /**
     * Value is true or false.
     */
    public Boolean scopevalue;
    
	@ManyToOne
	@JoinColumn(name = "target_id")
	public Target target;
	
    @Transient
    public Integer totalRows;
    
    public static final Model.Finder<Long, LookupEntry> find = new Model.Finder<Long, LookupEntry>(Long.class, LookupEntry.class);

    public String getName()
    {
        return name;
    }

    public static LookupEntry findByName(String name)
    {
    	Logger.debug("findByName() name: " + name);
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
    	Logger.debug("findByUrl() url: " + url);
    	LookupEntry res = new LookupEntry();
    	if (url != null && url.length() > 0 && !url.equals(Const.NONE)) {
    		res = find.where().eq(Const.URL, url).findUnique();
    	} else {
    		res.name = Const.NONE;
    	}
    	return res;
    }

    /**
     * Retrieve a lookup entry by name.
     * @param name The name of given site (URL)
     * @return lookup entry name
     */
    public static LookupEntry findBySiteName(String name) {
    	Logger.info("findBySiteName() name: " + name);
    	LookupEntry res = new LookupEntry();
    	
		List<LookupEntry> list = new ArrayList<LookupEntry>();
    	if (name != null && name.length() > 0 && !name.equals(Const.NONE)) {
	        ExpressionList<LookupEntry> ll = find.where().eq(Const.NAME, name);
//    		res = find.where().eq(Const.NAME, name).findUnique();
	    	list = ll.findList(); 
    	}
    	LookupEntry lookupEntry = null;
		if (list.size() > 0) {
	        lookupEntry = list.get(0);
		}
		if (lookupEntry == null) {
			res.name = Const.NONE;
    	} else {
    		res = lookupEntry;
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
    	Logger.info("getValueByUrl() url: " + url);
    	LookupEntry resLookupEntry = findBySiteName(url);    	
//    	Logger.debug("getValueByUrl() resLookupEntry: " + resLookupEntry);
    	if (resLookupEntry != null && resLookupEntry.scopevalue != null) {
        	Logger.info("getValueByUrl() resLookupEntry.scopevalue: " + resLookupEntry.scopevalue);
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
    	Logger.info("LookupEntry filterByName res size: " + res.size());
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