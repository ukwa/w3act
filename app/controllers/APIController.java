package controllers;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;

import play.*;
import play.libs.Json;
import play.mvc.*;
import models.*;
import uk.bl.Const;
import uk.bl.api.Utils;
import uk.bl.exception.ActException;
import uk.bl.exception.TaxonomyNotFoundException;
import uk.bl.scope.Scope;

public class APIController extends Controller {
  
    @With(SecuredAction.class)
    public static Result viewAsJson(Long id) {
    	Target target = Target.findById(id);
    	if( target != null) {
    		return ok(Json.toJson(target));
    	} else {
    		return notFound("There is no Target with ID "+id);
    	}
    }
    
    /**
     * @param id
     * @return
     * 
     * curl -v -H "Content-Type: application/json" -X PUT -d '{"uk_postal_address_url": "http://belly.co.uk"}' -u kinman.li@bl.uk:password http://localhost:9000/actdev/api/targets/74
     * curl -v -H "Content-Type: application/json" -X PUT -d '{"uk_postal_address_url": "http://belly.co.uk", "field_urls": ["http://www.poopoo.co.uk"]}' -u kinman.li@bl.uk:password http://localhost:9000/actdev/api/targets/74
     * curl -v -H "Content-Type: application/json" -X PUT -d '{"field_subjects": ["13","14"]}' -u kinman.li@bl.uk:password http://localhost:9000/actdev/api/targets/74
     * curl -v -H "Content-Type: application/json" -X PUT -d '{"field_collection_cats": ["2","4"]}' -u kinman.li@bl.uk:password http://localhost:9000/actdev/api/targets/74
     * curl -v -H "Content-Type: application/json" -X PUT -d '{"uk_postal_address_url": true,"field_uk_postal_address_url": "http://www.theguardian.com/"}' -u kinman.li@bl.uk:password http://www.webarchive.org.uk/actdev/api/targets/17356
	 * curl -v -H "Content-Type: application/json" -X PUT -d '{"uk_postal_address_url": "http://www.theguardian.com/","id": 17516,"field_uk_postal_address": true}' -u kinman.li@bl.uk:password http://www.webarchive.org.uk/actdev/api/targets/17516
	 * curl -v -H "Content-Type: application/json" -X PUT -d '{"uk_postal_address_url": "http://www.theguardian.com/","field_uk_postal_address": true}' -u kinman.li@bl.uk:password http://localhost:9000/actdev/api/targets/1
     * 
     * @throws ActException
     */
    @With(SecuredAction.class)
    @BodyParser.Of(BodyParser.Json.class)
    public static Result targetUpdate(Long id) throws ActException {
    	JsonNode node = request().body().asJson();
    	ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.setSerializationInclusion(Include.NON_DEFAULT);
    	Logger.debug(node.asText());
		try {
			if (id == null) {
				return badRequest("No ID found: " + id);
			}
			Logger.debug("UPDATE: " + node);
			
			// process Targets here
			Target dbTarget = Target.findById(id);
			
			if (dbTarget == null) {
				return badRequest("No Target Found for ID: " + id);
			}
			Logger.debug("ORIGINAL: " + dbTarget);
			
			// find field and then update/save
			ObjectReader updater = objectMapper.readerForUpdating(dbTarget);
			Target merged = updater.readValue(node);
			Logger.debug("MERGED: " + merged);
			
			// url update
			Target jsonTarget = objectMapper.readValue(node.toString(), Target.class);
			if (jsonTarget.fieldUrls != null) {
				if (merged.fieldUrls == null) {
					merged.fieldUrls = new ArrayList<FieldUrl>();
				}
				if (jsonTarget.getField_urls() != null) {
					for (String url : jsonTarget.getField_urls()) {
		            	String trimmed = url.trim();
						URL uri = new URI(trimmed).normalize().toURL();
		    			String extFormUrl = uri.toExternalForm();
		    			
		    			FieldUrl existingFieldUrl = FieldUrl.findByUrl(trimmed);
						if (existingFieldUrl != null) {
							Logger.debug("CONFLICT existingFieldUrl Url: " + existingFieldUrl.url);
							return status(Http.Status.CONFLICT);
						}
		
		            	FieldUrl fieldUrl = new FieldUrl(extFormUrl.trim());
						fieldUrl.domain = Scope.INSTANCE.getDomainFromUrl(fieldUrl.url);
						merged.fieldUrls.add(fieldUrl);
					}
				}
			}
			
			List<String> fieldSubjects = jsonTarget.getField_subjects();
			if (fieldSubjects != null) {
				if (merged.subjects == null) {
					merged.subjects = new ArrayList<Subject>();
				}
				for (String fieldSubject : fieldSubjects) {
					try {
						Subject subject = getSubject(fieldSubject);
		            	if (subject.parent != null) {
		            		merged.subjects = Utils.INSTANCE.processParentsSubjects(merged.subjects, subject.parent.id);
		            	}
		        		if (!merged.subjects.contains(subject)) {
		        			merged.subjects.add(subject);
		        		}
					} catch(TaxonomyNotFoundException e) {
						return badRequest("No Subject Found for : " + e);
					} catch(Exception e) {
						return badRequest("Issue with Subject: " + fieldSubject);
					}
				}
			}
			
			List<String> fieldCollections = jsonTarget.getField_collection_cats();
			if (fieldCollections != null) {
				if (merged.collections == null) {
					merged.collections = new ArrayList<Collection>();
				}
				for (String fieldCollection : fieldCollections) {
					try {
						Collection collection = getCollection(fieldCollection);
						Logger.debug("collection: " + collection);
		            	if (collection.parent != null) {
		            		merged.collections = Utils.INSTANCE.processParentsCollections(merged.collections, collection.parent.id);
		            	}
		        		if (!merged.collections.contains(collection)) {
		        			merged.collections.add(collection);
		        		}
					} catch(TaxonomyNotFoundException e) {
						return badRequest("No Collection Found for : " + fieldCollection);
					} catch(Exception e) {
						return badRequest("Issue with Collection: " + fieldCollection);
					}
				}
			}
			
			Logger.debug("fieldOrganisation...");
			String fieldOrganisation = jsonTarget.getField_nominating_org();				
			if (StringUtils.isNotEmpty(fieldOrganisation)) {
				Long orgId = Long.valueOf(fieldOrganisation);
				Organisation organisation = Organisation.findById(orgId);
				if (organisation == null) {
					return badRequest("No Organisation Found for : " + orgId);
				}
				merged.organisation = organisation;
			}
			
			// "field_crawl_start_date": "1417255200"
			if (jsonTarget.getField_crawl_start_date() != null) {
				merged.crawlStartDate = Utils.INSTANCE.getDateFromSeconds(jsonTarget.getField_crawl_start_date());
			}
			if (jsonTarget.getField_crawl_end_date() != null) {
				merged.crawlEndDate = Utils.INSTANCE.getDateFromSeconds(jsonTarget.getField_crawl_end_date());
			}
			
			if (StringUtils.isNotBlank(jsonTarget.getSelector())) {
				Long selectorId = Long.valueOf(jsonTarget.getSelector());
				User selector = User.findById(selectorId);
				if (selector != null) {
					merged.authorUser = selector;
				}
			}
			
			merged.runChecks();
			merged.update();
			String url = Play.application().configuration().getString("server_name") + Play.application().configuration().getString("application.context") + "/targets/" + merged.id;
			Logger.debug("location: " + url);
			response().setHeader(LOCATION, url);
			Logger.debug("response 200 updated");
		    return ok(response().getHeaders().get(LOCATION));
	    } catch (Exception e) {
	    	Logger.error("error: " + e);
	        return Results.internalServerError(e.getMessage());
	    }
    }
    
