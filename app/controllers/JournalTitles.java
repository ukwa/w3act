package controllers;

import com.avaje.ebean.Ebean;

import models.JournalTitle;
import models.Target;
import models.User;
import play.Logger;
import play.data.Form;
import play.mvc.Result;
import play.mvc.Security;
import views.html.journaltitles.edit;

@Security.Authenticated(Secured.class)
public class JournalTitles extends AbstractController {

	public static Result addJournalTitle(Long targetId, boolean toDocument) {
		Logger.info("JournalTitles.index()");
		
		Target target = Ebean.find(Target.class, targetId);
		
		JournalTitle journalTitle = new JournalTitle();
		journalTitle.target = target;
		Form<JournalTitle> journalTitleForm = Form.form(JournalTitle.class).fill(journalTitle);

		return ok(edit.render("Journal Title", journalTitleForm,
				User.findByEmail(request().username()), toDocument));
	}

	public static Result save(boolean toDocument) {
		Logger.info("JournalTitles.save()");
		
		String target = getFormParam("target");
		
		Form<JournalTitle> journalTitleForm = Form.form(JournalTitle.class).bindFromRequest();
		if (journalTitleForm.hasErrors()) {
			Logger.info("Show errors in html");
			return badRequest(edit.render("Journal Title", journalTitleForm,
					User.findByEmail(request().username()), toDocument));
		}
		
		JournalTitle journalTitle = journalTitleForm.get();
		
		Ebean.save(journalTitle);
		
		flash("journalTitle", "" + journalTitle.id);
		if (toDocument)
			return redirect(routes.Documents.continueEdit());
		else
			return redirect(routes.Targets.view(journalTitle.target.url));
	}

}
