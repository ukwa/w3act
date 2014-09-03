package uk.bl.api;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import models.DCollection;
import models.Instance;
import models.Organisation;
import models.Role;
import models.Target;
import models.Taxonomy;
import models.TaxonomyVocabulary;
import models.User;
import play.Logger;
import play.libs.Json;
import uk.bl.Const;
import uk.bl.Const.NodeType;
import uk.bl.Const.TaxonomyType;
import uk.bl.scope.Scope;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.QueryIterator;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * JSON object management.
 */
public class JsonUtils {

	/**
	 * This method extracts page number from the JSON in order to evaluate first
	 * and last page numbers.
	 * 
	 * @param node
	 * @param field
	 * @return page number as int
	 */
	private static int getPageNumber(JsonNode node, String field) {
		String page = getStringItem(node, field);
		Logger.info("page url: " + page);
		int idxPage = page.indexOf(Const.PAGE_IN_URL)
				+ Const.PAGE_IN_URL.length();
		return Integer.parseInt(page.substring(idxPage));
	}

	/**
	 * This method authenticates Drupal and loads data for particular node type.
	 * 
	 * @param urlStr
	 *            The Drupal data response
	 * @param type
	 *            The node type
	 * @return extracted data
	 */
	private static String authenticateAndLoadDrupal(String urlStr, NodeType type) {
		String res = urlStr;
		try {
			String user = "";
			String password = "";
			Properties customProps = new Properties();
			customProps.load(new FileInputStream(Const.PROJECT_PROPERTY_FILE));
			for (String key : customProps.stringPropertyNames()) {
				String value = customProps.getProperty(key);
				if (key.equals(Const.DRUPAL_USER)) {
					user = value;
				}
				if (key.equals(Const.DRUPAL_PASSWORD)) {
					password = value;
				}
			}
			Logger.info("authenticateAndLoadDrupal() url: " + urlStr);
			HttpBasicAuth.downloadFileWithAuth(urlStr, user, password, type
					.toString().toLowerCase() + Const.OUT_FILE_PATH);
			res = urlStr;
		} catch (IOException e) {
			throw new RuntimeException("Drupal authentification failed: " + e);
		}
		return res;
	}

	/**
	 * This method downloads remote data using HTTP request and retrieves
	 * contentfor passed type.
	 * 
	 * @param urlStr
	 * @param type
	 * @return list of objects
	 */
	private static String downloadData(String urlStr, NodeType type) {
		String res = "";
		if (urlStr != null && urlStr.length() > 0) {
			// aggregate data from drupal and store JSON content in a file
			urlStr = authenticateAndLoadDrupal(urlStr, type);
			// HttpBasicAuth.downloadFileWithAuth(urlStr, Const.AUTH_USER,
			// Const.AUTH_PASSWORD, type.toString().toLowerCase() +
			// Const.OUT_FILE_PATH);
			// read file and store content in String
			res = JsonUtils.readJsonFromFile(type.toString().toLowerCase()
					+ Const.OUT_FILE_PATH);
		}
		return res;
	}

