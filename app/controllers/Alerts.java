package controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.avaje.ebean.Ebean;

import models.Alert;
import models.User;
import play.data.DynamicForm;
import play.mvc.Result;
import play.mvc.Security;
import views.html.alerts.list;

@Security.Authenticated(SecuredController.class)
public class Alerts extends AbstractController {
	
	public static Result list(int pageNo, String sortBy, String order) {
		User user = User.findByEmail(request().username());
		return ok(
			list.render(
					user,
					Alert.page(user.id, pageNo, 20, sortBy, order),
					sortBy,
					order)
			);
	}
	
	public static Result action() {
		Map<String, String> data = (new DynamicForm()).bindFromRequest().data();
		
		List<Alert> alerts = new ArrayList<>();
		
		for (String key : data.keySet()) {
			if (key.startsWith("action_")) {
				long alertId = Integer.parseInt(key.split("_")[1]);
				alerts.add(Alert.find.byId(alertId));
			}
		}
		
		if (data.containsKey("delete")) {
			Ebean.delete(alerts);
		} else {
			for (Alert alert : alerts) {
				alert.read = true;
				Ebean.save(alert);
			}
		}
		return redirect(routes.Alerts.list(0, "createdAt", "desc"));
	}

}
