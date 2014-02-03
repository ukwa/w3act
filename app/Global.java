import play.*;
import play.libs.*;

import java.util.*;

import com.avaje.ebean.*;

import models.*;
import uk.bl.Const;
import uk.bl.api.*;

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
            }
            Ebean.save(sectionList);
	    }
	    
        @SuppressWarnings("unchecked")
		public static void insert(Application app) {
            if(Ebean.find(User.class).findRowCount() == 0) {
                try {
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
					// store organisations in DB
	                Ebean.save(allOrganisations);
	                JsonUtils.normalizeOrganisationUrlInUser();
	                Logger.info("organisations successfully loaded");
	                Logger.info("load curators ...");
	                // aggregate original curators from drupal extracting information from aggregated data
			        List<Object> allCurators = JsonUtils.extractDrupalData(Const.NodeType.USER);
					// store urls in DB
	                Ebean.save(allCurators);
	                Logger.info("curators successfully loaded");
	                Logger.info("load taxonomies ...");
	                // aggregate original taxonomies from drupal extracting information from aggregated data
			        List<Object> allTaxonomies = JsonUtils.extractDrupalData(Const.NodeType.TAXONOMY);
					// store urls in DB
	                Ebean.save(allTaxonomies);
	                Logger.info("taxonomies successfully loaded");
	                Logger.info("load taxonomy vocabularies ...");
	                // aggregate original taxonomy vocabulary from drupal extracting information from aggregated data
			        List<Object> allTaxonomyVocabularies = JsonUtils.extractDrupalData(Const.NodeType.TAXONOMY_VOCABULARY);
					// store urls in DB
	                Ebean.save(allTaxonomyVocabularies);
	                Logger.info("taxonomy vocabularies successfully loaded");
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
	                normalizeUrls();
                } catch (Exception e) {
                	Logger.info("Store error: " + e);
                }
            }
        }
        
    }
    
}