package uk.bl;

import java.util.HashMap;
import java.util.Map;

public final class Const {
  
    private Const() {}
	
	/**
	 * Taxonomy
	 */
	public static final String USERS           = "users";
	public static final String ROLES           = "roles";
	public static final String PERMISSIONS     = "permissions";
	public static final String ORGANISATIONS   = "organisations";
	public static final String MAILTEMPLATES   = "mailtemplates";
	public static final String CONTACTPERSONS  = "contactpersons";
	public static final String LICENSES        = "licenses";
	public static final String LICENCE         = "license";
	public static final String COLLECTION_URL  = "collection_url";
	public static final String ORGANISATION_URL = "organisation_url";
	public static final String SUBJECT_URL     = "subject_url";

    // Drupal connection definitions
    public static final String URI             = "uri";
    public static final String URL             = "url";
    public static final String URL_STR         = "http://www.webarchive.org.uk/act/node.json?type=";
    public static final String URL_STR_BASE    = "http://www.webarchive.org.uk/act/";
	public static final String DRUPAL_USER     = "drupal_user";
	public static final String DRUPAL_PASSWORD = "drupal_password";
	public static final String OUT_FILE_PATH   = "res.txt"; // res files are stored in root directory of the project
	public static final String TXT_EXT         = ".txt"; 
	
	// Names of the JSON nodes
	public static final String FIRST_PAGE      = "first";
	public static final String LAST_PAGE       = "last";
	public static final String PAGE_IN_URL     = "page=";
	public static final String LIST_NODE       = "list";
	public static final String FIELD_URL_NODE  = "field_url";
	public static final String NODE_TYPE       = "type";
	public static final String EMPTY           = "empty";
	public static final String ITEM            = "item";
	
	// Body elements in JSON node
	public static final String VALUE              = "value";
	public static final String SUMMARY            = "summary";
	public static final String FORMAT             = "format";
	public static final String BODY               = "body";
	public static final String AUTHOR             = "author";
	public static final String FIELD_AFFILIATION  = "field_affiliation";
	public static final String FIELD_SUBJECT      = "field_subject";
	public static final String FIELD_QA_STATUS    = "field_qa_status";
	public static final String FIELD_NOMINATING_ORGANISATION = "fieldNominatingOrganisation";
	public static final String FIELD_TARGET       = "fieldTarget";
	public static final String FIELD_QA_ISSUE     = "field_qa_issue";
	public static final String JSON               = ".json";
	public static final String COMMA              = ",";

	public static final int STRING_LIMIT          = 50;

	// Help constants
	public static final int PAGINATION_OFFSET     = 10; // offset is a page step from current page for pagination
	public static final int ROWS_PER_PAGE         = 50;
	public static final int MAX_USER_COUNT        = 100;
	public static final int BUFFER_SIZE           = 1024; // 1KB 
	public static final int MAX_CHUNKS            = 1000;  
	public static final int BODY_OFFSET           = 3;  
	public static final int BRACES_OFFSET         = 2;  
	public static final String ENCODING           = "UTF-8";
	public static final String WEBARCHIVE_LINK    = "www.webarchive.org.uk"; 
	public static final String ACT_URL            = "act-";
	public static final String WCT_URL            = "wct-";
	public static final String EDIT_LINK          = "/edit";
	public static final String INITIAL_REVISION   = "initial revision";
	public static final String STR_FORMAT         = "UTF-8";
	public static final String CSV                = "csv";
	public static final String TARGET_DEF         = "TARGETDEF";
	public static final String OFFSET             = "offset";
	public static final String LIMIT              = "limit";
	public static final String LIST_DELIMITER     = ", ";
	public static final String SLASH_DELIMITER    = "/";
	public static final String HOST          	  = "host";
	public static final String PORT               = "port";
	public static final String MAIL_USER          = "mail.user";
	public static final String MAIL_PASSWORD      = "mail.password";
	public static final String FROM               = "from";
	public static final String BL_MAIL_ADDRESS    = "wa-sysadm@bl.uk";
	public static final String TAGS               = "tags";
	public static final String TAXONOMIES         = "taxonomies";
	public static final String LAST_VERSION_FILE  = "last-version.txt";
	public static final String PLACE_HOLDER_DELIMITER = "||";
	public static final String URL_PLACE_HOLDER   = "||URL||";
	public static final String LINK_PLACE_HOLDER  = "||LINK||";
	public static final String LICENCE_PLACE_HOLDER = "||licence||";
	public static final String ACKNOWLEDGEMENT    = "Acknowledgement";
    public static final String HTTP_PREFIX        = "http://";
	public static final String SERVER_NAME        = "server_name";
	public static final String UND                = "und";
    public static final String NUMBER             = "number";
	
