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
 * This class describes the contact person details.
 */
@Entity
public class ContactPerson extends Model
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2257099575463302989L;

	@Id 
    public Long id;

    /**
     * This field with prefix "act-" builds an unique identifier in W3ACT database.
     */
    @Column(columnDefinition = "TEXT")
    public String url;
	
    /**
     * The name of the contact person.
     */
    @Column(columnDefinition = "TEXT")
    public String name;
    
    /**
     * The job or position of the contact person within the relevant organisation.
     */
    @Column(columnDefinition = "TEXT")
    public String position;
    
    /**
     * Telephone contact details of the contact.
     */
    @Column(columnDefinition = "TEXT")
    public String phone;
    
    /**
     * E-mail address of the contact person.
     */
    @Column(columnDefinition = "TEXT")
    public String email;
    
    /**
     * The postal address of the contact person.
     */
    @Column(columnDefinition = "TEXT")
    public String postalAddress;
    
    /**
     * The URL of a contact web form 
     * (in the absence of a known email address).
     */
    @Column(columnDefinition = "TEXT")
    public String webForm;
    
    @Column(columnDefinition = "TEXT")
    public String description;
    
    @Column(columnDefinition = "TEXT")
    public String contactOrganisation; 
       
    /**
     * This is a checkbox defining whether this contact
     * should be regarded as the first/default contact.
     */
    public Boolean defaultContact;
    
    /**
     * This field indicates that an initial form response to
     * permission request has been inspected by Archivist.
     */
    public Boolean permissionChecked;
    
    @Version
    public Timestamp lastUpdate;

    public static final Model.Finder<Long, ContactPerson> find = new Model.Finder<Long, ContactPerson>(Long.class, ContactPerson.class);

    public String getName()
    {
        return name;
    }

    public static ContactPerson findByName(String name)
    {
        return find.where()
                   .eq("name",
                       name)
                   .findUnique();
    }

    /**
     * Retrieve a contact person by URL.
     * @param url
     * @return contact person name
     */
    public static ContactPerson findByUrl(String url) {
    	ContactPerson res = new ContactPerson();
    	if (url != null && url.length() > 0 && !url.equals(Const.NONE)) {
    		res = find.where().eq(Const.URL, url).findUnique();
    	} else {
    		res.name = Const.NONE;
    	}
    	return res;
    }

    
	/**
	 * This method filters contact persons by name and returns a list 
	 * of filtered contact person objects.
	 * @param name
	 * @return
	 */
	public static List<ContactPerson> filterByName(String name) {
		List<ContactPerson> res = new ArrayList<ContactPerson>();
        ExpressionList<ContactPerson> ll = find.where().icontains(Const.NAME, name);
    	res = ll.findList();
		return res;
	}
        
    /**
     * Retrieve all contact persons.
     */
    public static List<ContactPerson> findAll() {
        return find.all();
    }
    
    /**
     * Retrieve a contact person by name.
     * @param name
     * @return contact person object
     */
//    public static ContactPerson findByName(String name) {
//    	ContactPerson res = new ContactPerson();
//    	if (name != null && name.length() > 0) {
//    		res = find.where().eq(Const.NAME, name).findUnique();
//    	} else {
//    		res.name = Const.NONE;
//    	}
//    	return res;
//    }
       
    /**
     * Retrieve the contact person names by URL list given as a string.
     * @param url
     * @return contact person name list
     */
    public static String findNamesByUrls(String urls) {
    	String res = "";
    	String[] parts = urls.split(Const.LIST_DELIMITER);
    	for (String part: parts)
        {
    		try {
	    		String name = findByUrl(part).name;
	    		res = res + name + Const.LIST_DELIMITER;
    		} catch (Exception e) {
    			System.out.println("findNamesByUrls error: " + e);
    		}
        }
    	return res;
    }          
	    
    public String toString() {
        return "ContactPerson(" + name + ")" + ", id:" + id;
    }    

}