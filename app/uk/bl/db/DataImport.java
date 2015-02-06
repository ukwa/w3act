package uk.bl.db;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

import models.Collection;
import models.ContactPerson;
import models.Flag;
import models.Instance;
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

import com.avaje.ebean.Ebean;

import play.Logger;
import play.libs.Yaml;
import uk.bl.Const;
import uk.bl.api.JsonUtils;
import uk.bl.api.PasswordHash;
import uk.bl.api.Utils;

public enum DataImport {

	INSTANCE;

	public void insert() {
		
    	Boolean importAccounts = play.Play.application().configuration().getBoolean("use.accounts");

        try {

			if (Ebean.find(User.class).findRowCount() == 0) {
	        	this.importPermissions();
	        	this.importRoles();
	        	this.importJsonOrganisations();
	        	this.importOrganisations();
	        	this.importCurators();
	        	if (importAccounts) {
	        		this.importAccounts();
	        	}
	        }
			if (Ebean.find(MailTemplate.class).findRowCount() == 0) {
	        	this.importMailTemplates();
			}
			if (Ebean.find(ContactPerson.class).findRowCount() == 0) {
	        	this.importContactPersons();
			}
			if (Ebean.find(TaxonomyType.class).findRowCount() == 0) {
				this.importJsonTaxonomyVocabularies();
			}
			if (Ebean.find(Taxonomy.class).findRowCount() == 0) {
				this.importJsonTaxonomies();
				this.importTaxonomies();
	        	this.importTags();
	        	this.importFlags();
			}
			if (Ebean.find(Target.class).findRowCount() == 0) {
	        	this.importTargets();
			}
			if (Ebean.find(Instance.class).findRowCount() == 0) {
				this.importInstances();
			}
            Logger.debug("+++ Data import completed +++");
            
        	String defaultAdminEmail = play.Play.application().configuration().getString("admin.default.email");
        	// find the imported admin user from Andy's act
			User user = User.findByEmail(defaultAdminEmail);
			String generated = UUID.randomUUID().toString();
			if (user != null) {
				user.password = PasswordHash.createHash(generated);
				user.update();
			}
        	
			//String password = AdminUserImport.INSTANCE.create(defaultAdminEmail);
			Logger.info("Email: " + user.email + ", ADMIN PASSWORD: " + generated);
            
        } catch (Exception e) {
        	e.printStackTrace();
        }
	}
	
	private void importPermissions() {
		@SuppressWarnings("unchecked")
		Map<String,List<Permission>> allPermissions = (Map<String,List<Permission>>)Yaml.load("accounts.yml");
		List<Permission> permissions = allPermissions.get(Const.PERMISSIONS);
		for (Permission permission : permissions) {
			permission.save();
		}
	}
	
	private void importRoles() {
		@SuppressWarnings("unchecked")
		Map<String,List<Role>> allRoles = (Map<String,List<Role>>)Yaml.load("accounts.yml");
		List<Role> roles = allRoles.get(Const.ROLES);
		for (Role role : roles) {
			role.save();
		}
	}
	
	private void importAccounts() {
		@SuppressWarnings("unchecked")
		Map<String,List<User>> accounts = (Map<String,List<User>>)Yaml.load("accounts.yml");
		List<User> users = accounts.get(Const.USERS);
		try {
			for (User user : users) {
				user.password = PasswordHash.createHash(user.password);
				user.createdAt = new Date();
				String roleHolder = user.roleHolder;
				Role role = Role.findByName(roleHolder);
				user.roles.add(role);
				user.save();
			}
		} catch (NoSuchAlgorithmException e) {
			Logger.debug("initial password creation - no algorithm error: " + e);
		} catch (InvalidKeySpecException e) {
			Logger.debug("initial password creation - key specification error: " + e);
		}
        Logger.debug("Loaded Permissions, Roles and Users");
	}
	
	private void importJsonTaxonomyVocabularies() {
		JsonUtils.INSTANCE.convertTaxonomyVocabulary();
        Logger.debug("Loaded Json Taxonomies Vocabularies");
	}

	private void importJsonTaxonomies() {
		JsonUtils.INSTANCE.convertTaxonomies();
        Logger.debug("Loaded Json Taxonomies");
	}
	
