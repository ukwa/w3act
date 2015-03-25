package controllers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;

import play.*;
import play.mvc.*;
import play.data.*;
import static play.data.Form.*;
import models.*;
import uk.bl.Const;
import uk.bl.api.PasswordHash;
import uk.bl.api.Utils;
import uk.bl.exception.ActException;
import uk.bl.exception.TaxonomyNotFoundException;
import uk.bl.scope.Scope;
import views.html.*;

public class ApplicationController extends Controller {
  
    // -- Authentication
    
    public static class Login {
        
        public String email;
        public String password;
        
        /**
         * We only store lowercase emails and transform user input to lowercase for this field.
         * @return null if authentication ok.
         */
        public String validate() {
        	boolean res = false;
        	try {
        		if (StringUtils.isBlank(email)) {
        			return "Please enter an email";
        		}
        		if (StringUtils.isBlank(password)) {
        			return "Please enter a password";
        		}
//				Logger.debug("validate() inserted password: " + password);
				String inputPassword = password;
//				Logger.debug("validate() db hash for email: " + email.toLowerCase());
				User user = User.findByEmail(email.toLowerCase());
				if (user == null) {
					return "Invalid email";
				}
				if (user.roles != null && !user.roles.isEmpty() && user.hasRole("closed")) {
					return "This user account has been closed. Please contact the British Library web archiving team";
				}
				String userPassword = User.findByEmail(email.toLowerCase()).password;
				Logger.debug("userPassword: " + userPassword + " - " + inputPassword);
        		res = PasswordHash.validatePassword(inputPassword, userPassword);
			} catch (NoSuchAlgorithmException e) {
				Logger.debug("validate() no algorithm error: " + e);
			} catch (InvalidKeySpecException e) {
				Logger.debug("validate() key specification error: " + e);
			}
            if(!res || User.authenticate(email.toLowerCase(), User.findByEmail(email.toLowerCase()).password) == null) {
                return "Password not recognised";
            }
        	Logger.debug("res: " + res);
            return null;
        }
        
    }

    /**
     * Login page.
     */
    public static Result login() {
    	// If user is already logged in, redirect to the homepage:
    	String email = session().get("email");
    	User user = User.findByEmail(email);
    	if (user != null) { 
            return redirect( routes.ApplicationController.index() );    		
    	}
		// Redirect to login page (embedding the flash scope url to redirect to afterwards):
        return ok(
            login.render(form(Login.class))
        );
    }
    
    /**
     * Handle login form submission.
     * We only store lowercase emails and transform user input to lowercase for this field.
     */
    public static Result authenticate() {
    	// Grab the url to redirect to after login:
    	DynamicForm requestData = Form.form().bindFromRequest();
    	String url = requestData.get("redirectToUrl");
		// Parse the login:
        Form<Login> loginForm = form(Login.class).bindFromRequest();
        if(loginForm.hasErrors()) {
        	flash().put("url", url);
            return badRequest(login.render(loginForm));
        } else {
            session("email", loginForm.get().email.toLowerCase());
            Logger.debug("url: " + url);
            if( StringUtils.isBlank(url) ) url = routes.ApplicationController.index().url();            
            return redirect( url );
        }
    }

    /**
     * Logout and clean the session.
     */
    public static Result logout() {
        session().clear();
        flash("success", "You've been logged out");
        return redirect(
            routes.ApplicationController.login()
        );
    }
  
    /**
     * Display the About tab.
     */
    public static Result index() {
    	String email = session().get("email");
    	User user = User.findByEmail(email);
    	Logger.debug("user: " + user + " - " + email);
    	if (user != null) {
    		return ok(about.render("About", user));
    	}
    	return redirect(routes.ApplicationController.login());
    }
    
    public static String getLastCommitHash() {
    	String res = "";
    	try {
    		BufferedReader br = new BufferedReader(new FileReader(Const.LAST_VERSION_FILE));
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
//    		row = Utils.buildWebRequestByUrl(Const.GITHUB, Const.LAST_COMMIT);
//        	Logger.debug("row: " + row);
//	    	if (row != null && row.length() > 0) {
//		    	int start = row.indexOf(Const.LAST_COMMIT) + Const.LAST_COMMIT.length();
//		    	row = row.substring(start, start + 40);
//	    	}
    	} catch (Exception e) {
    		Logger.debug("Error occured by last commit hash calculation: " + e);
    	}
    	Logger.debug("last commit hash: " + res);

    	return res;
    }
    
    public static Result addContent() {
		return ok(
            addcontent.render("AddContent", User.findByEmail(request().username()))
        );
    }
    
    public static Result findContent() {
		return ok(
            findcontent.render("FindContent", User.findByEmail(request().username()))
        );
    }

    // -- Javascript routing
    
    public static Result javascriptRoutes() {
        response().setContentType("text/javascript");
        return ok(
            Routes.javascriptRouter("jsRoutes",
            	controllers.routes.javascript.ApplicationController.index(),
                controllers.routes.javascript.CollectionController.index(),
                controllers.routes.javascript.InstanceController.index(),
                controllers.routes.javascript.TargetController.index(),
                controllers.routes.javascript.OrganisationController.index(),
                controllers.routes.javascript.UserController.index(),
                controllers.routes.javascript.ContactController.index()
            )
        );
    }
    
    /**
     * @param id
     * @return
     * 
     * curl -v -H "Content-Type: application/json" -X PUT -d '{"uk_postal_address_url": "http://belly.co.uk"}' -u kinman.li@bl.uk:password http://localhost:9000/actdev/api/targets/74
     * curl -v -H "Content-Type: application/json" -X PUT -d '{"uk_postal_address_url": "http://belly.co.uk", "field_urls": ["http://www.poopoo.co.uk"]}' -u kinman.li@bl.uk:password http://localhost:9000/actdev/api/targets/74
     * curl -v -H "Content-Type: application/json" -X PUT -d '{"field_subjects": ["13","14"]}' -u kinman.li@bl.uk:password http://localhost:9000/actdev/api/targets/74
     * curl -v -H "Content-Type: application/json" -X PUT -d '{"field_collection_cats": ["2","4"]}' -u kinman.li@bl.uk:password http://localhost:9000/actdev/api/targets/74
     * curl -v -H "Content-Type: application/json" -X PUT -d ' {
    "uk_postal_address_url": true,
    "field_uk_postal_address_url": "http://www.theguardian.com/"
}' -u kinman.li@bl.uk:password http://www.webarchive.org.uk/actdev/targets/17356
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
			Target jsonTarget = objectMapper.readValue(node.toString(), Target.class);
//			Long id = jsonTarget.id;
			if (id == null) {
				return badRequest("No ID found: " + id);
			}
			Logger.debug("node: " + node);
			
			// process Targets here
			Target dbTarget = Target.findById(id);
			
			if (dbTarget == null) {
				return badRequest("No Target Found for ID: " + id);
			}
			Logger.debug("old value: " + dbTarget.ukPostalAddressUrl);
			
			// find field and then update/save
			ObjectReader updater = objectMapper.readerForUpdating(dbTarget);
			Target merged = updater.readValue(node);
			Logger.debug("merged: " + merged.id + " " + merged.fieldUrl());
			
			// url update
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

