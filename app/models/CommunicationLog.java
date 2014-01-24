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
    public String type;
    
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
    
    public String toString() {
        return "CommunicationLog(" + name + ")" + ", id:" + id;
    }    

}