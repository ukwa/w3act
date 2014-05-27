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

import play.Logger;

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
    @Required
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
     * Retrieve an object by Id (id).
     * @param nid
     * @return object 
     */
    public static ContactPerson findById(Long id) {
    	ContactPerson res = find.where().eq(Const.ID, id).findUnique();
    	return res;
    }          
    
    /**
     * Retrieve a contact person by URL.
     * @param url
     * @return contact person name
     */
    public static ContactPerson findByUrl(String url) {
    	ContactPerson res = new ContactPerson();
    	if (url != null && url.length() > 0 && !url.equals(Const.NONE)) {
    		try {
    			res = find.where().eq(Const.URL, url).findUnique();
    			if (res == null) {
    				res = new ContactPerson();
    				res.name = Const.NONE;
    			}
    		} catch (Exception e) {
    			Logger.info("Contact person: findByUrl error: " + e);
    			res.name = Const.NONE;
    			return res;
    		}    			
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
     * This method is used to show contact person in a table.
     * It shows none value if no entry was found in database.
     * @param url
     * @return
     */
    public static ContactPerson showByUrl(String url) {
    	Logger.info("person findByUrl: " + url);
    	ContactPerson res = new ContactPerson();
    	if (url != null && url.length() > 0 && !url.equals(Const.NONE)) {
    		try {
    			res = find.where().eq(Const.URL, url).findUnique();
            	if (res == null) {
                	res = new ContactPerson();
                	res.name = Const.NONE;            	}
    		} catch (Exception e) {
    			Logger.info("contact person could not be find in database: " + e);
    		}
    	} else {
        	res.name = Const.NONE;
    	}
    	Logger.info("contact person res: " + res);
    	return res;
    }
    	
    /**
     * Retrieve all contact persons.
     */
    public static List<ContactPerson> findAll() {
        return find.all();
    }
       
    /**
     * Retrieve the contact person names by URL list given as a string.
     * @param url
     * @return contact person name list
     */
    public static String findNamesByUrls(String urls) {
    	String res = "";
		Logger.info("findNamesByUrls urls: " + urls);
    	if (urls != null) {
    		if (urls.contains(Const.LIST_DELIMITER)) {
		    	String[] parts = urls.split(Const.LIST_DELIMITER);
		    	for (String part: parts)
		        {
		    		try {
			    		String name = findByUrl(part).name;
			    		res = res + name + Const.LIST_DELIMITER;
		    		} catch (Exception e) {
		    			Logger.info("findNamesByUrls error: " + e);
		    		}
		        }
	    	} else {
	    		if (urls.length() > 0 && !urls.equals(Const.NONE)) {
	    			res = findByUrl(urls).name;   
	    		}
	    	}
    	}
		Logger.info("findNamesByUrls res: " + res);
    	return res;
    }          
	    
    /**
     * Retrieve the contact person emails by URL list given as a string.
     * @param urls
     * @param allMails
     * @return contact person email list
     */
    public static String findEmailsByUrls(String urls, String allMails) {
    	String res = "";
    	if (urls != null) {
	    	if (urls.contains(Const.LIST_DELIMITER)) {
		    	String[] parts = urls.split(Const.LIST_DELIMITER);
		    	for (String part: parts)
		        {
		    		try {
			    		String email = findByUrl(part).email;
			    		if (email != null && !res.contains(email) && !allMails.contains(email)) {
		//	    			System.out.println("test mail: " + email + ", res: " + res);
			    			res = res + email + Const.LIST_DELIMITER;
			    		}
		    		} catch (Exception e) {
		    			System.out.println("findEmailsByUrls error: " + e);
		    		}
		        }
	    	} else {
	    		if (urls.length() > 0 && !urls.equals(Const.NONE)) {
	    			res = findByUrl(urls).email;   
	    		}
	    	}
    	}
    	return res;
    }          
	    
    public String toString() {
        return "ContactPerson(" + name + ")" + ", id:" + id;
    }    

}