	/**
	 * This method retrieves JSON data from Drupal for particular domain object
	 * type (e.g. Target, Collection...) with parameters e.g.
	 * http://www.webarchive.org.uk/act/node.json?type=organisation&page=0
	 * 
	 * @param type
	 * @return a list of retrieved objects
	 */
	public static List<Object> getDrupalData(NodeType type) {
		List<Object> res = new ArrayList<Object>();
		try {
			String urlStr = Const.URL_STR + type.toString().toLowerCase();
			// aggregate data from drupal and store JSON content in a file
			urlStr = authenticateAndLoadDrupal(urlStr, type);
			// read file and store content in String
			String content = JsonUtils.readJsonFromFile(type.toString()
					.toLowerCase() + Const.OUT_FILE_PATH);
			// extract page information
			JsonNode mainNode = Json.parse(content);
			if (mainNode != null) {
				int firstPage = getPageNumber(mainNode, Const.FIRST_PAGE);
				int lastPage = getPageNumber(mainNode, Const.LAST_PAGE);
				Logger.info("pages: " + firstPage + ", " + lastPage);
				// aggregate data from drupal for all pages
				for (int i = firstPage; i <= lastPage; i++) {
					// if (i == 1) {
					// break; // if necessary for faster testing take only the
					// first page
					// }
					String pageContent = downloadData(urlStr + "&"
							+ Const.PAGE_IN_URL + String.valueOf(i), type);
					List<Object> pageList = JsonUtils.parseJson(pageContent,
							type);
					res.addAll(pageList);
				}
			}
		} catch (Exception e) {
			Logger.info("data aggregation error: " + e);
		}
		Logger.info("list size: " + res.size());
		// int idx = 0;
		// Iterator<Object> itr = res.iterator();
		// while (itr.hasNext()) {
		// Object obj = itr.next();
		// Logger.info("res getDrupalData: " + obj.toString() + ", idx: " +
		// idx);
		// idx++;
		// }
		// int idx = 0;
		Logger.info("res list size: " + res.size());
		if (!type.equals(NodeType.INSTANCE)) {
			Iterator<Object> itr = res.iterator();
			while (itr.hasNext()) {
				if (type.equals(NodeType.URL)) {
					Target obj = (Target) itr.next();
					obj.revision = Const.INITIAL_REVISION;
					obj.active = true;
					obj.selection_type = Const.SelectionType.SELECTION.name();
					if (obj.vid > 0) {
						obj.edit_url = Const.WCT_URL + obj.vid;
					}
					if (obj.language != null && obj.language.equals(Const.UND)) {
						obj.language = "";
					}
		        	/**
		        	 * NPLD scope values
		        	 */
//		        	Logger.debug("calculate NPLD scope for target: " + obj.field_url + ", ID: " + obj.url);
//		        	obj.isInScopeUkRegistrationValue   = Target.isInScopeUkRegistration(obj.field_url);
//		        	Logger.debug("   isInScopeUkRegistrationValue (WhoIs): " + obj.isInScopeUkRegistrationValue);
		        	obj.isInScopeDomainValue           = Target.isInScopeDomain(obj.field_url, obj.url);
		        	Logger.debug("   isInScopeDomainValue (UK_DOMAIN): " + obj.isInScopeDomainValue);
//		        	obj.isUkHostingValue               = Target.checkUkHosting(obj.field_url);
//		        	Logger.debug("   isUkHostingValue (GeoIp): " + obj.isUkHostingValue);
//		        	obj.isInScopeIpValue               = Target.isInScopeIp(obj.field_url, obj.url);;
//		        	Logger.debug("   isInScopeIpValue: (multiple rules WhoIs)" + obj.isInScopeIpValue);
//		        	obj.isInScopeIpWithoutLicenseValue = Target.isInScopeIpWithoutLicense(obj.field_url, obj.url);
//		        	Logger.debug("   isInScopeIpWithoutLicenseValue: " + obj.isInScopeIpWithoutLicenseValue);
				}
				if (type.equals(NodeType.ORGANISATION)) {
					Organisation obj = (Organisation) itr.next();
					if (obj.vid > 0) {
						obj.edit_url = Const.WCT_URL + obj.vid;
					}
					// } else {
					// Object obj = itr.next();
					// Logger.info("itr.next: " + obj + ", idx: " + idx);
				}
				// idx++;
			}
		}
		return res;
	}

	/**
	 * This method retrieves JSON data from Drupal for particular domain object
	 * type (e.g. Curator...) without parameter e.g.
	 * http://www.webarchive.org.uk/act/user.json
	 * 
	 * @param type
	 * @return a list of retrieved objects
	 */
	public static List<Object> getDrupalDataBase(NodeType type) {
		List<Object> res = new ArrayList<Object>();
		try {
			String urlStr = Const.URL_STR_BASE + type.toString().toLowerCase()
					+ Const.JSON;
			// aggregate data from drupal and store JSON content in a file
			urlStr = authenticateAndLoadDrupal(urlStr, type);
			// read file and store content in String
			String content = JsonUtils.readJsonFromFile(type.toString()
					.toLowerCase() + Const.OUT_FILE_PATH);
			// extract page information
			JsonNode mainNode = Json.parse(content);
			if (mainNode != null) {
				int firstPage = getPageNumber(mainNode, Const.FIRST_PAGE);
				int lastPage = getPageNumber(mainNode, Const.LAST_PAGE);
				Logger.info("pages: " + firstPage + ", " + lastPage);
				// aggregate data from drupal for all pages
				for (int i = firstPage; i <= lastPage; i++) {
					// if (i == 1) {
					// break; // if necessary for faster testing take only the
					// first page
					// }
					// String pageContent = downloadData(urlStr + "&" +
					// Const.PAGE_IN_URL + String.valueOf(i), type);
					String pageContent = downloadData(urlStr, type);
					// Logger.info("users content: " + pageContent);
					List<Object> pageList = JsonUtils.parseJson(pageContent,
							type);
					res.addAll(pageList);
				}
			}
		} catch (Exception e) {
			Logger.info("data aggregation error: " + e);
		}
		Logger.info("list size: " + res.size());

		int idx = 0;
		Iterator<Object> itr = res.iterator();
		while (itr.hasNext()) {
			Object obj = itr.next();
			if (type.equals(NodeType.USER)) {
				User newUser = (User) obj;
				if (newUser.email == null || newUser.email.length() == 0) {
					newUser.email = newUser.name.toLowerCase()
							.replace(" ", ".") + "@bl.uk";
				}
				if (newUser.password == null || newUser.password.length() == 0) {
					newUser.password = Const.DEFAULT_PASSWORD;
				}
				// Logger.info("initial password: " + newUser.password);
				if (newUser.password.length() > 0) {
					try {
						newUser.password = PasswordHash
								.createHash(newUser.password);
						// Logger.info("hash password: " + newUser.password);
					} catch (NoSuchAlgorithmException e) {
						Logger.info("initial password creation - no algorithm error: "
								+ e);
					} catch (InvalidKeySpecException e) {
						Logger.info("initial password creation - key specification error: "
								+ e);
					}
				}
//				if (newUser.roles == null || newUser.roles.length() == 0) {
//					newUser.roles = Const.DEFAULT_BL_ROLE;
//				}
				if (newUser.role_to_user == null || newUser.role_to_user.size() == 0) {
					newUser.role_to_user = Role.setDefaultRoleByName(Const.DEFAULT_BL_ROLE);
				}
				newUser.uid = null;
				// Logger.info("id: " + newUser.uid + ", url: " + newUser.url +
				// ", email: " + newUser.email +
				// ", name: " + newUser.name + ", password: " +
				// newUser.password);
			}
			Logger.info("res getDrupalData: " + obj.toString() + ", idx: "
					+ idx);
			idx++;
		}
		Logger.info("res list size: " + res.size());
		return res;
	}

