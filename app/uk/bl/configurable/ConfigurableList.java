package uk.bl.configurable;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import com.avaje.ebean.Ebean;

import play.Logger;
import play.Play;

public abstract class ConfigurableList<T extends Configurable> {
	
	public long lastUpdate;
	
	public List<T> getList() {
		long quiteRecent = System.currentTimeMillis() - 60 * 1000;
		if (lastUpdate < quiteRecent)
			update();
		return getActiveElements();
	}

	public void update() {
		Logger.info("update services");
		List<T> oldElements = getCurrentElements();
		Set<T> newElements = new HashSet<>();
		File file = Play.application().getFile(getFilePath());
		try {
			Scanner scanner = new Scanner(file);
			scanner.useDelimiter("[\r\n]+");
			while (scanner.hasNext())
				newElements.add(createElement(scanner.next().trim()));
			scanner.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		for (T oldPortal : oldElements) {
			if (oldPortal.isActive() != newElements.contains(oldPortal)) {
				oldPortal.setActive(newElements.contains(oldPortal));
				Ebean.update(oldPortal);
			}
			if (newElements.contains(oldPortal))
				newElements.remove(oldPortal);
		}
		
		Ebean.save(newElements);
		lastUpdate = System.currentTimeMillis();
	}

	public abstract String getFilePath();

	public abstract T createElement(String title);

	public abstract List<T> getCurrentElements();
	
	public abstract List<T> getActiveElements();
	
}
