package controllers;

import com.avaje.ebean.Ebean;

import models.ContactPerson;
import models.CrawlPermission;
import models.FlashMessage;
import models.License;
import models.Target;
import models.User;
import play.data.Form;
import play.mvc.Result;
import play.mvc.Security;
import views.html.licences.list;
import views.html.licences.edit;
import views.html.licence.ukwalicence;

@Security.Authenticated(SecuredController.class)
public class LicencesController extends AbstractController {
	
	public static Result list() {
		return ok(
			list.render(User.findByEmail(request().username()))
		);
	}
	
	public static Result newForm() {
    	User user = User.findByEmail(request().username());
    	if( !user.isSysAdmin() ) {
    		FlashMessage.sysAdminOnlyWarning.send();
    		return redirect(routes.LicencesController.list());
    	}
		Form<License> licenseForm = Form.form(License.class);
		License license = new License();
		licenseForm = licenseForm.fill(license);
        return ok(edit.render(licenseForm, user));
	}
	
	public static Result edit(Long id) {
    	User user = User.findByEmail(request().username());
    	if( !user.isSysAdmin() ) {
    		FlashMessage.sysAdminOnlyWarning.send();
    		return redirect(routes.LicencesController.list());
    	}
		License license = License.find.byId(id);
		Form<License> licenseForm = Form.form(License.class);
		licenseForm = licenseForm.fill(license);
		return ok(edit.render(licenseForm, user));
	}
	
	public static Result save() {
    	User user = User.findByEmail(request().username());
    	if( !user.isSysAdmin() ) {
    		FlashMessage.sysAdminOnlyWarning.send();
    		return redirect(routes.LicencesController.list());
    	}
    	//
		Form<License> licenseForm = Form.form(License.class).bindFromRequest();
		License license = licenseForm.get();
		
		if( license.id == null ) {
			Ebean.save(license);
		} else {
			Ebean.update(license);
		}
		
		FlashMessage.updateSuccess.send();
		return redirect(routes.LicencesController.list());
	}
	
	public static Result preview(Long id) {
		License license = License.find.byId(id);
		CrawlPermission crawlPermission = new CrawlPermission();
		crawlPermission.setLicense(license);
		crawlPermission.target = new Target();
		crawlPermission.contactPerson = new ContactPerson();
		crawlPermission.user =  User.findByEmail(request().username());
		crawlPermission.agree = Boolean.TRUE;
		
		return ok(
				ukwalicence.render(crawlPermission, false)
	        );

	}

}
