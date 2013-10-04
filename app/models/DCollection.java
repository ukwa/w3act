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
    public String summary;
    public String format;
//    @OneToMany(cascade=CascadeType.ALL)  
//    public List<Item> field_targets;
    public List<String> field_targets;
//    @OneToMany(cascade=CascadeType.ALL)  
//    public List<Item> field_sub_collections;
    public List<String> field_sub_collections;
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
        return "DCollection(" + nid + ") with title: " + title;// + ", field_targets: " + field_targets.size() +
//        	", format: " + format + ", summary: " + summary + ", value: " + value;
    }

}

