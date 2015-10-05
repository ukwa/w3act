package models;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import play.Logger;
import play.db.ebean.Model;
import scala.NotImplementedError;
import uk.bl.Const;

import uk.bl.api.Utils;
import uk.bl.api.models.FieldModel;

import com.avaje.ebean.Expr;
import com.avaje.ebean.ExpressionList;
import com.avaje.ebean.Page;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Taxonomy entity managed by Ebean
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="ttype")
public class Taxonomy extends ActModelMappedSuperClass { 
     
	public final static String TAXONOMY_TERM = "taxonomy_term";
    public static final String TAXONOMY_PARENTS  	 = "taxonomy_parents"; 
    public static final String TAXONOMY_PARENTS_ALL  = "taxonomy_parents_all"; 

    /**
	 * MappedSuperClass "superclass" is needed as "Properties from superclasses not mapped as @MappedSuperclass are ignored"
	 */
	private static final long serialVersionUID = -8987367110038045775L;

	@JsonIgnore
	@ManyToMany
	@JoinTable(name = "taxonomy_user", joinColumns = { @JoinColumn(name = "taxonomy_id", referencedColumnName="id") },
	inverseJoinColumns = { @JoinColumn(name = "user_id", referencedColumnName="id") }) 
	public List<User> ownerUsers;

	@JsonIgnore
	@ManyToOne(optional=true)
	@JoinColumn(name = "taxonomyType_id")
	public TaxonomyType taxonomyType;
	
	@Column(insertable=false, updatable=false)
	public String ttype;
	 
	@JsonIgnore
    @ManyToOne(cascade={CascadeType.ALL})
	@JoinColumn(name = "parent_id")
	public Taxonomy parent;
	
    @OneToMany(cascade=CascadeType.ALL, mappedBy="parent")
	public List<Taxonomy> children;

	@JsonIgnore
	@ManyToMany
	@JoinTable(name = TAXONOMY_PARENTS_ALL, joinColumns = { @JoinColumn(name = "taxonomy_id", referencedColumnName="id") },
	inverseJoinColumns = { @JoinColumn(name = "parent_id", referencedColumnName="id") }) 
	public List<Taxonomy> parentsAllList;
	
	@Column(name="start_date")
	public Date startDate;
	
	@Column(name="end_date")
	public Date endDate;
	
	@Transient
	public String startDateText;

	@Transient
	public String endDateText;
	
    @OneToMany(mappedBy="taxonomy")
    public List<Highlight> highlights;

	// Used during import:
    @Transient
    public String parentName;
	
    public Taxonomy() {}

    public Taxonomy(String name) {
        this.name = name;
    }
    
    public Taxonomy(String name, String description) {
        this(name);
        this.description = description;
    }
    
    // -- Queries
    
	public static Model.Finder<Long,Taxonomy> findTaxonomy = new Model.Finder<>(Long.class, Taxonomy.class);
    
    /**
     * Retrieve Taxonomy for user
     */
    public static List<Taxonomy> findInvolving() {
        return findTaxonomy.all();
    }
    
    /**
     * Retrieve all Taxonomies
     */
    public static List<Taxonomy> findAll() {
        return findTaxonomy.all();
    }
    
    /**
     * Retrieve an object by Id (tid).
     * @param nid
     * @return object 
     */
    public static Taxonomy findById(Long id) {
    	Taxonomy res = findTaxonomy.where().eq(Const.ID, id).findUnique();
    	return res;
    }
    
    /**
     * Retrieve a taxonomy by title.
     * @param title
     * @return taxonomy object
     */
    public static Taxonomy findByName(String name) {
    	return findTaxonomy.where().eq("name", name).findUnique();
    }

    public static Taxonomy findByNameAndType(String name, String type) {
    	return findTaxonomy.where().eq("name", name).eq("ttype", type).findUnique();
    }

    /**
     * Create a new Taxonomy.
     */
    public static Taxonomy create(String name) {
        Taxonomy Taxonomy = new Taxonomy(name);
        Taxonomy.save();
        return Taxonomy;
    }

    
    /**
     * Rename a Taxonomy
     */
    public static String rename(Long TaxonomyId, String newName) {
        Taxonomy Taxonomy = (Taxonomy) findTaxonomy.ref(TaxonomyId);
        Taxonomy.name = newName;
        Taxonomy.update();
        return newName;
    }
    
    /**
     * Retrieve a taxonomy object by URL.
     * @param url
     * @return taxonomy object
     */
    public static Taxonomy findByUrlExt(String url) {
//    	Logger.debug("taxonomy findByUrl: " + url);
    	Taxonomy res = new Taxonomy();
    	if (url != null && url.length() > 0 && !url.equals(Const.NONE)) {
    		res = findTaxonomy.where().eq(Const.URL, url).findUnique();
    	} else {
    		res.name = Const.NONE;
    	}
    	return res;
    }

