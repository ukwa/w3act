package controllers;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import models.User;
import models.WatchedTarget;

public class Users {
  
	public static Map<String, String> getUsers() {
		List<User> userList = User.findAllSorted();
		Map<String, String> users = new LinkedHashMap<>();
		users.put("","All");
		for (User user : userList)
			users.put(""+user.id, user.name);
		return users;
	}
	
	public static Map<String, String> getWatchedTargets(Long userId) {
		List<WatchedTarget> watchedTargets = WatchedTarget.find.where().eq("target.authorUser.id", userId).findList();
		Map<String, String> targets = new LinkedHashMap<>();
		targets.put("","All");
		for (WatchedTarget watchedTarget : watchedTargets)
			targets.put(""+watchedTarget.id, watchedTarget.target.title);
		return targets;
	}
	
	public static List<Long> getWatchedTargetIds(Long userId) {
		List<WatchedTarget> watchedTargets = WatchedTarget.find.where().eq("target.authorUser.id", userId).findList();
		List<Long> targetIds = new ArrayList<>();
		for (WatchedTarget watchedTarget : watchedTargets)
			targetIds.add(watchedTarget.id);
		return targetIds;
	}
}

