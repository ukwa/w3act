package controllers;

import com.avaje.ebean.Ebean;

import models.JournalTitle;
import models.User;
import play.Logger;
import play.data.Form;
import play.mvc.Result;
import play.mvc.Security;
import views.html.journaltitles.edit;

@Security.Authenticated(Secured.class)
public class JournalTitles extends AbstractController {

	public static Result addJournalTitle() {
		Logger.info("JournalTitles.index()");

		Form<JournalTitle> journalTitleForm = Form.form(JournalTitle.class);

		return ok(edit.render("Journal Title", journalTitleForm,
				User.findByEmail(request().username())));
	}

	public static Result save() {
		Logger.info("JournalTitles.save()");
		
		String target = getFormParam("target");
		
		Form<JournalTitle> journalTitleForm = Form.form(JournalTitle.class).bindFromRequest();
		if (journalTitleForm.hasErrors()) {
			Logger.info("Show errors in html");
			return badRequest(edit.render("Journal Title", journalTitleForm,
					User.findByEmail(request().username())));
		}
		
		JournalTitle journalTitle = journalTitleForm.get();
		
		Ebean.save(journalTitle);
		
		flash("journalTitle", "" + journalTitle.id);
		return redirect(routes.Documents.continueEdit());
	}

}