    /**
     * Retrieve a Taxonomy by URL.
     * @param url
     * @return taxonomy object
     */
    public static Taxonomy findByUrl(String url) {
    	Taxonomy taxonomy = findTaxonomy.where().eq(Const.URL, url).findUnique();
    	return taxonomy;
    }          
    
    /**
     * Retrieve a Taxonomy by URL.
     * @param url
     * @return taxonomy object
     */
    public static String findTaxonomyNamesByUrl(String url) {
    	String res = Const.NONE;
        
        if (url != null && url.length() > 0) {
    		if (url.contains(Const.LIST_DELIMITER)) {   	
		    	String[] parts = url.split(Const.LIST_DELIMITER);
		    	for (String part: parts)
		        {
			        Taxonomy taxonomy = findTaxonomy.where().eq(Const.URL, part).findUnique();
			        if (taxonomy != null) {
			        	if (res.equals(Const.NONE)) {
			        		res = taxonomy.name;
			        	} else {
			        		res = res + Const.LIST_DELIMITER + taxonomy.name;
			        	}
			        }
		        }
    		} else {
		        Taxonomy taxonomy = findTaxonomy.where().eq(Const.URL, url).findUnique();
		        if (taxonomy != null) {
		        	res = taxonomy.name;
		        }
    		}        	
        }
    	return res;
    }          
    
    /**
     * Retrieve an object by Id (tid).
     * @param nid
     * @return QA status name 
     */
    public static String findQAStatusById(Long id) {
    	Taxonomy taxonomy = findTaxonomy.where().eq(Const.ID, id).findUnique();
    	String res = taxonomy.name;
    	return res;
    }
    
    /**
     * Retrieve a Taxonomy by name.
     * @param QA status name
     * @return taxonomy object
     */
    public static Taxonomy findQaIssueByName(String name) {
    	Taxonomy res = new Taxonomy();
        if (name != null && name.length() > 0 && !name.contains(Const.COMMA)) {
	        Taxonomy res2 = findTaxonomy.where().eq(Const.NAME, name).findUnique();
	        if (res2 == null) {
	        	res.name = Const.NONE;
	        } else {
	        	res = res2;
	        }
//	        Logger.debug("taxonomy name: " + res.name);
        } else {
        	res.name = Const.NONE;
        }
    	return res;
    }          
    
    /**
     * Retrieve a QA status by URL.
     * @param url
     * @return QA status string
     */
    public static String findQaStatus(String url) {
//    	Logger.debug("findQaStatus url: " + url);
    	Taxonomy taxonomy = findByUrl(url);
    	String res = taxonomy.name;
//    	Logger.debug("findQaStatus taxonomy: " + taxonomy);
    	if (taxonomy.name.equals("No QA issues found (OK to publish)")) {
    		res = Const.QAStatusType.PASSED_PUBLISH_NO_ACTION_REQUIRED.name();
    	}
    	if (taxonomy.name.equals("QA issues found")) {
    		res = Const.QAStatusType.ISSUE_NOTED.name();
    	}
    	return res;
    }          
    
    /**
     * Retrieve a QA status by URL.
     * @param url
     * @return QA status string
     */
    public static String findQaStatusByName(String name) {
//    	Logger.debug("findQaStatus name: " + name);
    	Taxonomy taxonomy = findQaIssueByName(name);
    	String res = taxonomy.name;
//    	Logger.debug("findQaStatus taxonomy: " + taxonomy);
		// No QA issues found (OK to publish), QA issues found, Unknown
		// PASSED_PUBLISH_NO_ACTION_REQUIRED, ISSUE_NOTED, None
    	if (taxonomy.name.equals("No QA issues found (OK to publish)")) {
    		res = Const.QAStatusType.PASSED_PUBLISH_NO_ACTION_REQUIRED.name();
    	}
    	else if (taxonomy.name.equals("QA issues found")) {
    		res = Const.QAStatusType.ISSUE_NOTED.name();
    	}
    	else if (taxonomy.name.equals("Unknown")) {
    		res = Const.NONE_VALUE;
    	}
    	else {
    		res = Const.NONE_VALUE;
    	}
    	return res;
    }
    
    /**
     * Retrieve a QA status by Name.
     * @param QA status name
     * @return QA status string
     */
    public static String findQaStatusUrl(String name) {
    	if (name.equals(Const.QAStatusType.PASSED_PUBLISH_NO_ACTION_REQUIRED.name())) {
    		name = "No QA issues found (OK to publish)";
    	} else if (name.equals(Const.QAStatusType.ISSUE_NOTED.name())) {
    		name = "QA issues found";
    	}
    	Taxonomy taxonomy = findQaIssueByName(name);
    	String res = taxonomy.url;
    	if (res == null) {
    		res = "";
    	}
    	return res;
    }          
    
