package controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.ExpressionList;
import com.fasterxml.jackson.databind.JsonNode;

import models.Book;
import models.Document;
import models.DocumentType;
import models.Journal;
import models.JournalTitle;
import models.Tag;
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

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

import uk.bl.Const;
import uk.bl.api.CrawlData;
import views.html.documents.edit;
import views.html.documents.list;

@Security.Authenticated(Secured.class)
public class Documents extends AbstractController {
	
	/*public static List<String> modifiableFieldList = Arrays.asList("id", "title", "doi", "publicationDate",
			"filename", "type", "journal.journalTitleId", "journal.publicationYear",
			"journal.volume", "journal.issue", "journal.author");*/
	
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
		
		return ok(edit.render("Document" + id, documentForm,
				User.findByEmail(request().username()), getJournalTitles(), editable));
	}
	
	public static Result continueEdit() {
		Logger.info("Documents.continueEdit()");
		
		if (flash("journalTitle") != null)
			session("journal.journalTitleId", flash("journalTitle"));
		
		Form<Document> documentForm = Form.form(Document.class).bind(session());
		documentForm.discardErrors();
		
		return ok(edit.render("Document", documentForm,
				User.findByEmail(request().username()), getJournalTitles(), true));
	}

	public static Result save() {
		Logger.info("Documents.save()");
		
		String journalTitle = getFormParam("journalTitle");
		
		Form<Document> documentForm = Form.form(Document.class).bindFromRequest();
		
		if (journalTitle != null) {
			session().putAll(documentForm.data());
			return redirect(routes.JournalTitles.addJournalTitle(
					new Long(documentForm.apply("watchedTarget.target.nid").value()), true));
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
			return badRequest(edit.render("Document", documentForm,
					User.findByEmail(request().username()), getJournalTitles(), true));
		}
		Logger.info("Glob Errors: " + documentForm.hasGlobalErrors());
		Document document = documentForm.get();
		document.clearImproperFields();
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
	
	public static Map<String, String> getJournalTitles() {
		List<JournalTitle> journalTitles = Ebean.find(JournalTitle.class).findList();
		Map<String, String> titles = new LinkedHashMap<>();
		titles.put("","");
		for (JournalTitle journalTitle : journalTitles)
			titles.put(""+journalTitle.id, journalTitle.title);
		return titles;
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
    	
    	/*List<WatchedTarget> watchedTargets = Arrays.asList(
    			new WatchedTarget("http://www.ifs.org.uk/publications/re", false),
    			new WatchedTarget("http://www.thinknpc.org/publications/", true),
    			new WatchedTarget("http://www.ofsted.gov.uk/resources/surveys", false),
    			new WatchedTarget("http://www.parliament.uk/business/committees/committees-a-z/commons-select/home-affairs-committee/publications/", false),
    			new WatchedTarget("https://www.gov.uk/government/publications", false)
    			);
    	
    	for (WatchedTarget watchedTarget : watchedTargets) {
    		List<Document> documentList = CrawlData.crawlForDocuments(watchedTarget);
    		Ebean.save(documentList);
    	}*/
    	
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

}
