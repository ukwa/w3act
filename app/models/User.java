package models;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

import play.Logger;
import play.data.format.Formats;
import play.data.validation.Constraints;
import play.db.ebean.Model;
import uk.bl.Const;

import com.avaje.ebean.ExpressionList;
import com.avaje.ebean.Page;
import com.fasterxml.jackson.annotation.JsonIgnore;


/**
 * User entity managed by Ebean
 */
@SuppressWarnings("serial")
@Entity 
@Table(name="creator")
public class User extends Model {

    @Id @JsonIgnore
    @Constraints.Required
    @Formats.NonEmpty
    public String email;
    
    @Constraints.Required
    public String name;
    
    @Constraints.Required
    @JsonIgnore
    public String password;
    
    @JsonIgnore
    public String field_affiliation;
    @JsonIgnore
    public Long uid;
    public String url;
    @JsonIgnore
    public String edit_url;
    @JsonIgnore
    public String last_access;
    @JsonIgnore
    public String last_login;
    @JsonIgnore
    public String created;
    @JsonIgnore
    public Long status;
    @JsonIgnore
    public String language;
    @JsonIgnore
    public Long feed_nid;
    
    // lists
    @JsonIgnore
    @Column(columnDefinition = "TEXT")
    public String roles;
    @JsonIgnore
    @Column(columnDefinition = "TEXT")
    public String revision; 

    @JsonIgnore
    @Version
    public Timestamp lastUpdate;

    // -- Queries
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public static Model.Finder<String,User> find = new Model.Finder(String.class, User.class);
    
    public User() {
    	this.revision = "";
    	this.roles = Const.DEFAULT_ROLE;
    }

    public User(String name) {
    	this.name = name;
    	this.revision = "";
    	this.roles = Const.DEFAULT_ROLE;
    }

    public User(String name, String email, String password) {
    	this.name = name;
    	this.email = email;
    	this.password = password;
    	this.revision = "";
    	this.roles = Const.DEFAULT_ROLE;
    }
    
    /**
     * This method checks if this User has a role passed as string parameter.
     * @param roleName
     * @return true if exists
     */
    public boolean hasRole(String roleName) {
    	boolean res = false;
//    	Logger.info("hasRole: " + roleName);
    	if (roleName != null && roleName.length() > 0 
    			&& roles.contains(roleName)) {
    		res = true;
    	}
    	return res;
    }
    
    public List<? extends Role> getRoles()
    {
    	List<Role> res = new ArrayList<Role>();
		List<String> resList = Arrays.asList(roles.split(Const.COMMA));
		Iterator<String> itr = resList.iterator();
		while (itr.hasNext()) {
			res.add(Role.findByName(itr.next()));
		}
        return res;
    }

    /**
     * Retrieve all users.
     */
    public static List<User> findAll() {
        return find.all();
    }

    /**
     * This method returns all users related to given organisation.
     * @param organisation The organisation URL
     * @return user list
     */
    public static List<User> findAllSorted() {
    	List<User> res = new ArrayList<User>();
    	Page<User> page = page(0, find.all().size(), Const.NAME, Const.ASC, "");
//    	find.where()
//    			.orderBy("title asc")
//    			.findPagingList(find.all().size())
//    			.setFetchAhead(false)
//    			.getPage(0);
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
    		long timestampCreated = Long.valueOf(created);
    		Date dateCreated = new Date(timestampCreated * 1000);
    		long timestampLastAccess = Long.valueOf(last_access);
    		Date dateLastAccess = new Date(timestampLastAccess * 1000);
			Logger.info("date created: " + dateCreated);
			Logger.info("date last access: " + dateLastAccess);
			 
			DateTime dt1 = new DateTime(dateCreated);
			DateTime dt2 = new DateTime(dateLastAccess);
	 
//			Logger.info(Months.monthsBetween(dt1, dt2).getMonths() + " months, ");
//			Logger.info(Weeks.weeksBetween(dt1, dt2).getWeeks() + " weeks, ");			
//			Logger.info(Weeks.weeksBetween(dt1, dt2).toPeriod() + " period, ");
//			Logger.info(Days.daysBetween(dt1, dt2).getDays() + " days, ");
//			Logger.info(Hours.hoursBetween(dt1, dt2).getHours() % 24 + " hours, ");
//			Logger.info(Minutes.minutesBetween(dt1, dt2).getMinutes() % 60 + " minutes, ");
//			Logger.info(Seconds.secondsBetween(dt1, dt2).getSeconds() % 60 + " seconds.");
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
    
    public String toString() {
        return "User(" + name + ")" + ", url:" + url;
    }
    
    // Could really do with many_to_one relationship
    public Organisation getOrganisation() {
    	return Organisation.findByUrl(field_affiliation);
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
}