	private void importTaxonomies() {
		@SuppressWarnings("unchecked")
		Map<String,List<Taxonomy>> allTaxonomies = (Map<String,List<Taxonomy>>)Yaml.load("taxonomies.yml");
		List<Taxonomy> taxonomies = allTaxonomies.get(Const.TAXONOMIES);
		TaxonomyType tv = null;
		for (Taxonomy taxonomy : taxonomies) {
			
			// see if they are already stored?
			Taxonomy lookup = Taxonomy.findByNameAndType(taxonomy.name, taxonomy.ttype);
			if (lookup == null) {
				tv = TaxonomyType.findByMachineName(taxonomy.ttype);
				Logger.debug("ttype: " + taxonomy.ttype + " - " + tv);
				taxonomy.setTaxonomyType(tv);
				taxonomy.url = Const.ACT_URL + Utils.INSTANCE.createId();
				if (StringUtils.isNotEmpty(taxonomy.parentName)) {
					Taxonomy parent = Taxonomy.findByNameAndType(taxonomy.parentName, taxonomy.ttype);
					Logger.debug("Parent found: " + parent);
					if (parent != null) {
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
		@SuppressWarnings("unchecked")
		Map<String,List<Tag>> allTags = (Map<String,List<Tag>>)Yaml.load("tags.yml");
		List<Tag> tags = allTags.get(Const.TAGS);
		for (Tag tag : tags) {
			tag.url = Const.ACT_URL + Utils.INSTANCE.createId();
			tag.save();
		}
        Logger.debug("Loaded Tags");
	}
	
	private void importFlags() {
		@SuppressWarnings("unchecked")
		Map<String,List<Flag>> allFlags = (Map<String,List<Flag>>)Yaml.load("flags.yml");
		List<Flag> flags = allFlags.get(Const.FLAGS);
		for (Flag flag : flags) {
			flag.url = Const.ACT_URL + Utils.INSTANCE.createId();
			flag.save();
		}
        Logger.debug("Loaded Flags");
	}

	private void importMailTemplates() {
		@SuppressWarnings("unchecked")
		Map<String,List<MailTemplate>> allTemplates = (Map<String,List<MailTemplate>>)Yaml.load("mail-templates.yml");
		List<MailTemplate> mailTemplates = allTemplates.get(Const.MAILTEMPLATES);
		for (MailTemplate mailTemplate : mailTemplates) {
			mailTemplate.url = Const.ACT_URL + Utils.INSTANCE.createId();
			mailTemplate.save();
		}
        Logger.debug("Loaded MailTemplates");
	}
	
	private void importContactPersons() {
		@SuppressWarnings("unchecked")
		Map<String,List<ContactPerson>> allContactPersons = (Map<String,List<ContactPerson>>)Yaml.load("contact-persons.yml");
		List<ContactPerson> contactPersons = allContactPersons.get(Const.CONTACTPERSONS);
		for (ContactPerson contactPerson : contactPersons) {
			contactPerson.url = Const.ACT_URL + Utils.INSTANCE.createId();
			contactPerson.save();
		}
        Logger.debug("Loaded ContactPersons");
	}
	
	private void importOrganisations() {
		@SuppressWarnings("unchecked")
		Map<String,List<Organisation>> allOrganisations = (Map<String,List<Organisation>>)Yaml.load("organisations.yml");
		List<Organisation> organisations = allOrganisations.get(Const.ORGANISATIONS);
		for (Organisation organisation : organisations) {
			organisation.url = Const.ACT_URL + Utils.INSTANCE.createId();
			organisation.save();
		}
        Logger.debug("Loaded Organisations");
	}

	private void importJsonOrganisations() {
		JsonUtils.INSTANCE.convertOrganisations();
        Logger.debug("Loaded Json Organisations");
	}
	
	private void importCurators() {
		JsonUtils.INSTANCE.convertCurators();
        Logger.debug("Loaded Curators");
	}
	
	private void importTargets() {
		// store urls in DB
        JsonUtils.INSTANCE.convertTargets();
        Logger.debug("Loaded URLs");
	}
	
	private void importInstances() {
        JsonUtils.INSTANCE.convertInstances();;
        Logger.debug("Loaded Instances");
	}

	public static void main(String[] args) {
		Logger.debug("start");
		new play.core.StaticApplication(new java.io.File("."));
		DataImport.INSTANCE.insert();
		Logger.debug("finished");
	}
}
