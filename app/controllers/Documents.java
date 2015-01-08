package controllers;

import java.io.IOException;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.avaje.ebean.Ebean;
import com.fasterxml.jackson.databind.JsonNode;

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
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Result;
import play.mvc.Security;
import play.Play;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

import uk.bl.Const;
import views.html.documents.edit;
import views.html.documents.list;

@Security.Authenticated(Secured.class)
public class Documents extends AbstractController {
	
	public static Result view(Long id) {
		return render(id, false);
	}
	
	public static Result edit(Long id) {
		return render(id, true);
	}
	
	private static Result render(Long id, boolean editable) {
		Logger.info("Documents.render()");
		
		Document document = getDocumentFromDB(id);
		document.htmlFilename = document.filename.split("\\.")[0] + ".html";
		if (document.submitted) editable = false;
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
		
		WatchedTarget watchedTarget = WatchedTarget.find.byId(new Long(documentForm.apply("watchedTarget.id").value()));
		
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
		document.submitted = true;
		Ebean.save(document);
		return redirect(routes.Documents.view(id));
	}
	
	public static Document getDocumentFromDB(long id) {
		Document document = Ebean.find(Document.class, id);
		if (document.type == null) document.type = "";
		if (document.journal != null)
			document.journal.journalTitleId = document.journal.journalTitle.id;
			
		try {
			org.jsoup.nodes.Document doc = Jsoup.connect(document.landingPageUrl).get();
			Elements title = doc.select("div#stdPageMeat header.show_header h1");
			if (!title.isEmpty())
				document.title = title.get(0).text();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
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
		if (Portal.find.findRowCount() == 0) {
			Ebean.save(new Portal("Business"));
			Ebean.save(new Portal("SWP"));
			Ebean.save(new Portal("STM"));
			Ebean.save(new Portal("Other"));
		}
		return Portal.find.all();
	}
	
    /**
     * Display the paginated list of Documents.
     *
     * @param page Current page number (starts from 0)
     * @param sortBy Column to be sorted
     * @param order Sort order (either asc or desc)
     * @param filter Filter applied on Documents
     */
    public static Result list(Long watchedTargetId, boolean submitted, int pageNo, String sortBy, String order, String filter) {
    	Logger.info("Documents.list()");
    	
        return ok(
        	list.render(
        			WatchedTarget.find.byId(watchedTargetId),
        			User.findByEmail(request().username()),
        			submitted,
        			filter,
        			Document.page(watchedTargetId, submitted, pageNo, 10, sortBy, order, filter),
        			sortBy,
        			order)
        	);
    }

    public static Result search() {
    	DynamicForm form = Form.form().bindFromRequest();
    	String action = form.get("action");
    	String query = form.get(Const.QUERY);
		Logger.info("query: " + query);
		Logger.info("action: " + action);
    	
    	if (StringUtils.isBlank(query)) {
			Logger.info("Document name is empty. Please write name in search window.");
			flash("message", "Please enter a name in the search window");
	        return redirect(
	        		routes.Documents.list(new Long(form.get("watchedTarget.id")), true, 0, "title", "asc", "")
	        );
    	}

    	int pageNo = getQueryParamAsInt(Const.PAGE_NO, 0);
    	String sort = getQueryParam(Const.SORT_BY);
    	String order = getQueryParam(Const.ORDER);

    	if (StringUtils.isEmpty(action)) {
    		return badRequest("You must provide a valid action");
    	} else {
    		if (Const.SEARCH.equals(action)) {
    	    	return redirect(routes.Documents.list(new Long(form.get("watchedTarget.id")), true, pageNo, sort, order, query));
		    } else {
		      return badRequest("This action is not allowed");
		    }
    	}
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return ok();
	}

}
