package uk.bl.api;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;

import models.Collection;
import models.CollectionArea;
import models.FieldUrl;
import models.Instance;
import models.License;
import models.Organisation;
import models.QaIssue;
import models.Role;
import models.Subject;
import models.Tag;
import models.Target;
import models.Taxonomy;
import models.TaxonomyType;
import models.User;
import play.Logger;
import play.Play;
import play.libs.Json;
import uk.bl.Const;
import uk.bl.api.models.FieldModel;
import uk.bl.exception.TaxonomyNotFoundException;
import uk.bl.exception.UrlInvalidException;
import uk.bl.scope.Scope;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * JSON object management.
 */
public enum JsonUtils {

	/**
	 * This method extracts page number from the JSON in order to evaluate first
	 * and last page numbers.
	 * 
	 * @param node
	 * @param field
	 * @return page number as int
	 */
	
	INSTANCE;

	private String getActUrl(String jsonId) {
		return Utils.INSTANCE.getActUrl(jsonId);
	}
	
	private String getWctUrl(String jsonId) {
		return Utils.INSTANCE.getWctUrl(jsonId);
	}
	
	private String getUrlFromWebArchive(String prefix, String url) {
		return new StringBuilder(prefix).append(url.substring(url.lastIndexOf("/") + 1)).toString();
	}
	
	private String getActUrlFromWebArchive(String url) {
		return this.getUrlFromWebArchive(Const.ACT_URL, url);
	}

	private String getWctUrlFromWebArchive(String url) {
		StringBuilder stringBuilder = new StringBuilder(url.replace(Const.EDIT_LINK, ""));
		return this.getUrlFromWebArchive(Const.WCT_URL, stringBuilder.toString());
	}

	private String getAuthenticatedContent(String jsonUrl) throws IOException {
		InputStream in = null;
		try {
			String user = Play.application().configuration().getString(Const.DRUPAL_USER);
			String password = Play.application().configuration().getString(Const.DRUPAL_PASSWORD);
	
	        URL url = new URL(jsonUrl);
	        String authStr = user + ":" + password;
	        String authEncoded = Base64.encodeBytes(authStr.getBytes());
	
	        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
	        connection.setRequestMethod("GET");
	        connection.setDoOutput(true);
	        connection.setRequestProperty("Authorization", "Basic " + authEncoded);
	        in = connection.getInputStream();
		    BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
		    String content = readAll(br);
	        return content;
		} finally {
			if (in != null) {
				in.close();
//				Logger.debug("inputstream closed");
			}
		}
	}
	
	private String readAll(Reader rd) throws IOException {
		return readAll(rd, false);
	}
	
	private String readAll(Reader rd, boolean write) throws IOException {
		
		OutputStream out = null;
		
		if (write) {
			String outFilePath = Const.NodeType.URL.toString().toLowerCase() + Const.OUT_FILE_PATH;
	        File file = new File(outFilePath);
	        out = new BufferedOutputStream(new FileOutputStream(file));
		}
		
		StringBuilder sb = new StringBuilder();
		int cp;
		while ((cp = rd.read()) != -1) {
		  sb.append((char) cp);
		  if (write && out != null) {
			  out.write(cp);
		  }

		}
		if (out != null) {
	        out.close();
		}

        return sb.toString();
	}

	private Date getDateFromSeconds(Long seconds) {
		Date date = null;
		if (seconds != null) {
			date = new Date(seconds*1000L);
		}
		return date;
	}

	private int getPageNumber(JsonNode node, String field) {
		String page = node.get(field).textValue();
		Logger.debug("page url: " + page);
		int idxPage = page.indexOf(Const.PAGE_IN_URL)
				+ Const.PAGE_IN_URL.length();
		return Integer.parseInt(page.substring(idxPage));
	}
	