	/**
	 * This method replace textual link in User to Organisation
	 * "field_affiliation" e.g. "BL" by generated Organisation URL e.g.
	 * "act-123".
	 */
	public static void normalizeOrganisationUrlInUser() {
		List<Organisation> organisationList = Organisation.findAll();
		Iterator<Organisation> organisationItr = organisationList.iterator();
		while (organisationItr.hasNext()) {
			Organisation organisation = organisationItr.next();
			List<User> userList = User
					.findByOrganisation(organisation.field_abbreviation);
			Iterator<User> userItr = userList.iterator();
			while (userItr.hasNext()) {
				User user = userItr.next();
				user.field_affiliation = organisation.url;
				user.updateOrganisation();
				Ebean.update(user);
			}
		}
	}

	/**
	 * This method retrieves collections. Due to merging of different original
	 * object models the resulting collection set is evaluated from particular
	 * taxonomy type.
	 * 
	 * @return a list of retrieved collections
	 */
	public static List<Object> readCollectionsFromTaxonomies() {
		List<Object> res = new ArrayList<Object>();
		List<Taxonomy> taxonomyList = Taxonomy
				.findListByType(TaxonomyType.COLLECTION.toString()
						.toLowerCase());
		Iterator<Taxonomy> itr = taxonomyList.iterator();
		while (itr.hasNext()) {
			Taxonomy taxonomy = itr.next();
			if (taxonomy.name != null && taxonomy.name.length() > 0) {
				// check if collection title already in list
				boolean isInList = false;
				Iterator<Object> itrCollection = res.iterator();
				while (itrCollection.hasNext()) {
					DCollection collection = (DCollection) itrCollection.next();
					if (collection.title.equals(taxonomy.name)) {
						isInList = true;
						// replace collection URL in Targets with the existing
						// one
						List<Target> targets = Target
								.findAllByCollectionUrl(taxonomy.url);
						Iterator<Target> itrTargets = targets.iterator();
						while (itrTargets.hasNext()) {
							Target target = itrTargets.next();
							target.field_suggested_collections = collection.url;
							Ebean.update(target);
						}
						break;
					}
				}
				if (!isInList) {
					DCollection collection = new DCollection();
					collection.nid = taxonomy.tid;
					collection.author = taxonomy.field_owner;
					collection.summary = taxonomy.description;
					collection.title = taxonomy.name;
					collection.feed_nid = taxonomy.feed_nid;
					collection.url = taxonomy.url;
					collection.weight = taxonomy.weight;
					collection.node_count = taxonomy.node_count;
					collection.vocabulary = taxonomy.vocabulary;
					collection.field_owner = taxonomy.field_owner;
					collection.field_dates = taxonomy.field_dates;
					if (taxonomy.field_publish != null) {
						collection.publish = Utils
								.getNormalizeBooleanString(taxonomy.field_publish);
					}
					if (taxonomy.parent == null
							|| taxonomy.parent.length() == 0) {
						collection.parent = Const.NONE_VALUE;
					} else {
						collection.parent = taxonomy.parent;
					}
					collection.parents_all = taxonomy.parents_all;
					res.add(collection);
				}
			}
		}
		return res;
	}