    public static final String CREATED_AT			= "createdAt";
    public static final String UPDATED_AT			= "updatedAt";

    /**
	 * SQL constants
	 */
	public static final String ASC                = "asc";
	public static final String DESC               = "desc";

	/**
	 * RabbitMQ settings
	 */
	public static final String QUEUE_HOST         = "queue_host";
	public static final String QUEUE_PORT         = "queue_port";
	public static final String QUEUE_NAME         = "queue_name";
	public static final String ROUTING_KEY        = "routing_key";
	public static final String EXCHANGE_NAME      = "exchange_name";
		
	/**
	 * Elements in HTML forms
	 */
	public static final String FALSE              = "false";
	public static final String TRUE               = "true";
	public static final String FILTER             = "filter";
	public static final String NONE               = "none";
	public static final String USER               = "user";
	public static final String ALL                = "all";
	public static final String DATE_FORMAT        = "dd-MM-yyyy";
	public static final String NONE_VALUE         = "None";
	public static final String EITHER             = "either";

	/**
	 * Target
	 */
	public static final String TID                = "tid";
	public static final String TITLE              = "title";
	public static final String FIELD_URL          = "fieldUrl";
	public static final String KEYSITE            = "keysite";
	public static final String STATUS             = "status";
	public static final String LIVE_SITE_STATUS   = "livesitestatus";
	public static final String DESCRIPTION        = "description";
	public static final String SUBJECT            = "subject";
	public static final String SUBSUBJECT         = "subsubject";
	public static final String ORGANISATION       = "organisation";
	public static final String ORIGINATING_ORGANISATION = "originatingOrganisation";
	public static final String FIELD_SUGGESTED_COLLECTIONS = "fieldSuggestedCollections";
	public static final String IS_IN_SCOPE_UK_REGISTRATION_VALUE = "isInScopeUkRegistrationValue";
	public static final String FIELD_CRAWL_FREQUENCY = "field_crawl_frequency";
	public static final String FIELD_COLLECTION_CATEGORIES = "fieldCollectionCategories";
	public static final String FIELD_SCOPE        = "fieldScope";
	public static final String FIELD_DEPTH        = "fieldDepth";
	public static final String FLAG               = "flag";
	public static final String REVISION           = "revision";
	public static final String ACTIVE             = "active";
	public static final String FIELD_WCT_ID       = "wct";
	public static final String FIELD_WCT          = "fieldWct_id";
	public static final String FIELD_SPT_ID       = "spt";
	public static final String FIELD_LICENSE      = "license";
	public static final String FIELD_LICENSE_NODE = "fieldLicense";
	public static final String FIELD_UK_HOSTING   = "fieldUkHosting";
	public static final String FIELD_UK_POSTAL_ADDRESS = "fieldUkPostalAddress";
	public static final String FIELD_UK_POSTAL_ADDRESS_URL = "fieldUkPostalAddressUrl";
    public static final String FIELD_VIA_CORRESPONDENCE = "fieldViaCorrespondence";
    public static final String FIELD_NOTES        = "fieldNotes";
    public static final String FIELD_PROFESSIONAL_JUDGEMENT = "fieldProfessionalJudgement";
    public static final String FIELD_PROFESSIONAL_JUDGEMENT_EXP = "fieldProfessionalJudgementExp";
    public static final String FIELD_NO_LD_CRITERIA_MET = "fieldNoLdCriteriaMet";
    public static final String FIELD_IGNORE_ROBOTS_TXT = "fieldIgnoreRobotsTxt";
    public static final String FIELD_CRAWL_START_DATE = "fieldCrawlStartDate";
    public static final String FIELD_CRAWL_END_DATE = "fieldCrawlEndDate";
    public static final String WHITE_LIST = "whiteList";
    public static final String BLACK_LIST = "blackList";
    public static final String KEYWORDS = "keywords";
    public static final String SYNONYMS = "synonyms";
    public static final String SORTED = "sorted";
    public static final String LANGUAGE = "language";
    public static final String AUTHORS = "authors";
    public static final String FLAGS = "flags";
	public static final String DATE_OF_PUBLICATION = "dateOfPublication";
	public static final String JUSTIFICATION      = "justification";
	public static final String SELECTION_TYPE     = "selectionType";
	public static final String SELECTOR_NOTES     = "selectorNotes";
	public static final String ARCHIVIST_NOTES    = "archivistNotes";
	public static final String LEGACY_SITE_ID     = "legacySite_id";
	public static final String FIELD_TIMESTAMP    = "fieldTimestamp";
	public static final String QA_STATUS          = "qaStatus";
	public static final String QA_ISSUE_CATEGORY  = "qaIssueCategory";
	public static final String QA_NOTES           = "qaNotes";
	public static final String QUALITY_NOTES      = "qualityNotes";
	public static final String DOMAIN             = "domain";
	public static final String FLAG_NOTES         = "flagNotes";
	public static final String CHANGED            = "changed";
	public static final String TREE_KEYS          = "treeKeys";
	public static final String SUBJECT_TREE_KEYS  = "subjectTreeKeys";
	public static final String NA                 = "N/A";
	public static final int    MAX_NPLD_LIST_SIZE = 3;
	public static final String TAB_STATUS         = "tabStatus";
	public static final String NPLD               = "npld";
	public static final String TLD                = "tld";
	public static final String FILTER_NPLD        = "filterNpld";
	public static final String FILTER_TLD         = "filterTld";
	public static final String INSTANCE           = "instance";
	
