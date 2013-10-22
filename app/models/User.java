package models;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.persistence.*;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Hours;
import org.joda.time.Minutes;
import org.joda.time.Months;
import org.joda.time.Period;
import org.joda.time.Seconds;
import org.joda.time.Weeks;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

import play.Logger;
import play.db.ebean.*;
import play.data.format.*;
import play.data.validation.*;
import uk.bl.Const;

import com.avaje.ebean.*;

/**
 * User entity managed by Ebean
 */
@SuppressWarnings("serial")
@Entity 
@Table(name="creator")
public class User extends Model {

    @Id
    @Constraints.Required
    @Formats.NonEmpty
    public String email;
    
    @Constraints.Required
    public String name;
    
    @Constraints.Required
    public String password;
    
    public String field_affiliation;
    public Long uid;
    public String url;
    public String edit_url;
    public String last_access;
    public String last_login;
    public String created;
    public Long status;
    public String language;
    public Long feed_nid;
    
    // -- Queries
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public static Model.Finder<String,User> find = new Model.Finder(String.class, User.class);
    
    public User() {
    }

    public User(String name) {
    	this.name = name;
    }

    public User(String name, String email, String password) {
    	this.name = name;
    	this.email = email;
    	this.password = password;
    }
    
    /**
     * Retrieve all users.
     */
    public static List<User> findAll() {
        return find.all();
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
//    	User tmp = find.where().eq("url", url).findUnique();
//    	Logger.info("findByUrl: " + tmp.toString());
        return find.where().eq(Const.URL, url).findUnique();
    }

    /**
     * This method is used for filtering by URL.
     * @param url
     * @return
     */
    public static List<User> findFilteredByUrl(String url) {
    	List<User> ll = new ArrayList<User>();
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
    
    public String toString() {
        return "User(" + name + ")" + ", url:" + url;
    }

}

