package controllers;

import models.Document;
import models.User;
import play.Logger;
import play.data.Form;
import play.mvc.Result;
import play.mvc.Security;
import views.html.documents.edit;

@Security.Authenticated(Secured.class)
public class Documents extends AbstractController {
  
    public static Result index() {
    	Logger.info("Documents.index()");
    	
    	Form<Document> documentForm = Form.form(Document.class);
    	
        return ok(edit.render("Documents", documentForm, User.findByEmail(request().username()))
    	);
    }
    
    public static Result save() {
    	Logger.info("Documents.save()");
    	
    	Form<Document> documentForm = Form.form(Document.class);
    	
        return ok();
    }

    
}

