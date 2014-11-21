package uk.bl.api;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;

import models.Collection;
import models.Instance;
import models.License;
import models.Organisation;
import models.Role;
import models.Subject;
import models.Target;
import models.Taxonomy;
import models.TaxonomyVocabulary;
import models.User;
import play.Logger;
import play.Play;
import play.libs.Json;
import uk.bl.Const;
import uk.bl.Const.NodeType;
import uk.bl.Const.TaxonomyType;
import uk.bl.api.models.FieldModel;
import uk.bl.api.models.FieldValue;
import uk.bl.scope.Scope;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.QueryIterator;
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
		return this.getUrl(Const.ACT_URL, jsonId);
	}
	
	private String getWctUrl(String jsonId) {
		return this.getUrl(Const.WCT_URL, jsonId);
	}
	
	private String getUrl(String prefix, String jsonId) {
		String actUrl = null;
		if (StringUtils.isNotEmpty(jsonId)) {
			StringBuilder url = new StringBuilder(prefix).append(jsonId);
			actUrl = url.toString();
		}
		return actUrl;
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

		String user = Play.application().configuration().getString(Const.DRUPAL_USER);
		String password = Play.application().configuration().getString(Const.DRUPAL_PASSWORD);

        URL url = new URL(jsonUrl);
        String authStr = user + ":" + password;
        String authEncoded = Base64.encodeBytes(authStr.getBytes());

        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        connection.setRequestMethod("GET");
        connection.setDoOutput(true);
        connection.setRequestProperty("Authorization", "Basic " + authEncoded);
        InputStream in = (InputStream) connection.getInputStream();
	    BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
	    String content = readAll(br);
        return content;
	}
	
	private String readAll(Reader rd) throws IOException {
		StringBuilder sb = new StringBuilder();
		int cp;
		while ((cp = rd.read()) != -1) {
		  sb.append((char) cp);
		}
		return sb.toString();
	}

	private Date getDateFromSeconds(Long seconds) {
		Date date = null;
		if (seconds != null) {
			date = new Date(seconds*1000L);
			Logger.info("converted date: " + date);
		}
		return date;
	}
	
	public void convertOrganisations() {

		try {
			String jsonUrl = Const.URL_STR + Const.NodeType.ORGANISATION.toString().toLowerCase();
		    String content = this.getAuthenticatedContent(jsonUrl);
		    JsonNode parentNode = Json.parse(content);
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.setSerializationInclusion(Include.NON_NULL);
			if (parentNode != null) {
				JsonNode rootNode = parentNode.path(Const.LIST_NODE);
				Iterator<JsonNode> iterator = rootNode.iterator();
				while (iterator.hasNext()) {
					JsonNode node = iterator.next();
					Logger.info("json: " + node);
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
					
					
					Logger.info("organisation: " + organisation);
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
				JsonNode rootNode = parentNode.path(Const.LIST_NODE);
				Iterator<JsonNode> iterator = rootNode.iterator();
				while (iterator.hasNext()) {
					Logger.info(count + ") ");
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
							
					Logger.info("json: " + node);
					User user = objectMapper.readValue(node.toString(), User.class);
					
					// TODO: KL ANONYMOUS USER?
					if (user.name.toLowerCase().equals("anonymous")) {
						continue;
					}
					if (StringUtils.isEmpty(user.email)) {
						user.email = user.name.toLowerCase().replace(" ", ".") + "@bl.uk";
					}
					if (user.password == null || user.password.length() == 0) {
						user.password = Const.DEFAULT_PASSWORD;
					}
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
						user.roles = Role.setDefaultRoleByName(Const.DEFAULT_BL_ROLE);
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
					Logger.info("user: " + user);
					count++;
				}
			}
			Logger.info("No of Curators: " + count);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public void convertUrlsToTargets() {
		
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
				
				int count = 0;
				for (int i=firstPage; i<=lastPage; i++) {
					
					StringBuilder targetsUrl = new StringBuilder(jsonUrl).append("&").append(Const.PAGE_IN_URL).append(String.valueOf(i));
					Logger.info("targets url: " + targetsUrl);
					String pageContent = this.getAuthenticatedContent(targetsUrl.toString());
					JsonNode mainNode = Json.parse(pageContent.toString());
					
					JsonNode rootNode = mainNode.path(Const.LIST_NODE);
					Iterator<JsonNode> iterator = rootNode.iterator();
					while (iterator.hasNext()) {
						JsonNode node = iterator.next();
						Logger.info("json: " + node.toString());
						
						Target target = objectMapper.readValue(node.toString(), Target.class);
	
						target.url = this.getActUrl(target.getNid());
						target.edit_url = this.getWctUrl(target.vid);
						target.createdAt = this.getDateFromSeconds(target.getCreated());
						
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
						List<String> fieldUrls = new ArrayList<String>();
						for (Map<String,String> map : target.getField_url()) {
							Logger.info("Field Url: " + map.get("url"));
							fieldUrls.add(map.get("url"));
						}
						if (!fieldUrls.isEmpty()) {
							target.fieldUrl = StringUtils.join(fieldUrls, ", ");
						}
						
						// "field_subject":{"uri":"http://www.webarchive.org.uk/act/taxonomy_term/10","id":"10","resource":"taxonomy_term"},
						// "field_subject":{"uri":"http://www.webarchive.org.uk/act/taxonomy_term/16","id":"16","resource":"taxonomy_term"},"
						FieldModel fieldSubject = target.getField_subject();
						if (fieldSubject != null) {
							Subject subject = this.convertSubject(fieldSubject);
							target.subject = subject;
						}
						
						// "field_description":[],
						if (target.getField_description() != null && target.getField_description() instanceof Map) {
							Map<String, String> fieldDescription = (Map<String, String>)target.getField_description();
							String value = fieldDescription.get("value");
							String summary = fieldDescription.get("summary");
							if (StringUtils.isNotEmpty(value)) {
								target.fieldDescription = value;
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
							target.fieldUkPostalAddressUrl = url;
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
								License license = this.convertLicense(fieldModel);
								
								if (license != null) {
									target.licenses.add(license);
								}
							}
						}
						
						// "field_collection_categories":[{"uri":"http://www.webarchive.org.uk/act/taxonomy_term/160","id":"160","resource":"taxonomy_term"}],
						if (target.getField_collection_categories() != null) {
							List<FieldModel> collectionCategories = target.getField_collection_categories();
							
							for (FieldModel fieldModel : collectionCategories) {
								Collection collection = this.convertCollection(fieldModel);
								
								if (collection != null) {
									target.collections.add(collection);
								}
								
							}
						}
						
						// "field_crawl_start_date":null,
						if (target.getField_crawl_start_date() != null) {
							target.fieldCrawlStartDate = getDateFromSeconds(target.getField_crawl_start_date());
						}
						// "field_crawl_end_date":null,
						if (target.getField_crawl_end_date() != null) {
							target.fieldCrawlEndDate = getDateFromSeconds(target.getField_crawl_end_date());
						}
						
						// "field_notes":{"value":"<p>There are missing documents in this gather for example <a href=\"https://www.wjec.co.uk/uploads/publications/16330.pdf\">https://www.wjec.co.uk/uploads/publications/16330.pdf</a>. It could be a capping issue. I have taken off the cap.</p>\n","format":"filtered_html"}
						if (target.getField_notes() != null && target.getField_notes() instanceof Map) {
							Map<String,String> fieldNotes = (Map<String,String>)target.getField_notes();
							String value = fieldNotes.get("value");
							target.fieldNotes = value;
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
						
						target.fieldUkDomain = BooleanUtils.toBoolean(target.getField_uk_domain());
						target.fieldUkGeoip = BooleanUtils.toBoolean(target.getField_uk_geoip());
	
						
						target.revision = Const.INITIAL_REVISION;
						target.active = true;
						target.selectionType = Const.SelectionType.SELECTION.name();
						if (StringUtils.isNotBlank(target.language) && target.language.equals(Const.UND)) {
							target.language = null;
						}
	
						target.isInScopeUkRegistrationValue = false;
			        	target.isInScopeDomainValue = Target.isInScopeDomain(target.fieldUrl, target.url);
						target.isUkHostingValue = false; 
						target.isInScopeIpValue = false;
						target.isInScopeIpWithoutLicenseValue = false;
						
						if (target.field_no_ld_criteria_met == null) {
							target.field_no_ld_criteria_met = false;
						}
	
						if (target.field_key_site == null) {
							target.field_key_site = false;
						}
						
						if (target.field_ignore_robots_txt == null) {
							target.field_ignore_robots_txt = false;
						}
						
						target.createdAt = this.getDateFromSeconds(target.getCreated());

			        	target.save();
			        	count++;

						// TODO: KL
						// target.domain
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
				
				int count = 0;
				for (int i=firstPage; i<=lastPage; i++) {
					
					StringBuilder instanceUrl = new StringBuilder(jsonUrl).append("&").append(Const.PAGE_IN_URL).append(String.valueOf(i));
					Logger.info("instance url: " + instanceUrl);
					String pageContent = this.getAuthenticatedContent(instanceUrl.toString());
					JsonNode mainNode = Json.parse(pageContent.toString());
					
					JsonNode rootNode = mainNode.path(Const.LIST_NODE);
					Iterator<JsonNode> iterator = rootNode.iterator();
					while (iterator.hasNext()) {
						JsonNode node = iterator.next();
						Logger.info("json: " + node.toString());
						
						Instance instance = objectMapper.readValue(node.toString(), Instance.class);
	
						instance.url = this.getActUrl(instance.getNid());
						instance.edit_url = this.getWctUrl(instance.vid);
						instance.createdAt = this.getDateFromSeconds(instance.getCreated());
						
						// {"body":{"value":"<p>UK address on contacts page</p>\n", "summary":"", "format":"plain_text"},
						if (instance.getBody() instanceof LinkedHashMap) {
							Map<String, String> bodyMap = (LinkedHashMap<String,String>)instance.getBody();
							String value = bodyMap.get("value");
							String format = bodyMap.get("format");
							String summary = bodyMap.get("summary");
							if (StringUtils.isNotEmpty(value)) {
								instance.value = value;
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
						FieldModel qaIssues = instance.getField_qa_issues();

//						"field_target":{
//								"uri":"http:\/\/webarchive.org.uk\/act\/node\/676","id":"676","resource":"node"
//							},
						FieldModel fieldTarget = instance.getField_target();
						String actUrl = this.getActUrl(fieldTarget.getId());
						Target target = Target.findByUrl(actUrl);
						if (target != null) {
							instance.target = target;
						}
//						"field_description_of_qa_issues":{
//							"value":"\u003Cp\u003ESite not live\u003C\/p\u003E\n","format":"filtered_html"
//						},
						FieldValue descOfQaIssues = instance.getField_description_of_qa_issues();
						if (descOfQaIssues != null) {
							instance.fieldQaIssues = descOfQaIssues.getValue();
							instance.format = descOfQaIssues.getFormat();
						}
						
//						"field_published":false,
//						"field_to_be_published_":false,
						
						
						instance.revision = Const.INITIAL_REVISION;
						instance.selectionType = Const.SelectionType.SELECTION.name();
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	private void convertTaxonomyVocabulary(Taxonomy taxonomy) throws IOException {
		if (taxonomy.getVocabularyValue() != null) {
			FieldModel fmTaxVocab = taxonomy.getVocabularyValue();
			Long vid = Long.valueOf(fmTaxVocab.getId());
			TaxonomyVocabulary taxonomyVocabulary = TaxonomyVocabulary.findByVid(vid);
			if (taxonomyVocabulary == null) {
				String tvUrl = fmTaxVocab.getUri().replace("\\", "");
				StringBuilder taxonomyVocabularyUrl = new StringBuilder(tvUrl).append(Const.JSON);
				String tvContent = this.getAuthenticatedContent(taxonomyVocabularyUrl.toString());
				ObjectMapper tvMapper = new ObjectMapper();
				tvMapper.setSerializationInclusion(Include.NON_NULL);
				taxonomyVocabulary = tvMapper.readValue(tvContent, TaxonomyVocabulary.class);
				taxonomyVocabulary.save();
			}
			taxonomy.setTaxonomyVocabulary(taxonomyVocabulary);
		}
	}
	
	private Collection convertCollection(FieldModel fieldModel) throws IOException {
		
		StringBuilder url = new StringBuilder(fieldModel.getUri()).append(Const.JSON);
		Collection collection = null;
		String content = this.getAuthenticatedContent(url.toString());
		
		Logger.info("collection url: " + url);
		Logger.info("collection content: " + content);
		
		ObjectMapper mapper = new ObjectMapper();
		mapper.setSerializationInclusion(Include.NON_NULL);
		collection = mapper.readValue(content, Collection.class);
		Logger.info("collection: " + collection);
		
		collection.url = this.getActUrl(collection.getTid());
		
		// find to see if it's stored already
		
		Collection lookup = Collection.findByUrl(collection.url);
		
		Logger.info("lookup: " + lookup + " using " + collection.url);

		if (lookup == null) {
			// ownerUsers
			if (collection.getField_owner() != null) {
				// "field_owner":[{"uri":"http:\/\/www.webarchive.org.uk\/act\/user\/9","id":"9","resource":"user"}],
				List<FieldModel> fieldOwners = collection.getField_owner();
				for (FieldModel fieldOwner : fieldOwners) {
					String fieldActUrl = this.getActUrl(fieldOwner.getId());
					User owner = User.findByUrl(fieldActUrl);
					collection.getOwnerUsers().add(owner);
				}
			}
			
			// "field_dates":{"value":"1396310400","value2":"1404086400","duration":7776000},
			// "field_publish":true,
			// "tid":"250",
			// "name":"European Parliament Elections 2014","description":"",
			// "weight":"0",
			// "node_count":10,
			// "url":"http:\/\/www.webarchive.org.uk\/act\/taxonomy\/term\/250",
			
			// TODO: KL TaxonomyVocabulary IS CURRENTLY UNUSED
			// "vocabulary":{"uri":"http:\/\/www.webarchive.org.uk\/act\/taxonomy_vocabulary\/5","id":"5","resource":"taxonomy_vocabulary"},
			this.convertTaxonomyVocabulary(collection);

			Logger.info("act-url: " + collection.url);
			Logger.info("getParentFieldList: " + collection.getParentFieldList());
			// "parent":[],
			List<FieldModel> parentFieldList = collection.getParentFieldList();
			for (FieldModel parentFieldModel : parentFieldList) {
				
				String actUrl = this.getActUrl(parentFieldModel.getId());
				Collection parentCollection = Collection.findByUrl(actUrl);
				if (parentCollection == null) {
					parentCollection = convertCollection(parentFieldModel);
				}
				collection.getParentsList().add(parentCollection);
			}
			// TODO: KL parents_all doesn't seem to be used by views/controllers
			// "parents_all":[{"uri":"http:\/\/www.webarchive.org.uk\/act\/taxonomy_term\/250","id":"250","resource":"taxonomy_term"}],"feed_nid":null}
			collection.save();
		} else {
			collection = lookup;
		}
		Logger.info("collection id: " + collection.id);
		return collection;
	}
	
	private Subject convertSubject(FieldModel fieldModel) throws IOException {
//		"field_subject":{"uri":"http://www.webarchive.org.uk/act/taxonomy_term/10","id":"10","resource":"taxonomy_term"},
		StringBuilder url = new StringBuilder(fieldModel.getUri()).append(Const.JSON);
		Subject subject = null;
		String content = this.getAuthenticatedContent(url.toString());
		
		Logger.info("subject url: " + url);
		Logger.info("subject content: " + content);
		
		ObjectMapper mapper = new ObjectMapper();
		mapper.setSerializationInclusion(Include.NON_NULL);
		subject = mapper.readValue(content, Subject.class);
		Logger.info("subject: " + subject);
		
		subject.url = this.getActUrl(subject.getTid());
		
		// find to see if it's stored already
		
		Subject lookup = Subject.findByUrl(subject.url);
		
		Logger.info("lookup: " + lookup + " using " + subject.url);

		if (lookup == null) {
			
			this.convertTaxonomyVocabulary(subject);
			subject.save();
		} else {
			subject = lookup;
		}
		Logger.info("subject url: " + subject.url);
		return subject;
	}

	private License convertLicense(FieldModel fieldModel) throws IOException {
		StringBuilder url = new StringBuilder(fieldModel.getUri()).append(Const.JSON);
		License license = null;
		String content = this.getAuthenticatedContent(url.toString());
		
		Logger.info("license url: " + url);
		Logger.info("license content: " + content);
		
		ObjectMapper mapper = new ObjectMapper();
		mapper.setSerializationInclusion(Include.NON_NULL);
		license = mapper.readValue(content, License.class);
		Logger.info("license: " + license);
		
		license.url = this.getActUrl(license.getTid());
		
		// find to see if it's stored already
		
		License lookup = License.findByUrl(license.url);
		
		Logger.info("lookup: " + lookup + " using " + license.url);

		if (lookup == null) {
			// ownerUsers
			if (license.getField_owner() != null) {
				// "field_owner":[{"uri":"http:\/\/www.webarchive.org.uk\/act\/user\/9","id":"9","resource":"user"}],
				List<FieldModel> fieldOwners = license.getField_owner();
				for (FieldModel fieldOwner : fieldOwners) {
					String fieldActUrl = this.getActUrl(fieldOwner.getId());
					User owner = User.findByUrl(fieldActUrl);
					license.getOwnerUsers().add(owner);
				}
			}
			
			// TODO: KL TaxonomyVocabulary IS CURRENTLY UNUSED
			// "vocabulary":{"uri":"http:\/\/www.webarchive.org.uk\/act\/taxonomy_vocabulary\/5","id":"5","resource":"taxonomy_vocabulary"},
			this.convertTaxonomyVocabulary(license);

			Logger.info("act-url: " + license.url);
			Logger.info("getParentFieldList: " + license.getParentFieldList());
			// "parent":[],
			List<FieldModel> parentFieldList = license.getParentFieldList();
			for (FieldModel parentFieldModel : parentFieldList) {
				
				String actUrl = this.getActUrl(parentFieldModel.getId());
				Taxonomy parentTaxonomy = Taxonomy.findByUrl(actUrl);
				if (parentTaxonomy == null) {
					parentTaxonomy = this.convertTaxonomy(parentFieldModel);
				}
				license.getParentsList().add(parentTaxonomy);
			}
			// TODO: KL parents_all doesn't seem to be used by views/controllers
			// "parents_all":[{"uri":"http:\/\/www.webarchive.org.uk\/act\/taxonomy_term\/250","id":"250","resource":"taxonomy_term"}],"feed_nid":null}
			license.save();
		} else {
			license = lookup;
		}
		
		Logger.info("taxonomy id: " + license.id);
		return license;
	}
	
	private Taxonomy convertTaxonomy(FieldModel fieldModel) throws IOException {
		StringBuilder url = new StringBuilder(fieldModel.getUri()).append(Const.JSON);
		Taxonomy taxonomy = null;
		String content = this.getAuthenticatedContent(url.toString());
		
		Logger.info("taxonomy url: " + url);
		Logger.info("taxonomy content: " + content);
		
		ObjectMapper mapper = new ObjectMapper();
		mapper.setSerializationInclusion(Include.NON_NULL);
		taxonomy = mapper.readValue(content, Taxonomy.class);
		Logger.info("collection: " + taxonomy);
		
		taxonomy.url = this.getActUrl(taxonomy.getTid());
		
		// find to see if it's stored already
		
		Taxonomy lookup = Taxonomy.findByUrl(taxonomy.url);
		
		Logger.info("lookup: " + lookup + " using " + taxonomy.url);

		if (lookup == null) {
			// ownerUsers
			if (taxonomy.getField_owner() != null) {
				// "field_owner":[{"uri":"http:\/\/www.webarchive.org.uk\/act\/user\/9","id":"9","resource":"user"}],
				List<FieldModel> fieldOwners = taxonomy.getField_owner();
				for (FieldModel fieldOwner : fieldOwners) {
					String fieldActUrl = this.getActUrl(fieldOwner.getId());
					User owner = User.findByUrl(fieldActUrl);
					taxonomy.getOwnerUsers().add(owner);
				}
			}
			
			// "field_dates":{"value":"1396310400","value2":"1404086400","duration":7776000},
			// "field_publish":true,
			// "tid":"250",
			// "name":"European Parliament Elections 2014","description":"",
			// "weight":"0",
			// "node_count":10,
			// "url":"http:\/\/www.webarchive.org.uk\/act\/taxonomy\/term\/250",
			
			// TODO: KL TaxonomyVocabulary IS CURRENTLY UNUSED
			// "vocabulary":{"uri":"http:\/\/www.webarchive.org.uk\/act\/taxonomy_vocabulary\/5","id":"5","resource":"taxonomy_vocabulary"},
			this.convertTaxonomyVocabulary(taxonomy);

			Logger.info("act-url: " + taxonomy.url);
			Logger.info("getParentFieldList: " + taxonomy.getParentFieldList());
			// "parent":[],
			List<FieldModel> parentFieldList = taxonomy.getParentFieldList();
			for (FieldModel parentFieldModel : parentFieldList) {
				
				String actUrl = this.getActUrl(parentFieldModel.getId());
				Taxonomy parentTaxonomy = Taxonomy.findByUrl(actUrl);
				if (parentTaxonomy == null) {
					parentTaxonomy = this.convertTaxonomy(parentFieldModel);
				}
				taxonomy.getParentsList().add(parentTaxonomy);
			}
			// TODO: KL parents_all doesn't seem to be used by views/controllers
			// "parents_all":[{"uri":"http:\/\/www.webarchive.org.uk\/act\/taxonomy_term\/250","id":"250","resource":"taxonomy_term"}],"feed_nid":null}
			taxonomy.save();
		} else {
			taxonomy = lookup;
		}
		
//		"field_owner":[{"uri":"http:\/\/www.webarchive.org.uk\/act\/user\/9","id":"9","resource":"user"}],
//		"field_dates":{"value":"1396310400","value2":"1404086400","duration":7776000},
//		"field_publish":true,
//		"tid":"250",
//		"name":"European Parliament Elections 2014","description":"",
//		"weight":"0",
//		"node_count":10,
//		"url":"http:\/\/www.webarchive.org.uk\/act\/taxonomy\/term\/250",
//		"vocabulary":{"uri":"http:\/\/www.webarchive.org.uk\/act\/taxonomy_vocabulary\/5","id":"5","resource":"taxonomy_vocabulary"},
//		"parent":[],
//		"parents_all":[{"uri":"http:\/\/www.webarchive.org.uk\/act\/taxonomy_term\/250","id":"250","resource":"taxonomy_term"}],"feed_nid":null}

		Logger.info("taxonomy id: " + taxonomy.id);
		return taxonomy;
	}
	
	/**
	 * This method retrieves JSON data from Drupal for particular domain object
	 * type (e.g. Target, Collection...) with parameters e.g.
	 * http://www.webarchive.org.uk/act/node.json?type=organisation&page=0
	 * 
	 * @param type
	 * @return a list of retrieved objects
	 */
	public List<Object> getDrupalData(NodeType type) {
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
					List<Object> pageList = parseJson(pageContent,
							type);
					res.addAll(pageList);
				}
			}
		} catch (Exception e) {
			Logger.info("data aggregation error: " + e);
		}
//		Logger.info("list size: " + res.size());
		// int idx = 0;
		// Iterator<Object> itr = res.iterator();
		// while (itr.hasNext()) {
		// Object obj = itr.next();
		// Logger.info("res getDrupalData: " + obj.toString() + ", idx: " +
		// idx);
		// idx++;
		// }
		// int idx = 0;
//		Logger.info("res list size: " + res.size());
		if (!type.equals(NodeType.INSTANCE)) {
			Iterator<Object> itr = res.iterator();
			while (itr.hasNext()) {
				if (type.equals(NodeType.URL)) {
					Target obj = (Target) itr.next();
					obj.revision = Const.INITIAL_REVISION;
					obj.active = true;
					obj.selectionType = Const.SelectionType.SELECTION.name();
//					if (obj.vid > 0) {
						obj.edit_url = Const.WCT_URL + obj.vid;
//					}
					if (obj.language != null && obj.language.equals(Const.UND)) {
						obj.language = "";
					}
		        	/**
		        	 * NPLD scope values
		        	 */
//		        	Logger.debug("calculate NPLD scope for target: " + obj.field_url + ", ID: " + obj.url);
//		        	obj.isInScopeUkRegistrationValue   = Target.isInScopeUkRegistration(obj.field_url);
//		        	Logger.debug("   isInScopeUkRegistrationValue (WhoIs): " + obj.isInScopeUkRegistrationValue);
		        	obj.isInScopeDomainValue           = Target.isInScopeDomain(obj.fieldUrl, obj.url);
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
//					if (obj.vid > 0) {
						obj.edit_url = Const.WCT_URL + obj.vid;
//					}
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
	public List<Object> getDrupalDataBase(NodeType type) {
		List<Object> res = new ArrayList<Object>();
		try {
			String urlStr = Const.URL_STR_BASE + type.toString().toLowerCase()
					+ Const.JSON;
			// aggregate data from drupal and store JSON content in a file
			urlStr = authenticateAndLoadDrupal(urlStr, type);
			// read file and store content in String
			String content = JsonUtils.readJsonFromFile(type.toString().toLowerCase() + Const.OUT_FILE_PATH);
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
					// TODO: WHY DO THIS? already downloaded?
					String pageContent = downloadData(urlStr, type);
					Logger.info("users content: " + pageContent);
					List<Object> pageList = parseJson(pageContent,
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
				if (newUser.roles == null || newUser.roles.size() == 0) {
					newUser.roles = Role.setDefaultRoleByName(Const.DEFAULT_BL_ROLE);
				}
				newUser.id = null;
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
		String user = Play.application().configuration().getString(Const.DRUPAL_USER);
		String password = Play.application().configuration().getString(Const.DRUPAL_PASSWORD);

		Logger.info("authenticateAndLoadDrupal() url: " + urlStr);
		HttpBasicAuth.downloadFileWithAuth(urlStr, user, password, type
				.toString().toLowerCase() + Const.OUT_FILE_PATH);
		res = urlStr;
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
				user.affiliation = organisation.url;
        		// TODO: KL
//				user.updateOrganisation();
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
		// find all taxonomies by "collection" type 
		List<Taxonomy> taxonomyList = Taxonomy.findListByType(TaxonomyType.COLLECTION.toString().toLowerCase());
		Iterator<Taxonomy> itr = taxonomyList.iterator();
		while (itr.hasNext()) {
			Taxonomy taxonomy = itr.next();
			if (taxonomy.name != null && taxonomy.name.length() > 0) {
				// check if collection title already in list
				boolean isInList = false;
				Iterator<Object> itrCollection = res.iterator();
				while (itrCollection.hasNext()) {
					Collection collection = (Collection) itrCollection.next();
					if (collection.name.equals(taxonomy.name)) {
						isInList = true;
						// replace collection URL in Targets with the existing
						// one
						List<Target> targets = Target.findAllByCollectionUrl(taxonomy.url);
						Iterator<Target> itrTargets = targets.iterator();
						while (itrTargets.hasNext()) {
							Target target = itrTargets.next();
							// TODO: KL DO WE NEED TO PERSIST SUGGEST COLLECTIONS?
//							target.fieldSuggestedCollections = collection.url;
							Ebean.update(target);
						}
						break;
					}
				}
				if (!isInList) {
					// create collections
					Collection collection = new Collection();
					collection.id = taxonomy.id;
					// TODO: KL do I need this after the refactor?
//					collection.author = taxonomy.fieldOwner;
//					collection.summary = taxonomy.description;
//					collection.title = taxonomy.name;
//					collection.fee = taxonomy.feed_nid;
//					collection.url = taxonomy.url;
//					collection.weight = taxonomy.weight;
//					collection.nodeCount = taxonomy.node_count;
//					collection.vocabulary = taxonomy.vocabulary;
//					collection.fieldOwner = taxonomy.fieldOwner;
//					collection.fieldDates = taxonomy.fieldDates;
//					if (taxonomy.fieldPublish != null) {
//						collection.publish = Utils.getNormalizeBooleanString(taxonomy.fieldPublish);
//					}
					if (taxonomy.parent == null || taxonomy.parent.length() == 0) {
						collection.parent = Const.NONE_VALUE;
					} else {
						collection.parent = taxonomy.parent;
					}
					collection.parentsAll = taxonomy.parentsAll;
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
	private void aggregateObjectList(String urlStr,
			List<String> urlList, NodeType type, TaxonomyType taxonomy_type,
			List<Object> res) {
		Logger.info("extract data for: " + urlStr + " type: " + type);
		String content = downloadData(urlStr, type);
		JsonNode mainNode = Json.parse(content);
		if (mainNode != null) {
			List<Object> pageList = parseJsonExt(content, type,
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
	public void executeUrlRequest(String url, List<String> urlList,
			NodeType type, TaxonomyType taxonomy_type, List<Object> res) {
		Logger.info("exc 1" + url);
		url = getWebarchiveCreatorUrl(url, type);
		Logger.info("exc 2" + url);
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
	public void readListFromString(String fieldName, List<String> urlList, NodeType type, TaxonomyType taxonomy_type, List<Object> res) {
//		Logger.info("extractDrupalData: " + target.field_qa_status + " - " + urlList + " - " + type + " - " + TaxonomyType.QUALITY_ISSUE);
//		readListFromString(target.fieldCollectionCategories, urlList, type, TaxonomyType.COLLECTION, res);
		if (fieldName != null && fieldName.length() > 0) {
			if (fieldName.contains(Const.COMMA)) {
				List<String> resList = Arrays.asList(fieldName
						.split(Const.COMMA));
				Iterator<String> itr = resList.iterator();
				while (itr.hasNext()) {
					String next = itr.next();
					Logger.info("next: " + next);
					executeUrlRequest(next, urlList, type, taxonomy_type,
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
	public List<Object> extractDrupalData(NodeType type) {
		List<Object> res = new ArrayList<Object>();
		try {
			List<String> urlList = new ArrayList<String>();
			List<Target> targets = Target.findAll();
			Iterator<Target> itr = targets.iterator();
			while (itr.hasNext()) {
				Target target = itr.next();
				// String urlStr = "";
				if (type.equals(NodeType.USER)) {
//					readListFromString(target.authorRef, urlList, type, null, res);
				}
				if (type.equals(NodeType.TAXONOMY)) {
					readListFromString(target.fieldCollectionCategories, urlList, type, TaxonomyType.COLLECTION, res);
					// TODO: KL DO WE NEED TO PERSIST SUGGEST COLLECTIONS?
//					readListFromString(target.fieldSuggestedCollections, urlList, type, TaxonomyType.COLLECTION, res);
					readListFromString(target.fieldLicense, urlList, type, TaxonomyType.LICENSE, res);
					readListFromString(target.fieldSubject, urlList, type, TaxonomyType.SUBJECT, res);
//					Logger.info("extractDrupalData: " + target.field_qa_status + " - " + urlList + " - " + type + " - " + TaxonomyType.QUALITY_ISSUE);
					readListFromString(target.fieldQaStatus, urlList, type, TaxonomyType.QUALITY_ISSUE, res);
				}
				if (type.equals(NodeType.TAXONOMY_VOCABULARY)) {
//					List<Taxonomy> taxonomies = Taxonomy.findAll();
//					Iterator<Taxonomy> taxonomyItr = taxonomies.iterator();
//					while (taxonomyItr.hasNext()) {
//						Taxonomy taxonomy = (Taxonomy) taxonomyItr.next();
//						readListFromString(taxonomy.vocabulary, urlList, type,
//								null, res);
//					}
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
	public String getStringList(JsonNode resNode, String path,
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
	public String checkArchiveUrl(String url) {
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
	public String getStringFromSubNode(JsonNode resNode, String path) {
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
	public List<Object> parseJson(String content, NodeType type) {
		List<Object> res = new ArrayList<Object>();
		JsonNode json = Json.parse(content);
		if (json != null) {
			JsonNode rootNode = json.path(Const.LIST_NODE);
			Iterator<JsonNode> ite = rootNode.iterator();
//			Logger.info("rootNode elements count is: " + rootNode.size());

			while (ite.hasNext()) {
				JsonNode node = ite.next();
				Object obj = null;
				if (type.equals(Const.NodeType.URL)) {
					obj = new Target();
				}
				if (type.equals(Const.NodeType.COLLECTION)) {
					obj = new Collection();
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
	public List<Object> parseJsonExt(String content, NodeType type,
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
//				((Taxonomy) obj).ttype = taxonomy_type.toString().toLowerCase();
				if (((Taxonomy) obj).name == null
						|| ((Taxonomy) obj).name.length() == 0) {
					hasEmptyName = true;
				}
//				if (((Taxonomy) obj).ttype != null
//						&& ((Taxonomy) obj).ttype.equals(Const.LICENCE)) {
//					if (((Taxonomy) obj).name != null
//							&& ((Taxonomy) obj).name
//									.equals(Const.OLD_UKWA_LICENSE)) {
//						((Taxonomy) obj).name = Const.NEW_UKWA_LICENSE;
//					}
//				}
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
					existingUser.affiliation = newUser.affiliation;
            		// TODO: KL
//					existingUser.updateOrganisation();
					existingUser.id = newUser.id;
					existingUser.url = newUser.url;
					existingUser.edit_url = newUser.edit_url;
					existingUser.last_access = newUser.last_access;
					existingUser.last_login = newUser.last_login;
					existingUser.createdAt = newUser.createdAt;
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
	public void parseJsonString(Field f, JsonNode node, Object obj) {

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
	private boolean checkSubNode(Field f, JsonNode node, Object obj,
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
	public String normalizeArchiveUrl(String str) {
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
	public void parseJsonNode(JsonNode node, Object obj) {
		parseJsonNodeExt(node, obj, null, null, null, null);
	}

	/**
	 * This method parses JSON node and extracts fields
	 * 
	 * @param node
	 * @param obj
	 */
	public void parseJsonNodeExt(JsonNode node, Object obj,
			List<String> urlList, NodeType type, TaxonomyType taxonomy_type,
			List<Object> resList) {
		Field[] fields = obj.getClass().getFields();
		// if (obj.getClass().toString().contains("Taxonomy")) {
//		Logger.info("Taxonomy node: " + urlList + " " + type + " " + taxonomy_type);
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
					String jsonField = getStringList(resNode, f.getName(), false);
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

							// field_qa_issues seems to come from here
							if (obj instanceof Instance) {
								if (f.getName().equals("field_qa_issues")) {
									JsonNode resNode = getElement(node, f.getName());
	//								String jsonField = getStringItem(resNode, f.getName());
									String jsonField = getStringFromSubNode(resNode, "uri");
									Taxonomy taxonomy = Ebean.find(models.Taxonomy.class).where().eq("url", jsonField).findUnique();
									Logger.info("!checkSubNode: " + f.getName() + "-----" + resNode + " " + f.getType() + " " + jsonField + " ---- " + taxonomy.name);
									
	//								{"uri":"http://www.webarchive.org.uk/act/taxonomy_term/164","id":"164","resource":"taxonomy_term"} 
	//								class java.lang.String act-164 
	//								No QA issues found (OK to publish)
									Logger.info("setting " + obj.getClass() + " to " + taxonomy.name + " on field " + f.getName());
									f.set(obj, taxonomy.name);
////								((Instance)obj).field_qa_issues = taxonomy.name;
								} else if (f.getName().equals("qa_status")) {
									// No QA issues found (OK to publish), QA issues found, Unknown
									// PASSED_PUBLISH_NO_ACTION_REQUIRED, ISSUE_NOTED, None
									String fieldQaIssues = ((Instance)obj).fieldQaIssues;
									String convertedValue = Taxonomy.findQaStatusByName(fieldQaIssues);
//									Logger.info("Mapping " + obj.getClass() + " " + fieldQaIssues + " to " + f.getName() + " " + convertedValue);
									f.set(obj, convertedValue);
//									((Instance)obj).field_qa_status = convertedValue;
								} else if (f.getName().equals("qa_notes")) {
//									"qa_notes":"","quality_notes"
//									Description of QA Issues > QA Notes
									JsonNode resNode = getElement(node, "field_description_of_qa_issues");
									String jsonField = getStringFromSubNode(resNode, "value");
//									Logger.info("Mapping " + obj.getClass() + " field_description_of_qa_issues: " + jsonField + " to " + f.getName());
									if (StringUtils.isEmpty(jsonField)) {
										jsonField = "N/A";
									}
									f.set(obj, jsonField);
//									((Instance)obj).qa_notes = jsonField;
								} else if (f.getName().equals("quality_notes")) {
//									Notes > Quality Notes
									JsonNode resNode = getElement(node, "body");
									String jsonField = getStringFromSubNode(resNode, "value");
									Logger.info("Mapping " + obj.getClass() + " body: " + jsonField + " to " + f.getName());
									if (StringUtils.isEmpty(jsonField)) {
										jsonField = "N/A";
									}
									f.set(obj, jsonField);
//									((Instance)obj).quality_notes = jsonField;
								} else {
									parseJsonString(f, node, obj);
								}
							} else {
								parseJsonString(f, node, obj);
							}
						} else {
							if (obj instanceof Instance) {
								if (f.getName().equals(Const.FIELD_QA_STATUS)) {
									Logger.info("checkSubNode - FIELD_QA_STATUS >>>>>>>>>>>> " + f.getName());
									String fieldQaIssues = ((Instance)obj).fieldQaIssues;
									String convertedValue = Taxonomy.findQaStatusByName(fieldQaIssues);
									Logger.info("checkSubNode Mapping " + obj.getClass() + " " + fieldQaIssues + " to " + f.getName() + " " + convertedValue);
									f.set(obj, convertedValue);
								}
							}
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
			if (instance.fieldTarget != null) {
				Logger.info("map instance.field_target: "
						+ instance.fieldTarget);
				Target target = Target.findByUrl(instance.fieldTarget);
				instance.fieldUrl = target.fieldUrl;
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
			if (target.fieldUrl != null) {
				target.domain = Scope.getDomainFromUrl(target.fieldUrl);
				Logger.info("Target domain: " + target.domain
						+ " mapped to Target field URL: " + target.fieldUrl);
				Ebean.update(target);
			}
		}
	}
}
