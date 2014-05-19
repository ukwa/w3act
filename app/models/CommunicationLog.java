package models;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Version;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;
import uk.bl.Const;

import com.avaje.ebean.ExpressionList;

import uk.bl.api.Utils;

/**
 * This class supports the management of logging communications occurring
 * outside ACT. Archivist is treating each individual communication as a unique record.
 */
@Entity
public class CommunicationLog extends Model
{

	/**
	 * file id
	 */
	private static final long serialVersionUID = -2157699575463302989L;

	@Id 
    public Long id;

    /**
     * This field with prefix "act-" builds an unique identifier in W3ACT database.
     */
    @Column(columnDefinition = "TEXT")
    public String url;
	
    /**
     * The name of the communication.
     */
    @Required
    @Column(columnDefinition = "TEXT")
    public String name;
    
    /**
     * The name of the curator with whom the communication took place.
     * This name should be auto-populated with name of logged-in user.
     */
    @Column(columnDefinition = "TEXT")
    public String curator;
    
    /**
     * The date of communication in format (dd/mm/yyyy).
     */
    @Column(columnDefinition = "TEXT")
    public String date;
    
    /**
     * Communication type: Email, Phone, Letter, Web Form, Contact Detail Request, Other.
     */
    @Column(columnDefinition = "TEXT")
    public String ttype;
    
    /**
     * This field is for associated crawl permission ID.
     */
    @Column(columnDefinition = "TEXT")
    public String permission;
    
    /**
     * Allows the addition of further notes regarding communication.
     */
    @Column(columnDefinition = "TEXT")
    public String notes;

    @Version
    public Timestamp lastUpdate;

    public static final Model.Finder<Long, CommunicationLog> find = new Model.Finder<Long, CommunicationLog>(Long.class, CommunicationLog.class);

    public String getName()
    {
        return name;
    }

    public static CommunicationLog findByName(String name)
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
    public static CommunicationLog findById(Long id) {
    	CommunicationLog res = find.where().eq(Const.ID, id).findUnique();
    	return res;
    }          
    
    /**
     * Retrieve a communication log by URL.
     * @param url
     * @return communication log name
     */
    public static CommunicationLog findByUrl(String url) {
    	CommunicationLog res = new CommunicationLog();
    	if (url != null && url.length() > 0 && !url.equals(Const.NONE)) {
    		res = find.where().eq(Const.URL, url).findUnique();
    	} else {
    		res.name = Const.NONE;
    	}
    	return res;
    }
    
	/**
	 * This method filters communications by name and returns a list 
	 * of filtered CommunicationLog objects.
	 * @param name
	 * @return
	 */
	public static List<CommunicationLog> filterByName(String name) {
		List<CommunicationLog> res = new ArrayList<CommunicationLog>();
        ExpressionList<CommunicationLog> ll = find.where().icontains(Const.NAME, name);
    	res = ll.findList();
		return res;
	}
        
    /**
     * Retrieve all communications.
     */
    public static List<CommunicationLog> findAll() {
        return find.all();
    }
    
    /**
     * This method returns a list of all refusal type values for crawl permission record.
     * @return
     */
    public static List<String> getAllTypes() {
    	List<String> res = new ArrayList<String>();
	    Const.CommunicationLogTypes[] resArray = Const.CommunicationLogTypes.values();
	    for (int i=0; i < resArray.length; i++) {
		    res.add(resArray[i].name());
	    }
	    return res;
    }         
        
    /**
     * This method is used for automated logging of crawl permission events
     * like creation of permission, date when permission was queued, send and granted
     * or refused. 
     * @param name The title of the log entry
     * @param permission The crawl permission
     * @param user The responsible user
     * @param notes Additional information about log entry like save, update, remove
     */
    public static CommunicationLog logHistory(String name, String permission, String user, String notes) {
        CommunicationLog log = new CommunicationLog();
        log.id = Utils.createId();
        log.url = Const.ACT_URL + log.id;
//        log.curator = User.find.byId(request().username()).url; 
        log.curator = user;
        log.ttype = Const.CommunicationLogTypes.OTHER.name();
        log.date = Utils.getCurrentDate();
        log.name = name;
        log.permission = permission;
        log.notes = notes;
       	return log;
    }
    
    public String toString() {
        return "CommunicationLog(" + name + ")" + ", id:" + id;
    }    

}