	/**
	 * This method aggregates object list from JSON data for particular domain
	 * object type.
	 * 
	 * @param urlStr
	 * @param type
	 * @param taxonomy_type
	 *            The type of taxonomy
	 * @param res
	 */
	private static void aggregateObjectList(String urlStr,
			List<String> urlList, NodeType type, TaxonomyType taxonomy_type,
			List<Object> res) {
		Logger.info("extract data for: " + urlStr + " type: " + type);
		String content = downloadData(urlStr, type);
		JsonNode mainNode = Json.parse(content);
		if (mainNode != null) {
			List<Object> pageList = JsonUtils.parseJsonExt(content, type,
					taxonomy_type, urlList, res);
			res.addAll(pageList);
		}
	}

	/**
	 * This method executes JSON URL request for particular object.
	 * 
	 * @param url
	 *            The current URL
	 * @param urlList
	 *            The list of aggregated URLs (to avoid duplicates)
	 * @param type
	 *            The object type
	 * @param taxonomy_type
	 *            The type of taxonomy
	 * @param res
	 *            Resulting list
	 */
	public static void executeUrlRequest(String url, List<String> urlList,
			NodeType type, TaxonomyType taxonomy_type, List<Object> res) {
		url = getWebarchiveCreatorUrl(url, type);
		String urlStr = url + Const.JSON;
		if (!urlList.contains(urlStr)) {
			urlList.add(urlStr);
			aggregateObjectList(urlStr, urlList, type, taxonomy_type, res);
		}
	}

	/**
	 * This method prepares URLs for JSON URL request.
	 * 
	 * @param fieldName
	 *            Contains one or many URLs separated by comma
	 * @param urlList
	 *            The list of aggregated URLs (to avoid duplicates)
	 * @param type
	 *            The object type
	 * @param taxonomy_type
	 *            The type of taxonomy
	 * @param res
	 *            Resulting list
	 */
	public static void readListFromString(String fieldName,
			List<String> urlList, NodeType type, TaxonomyType taxonomy_type,
			List<Object> res) {
		// Logger.info("fieldName: " + fieldName);
		if (fieldName != null && fieldName.length() > 0) {
			if (fieldName.contains(Const.COMMA)) {
				List<String> resList = Arrays.asList(fieldName
						.split(Const.COMMA));
				Iterator<String> itr = resList.iterator();
				while (itr.hasNext()) {
					executeUrlRequest(itr.next(), urlList, type, taxonomy_type,
							res);
				}
			} else {
				executeUrlRequest(fieldName, urlList, type, taxonomy_type, res);
			}
		}
	}

	/**
	 * This method retrieves secondary JSON data from Drupal for particular
	 * domain object type (e.g. User, Taxonomy ...). The URL to the secondary
	 * JSON data is included in previously aggregated main domain objects (e.g.
	 * link to User is contained in Target).
	 * 
	 * @param type
	 * @return a list of retrieved objects
	 */
	public static List<Object> extractDrupalData(NodeType type) {
		List<Object> res = new ArrayList<Object>();
		try {
			List<String> urlList = new ArrayList<String>();
			List<Target> targets = Target.findAll();
			Iterator<Target> itr = targets.iterator();
			while (itr.hasNext()) {
				Target target = itr.next();
				// String urlStr = "";
				if (type.equals(NodeType.USER)) {
					readListFromString(target.author, urlList, type, null, res);
				}
				if (type.equals(NodeType.TAXONOMY)) {
					readListFromString(target.field_collection_categories,
							urlList, type, TaxonomyType.COLLECTION, res);
					readListFromString(target.field_suggested_collections,
							urlList, type, TaxonomyType.COLLECTION, res);
					readListFromString(target.field_license, urlList, type,
							TaxonomyType.LICENSE, res);
					readListFromString(target.field_subject, urlList, type,
							TaxonomyType.SUBJECT, res);
					readListFromString(target.field_qa_status, urlList, type,
							TaxonomyType.QUALITY_ISSUE, res);
				}
				if (type.equals(NodeType.TAXONOMY_VOCABULARY)) {
					List<Taxonomy> taxonomies = Taxonomy.findAll();
					Iterator<Taxonomy> taxonomyItr = taxonomies.iterator();
					while (taxonomyItr.hasNext()) {
						Taxonomy taxonomy = (Taxonomy) taxonomyItr.next();
						readListFromString(taxonomy.vocabulary, urlList, type,
								null, res);
					}
				}
				// if (type.equals(NodeType.INSTANCE)) {
				// readListFromString(target.field_qa_issues, urlList, type,
				// TaxonomyType.QUALITY_ISSUE, res);
				// }
			}
		} catch (Exception e) {
			Logger.info("data aggregation error: " + e);
		}
		Logger.info("list size: " + res.size());
		int idx = 0;
		Iterator<Object> itr = res.iterator();
		while (itr.hasNext()) {
			Object obj = itr.next();
			Logger.info("res getDrupalData: " + obj.toString() + ", idx: "
					+ idx);
			idx++;
		}
		return res;
	}

