package controllers;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Scanner;

import com.avaje.ebean.Ebean;
import com.fasterxml.jackson.databind.JsonNode;

import models.AssignableArk;
import models.Book;
import models.Document;
import models.FlashMessage;
import models.Journal;
import models.JournalTitle;
import models.Portal;
import models.User;
import models.WatchedTarget;
import play.Logger;
import play.data.DynamicForm;
import play.data.Form;
import play.data.validation.ValidationError;
import play.libs.F.Function;
import play.libs.F.Promise;
import play.libs.Json;
import play.libs.ws.WS;
import play.libs.ws.WSRequestHolder;
import play.libs.ws.WSResponse;
import play.libs.XPath;
import play.mvc.BodyParser;
import play.mvc.Result;
import play.mvc.Security;
import play.Play;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import uk.bl.Const;
import views.html.documents.edit;
import views.html.documents.list;
import views.xml.documents.sip;

@Security.Authenticated(Secured.class)
public class Documents extends AbstractController {
	
	public static long lastUpdatePortals;
	
	public static Result view(Long id) {
		return render(id, false);
	}
	
	public static Result edit(Long id) {
		return render(id, true);
	}
	
	private static Result render(Long id, boolean editable) {
		Logger.info("Documents.render()");
		
		Document document = getDocumentFromDB(id);
		if (document.status == Document.Status.SUBMITTED) editable = false;
		Form<Document> documentForm = Form.form(Document.class).fill(document);
		setPortalsOfView(documentForm, document);
		
		return ok(edit.render("Document" + id, documentForm,
				User.findByEmail(request().username()), editable));
	}
	
	public static Result continueEdit() {
		Logger.info("Documents.continueEdit()");
		
		if (flash("journalTitle") != null)
			session("journal.journalTitleId", flash("journalTitle"));
		
		Form<Document> documentForm = Form.form(Document.class).bind(session());
		documentForm.discardErrors();
		
		return ok(edit.render("Document", documentForm,
				User.findByEmail(request().username()), true));
	}

	public static Result save() {
		Logger.info("Documents.save()");
		
		String journalTitle = getFormParam("journalTitle");
		
		Form<Document> documentForm = Form.form(Document.class).bindFromRequest();
		
		if (journalTitle != null) {
			session().putAll(documentForm.data());
			return redirect(routes.JournalTitles.addJournalTitle(
					new Long(documentForm.apply("watchedTarget.id").value()), true));
		}
		
		Logger.info("Errors: " + documentForm.hasErrors());
		for (String key : documentForm.errors().keySet()) {
			Logger.info("Key: " + key);
			for (ValidationError error : documentForm.errors().get(key)) {
				for (String message : error.messages()) {
					Logger.info("Message: " + message);
				}
			}
		}
		if (documentForm.hasErrors()) {
			Logger.info("Show errors in html");
			FlashMessage.validationWarning.send();
			return status(303, edit.render("Document", documentForm,
					User.findByEmail(request().username()), true));
		}
		Logger.info("Glob Errors: " + documentForm.hasGlobalErrors());
		Document document = documentForm.get();
		document.clearImproperFields();
		setPortalsOfModel(document, documentForm);
		Ebean.update(document);
		
		if (!document.isBookOrBookChapter() && document.book.id != null) {
			Book book = Ebean.find(Book.class, document.book.id);
			Ebean.delete(book);
		}
		if (!document.isJournalArticleOrIssue() && document.journal.id != null) {
			Journal journal = Ebean.find(Journal.class, document.journal.id);
			Ebean.delete(journal);
		}
		
		if (document.isBookOrBookChapter()) {
			document.book.document = document;
			if (document.book.id == null) {
				Ebean.save(document.book);
			} else {
				Ebean.update(document.book);
			}
		} else if (document.isJournalArticleOrIssue()) {
			document.journal.document = document;
			document.journal.journalTitle = Ebean.find(JournalTitle.class, document.journal.journalTitleId);
			if (document.journal.id == null) {
				Ebean.save(document.journal);
			} else {
				Ebean.update(document.journal);
			}
		}
		
		FlashMessage.updateSuccess.send();
		
		return redirect(routes.Documents.edit(document.id));
	}
	
