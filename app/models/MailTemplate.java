package models;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;
import uk.bl.Const;
import uk.bl.api.Utils;

import com.avaje.ebean.ExpressionList;

/**
 * This class supports the management of e-mail templates.
 */
@Entity
@Table(name = "mail_template")
public class MailTemplate extends ActModel {

	/**
	 * file id
	 */
	private static final long serialVersionUID = -2157694575463302989L;

    //bi-directional one-to-many association to CrawlPermission
    @OneToMany(mappedBy="mailTemplate", cascade=CascadeType.ALL)
    public List<CrawlPermission> crawlPermissions;
     
    /**
     * The name of the e-mail.
     */
    @Column(columnDefinition = "text")
    @Required(message="Name is required")
    public String name;
       
    /**
     * E-mail type: Permission Request, Thank you - Online Permission Form, 
     * Thank you - Online Nomination by Owner, Opt out.
     */
    @Column(columnDefinition = "text")
    public String ttype;
    
    /**
     * E-mail subject.
     */
    @Column(columnDefinition = "text")
    public String subject;

    /**
     * E-mail from field.
     */
    @Column(columnDefinition = "text")
    @Required(message="From email is required")
    public String fromEmail;

    /**
     * The place holders in E-mail that should be rewritten by user.
     */
    @Column(columnDefinition = "text")
    public String placeHolders;

    /**
     * This is a checkbox defining whether this e-mail
     * should be regarded as a default mail.
     */
    public Boolean defaultEmail;
    
    /**
     * Either text as a string or name of the associated text file.
     */
    @Column(columnDefinition = "text")
    public String text;

    public static final Model.Finder<Long, MailTemplate> find = new Model.Finder<Long, MailTemplate>(Long.class, MailTemplate.class);

    
    public MailTemplate() {
    	super();
    }

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
     * Retrieve an object by Id (id).
     * @param nid
     * @return object 
     */
    public static MailTemplate findById(Long id) {
    	MailTemplate res = find.where().eq(Const.ID, id).findUnique();
    	return res;
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
    	return Utils.INSTANCE.readTextFile(Const.TEMPLATES_PATH + text);
    }
    
    /**
     * This method reads initial data from configuration files of the mail templates
     * @return
     */
    public String readInitialTemplate() {
    	text = Utils.INSTANCE.readTextFile(Const.TEMPLATES_PATH + text);
    	return text;
    }
    
    public String toString() {
        return "MailTemplate(" + name + ")" + ", id:" + id;
    }
    
    public static Map<String,String> options() {
        LinkedHashMap<String,String> options = new LinkedHashMap<String,String>();
        for(MailTemplate c: find.all()) {
            options.put(c.id.toString(), c.name);
        }
        return options;
    }   
}