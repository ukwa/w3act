package controllers;

import models.Document;
import play.mvc.Result;

import views.xml.documents.sip;

public class DocumentSIPController extends AbstractController {
		
	public static Result sip(Long id) {
		Document document = Document.find.byId(id);
		response().setContentType("text/xml");
		return ok(sip.render(document));
	}
	
}