	/**
	 * This method extracts multiple items for JSON path
	 * 
	 * @param node
	 * @param path
	 * @return list of String items
	 */
	public static List<String> getStringItems(JsonNode node, String path) {
		List<String> res = new ArrayList<String>();
		JsonNode resNode = getElement(node, path);
		// Logger.info("getStringItems path: " + path + ", resNode: " +
		// resNode);
		if (resNode != null) {
			Iterator<JsonNode> it = resNode.iterator();
			while (it.hasNext()) {
				String fieldName = "";
				JsonNode subNode = it.next();
				if (subNode.has(Const.URI)) {
					fieldName = Const.URI;
				}
				if (subNode.has(Const.URL)) {
					fieldName = Const.URL;
				}
				String item = subNode.findPath(fieldName).textValue();
				if (item != null) {
					res.add(item);
				}
				// Logger.info("subNode: " + subNode + ", path: " + path +
				// ", fieldName: " + fieldName + ", item: " + item + ", res: " +
				// res.size());
			}
		}
		// Logger.info("getStringItems res: " + res);
		return res;
	}

	/**
	 * This method returns list objects from JSON node as a String
	 * 
	 * @param resNode
	 * @param path
	 * @return list as a String
	 */
	public static String getStringList(JsonNode resNode, String path,
			boolean isArchived) {
		String res = "";
		// Logger.info("getStringList path: " + path + ", resNode: " + resNode);
		if (resNode != null) {
			Iterator<JsonNode> it = resNode.iterator();
			while (it.hasNext()) {
				String fieldName = "";
				JsonNode subNode = it.next();
				// Logger.info("subNode: " + subNode);
				if (subNode.has(Const.URI)) {
					fieldName = Const.URI;
				}
				if (subNode.has(Const.URL)) {
					fieldName = Const.URL;
				}
				String item = subNode.findPath(fieldName).textValue();
				if (isArchived) {
					item = normalizeArchiveUrl(item);
				}
				if (item != null) {
					if (res.length() > 0) {
						res = res + "," + item;
					} else {
						res = item;
					}
				}
				// Logger.info("list subNode: " + subNode + ", path: " + path +
				// ", fieldName: " + fieldName + ", item: " + item + ", res: " +
				// res);
			}
		}
		// Logger.info("getStringList res: " + res);
		return res;
	}

	/**
	 * This method checks if given URL is a webarchive url from original Drupal
	 * ACT. If yes it is converted to the new url using NID at the end of the
	 * url e.g. "act-<NID>" If it is an edit URL (ends with "/edit") it is
	 * converted to the WCT URL e.g. "wct-<VID>"
	 * 
	 * @param url
	 * @return identifier URL
	 */
	public static String checkArchiveUrl(String url) {
		// Logger.info("checkArchiveUrl() url: " + url);
		String res = url;
		if (url != null) {
			if (url.contains(Const.WEBARCHIVE_LINK)) {
				if (url.contains(Const.EDIT_LINK)) {
					String root = url.replace(Const.EDIT_LINK, "");
					res = Const.WCT_URL
							+ root.substring(root.lastIndexOf("/") + 1);
				} else {
					res = Const.ACT_URL
							+ url.substring(url.lastIndexOf("/") + 1);
				}
			}
		}
		// if (!url.equals(res)) {
		// Logger.info("checkArchiveUrl() res: " + res);
		// }
		return res;
	}

	/**
	 * This method converts the given W3ACT user URL in a webarchive URL using
	 * NID at the end of the url e.g. "act-<NID>"
	 * 
	 * @param url
	 *            The W3ACT identifier
	 * @return the Webarchive identifier URL
	 * @param type
	 *            The node type
	 * @return
	 */
	public static String getWebarchiveCreatorUrl(String url, NodeType type) {
		String res = url;
		if (url != null) {
			if (url.contains(Const.ACT_URL)) {
				String entryType = "user";
				if (type.equals(NodeType.TAXONOMY)) {
					entryType = "taxonomy_term";
				}
				if (type.equals(NodeType.TAXONOMY_VOCABULARY)) {
					entryType = "taxonomy_vocabulary";
				}
				res = "http://"
						+ Const.WEBARCHIVE_LINK
						+ "/act/"
						+ entryType
						+ "/"
						+ url.substring(url.lastIndexOf(Const.ACT_URL)
								+ Const.ACT_URL.length());
			}
		}
		return res;
	}

	/**
	 * This method returns object from JSON sub node as a String
	 * 
	 * @param resNode
	 * @param path
	 * @return list as a String
	 */
	public static String getStringFromSubNode(JsonNode resNode, String path) {
		String res = "";
		// Logger.info("getStringList path: " + path + ", resNode: " + resNode);
		if (resNode != null) {
			String item = resNode.findPath(path).textValue();
			res = normalizeArchiveUrl(item);
		}
		// Logger.info("getStringFromSubNode res: " + res);
		return res;
	}