    /**
     * Creator
     */
	public static final String UID                = "uid";
	public static final String NAME               = "name";
	public static final String EMAIL              = "email";
	public static final String DEFAULT_ROLE       = "user";
	public static final String DEFAULT_BL_ROLE    = "archivist";
	public static final String PASSWORD           = "password";
	public static final String PASSWORD_VALIDATION = "password_validation";
	public static final String OLD_PASSWORD       = "oldpassword";
	public static final String USER_URL           = "user_url";

	/**
	 * Taxonomy
	 */
	public static final String TYPE               = "type";
	public static final String TTYPE              = "ttype";
	public static final String FIELD_OWNER        = "field_owner";
	public static final String PARENT             = "parent";
	public static final String PARENTS_ALL        = "parents_all";
	public static final String OPEN_UKWA_LICENSE  = "Open UKWA Licence (2014-)";
	public static final String FIELD_PUBLISH      = "field_publish";
	
	/**
	 * Organisation
	 */
	public static final String FIELD_ABBREVIATION = "field_abbreviation";
	public static final String ALL_AGENCIES       = "All Agencies";
	
	/**
	 * Permission
	 */
	public static final String ID                 = "id";
	public static final String PERMISSION         = "permission";
	public static final String ROLE               = "role";	
	/**
	 * Crawl Permission
	 */
	public static final String TARGET             = "target";
	public static final String CONTACT_PERSON     = "contactPerson";
	public static final String CREATOR_USER       = "creatorUser";
	public static final String TEMPLATE           = "template";
	public static final String REQUEST_FOLLOW_UP  = "requestFollowup";
	public static final String LICENSE_DATE       = "licenseDate";
	
