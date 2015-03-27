package models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import play.data.format.Formats.DateTime;
import play.libs.F.Option;
import play.mvc.QueryStringBindable;

public class DocumentFilter implements QueryStringBindable<DocumentFilter> {
	public Document.Status status;
	public Long user;
	public Long watchedtarget;
	public String service;
	public List<String> fastSubjects = new ArrayList<>();
	SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
	@DateTime(pattern="dd-MM-yyyy")
	public Date startdate;
	@DateTime(pattern="dd-MM-yyyy")
	public Date enddate;
	
	public DocumentFilter() {
		status = Document.Status.NEW;
	}
	
	public DocumentFilter(Long userId) {
		status = Document.Status.NEW;
		this.user = userId;
	}
	
	public DocumentFilter(DocumentFilter documentFilter) {
		status = documentFilter.status;
		user = documentFilter.user;
		watchedtarget = documentFilter.watchedtarget;
		service = documentFilter.service;
		fastSubjects = documentFilter.fastSubjects;
		startdate = documentFilter.startdate;
		enddate = documentFilter.enddate;
	}

	public DocumentFilter withWatchedTarget(Long watchedTargetId) {
		DocumentFilter documentFilter = new DocumentFilter(this);
		documentFilter.watchedtarget = watchedTargetId;
		documentFilter.user = WatchedTarget.find.byId(watchedTargetId).target.authorUser.id;
		return documentFilter;
	}
	
	public DocumentFilter withStatus(Document.Status status) {
		DocumentFilter documentFilter = new DocumentFilter(this);
		documentFilter.status = status;
		return documentFilter;
	}
	
	@Override
	public Option<DocumentFilter> bind(String key, Map<String, String[]> data) {
		if (data.containsKey("status"))
			status = Document.Status.valueOf(data.get("status")[0]);
		if (data.containsKey("user") && !data.get("user")[0].isEmpty()) {
			user = Long.parseLong(data.get("user")[0]);
		}
		if (data.containsKey("watchedtarget") && !data.get("watchedtarget")[0].isEmpty()) {
			watchedtarget = Long.parseLong(data.get("watchedtarget")[0]);
		}
		if (data.containsKey("service") && !data.get("service")[0].equals("All"))
			service = data.get("service")[0];
		for (FastSubject fastSubject : FastSubject.find.all())
			if (data.containsKey(fastSubject.fastId))
				fastSubjects.add(fastSubject.fastId);
		if (data.containsKey("startdate"))
			try {
				startdate = dateFormat.parse(data.get("startdate")[0]);
			} catch (ParseException e) {}
		if (data.containsKey("enddate"))
			try {
				enddate = dateFormat.parse(data.get("enddate")[0]);
			} catch (ParseException e) {}
		return Option.Some(this);
	}
	
	@Override
	public String unbind(String key) {
		String result = "status=" + status.toString();
		if (user != null) result += "&user=" + user;
		if (watchedtarget != null) result += "&watchedtarget=" + watchedtarget;
		if (service != null) result += "&service=" + service;
		for (String fastSubject : fastSubjects)
			result += "&" + fastSubject + "=true";
		if (startdate != null) result += "&startdate=" + dateFormat.format(startdate);
		if (enddate != null) result += "&enddate=" + dateFormat.format(enddate);
		return result;
	}
	
	@Override
	public String javascriptUnbind() {
		return null;
	}
}