	/**
	 * This method extracts one item for JSON field
	 * 
	 * @param node
	 * @param field
	 * @return String item
	 */
	public static String getStringItem(JsonNode node, String fieldName) {
		String res = "";
		JsonNode resNode = getElement(node, fieldName);
		if (resNode != null) {
			if (resNode.isBoolean()) {
				res = resNode.asText();
			} else {
				res = resNode.textValue();
			}
		}
		// Logger.info("getStringItem field name: " + fieldName + ", res: " +
		// res);
		return res;
	}

	/**
	 * This method evaluates element from the root node associated with passed
	 * field name.
	 * 
	 * @param node
	 * @param fieldName
	 * @return sub node
	 */
	public static JsonNode getElement(JsonNode node, String fieldName) {
		JsonNode res = null;
		Iterator<Map.Entry<String, JsonNode>> elt = node.fields();
		while (elt.hasNext()) {
			Map.Entry<String, JsonNode> element = elt.next();
			if (element.getKey().equals(fieldName)) {
				res = element.getValue();
				break;
			}
		}
		return res;
	}

	/**
	 * This method extracts JSON nodes and passes them to parser
	 * 
	 * @param content
	 * @param type
	 * @return object list for particular domain object type
	 */
	public static List<Object> parseJson(String content, NodeType type) {
		List<Object> res = new ArrayList<Object>();
		JsonNode json = Json.parse(content);
		if (json != null) {
			JsonNode rootNode = json.path(Const.LIST_NODE);
			Iterator<JsonNode> ite = rootNode.iterator();
			Logger.info("rootNode elements count is: " + rootNode.size());

			while (ite.hasNext()) {
				JsonNode node = ite.next();
				Object obj = null;
				if (type.equals(Const.NodeType.URL)) {
					obj = new Target();
				}
				if (type.equals(Const.NodeType.COLLECTION)) {
					obj = new DCollection();
				}
				if (type.equals(Const.NodeType.ORGANISATION)) {
					obj = new Organisation();
				}
				if (type.equals(Const.NodeType.INSTANCE)) {
					obj = new Instance();
				}
				if (type.equals(Const.NodeType.USER)) {
					obj = new User();
				}
				parseJsonNode(node, obj);
				res.add(obj);
			}
		} else {
			Logger.info("json is null");
		}
		return res;
	}

	/**
	 * This method extracts JSON node without root node and passes them to
	 * parser
	 * 
	 * @param content
	 * @param type
	 * @param taxonomy_type
	 *            The type of taxonomy
	 * @return object list for particular domain object type
	 */
	public static List<Object> parseJsonExt(String content, NodeType type,
			TaxonomyType taxonomy_type, List<String> urlList,
			List<Object> resList) {
		List<Object> res = new ArrayList<Object>();
		JsonNode node = Json.parse(content);
		if (node != null) {
			Object obj = null;
			if (type.equals(Const.NodeType.USER)) {
				obj = new User();
			}
			if (type.equals(Const.NodeType.TAXONOMY)) {
				obj = new Taxonomy();
			}
			if (type.equals(Const.NodeType.TAXONOMY_VOCABULARY)) {
				obj = new TaxonomyVocabulary();
			}
			parseJsonNodeExt(node, obj, urlList, type, taxonomy_type, res);
			boolean hasEmptyName = false;
			if (type.equals(Const.NodeType.TAXONOMY)) {
				((Taxonomy) obj).ttype = taxonomy_type.toString().toLowerCase();
				if (((Taxonomy) obj).name == null
						|| ((Taxonomy) obj).name.length() == 0) {
					hasEmptyName = true;
				}
				if (((Taxonomy) obj).ttype != null
						&& ((Taxonomy) obj).ttype.equals(Const.LICENCE)) {
					if (((Taxonomy) obj).name != null
							&& ((Taxonomy) obj).name
									.equals(Const.OLD_UKWA_LICENSE)) {
						((Taxonomy) obj).name = Const.NEW_UKWA_LICENSE;
					}
				}
//				((Taxonomy) obj).tid = Utils.createId();
				// Logger.info("taxonomy type: " +
				// taxonomy_type.toString().toLowerCase());
			}
			boolean isNew = true;
			if (type.equals(Const.NodeType.USER)) {
				User newUser = (User) obj;
				if (newUser.email == null || newUser.email.length() == 0) {
					newUser.email = newUser.name.toLowerCase()
							.replace(" ", ".") + "@bl.uk";
				}
				User existingUser = User.findByName(newUser.name);
				if (existingUser != null && existingUser.name.length() > 0) {
					isNew = false;
					existingUser.field_affiliation = newUser.field_affiliation;
					existingUser.updateOrganisation();
					existingUser.uid = newUser.uid;
					existingUser.url = newUser.url;
					existingUser.edit_url = newUser.edit_url;
					existingUser.last_access = newUser.last_access;
					existingUser.last_login = newUser.last_login;
					existingUser.created = newUser.created;
					existingUser.status = newUser.status;
					existingUser.language = newUser.language;
					existingUser.feed_nid = newUser.feed_nid;
					existingUser.update();
				}
			}
			if (isNew && !hasEmptyName) {
				Logger.info("parseJsonExt()" + obj.toString());
				res.add(obj);
			}
		} else {
			Logger.info("json is null");
		}
		return res;
	}