    /**
     * Get a taxonomy by URL if exists in database.
     * @param url
     * @return
     */
    public static Taxonomy getByUrl(String url) {
    	Taxonomy res = new Taxonomy();
    	if (url != null && url.length() > 0 && !url.equals(Const.NONE)) {
    		res = findTaxonomy.where().eq(Const.URL, url).findUnique();
    	} else {
    		res.name = Const.NONE;
    	}
    	return res;
    }    

    /**
     * Retrieve a Taxonomy names by URL list given as a string.
     * @param url
     * @return taxonomy object
     */
    public static String findNamesByUrls(String urls) {
    	String res = "";
    	if (urls != null) {
	    	if (urls.contains(Const.LIST_DELIMITER)) {
		    	String[] parts = urls.split(Const.LIST_DELIMITER);
		    	for (String part: parts)
		        {
		    		String name = findByUrl(part).name;
		    		res = res + name + Const.LIST_DELIMITER;
		        }
	    	} else {
	    		res = urls;    	
	    	}
    	}
    	return res;
    }          

    /**
     * Retrieve a Taxonomy list by URL.
     * @param url
     * @return taxonomy name
     */
    public static List<Taxonomy> findListByUrl(String url) {
    	List<Taxonomy> res = new ArrayList<Taxonomy>();
//        Logger.debug("taxonomy url: " + url);
    	if (url != null && url.length() > 0) {
    		if (url.contains(Const.COMMA)) {
    			List<String> resList = Arrays.asList(url.split(Const.COMMA));
    			Iterator<String> itr = resList.iterator();
    			while (itr.hasNext()) {
        			Taxonomy taxonomy = findByUrl(itr.next());
        			res.add(taxonomy);
    			}
    		} else {
    			Taxonomy taxonomy = findByUrl(url);
    			res.add(taxonomy);
    		}
        }
    	return res;
    }
        
    /**
     * Retrieve a taxonomy by name. The origin of these subjects is from the configuration file.
     * @param title
     * @return taxonomy object
     */
    public static Taxonomy findByNameConf(String name) {
    	Taxonomy res = new Taxonomy();
    	if (name != null && name.length() > 0) {
//    		Logger.debug("p1: " + name);
    		if (name.contains(Const.COMMA)) {
    			name = name.replace(Const.COMMA, Const.COMMA + " "); // in database entry with comma has additional space after comma
    		}
    		res = findTaxonomy.where()
    				.eq(Const.NAME, name)
    				.not(Expr.icontains(Const.PARENT, Const.ACT_URL))
//                Expr.icontains(Const.FIELD_URL_NODE, filter),
//                Expr.icontains(Const.TITLE, filter)
//             ))
//    				.(Const.PARENT, Const.ACT_URL)
    				.findUnique();
    	} else {
    		res.name = Const.NONE;
    	}
//		Logger.debug("res: " + res);
    	return res;
    }

    /**
     * Retrieve a taxonomy by title for the case that multiple definitions for the same
     * title were created in database e.g. one retrieved from Drupal and second from
     * configuration files.
     * @param title
     * @return taxonomy object
     */
    public static Taxonomy findByNameExt(String name) {
    	Taxonomy res = new Taxonomy();
    	if (name != null && name.length() > 0) {
//    		Logger.debug("p1: " + name);
    		if (name.contains(Const.COMMA)) {
    			name = name.replace(Const.COMMA, Const.COMMA + " "); // in database entry with comma has additional space after comma
    		}
	        ExpressionList<Taxonomy> ll = findTaxonomy.where().eq(Const.NAME, name);
	    	List<Taxonomy> taxonomyList = ll.findList(); 
	    	if (taxonomyList.size() > 0) {
	    		res = taxonomyList.get(0);
	    	} else {
	    		res.name = Const.NONE;
	    	}
    	} else {
    		res.name = Const.NONE;
    	}
//		Logger.debug("res: " + res);
    	return res;
    }

    /**
     * This method normalize database entries with comma e.g. subjects. 
     * @param name
     * @return normalized name
     */
    public static String normalizeComma(String name) {
    	String res = "";
    	if (name != null && name.length() > 0) {
    		if (name.contains(Const.COMMA)) {
    			name = name.replace(Const.COMMA, Const.COMMA + " "); // in database entry with comma has additional space after comma
    		}
    		res = name;
    	} 
        return res;
    }
    	
    /**
     * This method formats taxonomy comma for database entries e.g. subjects. 
     * @param name
     * @return normalized name
     */
    public static String formatDbComma(String name) {
    	String res = "";
    	if (name != null && name.length() > 0) {
    		if (name.contains(Const.COMMA)) {
    			name = name.replace(Const.COMMA + " ", Const.COMMA); // in database entry with comma has additional space after comma
    		}
    		res = name;
    	} 
        return res;
    }
    	
