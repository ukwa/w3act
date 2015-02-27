package controllers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;

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
	 * 
	 * {"body":[],"field_scope":"root","field_url":[{"url":"http:\/\/www.electionwatch.co.uk\/","attributes":[]}],"field_subject":{"uri":"http:\/\/webarchive.org.uk\/act\/taxonomy_term\/227","id":"227","resource":"taxonomy_term"},"field_depth":"capped","field_via_correspondence":false,"field_uk_postal_address":false,"field_uk_hosting":false,"field_description":[],"field_uk_postal_address_url":[],"field_nominating_organisation":{"uri":"http:\/\/webarchive.org.uk\/act\/node\/101","id":"101","resource":"node"},"field_crawl_frequency":"daily","field_suggested_collections":[],"field_collections":[],"field_crawl_start_date":"1422943200","field_crawl_end_date":"1431057600","field_uk_domain":"Yes","field_license":[],"field_crawl_permission":"","field_collection_categories":[{"uri":"http:\/\/webarchive.org.uk\/act\/taxonomy_term\/300","id":"300","resource":"taxonomy_term"},{"uri":"http:\/\/webarchive.org.uk\/act\/taxonomy_term\/305","id":"305","resource":"taxonomy_term"}],"field_special_dispensation":false,"field_special_dispensation_reaso":null,"field_live_site_status":null,"field_notes":[],"field_wct_id":null,"field_spt_id":null,"field_snapshots":[],"field_no_ld_criteria_met":false,"field_key_site":false,"field_uk_geoip":"Yes","field_professional_judgement":false,"field_professional_judgement_exp":null,"field_ignore_robots_txt":false,"field_instances":[],"nid":"21593","vid":"34746","is_new":false,"type":"url","title":"Election Watch - UK General Election 2015 ","language":"en","url":"http:\/\/webarchive.org.uk\/act\/node\/21593","edit_url":"http:\/\/webarchive.org.uk\/act\/node\/21593\/edit","status":"1","promote":"0","sticky":"0","created":"1422887984","changed":"1422887984","author":{"uri":"http:\/\/webarchive.org.uk\/act\/user\/61","id":"61","resource":"user"},"log":"","revision":null,"comment":"2","comments":[],"comment_count":"0","comment_count_new":"0","feed_nid":null}  
	 *  
	 * curl -v -H "Accept: application/json" -H "Content-Type: application/json" -X POST -u kinman.li@bl.uk:password -d '{"body":[],"field_scope":"root","field_url":[{"url":"http:\/\/www.electionwatch.co.uk\/","attributes":[]}],"field_subject":{"uri":"http:\/\/webarchive.org.uk\/act\/taxonomy_term\/227","id":"227","resource":"taxonomy_term"},"field_depth":"capped","field_via_correspondence":false,"field_uk_postal_address":false,"field_uk_hosting":false,"field_description":[],"field_uk_postal_address_url":[],"field_nominating_organisation":{"uri":"http:\/\/webarchive.org.uk\/act\/node\/101","id":"101","resource":"node"},"field_crawl_frequency":"daily","field_suggested_collections":[],"field_collections":[],"field_crawl_start_date":"1422943200","field_crawl_end_date":"1431057600","field_uk_domain":"Yes","field_license":[],"field_crawl_permission":"","field_collection_categories":[{"uri":"http:\/\/webarchive.org.uk\/act\/taxonomy_term\/300","id":"300","resource":"taxonomy_term"},{"uri":"http:\/\/webarchive.org.uk\/act\/taxonomy_term\/305","id":"305","resource":"taxonomy_term"}],"field_special_dispensation":false,"field_special_dispensation_reaso":null,"field_live_site_status":null,"field_notes":[],"field_wct_id":null,"field_spt_id":null,"field_snapshots":[],"field_no_ld_criteria_met":false,"field_key_site":false,"field_uk_geoip":"Yes","field_professional_judgement":false,"field_professional_judgement_exp":null,"field_ignore_robots_txt":false,"field_instances":[],"nid":"21593","vid":"34746","is_new":false,"type":"url","title":"Election Watch - UK General Election 2015 ","language":"en","url":"http:\/\/webarchive.org.uk\/act\/node\/21593","edit_url":"http:\/\/webarchive.org.uk\/act\/node\/21593\/edit","status":"1","promote":"0","sticky":"0","created":"1422887984","changed":"1422887984","author":{"uri":"http:\/\/webarchive.org.uk\/act\/user\/61","id":"61","resource":"user"},"log":"","revision":null,"comment":"2","comments":[],"comment_count":"0","comment_count_new":"0","feed_nid":null}' http://localhost:9000/actdev/api/targets
	 * curl -v -H "Content-Type: application/json" -X POST -u kinman.li@bl.uk:password -d '{"body":[],"field_scope":"root","field_url":[{"url":"http:\/\/www.electionwatch.co.uk\/","attributes":[]}],"field_subject":{"uri":"http:\/\/webarchive.org.uk\/act\/taxonomy_term\/227","id":"227","resource":"taxonomy_term"},"field_depth":"capped","field_via_correspondence":false,"field_uk_postal_address":false,"field_uk_hosting":false,"field_description":[],"field_uk_postal_address_url":[],"field_nominating_organisation":{"uri":"http:\/\/webarchive.org.uk\/act\/node\/101","id":"101","resource":"node"},"field_crawl_frequency":"daily","field_suggested_collections":[],"field_collections":[],"field_crawl_start_date":"1422943200","field_crawl_end_date":"1431057600","field_uk_domain":"Yes","field_license":[],"field_crawl_permission":"","field_collection_categories":[{"uri":"http:\/\/webarchive.org.uk\/act\/taxonomy_term\/300","id":"300","resource":"taxonomy_term"},{"uri":"http:\/\/webarchive.org.uk\/act\/taxonomy_term\/305","id":"305","resource":"taxonomy_term"}],"field_special_dispensation":false,"field_special_dispensation_reaso":null,"field_live_site_status":null,"field_notes":[],"field_wct_id":null,"field_spt_id":null,"field_snapshots":[],"field_no_ld_criteria_met":false,"field_key_site":false,"field_uk_geoip":"Yes","field_professional_judgement":false,"field_professional_judgement_exp":null,"field_ignore_robots_txt":false,"field_instances":[],"nid":"21593","vid":"34746","is_new":false,"type":"url","title":"Election Watch - UK General Election 2015 ","language":"en","url":"http:\/\/webarchive.org.uk\/act\/node\/21593","edit_url":"http:\/\/webarchive.org.uk\/act\/node\/21593\/edit","status":"1","promote":"0","sticky":"0","created":"1422887984","changed":"1422887984","author":{"uri":"http:\/\/webarchive.org.uk\/act\/user\/61","id":"61","resource":"user"},"log":"","revision":null,"comment":"2","comments":[],"comment_count":"0","comment_count_new":"0","feed_nid":null}' http://localhost:9000/actdev/api/targets
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
				
				target.url = Utils.INSTANCE.getActUrl(target.getNid());
				target.edit_url = Utils.INSTANCE.getWctUrl(target.vid);
				target.createdAt = Utils.INSTANCE.getDateFromSeconds(target.getCreated());
				FieldModel qaIssueField = target.getField_qa_status();
				try {
					if (qaIssueField != null) {
						QaIssue qaIssue = getQaIssue(qaIssueField);
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
						url = Utils.INSTANCE.validateUrl(url);
						Logger.debug("Checked Url: " + url);
						
						FieldUrl existingFieldUrl = FieldUrl.findByUrl(url);
						FieldUrl fieldUrl = new FieldUrl(url);
						fieldUrl.domain = Scope.INSTANCE.getDomainFromUrl(fieldUrl.url);
						fieldUrls.add(fieldUrl);

						if (existingFieldUrl != null) {
							Logger.debug("CONFLICT existingFieldUrl Url: " + existingFieldUrl.url);
							return status(Http.Status.CONFLICT);
						}
					} catch (UrlInvalidException e) {
						throw new ActException(e);
					}
				}
				
				if (!fieldUrls.isEmpty()) {
					target.fieldUrls = fieldUrls;
//					As for the multiple URL case, the logic should be that there is only one 
//					"UK Hosting", 
//					"UK top-level domain", 
//					"UK Registration" 
//					field, but all URLs have to meet it to get a tick.
//					e.g. if all URLs end '.uk' then "UK top-level domain" is true.
//					field_uk_hosting
//					target.isUkHosting = Scope.INSTANCE.isUkHosting(fieldUrls); // check if UK IP Address
//					field_uk_domain
//					target.isTopLevelDomain = Scope.INSTANCE.isTopLevelDomain(fieldUrls);
//					field_uk_geoip
//					target.isUkRegistration = Scope.INSTANCE.isUkRegistration(fieldUrls);
//					Logger.debug(target.isUkHosting + ", " + target.isTopLevelDomain + ", " + target.isUkRegistration);
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
						Subject subject = getSubject(fieldSubject);
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
					String orgUrl = Utils.INSTANCE.getActUrl(fieldNominatingOrganisation.getId());
					if (StringUtils.isNotEmpty(orgUrl)) {
						target.organisation = Organisation.findByUrl(orgUrl);
					}
				}

				// "field_license":[{"uri":"http://www.webarchive.org.uk/act/taxonomy_term/168","id":"168","resource":"taxonomy_term"}],
				if (target.getField_license() != null) {
					List<FieldModel> licenses = target.getField_license();
					for (FieldModel fieldModel : licenses) {
						try {
							License license = getLicense(fieldModel);
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
							Collection collection = getCollection(fieldModel);
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
					target.crawlStartDate = Utils.INSTANCE.getDateFromSeconds(target.getField_crawl_start_date());
				}
				// "field_crawl_end_date":null,
				if (target.getField_crawl_end_date() != null) {
					target.crawlEndDate = Utils.INSTANCE.getDateFromSeconds(target.getField_crawl_end_date());
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
						String actUrl = Utils.INSTANCE.getActUrl(fieldModel.getId());
						fieldSnapshotsList.add(actUrl);
					}

				}
				
				// "field_instances":[],
				target.getField_instances();

				// "author":{"uri":"http://www.webarchive.org.uk/act/user/9","id":"9","resource":"user"},
				FieldModel author = target.getAuthor();
				if (author != null) {
					User authorUser = User.findByUrl(Utils.INSTANCE.getActUrl(author.getId()));
					target.authorUser = authorUser;
				}

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
				
	        	target.save();
				
				Logger.debug("target: " + target);
				String url = request().host() + Play.application().configuration().getString("application.context") + "/targets/" + target.id;
				Logger.debug("location: " + url);
				response().setHeader(LOCATION, url);
				target.save();
				Logger.debug("response 201 created");
			    return created(response().getHeaders().get(LOCATION));
	    	}
        } catch (IOException e) {
            return Results.internalServerError();
        }
	}
    
	private static Collection getCollection(FieldModel fieldModel) throws IOException, TaxonomyNotFoundException {
		String actUrl = Utils.INSTANCE.getActUrl(fieldModel.getId());
		Collection collection = Collection.findByUrl(actUrl);
		if (collection == null) {
			throw new TaxonomyNotFoundException("No Collection for actUrl: " + actUrl);
		}
		return collection;
	}
	
	private static License getLicense(FieldModel fieldModel) throws IOException, TaxonomyNotFoundException {
		String actUrl = Utils.INSTANCE.getActUrl(fieldModel.getId());
		License license = License.findByUrl(actUrl);
		if (license == null) {
			throw new TaxonomyNotFoundException("No License for actUrl: " + actUrl);
		}
		return license;
	}
	
	private static QaIssue getQaIssue(FieldModel fieldModel) throws IOException, TaxonomyNotFoundException {
		String actUrl = Utils.INSTANCE.getActUrl(fieldModel.getId());
		QaIssue qaIssue = QaIssue.findByUrl(actUrl);
		if (qaIssue == null) {
			throw new TaxonomyNotFoundException("No QaIssue for actUrl: " + actUrl);
		}
		return qaIssue;
	}
	
	private static Subject getSubject(FieldModel fieldModel) throws IOException, TaxonomyNotFoundException {
		String actUrl = Utils.INSTANCE.getActUrl(fieldModel.getId());
		Subject subject = Subject.findByUrl(actUrl);
		if (subject == null) {
			throw new TaxonomyNotFoundException("No Subject for actUrl: " + actUrl);
		}
		return subject;
	}
}