	/**
	 * This method parses String value from JSON and converts it in associated
	 * field type of the object.
	 * 
	 * @param f
	 * @param node
	 * @param obj
	 */
	public static void parseJsonString(Field f, JsonNode node, Object obj) {
		try {
			String jsonField = getStringItem(node, f.getName());
			jsonField = normalizeArchiveUrl(jsonField);
			if (f.getType().equals(String.class)) {
				if (jsonField == null || jsonField.length() == 0) {
					jsonField = "";
				}
				f.set(obj, jsonField);
			}
			if (f.getType().equals(Long.class)) {
				if (jsonField == null || jsonField.length() == 0) {
					jsonField = "0";
				}
				Long jsonFieldLong = new Long(Long.parseLong(jsonField, 10));
				f.set(obj, jsonFieldLong);
			}
			if (f.getType().equals(Boolean.class)) {
				boolean flag = Utils.getNormalizeBooleanString(jsonField);
				Boolean jsonFieldBoolean = new Boolean(flag);
				f.set(obj, jsonFieldBoolean);
			}
		} catch (IllegalArgumentException e) {
			Logger.info("parseJsonString IllegalArgument error: " + e + ", f: "
					+ f);
		} catch (IllegalAccessException e) {
			Logger.info("parseJsonString IllegalAccess error: " + e + ", f: "
					+ f);
		} catch (Exception e) {
			Logger.info("parseJsonString error: " + e + ", f: " + f);
		}
	}

	/**
	 * This method checks if node is a sub node and processes it if that
	 * assumption is true
	 * 
	 * @param f
	 * @param node
	 * @param obj
	 * @return check result
	 */
	private static boolean checkSubNode(Field f, JsonNode node, Object obj,
			List<String> urlList, NodeType type, TaxonomyType taxonomy_type,
			List<Object> resList) {
		boolean res = false;
		if (Const.subNodeMap.containsKey(f.getName())) {
			// if (taxonomy_type != null &&
			// taxonomy_type.equals(TaxonomyType.SUBJECT)) {
			// int ll = 2;
			// }
			res = true;
			JsonNode resNode = getElement(node, f.getName());
			String jsonField = getStringFromSubNode(resNode,
					Const.subNodeMap.get(f.getName()));
			if (f.getName().equals(Const.PARENTS_ALL)) {
				jsonField = getStringList(resNode, f.getName(), true);
			}
			// Logger.info("resNode: " + resNode + ", jsonField: " + jsonField);
			if (urlList != null && type != null
					&& type.equals(NodeType.TAXONOMY)) {
				readListFromString(jsonField, urlList, type, taxonomy_type,
						resList);
			}
			if (f.getType().equals(String.class)) {
				if (jsonField == null || jsonField.length() == 0) {
					jsonField = "";
				}
				try {
					f.set(obj, jsonField);
				} catch (Exception e) {
					Logger.info("checkSubNode: " + e);
				}
			}
		}
		return res;
	}

	/**
	 * This method converts archiving URLs into W3ACT URLs.
	 * 
	 * @param str
	 *            A single URL or multiple URLs separated by comma
	 * @return W3ACT URLs as a string
	 */
	public static String normalizeArchiveUrl(String str) {
		String res = "";
		if (str != null && str.length() > 0) {
			if (str.contains(Const.COMMA)) {
				List<String> resList = Arrays.asList(str.split(Const.COMMA));
				int idx = 0;
				Iterator<String> itr = resList.iterator();
				while (itr.hasNext()) {
					String urlItem = checkArchiveUrl(itr.next());
					if (idx == 0) {
						res = urlItem;
					} else {
						res = res + Const.COMMA + " " + urlItem;
					}
					idx++;
				}
			} else {
				res = checkArchiveUrl(str);
			}
		}
		return res;
	}

	/**
	 * This method parses JSON node and extracts fields
	 * 
	 * @param node
	 * @param obj
	 */
	public static void parseJsonNode(JsonNode node, Object obj) {
		parseJsonNodeExt(node, obj, null, null, null, null);
	}

