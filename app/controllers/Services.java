package controllers;

import com.avaje.ebean.Ebean;

import models.FlashMessage;
import models.License;
import models.User;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Result;
import play.mvc.Security;
import views.html.services.edit;
import views.html.services.list;

@Security.Authenticated(SecuredController.class)
public class Services extends AbstractController {
	
	public static Result edit(Long id) {
		License license = License.find.byId(id);
		DynamicForm form = Form.form().bind(Portals.getFormData(license.portals));
		return ok(edit.render(form, User.findByEmail(request().username()), license));
	}
	
	public static Result save(Long id) {
		DynamicForm form = Form.form().bindFromRequest();
		License license = License.find.byId(id);
		
		license.portals = Portals.getPortals(form);
		
		Ebean.save(license);
		
		FlashMessage.updateSuccess.send();
		return redirect(routes.LicencesController.list());
	}

}
