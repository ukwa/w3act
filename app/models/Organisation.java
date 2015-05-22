package models;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;
import uk.bl.Const;
import uk.bl.api.models.FieldModel;

import com.avaje.ebean.ExpressionList;
import com.avaje.ebean.Page;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * Organisation entity managed by Ebean
 */
@Entity 
@Table(name = "organisation")
public class Organisation extends ActModel {

    /**
	 * 
	 */
	private static final long serialVersionUID = -651293364349635342L;

    //bi-directional many-to-one association to User
    @JsonIgnore
    @OneToMany(mappedBy="organisation", cascade=CascadeType.ALL)
    public List<User> users;
     
    //bi-directional one-to-many association to Target
    @JsonIgnore
    @OneToMany(mappedBy="organisation", cascade=CascadeType.ALL)
    public List<Target> targets;

	@JsonIgnore
    @ManyToMany
	@JoinTable(name = "organisation_instance", joinColumns = { @JoinColumn(name = "organisation_id", referencedColumnName="id") },
		inverseJoinColumns = { @JoinColumn(name = "instance_id", referencedColumnName="id") }) 
	public List<Instance> instances;
	
    @JsonProperty
    @Required(message="Title is required")
    public String title;

    @JsonIgnore
    public String edit_url;

    @JsonIgnore
    public String summary;
    
	@ManyToOne
	@JoinColumn(name = "author_id")
	@JsonIgnore
	public User authorUser;

    @JsonProperty
    @Required(message="Abbreviation is required")
    @Column(name="affiliation")
    public String field_abbreviation;

    @JsonIgnore
    @Column(columnDefinition = "text")
    public String revision;

    @Transient
    @JsonIgnore
    @Column(columnDefinition = "text") 
    public String value;

    public Organisation() {
    	//    	
    }

//    public Organisation(String title) {
//        this.title = title;
//    }
    
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
     * Retrieve an object by Id (nid).
     * @param nid
     * @return object 
     */
    public static Organisation findById(Long id) {
    	Organisation res = find.where().eq(Const.ID, id).findUnique();
    	return res;
    }          

    public static Organisation findByUrl(String url) {
    	return find.where().eq(Const.URL, url).findUnique();
    }
    
	public static Organisation findByWct(String url) {
		return find.where().eq("edit_url", url).findUnique();
	}
	
    /**
     * This method returns all organisations related alphabetically sorted.
     * @return user list
     */
    public static List<Organisation> findAllSorted() {
    	List<Organisation> organisations = find.where().orderBy("title asc").findList();
        return organisations;
    }
        
    /**
     * Create a new Organisation.
     */
    public static Organisation create(String title) {
        Organisation organisation = new Organisation();
        organisation.title = title;
        organisation.save();
        return organisation;
    }
    
    /**
     * Rename a Organisation
     */
    public static String rename(Long OrganisationId, String newName) {
        Organisation organisation = (Organisation) find.ref(OrganisationId);
        organisation.title = newName;
        organisation.update();
        return newName;
    }
        
    /**
     * Check if given by URL organisation exists in database.
     * @param url
     * @return true if exists
     */
    public static boolean existsByUrl(String url) {
    	boolean res = false;
    	if (url != null && url.length() > 0 && !url.equals(Const.NONE)) {
    		Organisation organisation = find.where().eq(Const.URL, url).findUnique();
    		if (organisation != null) {
    			res = true;
    		}
    	}
    	return res;
    }
    
    /**
     * Check if given by title organisation exists in database.
     * @param url
     * @return true if exists
     */
    public static boolean existsByTitle(String title) {
    	boolean res = false;
    	if (title != null && title.length() > 0 && !title.equals(Const.NONE)) {
    		Organisation organisation = find.where().eq(Const.TITLE, title).findUnique();
    		if (organisation != null) {
    			res = true;
    		}
    	}
    	return res;
    }
    
    /**
     * Retrieve an organisation by title.
     * @param title
     * @return organisation object
     */
    public static Organisation findByTitle(String title) {
//    	Logger.debug("organisation title: " + title);
    	Organisation res = new Organisation();
    	if (title != null && title.length() > 0) {
    		res = find.where().eq(Const.TITLE, title).findUnique();
//        	Logger.debug("res: " + res);
    		if (res == null) {
    			res = new Organisation();
        		res.title = Const.NONE;
        		res.url = Const.NONE;
    		}
    	} else {
    		res.title = Const.NONE;
    		res.url = Const.NONE;
    	}
//    	Logger.debug("final organisation res: " + res);
    	return res;
    }

	/**
	 * This method filters organisations by title and returns a list of filtered Organisation objects.
	 * @param title
	 * @return
	 */
	public static List<Organisation> filterByName(String name) {
		List<Organisation> res = new ArrayList<Organisation>();
        ExpressionList<Organisation> ll = find.where().icontains(Const.TITLE, name);
    	res = ll.findList();
		return res;
	}
    

    /**
     * This method is used for filtering by URL.
     * @param url
     * @return
     */
    public static List<Organisation> findFilteredByUrl(String url) {
    	List<Organisation> ll = new ArrayList<Organisation>();
//    	Logger.debug("organisation findFilteredByUrl(): " + url);
    	if (url != null && url.length() > 0 && !url.equals(Const.NONE)) { 
            Organisation organisation = find.where().eq(Const.URL, url).findUnique();
            ll.add(organisation);            
    	} else {
            ll = find.all();
    	}
    	return ll;
    }

    /**
     * Retrieve all organisations.
     */
    public static List<Organisation> findAll() {
        return find.all();
    }

    /**
     * Return a page of Organisations
     *
     * @param page Page to display
     * @param pageSize Number of Organisations per page
     * @param sortBy Target property used for sorting
     * @param order Sort order (either or asc or desc)
     * @param filter Filter applied on the name column
     */
    public static Page<Organisation> page(int page, int pageSize, String sortBy, String order, String query) {
    	return find.where().icontains(Const.TITLE, query)
        		.orderBy(sortBy + " " + order)
        		.findPagingList(pageSize)
        		.setFetchAhead(false)
        		.getPage(page);
    }

	public static Map<String, String> options() {
        LinkedHashMap<String,String> options = new LinkedHashMap<String,String>();
        for(Organisation s : Organisation.findAll()) {
            options.put(s.id.toString(), s.title);
        }
        return options;
		
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Organisation other = (Organisation) obj;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Organisation [users=" + users + ", targets=" + targets + ", title=" + title
				+ ", edit_url=" + edit_url + ", summary=" + summary
				+ ", authorUser=" + authorUser + ", field_abbreviation="
				+ field_abbreviation + ", revision=" + revision + ", value=" + value
				+ ", id=" + id + ", url=" + url + ", createdAt="
				+ createdAt + ", updatedAt=" + updatedAt + "]";
	}
}

