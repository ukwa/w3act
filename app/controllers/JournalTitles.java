package controllers;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.ExpressionList;

import models.BlCollectionSubset;
import models.FlashMessage;
import models.JournalTitle;
import models.Taxonomy;
import models.User;
import models.WatchedTarget;
import play.Logger;
import play.data.Form;
import play.mvc.Result;
import play.mvc.Security;
import uk.bl.Const;
import views.html.journaltitles.edit;

@Security.Authenticated(SecuredController.class)
public class JournalTitles extends AbstractController {
	
	public static Result addJournalTitle(Long watchedTargetId, boolean toDocument) {
		Logger.info("JournalTitles.addJournalTitle()");
		
		WatchedTarget watchedTarget = Ebean.find(WatchedTarget.class, watchedTargetId);
		
		JournalTitle journalTitle = new JournalTitle();
		journalTitle.watchedTarget = watchedTarget;
		journalTitle.language = Const.JOURNAL_TITLE_LANGUAGE;
		//TODO: adapt
		//journalTitle.subject = watchedTarget.target.field_subject;
		Form<JournalTitle> journalTitleForm = Form.form(JournalTitle.class).fill(journalTitle);

		return ok(edit.render("Journal Title", journalTitleForm,
				User.findByEmail(request().username()), toDocument));
	}
	
	public static Result edit(Long id) {
		Logger.info("JournalTitles.edit()");
		
		JournalTitle journalTitle = Ebean.find(JournalTitle.class, id);
		//TODO: adapt
		//journalTitle.subject = TaxonomyController.serializeTaxonomies(journalTitle.taxonomies);
		Form<JournalTitle> journalTitleForm = Form.form(JournalTitle.class).fill(journalTitle);
		setBlCollectionSubsetsOfView(journalTitleForm, journalTitle);

		return ok(edit.render("Journal Title", journalTitleForm,
				User.findByEmail(request().username()), false));
	}
	
	private static void setBlCollectionSubsetsOfModel(JournalTitle journalTitle, Form<JournalTitle> journalTitleForm) {
		for (BlCollectionSubset blCollectionSubset : Documents.blCollectionSubsetList.getList())
			if (journalTitleForm.apply("blCollectionSubset_" + blCollectionSubset.id).value() != null)
				journalTitle.blCollectionSubsets.add(blCollectionSubset);
	}
	
	private static void setBlCollectionSubsetsOfView(Form<JournalTitle> journalTitleForm, JournalTitle journalTitle) {
		for (BlCollectionSubset portal : journalTitle.blCollectionSubsets)
			journalTitleForm.data().put("blCollectionSubset_" + portal.id, "true");
	}

	public static Result save(boolean toDocument) {
		Logger.info("JournalTitles.save()");
		
		String delete = getFormParam("delete");
		
		Form<JournalTitle> journalTitleForm = Form.form(JournalTitle.class).bindFromRequest();
		if (delete != null) {
			JournalTitle journalTitle = Ebean.find(JournalTitle.class, journalTitleForm.apply("id").value());
			Ebean.delete(journalTitle);
			return redirect(routes.TargetController.view(journalTitle.watchedTarget.target.id));
		}
		
		if (journalTitleForm.hasErrors()) {
			Logger.info("Show errors in html");
			FlashMessage.validationWarning.send();
			return status(303, edit.render("Journal Title", journalTitleForm,
					User.findByEmail(request().username()), toDocument));
		}
		
		JournalTitle journalTitle = journalTitleForm.get();
		//TODO: adapt
		//journalTitle.taxonomies = Taxonomy.convertUrlsToObjects(journalTitle.subject);
		
		ExpressionList<JournalTitle> expressionList = JournalTitle.find.where()
				.eq("id_watched_target", journalTitle.watchedTarget.id)
				.eq("title", journalTitle.title);
		if (journalTitle.id != null)
			expressionList = expressionList.ne("id", journalTitle.id);
		boolean titleExists = expressionList.findRowCount() > 0;
		if (titleExists) {
			new FlashMessage(FlashMessage.Type.ERROR, "This journal title is already defined for the target.").send();
			return status(303, edit.render("Journal Title", journalTitleForm,
					User.findByEmail(request().username()), toDocument));
		}
		
		setBlCollectionSubsetsOfModel(journalTitle, journalTitleForm);
		
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
