package controllers;

import org.apache.commons.lang3.StringUtils;

import models.Document;
import play.mvc.Result;
import views.xml.documents.sip;

public class DocumentSIPController extends AbstractController {
		
	public static Result sip(Long id) {
		Document document = Document.find.byId(id);
		if( StringUtils.isEmpty(document.ark) ) {
			document.ark = "ark://PLACEHOLDER";
		}
		response().setContentType("text/xml");
		return ok(sip.render(document));
	}
	
}