    /**
     * Retrieve a taxonomy by title.
     * @param title
     * @return taxonomy object
     */
    public static Taxonomy findByFullName(String name) {
    	Taxonomy res = new Taxonomy();
    	Logger.debug("findByFullName: " + name);
    	if (name != null && name.length() > 0) {
    		res = findTaxonomy.where().eq(Const.NAME, name).findUnique();
    	} else {
    		res.name = Const.NONE;
    	}
//		Logger.debug("res: " + res);
    	return res;
    }
    
    /**
     * Retrieve a taxonomy by name and type.
     * @param title
     * @return taxonomy object
     */
    public static Taxonomy findByFullNameExt(String name, String ttype) {
    	Taxonomy res = new Taxonomy();
//    	Logger.debug("findByFullNameExt: " + name);
    	if (name != null && name.length() > 0) {
    		res = findTaxonomy.where().eq(Const.NAME, name).eq(Const.TTYPE, ttype).findUnique();
    	} else {
    		res.name = Const.NONE;
    	}
//		Logger.debug("res: " + res);
    	return res;
    }
    
	/**
	 * This method returns taxonomy collection filtered by type e.g. license
	 * @param type The taxonomy type
	 * @return taxonomy list
	 */
	public static List<Taxonomy> findByType(String type) {
		List<Taxonomy> res = new ArrayList<Taxonomy>();
        ExpressionList<Taxonomy> ll = findTaxonomy.where().eq(Const.TTYPE, type);
    	res = ll.findList();
		return res;
	}       
        
    /**
     * Retrieve a Taxonomy list by type.
     * @param taxonomy type
     * @return taxonomy list
     */
	public static List<Taxonomy> findListByType(String type) {
    	List<Taxonomy> res = new ArrayList<Taxonomy>();
    	if (type != null && type.length() > 0) {
	        ExpressionList<Taxonomy> ll = findTaxonomy.where().eq(Const.TTYPE, type);
	    	res = ll.findList(); 
        }
    	return res;
    }
        
	/**
	 * This method retrieves subjects with parents - child level subjects.
	 * @return
	 */
	public static List<Taxonomy> getChildLevelSubjects(String url) {
		List<Taxonomy> res = new ArrayList<Taxonomy>();
        ExpressionList<Taxonomy> ll = findTaxonomy.where().eq(Const.PARENT, url);
    	res = ll.findList();
		return res;
	}       
    	
    /**
     * This method returns all taxonomies by type alphabetically sorted.
     * @return user list
     */
    public static List<Taxonomy> findListByTypeSorted(String type) {
    	List<Taxonomy> res = new ArrayList<Taxonomy>();
    	Page<Taxonomy> page = pageByType(0, findTaxonomy.all().size(), Const.NAME, Const.ASC, "", type);
    	res = page.getList();
        return res;
    }
        	
    /**
     * This method returns all taxonomies by type alphabetically sorted.
     * @return user list
     */
    public static List<Taxonomy> findListByTypeSortedAll() {
    	List<Taxonomy> res = new ArrayList<Taxonomy>();
    	Page<Taxonomy> page = pageByTypeAll(0, findTaxonomy.all().size(), Const.NAME, Const.ASC, "");
    	res = page.getList();
    	Logger.debug("findListByTypeSortedAll() subjects list size: " + res.size());
        return res;
    }
        	
    /**
     * This method returns all taxonomies by type alphabetically sorted.
     * @return user list
     */
    public static List<Taxonomy> findListByTypeSortedExt(String type, String value) {
    	List<Taxonomy> res = new ArrayList<Taxonomy>();
    	Page<Taxonomy> page = pageByTypeAndValue(0, findTaxonomy.all().size(), Const.NAME, Const.ASC, "", type, value);
    	res = page.getList();
        return res;
    }
        	
    /**
     * This method returns all taxonomies by type alphabetically sorted.
     * @return user list
     */
    public static List<Taxonomy> findListByTypeAndParentSorted(String type, String parent) {
    	List<Taxonomy> res = new ArrayList<Taxonomy>();
    	Page<Taxonomy> page = pageByTypeAndParent(0, findTaxonomy.all().size(), Const.NAME, Const.ASC, "", type, parent);
    	res = page.getList();
        return res;
    }
        	
    /**
     * Retrieve a list of the 2nd level subjects by parent name.
     * @param parent name as a string
     * @return 2nd level subjects list
     */
	public static List<Taxonomy> findSubSubjectsList(String parent) {
    	List<Taxonomy> res = new ArrayList<Taxonomy>();
    	if (parent != null && parent.length() > 0) {
//    		Logger.debug("findSubSubjectsList() parent: " + parent);
    		parent = formatDbComma(parent);
	        ExpressionList<Taxonomy> ll = findTaxonomy.where()
	        		.eq(Const.TTYPE, Const.TaxonomyType.SUBSUBJECT.name().toLowerCase())
	        		.eq(Const.PARENT, parent);
	    	res = ll.findList(); 
//	    	Logger.debug("size: " + res.size());
        }
    	return res;
    }
        