	public static Result submit(Long id) {
		Document document = Document.find.byId(id);
		List<AssignableArk> assignableArks = AssignableArk.find.all();
		if (assignableArks.isEmpty()) {
			requestNewArks();
			assignableArks = AssignableArk.find.all();
			if (assignableArks.isEmpty()) {
				FlashMessage arkError = new FlashMessage(FlashMessage.Type.ERROR,
						"Submission failed! It was not possible to get an ARK identifier.");
				arkError.send();
				return redirect(routes.Documents.view(id));
			}
		}
		document.ark = assignableArks.get(0).ark;
		Ebean.delete(assignableArks.get(0));
		document.status = Document.Status.SUBMITTED;
		Ebean.save(document);
		deleteHtmlFile(document.getHtmlFilename());
		return redirect(routes.Documents.view(id));
	}
	
	private static void requestNewArks() {
		String piiUrl = Play.application().configuration().getString("pii_url");
		WSRequestHolder holder = WS.url(piiUrl);

		Promise<List<AssignableArk>> arksPromise = holder.get().map(
				new Function<WSResponse, List<AssignableArk>>() {
					public List<AssignableArk> apply(WSResponse response) {
						System.out.println("xml: " + response.getBody());
						org.w3c.dom.Document xml = response.asXml();
						List<AssignableArk> arks = new ArrayList<>();
						if (xml != null) {
							NodeList nodes = XPath.selectNodes("/pii/results/arkList/ark", xml);
							for (int i=0; i < nodes.getLength(); i++) {
								Node node = nodes.item(i);
								arks.add(new AssignableArk(node.getTextContent()));
							}
						}
						return arks;
					}
				}
		);
		
		List<AssignableArk> arks = arksPromise.get(5000);
		Ebean.save(arks);
	}

	public static Result ignore(Long id, String userString, String watchedTargetString, String statusString,
    		int pageNo, String sortBy, String order, String filter, boolean filters) {
		Document document = Document.find.byId(id);
		if (statusString.equals(Document.Status.NEW.toString()))
			document.status = Document.Status.IGNORED;
		else
			document.status = Document.Status.NEW;
		Ebean.save(document);
		if (filters)
			return redirect(routes.Documents.list(userString, watchedTargetString,
					statusString, pageNo, sortBy, order, filter));
		else
			return redirect(routes.Documents.overview(pageNo, sortBy, "asc"));
			
	}
	
	public static Result sip(Long id) {
		Document document = Document.find.byId(id);
		return ok(sip.render(document));
	}
	
	public static void addHash(Document document) throws FileNotFoundException {
		File file = Play.application().getFile("conf/converter/" + document.id + ".sha256");
		Scanner scanner = new Scanner(file);
		String sha1Hash = scanner.next();
		scanner.close();
		document.sha256Hash = sha1Hash;
		Ebean.save(document);
	}
	
	public static Document getDocumentFromDB(long id) {
		Document document = Ebean.find(Document.class, id);
		if (document.type == null) document.type = "";
		if (document.journal != null)
			document.journal.journalTitleId = document.journal.journalTitle.id;		
		return document;
	}
	
	public static Map<String, String> getJournalTitles(Form<Document> form) {
		WatchedTarget watchedTarget = WatchedTarget.find.byId(new Long(form.apply("watchedTarget.id").value()));
		Map<String, String> titles = new LinkedHashMap<>();
		titles.put("","");
		for (JournalTitle journalTitle : watchedTarget.journalTitles)
			titles.put(""+journalTitle.id, journalTitle.title);
		return titles;
	}
	
	private static void setPortalsOfModel(Document document, Form<Document> documentForm) {
		for (Portal portal : getPortals())
			if (documentForm.apply("portal_" + portal.id).value() != null)
				document.portals.add(portal);
	}
	
	private static void setPortalsOfView(Form<Document> documentForm, Document document) {
		for (Portal portal : document.portals)
			documentForm.data().put("portal_" + portal.id, "true");
	}
	