	public static final String DEFAULT_CRAWL_PERMISSION_STATUS = "QUEUED";
	
	/**
	 * Contact Person
	 */
	public static final String POSITION          = "position";
	public static final String CONTACT_ORGANISATION = "contactOrganisation";
	public static final String PHONE             = "phone";
	public static final String POSTAL_ADDRESS    = "postalAddress";
	public static final String WEB_FORM          = "webForm";
	public static final String DEFAULT_CONTACT   = "defaultContact";
	public static final String PERMISSION_CHECKED = "permissionChecked";
	
	/**
	 * Licence
	 */
	public static final String AGREE             = "agree";
	public static final String CONTENT           = "content";
	public static final String PUBLISH           = "publish";
	public static final String DATE              = "date";
	public static final String ON                = "on";
	public static final String OLD_UKWA_LICENSE  = "UKWA Selective Archive Licence";
	public static final String NEW_UKWA_LICENSE  = "UKWA Selective Archive Licence (pre-2014)";
	
	/**
	 * Permission Refusals
	 */
	public static final String REFUSAL_DATE      = "refusal-date";
	public static final String REASON            = "reason";

	/**
	 * Communications Logging
	 */
	public static final String LOG_DATE          = "log-date";
	public static final String NOTES             = "notes";
	public static final String CURATOR           = "curator";

	/**
	 * Mail Template
	 */
	public static final String TEXT              = "text";
	public static final String FROM_EMAIL        = "fromEmail";
	public static final String PLACE_HOLDERS     = "placeHolders";
	public static final String DEFAULT_EMAIL_FLAG = "defaultEmail";
	public static final String TEMPLATES_PATH    = "conf" + System.getProperty("file.separator") +
													"templates" + System.getProperty("file.separator");
	public static final String DEFAULT_TEMPLATE  = "General";
	public static final String LICENCE_ACK       = "License";
	public static final String WEBSITE_TITLE_ACK = "Title of Website";
	public static final String WEB_ADDRESS_ACK   = "Web Address (URL)";
	public static final String NAME_ACK          = "Name";
	public static final String POSITION_ACK      = "Position";
	public static final String EMAIL_ACK         = "E-mail";
	public static final String POSTAL_ADDRESS_ACK = "Postal Address";
	public static final String CONTACT_ORGANISATION_ACK = "Contact Organisation";
	public static final String TEL_ACK           = "Tel";
	public static final String DESCRIPTION_ACK   = "Any other information";
	public static final String THIRD_PARTY_ACK   = "Third-Party Content";
	public static final String AGREE_ACK         = "I/We agree";
	public static final String DATE_ACK          = "Date";
	public static final String PUBLICITY_ACK     = "Future publicity for the Web Archive";
	
	/**
	 * Nomination
	 */
	public static final String WEBSITE_URL              = "website_url";
	public static final String TEL                      = "tel";
	public static final String ADDRESS                  = "address";
	public static final String NOMINATED_WEBSITE_OWNER  = "nominated_website_owner";
	public static final String NOMINATION_CHECKED       = "nomination_checked";
	public static final String NOMINATION_DATE          = "nomination_date";
	
	/**
	 * Reports
	 */
	public static final String EXPORT_TARGETS_REPORTS_QA              = "export_targets_reports_qa.csv";
	public static final String EXPORT_TARGETS_WITH_QAED_INSTANCES     = "export_targets_with_qaed_instances.csv";
	public static final String EXPORT_TARGETS_WITH_AWAITING_QA        = "export_targets_with_awaiting_qa.csv";
	public static final String EXPORT_TARGETS_WITH_QA_ISSUES          = "export_targets_with_qa_issues.csv";
	public static final String EXPORT_TARGETS_WITH_NO_QA_ISSUES       = "export_targets_with_no_qa_issues.csv";
	public static final String EXPORT_TARGETS_WITH_FAILED_INSTANCES   = "export_targets_with_failed_instances.csv";
	public static final String EXPORT_TARGETS_WITH_PASSED_INSTANCES   = "export_targets_with_passed_instances.csv";
	public static final String EXPORT_TARGETS_WITH_QA_ISSUES_RESOLVED = "export_targets_with_qa_issues_resolved.csv";
	public static final String EXPORT_TARGETS_REPORT_CREATION         = "export_targets_report_creation.csv";

