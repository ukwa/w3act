package uk.bl.db;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import models.Collection;
import models.ContactPerson;
import models.FastSubject;
import models.FieldUrl;
import models.Flag;
import models.MailTemplate;
import models.Organisation;
import models.Permission;
import models.Role;
import models.Subject;
import models.Tag;
import models.Target;
import models.Taxonomy;
import models.TaxonomyType;
import models.User;
import models.WatchedTarget;

import com.avaje.ebean.Ebean;

import play.Logger;
import play.libs.Yaml;
import uk.bl.Const;
import uk.bl.api.PasswordHash;
import uk.bl.api.Utils;

public enum DataImport {

	INSTANCE;

	public void insert(boolean importInstances) {
		
        try {
        	
        	importFastSubjects();
        	
            Logger.debug("+++ Importing test data +++");
			if (Ebean.find(Permission.class).findRowCount() == 0) {
	        	this.importPermissions();
	        }
			if (Ebean.find(Role.class).findRowCount() == 0) {
	        	this.importRoles();
	        } else {
	        	for( Role role : Ebean.find(Role.class).findList()) {
	        		Logger.info("Role "+role+" already exists...");
	        	}
	        }
			if (Ebean.find(Organisation.class).findRowCount() == 0) {
	        	this.importOrganisations();
	        }
			if (Ebean.find(User.class).findRowCount() == 0) {
        		this.importAccounts();
	        }
			if (Ebean.find(MailTemplate.class).findRowCount() == 0) {
	        	this.importMailTemplates();
			}
			if (Ebean.find(ContactPerson.class).findRowCount() == 0) {
	        	this.importContactPersons();
			}
			if (Ebean.find(Taxonomy.class).findRowCount() == 0) {
				this.importTaxonomies();
	        	this.importTags();
	        	this.importFlags();
			}
			if (Ebean.find(Target.class).findRowCount() == 0) {
				this.importTargets();
			} else {
				Logger.debug("Targets already exist!");
			}
            Logger.debug("+++ Data import completed +++");
            
        } catch (Exception e) {
        	e.printStackTrace();
        }
	}
	
	public static void importFastSubjects() {
        Logger.debug("Loading Subjects");
    	if (Ebean.find(FastSubject.class).findRowCount() == 0) {
    		@SuppressWarnings("unchecked")
    		Map<String,List<FastSubject>> allFastSubjects = (Map<String,List<FastSubject>>)Yaml.load("fast-subjects.yml");
    		List<FastSubject> fastSubjects = allFastSubjects.get(Const.FAST_SUBJECTS);
    		Ebean.save(fastSubjects);
    	}
	}

	public void importPermissions() {
        Logger.debug("Loading Permissions");
		@SuppressWarnings("unchecked")
		Map<String,List<Permission>> allPermissions = (Map<String,List<Permission>>)Yaml.load("testdata-accounts.yml");
		List<Permission> permissions = allPermissions.get(Const.PERMISSIONS);
		for (Permission permission : permissions) {
			Permission existingPermission = Permission.findByName(permission.name);
			if (existingPermission == null) {
				permission.save();
			}
		}
	}
	
	public void importRoles() {
        Logger.debug("Loading Roles");
		@SuppressWarnings("unchecked")
		Map<String,List<Role>> allRoles = (Map<String,List<Role>>)Yaml.load("testdata-accounts.yml");
		List<Role> roles = allRoles.get(Const.ROLES);
		for (Role role : roles) {
	        Role existingRole = Role.findByName(role.name);
	        if (existingRole == null) {
				role.save();
	        }
		}
	}
	
	public void importAccounts() {
        Logger.debug("Loading Accounts");
		@SuppressWarnings("unchecked")
		Map<String,List<User>> accounts = (Map<String,List<User>>)Yaml.load("testdata-accounts.yml");
		List<User> users = accounts.get(Const.USERS);
		try {
			for (User user : users) {
				Logger.debug("email: " + user.email);
				User existingUser = User.findByEmail(user.email);
				if (existingUser == null) {
					user.url = "act-" + Utils.INSTANCE.createId();
					user.password = PasswordHash.createHash(user.password);
					user.createdAt = new Date();
					String roleHolder = user.roleHolder;
					Role role = Role.findByName(roleHolder);
					if( role != null ) {
						user.roles.add(role);
					}
					Logger.debug("Saving "+user);
					user.save();
				}
			}
		} catch (NoSuchAlgorithmException e) {
			Logger.debug("initial password creation - no algorithm error: " + e);
		} catch (InvalidKeySpecException e) {
			Logger.debug("initial password creation - key specification error: " + e);
		}
        Logger.debug("Loaded Permissions, Roles and Users");
	}
	
