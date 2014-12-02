package controllers;

import java.util.List;

import com.avaje.ebean.Ebean;

import models.FlashMessage;
import models.JournalTitle;
import models.Target;
import models.Taxonomy;
import models.User;
import play.Logger;
import play.data.Form;
import play.mvc.Result;
import play.mvc.Security;
import uk.bl.Const;
import views.html.journaltitles.edit;

@Security.Authenticated(Secured.class)
public class JournalTitles extends AbstractController {

	public static Result addJournalTitle(Long targetId, boolean toDocument) {
		Logger.info("JournalTitles.addJournalTitle()");
		
		Target target = Ebean.find(Target.class, targetId);
		
		JournalTitle journalTitle = new JournalTitle();
		journalTitle.target = target;
		journalTitle.language = Const.JOURNAL_TITLE_LANGUAGE;
		journalTitle.subject = target.field_subject;
		Form<JournalTitle> journalTitleForm = Form.form(JournalTitle.class).fill(journalTitle);

		return ok(edit.render("Journal Title", journalTitleForm,
				User.findByEmail(request().username()), toDocument));
	}
	
	public static Result edit(Long id) {
		Logger.info("JournalTitles.edit()");
		
		JournalTitle journalTitle = Ebean.find(JournalTitle.class, id);
		journalTitle.subject = serializeTaxonomies(journalTitle.taxonomies);
		Form<JournalTitle> journalTitleForm = Form.form(JournalTitle.class).fill(journalTitle);

		return ok(edit.render("Journal Title", journalTitleForm,
				User.findByEmail(request().username()), false));
	}
	
	public static String serializeTaxonomies(List<Taxonomy> taxonomies) {
		String subject = "";
		for (Taxonomy taxonomy : taxonomies) {
			if (!subject.isEmpty()) subject += ", ";
			subject += taxonomy.url;
		}
		return subject;
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
			FlashMessage.validationWarning.send();
			return status(303, edit.render("Journal Title", journalTitleForm,
					User.findByEmail(request().username()), toDocument));
		}
		
		JournalTitle journalTitle = journalTitleForm.get();
		journalTitle.taxonomies = Taxonomy.convertUrlsToObjects(journalTitle.subject);
		
		if (journalTitle.id == null)
			Ebean.save(journalTitle);
		else
			Ebean.update(journalTitle);
		
		flash("journalTitle", "" + journalTitle.id);
		if (toDocument)
			return redirect(routes.Documents.continueEdit());
		else {
			FlashMessage.updateSuccess.send();
			return redirect(routes.JournalTitles.edit(journalTitle.id));
		}
	}

}
