package uk.bl.db;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Date;
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

import com.avaje.ebean.Ebean;

import play.Logger;
import play.libs.Yaml;
import uk.bl.Const;
import uk.bl.api.JsonUtils;
import uk.bl.api.PasswordHash;

public enum DataImport {

	INSTANCE;

	public void insert() {
        try {

			if (Ebean.find(User.class).findRowCount() == 0) {
	        	this.importPermissions();
	        	this.importRoles();
	        	this.importJsonOrganisations();
	        	this.importOrganisations();
	        	this.importCurators();
	            this.importAccounts();
	        }
			if (Ebean.find(Tag.class).findRowCount() == 0) {
	        	this.importTags();
			}
			if (Ebean.find(Flag.class).findRowCount() == 0) {
	        	this.importFlags();
			}
			if (Ebean.find(MailTemplate.class).findRowCount() == 0) {
	        	this.importMailTemplates();
			}
			if (Ebean.find(ContactPerson.class).findRowCount() == 0) {
	        	this.importContactPersons();
			}
			if (Ebean.find(Target.class).findRowCount() == 0) {
	        	this.importUrlsToTargets();
			}
			if (Ebean.find(Taxonomy.class).findRowCount() == 0) {
				this.importTaxonomies();
			}
	        	
//			if (Ebean.find(Collection.class).findRowCount() == 0) {
//	        	this.importCollections();
//			}
//				// aggregate url data from drupal and store JSON content in a file
//		        List<Object> allUrls = JsonUtils.getDrupalData(Const.NodeType.URL);
//				// store urls in DB
//                Ebean.save(allUrls);
//                Logger.info("targets successfully loaded");

                
                
                ////                List<Target> targetList = (List<Target>) Target.find.all();
////                Iterator<Target> targetItr = targetList.iterator();
////                while (targetItr.hasNext()) {
////                	Target target = targetItr.next();
//////                    Logger.info("Target test object: " + target.toString());
////					if (target.field_subject == null
////							|| target.field_subject.length() == 0) {
////						target.field_subject = Const.NONE;
////						Ebean.update(target);
////					}
////                }
//                Logger.info("load organisations ...");
//				// aggregate organisations data from drupal and store JSON content in a file
//		        List<Object> allOrganisations = JsonUtils.getDrupalData(Const.NodeType.ORGANISATION);
//		        List<Object> allSingleOrganisations = Organisations.skipExistingObjects(allOrganisations);
//				// store organisations in DB
//                Ebean.save(allSingleOrganisations);
//                JsonUtils.normalizeOrganisationUrlInUser();
//                Logger.info("organisations successfully loaded");
			
//                Logger.info("load taxonomies ...");
//                // aggregate original taxonomies from drupal extracting information from aggregated data
//		        List<Object> allTaxonomies = JsonUtils.INSTANCE.extractDrupalData(Const.NodeType.TAXONOMY);
////		        List<Taxonomy> cleanedTaxonomies = cleanUpTaxonomies(allTaxonomies);
//				// store taxonomies in DB
//                Ebean.save(allTaxonomies);
////                Ebean.save(cleanedTaxonomies);
//                Logger.info("taxonomies successfully loaded");
			
			
			
			
//                // due to merging of different original object models the resulting 
//                // collection set is evaluated from particular taxonomy type
//                Logger.info("load collections ..."); 
//		        List<Object> allCollections = JsonUtils.readCollectionsFromTaxonomies();
//				// store collections in DB
//                Ebean.save(allCollections);
//                Logger.info("collections successfully loaded");
			
//                Logger.info("load instances");
//				// aggregate instances data from drupal and store JSON content in a file
			
			
//		        List<Object> allInstances = JsonUtils.getDrupalData(Const.NodeType.INSTANCE);
			
			
			
//		        Logger.info("Number of instances: " + allInstances.size());
//				// store instances in DB
//                Ebean.save(allInstances);
//                Logger.info("instances successfully loaded");
//                JsonUtils.mapInstancesToTargets();
//                Logger.info("map instances to targets");
//                JsonUtils.getDomainForTargets();
//                Logger.info("Target domains extracted");
//                normalizeUrls();
//                // Create association between Creator and Organisation
//	            List<User> creatorList = (List<User>) User.find.all();
//	            Iterator<User> creatorItr = creatorList.iterator();
//	            while (creatorItr.hasNext()) {
//	              	User creator = creatorItr.next();
////                    Logger.info("Test creator test object: " + creator.toString());
//                    creator.updateOrganisation();
//                    // Create association between User and Role
////                	creator.role_to_user = Role.convertUrlsToObjects(creator.roles);
//        			Ebean.update(creator);
//	            }                
//                // Create associations for Target
//	            List<Target> targetList = (List<Target>) Target.find.all();
//	            Iterator<Target> targetItr = targetList.iterator();
//	            while (targetItr.hasNext()) {
//	            	Target target = targetItr.next();
////                    Logger.info("Test target object: " + target.toString());
//	            	// Create association between Target and Organisation
//	            	target.updateOrganisation();
//                    // Create association between Target and DCollection
//                	target.collectionToTarget = Collection.convertUrlsToObjects(target.fieldCollectionCategories);
//                    // Create association between Target and Subject (Taxonomy)
//                	target.subject = Taxonomy.convertUrlsToObjects(target.fieldSubject);
//                    // Create association between Target and License (Taxonomy)
//                	target.licenseToTarget = Taxonomy.convertUrlsToObjects(target.fieldLicense);
//                    // Create association between Target and Flag
//                	target.flagToTarget = Flag.convertUrlsToObjects(target.flags);
//                    // Create association between Target and Tag
//                	target.tagToTarget = Tag.convertUrlsToObjects(target.tags);
//        			Ebean.update(target);
//	            }
//                // Create associations for Instance
//	            List<Instance> instanceList = (List<Instance>) Instance.find.all();
//	            Iterator<Instance> instanceItr = instanceList.iterator();
//	            while (instanceItr.hasNext()) {
//	            	Instance instance = instanceItr.next();
//	                // Create association between Instance and Organisation
//                    instance.updateOrganisation();
//                    // Create association between Instance and DCollection
//                	instance.collectionToInstance = Collection.convertUrlsToObjects(instance.fieldCollectionCategories);
//                    // Create association between Instance and Subject (Taxonomy)
//                	instance.subjectToInstance = Taxonomy.convertUrlsToObjects(instance.fieldSubject);                    
//                    // Create association between Instance and Flag
//                	instance.flagToInstance = Flag.convertUrlsToObjects(instance.flags);
//                    // Create association between Instance and Tag
//                	instance.tagToInstance = Tag.convertUrlsToObjects(instance.tags);
//        			Ebean.update(instance);
//	            }
//                // Create association between Permission and Role
//	            List<Permission> permissionList = (List<Permission>) Permission.find.all();
//	            Iterator<Permission> permissionItr = permissionList.iterator();
//	            while (permissionItr.hasNext()) {
//	            	Permission permission = permissionItr.next();
////                    Logger.info("Test permission test object: " + permission.toString());
//                    permission.updateRole();
//        			Ebean.update(permission);
//	            }
                Logger.info("+++ Data import completed +++");
	        } catch (Exception e) {
            	e.printStackTrace();
            }
	}
	
