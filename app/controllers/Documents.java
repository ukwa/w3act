package controllers;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Query;
import com.avaje.ebean.SqlUpdate;
import com.fasterxml.jackson.databind.JsonNode;

import models.Alert;
import models.AssignableArk;
import models.BlCollectionSubset;
import models.Book;
import models.Document;
import models.DocumentFilter;
import models.FlashMessage;
import models.Journal;
import models.JournalTitle;
import models.Portal;
import models.User;
import models.WatchedTarget;
import play.Logger;
import play.data.Form;
import play.data.validation.ValidationError;
import play.libs.F.Function;
import play.libs.F.Function0;
import play.libs.F.Promise;
import play.libs.Json;
import play.libs.ws.WS;
import play.libs.ws.WSRequestHolder;
import play.libs.ws.WSResponse;
import play.libs.XPath;
import play.mvc.BodyParser;
import play.mvc.Result;
import play.mvc.Security;
import play.Play;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import uk.bl.Const;
import uk.bl.configurable.BlCollectionSubsetList;
import views.html.documents.edit;
import views.html.documents.list;
import views.xml.documents.sip;

@Security.Authenticated(SecuredController.class)
public class Documents extends AbstractController {
	
	public static BlCollectionSubsetList blCollectionSubsetList = new BlCollectionSubsetList();
	
	public static Result view(Long id) {
		return render(id, false);
	}
	
	public static Result edit(Long id) {
		return render(id, true);
	}
	
	private static Result render(Long id, boolean editable) {
		Logger.info("Documents.render()");
		
		Document document = getDocumentFromDB(id);
		if (document.status == Document.Status.SUBMITTED) editable = false;
		Form<Document> documentForm = Form.form(Document.class).fill(document);
		setRelatedEntitiesOfView(documentForm, document);
		
		return ok(edit.render("Document" + id, documentForm,
				User.findByEmail(request().username()), editable));
	}
	
	public static Result continueEdit() {
		Logger.info("Documents.continueEdit()");
		
		if (flash("journalTitle") != null)
			session("journal.journalTitleId", flash("journalTitle"));
		
		Form<Document> documentForm = Form.form(Document.class).bind(session());
		documentForm.discardErrors();
		
		return ok(edit.render("Document", documentForm,
				User.findByEmail(request().username()), true));
	}

	public static Result save(Long id) {
		Logger.info("Documents.save()");
		
		String journalTitle = getFormParam("journalTitle");
		
		Form<Document> documentForm = Form.form(Document.class).bindFromRequest();
		
		if (journalTitle != null) {
			session().putAll(documentForm.data());
			return redirect(routes.JournalTitles.addJournalTitle(
					new Long(documentForm.apply("watchedTarget.id").value()), true));
		}
		
		Logger.info("Errors: " + documentForm.hasErrors());
		for (String key : documentForm.errors().keySet()) {
			Logger.info("Key: " + key);
			for (ValidationError error : documentForm.errors().get(key)) {
				for (String message : error.messages()) {
					Logger.info("Message: " + message);
				}
			}
		}
		if (documentForm.hasErrors()) {
			Logger.info("Show errors in html");
			FlashMessage.validationWarning.send();
			return status(303, edit.render("Document", documentForm,
					User.findByEmail(request().username()), true));
		}
		Logger.info("Glob Errors: " + documentForm.hasGlobalErrors());
		Document document = documentForm.get();
		document.clearImproperFields();
		setRelatedEntitiesOfModel(document, documentForm);
		Ebean.update(document);
		if (document.publicationDate == null) {
			Ebean.createUpdate(Document.class, "update document SET publication_date=null where id=:id")
					.setParameter("id", document.id).execute();
		}
		
		if (!document.isBookOrBookChapter() && document.book.id != null) {
			Book book = Ebean.find(Book.class, document.book.id);
			Ebean.delete(book);
		}
		if (!document.isJournalArticleOrIssue() && document.journal.id != null) {
			Journal journal = Ebean.find(Journal.class, document.journal.id);
			Ebean.delete(journal);
		}
		
		if (document.isBookOrBookChapter()) {
			document.book.document = document;
			if (document.book.id == null) {
				Ebean.save(document.book);
			} else {
				Ebean.update(document.book);
			}
		} else if (document.isJournalArticleOrIssue()) {
			document.journal.document = document;
			document.journal.journalTitle = Ebean.find(JournalTitle.class, document.journal.journalTitleId);
			if (document.journal.id == null) {
				Ebean.save(document.journal);
			} else {
				Ebean.update(document.journal);
			}
		}
		
		FlashMessage.updateSuccess.send();
		
		return redirect(routes.Documents.view(document.id));
	}
	