	private void importTaxonomies() {
        Logger.debug("Loading Taxonomies");
		@SuppressWarnings("unchecked")
		Map<String,List<Taxonomy>> allTaxonomies = (Map<String,List<Taxonomy>>)Yaml.load("testdata-taxonomies.yml");
		List<Taxonomy> taxonomies = allTaxonomies.get(Const.TAXONOMIES);
		TaxonomyType tv = null;
		for (Taxonomy taxonomy : taxonomies) {
			
			// see if they are already stored?
			Taxonomy lookup = Taxonomy.findByNameAndType(taxonomy.name, taxonomy.ttype);
			if (lookup == null) {
				tv = TaxonomyType.findByMachineName(taxonomy.ttype);
				taxonomy.setTaxonomyType(tv);
				taxonomy.url = Const.ACT_URL + Utils.INSTANCE.createId();
				if (StringUtils.isNotEmpty(taxonomy.parentName)) {
					Taxonomy parent = Taxonomy.findByNameAndType(taxonomy.parentName, taxonomy.ttype);
					if (parent != null) {
						Logger.debug("Parent found: " + parent);
						if (taxonomy instanceof Collection) {
							((Collection)taxonomy).parent = (Collection)parent;
						}
						else if (taxonomy instanceof Subject) {
							((Subject)taxonomy).parent = (Subject)parent;
						}
					}
				}
				taxonomy.save();
			}
		}
        Logger.debug("Loaded Taxonomies");
	}
	
	private void importTags() { 
        Logger.debug("Loading Tags");
		@SuppressWarnings("unchecked")
		Map<String,List<Tag>> allTags = (Map<String,List<Tag>>)Yaml.load("testdata-tags.yml");
		List<Tag> tags = allTags.get(Const.TAGS);
		for (Tag tag : tags) {
			tag.url = Const.ACT_URL + Utils.INSTANCE.createId();
			tag.save();
		}
        Logger.debug("Loaded Tags");
	}
	
	private void importFlags() {
        Logger.debug("Loading Flags");
		@SuppressWarnings("unchecked")
		Map<String,List<Flag>> allFlags = (Map<String,List<Flag>>)Yaml.load("testdata-flags.yml");
		List<Flag> flags = allFlags.get(Const.FLAGS);
		for (Flag flag : flags) {
			flag.url = Const.ACT_URL + Utils.INSTANCE.createId();
			flag.save();
		}
        Logger.debug("Loaded Flags");
	}

	private void importMailTemplates() {
        Logger.debug("Loading MailTemplates");
		@SuppressWarnings("unchecked")
		Map<String,List<MailTemplate>> allTemplates = (Map<String,List<MailTemplate>>)Yaml.load("testdata-mail-templates.yml");
		List<MailTemplate> mailTemplates = allTemplates.get(Const.MAILTEMPLATES);
		for (MailTemplate mailTemplate : mailTemplates) {
			mailTemplate.url = Const.ACT_URL + Utils.INSTANCE.createId();
			mailTemplate.save();
		}
        Logger.debug("Loaded MailTemplates");
	}
	
	private void importContactPersons() {
        Logger.debug("Loading People");
		@SuppressWarnings("unchecked")
		Map<String,List<ContactPerson>> allContactPersons = (Map<String,List<ContactPerson>>)Yaml.load("testdata-contact-persons.yml");
		List<ContactPerson> contactPersons = allContactPersons.get(Const.CONTACTPERSONS);
		for (ContactPerson contactPerson : contactPersons) {
			contactPerson.url = Const.ACT_URL + Utils.INSTANCE.createId();
			contactPerson.save();
		}
        Logger.debug("Loaded ContactPersons");
	}
	
	private void importOrganisations() {
        Logger.debug("Loading Organisations");
		@SuppressWarnings("unchecked")
		Map<String,List<Organisation>> allOrganisations = (Map<String,List<Organisation>>)Yaml.load("testdata-organisations.yml");
		List<Organisation> organisations = allOrganisations.get(Const.ORGANISATIONS);
		for (Organisation organisation : organisations) {
			organisation.url = Const.ACT_URL + Utils.INSTANCE.createId();
			organisation.save();
		}
        Logger.debug("Loaded Organisations");
	}
	
	private void importTargets() {
        Logger.debug("Loading Targets...");
		@SuppressWarnings("unchecked")
		Map<String,List<Target>> map = (Map<String,List<Target>>)Yaml.load("testdata-targets.yml");
		List<Target> list = map.get("targets");
		Logger.info("Got "+list.size()+" targets");
		for (Target i : list) {
			i.url = Const.ACT_URL + Utils.INSTANCE.createId();
			i.save();
			// WatchedTargets do not cascade automatically:
			if( i.watchedTarget != null) {
				i.watchedTarget.target = i;
				i.watchedTarget.save();
			}
			// Users do not cascade automatically:
			i.authorUser.save();
			/*
			for( FieldUrl f : i.fieldUrls) {
				f.save();
			}
			*/
		}
        Logger.debug("Loaded Targets");
	}

	public static void main(String[] args) {
		Logger.debug("start");
		new play.core.StaticApplication(new java.io.File("."));
		DataImport.INSTANCE.insert(true);
		Logger.debug("finished");
	}
}
