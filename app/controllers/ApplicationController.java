package controllers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Transient;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import play.*;
import play.mvc.*;
import play.data.*;
import static play.data.Form.*;
import models.*;
import uk.bl.Const;
import uk.bl.api.PasswordHash;
import uk.bl.api.Utils;
import uk.bl.api.models.FieldModel;
import uk.bl.exception.ActException;
import uk.bl.exception.TaxonomyNotFoundException;
import uk.bl.exception.UrlInvalidException;
import uk.bl.exception.WhoisException;
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
        	Logger.debug("res: " + res);
            if(!res || User.authenticate(email.toLowerCase(), User.findByEmail(email.toLowerCase()).password) == null) {
                return "Password not recognised";
            }
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
            if( url == null ) url = routes.ApplicationController.index().url();            
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
    
    /***
	 *
	 * curl -v -H "Content-Type: application/json" -X POST -d '{"title": "Turok 2","field_subjects": ["13","14"],"field_crawl_frequency": "monthly","field_nominating_org": "1","field_urls": ["http://turok99.com"],"field_collection_cats": ["8","9"],"field_crawl_start_date": "1417255200"}' -u kinman.li@bl.uk:password http://localhost:9000/actdev/api/targets
	 * curl -v -H "Content-Type: application/json" -X POST -d '{"field_collection_cats": ["188"],"field_crawl_frequency": "daily","field_urls": ["http://www.independent.co.uk/news/uk/politics/"],"field_nominating_org": "1","title": "Independent, The: UK Politics"}' -u kinman.li@bl.uk:password http://localhost:9000/actdev/api/targets
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
				
				Logger.debug("target: " + target);
				
//				{
//					  "title": "Your Thurrock",
//					  "field_subjects": ["13","14"],
//					  "field_crawl_frequency": "monthly",
//					  "field_nominating_org": "1",
//					  "field_url": ["http://yourthurrock.com"],
//					  "field_collection_cats": ["10","11"],
//					  "field_crawl_start_date": "1417255200"
//				}
				
//				public Object field_subjects;
//				public Object field_nominating_org;
//				public Object field_collection_cats;

				List<FieldUrl> fieldUrls = new ArrayList<FieldUrl>();
				
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
				List<String> fieldSubjects = target.getField_subjects();
				if (fieldSubjects != null) {
					for (String fieldSubject : fieldSubjects) {
						try {
							Subject subject = getSubject(fieldSubject);
							if (subject != null) {
								target.subjects.add(subject);
							}
						} catch(TaxonomyNotFoundException e) {
							return badRequest("No Subject Found for : " + e);
						} catch(Exception e) {
							return badRequest("Issue with Subject: " + fieldSubject);
						}
					}
				}

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
					target.organisation = Organisation.findById(id);
				}

				List<String> fieldCollections = target.getField_collection_cats();
				if (fieldCollections != null) {
					for (String fieldCollection : fieldCollections) {
						try {
							Collection collection = getCollection(fieldCollection);
							if (collection != null) {
								target.collections.add(collection);
							}
						} catch(TaxonomyNotFoundException e) {
							return badRequest("No Collection Found for : " + fieldCollection);
						} catch(Exception e) {
							return badRequest("Issue with Collection: " + fieldCollection);
						}
					}
				}

				Logger.debug("fieldSubjects: " + fieldSubjects);
				Logger.debug("fieldOrganisations: " + fieldOrganisation);
				Logger.debug("fieldCategories: " + fieldCollections);

				// "field_crawl_start_date": "1417255200"
				if (target.getField_crawl_start_date() != null) {
					target.crawlStartDate = Utils.INSTANCE.getDateFromSeconds(target.getField_crawl_start_date());
				}

				target.url = "act-" + Utils.INSTANCE.createId();
				
				target.isUkHosting = target.isUkHosting();
				target.isTopLevelDomain = target.isTopLevelDomain();
				target.isUkRegistration = target.isUkRegistration();
				
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
        }
        catch (Exception e) {
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