	/**
	 * This method parses JSON node and extracts fields
	 * 
	 * @param node
	 * @param obj
	 */
	public static void parseJsonNodeExt(JsonNode node, Object obj,
			List<String> urlList, NodeType type, TaxonomyType taxonomy_type,
			List<Object> resList) {
		Field[] fields = obj.getClass().getFields();
		// if (obj.getClass().toString().contains("Taxonomy")) {
		// Logger.info("Taxonomy node: " + node.toString());
		// }
		for (Field f : fields) {
			try {
				if (Const.targetMap.containsKey(f.getName())
						|| Const.collectionMap.containsKey(f.getName())) {
					// if (f.getName().contains("hosting") ||
					// f.getName().contains("domain")) {
					// int ll = 0;
					// }
					JsonNode resNode = getElement(node, f.getName());
					String jsonField = getStringList(resNode, f.getName(),
							false);
					if (!f.getName().equals(
							Const.targetMap.get(Const.FIELD_URL_NODE))) {
						jsonField = normalizeArchiveUrl(jsonField);
					}
					// Logger.info("resNode: " + resNode + ", jsonField: " +
					// jsonField);
					if (f.getName().contains(Const.FIELD_UK_POSTAL_ADDRESS_URL)) {
						if (resNode != null) {
							jsonField = getStringItem(resNode, Const.URL);
						}
					}
					if (f.getType().equals(String.class)) {
						if (jsonField == null || jsonField.length() == 0) {
							jsonField = "";
						}
						f.set(obj, jsonField);
					}
				} else {
					if (f.getName().equals(Const.VALUE) // body elements
							|| f.getName().equals(Const.SUMMARY)
							|| f.getName().equals(Const.FORMAT)) {
						JsonNode resNode = getElement(node, Const.BODY);
						parseJsonString(f, resNode, obj);
					} else {
						if (!checkSubNode(f, node, obj, urlList, type,
								taxonomy_type, resList)) {
							parseJsonString(f, node, obj);
						}
					}
				}
			} catch (IllegalArgumentException e) {
				Logger.info("parseJsonNode IllegalArgument error: " + e
						+ ", f: " + f);
			} catch (IllegalAccessException e) {
				Logger.info("parseJsonNode IllegalAccess error: " + e + ", f: "
						+ f);
			} catch (Exception e) {
				Logger.info("parseJsonNode error: " + e + ", f: " + f);
			}
		}
	}

	/**
	 * This method reads JSON content from a file for given path.
	 * 
	 * @param outFilePath
	 * @return JSON as a String
	 */
	public static String readJsonFromFile(String outFilePath) {
		String res = "";
		try {
			BufferedReader br = new BufferedReader(new FileReader(outFilePath));
			try {
				StringBuilder sb = new StringBuilder();
				String line = br.readLine();

				while (line != null) {
					sb.append(line);
					sb.append('\n');
					line = br.readLine();
				}
				res = sb.toString();
			} finally {
				br.close();
			}
		} catch (FileNotFoundException e) {
			Logger.info("JSON content file not found: " + e.getMessage());
		} catch (IOException e) {
			Logger.info("document path error: " + e.getMessage());
		} catch (Exception e) {
			Logger.info("error: " + e);
		}
		return res;
	}

	/**
	 * This method fills field_url for instance object in order to create
	 * mapping to target object.
	 */
	public static void mapInstancesToTargets() {
		Logger.info("mapInstancesToTargets");
		// List<Instance> instanceList = Instance.findAll();
		QueryIterator<Instance> iter = Instance.getIterator();
		// Logger.info("Instance list size: " + instanceList.size());
		// Iterator<Instance> instanceItr = instanceList.iterator();
		while (iter.hasNext()) {
			// while (instanceItr.hasNext()) {
			// Instance instance = instanceItr.next();
			Instance instance = iter.next();
			// Logger.info("map instance: " + instance.toString());
			if (instance.field_target != null) {
				Logger.info("map instance.field_target: "
						+ instance.field_target);
				Target target = Target.findByUrl(instance.field_target);
				instance.field_url = target.field_url;
				// Logger.info("Instance mapped to Target object: " +
				// instance.field_url);
				Ebean.update(instance);
			}
		}
	}

	/**
	 * This method extracts domain name for Targets.
	 */
	public static void getDomainForTargets() {
		List<Target> targetList = (List<Target>) Target.find.all();
		Iterator<Target> targetItr = targetList.iterator();
		while (targetItr.hasNext()) {
			Target target = targetItr.next();
			if (target.field_url != null) {
				target.domain = Scope.getDomainFromUrl(target.field_url);
				Logger.info("Target domain: " + target.domain
						+ " mapped to Target field URL: " + target.field_url);
				Ebean.update(target);
			}
		}
	}
}