	public static Result submit(Long id) {
		Document document = Document.find.byId(id);
		List<AssignableArk> assignableArks = AssignableArk.find.all();
		if (assignableArks.isEmpty()) {
			requestNewArks();
			assignableArks = AssignableArk.find.all();
			if (assignableArks.isEmpty()) {
				FlashMessage arkError = new FlashMessage(FlashMessage.Type.ERROR,
						"Submission failed! It was not possible to get an ARK identifier.");
				arkError.send();
				return redirect(routes.Documents.view(id));
			}
		}
		document.ark = assignableArks.get(0).ark;
		Ebean.delete(assignableArks.get(0));
		document.setStatus(Document.Status.SUBMITTED);
		Ebean.save(document);
		deleteHtmlFile(document.getHtmlFilename());
		FlashMessage submitSuccess = new FlashMessage(FlashMessage.Type.SUCCESS,
				"The document has been submitted.");
		submitSuccess.send();
		return redirect(routes.Documents.view(id));
	}
	
	private static void requestNewArks() {
		String piiUrl = Play.application().configuration().getString("pii_url");
		WSRequestHolder holder = WS.url(piiUrl);

		Promise<List<AssignableArk>> arksPromise = holder.get().map(
				new Function<WSResponse, List<AssignableArk>>() {
					public List<AssignableArk> apply(WSResponse response) {
						Logger.debug("PII XML-Response: " + response.getBody());
						List<AssignableArk> arks = new ArrayList<>();
						try {
							org.w3c.dom.Document xml = response.asXml();
							if (xml != null) {
								NodeList nodes = XPath.selectNodes("/pii/results/arkList/ark", xml);
								for (int i=0; i < nodes.getLength(); i++) {
									Node node = nodes.item(i);
									arks.add(new AssignableArk(node.getTextContent()));
								}
							}
						} catch (Exception e) {
							Logger.error("Can't get ARKs from the PII server: " + e.getMessage());
						}
						return arks;
					}
				}
		);
		
		List<AssignableArk> arks = arksPromise.get(5000);
		Ebean.save(arks);
	}

	public static Result ignore(Long id, DocumentFilter documentFilter, int pageNo,
			String sortBy, String order, String filter, boolean filters) {
		Document document = Document.find.byId(id);
		if (documentFilter.status == Document.Status.NEW)
			document.setStatus(Document.Status.IGNORED);
		else
			document.setStatus(Document.Status.NEW);
		Ebean.save(document);
		if (filters)
			return redirect(routes.Documents.list(documentFilter, pageNo, sortBy, order, filter));
		else
			return redirect(routes.Documents.overview(pageNo, sortBy, "asc"));
	}
	
	public static Result ignoreAll(DocumentFilter documentFilter, String filter, boolean filters) {
		Query<Document> query = Document.query(documentFilter, "title", "asc", filter);
		Document.Status status;
		if (documentFilter.status == Document.Status.NEW)
			status = Document.Status.IGNORED;
		else
			status = Document.Status.NEW;
		for (Document document : query.findList()) {
			document.setStatus(status);
			Ebean.save(document);
		}
		if (filters)
			return redirect(routes.Documents.list(documentFilter, 0, "title", "asc", filter));
		else
			return redirect(routes.Documents.overview(0, "title", "asc"));
	}
	
	@BodyParser.Of(value = BodyParser.Json.class, maxLength = 1024 * 1024)
	public static Result importJson() {
		JsonNode json = request().body().asJson();
		List<Document> documents = new ArrayList<>();
		for (JsonNode objNode : json) {
			Document document = new Document();
			Long watchedTargetId = objNode.get("id_watched_target").longValue();
			document.watchedTarget = WatchedTarget.find.byId(watchedTargetId);
			document.waybackTimestamp = objNode.get("wayback_timestamp").textValue();
			if (document.watchedTarget.waybackTimestamp == null ||
					document.waybackTimestamp.compareTo(document.watchedTarget.waybackTimestamp) > 0) {
				document.watchedTarget.waybackTimestamp = document.waybackTimestamp;
				Ebean.save(document.watchedTarget);
			}
			try {
				document.landingPageUrl = URLEncoder.encode(objNode.get("landing_page_url").textValue(), "UTF-8");
				document.documentUrl = URLEncoder.encode(objNode.get("document_url").textValue(), "UTF-8");
			} catch (UnsupportedEncodingException e) {/*should not happen*/}
			document.filename = objNode.get("filename").textValue();
			if (document.filename.contains("."))
				document.title = document.filename.substring(0, document.filename.lastIndexOf("."));
			else
				document.title = document.filename;
			document.setStatus(Document.Status.NEW);
			document.fastSubjects = WatchedTarget.find.byId(watchedTargetId).fastSubjects;
			Logger.debug("add document " + document.filename);
			documents.add(document);
		}
		Ebean.save(filterNew(documents));
		return ok("Documents added");
	}
	
