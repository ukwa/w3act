package models;

import java.lang.reflect.Field;
import java.util.*;

import javax.persistence.*;

import play.Logger;
import play.db.ebean.*;
import uk.bl.Const;


/**
 * DCollection entity managed by Ebean
 */
@SuppressWarnings("serial")
@Entity 
public class DCollection extends Model {

    @Id
    public Long nid;
    @Column(columnDefinition = "TEXT")
    public String value;
    @Column(columnDefinition = "TEXT")
    public String summary;
    public String format;
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
    // lists
    @Column(columnDefinition = "TEXT")
    public String field_targets; 
    @Column(columnDefinition = "TEXT")
    public String field_sub_collections; 

    /**
     * Constructor
     * @param title
     */
    public DCollection(String title) {
        this.title = title;
    }

    public DCollection() {
    }
    
    // -- Queries
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public static Model.Finder<Long,DCollection> find = new Model.Finder(Long.class, DCollection.class);
    
    /**
     * Retrieve dcollections 
     */
    public static List<DCollection> findInvolving() {
        return find.all();
    }
    
    /**
     * Retrieve all collections.
     */
    public static List<DCollection> findAll() {
        return find.all();
    }
    
    /**
     * Create a new dcollection.
     */
    public static DCollection create(String title) {
        DCollection dcollection = new DCollection(title);
        dcollection.save();
        return dcollection;
    }
    
    /**
     * Rename a dcollection
     */
    public static String rename(Long dcollectionId, String newName) {
        DCollection dcollection = (DCollection) find.ref(dcollectionId);
        dcollection.title = newName;
        dcollection.update();
        return newName;
    }
        
    /**
     * This method translates database view to the HTML view.
     * @return list of Strings
     */
	public List<String> get_field_list(String fieldName) {
    	List<String> res = new ArrayList<String>();
    	try {
    		res.add(Const.EMPTY);
			Field field = this.getClass().getField(fieldName); 
			String content = (String) field.get(this);
			res = Arrays.asList(content.split("\\s*,\\s*"));
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
        return "DCollection(" + nid + ") with title: " + title + ", field_targets: " + field_targets +
        	", format: " + format + ", summary: " + summary + ", value: " + value;
    }

}

