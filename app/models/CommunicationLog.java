package models;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;
import uk.bl.Const;
import uk.bl.api.Utils;

import com.avaje.ebean.ExpressionList;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * This class supports the management of logging communications occurring
 * outside ACT. Archivist is treating each individual communication as a unique record.
 */
@Entity
@Table(name = "communication_log")
public class CommunicationLog extends ActModel {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 6095384513958569564L;

	/**
     * The name of the communication.
     */
    @Required(message="Name is required")
    @Column(columnDefinition = "text")
    public String name;

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "user_id")
	public User user;
    
    /**
     * The date of communication in format (dd/mm/yyyy).
     */
    @Column(columnDefinition = "text")
    public String date;
    
    /**
     * Communication type: Email, Phone, Letter, Web Form, Contact Detail Request, Other.
     */
    @Column(columnDefinition = "text")
    public String ttype;
    
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "crawlPermission_id")
	public CrawlPermission crawlPermission;
    
    /**
     * Allows the addition of further notes regarding communication.
     */
    @Column(columnDefinition = "text")
    public String notes;

    public static final Model.Finder<Long, CommunicationLog> find = new Model.Finder<Long, CommunicationLog>(Long.class, CommunicationLog.class);

    public static CommunicationLog findByName(String name) {
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
     * @param crawlPermission The crawl permission
     * @param user The responsible user
     * @param notes Additional information about log entry like save, update, remove
     */
    public static CommunicationLog logHistory(String name, CrawlPermission crawlPermission, User user, String notes) {
        CommunicationLog log = new CommunicationLog();
        log.id = Utils.INSTANCE.createId();
        log.url = Const.ACT_URL + log.id;
//        log.curator = User.findByEmail(request().username()).url; 
        log.user = user;
        log.ttype = Const.CommunicationLogTypes.OTHER.name();
        log.date = Utils.INSTANCE.getCurrentDate();
        log.name = name;
        log.crawlPermission = crawlPermission;
        log.notes = notes;
       	return log;
    }
    
    public String toString() {
        return "CommunicationLog(" + name + ")" + ", id:" + id;
    }
    
    public static Map<String,String> options() {
        LinkedHashMap<String,String> options = new LinkedHashMap<String,String>();
        for(CommunicationLog c: find.all()) {
            options.put(c.id.toString(), c.name);
        }
        return options;
    }


}