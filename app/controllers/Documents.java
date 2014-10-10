package controllers;

import java.io.IOException;
import java.util.List;

import com.avaje.ebean.Ebean;

import models.Book;
import models.Document;
import models.Journal;
import models.User;
import play.Logger;
import play.data.Form;
import play.data.validation.ValidationError;
import play.mvc.Result;
import play.mvc.Security;

import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

import uk.bl.api.CrawlData;
import views.html.documents.edit;
import views.html.documents.list;

@Security.Authenticated(Secured.class)
public class Documents extends AbstractController {

	public static Result edit(Long id) {
		Logger.info("Documents.index()");
		
		Document document = Document.findById(id);
			
		try {
			org.jsoup.nodes.Document doc = Jsoup.connect(document.landingPageUrl).get();
			Elements title = doc.select("div#stdPageMeat header.show_header h1");
			if (!title.isEmpty())
				document.title = title.get(0).text();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Form<Document> documentForm = Form.form(Document.class);
		documentForm = documentForm.fill(document);
		
		return ok(edit.render("Document" + id, documentForm,
				User.findByEmail(request().username())));
	}

	public static Result save() {
		Logger.info("Documents.save()");
		
		Form<Document> documentForm = Form.form(Document.class).bindFromRequest();
		Logger.info("Errors: " + documentForm.hasErrors());
		//if (documentForm.apply("type").value)
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
					User.findByEmail(request().username())));
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
			if (document.journal.id == null) {
				Ebean.save(document.journal);
			} else {
				Ebean.update(document.journal);
			}
		}

		return redirect(routes.Documents.edit(document.id));
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
    	
    	if(Ebean.find(Document.class).findRowCount() == 0) {
	    	List<Document> documentList = CrawlData.renameThisMethodLaterAccordingly();
	    	Ebean.save(documentList);
    	}
    	
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
