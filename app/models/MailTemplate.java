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
import com.avaje.ebean.Page;

import uk.bl.api.Utils;

/**
 * This class supports the management of e-mail templates.
 */
@Entity
public class MailTemplate extends Model
{

	/**
	 * file id
	 */
	private static final long serialVersionUID = -2157694575463302989L;

	@Id 
    public Long id;

    /**
     * This field with prefix "act-" builds an unique identifier in W3ACT database.
     */
    @Column(columnDefinition = "TEXT")
    public String url;
	
    /**
     * The name of the e-mail.
     */
    @Column(columnDefinition = "TEXT")
    public String name;
       
    /**
     * E-mail type: Permission Request, Thank you - Online Permission Form, 
     * Thank you - Online Nomination by Owner, Opt out.
     */
    @Column(columnDefinition = "TEXT")
    public String ttype;
    
    /**
     * E-mail subject.
     */
    @Column(columnDefinition = "TEXT")
    public String subject;

    /**
     * E-mail from field.
     */
    @Column(columnDefinition = "TEXT")
    public String fromEmail;

    /**
     * The place holders in E-mail that should be rewritten by user.
     */
    @Column(columnDefinition = "TEXT")
    public String placeHolders;

    /**
     * This is a checkbox defining whether this e-mail
     * should be regarded as a default mail.
     */
    public Boolean defaultEmail;
    
    /**
     * Either text as a string or name of the associated text file.
     */
    @Column(columnDefinition = "TEXT")
    public String text;

    @Version
    public Timestamp lastUpdate;

    public static final Model.Finder<Long, MailTemplate> find = new Model.Finder<Long, MailTemplate>(Long.class, MailTemplate.class);

    public String getName()
    {
        return name;
    }

    public static MailTemplate findByName(String name)
    {
        return find.where()
                   .eq("name",
                       name)
                   .findUnique();
    }
    
    /**
     * Retrieve a mail template by URL.
     * @param url
     * @return mail template name
     */
    public static MailTemplate findByUrl(String url) {
    	MailTemplate res = new MailTemplate();
    	if (url != null && url.length() > 0 && !url.equals(Const.NONE)) {
    		res = find.where().eq(Const.URL, url).findUnique();
    	} else {
    		res.name = Const.NONE;
    	}
    	return res;
    }
    
    /**
	 * This method filters templates by name and returns a list 
	 * of filtered Template objects.
	 * @param name
	 * @return
	 */
	public static List<MailTemplate> filterByName(String name) {
		List<MailTemplate> res = new ArrayList<MailTemplate>();
        ExpressionList<MailTemplate> ll = find.where().icontains(Const.NAME, name);
    	res = ll.findList();
		return res;
	}
        
    /**
     * Retrieve all templates.
     */
    public static List<MailTemplate> findAll() {
        return find.all();
    }
    
    /**
     * This method returns a list of all status values for crawl permission record.
     * @return
     */
    public static List<String> getAllTypes() {
    	List<String> res = new ArrayList<String>();
	    Const.MailTemplateType[] resArray = Const.MailTemplateType.values();
	    for (int i=0; i < resArray.length; i++) {
		    res.add(resArray[i].name());
	    }
	    return res;
    }    
    
    /**
     * This file retrieves template text.
     * @return
     */
    public String readTemplate() {
    	return Utils.readTextFile(Const.TEMPLATES_PATH + text);
    }
    
    public String toString() {
        return "MailTemplate(" + name + ")" + ", id:" + id;
    }
}