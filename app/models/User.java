package models;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

import play.Logger;
import play.data.format.Formats;
import play.data.validation.Constraints;
import play.db.ebean.Model;
import uk.bl.Const;
import uk.bl.api.models.Field_Affiliation;

import com.avaje.ebean.ExpressionList;
import com.avaje.ebean.Page;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * User entity managed by Ebean
 */
@Entity
@Table(name="creator")
public class User extends ActModel {

    /**
	 * 
	 */
	private static final long serialVersionUID = -5018094620896138537L;

	//bi-directional many-to-one association to Organisation
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name="organisation_id") 
	public Organisation organisation;
	
	//bi-directional many-to-many association to Role
	@JsonIgnore
	@ManyToMany
	@JoinTable(name = Const.ROLE_USER, joinColumns = { @JoinColumn(name = "user_id", referencedColumnName="id") },
		inverseJoinColumns = { @JoinColumn(name = "role_id", referencedColumnName="id") }) 
	public List<Role> roles = new ArrayList<Role>();
    	
    @JsonIgnore
    @Constraints.Required
    @Formats.NonEmpty
    public String email;
    
    @JsonIgnore
    public String password;
    
    // FROM ACT
    @Constraints.Required
    public String name;
    @JsonIgnore
    public String fieldAffiliation;
    @JsonIgnore
    public String edit_url;
    @JsonIgnore
    public String last_access;
    @JsonIgnore
    public String last_login;
    @JsonIgnore
    public Long status;
    @JsonIgnore
    public String language;
    @JsonIgnore
    public Long feed_nid;
    
    @Transient
    @JsonProperty
    private Field_Affiliation field_affiliation;
    
    @Transient
    @JsonProperty
    private String uid;
    
    @Transient
    @JsonProperty
    private String created;
    
    @Transient
    @JsonProperty
    private String mail;
    
    public Field_Affiliation getField_affiliation() {
		return field_affiliation;
	}

	public void setField_affiliation(Field_Affiliation field_affiliation) {
		this.field_affiliation = field_affiliation;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getCreated() {
		return created;
	}

	public void setCreated(String created) {
		this.created = created;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	@JsonIgnore
    @Column(columnDefinition = "text")
    public String revision; 

    // -- Queries
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public static Model.Finder<String,User> find = new Model.Finder(String.class, User.class);
    
    public User() {
    	this.revision = "";
    }

    public User(String name) {
    	this.name = name;
    	this.revision = "";
    }

    public User(String name, String email, String password) {
    	this.name = name;
    	this.email = email;
    	this.password = password;
    	this.revision = "";
    }
    
    /**
     * This method checks whether user has a role by its id.
     * @param roleName
     * @return true if exists
     */
    public boolean hasRole(long roleId) {
    	boolean res = false;
//    	Logger.info("hasRole() role id: " + roleId + ", role_to_user.size(): " + role_to_user.size());
    	if (roles != null && roles.size() > 0) {
    		Iterator<Role> itr = roles.iterator();
    		while (itr.hasNext()) {
    			Role role = itr.next();
//    	    	Logger.info("hasRole() role id: " + roleId + ", role_to_user id: " + role.id);
    			if (role.id == roleId) {
    				res = true;
    				break;
    			}
    		}
    	}
//    	Logger.info("hasRole res: " + res);
    	return res;
    }
    
    /**
     * This method checks whether user has a role by its name.
     * @param roleName
     * @return
     */
    public boolean hasRole(String roleName) {
    	boolean res = false;
//    	Logger.info("hasRole: " + roleName);
    	if (roleName != null && roleName.length() > 0) {
    		Role role = Role.findByName(roleName);
//        	Logger.info("role id: " + role.id);
    		res = hasRole(role.id);
    	}
    	return res;
    }
    
    public List<? extends Role> getRoles()
    {
    	return roles;
    }

    /**
     * Retrieve all users.
     */
    public static List<User> findAll() {
        return find.all();
    }

    /**
     * Retrieve an object by Id (uid).
     * @param nid
     * @return object 
     */
    public static User findById(Long uid) {
    	User res = find.where().eq(Const.UID, uid).findUnique();
    	return res;
    }
    
    /**
     * This method returns all users alphabetically sorted.
     * @return user list
     */
    public static List<User> findAllSorted() {
    	List<User> res = new ArrayList<User>();
    	Page<User> page = page(0, find.all().size(), Const.NAME, Const.ASC, "");
    	res = page.getList();
        return res;
    }
        
    /**
     * Retrieve a User from email.
     */
    public static User findByEmail(String email) {
        return find.where().eq("email", email).findUnique();
    }
    
    /**
     * Retrieve a User by name.
     * @param name
     * @return
     */
    public static User findByName(String name) {
        return find.where().eq("name", name).findUnique();
    }
    
    /**
     * Retrieve a User by URL.
     * @param url
     * @return user name
     */
    public static User findByUrl(String url) {
        return find.where().eq(Const.URL, url).findUnique();
    }

    /**
     * Retrieve a User by UID
     * @param id
     * @return
     */
    public static User findByUid(Long id) {
        return find.where().eq(Const.UID, id).findUnique();
    }

    /**
     * This method returns all users related to given organisation.
     * @param organisation The organisation URL
     * @return user list
     */
    public static List<User> findByOrganisation(String organisation) {
    	List<User> res = new ArrayList<User>();
        ExpressionList<User> ll = find.where().eq(Const.FIELD_AFFILIATION, organisation);
        res = ll.findList();
        return res;
    }
    
    /**
     * This method returns all users related to given organisation.
     * @param organisation The organisation URL
     * @return user list
     */
    public static List<User> findByNotEqualOrganisation(String organisation) {
    	List<User> res = new ArrayList<User>();
        ExpressionList<User> ll = find.where().ne(Const.FIELD_AFFILIATION, organisation);
        res = ll.findList();
        return res;
    }
    
    /**
     * This method is used for filtering by URL.
     * @param url
     * @return
     */
    public static List<User> findFilteredByUrl(String url) {
    	List<User> ll = new ArrayList<User>();
//    	Logger.info("user findFilteredByUrl(): " + url);
    	if (url != null && url.length() > 0  && !url.equals(Const.NONE)) { 
            User user = find.where().eq(Const.URL, url).findUnique();
            ll.add(user);            
    	} else {
            ll = find.all();
    	}
    	return ll;
    }

    /**
     * Authenticate a User.
     */
    public static User authenticate(String email, String password) {
        return find.where()
            .eq("email", email)
            .eq("password", password)
            .findUnique();
    }
    
    /**
     * This method calculates memership period for the user.
     * @return
     */
    public String calculate_membership() {
    	String res = "";
//    	Logger.info("created: " + created + ", last_access: " + last_access + ", last_login: " + last_login);
    	try {
    		long timestampCreated = createdAt.getTime();
    		Date dateCreated = new Date(timestampCreated * 1000);
    		long timestampLastAccess = Long.valueOf(last_access);
    		Date dateLastAccess = new Date(timestampLastAccess * 1000);
			Logger.info("date created: " + dateCreated);
			Logger.info("date last access: " + dateLastAccess);
			 
			DateTime dt1 = new DateTime(dateCreated);
			DateTime dt2 = new DateTime(dateLastAccess);
	 
			Period period = new Period(dt1, dt2);
			PeriodFormatterBuilder formaterBuilder = new PeriodFormatterBuilder()
		        .appendMonths().appendSuffix(" months ")
		        .appendWeeks().appendSuffix(" weeks");
			PeriodFormatter pf = formaterBuilder.toFormatter();
//	        Logger.info(pf.print(period));
	        res = pf.print(period);
		} catch (Exception e) {
			Logger.info("date difference calculation error: " + e);
		}
    	return res;
    }
    
    /**
     * This method calculates target number for given user URL.
     * @return
     */
    public int getTargetNumberByCuratorUrl() {
    	int res = 0;
    	res = Target.getTargetNumberByCuratorUrl(this.url);
    	return res;
    }
    
	/**
	 * This method filters users by name and returns a list of filtered User objects.
	 * @param name
	 * @return
	 */
	public static List<User> filterByName(String name) {
		List<User> res = new ArrayList<User>();
        ExpressionList<User> ll = find.where().contains(Const.EMAIL, name.toLowerCase());
    	res = ll.findList();
		return res;
	}
    
    // Could really do with many_to_one relationship
    public Organisation getOrganisation() {
    	return Organisation.findByUrl(fieldAffiliation);
    }

    /**
     * This method shows user in HTML page.
     * @param userUrl The link to user in Target object field 'author'
     * @return
     */
    public static String showUser(String userUrl) {
        return User.findByUrl(userUrl).name; 
    }
            
    /**
     * Return a page of User
     *
     * @param page Page to display
     * @param pageSize Number of Users per page
     * @param sortBy User property used for sorting
     * @param order Sort order (either or asc or desc)
     * @param filter Filter applied on the name column
     */
    public static Page<User> page(int page, int pageSize, String sortBy, String order, String filter) {

        return find.where().icontains("name", filter)
        		.orderBy(sortBy + " " + order)
        		.findPagingList(pageSize)
        		.setFetchAhead(false)
        		.getPage(page);
    }
    
    /**
     * This method updates foreign key mapping between a user and an organisation.
     */
    public void updateOrganisation() {
		if (fieldAffiliation != null
				&& fieldAffiliation.length() > 0) {
			Organisation organisation = Organisation.findByUrl(fieldAffiliation);
//            Logger.info("Add creator to organisation: " + organisation.toString());
            this.organisation = organisation;
		}    	
    }

	@Override
	public String toString() {
		return "User [organisation=" + organisation + ", roles=" + roles
				+ ", email=" + email + ", password=" + password + ", name="
				+ name + ", fieldAffiliation=" + fieldAffiliation
				+ ", edit_url=" + edit_url + ", last_access=" + last_access
				+ ", last_login=" + last_login + ", status=" + status
				+ ", language=" + language + ", feed_nid=" + feed_nid
				+ ", field_affiliation=" + field_affiliation + ", uid=" + uid
				+ ", created=" + created + ", mail=" + mail + ", revision="
				+ revision + ", id=" + id + ", url=" + url + ", createdAt="
				+ createdAt + ", updatedAt=" + updatedAt + "]";
	}
}