	public static List<Portal> getPortals() {
		long quiteRecent = System.currentTimeMillis() - 60 * 1000;
		if (lastUpdatePortals < quiteRecent)
			updatePortals();
		return Portal.find.where().eq("active", true).findList();
	}
	
	private static void updatePortals() {
		Logger.info("update services");
		List<Portal> oldPortals = Portal.find.all();
		Set<Portal> newPortals = new HashSet<>();
		File file = Play.application().getFile("conf/services.txt");
		try {
			Scanner scanner = new Scanner(file);
			scanner.useDelimiter("[\r\n]+");
			while (scanner.hasNext())
				newPortals.add(new Portal(scanner.next().trim()));
			scanner.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		for (Portal oldPortal : oldPortals) {
			if (oldPortal.active != newPortals.contains(oldPortal)) {
				oldPortal.active = newPortals.contains(oldPortal);
				Ebean.update(oldPortal);
			}
			if (newPortals.contains(oldPortal))
				newPortals.remove(oldPortal);
		}
		
		Ebean.save(newPortals);
		lastUpdatePortals = System.currentTimeMillis();
	}
	
    /**
     * Display the paginated list of Documents.
     *
     * @param page Current page number (starts from 0)
     * @param sortBy Column to be sorted
     * @param order Sort order (either asc or desc)
     * @param filter Filter applied on Documents
     */
	public static Result list(String userString, String watchedTargetString, String statusString,
			int pageNo, String sortBy, String order, String filter) {
		Document.Status status = Document.Status.valueOf(statusString);
		return renderList(userString, watchedTargetString, status, pageNo, sortBy, order, filter, true);
	}
	
    public static Result overview(int pageNo, String sortBy, String order) {
    	return renderList("" + User.findByEmail(request().username()).uid, "", Document.Status.NEW, pageNo, sortBy, order, "", false);
    }
    
    public static Result renderList(String userString, String watchedTargetString, Document.Status status,
    		int pageNo, String sortBy, String order, String filter, boolean filters) {
    	Logger.info("Documents.list()");

    	Long watchedTargetId = watchedTargetString.isEmpty() || watchedTargetString.equals("null") ?
    			null : new Long(watchedTargetString);
    	
    	Long userId;
    	if (watchedTargetId == null) {
    		userId = userString.isEmpty() || userString.equals("null") ?
    				null : new Long(userString);
    	} else {
    		userId = WatchedTarget.find.byId(watchedTargetId).user.uid;
    	}
    	
        return ok(
        	list.render(
        			User.findByEmail(request().username()),
        			filterForm(userId, watchedTargetId),
        			status,
        			filter,
        			Document.page(userId, watchedTargetId, status, pageNo, 10, sortBy, order, filter),
        			sortBy,
        			order,
        			filters)
        	);
    }
    
    @BodyParser.Of(BodyParser.Json.class)
    public static Result filterByJson(String title) {
        JsonNode jsonData = null;
        if (title != null) {
	        List<Document> documents = Document.find.where().icontains("title", title).findList();
	        jsonData = Json.toJson(documents);
        }
        return ok(jsonData);
    }
    
    public static Result html(String encodedFilename) {
		try {
			String filename = URLDecoder.decode(encodedFilename, "UTF-8");
			File file = Play.application().getFile("../html/" + filename);
			return ok(file, filename);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return ok("There is a problem with the encoding.");
		} catch (Exception e) {
			return ok("This file was not found on the system.");
		}
	}
    
	public static void deleteHtmlFiles(WatchedTarget watchedTarget) {
		for (Document document : watchedTarget.documents)
			deleteHtmlFile(document.getHtmlFilename());
	}
    
	public static void deleteHtmlFile(String filename) {
		File file = Play.application().getFile("../html/" + filename);
		file.delete();
	}
	
	public static DynamicForm filterForm(Long userId, Long targetId) {
    	Map<String,String> filterData = new HashMap<>();
    	filterData.put("curator", "" + userId);
    	filterData.put("watchedtarget", "" + targetId);
    	return Form.form().bind(filterData);
    }

}
