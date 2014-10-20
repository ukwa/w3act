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
		Logger.info("JournalTitles.addJournalTitle()");
		
		Target target = Ebean.find(Target.class, targetId);
		
		JournalTitle journalTitle = new JournalTitle();
		journalTitle.target = target;
		Form<JournalTitle> journalTitleForm = Form.form(JournalTitle.class).fill(journalTitle);

		return ok(edit.render("Journal Title", journalTitleForm,
				User.findByEmail(request().username()), toDocument));
	}
	
	public static Result edit(Long id) {
		Logger.info("JournalTitles.edit()");
		
		JournalTitle journalTitle = Ebean.find(JournalTitle.class, id);
		Form<JournalTitle> journalTitleForm = Form.form(JournalTitle.class).fill(journalTitle);

		return ok(edit.render("Journal Title", journalTitleForm,
				User.findByEmail(request().username()), false));
	}

	public static Result save(boolean toDocument) {
		Logger.info("JournalTitles.save()");
		
		String delete = getFormParam("delete");
		
		Form<JournalTitle> journalTitleForm = Form.form(JournalTitle.class).bindFromRequest();
		if (delete != null) {
			JournalTitle journalTitle = Ebean.find(JournalTitle.class, journalTitleForm.apply("id").value());
			Ebean.delete(journalTitle);
			return redirect(routes.Targets.view(journalTitle.target.url));
		}
		
		if (journalTitleForm.hasErrors()) {
			Logger.info("Show errors in html");
			return badRequest(edit.render("Journal Title", journalTitleForm,
					User.findByEmail(request().username()), toDocument));
		}
		
		JournalTitle journalTitle = journalTitleForm.get();
		
		if (journalTitle.id == null)
			Ebean.save(journalTitle);
		else
			Ebean.update(journalTitle);
		
		flash("journalTitle", "" + journalTitle.id);
		if (toDocument)
			return redirect(routes.Documents.continueEdit());
		else
			return redirect(routes.JournalTitles.edit(journalTitle.id));
	}

}