    /**
     * Retrieve a Taxonomy by parent.
     * @param taxonomy parent
     * @return taxonomy object
     */
	public static Taxonomy findByParent(String parent) {
    	Taxonomy res = null;
    	if (parent != null && parent.length() > 0) {
	        res = findTaxonomy.where().eq(Const.PARENT, parent).findUnique();
        }
    	return res;
    }
        
	/**
	 * This method retrieves subject index by name.
	 * @param subject
	 * @return subject index in selection list
	 */
	public static int getSubjectIndexByName(String subject) {
		int res = 0;
		List<Taxonomy> subjectList = findListByType(Const.SUBJECT);
		Iterator<Taxonomy> itr = subjectList.iterator();
		while (itr.hasNext()) {
			Taxonomy taxonomy = itr.next();
			if (taxonomy.name.equals(subject)) {
				break;
			}
			res++;			
		}
		return res;
	}
	
	/**
	 * This method filters taxonomies by name and returns a list 
	 * of filtered taxonomy objects.
	 * @param name
	 * @return
	 */
	public static List<Taxonomy> filterByName(String name) {
		List<Taxonomy> res = new ArrayList<Taxonomy>();
        ExpressionList<Taxonomy> ll = findTaxonomy.where().icontains(Const.NAME, name);
    	res = ll.findList();
		return res;
	}

	/**
	 * This method filters subjects by name and returns a list 
	 * of filtered subject objects.
	 * @param name
	 * @return
	 */
	public static List<Taxonomy> filterSubjectsByName(String name) {
		List<Taxonomy> res = new ArrayList<Taxonomy>();
        ExpressionList<Taxonomy> ll = findTaxonomy.where().icontains(Const.NAME, name)
    			.add(Expr.or(
    	                Expr.eq(Const.TTYPE, Const.SUBJECT),
    	                Expr.eq(Const.TTYPE, Const.SUBSUBJECT)
    	             )
    	        );
    	res = ll.findList();
		return res;
	}

    /**
     * Return a page of Taxonomy
     *
     * @param page Page to display
     * @param pageSize Number of targets per page
     * @param sortBy Target property used for sorting
     * @param order Sort order (either or asc or desc)
     * @param filter Filter applied on the name column
     */
    public static Page<Taxonomy> page(int page, int pageSize, String sortBy, String order, String filter) {

        return findTaxonomy.where().icontains(Const.NAME, filter)
        		.orderBy(sortBy + " " + order)
        		.findPagingList(pageSize)
        		.setFetchAhead(false)
        		.getPage(page);
    }    
    
    /**
     * Return a page of Taxonomy by Type
     *
     * @param page Page to display
     * @param pageSize Number of targets per page
     * @param sortBy Target property used for sorting
     * @param order Sort order (either or asc or desc)
     * @param type Taxonomy type
     * @param filter Filter applied on the name column
     */
    public static Page<Taxonomy> pageByType(int page, int pageSize, String sortBy, String order, 
    		String filter, String type) {

        return findTaxonomy.where().icontains(Const.NAME, filter)
        		.eq(Const.TTYPE, type)
        		.orderBy(sortBy + " " + order)
        		.findPagingList(pageSize)
        		.setFetchAhead(false)
        		.getPage(page);
    }    
    
    /**
     * Return a page of Taxonomy by Type
     *
     * @param page Page to display
     * @param pageSize Number of targets per page
     * @param sortBy Target property used for sorting
     * @param order Sort order (either or asc or desc)
     * @param type Taxonomy type
     * @param filter Filter applied on the name column
     */
    public static Page<Taxonomy> pageByTypeAll(int page, int pageSize, String sortBy, String order, 
    		String filter) {

        return findTaxonomy.where()
        		.icontains(Const.NAME, filter)
	        	.add(Expr.or(
		                Expr.icontains(Const.TTYPE, Const.SUBJECT),
		                Expr.icontains(Const.TTYPE, Const.SUBSUBJECT)
	        		))
	        	.add(Expr.or(
		                Expr.isNull(Const.PARENT),
		                Expr.not(Expr.icontains(Const.PARENT, Const.ACT_URL))
	        		))
        		.orderBy(sortBy + " " + order)
        		.findPagingList(pageSize)
        		.setFetchAhead(false)
        		.getPage(page);
    }    
    
