package uk.bl.configurable;

import java.util.List;

import models.Portal;

public class PortalList extends ConfigurableList<Portal> {
	
	@Override
	public String getFilePath() {
		return "conf/services.txt";
	}

	@Override
	public Portal createElement(String title) {
		return new Portal(title);
	}

	@Override
	public List<Portal> getCurrentElements() {
		return Portal.find.all();
	}

	@Override
	public List<Portal> getActiveElements() {
		return Portal.find.where().eq("active", true).findList();
	}
		
	
}
