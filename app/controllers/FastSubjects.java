package controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.avaje.ebean.Ebean;

import models.FastSubject;
import models.FlashMessage;
import models.User;
import play.data.Form;
import play.mvc.Result;
import play.mvc.Security;
import views.html.fastsubjects.edit;
import views.html.fastsubjects.list;

@Security.Authenticated(SecuredController.class)
public class FastSubjects extends AbstractController {
	
	public static Result list(int pageNo, String sortBy, String order, String filter) {
		return ok(
			list.render(
					User.findByEmail(request().username()),
					filter,
					FastSubject.page(pageNo, 20, sortBy, order, filter),
					sortBy,
					order)
			);
	}
	
	private static Result renderEdit(FastSubject fastSubject) {
		Form<FastSubject> fastSubjectForm = Form.form(FastSubject.class).fill(fastSubject);
		
		return ok(edit.render(fastSubjectForm,
				User.findByEmail(request().username())));
	}
	
	public static Result edit(Long id) {
		return renderEdit(FastSubject.find.byId(id));
	}
	
	public static Result delete(Long id) {
		FastSubject fastSubject = FastSubject.find.byId(id);
		Ebean.delete(fastSubject);
		FlashMessage.updateSuccess.send();
		return redirect(routes.FastSubjects.list(0, "name", "asc", ""));
	}
	
	public static Result newForm() {
		return renderEdit(new FastSubject());
	}
	
	public static Result save() {
		Form<FastSubject> fastSubjectForm = Form.form(FastSubject.class).bindFromRequest();
		FastSubject fastSubject = fastSubjectForm.get();
		if (fastSubject.id == null)
			Ebean.save(fastSubject);
		else
			Ebean.update(fastSubject);
		FlashMessage.updateSuccess.send();
		return redirect(routes.FastSubjects.edit(fastSubject.id));
	}
	
	public static List<FastSubject> getFastSubjects(Form<?> form) {
		List<FastSubject> fastSubjects = new ArrayList<>();
		for (FastSubject fastSubject : FastSubject.find.all())
			if (form.apply(fastSubject.fastId).value() != null)
				fastSubjects.add(fastSubject);
		return fastSubjects;
	}
	
	public static Map<String, String> getFormData(List<FastSubject> fastSubjects) {
		Map<String, String> formData = new HashMap<>();
		for (FastSubject fastSubject : fastSubjects)
			formData.put(fastSubject.fastId, "true");
		return formData;
	}

}
