package controllers;

import com.avaje.ebean.Ebean;

import models.Document;
import models.Journal;
import models.User;
import play.Logger;
import play.data.Form;
import play.mvc.Result;
import play.mvc.Security;
import views.html.documents.edit;

@Security.Authenticated(Secured.class)
public class Documents extends AbstractController {

	public static Form<Journal> journalForm;

	public static Result index() {
		Logger.info("Documents.index()");

		journalForm = Form.form(Journal.class);

		return ok(edit.render("Documents", journalForm,
				User.findByEmail(request().username())));
	}

	public static Result save() {
		Logger.info("Documents.save()");
		
		Journal journal = journalForm.bindFromRequest().get();
		
		Ebean.save(journal.document);
		Ebean.save(journal);

		return ok();
	}

}