	private void importPermissions() {
		@SuppressWarnings("unchecked")
		Map<String,List<Permission>> allPermissions = (Map<String,List<Permission>>)Yaml.load("Accounts.yml");
		List<Permission> permissions = allPermissions.get(Const.PERMISSIONS);
		Ebean.save(permissions);
	}
	
	private void importRoles() {
		@SuppressWarnings("unchecked")
		Map<String,List<Role>> allRoles = (Map<String,List<Role>>)Yaml.load("Accounts.yml");
		List<Role> roles = allRoles.get(Const.ROLES);
		Ebean.save(roles);
	}
	
	private void importAccounts() {
		@SuppressWarnings("unchecked")
		Map<String,List<User>> accounts = (Map<String,List<User>>)Yaml.load("Accounts.yml");
		List<User> users = accounts.get(Const.USERS);
		try {
			for (User user : users) {
				user.password = PasswordHash.createHash(user.password);
				user.createdAt = new Date();
			}
			Ebean.save(users);
//			Logger.info("hash password: " + user.password);
		} catch (NoSuchAlgorithmException e) {
			Logger.info("initial password creation - no algorithm error: " + e);
		} catch (InvalidKeySpecException e) {
			Logger.info("initial password creation - key specification error: " + e);
		}
        Logger.info("Loaded Permissions, Roles and Users");
	}
	
	private void importTaxonomies() {
		@SuppressWarnings("unchecked")
		Map<String,List<Taxonomy>> allTaxonomies = (Map<String,List<Taxonomy>>)Yaml.load("taxonomies.yml");
		List<Taxonomy> taxonomies = allTaxonomies.get(Const.TAXONOMIES);
		for (Taxonomy taxonomy : taxonomies) {
			taxonomy.save();
		}
        Logger.info("Loaded Taxonomies");
	}
	
