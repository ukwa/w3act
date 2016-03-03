package controllers;

import org.apache.commons.lang3.StringUtils;

import models.Document;
import play.mvc.Result;
import views.xml.documents.sip;

public class DocumentSIPController extends AbstractController {
		
	public static Result sip(Long id) {
		Document document = Document.find.byId(id);
		if( StringUtils.isEmpty(document.ark) ) {
			document.ark = "ark://LOGICAL_ARK_PLACEHOLDER";
		}
		if( StringUtils.isEmpty(document.md_ark) ) {
			document.md_ark = "ark://METADATA_ARK_PLACEHOLDER";
		}
		if( StringUtils.isEmpty(document.d_ark) ) {
			document.d_ark = "ark://DATA_ARK_PLACEHOLDER";
		}
		response().setContentType("text/xml");
		return ok(sip.render(document));
	}
	
}
