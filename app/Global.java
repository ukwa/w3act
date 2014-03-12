import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import models.ContactPerson;
import models.Flag;
import models.MailTemplate;
import models.Organisation;
import models.Permission;
import models.Role;
import models.Tag;
import models.Target;
import models.Taxonomy;
import models.User;
import play.Application;
import play.GlobalSettings;
import play.Logger;
import play.libs.Yaml;
import uk.bl.Const;
import uk.bl.api.JsonUtils;
import uk.bl.api.PasswordHash;
import uk.bl.api.Utils;

import com.avaje.ebean.Ebean;

import controllers.Organisations;

public class Global extends GlobalSettings {
    
    public void onStart(Application app) {
        InitialData.insert(app);
    }
    
    static class InitialData {
        
    	/**
    	 * normalize URL if there is "_" e.g. in taxonomy_term
    	 */
    	public static void normalizeUrls() {
            List<Target> targets = Target.findAll();
            Iterator<Target> itr = targets.iterator();
            while (itr.hasNext()) {
            	Target target = itr.next();
				if (target.field_collection_categories != null && target.field_collection_categories.contains("_")) {
					target.field_collection_categories = target.field_collection_categories.replace("_", "/");
				}
				if (target.field_license != null && target.field_license.contains("_")) {
					target.field_license = target.field_license.replace("_", "/");
				}
	            Ebean.update(target);
			}
    	}
    	
	    /**
	     * This method adds different section elements described in initial-data file.
	     * @param sectionName
	     * @param cls The current object type
	     * @param all The whole data
	     */
	    private static void insertInitialData(String sectionName, Class<?> cls, Map<String,List<Object>> all) {
            List<Object> sectionList = all.get(sectionName);
            Iterator<Object> sectionItr = sectionList.iterator();
            while (sectionItr.hasNext()) {
    	        if (cls == User.class) {
	            	User user = (User) sectionItr.next();
	            	user.uid = Utils.createId();
	            	user.url = Const.ACT_URL + user.uid;
	        		try {
						user.password = PasswordHash.createHash(user.password);
						Logger.info("hash password: " + user.password);
					} catch (NoSuchAlgorithmException e) {
						Logger.info("initial password creation - no algorithm error: " + e);
					} catch (InvalidKeySpecException e) {
						Logger.info("initial password creation - key specification error: " + e);
					}
	                Logger.info("Predefined " + User.class.getSimpleName() + ": " + user.toString());
    	        }
    	        if (cls == Role.class) {
                	Role role = (Role) sectionItr.next();
                	role.id = Utils.createId();
    	        	role.url= Const.ACT_URL + role.id;
	                Logger.info("Predefined " + Role.class.getSimpleName() + ": " + role.toString());
    	        }
    	        if (cls == Permission.class) {
    	        	Permission permission = (Permission) sectionItr.next();
    	        	permission.id = Utils.createId();
    	        	permission.url= Const.ACT_URL + permission.id;
	                Logger.info("Predefined " + Permission.class.getSimpleName() + ": " + permission.toString());
    	        }
    	        if (cls == Organisation.class) {
    	        	Organisation organisation = (Organisation) sectionItr.next();
    	        	organisation.nid = Utils.createId();
    	        	organisation.url= Const.ACT_URL + organisation.nid;
	                Logger.info("Predefined " + Organisation.class.getSimpleName() + ": " + organisation.toString());
    	        }
    	        if (cls == MailTemplate.class) {
    	        	MailTemplate mailTemplate = (MailTemplate) sectionItr.next();
    	        	mailTemplate.id = Utils.createId();
    	        	mailTemplate.url = Const.ACT_URL + mailTemplate.id;
	                Logger.info("Predefined " + MailTemplate.class.getSimpleName() + ": " + mailTemplate.toString());
    	        }
    	        if (cls == ContactPerson.class) {
    	        	ContactPerson contactPerson = (ContactPerson) sectionItr.next();
    	        	contactPerson.id = Utils.createId();
    	        	contactPerson.url = Const.ACT_URL + contactPerson.id;
	                Logger.info("Predefined " + ContactPerson.class.getSimpleName() + ": " + contactPerson.toString());
    	        }
    	        if (cls == Tag.class) {
    	        	Tag tag = (Tag) sectionItr.next();
    	        	tag.id = Utils.createId();
    	        	tag.url = Const.ACT_URL + tag.id;
	                Logger.info("Predefined " + Tag.class.getSimpleName() + ": " + tag.toString());
    	        }
    	        if (cls == Flag.class) {
    	        	Flag flag = (Flag) sectionItr.next();
    	        	flag.id = Utils.createId();
    	        	flag.url = Const.ACT_URL + flag.id;
	                Logger.info("Predefined " + Flag.class.getSimpleName() + ": " + flag.toString());
    	        }
    	        if (cls == Taxonomy.class) {
    	        	Taxonomy taxonomy = (Taxonomy) sectionItr.next();
    	        	taxonomy.tid = Utils.createId();
    	        	taxonomy.url = Const.ACT_URL + taxonomy.tid;
	                Logger.info("Predefined " + Taxonomy.class.getSimpleName() + ": " + taxonomy.toString());
    	        }
            }
            Ebean.save(sectionList);
	    }
	    