	public static List<Document> filterNew(List<Document> documentList) {
		List<Document> newDocumentList = new ArrayList<>();
		for (Document document : documentList) {
			String urlWithoutSchema = document.documentUrl.replaceFirst("^.*://", "");
			if (Document.find.where().eq("regexp_replace(document_url,'^.*://','')", urlWithoutSchema).findRowCount() == 0)
				newDocumentList.add(document);
		}
		return newDocumentList;
	}
	
	public static Result export(DocumentFilter documentFilter, String sortBy,
			String order, String filter) {
		Query<Document> query = Document.query(documentFilter, sortBy, order, filter);
		
		StringBuilder builder = new StringBuilder();
		
		builder.append(
				"id" + Const.CSV_SEPARATOR +
				"id_watched_target" + Const.CSV_SEPARATOR +
				"title" + Const.CSV_SEPARATOR +
				"landing_page_url" + Const.CSV_SEPARATOR +
				"document_url" + Const.CSV_SEPARATOR +
				"wayback_timestamp" + Const.CSV_LINE_END
		);
		
		for (Document document : query.findList()) {
			builder.append(
					document.id + Const.CSV_SEPARATOR +
					document.watchedTarget.id + Const.CSV_SEPARATOR +
					document.title + Const.CSV_SEPARATOR +
					document.landingPageUrl + Const.CSV_SEPARATOR +
					document.documentUrl + Const.CSV_SEPARATOR +
					document.waybackTimestamp + Const.CSV_LINE_END
			);
		}
		
		response().setContentType("text/csv; charset=utf-8");
		response().setHeader("Content-Disposition","attachment; filename=\"document-export.csv");
		return ok(builder.toString());
	}
	
	public static Result sip(Long id) {
		Document document = Document.find.byId(id);
		return ok(sip.render(document));
	}
	
	public static void addHashes(Document document) {
		File file = Play.application().getFile("conf/converter/" + document.id + ".sha256");
		try {
			Scanner scanner = new Scanner(file);
			document.sha256Hash = scanner.next();
			scanner.close();
			file.delete();
		} catch (Exception e) {
			Logger.warn("can't read sha256 hash: " + e.getMessage());
		}
		file = Play.application().getFile("conf/converter/" + document.id + ".ctp");
		try {
			Scanner scanner = new Scanner(file);
			document.ctpHash = scanner.next();
			scanner.close();
			file.delete();
		} catch (Exception e) {
			Logger.warn("can't read ctp hash: " + e.getMessage());
		}
		Ebean.save(document);
	}
	
	public static void addDuplicateAlert() {
		File file = Play.application().getFile("conf/converter/matches.out");
		try {
			Scanner scanner = new Scanner(file);
			scanner.useDelimiter("[\r\n]+");
			while (scanner.hasNext()) {
				String line = scanner.next();
				String[] parts = line.split("[ :]");
				if (parts.length == 6) {
					long docId1 = Long.parseLong(parts[1].split("\\.")[0]);
					long docId2 = Long.parseLong(parts[4].split("\\.")[0]);
					int similarity = Integer.parseInt(parts[5].replace("(", "").replace(")", ""));
					Document doc1 = Document.find.byId(docId1);
					Document doc2 = Document.find.byId(docId2);
					Alert alert = new Alert();
					alert.user = doc1.watchedTarget.target.authorUser;
					alert.text = "possible duplicate found: " + Alert.link(doc1) + " matches " +
							Alert.link(doc2) + " with " + similarity + "%";
					Ebean.save(alert);
				}
			}
			scanner.close();
			file.delete();
		} catch (Exception e) {
			Logger.warn("can't read ctph matches:");
			e.printStackTrace();
		}
	}
	
	public static Document getDocumentFromDB(long id) {
		Document document = Ebean.find(Document.class, id);
		if (document.type == null) document.type = "";
		if (document.journal != null)
			document.journal.journalTitleId = document.journal.journalTitle.id;
		return document;
	}
	
	public static Map<String, String> getJournalTitles(Form<Document> form) {
		WatchedTarget watchedTarget = WatchedTarget.find.byId(new Long(form.apply("watchedTarget.id").value()));
		Map<String, String> titles = new LinkedHashMap<>();
		titles.put("","");
		for (JournalTitle journalTitle : watchedTarget.journalTitles)
			titles.put(""+journalTitle.id, journalTitle.title);
		return titles;
	}
	
