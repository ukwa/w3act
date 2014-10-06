package controllers;

import java.util.HashMap;
import java.util.Map;

import com.avaje.ebean.Ebean;

import models.Document;
import models.Journal;
import models.Tag;
import models.User;
import play.Logger;
import play.data.Form;
import play.mvc.Result;
import play.mvc.Security;
import uk.bl.Const;
import views.html.documents.edit;
import views.html.documents.list;

@Security.Authenticated(Secured.class)
public class Documents extends AbstractController {

	public static Form<Journal> journalForm;

	public static Result edit(Long id) {
		Logger.info("Documents.index()");
		
		Document document = Document.findById(id);
		
		journalForm = Form.form(Journal.class);
		
		journalForm = journalForm.fill(new Journal(document));
		
		return ok(edit.render("Document" + id, journalForm,
				User.findByEmail(request().username())));
	}

	public static Result save() {
		Logger.info("Documents.save()");
		
		Journal journal = journalForm.bindFromRequest().get();
		journal.document.filename = "tmp.pdf";
		
		Ebean.save(journal.document);
		Ebean.save(journal);

		return ok();
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
