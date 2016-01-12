package controllers;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.net.URLDecoder;
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
import uk.bl.crawling.Crawler;
import views.html.documents.compare;
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
		Logger.debug("Documents.render()");
		
		Document document = getDocumentFromDB(id);
		if (document == null) return ok("Document not found: The requested document is no longer available.");
		if (document.status == Document.Status.SUBMITTED) editable = false;
		
		User user = User.findByEmail(request().username());
		if (!document.isEditableFor(user))
			editable = false;
		
		Form<Document> documentForm = Form.form(Document.class).fill(document);
		setRelatedEntitiesOfView(documentForm, document);
		
		return ok(edit.render("Document" + id, document, documentForm,
				user, editable));
	}
	
	public static Result continueEdit() {
		Logger.debug("Documents.continueEdit()");
		
		if (flash("journalTitle") != null)
			session("journal.journalTitleId", flash("journalTitle"));
		
		Form<Document> documentForm = Form.form(Document.class).bind(session());
		documentForm.discardErrors();
		
		return ok(edit.render("Document", Document.find.byId(new Long(session("id"))), documentForm,
				User.findByEmail(request().username()), true));
	}

	public static Result save(Long id) {
		Logger.debug("Documents.save()");
		
		String journalTitle = getFormParam("journalTitle");
		
		Form<Document> documentForm = Form.form(Document.class).bindFromRequest();
		
		if (journalTitle != null) {
			session().putAll(documentForm.data());
			return redirect(routes.JournalTitles.addJournalTitle(
					new Long(documentForm.apply("watchedTarget.id").value()), true));
		}
		
		Logger.debug("Errors: " + documentForm.hasErrors());
		for (String key : documentForm.errors().keySet()) {
			Logger.debug("Key: " + key);
			for (ValidationError error : documentForm.errors().get(key)) {
				for (String message : error.messages()) {
					Logger.debug("Message: " + message);
				}
			}
		}
		if (documentForm.hasErrors()) {
			Logger.debug("Show errors in html");
			FlashMessage.validationWarning.send();
			return status(303, edit.render("Document",
					Document.find.byId(new Long(documentForm.field("id").value())), documentForm,
					User.findByEmail(request().username()), true));
		}
		Logger.debug("Glob Errors: " + documentForm.hasGlobalErrors());
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
				
				//code to download sip.xml file to server
				String url = "https://www.webarchive.org.uk/act-ddhapt/documents/"+id+"/sip";
				Promise<File> filePromise = WS.url(url).get().map(response -> {
					 InputStream inputStream = null;
					 OutputStream outputStream = null;
					 String fileName = "sip.xml";
					 String saveDir = Play.application().configuration().getString("ddhapt.input.dir");
					    try {
					        inputStream = response.getBodyAsStream();
					    
					        // save inputStream to a file
					        final File file = new File(saveDir + File.separator + fileName);
					        outputStream = new FileOutputStream(file);
					    
					        int read = 0;
					        byte[] buffer = new byte[1024];

					        while ((read = inputStream.read(buffer)) != -1) {
					            outputStream.write(buffer, 0, read);
					        }
					  
					        return file;  
					    } catch (IOException e) {
					        throw e;
					    } finally {
					        if (inputStream != null) {inputStream.close();}
					        if (outputStream != null) {outputStream.close();}
					    } 
					 
				});
				
				return redirect(routes.Documents.view(id));
			}
		}
		document.ark = assignableArks.get(0).ark;
		Ebean.delete(assignableArks.get(0));
		document.setStatus(Document.Status.SUBMITTED);
		Ebean.save(document);
		deleteHtmlFile(document.htmlFilename());
		FlashMessage submitSuccess = new FlashMessage(FlashMessage.Type.SUCCESS,
				"The document has been submitted.");
		submitSuccess.send();
		return redirect(routes.Documents.view(id));
	}
	
	public static Result compare(Long id1, Long id2) {
		Document d1 = Document.find.byId(id1);
		Document d2 = Document.find.byId(id2);
		return ok(compare.render(d1, d2, User.findByEmail(request().username())));
	}
	
	private static void requestNewArks() {
		String piiUrl = Play.application().configuration().getString("pii_url");
		WSRequestHolder holder = WS.url(piiUrl);

		Promise<List<AssignableArk>> arksPromise = holder.post("").map(
				new Function<WSResponse, List<AssignableArk>>() {
					@Override
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
		Logger.info("documents are imported");
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
			document.landingPageUrl = objNode.get("landing_page_url").textValue().replace("'", "%27");
			document.documentUrl = objNode.get("document_url").textValue().replace("'", "%27");
			document.filename = objNode.get("filename").textValue();
			if (document.filename.contains("."))
				document.title = document.filename.substring(0, document.filename.lastIndexOf("."));
			else
				document.title = document.filename;
			document.size = objNode.get("size").longValue();
			document.setStatus(Document.Status.NEW);
			document.fastSubjects = WatchedTarget.find.byId(watchedTargetId).fastSubjects;
			Logger.debug("add document " + document.filename);
			documents.add(document);
		}
		Promise.promise(new ExtractFunction(documents));
		return ok("Documents added");
	}
	
	private static class ExtractFunction implements Function0<Boolean> {
		
		public List<Document> documents;
		public ExtractFunction(List<Document> documents) {
			this.documents = documents;
		}
		
		@Override
		public Boolean apply() {
			for (Document document : filterNew(documents)) {
				Crawler crawler = new Crawler(true);
				crawler.extractMetadata(document);
				Ebean.save(document);
			}
			return true;
		}
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
	
	public static void addDuplicateAlert(String ctphFile) {
		File file = Play.application().getFile("conf/converter/" + ctphFile + ".out");
		try {
			Scanner scanner = new Scanner(file);
			scanner.useDelimiter("[\r\n]+");
			while (scanner.hasNext()) {
				String line = scanner.next();
				String[] parts = line.split("[ :]");
				if (parts.length == 6) {
					int similarity = Integer.parseInt(parts[5].replace("(", "").replace(")", ""));
					if (similarity >= 98 && similarity < 100) {
						long docId1 = Long.parseLong(parts[1].split("\\.")[0]);
						long docId2 = Long.parseLong(parts[4].split("\\.")[0]);
						Document doc1 = Document.find.byId(docId1);
						Document doc2 = Document.find.byId(docId2);
						Alert alert = new Alert();
						alert.user = doc1.watchedTarget.target.authorUser;
						alert.text = "possible duplicate found: " + Alert.link(doc1) + " matches " +
								Alert.link(doc2) + " with " + similarity + "% " +
								"(" + Alert.compareLink(doc1, doc2) + ")";
						Ebean.save(alert);
					}
				}
			}
			scanner.close();
			file.delete();
		} catch (Exception e) {
			Logger.warn("can't read ctph matches:", e);
		}
	}
	
	public static Document getDocumentFromDB(long id) {
		Document document = Ebean.find(Document.class, id);
		if (document == null) return null;
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
			@Override
			public Boolean apply() {
				List<Document> documents = Document.find.where().eq("status", Document.Status.DELETED.ordinal()).findList();
				for (Document document : documents)
					deleteHtmlFile(document.htmlFilename());
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
    	Logger.debug("Documents.list()");
    	
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
		} catch (Exception e) {
			return ok("This file was not found on the system.");
		}
	}
    
	public static void deleteHtmlFiles(WatchedTarget watchedTarget) {
		for (Document document : watchedTarget.documents)
			deleteHtmlFile(document.htmlFilename());
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
