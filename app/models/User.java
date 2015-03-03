package models;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Date;
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

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

import play.Logger;
import play.data.format.Formats;
import play.data.validation.Constraints;
import play.db.ebean.Model;
import uk.bl.Const;
import uk.bl.api.PasswordHash;
import uk.bl.api.models.FieldModel;
import uk.bl.exception.ActException;

import com.avaje.ebean.Expr;
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

	@JsonIgnore
	@OneToMany(mappedBy = "authorUser", cascade = CascadeType.PERSIST)
	public List<Target> targets;

	@JsonIgnore
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	public List<CrawlPermission> crawlPermissions;

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name="organisation_id") 
	public Organisation organisation;
	
	@JsonIgnore
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "role_user", joinColumns = { @JoinColumn(name = "user_id", referencedColumnName="id") },
		inverseJoinColumns = { @JoinColumn(name = "role_id", referencedColumnName="id") }) 
	public List<Role> roles = new ArrayList<Role>();
    	
    @Constraints.Required(message="Email is required")
    @Formats.NonEmpty
	@JsonProperty("mail")
    public String email;
    
    @JsonIgnore
    public String password;
    
    // FROM ACT
    @Constraints.Required
    public String name;
    
    @JsonIgnore
    public String affiliation;
    
    @JsonIgnore
    @JsonProperty
    public String edit_url;
    
    @Transient
    public Role role;
    
    @Transient
    @JsonIgnore
    @JsonProperty
    public String last_access;
    
    @Transient
    @JsonIgnore
    @JsonProperty
    public String last_login;
    
    @Transient
    @JsonIgnore
    @JsonProperty
    public Long status;
    
    @Transient
    @JsonIgnore
    public String roleHolder;
    
    @Transient
    @JsonIgnore
    @JsonProperty
    public String language;
    
    @Transient
    @JsonIgnore
    @JsonProperty
    public Long feed_nid;
    
    @Transient
    @JsonIgnore
    @JsonProperty
    private FieldModel field_affiliation;
    
    @Transient
    @JsonIgnore
    @JsonProperty
    private String uid;
    
    @Transient
    @JsonIgnore
    @JsonProperty
    private Long created;
    
    
	@Transient
	@JsonProperty("roles")
    @JsonIgnore
    private Object rolesAct;
    
	@Transient
    private String theme;
    
//    {
//    	"uid":"1",
//    	"name":"admin",
//    	"mail":"wa-sysadmin@bl.uk",
//    	"url":"http:\/\/webarchive.org.uk\/act\/user\/1",
//    	"edit_url":"http:\/\/webarchive.org.uk\/act\/user\/1\/edit",
//    	"last_access":"1421857188",
//    	"last_login":"1421840203",
//    	"created":"1355148005",
//    	"roles":[2,3],
//    	"status":"1",
//    	"theme":"ukwa_basic",
//    	"language":"en",
//    	"feed_nid":null
//    }
    
    public FieldModel getField_affiliation() {
		return field_affiliation;
	}

	public void setField_affiliation(FieldModel field_affiliation) {
		this.field_affiliation = field_affiliation;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public Long getCreated() {
		return created;
	}

	public void setCreated(Long created) {
		this.created = created;
	}

	public Object getRolesAct() {
		return rolesAct;
	}

	public void setRolesAct(Object rolesAct) {
		this.rolesAct = rolesAct;
	}

	public String getTheme() {
		return theme;
	}

	public void setTheme(String theme) {
		this.theme = theme;
	}

	@JsonIgnore
    @Column(columnDefinition = "text")
    public String revision; 

    // -- Queries
    
    public static Model.Finder<Long,User> find = new Model.Finder<>(Long.class, User.class);
    
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
     * Retrieve all users.
     */
    public static List<User> findAll() {
        return find.all();
    }
    
    public boolean isSysAdmin() {
    	return (hasRole("sys_admin"));
    }

    /**
     * This method checks whether user has a role by its name.
     * @param roleName
     * @return
     */
    public boolean hasRole(String roleName) {
    	boolean res = false;
    	if (StringUtils.isNotBlank(roleName)) {
    		Role role = Role.findByName(roleName);
    		res = this.hasRole(role);
    	}
    	return res;
    }
    
    public boolean hasRole(Role role) {
    	return (this.roles != null && this.roles.contains(role));
    }

    /**
     * This method returns all users alphabetically sorted.
     * @return user list
     */
    public static List<User> findAllSorted() {
    	List<User> users = find.where().orderBy("name asc").findList();
        return users;
    }
        
    /**
     * Retrieve a User from email.
     */
    public static User findByEmail(String email) {
        return find.where().ieq("email", email).findUnique();
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
    
    public static User findById(Long id) {
    	User res = find.byId(id);
    	return res;
    }
    
	public static User findByWct(String url) {
		return find.where().eq("edit_url", url).findUnique();
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
    public static List<User> findByNotEqualOrganisation(Long organisationId) {
    	List<User> res = new ArrayList<User>();
        ExpressionList<User> ll = find.where().add(Expr.or(Expr.isNull("organisation.id"), Expr.ne("organisation.id", organisationId)));
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
//    	Logger.debug("user findFilteredByUrl(): " + url);
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
    	Logger.debug("createdAt: " + createdAt + ", last_access: " + last_access + ", last_login: " + last_login);
    	try {
    		long timestampCreated = createdAt.getTime();
    		Date dateCreated = new Date(timestampCreated * 1000);
    		long timestampLastAccess = Long.valueOf(last_access);
    		Date dateLastAccess = new Date(timestampLastAccess * 1000);
			Logger.debug("date created: " + dateCreated);
			Logger.debug("date last access: " + dateLastAccess);
			 
			DateTime dt1 = new DateTime(dateCreated);
			DateTime dt2 = new DateTime(dateLastAccess);
	 
			Period period = new Period(dt1, dt2);
			PeriodFormatterBuilder formaterBuilder = new PeriodFormatterBuilder()
		        .appendMonths().appendSuffix(" months ")
		        .appendWeeks().appendSuffix(" weeks");
			PeriodFormatter pf = formaterBuilder.toFormatter();
//	        Logger.debug(pf.print(period));
	        res = pf.print(period);
		} catch (Exception e) {
			Logger.debug("date difference calculation error: " + e);
		}
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
//    public Organisation getOrganisation() {
//    	return Organisation.findByUrl(affiliation);
//    }
//
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
    
    public static Map<String,String> options() {
        LinkedHashMap<String,String> options = new LinkedHashMap<String,String>();
        for(User c: User.findAll()) {
            options.put(c.id.toString(), c.name);
        }
        return options;
    }

    public void changePassword(String password) throws ActException {
		try {
			this.password = PasswordHash.createHash(password);
//	        this.password = Hash.createPassword(password);
	        this.save();
	    } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			throw new ActException(e);
		}
    }
    
    @Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		User other = (User) obj;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (id == null) {
			if (other.uid != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "User [organisation=" + organisation + ", roles=" + roles
				+ ", email=" + email + ", password=" + password + ", name="
				+ name + ", fieldAffiliation=" + affiliation
				+ ", edit_url=" + edit_url + ", last_access=" + last_access
				+ ", last_login=" + last_login + ", status=" + status
				+ ", language=" + language + ", feed_nid=" + feed_nid
				+ ", field_affiliation=" + field_affiliation + ", uid=" + uid
				+ ", created=" + created + ", revision="
				+ revision + ", id=" + id + ", url=" + url + ", createdAt="
				+ createdAt + ", updatedAt=" + updatedAt + "]";
	}
}
