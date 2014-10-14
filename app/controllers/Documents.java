package controllers;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.avaje.ebean.Ebean;

import models.Book;
import models.Document;
import models.Journal;
import models.JournalTitle;
import models.User;
import play.Logger;
import play.data.Form;
import play.data.validation.ValidationError;
import play.mvc.Result;
import play.mvc.Security;

import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

import views.html.documents.edit;
import views.html.documents.list;

@Security.Authenticated(Secured.class)
public class Documents extends AbstractController {
	
	/*public static List<String> modifiableFieldList = Arrays.asList("id", "title", "doi", "publicationDate",
			"filename", "type", "journal.journalTitleId", "journal.publicationYear",
			"journal.volume", "journal.issue", "journal.author");*/
	
	public static Result edit(Long id) {
		Logger.info("Documents.index()");
		
		Form<Document> documentForm = Form.form(Document.class);
		
		if (flash("journalTitle") != null) {
			session("journal.journalTitleId", flash("journalTitle"));
			documentForm = documentForm.bind(session());
			documentForm.discardErrors();
		} else {
			Document document = getDocumentFromDB(id);
			documentForm = documentForm.fill(document);
		}
		
		return ok(edit.render("Document" + id, documentForm,
				User.findByEmail(request().username()), getJournalTitles()));
	}

	public static Result save() {
		Logger.info("Documents.save()");
		
		String journalTitle = getFormParam("journalTitle");
		
		Form<Document> documentForm = Form.form(Document.class).bindFromRequest();
		
		if (journalTitle != null) {
			session().putAll(documentForm.data());
			return redirect(routes.JournalTitles.addJournalTitle());
		} else {
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
						User.findByEmail(request().username()), getJournalTitles()));
			}
			Logger.info("Glob Errors: " + documentForm.hasGlobalErrors());
			Document document = documentForm.get();
			Ebean.update(document);
			
			if (!document.type.toLowerCase().startsWith("book") && document.book.id != null) {
				Book book = Ebean.find(Book.class, document.book.id);
				Ebean.delete(book);
			}
			if (!document.type.toLowerCase().startsWith("journal") && document.journal.id != null) {
				Journal journal = Ebean.find(Journal.class, document.journal.id);
				Ebean.delete(journal);
			}
			
			if (document.type.toLowerCase().startsWith("book")) {
				document.book.document = document;
				if (document.book.id == null) {
					Ebean.save(document.book);
				} else {
					Ebean.update(document.book);
				}
			} else if (document.type.toLowerCase().startsWith("journal")) {
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
	}
	
	public static Document getDocumentFromDB(long id) {
		Document document = Ebean.find(Document.class, id);
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
    public static Result list(int pageNo, String sortBy, String order, String filter) {
    	Logger.info("Documents.list()");
    	
        return ok(
        	list.render(
        			"Documents", 
        			User.findByEmail(request().username()), 
        			filter, 
        			Document.page(pageNo, 10, sortBy, order, filter), 
        			sortBy, 
        			order)
        	);
    }    

}