	/**
	 * Buttons
	 */
	public static final String SAVE               = "save";
	public static final String DELETE             = "delete";
	public static final String CLEAR              = "clear";
	public static final String EXPORT             = "export";
	public static final String EXPORT_FILE        = "export.csv";
	public static final String EXPORT_INSTANCE_FILE = "exportinstances.csv";
	public static final String EXPORT_REQUESTED_LICENCE_FILE = "export_requested_licences.csv";
	public static final String EXPORT_GRANTED_LICENCE_FILE = "export_granted_licences.csv";
	public static final String EXPORT_REFUSED_LICENCE_FILE = "export_refused_licences.csv";
	public static final String CSV_SEPARATOR      = ";";
	public static final String CSV_LINE_END       = "\n";
	public static final String TWO_POINTS         = ": ";
	public static final String SEARCH             = "search";
	public static final String ADDENTRY           = "addentry";
	public static final String SEND               = "send";
	public static final String SELECT_ALL         = "selectall";
	public static final String DESELECT_ALL       = "deselectall";
	public static final String SEND_ALL           = "sendall";
	public static final String SEND_SOME          = "sendsome";
	public static final String PREVIEW            = "preview";
	public static final String REJECT             = "reject";
	public static final String SUBMIT             = "submit";
	public static final String UPDATE             = "update";
	public static final String REQUEST            = "request";	
	public static final String ARCHIVE            = "archive";	
	public static final String APPLY              = "apply";

	/**
	 * Sorting/Pagination
	 */
	public static final String PAGE_NO             	= "p";
	public static final String PAGE_SIZE          	= "page_size";
	public static final String SORT_BY				= "s";
	public static final String ORDER             	= "o";
	public static final String QUERY             	= "q";
	public static final String QUERY_COLLECTION    	= "query_collection";
	public static final String QUERY_QA_STATUS     	= "query_qa_status";
	
	/**
	 * Export settings
	 */
	public static final String ACTION               = "action";   
	public static final String EXPORT_LOOKUPS       = "export_lookups";  
	public static final String EXPORT_LOOKUP_FILE   = "exportlookups.csv"; 
	public static final String LOOKUP_DEF           = "lookup_def"; 
	public static final String YES                  = "yes"; 
	public static final String NO                   = "no"; 
	public static final String MAX_COUNT            = "max_count"; 
	
	/**
	 * HTTP request
	 */
    public static final String GITHUB = "https://github.com/ukwa/w3act/commits/master/"; 
    public static final String LAST_COMMIT = "/ukwa/w3act/commit/"; 
	
	/**
	 * Database associations
	 */
    public static final String SUBJECT_TARGET        = "subject_target"; 
    public static final String SUBJECT_INSTANCE      = "subject_instance"; 
    public static final String COLLECTION_TARGET     = "collection_target"; 
    public static final String COLLECTION_INSTANCE   = "collection_instance"; 
    public static final String LICENSE_TARGET        = "license_target"; 
    public static final String TAG_TARGET            = "tag_target"; 
    public static final String FLAG_TARGET           = "flag_target"; 
    public static final String TAG_INSTANCE          = "tag_instance"; 
    public static final String FLAG_INSTANCE         = "flag_instance"; 
    public static final String ROLE_USER             = "role_user"; 
    public static final String PERMISSION_ROLE       = "permission_role"; 

