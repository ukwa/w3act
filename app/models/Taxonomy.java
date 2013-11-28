package models;

import java.net.URI;
import java.util.*;

import javax.persistence.*;

import com.avaje.ebean.ExpressionList;

import play.Logger;
import play.db.ebean.*;
import uk.bl.Const;
import uk.bl.Const.NodeType;


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
        
        if (!url.contains(Const.COMMA)) {
	        // in order to replace "taxonomy_term" read from target.collection_categories by "taxonomy/term"
//	        url = url.replace("_", "/"); 
	        Taxonomy res2 = find.where().eq(Const.URL, url).findUnique();
	        if (res2 == null) {
	        	res.name = Const.NONE;
	        } else {
	        	res = res2;
	        }
	        Logger.info("taxonomy name: " + res.name);
        }
//        return find.where().eq(Const.URL, url).findUnique();
    	return res;
    }          

//    /**
//     * Retrieve a Taxonomy by taxonomy type (e.g. collection, subject, license).
//     * @param type
//     * @return taxonomy object
//     */
//    public static Taxonomy findByType(String type) {
//    	Taxonomy res = new Taxonomy();
//        
//        if (type != null && type.length() > 0) {
//	        Taxonomy resObj = find.where().eq(Const.TYPE, type).findUnique();
//	        if (resObj == null) {
//	        	res.name = Const.NONE;
//	        } else {
//	        	res = resObj;
//	        }
//	        Logger.info("taxonomy name: " + res.name);
//        }
//    	return res;
//    }          

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
     * Retrieve a Taxonomy list by type.
     * @param taxonomy type
     * @return taxonomy list
     */
	public static List<Taxonomy> findListByType(String type) {
    	List<Taxonomy> res = new ArrayList<Taxonomy>();
    	if (type != null && type.length() > 0) {
//	        res = (List<Taxonomy>) find.where().eq(Const.TYPE, type);
	        ExpressionList<Taxonomy> ll = find.where().eq(Const.TYPE, type);
	    	res = ll.findList(); 
//	        Taxonomy taxonomy = findByType(type);
//   			res.add(taxonomy);
        }
    	return res;
    }
        
    public String toString() {
        return "Taxonomy(" + tid + ") with name: " + name;
    }

}

