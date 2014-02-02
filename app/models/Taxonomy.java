package models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import play.Logger;
import play.db.ebean.Model;
import uk.bl.Const;

import com.avaje.ebean.ExpressionList;


/**
 * Taxonomy entity managed by Ebean
 */
@SuppressWarnings("serial")
@Entity 
public class Taxonomy extends Model {
     
    @Id
    public Long tid;
    public String name; 
    // additional field to make a difference between collection, subject, license and quality issue. 
    public String type;  
    @Column(columnDefinition = "TEXT")
    public String description;
    public Long weight;
    public Long node_count;
    @Column(columnDefinition = "TEXT")
    public String url;
    @Column(columnDefinition = "TEXT")
    public String vocabulary;
    public Long feed_nid;    
    // lists
    @Column(columnDefinition = "TEXT") 
    public String field_owner;
    @Column(columnDefinition = "TEXT") 
    public String parent;
    @Column(columnDefinition = "TEXT") 
    public String parents_all;

    public Taxonomy(String name) {
        this.name = name;
    }
    
    public Taxonomy() {
    }
    
    // -- Queries
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public static Model.Finder<Long,Taxonomy> find = new Model.Finder(Long.class, Taxonomy.class);
    
    /**
     * Retrieve Taxonomy for user
     */
    public static List<Taxonomy> findInvolving() {
        return find.all();
    }
    
    /**
     * Retrieve all Taxonomies
     */
    public static List<Taxonomy> findAll() {
        return find.all();
    }
    
    /**
     * Create a new Taxonomy.
     */
    public static Taxonomy create(String name) {
        Taxonomy Taxonomy = new Taxonomy(name);
        Taxonomy.save();
        return Taxonomy;
    }
    
    /**
     * Rename a Taxonomy
     */
    public static String rename(Long TaxonomyId, String newName) {
        Taxonomy Taxonomy = (Taxonomy) find.ref(TaxonomyId);
        Taxonomy.name = newName;
        Taxonomy.update();
        return newName;
    }
    
    /**
     * Retrieve a Taxonomy by URL.
     * @param url
     * @return taxonomy object
     */
    public static Taxonomy findByUrl(String url) {
    	Taxonomy res = new Taxonomy();
//        Logger.info("taxonomy url: " + url);
        
        if (url != null && url.length() > 0 && !url.contains(Const.COMMA)) {
	        // in order to replace "taxonomy_term" read from target.collection_categories by "taxonomy/term"
//	        url = url.replace("_", "/"); 
	        Taxonomy res2 = find.where().eq(Const.URL, url).findUnique();
	        if (res2 == null) {
	        	res.name = Const.NONE;
	        } else {
	        	res = res2;
	        }
//	        Logger.info("taxonomy name: " + res.name);
        } else {
        	res.name = Const.NONE;
        }
//        return find.where().eq(Const.URL, url).findUnique();
    	return res;
    }          

    /**
     * Retrieve a Taxonomy names by URL list given as a string.
     * @param url
     * @return taxonomy object
     */
    public static String findNamesByUrls(String urls) {
    	String res = "";
    	if (urls != null) {
	    	if (urls.contains(Const.LIST_DELIMITER)) {
		    	String[] parts = urls.split(Const.LIST_DELIMITER);
		    	for (String part: parts)
		        {
		    		String name = findByUrl(part).name;
		    		res = res + name + Const.LIST_DELIMITER;
		        }
	    	} else {
	    		res = urls;    	
	    	}
    	}
    	return res;
    }          

    /**
     * Retrieve a Taxonomy list by URL.
     * @param url
     * @return taxonomy name
     */
    public static List<Taxonomy> findListByUrl(String url) {
    	List<Taxonomy> res = new ArrayList<Taxonomy>();
//        Logger.info("taxonomy url: " + url);
    	if (url != null && url.length() > 0) {
    		if (url.contains(Const.COMMA)) {
    			List<String> resList = Arrays.asList(url.split(Const.COMMA));
    			Iterator<String> itr = resList.iterator();
    			while (itr.hasNext()) {
        			Taxonomy taxonomy = findByUrl(itr.next());
        			res.add(taxonomy);
    			}
    		} else {
    			Taxonomy taxonomy = findByUrl(url);
    			res.add(taxonomy);
    		}
        }
    	return res;
    }
        
    /**
     * Retrieve a taxonomy by title.
     * @param title
     * @return taxonomy object
     */
    public static Taxonomy findByName(String name) {
    	Taxonomy res = new Taxonomy();
    	if (name != null && name.length() > 0) {
//    		Logger.info("p1: " + name);
    		if (name.contains(Const.COMMA)) {
    			name = name.replace(Const.COMMA, Const.COMMA + " "); // in database entry with comma has additional space after comma
    		}
    		res = find.where().eq(Const.NAME, name).findUnique();
    	} else {
    		res.name = Const.NONE;
    	}
		Logger.info("res: " + res);
    	return res;
    }
    
    /**
     * Retrieve a taxonomy by title.
     * @param title
     * @return taxonomy object
     */
    public static Taxonomy findByFullName(String name) {
    	Taxonomy res = new Taxonomy();
    	if (name != null && name.length() > 0) {
    		res = find.where().eq(Const.NAME, name).findUnique();
    	} else {
    		res.name = Const.NONE;
    	}
		Logger.info("res: " + res);
    	return res;
    }
    
    /**
     * Retrieve a Taxonomy list by type.
     * @param taxonomy type
     * @return taxonomy list
     */
	public static List<Taxonomy> findListByType(String type) {
    	List<Taxonomy> res = new ArrayList<Taxonomy>();
    	if (type != null && type.length() > 0) {
	        ExpressionList<Taxonomy> ll = find.where().eq(Const.TYPE, type);
	    	res = ll.findList(); 
        }
    	return res;
    }
        
	/**
	 * This method retrieves subject index by name.
	 * @param subject
	 * @return subject index in selection list
	 */
	public static int getSubjectIndexByName(String subject) {
		int res = 0;
		List<Taxonomy> subjectList = findListByType(Const.SUBJECT);
		Iterator<Taxonomy> itr = subjectList.iterator();
		while (itr.hasNext()) {
			Taxonomy taxonomy = itr.next();
			if (taxonomy.name.equals(subject)) {
				break;
			}
			res++;			
		}
		return res;
	}
	
	/**
	 * This method filters taxonomies by name and returns a list 
	 * of filtered taxonomy objects.
	 * @param name
	 * @return
	 */
	public static List<Taxonomy> filterByName(String name) {
		List<Taxonomy> res = new ArrayList<Taxonomy>();
        ExpressionList<Taxonomy> ll = find.where().icontains(Const.NAME, name);
    	res = ll.findList();
		return res;
	}
        	
    public String toString() {
        return "Taxonomy(" + tid + ") with name: " + name;
    }

}