    // Types of the JSON nodes
	public enum NodeType {
        URL, 
		COLLECTION,
		ORGANISATION,
		USER,
		TAXONOMY,
		TAXONOMY_VOCABULARY,
		INSTANCE;
    }
	
	public enum TaxonomyType {
		COLLECTION,
		LICENSE,
		SUBJECT,
		SUBSUBJECT,
		QUALITY_ISSUE;
    }
	
	/**
	 * Records status of permission process.
	 */
	public enum CrawlPermissionStatus {
		NOT_INITIATED,
		QUEUED,
		PENDING,
		REFUSED,
		EMAIL_REJECTED,
		GRANTED,
		SUPERSEDED;
    }
    
    /**
     * E-mail type
     */
	public enum MailTemplateType {
		PERMISSION_REQUEST,
		THANK_YOU_ONLINE_PERMISSION_FORM,
		THANK_YOU_ONLINE_NOMINATION_BY_OWNER,
		THANK_YOU_ONLINE_NOMINATION_BY_NOMINATOR;
    }
	    
	/**
	 * Types of permission refusal.
	 */
	public enum RefusalType {
		THIRD_PARTY_CONTENT,
		IMPRACTICALITY,
		INTERNAL_REASONS,
		LEGALISTIC_FORM,
		NO_REASON,
		PRIVACY,
		OTHER;
    }
    	
	/**
	 * Types of communication logs.
	 */
	public enum CommunicationLogTypes {
		EMAIL,
		PHONE,
		LETTER,
		WEB_FORM,
		CONTACT_DETAIL_REQUEST,
		OTHER;
    }
    	
	/**
	 * The predominant language of target.
	 */
	public enum TargetLanguage {
		EN,
		DE;
    }
    
	/**
	 * The predominant language of target.
	 */
	public enum RequestTypes {
		ALL,
		FIRST_REQUEST,
		FOLLOW_UP;
    }
    
	/**
	 * The flag types of target.
	 */
	public enum TargetFlags {
	    PRIORITY_PERMISSION,
	    PRIORITY_CRAWL_AND_QA, 
	    PRIORITY_QA,
	    QA_ISSUE_APPEARANCE,
	    QA_ISSUE_FUNCTIONALITY,
	    QA_ISSUE_CONTENT,
	    FOLLOW_UP_PERMISSION,
	    GENERAL_CHANGE_REQUEST;
    }
    
	public enum Roles {
        sys_admin, 
		archivist,
		expert_user,
		user,
		viewer;
    }
	
	public enum TabStatus {
		overview,
		metadata,
		crawlpermission,
		crawlpolicy,
		licensing;
    }
		
    /**
     * Help mapping to present predefined Flag values in GUI.
     */
    public static final Map<String, String> guiFlagMap = new HashMap<String, String>();
    	static {
    	guiFlagMap.put("PRIORITY_PERMISSION",    "Priority Permission");
    	guiFlagMap.put("PRIORITY_CRAWL_AND_QA",  "Priority Crawl & QA");
    	guiFlagMap.put("PRIORITY_QA",            "Priority QA");
    	guiFlagMap.put("QA_ISSUE_APPEARANCE",    "QA Issue - Appearance");
    	guiFlagMap.put("QA_ISSUE_FUNCTIONALITY", "QA Issue - Functionality");
    	guiFlagMap.put("QA_ISSUE_CONTENT",       "QA Issue - Content");
    	guiFlagMap.put("FOLLOW_UP_PERMISSION",   "Follow Up Permission");
    	guiFlagMap.put("GENERAL_CHANGE_REQUEST", "General Change Request");
    }
		