        @SuppressWarnings("unchecked")
		public static void insert(Application app) {
            if(Ebean.find(User.class).findRowCount() == 0) {
                try {
	                Logger.info("loading taxonomies from configuration ...");
	                Map<String,List<Object>> alltaxonomies = (Map<String,List<Object>>)Yaml.load("taxonomies.yml");
	                insertInitialData(Const.TAXONOMIES, Taxonomy.class, alltaxonomies);	
	                Logger.info("loading open tags from configuration ...");
	                Map<String,List<Object>> alltags = (Map<String,List<Object>>)Yaml.load("tags.yml");
	                insertInitialData(Const.TAGS, Tag.class, alltags);	
	                Logger.info("loading flags from configuration ...");
	                Map<String,List<Object>> allflags = (Map<String,List<Object>>)Yaml.load("flags.yml");
	                insertInitialData(Const.FLAGS, Flag.class, allflags);	
	                Logger.info("loading e-mail templates from configuration ...");
	                Map<String,List<Object>> alltemplates = (Map<String,List<Object>>)Yaml.load("templates.yml");
	                insertInitialData(Const.MAILTEMPLATES, MailTemplate.class, alltemplates);	
	                Logger.info("loading contact persons from configuration ...");
	                Map<String,List<Object>> allContactPersons = (Map<String,List<Object>>)Yaml.load("contact-persons.yml");
	                insertInitialData(Const.CONTACTPERSONS, ContactPerson.class, allContactPersons);	
	                Logger.info("loading users from configuration ...");
	                Map<String,List<Object>> allusers = (Map<String,List<Object>>)Yaml.load("users.yml");
	                insertInitialData(Const.USERS, User.class, allusers);	
	                Map<String,List<Object>> all = (Map<String,List<Object>>)Yaml.load("initial-data.yml");
	                insertInitialData(Const.ROLES, Role.class, all);	
	                insertInitialData(Const.PERMISSIONS, Permission.class, all);	
	                insertInitialData(Const.ORGANISATIONS, Organisation.class, all);

	                Logger.info("test load curators ...");
			        List<Object> allCurators = JsonUtils.getDrupalDataBase(Const.NodeType.USER);
					// store curators in DB
	                Ebean.save(allCurators);
	                Logger.info("test curators successfully loaded");
	                Logger.info("load urls");
					// aggregate url data from drupal and store JSON content in a file
			        List<Object> allUrls = JsonUtils.getDrupalData(Const.NodeType.URL);
					// store urls in DB
	                Ebean.save(allUrls);
	                Logger.info("targets successfully loaded");
	//                List<Target> targetList = (List<Target>) Target.find.all();
	//                Iterator<Target> targetItr = targetList.iterator();
	//                while (targetItr.hasNext()) {
	//                	Target target = targetItr.next();
	//                    Logger.info("Target test object: " + target.toString());
	//                }
	                Logger.info("load organisations ...");
					// aggregate organisations data from drupal and store JSON content in a file
			        List<Object> allOrganisations = JsonUtils.getDrupalData(Const.NodeType.ORGANISATION);
			        List<Object> allSingleOrganisations = Organisations.skipExistingObjects(allOrganisations);
					// store organisations in DB
	                Ebean.save(allSingleOrganisations);
	                JsonUtils.normalizeOrganisationUrlInUser();
	                Logger.info("organisations successfully loaded");
	                Logger.info("load taxonomies ...");
	                // aggregate original taxonomies from drupal extracting information from aggregated data
			        List<Object> allTaxonomies = JsonUtils.extractDrupalData(Const.NodeType.TAXONOMY);
					// store urls in DB
	                Ebean.save(allTaxonomies);
	                Logger.info("taxonomies successfully loaded");
	                // due to merging of different original object models the resulting 
	                // collection set is evaluated from particular taxonomy type
	                Logger.info("load collections ..."); 
			        List<Object> allCollections = JsonUtils.readCollectionsFromTaxonomies();
					// store collections in DB
	                Ebean.save(allCollections);
	                Logger.info("collections successfully loaded");
	                Logger.info("load instances");
					// aggregate instances data from drupal and store JSON content in a file
			        List<Object> allInstances = JsonUtils.getDrupalData(Const.NodeType.INSTANCE);
					// store instances in DB
	                Ebean.save(allInstances);
	                Logger.info("instances successfully loaded");
	                JsonUtils.mapInstancesToTargets();
	                Logger.info("map instances to targets");
	                JsonUtils.getDomainForTargets();
	                Logger.info("Target domains extracted");
	                normalizeUrls();
                } catch (Exception e) {
                	Logger.info("Store error: " + e);
                }
            }
        }
        
    }
    
}