	private void importTags() { 
		@SuppressWarnings("unchecked")
		Map<String,List<Tag>> allTags = (Map<String,List<Tag>>)Yaml.load("tags.yml");
		List<Tag> tags = allTags.get(Const.TAGS);
		for (Tag tag : tags) {
			tag.save();
		}
        Logger.info("Loaded Tags");
	}
	
	private void importFlags() {
		@SuppressWarnings("unchecked")
		Map<String,List<Flag>> allFlags = (Map<String,List<Flag>>)Yaml.load("flags.yml");
		List<Flag> flags = allFlags.get(Const.FLAGS);
		for (Flag flag : flags) {
			flag.save();
		}
        Logger.info("Loaded Flags");
	}

	private void importMailTemplates() {
		@SuppressWarnings("unchecked")
		Map<String,List<MailTemplate>> allTemplates = (Map<String,List<MailTemplate>>)Yaml.load("mail-templates.yml");
		List<MailTemplate> mailTemplates = allTemplates.get(Const.MAILTEMPLATES);
		for (MailTemplate mailTemplate : mailTemplates) {
			mailTemplate.save();
		}
        Logger.info("Loaded MailTemplates");
	}
	
	private void importContactPersons() {
		@SuppressWarnings("unchecked")
		Map<String,List<ContactPerson>> allContactPersons = (Map<String,List<ContactPerson>>)Yaml.load("contact-persons.yml");
		List<ContactPerson> contactPersons = allContactPersons.get(Const.CONTACTPERSONS);
		for (ContactPerson contactPerson : contactPersons) {
			contactPerson.save();
		}
        Logger.info("Loaded ContactPersons");
	}
	
	private void importOrganisations() {
		@SuppressWarnings("unchecked")
		Map<String,List<Organisation>> allOrganisations = (Map<String,List<Organisation>>)Yaml.load("organisations.yml");
		List<Organisation> organisations = allOrganisations.get(Const.ORGANISATIONS);
		for (Organisation organisation : organisations) {
			organisation.save();
		}
        Logger.info("Loaded Organisations");
	}

	private void importJsonOrganisations() {
		JsonUtils.INSTANCE.convertOrganisations();
	}
	
	private void importCurators() {
		JsonUtils.INSTANCE.convertCurators();
        Logger.info("Loaded Curators");
	}
	
	private void importUrlsToTargets() {
		// store urls in DB
        JsonUtils.INSTANCE.convertUrlsToTargets();
        Logger.info("Loaded URLs");
	}

    /**
	 * normalize URL if there is "_" e.g. in taxonomy_term
	 */
	public void normalizeUrls() {
        List<Target> targets = Target.findAll();
        Iterator<Target> itr = targets.iterator();
        while (itr.hasNext()) {
        	Target target = itr.next();
			if (target.fieldCollectionCategories != null && target.fieldCollectionCategories.contains("_")) {
				target.fieldCollectionCategories = target.fieldCollectionCategories.replace("_", "/");
			}
			if (target.fieldLicense != null && target.fieldLicense.contains("_")) {
				target.fieldLicense = target.fieldLicense.replace("_", "/");
			}
            Ebean.update(target);
		}
	}

    /**
     * This method removes from taxonomy list old subject taxonomies.
     * @param taxonomyList
     * @return
     */
    public List<Taxonomy> cleanUpTaxonomies(List<Object> taxonomyList) {
    	List<Taxonomy> res = new ArrayList<Taxonomy>();
        Iterator<Object> taxonomyItr = taxonomyList.iterator();
        while (taxonomyItr.hasNext()) {
        	Taxonomy taxonomy = (Taxonomy) taxonomyItr.next();
        	if (!(taxonomy.ttype.equals(Const.SUBJECT) && (taxonomy.parent == null || taxonomy.parent.length() == 0)) 
        			&& !(taxonomy.ttype.equals(Const.SUBSUBJECT) && taxonomy.parent.contains(Const.ACT_URL))) {
        		res.add(taxonomy);
        	}
        }
        return res;
    }
    
	public static void main(String[] args) {
		Logger.info("start");
		new play.core.StaticApplication(new java.io.File("."));
		DataImport.INSTANCE.insert();
		Logger.info("finished");
	}
}