    /***
	 *
	 * curl -v -H "Content-Type: application/json" -X POST -d '{"title": "Turok 2","field_subjects": ["13","14"],"field_crawl_frequency": "monthly","field_nominating_org": "1","field_urls": ["http://turok99.com"],"field_collection_cats": ["8","9"],"field_crawl_start_date": "1417255200"}' -u kinman.li@bl.uk:password http://localhost:9000/actdev/api/targets
	 * curl -v -H "Content-Type: application/json" -X POST -d '{"field_collection_cats": ["188"],"field_crawl_frequency": "daily","field_urls": ["http://www.independent.co.uk/news/uk/politics/"],"field_nominating_org": "1","title": "Independent, The: UK Politics"}' -u kinman.li@bl.uk:password http://localhost:9000/actdev/api/targets
	 * curl -v -H "Content-Type: application/json" -X POST -d '{"field_collection_cats": ["188"],"field_crawl_frequency": "daily","field_urls": ["http://www.independent.co.uk/news/uk/politics/"],"field_nominating_org": "1"}' -u kinman.li@bl.uk:password http://localhost:9000/actdev/api/targets
	 * curl -v -H "Content-Type: application/json" -X POST -d '{"field_urls": ["http://www.sdfsdfsf.co.ukxxxxxxxxxxxxxxxxxxx"],"title": "Daily Mail: Labours 500k help from tax avoidance firm","field_nominating_org": "1","field_collection_cats": ["188"],"field_crawl_frequency": "annual","field_crawl_end_date": "1425877200","field_crawl_start_date": "1425790800","field_subjects": [],"field_uk_postal_address": true,"uk_postal_address_url": "http://territest.co.uk", "field_via_correspondence": false,"field_professional_judgement": true,"field_professional_judgement_exp": ""}' -u kinman.li@bl.uk:password http://localhost:9000/actdev/api/targets 
	 * curl -v -H "Content-Type: application/json" -X POST -d '{"field_collection_cats": ["188"],"field_crawl_frequency": "daily","field_nominating_org": "1"}' -u kinman.li@bl.uk:password http://localhost:9000/actdev/api/targets
	 * curl -v -H "Content-Type: application/json" -X POST -d '{
    "field_subjects":[
        "189"
    ],
    "field_crawl_frequency":"annual",
    "field_urls":[
        "http://www.nottheguardian.com/commentisfree/2015/feb/10/tories-britain-democracy-auction-party-funds-donors-test"
    ],
    "field_crawl_end_date":"1425877200",
    "title":"Guardian, The: The Tories are putting Britain\u0027s democracy up for auction",
    "uk_postal_address_url":"http://www.theguardian.com/",
    "field_collection_cats":[
        "188"
    ],
    "field_nominating_org":"4",
    "field_crawl_start_date":"1425790800",
    "field_uk_postal_address":true}' -u kinman.li@bl.uk:password http://localhost:9000/actdev/api/targets
     * @throws ActException 
	 **/
    @With(SecuredAction.class)
    @BodyParser.Of(BodyParser.Json.class)
    public static Result bulkImport() throws ActException {
    	JsonNode node = request().body().asJson();

        
        try {
	    	if(node == null) {
	    		return badRequest("Expecting Json data");
	    	} else {
				ObjectMapper objectMapper = new ObjectMapper();
				objectMapper.setSerializationInclusion(Include.NON_DEFAULT);
				Logger.debug("node: " + node);
	    	    // process Targets here
				Target target = objectMapper.readValue(node.toString(), Target.class);
				
//				Logger.debug("target: " + target);
				
//				{
//					  "title": "Your Thurrock",
//					  "field_subjects": ["13","14"],
//					  "field_crawl_frequency": "monthly",
//					  "field_nominating_org": "1",
//					  "field_url": ["http://yourthurrock.com"],
//					  "field_collection_cats": ["10","11"],
//					  "field_crawl_start_date": "1417255200"
//				}
				
//				{
//				    "field_urls": [
//				        "http://www.dailymail.co.uk/news/article-2943512/Labour-s-500k-help-tax-avoidance-firm-Party-urged-stop-taking-advice-company-accused-controversial-schemes.html"
//				    ],
//				    "title": "Daily Mail: Labour's 500k help from 'tax avoidance' firm",
//				    "field_nominating_org": "1",
//				    "field_collection_cats": ["188"],
//				    "field_crawl_frequency": "annual",
//				    "field_crawl_end_date": "1425877200",
//				    "field_crawl_start_date": "1425790800",
//				    "field_subjects": [],
//				    "field_uk_postal_address": false,
//				    "field_via_correspondence": false,
//				    "field_professional_judgement": false,
//				    "field_professional_judgement_exp": ""
//				}				
				
				if (StringUtils.isEmpty(target.title)) {
					return badRequest("No Title Found for Target");
				}

				List<FieldUrl> fieldUrls = new ArrayList<FieldUrl>();
				
				if (target.getField_urls() == null || target.getField_urls().isEmpty()) {
					return badRequest("No URL(s) Found for Target");
				}
				
				for (String url : target.getField_urls()) {
	            	String trimmed = url.trim();
					URL uri = new URI(trimmed).normalize().toURL();
        			String extFormUrl = uri.toExternalForm();
        			
        			FieldUrl existingFieldUrl = FieldUrl.findByUrl(trimmed);
					if (existingFieldUrl != null) {
						Logger.debug("CONFLICT existingFieldUrl Url: " + existingFieldUrl.url);
						return status(Http.Status.CONFLICT);
					}

	            	FieldUrl fieldUrl = new FieldUrl(extFormUrl.trim());
					fieldUrl.domain = Scope.INSTANCE.getDomainFromUrl(fieldUrl.url);
					fieldUrls.add(fieldUrl);
					Logger.debug("fieldUrls: " + fieldUrl.url);
				}
				// "field_url":[{"url":"http://www.childrenslegalcentre.com/"}],
				if (!fieldUrls.isEmpty()) {
					target.fieldUrls = fieldUrls;
				}
				
				Logger.debug("subjects...");
		        List<Subject> newSubjects = new ArrayList<Subject>();
				List<String> fieldSubjects = target.getField_subjects();
				if (fieldSubjects != null) {
					for (String fieldSubject : fieldSubjects) {
						try {
							Subject subject = getSubject(fieldSubject);
			            	if (subject.parent != null) {
			            		newSubjects = Utils.INSTANCE.processParentsSubjects(newSubjects, subject.parent.id);
			            	}
			        		if (!newSubjects.contains(subject)) {
			        			newSubjects.add(subject);
			        		}
						} catch(TaxonomyNotFoundException e) {
							return badRequest("No Subject Found for : " + e);
						} catch(Exception e) {
							return badRequest("Issue with Subject: " + fieldSubject);
						}
					}
		            target.subjects = newSubjects;
				}
				
				if (!target.ukPostalAddress) {
					target.ukPostalAddressUrl = null;
				}

				Logger.debug("ukPostalAddress: " + target.ukPostalAddress);
				Logger.debug("ukPostalAddressUrl: " + target.ukPostalAddressUrl);

				// "field_crawl_frequency": "monthly"
				Logger.debug("crawlFrequency...");
				
				if (target.crawlFrequency != null) {
					target.crawlFrequency = target.crawlFrequency.toUpperCase();
				}
				
				Logger.debug("fieldOrganisation...");
				String fieldOrganisation = target.getField_nominating_org();				
				if (StringUtils.isNotEmpty(fieldOrganisation)) {
					Long id = Long.valueOf(fieldOrganisation);
					Organisation organisation = Organisation.findById(id);
					if (organisation == null) {
						return badRequest("No Organisation Found for : " + id);
					}
					target.organisation = organisation;
				}

		        List<Collection> newCollections = new ArrayList<Collection>();
				List<String> fieldCollections = target.getField_collection_cats();
				if (fieldCollections != null) {
					for (String fieldCollection : fieldCollections) {
						try {
							Collection collection = getCollection(fieldCollection);
			            	if (collection.parent != null) {
			            		newCollections = Utils.INSTANCE.processParentsCollections(newCollections, collection.parent.id);
			            	}
			        		if (!newCollections.contains(collection)) {
			        			newCollections.add(collection);
			        		}
						} catch(TaxonomyNotFoundException e) {
							return badRequest("No Collection Found for : " + fieldCollection);
						} catch(Exception e) {
							return badRequest("Issue with Collection: " + fieldCollection);
						}
					}
					target.collections = newCollections;
				}

				Logger.debug("fieldSubjects: " + fieldSubjects);
				Logger.debug("fieldOrganisations: " + fieldOrganisation);
				Logger.debug("fieldCategories: " + fieldCollections);
				
				// "field_crawl_start_date": "1417255200"
				if (target.getField_crawl_start_date() != null) {
					target.crawlStartDate = Utils.INSTANCE.getDateFromSeconds(target.getField_crawl_start_date());
				}
				if (target.getField_crawl_end_date() != null) {
					target.crawlEndDate = Utils.INSTANCE.getDateFromSeconds(target.getField_crawl_end_date());
				}
				
				if (StringUtils.isNotBlank(target.getSelector())) {
					Long selectorId = Long.valueOf(target.getSelector());
					User selector = User.findById(selectorId);
					if (selector != null) {
						target.authorUser = selector;
					}
				}
				
				target.url = "act-" + Utils.INSTANCE.createId();
				
				target.runChecks();
				
//				target.edit_url = Utils.INSTANCE.getWctUrl(target.vid);
//				target.createdAt = Utils.INSTANCE.getDateFromSeconds(target.getCreated());
				
				target.revision = Const.INITIAL_REVISION;
				target.active = true;
				
				target.selectionType = Const.SelectionType.SELECTION.name();
				
				if (target.noLdCriteriaMet == null) {
					target.noLdCriteriaMet = Boolean.FALSE;
				}

				if (target.keySite == null) {
					target.keySite = Boolean.FALSE;
				}

				if (target.ignoreRobotsTxt == null) {
					target.ignoreRobotsTxt = Boolean.FALSE;
				}

	        	target.save();
				
				Logger.debug("target: " + target);
				String url = Play.application().configuration().getString("server_name") + Play.application().configuration().getString("application.context") + "/targets/" + target.id;
				Logger.debug("location: " + url);
				response().setHeader(LOCATION, url);
				target.save();
				Logger.debug("response 201 created");
			    return created(response().getHeaders().get(LOCATION));
	    	}
        } catch (IllegalArgumentException e) {
			return badRequest("URL invalid: " + e);
        } catch (Exception e) {
        	Logger.error("error: " + e);
            return Results.internalServerError(e.getMessage());
        }
	}
    
	private static Collection getCollection(String stringId) throws IOException, TaxonomyNotFoundException, NumberFormatException {
		Long id = Long.valueOf(stringId);
		Collection collection = Collection.findById(id);
		if (collection == null) {
			throw new TaxonomyNotFoundException("No Collection id for: " + id);
		}
		return collection;
	}
	
	private static Subject getSubject(String stringId) throws IOException, TaxonomyNotFoundException {
		Long id = Long.valueOf(stringId);
		Subject subject = Subject.findById(id);
		if (subject == null) {
			throw new TaxonomyNotFoundException("No Subject id for: " + id);
		}
		return subject;
	}
}