    /**
     * Return a page of Taxonomy by Type
     *
     * @param page Page to display
     * @param pageSize Number of targets per page
     * @param sortBy Target property used for sorting
     * @param order Sort order (either or asc or desc)
     * @param type Taxonomy type
     * @param filter Filter applied on the name column
     */
    public static Page<Taxonomy> pageByTypeAndValue(int page, int pageSize, String sortBy, String order, 
    		String filter, String type, String value) {
//		.add(Expr.or(
//                Expr.icontains(Const.FIELD_URL_NODE, filter),
//                Expr.icontains(Const.TITLE, filter)
//             ))

        return findTaxonomy.where()
        		.icontains(Const.NAME, filter)
        		.eq(Const.TTYPE, type)
        		.orderBy(sortBy + " " + order)
        		.findPagingList(pageSize)
        		.setFetchAhead(false)
        		.getPage(page);
    }    
    
    /**
     * Return a page of Taxonomy by Type
     *
     * @param page Page to display
     * @param pageSize Number of targets per page
     * @param sortBy Target property used for sorting
     * @param order Sort order (either or asc or desc)
     * @param type Taxonomy type
     * @param filter Filter applied on the name column
     */
    public static Page<Taxonomy> pageByTypeAndParent(int page, int pageSize, String sortBy, String order, 
    		String filter, String type, String parent) {

        return findTaxonomy.where().icontains(Const.NAME, filter)
        		.eq(Const.TTYPE, type)
        		.eq(Const.PARENT, parent)
        		.orderBy(sortBy + " " + order)
        		.findPagingList(pageSize)
        		.setFetchAhead(false)
        		.getPage(page);
    }    
    
    /**
     * This method calculates selected taxonomies for presentation in view page.
     * @param type The type of taxonomy
     * @param target The target object
     * @return taxonomy list as a string
     */
    public static String getSelectedSubjects(String type, Target target) {
    	String res = "";
		List<Taxonomy> taxonomyList = Taxonomy.findListByType(type);
		Iterator<Taxonomy> itr = taxonomyList.iterator();
		boolean firstTime = true;
		while (itr.hasNext()) {
			Taxonomy taxonomy = itr.next();
			if(target.hasSubject(taxonomy.url)) {
				if (firstTime) {
					res = taxonomy.name;
					firstTime = false;
				} else {
					res = res + Const.COMMA + " " + taxonomy.name;
				}
			}
		}
		if (res.length() == 0) {
			res = Const.NONE;
		}
        return res;
    }

    /**
     * This method calculates selected taxonomies for presentation in view page.
     * @param type The type of taxonomy
     * @param target The target object
     * @return taxonomy list as a string
     */
    public static String getSelectedLicenses(String type, Target target) {
    	String res = "";
		List<Taxonomy> taxonomyList = Taxonomy.findListByType(type);
		Iterator<Taxonomy> itr = taxonomyList.iterator();
		boolean firstTime = true;
		while (itr.hasNext()) {
			Taxonomy taxonomy = itr.next();
			if(target.hasLicense(taxonomy.url)) {
				if (firstTime) {
					res = taxonomy.name;
					firstTime = false;
				} else {
					res = res + Const.COMMA + " " + taxonomy.name;
				}
			}
		}
		if (res.length() == 0) {
			res = Const.NONE;
		}
        return res;
    }

    /**
     * This method calculates selected taxonomies for presentation in view page.
     * @param type The type of taxonomy
     * @param targetName The instance object
     * @return taxonomy list as a string
     */
    public static String getSelectedSubjectsByInstance(String type, Instance instance) {
    	String res = "";
		List<Taxonomy> taxonomyList = Taxonomy.findListByType(type);
		Iterator<Taxonomy> itr = taxonomyList.iterator();
		boolean firstTime = true;
		while (itr.hasNext()) {
			Taxonomy taxonomy = itr.next();
//			if(instance.hasSubject(taxonomy.url)) {
//				if (firstTime) {
//					res = taxonomy.name;
//					firstTime = false;
//				} else {
//					res = res + Const.COMMA + " " + taxonomy.name;
//				}
//			}
		}
		if (res.length() == 0) {
			res = Const.NONE;
		}
        return res;
    }

    /**
     * This method calculates selected taxonomies in second level for presentation in view page.
     * @param type The type of taxonomy
     * @param target The target object
     * @return taxonomy list as a string
     */
    public static String getSelectedSubjectsSecondLevel(String type, Target target) {
    	String res = "";
		boolean firstTime = true;
		List<Taxonomy> taxonomyList = Taxonomy.findListByType(type);
		Iterator<Taxonomy> itr = taxonomyList.iterator();
		while (itr.hasNext()) {
			Taxonomy taxonomy = itr.next();
			List<Taxonomy> subTaxonomyList = Taxonomy.findSubSubjectsList(taxonomy.name);
			Iterator<Taxonomy> itrSub = subTaxonomyList.iterator();
			while (itrSub.hasNext()) {
				Taxonomy subTaxonomy = itrSub.next();
				if(target.hasSubSubject(subTaxonomy.url)) {
					if (firstTime) {
						res = subTaxonomy.name;
						firstTime = false;
					} else {
						res = res + Const.COMMA + " " + subTaxonomy.name;
					}
				}
			}
		}
		if (res.length() == 0) {
			res = Const.NONE;
		}
        return res;
    }

