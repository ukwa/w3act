package controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import play.data.Form;
import uk.bl.configurable.PortalList;
import models.Document;
import models.Portal;

public class Portals {
	
	public static PortalList portalList = new PortalList();
	
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
	
	public static List<Portal> getPortals(Form<?> form) {
		List<Portal> portals = new ArrayList<>();
		for (Portal portal : portalList.getList())
			if (form.apply("portal_" + portal.id).value() != null)
				portals.add(portal);
		return portals;
	}
	
	public static Map<String, String> getFormData(List<Portal> portals) {
		Map<String, String> formData = new HashMap<>();
		for (Portal portal : portals)
			formData.put("portal_" + portal.id, "true");
		return formData;
	}
}

