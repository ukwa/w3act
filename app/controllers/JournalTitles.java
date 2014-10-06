package controllers;

import com.avaje.ebean.Ebean;

import models.Document;
import models.Journal;
import models.JournalTitle;
import models.User;
import play.Logger;
import play.data.Form;
import play.mvc.Result;
import play.mvc.Security;
import views.html.journaltitles.edit;

@Security.Authenticated(Secured.class)
public class JournalTitles extends AbstractController {

	public static Form<JournalTitle> journalTitleForm;

	public static Result index() {
		Logger.info("JournalTitles.index()");

		journalTitleForm = Form.form(JournalTitle.class);

		return ok(edit.render("Journal Title", journalTitleForm,
				User.findByEmail(request().username())));
	}

	public static Result save() {
		Logger.info("JournalTitles.save()");
		
		JournalTitle journalTitle = journalTitleForm.bindFromRequest().get();
		
		Ebean.save(journalTitle);

		return ok();
	}

}
