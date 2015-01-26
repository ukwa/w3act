package controllers;

import java.util.ArrayList;
import java.util.List;

import models.Document;
import models.Portal;

public class Portals {
	
	public static List<Long> getDocumentIds(String service) {
		List<Long> documentIds = new ArrayList<>();
		try {
			List<Document> documents = Portal.find.where()
					.eq("title", service).findUnique().documents;
			for (Document document : documents)
				documentIds.add(document.id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return documentIds;
	}
}

