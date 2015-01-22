package controllers;

import play.mvc.Result;

import views.xml.documents.pii;

public class Pii extends AbstractController {
	
	public static Result pii(int numberOfArks) {
		
		return ok(pii.render());
	}

}