    /**
     * This method calculates selected taxonomies in second level for presentation in view page.
     * @param type The type of taxonomy
     * @param targetName The target object
     * @return taxonomy list as a string
     */
    public static String getSelectedSubjectsList(String type, String targetUrl) {
    	String res = "";
		boolean firstTime = true;
		Target target = Target.findByUrl(targetUrl);
		if (target != null) {
			List<Taxonomy> taxonomyList = Taxonomy.findListByType(type);
			Iterator<Taxonomy> itr = taxonomyList.iterator();
			while (itr.hasNext()) {
				Taxonomy taxonomy = itr.next();
				List<Taxonomy> subTaxonomyList = Taxonomy.findSubSubjectsList(taxonomy.name);
				Iterator<Taxonomy> itrSub = subTaxonomyList.iterator();
				while (itrSub.hasNext()) {
					Taxonomy subTaxonomy = itrSub.next();
					if(target.hasSubSubject(subTaxonomy.url)) {
						if (firstTime) {
							res = subTaxonomy.name;
							firstTime = false;
						} else {
							res = res + Const.COMMA + " " + subTaxonomy.name;
						}
					}
				}
			}
		}
		if (res.length() == 0) {
			res = Const.NONE;
		}
        return res;
    }

    /**
     * This method calculates selected taxonomies in second level for presentation in view page.
     * @param type The type of taxonomy
     * @param targetName The instance object
     * @return taxonomy list as a string
     */
    public static String getSelectedSubjectsByInstanceSecondLevel(String type, Instance instance) {
    	String res = "";
		boolean firstTime = true;
		List<Taxonomy> taxonomyList = Taxonomy.findListByType(type);
		Iterator<Taxonomy> itr = taxonomyList.iterator();
		while (itr.hasNext()) {
			Taxonomy taxonomy = itr.next();
			List<Taxonomy> subTaxonomyList = Taxonomy.findSubSubjectsList(taxonomy.name);
			Iterator<Taxonomy> itrSub = subTaxonomyList.iterator();
			while (itrSub.hasNext()) {
				Taxonomy subTaxonomy = itrSub.next();
//				if(instance.hasSubSubject(subTaxonomy.url)) {
//					if (firstTime) {
//						res = subTaxonomy.name;
//						firstTime = false;
//					} else {
//						res = res + Const.COMMA + " " + subTaxonomy.name;
//					}
//				}
			}
		}
		if (res.length() == 0) {
			res = Const.NONE;
		}
        return res;
    }
    
	/**
	 * This method retrieves selected subjects from target object.
	 * @param targetUrl
	 * @return
	 */
	public static List<Taxonomy> getSelectedSubjects(String targetUrl) {
//		Logger.debug("getSelectedSubjects() targetUrl: " + targetUrl);
//		List<Taxonomy> res = new ArrayList<Taxonomy>();
//    	if (targetUrl != null && targetUrl.length() > 0) {
//    		Target target = Target.findByUrl(targetUrl);
//    		if (target.fieldSubject != null) {
////    			Logger.debug("getSelectedSubjects() field_subject: " + target.field_subject);
//		    	String[] parts = target.fieldSubject.split(Const.COMMA + " ");
//		    	for (String part: parts) {
////		    		Logger.debug("part: " + part);
//		    		Taxonomy subject = findByUrl(part);
//		    		if (subject != null && subject.name != null && subject.name.length() > 0) {
////			    		Logger.debug("subject name: " + subject.name);
//		    			res.add(subject);
//		    		}
//		    	}
//    		}
//    	}
//		return res;
		throw new NotImplementedError();
	}       
    
	/**
	 * This method retrieves selected subjects from target object.
	 * @param targetUrl
	 * @return
	 */
	public static List<Taxonomy> getSelectedSubjectsForInstance(String targetUrl) {
//		Logger.debug("getSelectedSubjects() targetUrl: " + targetUrl);
		List<Taxonomy> res = new ArrayList<Taxonomy>();
    	if (targetUrl != null && targetUrl.length() > 0) {
    		Instance target = Instance.findByUrl(targetUrl);
//    		if (target.fieldSubject != null) {
////    			Logger.debug("getSelectedSubjects() field_subject: " + target.field_subject);
//		    	String[] parts = target.fieldSubject.split(Const.COMMA + " ");
//		    	for (String part: parts) {
////		    		Logger.debug("part: " + part);
//		    		Taxonomy subject = findByUrl(part);
//		    		if (subject != null && subject.name != null && subject.name.length() > 0) {
////			    		Logger.debug("subject name: " + subject.name);
//		    			res.add(subject);
//		    		}
//		    	}
//    		}
    	}
		return res;
	}       
    