    /**
     * GUI fields in relation to domain field names.
     */
    public static final Map<String, String> guiMap = new HashMap<String, String>();
    	static {
    	guiMap.put(NAME,                     "Name");
    	guiMap.put(TITLE,                    "Title");
    	guiMap.put(ID,                       "ID");
    	guiMap.put(FIELD_SUBJECT,            "Subject");
    	guiMap.put(FIELD_URL_NODE,           "Seed URL(s)");
    	guiMap.put(AUTHOR,                   "Selector");
    	guiMap.put(SELECTION_TYPE,           "Selection Type");
    	guiMap.put(TARGET,                   "Target");
    	guiMap.put(EMAIL,                    "E-mail");
    	guiMap.put(FIELD_ABBREVIATION,       "Abbreviation");
    	guiMap.put(FROM_EMAIL,               "From E-mail");
    }
    		
	public enum SelectionType {
		NOMINATION, // when created from UKWA
		SELECTION;
    }
	
    /**
     * Help mapping to present predefined Scope values in GUI.
     */
    public static final Map<String, String> guiScopeMap = new HashMap<String, String>();
    	static {
		guiScopeMap.put("resource",   "Just this URL.");
		guiScopeMap.put("plus1",      "This URL plus any directly linked resources.");
		guiScopeMap.put("root",       "All URLs that start like this.");
		guiScopeMap.put("subdomains", "All URLs that match match this host or any subdomains.");
    }
		
	public enum ScopeType {
		resource, 
		plus1,
		root,
		subdomains;
    }
    	
    /**
     * Help mapping to present predefined Depth values in GUI.
     */
    public static final Map<String, String> guiDepthMap = new HashMap<String, String>();
    	static {
		guiDepthMap.put("capped",       "Capped (small - 500MB)");
		guiDepthMap.put("capped_large", "Capped (large - 2GB)");
		guiDepthMap.put("deep",         "Uncapped");
    }
		
	public enum DepthType {
		capped, 
		capped_large,
		deep;
    }
    	
	/**
	 * The QA status types.
	 */
	public enum QAStatusType { 
		
		PASSED_PUBLISH_NO_ACTION_REQUIRED("Passed, Publish, No Action Required"),
		FAILED_DO_NOT_PUBLISH("Failed, Do Not Publish"),
		FAILED_PASS_TO_ENGINEER("Failed, Pass to Engineer"),
		RECRAWL_REQUESTED("Recrawl Requested"),
		ISSUE_NOTED("Issue Noted");
		
        private String value;

        private QAStatusType(String value) {
                this.value = value;
        }
        
        public String getValue() {
        	return value;
        }

    	public static QAStatusType getQaStatus(String name) {
    		for (QAStatusType qaStatusType : QAStatusType.values()) {
    			if (qaStatusType.name().equals(name)) {
        			System.out.println("" + qaStatusType.name() + " - " + qaStatusType.getValue());
    				return qaStatusType;
    			}
    		}
    		return null;
    	}
	}
	
	/**
	 * The QA issue category.
	 */
	public enum QAIssueCategory { 
		APPEARANCE_BACKGROUND_TEXT_COLOUR_AND_FONT,
		APPEARANCE_CHARACTERS,
		APPEARANCE_FORMATTING_OF_THE_PAGE,
		FUNCTIONALITY_LANGUAGE,
		FUNCTIONALITY_ACCESSIBILITY,
		FUNCTIONALITY_NAVIGATION,
		FUNCTIONALITY_MEDIA,
		CONTENT_IMAGES,
		CONTENT_VIDEO,
		CONTENT_DOCUMENTS,
		CONTENT_PAGES_SUB_SECTIONS,
		CONTENT_MENUS,
		OTHER;
    }
    
	/**
	 * The report QA status types.
	 */
	public enum ReportQaStatusType { 
		QAED,
		AWAITINGQA,
		WITHQAISSUES,
		WITHNOQAISSUES,
		FAILEDINSTANCES,
		PASSED,
		WITHQAISSUESRESOLVED;
    }
    