	public void convertOrganisations() {

		try {
			String jsonUrl = Const.URL_STR + Const.NodeType.ORGANISATION.toString().toLowerCase();
		    String content = this.getAuthenticatedContent(jsonUrl);
		    JsonNode parentNode = Json.parse(content);
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.setSerializationInclusion(Include.NON_NULL);
			if (parentNode != null) {
				
				int firstPage = getPageNumber(parentNode, Const.FIRST_PAGE);
				int lastPage = getPageNumber(parentNode, Const.LAST_PAGE);
				
				Logger.debug("Pages: " + firstPage + "/" + lastPage);
				
				for (int i=firstPage; i<=lastPage; i++) {
					
					StringBuilder targetsUrl = new StringBuilder(jsonUrl).append("&").append(Const.PAGE_IN_URL).append(String.valueOf(i));	
				
					String pageContent = this.getAuthenticatedContent(targetsUrl.toString());
					
					JsonNode mainNode = Json.parse(pageContent.toString());
					
					JsonNode rootNode = mainNode.path(Const.LIST_NODE);
					Iterator<JsonNode> iterator = rootNode.iterator();
					while (iterator.hasNext()) {
						JsonNode node = iterator.next();
//						Logger.debug("json: " + node);
						Organisation organisation = objectMapper.readValue(node.toString(), Organisation.class);
						organisation.url = this.getActUrl(organisation.getNid());
						organisation.edit_url = this.getWctUrl(organisation.vid);
						FieldModel author = organisation.getAuthor();
						if (author != null && StringUtils.isNotBlank(author.getId())) {
							User authorUser = User.findByUrl(this.getActUrl(author.getId()));
							organisation.authorUser = authorUser;
						}
						
						organisation.createdAt = this.getDateFromSeconds(organisation.getCreated());
						organisation.save();
	//					{
	//						"body":[],
	//						"field_abbreviation":"TCD",
	//						"nid":"7404",
	//						"vid":"12953",
	//						"is_new":false,
	//						"type":"organisation",
	//						"title":"Trinity College Dublin",
	//						"language":"en",
	//						"url":"http://www.webarchive.org.uk/act/node/7404",
	//						"edit_url":"http://www.webarchive.org.uk/act/node/7404/edit",
	//						"status":"1","promote":"0","sticky":"0",
	//						"created":"1383558808","changed":"1383558808",
	//						"author":{
	//							"uri":"http://www.webarchive.org.uk/act/user/1",
	//							"id":"1","resource":"user"
	//							},
	//						"log":"",
	//						"revision":null,
	//						"comment":"1",
	//						"comments":[],"comment_count":"0","comment_count_new":"0","feed_nid":null}
	//					}
	
						//Organisation 
	//						[users=[], targets=[], instances=[], value=null, summary=null, format=null, 
	//								field_abbreviation=TCD, 
	//								body=[], nid=7404, vid=12953, is_new=false, type=organisation, title=Trinity College Dublin, language=en, 
	//								edit_url=http://www.webarchive.org.uk/act/node/7404/edit, status=1, promote=0, sticky=0, created=1383558808, changed=1383558808, 
	//									author=Author [uri=http://www.webarchive.org.uk/act/user/1, id=1, resource=user], 
	//									log=, revision=null, comment=1, comments=[], comment_count=0, comment_count_new=0, feed_nid=null]
	
	//					7404;"''";"''";"''";"TCD";12953;FALSE;"organisation";"Trinity College Dublin";"en";"act-7404";"wct-12953";1;0;0;"1383558808";"1383558808";"act-1";"''";1;0;0;"''";0;"2014-11-12 09:35:28.449"
						
						
						Logger.debug("organisation: " + organisation);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}		    
	}
	
	public void convertCurators() {
		
		try {
			String jsonUrl = Const.URL_STR_BASE + Const.NodeType.USER.toString().toLowerCase() + Const.JSON;

		    String content = this.getAuthenticatedContent(jsonUrl);		    
		    JsonNode parentNode = Json.parse(content);
		    
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.setSerializationInclusion(Include.NON_NULL);

			int count = 0;
			if (parentNode != null) {
				int firstPage = getPageNumber(parentNode, Const.FIRST_PAGE);
				int lastPage = getPageNumber(parentNode, Const.LAST_PAGE);
				
				Logger.debug("Pages: " + firstPage + "/" + lastPage);

				for (int i=firstPage; i<=lastPage; i++) {
					
					StringBuilder usersUrl = new StringBuilder(jsonUrl).append("?").append(Const.PAGE_IN_URL).append(String.valueOf(i));	
				
					String pageContent = this.getAuthenticatedContent(usersUrl.toString());
					
					JsonNode mainNode = Json.parse(pageContent.toString());
					
					JsonNode rootNode = mainNode.path(Const.LIST_NODE);
					Iterator<JsonNode> iterator = rootNode.iterator();
					while (iterator.hasNext()) {
						Logger.debug(count + ") ");
						JsonNode node = iterator.next();
						
	//					"field_affiliation":
	//					{
	//						"uri":"http://www.webarchive.org.uk/act/node/102","id":"102","resource":"node"
	//					},
	//					"uid":"279","name":"Aled Betts",
	//					"url":"http://www.webarchive.org.uk/act/user/279",
	//					"edit_url":"http://www.webarchive.org.uk/act/user/279/edit","created":"1409663132","language":"en","feed_nid":null
		
	//					User [
	//					      organisation=null, roles=[], email=null, password=null, name=Aled Betts, fieldAffiliation=null, edit_url=null, last_access=null, last_login=null, status=null, language=null, feed_nid=null, 
	//					      field_affiliation=null, uid=279, created=1409663132, mail=null, revision=, id=null, url=http://www.webarchive.org.uk/act/user/279, createdAt=null, updatedAt=null
	//					]
								
//						Logger.debug("json: " + node);
						User user = objectMapper.readValue(node.toString(), User.class);
						
						// TODO: KL ANONYMOUS USER?
						if (user.name.toLowerCase().equals("anonymous")) {
							continue;
						}
						if (StringUtils.isEmpty(user.email)) {
							user.email = user.name.toLowerCase().replace(" ", ".") + "@bl.uk";
						}

						user.password = UUID.randomUUID().toString();

						if (user.password.length() > 0) {
							try {
								user.password = PasswordHash.createHash(user.password);
							} catch (NoSuchAlgorithmException e) {
								Logger.error("initial password creation - no algorithm error: " + e);
							} catch (InvalidKeySpecException e) {
								Logger.error("initial password creation - key specification error: " + e);
							}
						}
						if (user.roles == null || user.roles.isEmpty()) {
							user.roles = Role.setRoleByName(Const.DEFAULT_ROLE);
						}
						user.url = this.getActUrlFromWebArchive(user.url);
						user.edit_url = this.getWctUrlFromWebArchive(user.edit_url);
						user.createdAt = this.getDateFromSeconds(user.getCreated());
						FieldModel fieldAffiliation = user.getField_affiliation();
						if (fieldAffiliation!= null) {
							if (StringUtils.isNotEmpty(fieldAffiliation.getUri())) {
								// TODO: DO WE NEED AFFILIATION? - USE ORGANISATION ID?
	//							user.affiliation = this.checkArchiveUrl(fieldAffiliation.getUri());
								user.affiliation = this.getActUrl(fieldAffiliation.getId());
								Organisation organisation = Organisation.findByUrl(user.affiliation);
								user.organisation = organisation;
							}
						}
						user.save();
						Logger.debug("user: " + user);
						count++;
					}
				}
			}
			Logger.debug("No of Curators: " + count);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void convertTaxonomyVocabulary() {
//		http://www.webarchive.org.uk/act/taxonomy_vocabulary.json?page=0
		
//		{"self":"http:\/\/webarchive.org.uk\/act\/taxonomy_vocabulary?page=0","first":"http:\/\/webarchive.org.uk\/act\/taxonomy_vocabulary?page=0","last":"http:\/\/webarchive.org.uk\/act\/taxonomy_vocabulary?page=0","list":[

//		http://www.webarchive.org.uk/act/taxonomy_term.json
		try {

			String jsonUrl = Const.URL_STR_BASE + TaxonomyType.TAXONOMY_VOCABULARY + Const.JSON;

			Logger.debug("act url: " + jsonUrl);
			
		    String content = this.getAuthenticatedContent(jsonUrl);		    
		    JsonNode parentNode = Json.parse(content);
		    
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.setSerializationInclusion(Include.NON_NULL);

			int count = 0;
			if (parentNode != null) {
				
				int firstPage = getPageNumber(parentNode, Const.FIRST_PAGE);
				int lastPage = getPageNumber(parentNode, Const.LAST_PAGE);
				
				Logger.debug("Pages: " + firstPage + "/" + lastPage);

				for (int i=firstPage; i<=lastPage; i++) {
					
					StringBuilder url = new StringBuilder(jsonUrl).append("?").append(Const.PAGE_IN_URL).append(String.valueOf(i));	
				
					String pageContent = this.getAuthenticatedContent(url.toString());
					
					JsonNode mainNode = Json.parse(pageContent.toString());
					
					JsonNode rootNode = mainNode.path(Const.LIST_NODE);
					Iterator<JsonNode> iterator = rootNode.iterator();
					while (iterator.hasNext()) {
						Logger.debug(count + ") ");
						JsonNode node = iterator.next();

//						{"vid":"5","name":"Web Archive Collections","machine_name":"collections","description":"Taxonomy for structuring collections.","term_count":"160"},
//						{"vid":"6","name":"Collection Areas","machine_name":"collection_areas","description":"Taxonomy for linking up with BL Collection Strategy subject terms.","term_count":"37"},
//						{"vid":"4","name":"Licenses","machine_name":"licenses","description":"List of licenses under which content has been archived.","term_count":"2"},
//						{"vid":"7","name":"Quality Issues","machine_name":"quality_issues","description":"Taxonomy used to categorise QA issues.","term_count":"3"},
//						{"vid":"2","name":"Subject","machine_name":"subject","description":"Subject tags","term_count":"76"},
//						{"vid":"1","name":"Tags","machine_name":"tags","description":"Use tags to group articles on similar topics into categories.","term_count":"0"}

//						Logger.debug("json: " + node);
						TaxonomyType taxonomyType = objectMapper.readValue(node.toString(), TaxonomyType.class);

						ObjectMapper mapper = new ObjectMapper();
						mapper.setSerializationInclusion(Include.NON_NULL);

						Logger.debug("taxonomy: " + taxonomyType);
						
						// find to see if it's stored already
						
						TaxonomyType lookup = TaxonomyType.findByVid(taxonomyType.getVid());
						
						Logger.debug("lookup: " + lookup + " using " + taxonomyType.getVid());

						if (lookup == null) {
							taxonomyType.save();
						}
						
						Logger.debug("taxonomyType: " + taxonomyType);
						count++;
					}
				}
			}
			Logger.debug("No of Taxonomies: " + count);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private TaxonomyType getTaxonomyType(Taxonomy taxonomy) throws IOException {
		FieldModel fmTaxVocab = taxonomy.getVocabularyValue();
//		Logger.debug("TaxonomyType: " + fmTaxVocab.getId());
		Long vid = Long.valueOf(fmTaxVocab.getId());
		TaxonomyType taxonomyType = TaxonomyType.findByVid(vid);
		return taxonomyType;
	}
	
	public void convertTaxonomies() {
//		http://www.webarchive.org.uk/act/taxonomy_term.json
		try {

			String jsonUrl = Const.URL_STR_BASE + Taxonomy.TAXONOMY_TERM + Const.JSON;

			Logger.debug("act url: " + jsonUrl);

		    String content = this.getAuthenticatedContent(jsonUrl);		    
		    JsonNode parentNode = Json.parse(content);
		    
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.setSerializationInclusion(Include.NON_NULL);

			int count = 0;
			if (parentNode != null) {
				int firstPage = getPageNumber(parentNode, Const.FIRST_PAGE);
				int lastPage = getPageNumber(parentNode, Const.LAST_PAGE);
				
				Logger.debug("Pages: " + firstPage + "/" + lastPage);

				for (int i=firstPage; i<=lastPage; i++) {
					
					StringBuilder url = new StringBuilder(jsonUrl).append("?").append(Const.PAGE_IN_URL).append(String.valueOf(i));	
				
					String pageContent = this.getAuthenticatedContent(url.toString());
					
					JsonNode mainNode = Json.parse(pageContent.toString());
					
					JsonNode rootNode = mainNode.path(Const.LIST_NODE);
					Iterator<JsonNode> iterator = rootNode.iterator();
					while (iterator.hasNext()) {
						Logger.debug(count + ") ");
						JsonNode node = iterator.next();
						// TODO: KL WHICH IS COLLECTION, SUBJECT, ETC?
//						"field_owner":[],
//						"field_dates":[],
//						"field_publish":null,
//						"tid":"170",
//						"name":"100 Best Sites",
//						"description":"\u003Cp\u003EA list of 100 websites drawn up by the BL Press Office as part of publicity surrounding Non-Print Legal Deposit Legislation which came into force in April 2013.\u003C\/p\u003E\n",
//						"weight":"0",
//						"node_count":10,
//						"url":"http:\/\/webarchive.org.uk\/act\/taxonomy\/term\/170",
//						"vocabulary":{
//							"uri":"http:\/\/webarchive.org.uk\/act\/taxonomy_vocabulary\/5","id":"5","resource":"taxonomy_vocabulary"
//						},
//						"parent":[],
//						"parents_all":[{"uri":"http:\/\/webarchive.org.uk\/act\/taxonomy_term\/170","id":"170","resource":"taxonomy_term"}],
//						"feed_nid":null
						
//						Logger.debug("json: " + node);
						
						String row = node.toString();
						// just read in the Taxonomy Vocabulary???
						Taxonomy taxonomy = objectMapper.readValue(row, Taxonomy.class);

						// find to see if it's stored already
						String actUrl = this.getActUrl(taxonomy.getTid());
						Taxonomy lookup = Taxonomy.findByUrl(actUrl);
						
						Logger.debug("lookup: " + lookup + " using " + taxonomy.url);

						if (lookup == null) {
							
							// "vocabulary":{"uri":"http:\/\/www.webarchive.org.uk\/act\/taxonomy_vocabulary\/5","id":"5","resource":"taxonomy_vocabulary"},
							taxonomy = this.convertTaxonomy(taxonomy, row, objectMapper);
							taxonomy.save();
						}
						
//						"field_owner":[{"uri":"http:\/\/www.webarchive.org.uk\/act\/user\/9","id":"9","resource":"user"}],
//						"field_dates":{"value":"1396310400","value2":"1404086400","duration":7776000},
//						"field_publish":true,
//						"tid":"250",
//						"name":"European Parliament Elections 2014","description":"",
//						"weight":"0",
//						"node_count":10,
//						"url":"http:\/\/www.webarchive.org.uk\/act\/taxonomy\/term\/250",
//						"vocabulary":{"uri":"http:\/\/www.webarchive.org.uk\/act\/taxonomy_vocabulary\/5","id":"5","resource":"taxonomy_vocabulary"},
//						"parent":[],
//						"parents_all":[{"uri":"http:\/\/www.webarchive.org.uk\/act\/taxonomy_term\/250","id":"250","resource":"taxonomy_term"}],"feed_nid":null}

						Logger.debug("taxonomy: " + taxonomy);
						count++;
					}
				}
			}
			Logger.debug("No of Taxonomies: " + count);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private Taxonomy getTaxonomyClassSubType(TaxonomyType taxonomyType, ObjectMapper objectMapper, String row) throws JsonParseException, JsonMappingException, IOException {
		Taxonomy taxonomy = null;
		String machineName = taxonomyType.getMachine_name();
		Logger.debug("machineName: " + machineName);
		switch (machineName.toLowerCase()) {
			case TaxonomyType.COLLECTION:
				taxonomy = objectMapper.readValue(row, Collection.class);
				break;
			case TaxonomyType.LICENSES:
				taxonomy = objectMapper.readValue(row, License.class);
				break;
			case TaxonomyType.QUALITY_ISSUES:
				taxonomy = objectMapper.readValue(row, QaIssue.class);
				break;
			case TaxonomyType.SUBJECT:
				taxonomy = objectMapper.readValue(row, Subject.class);
				break;
			case TaxonomyType.COLLECTION_AREAS:
				taxonomy = objectMapper.readValue(row, CollectionArea.class);
				break;
			case TaxonomyType.TAGS:
				taxonomy = objectMapper.readValue(row, Tag.class);
				break;
			default: 
				Logger.debug("Default hit");
				break;
		}
		Logger.debug("Taxonomy Type: " + taxonomy.getClass());
		return taxonomy;
	}
	
	private void convertTaxonomyOwners(List<FieldModel> fieldOwners, List<User> taxonomyOwners) {
		for (FieldModel fieldOwner : fieldOwners) {
			String fieldActUrl = this.getActUrl(fieldOwner.getId());
			User owner = User.findByUrl(fieldActUrl);
			taxonomyOwners.add(owner);
		}
	}

	private Taxonomy convertParents(List<FieldModel> fieldParents, Taxonomy taxonomy, ObjectMapper objectMapper) throws IOException {
		Logger.debug("fieldParents: " + fieldParents);
		if (fieldParents != null && !fieldParents.isEmpty()) {
			if (fieldParents.size() > 1) {
				try {
					throw new TaxonomyNotFoundException("fieldParents: " + fieldParents);
				} catch (TaxonomyNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			FieldModel parentFieldModel = fieldParents.get(0);
			String actUrl = this.getActUrl(parentFieldModel.getId());
			Taxonomy parentTaxonomy = Taxonomy.findByUrl(actUrl);
			if (parentTaxonomy == null) {
				parentTaxonomy = this.convertFieldTaxonomy(parentFieldModel, objectMapper);
				Logger.debug("parentTaxonomy TYPE: " + parentTaxonomy.getClass());
				parentTaxonomy.save();
			}
			Logger.debug("parentFieldModel: " + parentFieldModel + " - " + parentTaxonomy);
			taxonomy.parent = parentTaxonomy;
		}
		return taxonomy;
	}

	private Taxonomy convertParentsAll(List<FieldModel> fieldParentsAll, Taxonomy taxonomy, ObjectMapper objectMapper) throws IOException {
		// TODO: KL parents_all doesn't seem to be used by views/controllers
		for (FieldModel parentAllFieldModel : fieldParentsAll) {
			String actUrl = this.getActUrl(parentAllFieldModel.getId());
			// save so we don't save twice because of parent
			Taxonomy parentAllTaxonomy = Taxonomy.findByUrl(actUrl);
			// pass node?
			if (parentAllTaxonomy == null) {
				parentAllTaxonomy = this.convertFieldTaxonomy(parentAllFieldModel, objectMapper);
				Logger.debug("parentAllTaxonomy TYPE: " + parentAllTaxonomy.getClass());
				parentAllTaxonomy.save();
			}
			taxonomy.parentsAllList.add(parentAllTaxonomy);
		}
		return taxonomy;
	}
	
	private Taxonomy convertTaxonomy(Taxonomy taxonomy, String row, ObjectMapper objectMapper) throws IOException {
		// "vocabulary":{"uri":"http:\/\/www.webarchive.org.uk\/act\/taxonomy_vocabulary\/5","id":"5","resource":"taxonomy_vocabulary"},
		TaxonomyType taxonomyType = this.getTaxonomyType(taxonomy);
		// to get the correct Taxonomy Instance
		taxonomy = this.getTaxonomyClassSubType(taxonomyType, objectMapper, row);
		
//		taxonomy.id = Long.valueOf(taxonomy.getTid());
		taxonomy.url = this.getActUrl(taxonomy.getTid());
		
		taxonomy.setTaxonomyType(taxonomyType);
		
		// ownerUsers
		if (taxonomy.getField_owner() != null) {
			// "field_owner":[{"uri":"http:\/\/www.webarchive.org.uk\/act\/user\/9","id":"9","resource":"user"}],
			this.convertTaxonomyOwners(taxonomy.getField_owner(), taxonomy.getOwnerUsers());
		}
		
		// save first so we can use the ID - so we don't save twice because of parent
		taxonomy.save();
	
		// "parent":[],
		taxonomy = this.convertParents(taxonomy.getParentFieldList(), taxonomy, objectMapper);
		// "parents_all":[{"uri":"http:\/\/www.webarchive.org.uk\/act\/taxonomy_term\/250","id":"250","resource":"taxonomy_term"}],"feed_nid":null}
		taxonomy = this.convertParentsAll(taxonomy.getParents_all(), taxonomy, objectMapper);
		return taxonomy;
	}
	
	private Taxonomy convertFieldTaxonomy(FieldModel fieldModel, ObjectMapper objectMapper) throws IOException {
		StringBuilder url = new StringBuilder(fieldModel.getUri()).append(Const.JSON);
		String content = this.getAuthenticatedContent(url.toString());
		
		Taxonomy taxonomy = objectMapper.readValue(content, Taxonomy.class);
		
		Logger.debug("taxonomy url: " + url);
//		Logger.debug("taxonomy content: " + content);

		taxonomy = this.convertTaxonomy(taxonomy, content, objectMapper);
		
		return taxonomy;
	}
	
	// some specific pre-pending
	// check if valid url 
	private String validateUrl(String url) throws UrlInvalidException {
		if (url.startsWith("at ")) {
			url = url.replace("at ", "");
		}
		else if (url.startsWith("www.")) {
			url = "http://" + url;
		}
		else if (url.startsWith("ttp")) {
			url = url.replace("ttp", "");
			url = "http" + url;
		}
		else if (url.startsWith("ttps")) {
			url = url.replace("ttps", "");
			url = "https" + url;
		}
		else if (!url.startsWith("http")) {
			url = "http://" + url;
		}
		url = url.replaceAll(" ", "%20");
		
//		UrlValidator urlValidator = new UrlValidator(UrlValidator.ALLOW_ALL_SCHEMES);
//	    if (!urlValidator.isValid(url)) {
//	    	if (!url.endsWith(Scope.UK_DOMAIN) || !url.endsWith(Scope.SCOT_DOMAIN) || !url.endsWith(Scope.LONDON_DOMAIN)) {
//	    		throw new UrlInvalidException("Something wrong with this url: " + url);
//	    	}
//	    }
		return url;
	}
	
	@SuppressWarnings("unchecked")
	public void convertTargets() {
		
		try {
			// TODO: CHECK JSON URL
			String jsonUrl = Const.URL_STR + Const.NodeType.URL.toString().toLowerCase();
			String content = this.getAuthenticatedContent(jsonUrl);

			JsonNode parentNode = Json.parse(content);
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.setSerializationInclusion(Include.NON_DEFAULT);
			
			if (parentNode != null) {
				
				int firstPage = getPageNumber(parentNode, Const.FIRST_PAGE);
				int lastPage = getPageNumber(parentNode, Const.LAST_PAGE);
				
				Logger.debug("Pages: " + firstPage + "/" + lastPage);

				int count = 0;
				
				for (int i=firstPage; i<=lastPage; i++) {
					
					StringBuilder targetsUrl = new StringBuilder(jsonUrl).append("&").append(Const.PAGE_IN_URL).append(String.valueOf(i));
					
//					Logger.debug("targets url: " + targetsUrl);

					String pageContent = this.getAuthenticatedContent(targetsUrl.toString());
					
					JsonNode mainNode = Json.parse(pageContent.toString());
					
					JsonNode rootNode = mainNode.path(Const.LIST_NODE);
					Iterator<JsonNode> iterator = rootNode.iterator();
					while (iterator.hasNext()) {
						JsonNode node = iterator.next();
//						Logger.debug("json: " + node.toString());
						
						Target target = objectMapper.readValue(node.toString(), Target.class);
	
						target.url = this.getActUrl(target.getNid());
						target.edit_url = this.getWctUrl(target.vid);
						target.createdAt = this.getDateFromSeconds(target.getCreated());
						
						FieldModel qaIssueField = target.getField_qa_status();
						try {
							if (qaIssueField != null) {
								QaIssue qaIssue = this.getQaIssue(qaIssueField);
								target.qaIssue = qaIssue;
							}
						} catch (TaxonomyNotFoundException tnfe) {
							tnfe.printStackTrace();
						}

						// {"body":{"value":"<p>UK address on contacts page</p>\n", "summary":"", "format":"plain_text"},
						if (target.getBody() instanceof LinkedHashMap) {
							Map<String, String> bodyMap = (LinkedHashMap<String,String>)target.getBody();
							String value = bodyMap.get("value");
							String format = bodyMap.get("format");
							String summary = bodyMap.get("summary");
							if (StringUtils.isNotEmpty(value)) {
								target.value = value;
							}
							if (StringUtils.isNotEmpty(format)) {
								target.format = format;
							}
							if (StringUtils.isNotEmpty(summary)) {
								target.summary = summary;
							}
						}
						
						// "field_url":[{"url":"http://www.childrenslegalcentre.com/"}],
						List<FieldUrl> fieldUrls = new ArrayList<FieldUrl>();
						for (Map<String,String> map : target.getField_url()) {
							String url = map.get("url");
							try {
								url = validateUrl(url);
								Logger.debug("Checked Url: " + url);
								FieldUrl fieldUrl = new FieldUrl(url);
								fieldUrl.domain = Scope.INSTANCE.getDomainFromUrl(fieldUrl.url);
								fieldUrls.add(fieldUrl);
							} catch (UrlInvalidException e) {
								// SKIP BAD URLS
								e.printStackTrace();
							}
						}
						
						if (!fieldUrls.isEmpty()) {
							target.fieldUrls = fieldUrls;
//							As for the multiple URL case, the logic should be that there is only one 
//							"UK Hosting", 
//							"UK top-level domain", 
//							"UK Registration" 
//							field, but all URLs have to meet it to get a tick.
//							e.g. if all URLs end '.uk' then "UK top-level domain" is true.
//							field_uk_hosting
//							target.isUkHosting = Scope.INSTANCE.isUkHosting(fieldUrls); // check if UK IP Address
//							field_uk_domain
//							target.isTopLevelDomain = Scope.INSTANCE.isTopLevelDomain(fieldUrls);
//							field_uk_geoip
//							target.isUkRegistration = Scope.INSTANCE.isUkRegistration(fieldUrls);
//							Logger.debug(target.isUkHosting + ", " + target.isTopLevelDomain + ", " + target.isUkRegistration);
							// .uk .london .scot
						}

						// uk hosting
						target.isUkHosting = BooleanUtils.toBoolean(target.getField_uk_geoip());
						
						// top level domain
						target.isTopLevelDomain = BooleanUtils.toBoolean(target.getField_uk_domain());

						target.isInScopeIp = false;
						target.isInScopeIpWithoutLicense = false;

						
						// "field_subject":{"uri":"http://www.webarchive.org.uk/act/taxonomy_term/10","id":"10","resource":"taxonomy_term"},
						// "field_subject":{"uri":"http://www.webarchive.org.uk/act/taxonomy_term/16","id":"16","resource":"taxonomy_term"},"
						FieldModel fieldSubject = target.getField_subject();
						if (fieldSubject != null) {
							try {
								Subject subject = this.getSubject(fieldSubject);
								target.subjects.add(subject);
							} catch (TaxonomyNotFoundException tnfe) {
								tnfe.printStackTrace();
							}
						}
						
						// "field_description":[],
						if (target.getField_description() != null && target.getField_description() instanceof Map) {
							Map<String, String> fieldDescription = (Map<String, String>)target.getField_description();
							String value = fieldDescription.get("value");
							String summary = fieldDescription.get("summary");
							if (StringUtils.isNotEmpty(value)) {
								target.description = value;
							}
							
							if (StringUtils.isNotEmpty(summary)) {
								// populate if not already filled in by body->value
								if (StringUtils.isEmpty(target.summary)) {
									target.summary = summary;
								}
							}
						}
						
						// "field_uk_postal_address_url":{"url":"http://www.childrenslegalcentre.com/index.php?page=contact_us"},
						if (target.getField_uk_postal_address_url() != null && target.getField_uk_postal_address_url() instanceof Map) {
							Map<String,String> postalAddressUrl = (Map<String,String>)target.getField_uk_postal_address_url();
							String url = postalAddressUrl.get("url");
							target.ukPostalAddressUrl = url;
						}
	
						// "field_nominating_organisation":{"uri":"http://www.webarchive.org.uk/act/node/101","id":"101","resource":"node"},
						FieldModel fieldNominatingOrganisation = target.getField_nominating_organisation();
						if (fieldNominatingOrganisation != null && StringUtils.isNotEmpty(fieldNominatingOrganisation.getId())) {
							String orgUrl = this.getActUrl(fieldNominatingOrganisation.getId());
							if (StringUtils.isNotEmpty(orgUrl)) {
								target.organisation = Organisation.findByUrl(orgUrl);
							}
						}
						// some are dead nodes
						// "field_suggested_collections":[{"uri":"http://www.webarchive.org.uk/act/node/2013","id":"2013","resource":"node"}],
						// "field_suggested_collections":[{"uri":"http://webarchive.org.uk/act/node/108","id":"108","resource":"node"}]
						// KL UNUSED
//						if (target.getField_suggested_collections() != null) {
//							List<FieldModel> suggestCollections = target.getField_suggested_collections();
//							
//							for (FieldModel fieldModel : suggestCollections) {
//								try {
//									Collection collection = this.convertCollection(fieldModel);
//									
//									if (collection != null) {
//										target.collections.add(collection);
//									}
//								} catch (IOException e) {
//									Logger.error("Move onto the next Collection: " + e.getMessage());
//								}
//							}
//							
//						}

						// "field_collections":[],
						// KL UNUSED
//						if (target.getField_collections() != null) {
//							List<FieldModel> collections = target.getField_collections();
//							for (FieldModel fieldModel : collections) {
//								try {
//									Collection collection = this.convertCollection(fieldModel);
//									
//									if (collection != null) {
//										target.collections.add(collection);
//									}
//								} catch (IOException e) {
//									Logger.error("Move onto the next Collection: " + e.getMessage());
//								}
//							}
//						}
						
						// "field_license":[{"uri":"http://www.webarchive.org.uk/act/taxonomy_term/168","id":"168","resource":"taxonomy_term"}],
						if (target.getField_license() != null) {
							List<FieldModel> licenses = target.getField_license();
							for (FieldModel fieldModel : licenses) {
								try {
									License license = this.getLicense(fieldModel);
									if (license != null) {
										target.licenses.add(license);
									}
								} catch (TaxonomyNotFoundException tnfe) {
									tnfe.printStackTrace();
								}
							}
						}
						
						// "field_collection_categories":[{"uri":"http://www.webarchive.org.uk/act/taxonomy_term/160","id":"160","resource":"taxonomy_term"}],
						if (target.getField_collection_categories() != null) {
							List<FieldModel> collectionCategories = target.getField_collection_categories();
							
							for (FieldModel fieldModel : collectionCategories) {
								try {
									Collection collection = this.getCollection(fieldModel);
									if (collection != null) {
										target.collections.add(collection);
									}
								} catch (TaxonomyNotFoundException tnfe) {
									tnfe.printStackTrace();
								}
							}
						}
						
						// "field_crawl_start_date":null,
						if (target.getField_crawl_start_date() != null) {
							target.crawlStartDate = getDateFromSeconds(target.getField_crawl_start_date());
							Logger.debug(target.id + " - " + target.title + " - " + target.crawlStartDate);
						}
						// "field_crawl_end_date":null,
						if (target.getField_crawl_end_date() != null) {
							target.crawlEndDate = getDateFromSeconds(target.getField_crawl_end_date());
						}
						
						// "field_notes":{"value":"<p>There are missing documents in this gather for example <a href=\"https://www.wjec.co.uk/uploads/publications/16330.pdf\">https://www.wjec.co.uk/uploads/publications/16330.pdf</a>. It could be a capping issue. I have taken off the cap.</p>\n","format":"filtered_html"}
						if (target.getField_notes() != null && target.getField_notes() instanceof Map) {
							Map<String,String> fieldNotes = (Map<String,String>)target.getField_notes();
							String value = fieldNotes.get("value");
							target.notes = value;
						}
						
						// "field_snapshots":[{"uri":"http://www.webarchive.org.uk/act/field_collection_item/6","id":"6","resource":"field_collection_item"}]
						if (target.getField_snapshots() != null) {
							List<FieldModel> fieldSnapshots = target.getField_snapshots();
							List<String> fieldSnapshotsList = new ArrayList<String>();
							for (FieldModel fieldModel : fieldSnapshots) {
								String actUrl = this.getActUrl(fieldModel.getId());
								fieldSnapshotsList.add(actUrl);
							}
							if (!fieldSnapshotsList.isEmpty()) {
								// TODO: KL COLLECTIONS?
//								target. = StringUtils.join(fieldSnapshotsList, ", ");
							}					
						}
						
						// "field_instances":[],
						target.getField_instances();

						// "author":{"uri":"http://www.webarchive.org.uk/act/user/9","id":"9","resource":"user"},
						FieldModel author = target.getAuthor();
						if (author != null) {
							User authorUser = User.findByUrl(this.getActUrl(author.getId()));
							target.authorUser = authorUser;
						}

//						"comments":[],
						
						target.revision = Const.INITIAL_REVISION;
						target.active = true;
						target.selectionType = Const.SelectionType.SELECTION.name();
						if (StringUtils.isNotBlank(target.language) && target.language.equals(Const.UND)) {
							target.language = null;
						} else {
							target.language = target.language.toUpperCase();
						}
	
						if (target.noLdCriteriaMet == null) {
							target.noLdCriteriaMet = false;
						}
	
						if (target.keySite == null) {
							target.keySite = false;
						}
						
						if (target.ignoreRobotsTxt == null) {
							target.ignoreRobotsTxt = false;
						}
						
						if (StringUtils.isNotEmpty(target.liveSiteStatus)) {
							target.liveSiteStatus = target.liveSiteStatus.toUpperCase();
						}
						
						target.crawlFrequency = target.crawlFrequency.toUpperCase();
						target.depth = target.depth.toUpperCase();
						
						target.createdAt = this.getDateFromSeconds(target.getCreated());

			        	target.save();
			        	count++;
					}
				}

			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		

	}
	
	@SuppressWarnings("unchecked")
	public void convertInstances() {
		try {
			// TODO: CHECK JSON URL
			String jsonUrl = Const.URL_STR + Const.NodeType.INSTANCE.toString().toLowerCase();
			String content = this.getAuthenticatedContent(jsonUrl);

			JsonNode parentNode = Json.parse(content);
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.setSerializationInclusion(Include.NON_DEFAULT);
			
			if (parentNode != null) {
				
				int firstPage = getPageNumber(parentNode, Const.FIRST_PAGE);
				int lastPage = getPageNumber(parentNode, Const.LAST_PAGE);
				
				Logger.debug("Pages: " + firstPage + "/" + lastPage);

				int count = 0;
				
				for (int i=firstPage; i<=lastPage; i++) {
					
					StringBuilder instanceUrl = new StringBuilder(jsonUrl).append("&").append(Const.PAGE_IN_URL).append(String.valueOf(i));
					String pageContent = this.getAuthenticatedContent(instanceUrl.toString());
					JsonNode mainNode = Json.parse(pageContent.toString());
					
					JsonNode rootNode = mainNode.path(Const.LIST_NODE);
					Iterator<JsonNode> iterator = rootNode.iterator();
					while (iterator.hasNext()) {
						JsonNode node = iterator.next();
						
						Instance instance = objectMapper.readValue(node.toString(), Instance.class);
	
						instance.url = this.getActUrl(instance.getNid());
						instance.edit_url = this.getWctUrl(instance.vid);
						instance.createdAt = this.getDateFromSeconds(instance.getCreated());
						
//						"author":{"uri":"http://webarchive.org.uk/act/user/80","id":"80","resource":"user"}
						FieldModel author = instance.getAuthor();
						if (author != null) {
							User authorUser = User.findByUrl(this.getActUrl(author.getId()));
//							Logger.debug("Author found: " + authorUser.name);
							instance.authorUser = authorUser;
						}

						// {"body":{"value":"<p>UK address on contacts page</p>\n", "summary":"", "format":"plain_text"},
						if (instance.getBody() instanceof LinkedHashMap) {
							Map<String, String> bodyMap = (LinkedHashMap<String,String>)instance.getBody();
							String value = bodyMap.get("value");
							String format = bodyMap.get("format");
							String summary = bodyMap.get("summary");
							if (StringUtils.isNotEmpty(value)) {
								instance.notes = value;
							}
							if (StringUtils.isNotEmpty(format)) {
								instance.format = format;
							}
							if (StringUtils.isNotEmpty(summary)) {
								instance.summary = summary;
							}
						}
						
						String timestamp = instance.getField_timestamp();
						// "field_timestamp":"20130908110118",
						if (StringUtils.isNotEmpty(timestamp)) {
							DateFormat dateFormat = new SimpleDateFormat("yyyyMMddssmmhh");
							try {
								Date fieldDate = dateFormat.parse(timestamp);
							    instance.fieldDate = fieldDate;
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						
//						"field_qa_issues":{
//							"uri":"http:\/\/webarchive.org.uk\/act\/taxonomy_term\/165","id":"165","resource":"taxonomy_term"
//							},
//						"field_description_of_qa_issues":{
//						"value":"\u003Cp\u003ESite not live\u003C\/p\u003E\n","format":"filtered_html"
//					},
						FieldModel qaIssueField = instance.getField_qa_issues();
						try {
							if (qaIssueField != null) {
								QaIssue qaIssue = this.getQaIssue(qaIssueField);
								if (instance.getField_description_of_qa_issues() instanceof LinkedHashMap) {
									Map<String, String> qaDesc = (LinkedHashMap<String,String>) instance.getField_description_of_qa_issues();
//									Logger.debug("qaDesc: " + qaDesc.get("value"));
									qaIssue.description = qaDesc.get("value");
								}
								instance.qaIssue = qaIssue;
							}
						} catch (TaxonomyNotFoundException tnfe) {
							tnfe.printStackTrace();
						}

//						"field_target":{
//								"uri":"http:\/\/webarchive.org.uk\/act\/node\/676","id":"676","resource":"node"
//							},
						FieldModel fieldTarget = instance.getField_target();
						String actUrl = this.getActUrl(fieldTarget.getId());
						Target target = Target.findByUrl(actUrl);
						if (target != null) {
							instance.target = target;
						}
						
//						"field_published":false,
//						"field_to_be_published_":false,
						
						instance.revision = Const.INITIAL_REVISION;
						if (StringUtils.isNotBlank(instance.language) && instance.language.equals(Const.UND)) {
							instance.language = null;
						}
	
						instance.createdAt = this.getDateFromSeconds(instance.getCreated());

			        	instance.save();
			        	count++;
					}
				}

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		Logger.debug("Instances complete");
	}
	
	private License getLicense(FieldModel fieldModel) throws IOException, TaxonomyNotFoundException {
		String actUrl = this.getActUrl(fieldModel.getId());
		License license = License.findByUrl(actUrl);
		if (license == null) {
			throw new TaxonomyNotFoundException("No License for actUrl: " + actUrl);
		}
		return license;
	}
	
	private Subject getSubject(FieldModel fieldModel) throws IOException, TaxonomyNotFoundException {
		String actUrl = this.getActUrl(fieldModel.getId());
		Subject subject = Subject.findByUrl(actUrl);
		if (subject == null) {
			throw new TaxonomyNotFoundException("No Subject for actUrl: " + actUrl);
		}
		return subject;
	}
	
	private Collection getCollection(FieldModel fieldModel) throws IOException, TaxonomyNotFoundException {
		String actUrl = this.getActUrl(fieldModel.getId());
		Collection collection = Collection.findByUrl(actUrl);
		if (collection == null) {
			throw new TaxonomyNotFoundException("No Collection for actUrl: " + actUrl);
		}
		return collection;
	}
	
	private QaIssue getQaIssue(FieldModel fieldModel) throws IOException, TaxonomyNotFoundException {
		String actUrl = this.getActUrl(fieldModel.getId());
		QaIssue qaIssue = QaIssue.findByUrl(actUrl);
		if (qaIssue == null) {
			throw new TaxonomyNotFoundException("No QaIssue for actUrl: " + actUrl);
		}
		return qaIssue;
	}
}
