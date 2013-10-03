package models;

import java.lang.reflect.Field;
import java.util.*;

import javax.persistence.*;

import play.Logger;
import play.db.ebean.*;
import uk.bl.Const;


/**
 * Organisation entity managed by Ebean
 */
@SuppressWarnings("serial")
@Entity 
public class Organisation extends Model {

    @Id
    public Long nid;
    public String value;
    public String summary;
    public String format;
    public String field_abbreviation;  
    public Long vid;
    public Boolean is_new;
    public String type;
    public String title;
    public String language;
    public String url;
    public String edit_url;
    public Long status;
    public Long promote;
    public Long sticky;
    public String created;
    public String changed;
    public String author;
    public String log;
    public Long comment;
    public Long comment_count;
    public Long comment_count_new;
    // TODO difference between XML and JSON
    public String revision;
    public Long feed_nid;
    
    
    public Organisation(String title) {
        this.title = title;
    }
    
    public Organisation() {
    }
    
    // -- Queries
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public static Model.Finder<Long,Organisation> find = new Model.Finder(Long.class, Organisation.class);
    
    /**
     * Retrieve Organisation for user
     */
    public static List<Organisation> findInvolving() {
        return find.all();
    }
    
    /**
     * Create a new Organisation.
     */
    public static Organisation create(String title) {
        Organisation Organisation = new Organisation(title);
        Organisation.save();
        return Organisation;
    }
    
    /**
     * Rename a Organisation
     */
    public static String rename(Long OrganisationId, String newName) {
        Organisation Organisation = (Organisation) find.ref(OrganisationId);
        Organisation.title = newName;
        Organisation.update();
        return newName;
    }
        
    /**
     * This method translates database view to the HTML view.
     * @return list of Strings
     */
    @SuppressWarnings("unchecked")
	public List<String> get_field_list(String fieldName) {
    	List<String> res = new ArrayList<String>();
    	try {
    		res.add(Const.EMPTY);
			Field field = this.getClass().getField(fieldName); 
	        if (((List<Item>) field.get(this)).size() > 0) {
	        	res.remove(Const.EMPTY);
		        Iterator<Item> itemItr = ((List<Item>) field.get(this)).iterator();
		        while (itemItr.hasNext()) {
		        	Item item = itemItr.next();
		        	res.add(item.value);
		        }
	        }
		} catch (IllegalArgumentException e) {
			Logger.info(e.getMessage());
		} catch (IllegalAccessException e) {
			Logger.info(e.getMessage());
		} catch (SecurityException e) {
			Logger.info(e.getMessage());
		} catch (NoSuchFieldException e) {
			Logger.info(e.getMessage());
		} catch (Exception e) {
			Logger.info(e.getMessage());
		}
    	return res;
    }

    public String toString() {
        return "Organisation(" + nid + ") with title: " + title + 
        	", format: " + format + ", summary: " + summary + ", value: " + value;
    }

}

