package controllers;

import org.apache.commons.lang3.StringUtils;

import models.Document;
import play.mvc.Result;
import views.xml.documents.sip_ebook;
import views.xml.documents.sip_ejournal;

public class DocumentSIPController extends AbstractController {
		
	public static Result sip(Long id) {
		Document document = Document.find.byId(id);
		if( StringUtils.isEmpty(document.ark) ) {
			document.ark = "ark:/81055/vdc_200000000820.0x000001";
		}
		if( StringUtils.isEmpty(document.md_ark) ) {
			document.md_ark = "ark:/81055/vdc_200000000820.0x000002";
		}
		if( StringUtils.isEmpty(document.d_ark) ) {
			document.d_ark = "ark:/81055/vdc_200000000820.0x000003";
		}
		if( StringUtils.isEmpty(document.mets_d_ark) ) {
			document.mets_d_ark = "ark:/81055/vdc_200000000820.0x000004";
		}
		response().setContentType("text/xml");
		if( document.isJournalArticleOrIssue()) {
			return ok(sip_ejournal.render(document));
			
		} else {
			return ok(sip_ebook.render(document));
		}
	}
	
}