	private static void setRelatedEntitiesOfModel(Document document, Form<Document> documentForm) {
		document.fastSubjects = FastSubjects.getFastSubjects(documentForm);
		document.portals = Portals.getPortals(documentForm);
		if (document.isBookOrBookChapter())
			for (BlCollectionSubset blCollectionSubset : blCollectionSubsetList.getList())
				if (documentForm.apply("blCollectionSubset_" + blCollectionSubset.id).value() != null)
					document.book.blCollectionSubsets.add(blCollectionSubset);
	}
	
	private static void setRelatedEntitiesOfView(Form<Document> documentForm, Document document) {
		documentForm.data().putAll(FastSubjects.getFormData(document.fastSubjects));
		documentForm.data().putAll(Portals.getFormData(document.portals));
		if (document.isBookOrBookChapter())
			for (BlCollectionSubset portal : document.book.blCollectionSubsets)
				documentForm.data().put("blCollectionSubset_" + portal.id, "true");
	}
	
	public static List<String> getPortalsSelection() {
		List<Portal> portals = Portals.portalList.getList();
		List<String> portalTitles = new ArrayList<>();
		portalTitles.add("All");
		for (Portal portal : portals)
			portalTitles.add(portal.title);
		return portalTitles;
	}
	
    /**
     * Display the paginated list of Documents.
     *
     * @param page Current page number (starts from 0)
     * @param sortBy Column to be sorted
     * @param order Sort order (either asc or desc)
     * @param filter Filter applied on Documents
     */
	public static Result list(DocumentFilter documentFilter,
			int pageNo, String sortBy, String order, String filter) {
		if (documentFilter.status == Document.Status.IGNORED)
			changeStatusOfIgnoredDocuments();
		return renderList(documentFilter, pageNo, sortBy, order, filter, true);
	}

	private static void changeStatusOfIgnoredDocuments() {
		SqlUpdate su = Ebean.createSqlUpdate("update document" +
				" set status=" + Document.Status.DELETED.ordinal() +
				" where status=" + Document.Status.IGNORED.ordinal() +
				" and age(current_status_set) >= interval '1 month'");
		Ebean.execute(su);
		
		Promise.promise(new Function0<Boolean>() {
			public Boolean apply() {
				List<Document> documents = Document.find.where().eq("status", Document.Status.DELETED.ordinal()).findList();
				for (Document document : documents)
					deleteHtmlFile(document.getHtmlFilename());
				return true;
			}
		});
	}
	
    public static Result overview(int pageNo, String sortBy, String order) {
    	DocumentFilter documentFilter = new DocumentFilter(User.findByEmail(request().username()).id);
    	return renderList(documentFilter, pageNo, sortBy, order, "", false);
    }
    
    public static Result renderList(DocumentFilter documentFilter,
    		int pageNo, String sortBy, String order, String filter, boolean filters) {
    	Logger.info("Documents.list()");
    	
    	Form<DocumentFilter> filterForm = Form.form(DocumentFilter.class).fill(documentFilter);
    	for (String fastSubject : documentFilter.fastSubjects)
    		filterForm.data().put(fastSubject, "true");
    	
        return ok(
        	list.render(
        			User.findByEmail(request().username()),
        			filterForm,
        			filter,
        			Document.page(documentFilter, pageNo, 20, sortBy, order, filter),
        			sortBy,
        			order,
        			filters)
        	);
    }
    
    @BodyParser.Of(BodyParser.Json.class)
    public static Result filterByJson(String title) {
        JsonNode jsonData = null;
        if (title != null) {
	        List<Document> documents = Document.find.where().icontains("title", title).findList();
	        jsonData = Json.toJson(documents);
        }
        return ok(jsonData);
    }
    
    public static Result html(String encodedFilename) {
		try {
			String filename = URLDecoder.decode(encodedFilename, "UTF-8");
			File file = Play.application().getFile("../html/" + filename);
			return ok(file, filename);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return ok("There is a problem with the encoding.");
		} catch (Exception e) {
			return ok("This file was not found on the system.");
		}
	}
    
	public static void deleteHtmlFiles(WatchedTarget watchedTarget) {
		for (Document document : watchedTarget.documents)
			deleteHtmlFile(document.getHtmlFilename());
	}
    
	public static void deleteHtmlFile(String filename) {
		File file = Play.application().getFile("../html/" + filename);
		file.delete();
	}
	
	public static List<Long> stringToLongList(String subject) {
		List<Long> subjectIds = new ArrayList<Long>();
    	if (subject != null && !subject.isEmpty()) {
            String[] subjects = subject.split(", ");
            for (String sId : subjects) {
            	Long subjectId = Long.valueOf(sId);
            	subjectIds.add(subjectId);
            }
        }
    	return subjectIds;
	}
	
	public static String longListToString(List<Long> subjectIds) {
    	return StringUtils.join(subjectIds, ", ");
	}

}
