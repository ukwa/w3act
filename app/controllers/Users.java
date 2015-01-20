package controllers;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import models.Document;
import models.User;
import models.WatchedTarget;
import play.data.Form;

public class Users {
  
	public static Map<String, String> getUsers() {
		List<User> userList = User.findAllSorted();
		Map<String, String> users = new LinkedHashMap<>();
		users.put("","All");
		for (User user : userList)
			users.put(""+user.uid, user.name);
		return users;
	}
	
	public static Map<String, String> getWatchedTargets(Long userId) {
		List<WatchedTarget> watchedTargets = WatchedTarget.find.where().eq("id_creator", userId).findList();
		Map<String, String> targets = new LinkedHashMap<>();
		targets.put("","All");
		for (WatchedTarget watchedTarget : watchedTargets)
			targets.put(""+watchedTarget.id, watchedTarget.target.title);
		return targets;
	}
	
	public static List<Long> getWatchedTargetIds(Long userId) {
		List<WatchedTarget> watchedTargets = WatchedTarget.find.where().eq("id_creator", userId).findList();
		List<Long> targetIds = new ArrayList<>();
		for (WatchedTarget watchedTarget : watchedTargets)
			targetIds.add(watchedTarget.id);
		return targetIds;
	}
}