	/**
	 * This method retrieves selected licenses from target object.
	 * @param targetUrl
	 * @return
	 */
	public static List<Taxonomy> getSelectedLicenses(String targetUrl) {
//		Logger.debug("getSelectedLicenses() targetUrl: " + targetUrl);
//		List<Taxonomy> res = new ArrayList<Taxonomy>();
//    	if (targetUrl != null && targetUrl.length() > 0) {
//    		Target target = Target.findByUrl(targetUrl);
//    		if (target.fieldLicense != null) {
////    			Logger.debug("getSelectedLicenses() field_license: " + target.field_license);
//		    	String[] parts = target.fieldLicense.split(Const.COMMA + " ");
//		    	for (String part: parts) {
////		    		Logger.debug("part: " + part);
//		    		Taxonomy license = findByUrl(part);
//		    		if (license != null && license.name != null && license.name.length() > 0) {
////			    		Logger.debug("license name: " + license.name);
//		    			res.add(license);
//		    		}
//		    	}
//    		}
//    	}
//		return res;
		throw new NotImplementedError();
	}       
    
	/**
	 * This method retrieves selected subjects from instance object.
	 * @param targetUrl
	 * @return
	 */
	public static List<Taxonomy> getSelectedSubjectsByInstance(String targetUrl) {
//		Logger.debug("getSelectedSubjectsByInstance() targetUrl: " + targetUrl);
		List<Taxonomy> res = new ArrayList<Taxonomy>();
    	if (targetUrl != null && targetUrl.length() > 0) {
    		Instance target = Instance.findByUrl(targetUrl);
//    		if (target.fieldSubject != null) {
////    			Logger.debug("getSelectedSubjectsByInstance() field_subject: " + target.field_subject);
//		    	String[] parts = target.fieldSubject.split(Const.COMMA + " ");
//		    	for (String part: parts) {
////		    		Logger.debug("part: " + part);
//		    		Taxonomy subject = findByUrl(part);
//		    		if (subject != null && subject.name != null && subject.name.length() > 0) {
////			    		Logger.debug("subject name: " + subject.name);
//		    			res.add(subject);
//		    		}
//		    	}
//    		}
    	}
		return res;
	}       
    
	/**
	 * This method presents subjects list for view page.
	 * @param list
	 * @return presentation string
	 */
	public static String getSubjectsAsString(List<Taxonomy> list) {
    	String res = "";
//		Logger.debug("getSubjectsAsString() list size: " + list.size());
		Iterator<Taxonomy> itr = list.iterator();
		boolean firstTime = true;
		while (itr.hasNext()) {
			Taxonomy subject = itr.next();
			if (firstTime) {
//				Logger.debug("add first subject.name: " + subject.name);
				res = subject.name;
				firstTime = false;
			} else {
//				Logger.debug("add subject.name: " + subject.name);
				res = res + Const.COMMA + " " + subject.name;
			}
		}
		if (res.length() == 0) {
			res = Const.NONE;
		}
        return res;
	}      
    	
    public static Taxonomy findByTypeAndUrl(String type, String url) {
        Taxonomy taxonomy = findTaxonomy.where().eq(Const.TTYPE, type).eq(Const.URL, url).findUnique();
    	return taxonomy;
    }

	public List<User> getOwnerUsers() {
		return ownerUsers;
	}

	public void setOwnerUsers(List<User> ownerUsers) {
		this.ownerUsers = ownerUsers;
	}

	public TaxonomyType getTaxonomyType() {
		return taxonomyType;
	}

	public void setTaxonomyType(TaxonomyType taxonomyType) {
		this.taxonomyType = taxonomyType;
	}

	public String getTtype() {
		return ttype;
	}

	public void setTtype(String ttype) {
		this.ttype = ttype;
	}
	
	public String getStartDateText() {
		if (startDate != null) {
			startDateText = Utils.INSTANCE.convertToDateString(startDate);
		}else{
			
			startDateText = "";
		}
		return startDateText;
	}
	
	public String getEndDateText() {
		if (endDate != null) {
			endDateText = Utils.INSTANCE.convertToDateString(endDate);
		}else{
			endDateText = "";
		}
		return endDateText;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		Taxonomy other = (Taxonomy) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Taxonomy [name=" + name + ", description="
				+ description + ", ttype=" + ttype + ", startDate="
				+ startDate + ", endDate="
				+ endDate + ", publish=" + publish + "]";
	}
}