    /**
     * Help collections to read JSON lists like
     * "field_url":[{"url":"http:\/\/www.adoptionuk.org\/"}]
     */
    public static final Map<String, Integer> targetMap = new HashMap<String, Integer>();
		static {
		targetMap.put("field_url", 0);
		targetMap.put("field_description", 1);
		targetMap.put("field_uk_postal_address_url", 2);
		targetMap.put("field_suggested_collections", 3);
		targetMap.put("field_collections", 4);
		targetMap.put("field_license", 5);
		targetMap.put("field_collection_categories", 6);
		targetMap.put("field_notes", 7);
		targetMap.put("field_instances", 8);
	}
	
    public static final Map<String, Integer> targetExportMap = new HashMap<String, Integer>();
		static {
		targetExportMap.put("nid", 0);
		targetExportMap.put("title", 1);
		targetExportMap.put("field_url", 2);
		targetExportMap.put("author", 3);
		targetExportMap.put("field_crawl_frequency", 4);
		targetExportMap.put("created", 5);
	}
		
    public static final Map<String, Integer> permissionExportMap = new HashMap<String, Integer>();
		static {
		permissionExportMap.put("target", 0);
		permissionExportMap.put("licenseDate", 1);
	}
		
    public static final Map<String, Integer> collectionMap = new HashMap<String, Integer>();
    	static {
    	collectionMap.put("field_targets", 0);
    	collectionMap.put("field_sub_collections", 1);
    }
		
    public static final Map<String, Long> statusMap = new HashMap<String, Long>();
    	static {
    	statusMap.put("N/A", 1L);
    	statusMap.put("No QA issues found", 2L);
    	statusMap.put("QA issue", 3L);
    }
		
    public static final Map<Long, String> statusStrMap = new HashMap<Long, String>();
    	static {
    	statusStrMap.put(1L, NA);
    	statusStrMap.put(2L, "No QA issues found");
    	statusStrMap.put(3L, "QA issue");
    }
		
    /**
     * Help collection to read JSON sub nodes like 
     * "field_affiliation":{"uri":"http:\/\/www.webarchive.org.uk\/act\/node\/101","id":"101","resource":"node"}
     */
    public static final Map<String, String> subNodeMap = new HashMap<String, String>();
    	static {
    	subNodeMap.put(AUTHOR, URI);
    	subNodeMap.put(FIELD_AFFILIATION, URI);
    	subNodeMap.put(FIELD_NOMINATING_ORGANISATION, URI);
    	subNodeMap.put(FIELD_SUBJECT, URI);
    	subNodeMap.put(FIELD_QA_STATUS, URI);
    	subNodeMap.put(FIELD_OWNER, URI);
    	subNodeMap.put(PARENT, URI);
    	subNodeMap.put(PARENTS_ALL, URI);
    	subNodeMap.put(FIELD_TARGET, URI);
    	subNodeMap.put(FIELD_QA_ISSUE, URI);
    }

	public enum ScopeCheckType {
		ALL,
		IP,
		DOMAIN;
    }    	
    	
	public enum NpldType {
		UK_HOSTING("UK Hosting"),
		UK_TOP_LEVEL_DOMAIN("UK Top Level Domain"),
		UK_REGISTRATION("UK Registration"),
		UK_POSTAL_ADDRESS("UK Postal Address"),
		VIA_CORRESPONDENCE("Via Correspondance"),
		PROFESSIONAL_JUDGEMENT("Professional Judgement"),
		NO_LD_CRITERIA_MET("No LD Criteria Met");
		
        private String value;

        private NpldType(String value) {
                this.value = value;
        }
        
        public String getValue() {
        	return value;
        }

    }    

    /**
     * DEFINITIONS FOR TESTING  	
     */
    
    // test fields
    public static String FIELD_EMAIL = "email";
    public static String FIELD_PASSWORD = "password";
    
    // test values
    public static String DEFAULT_EMAIL = "ross.king@ait.ac.at";    	
    public static String DEFAULT_PASSWORD = "secret";  
    public static String TEST_ORGANISATIONS_URL = "http://localhost:9000/organisations";
}
