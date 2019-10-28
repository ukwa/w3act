package controllers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.tika.io.IOUtils;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

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
import models.FastSubject;
import models.FlashMessage;
import models.Journal;
import models.JournalTitle;
import models.Portal;
import models.Target;
import models.User;
import models.WatchedTarget;
import play.Logger;
import play.Play;
import play.data.Form;
import play.data.validation.ValidationError;
import play.libs.F.Function;
import play.libs.F.Function0;
import play.libs.F.Promise;
import play.libs.Json;
import play.libs.XPath;
import play.libs.ws.WS;
import play.libs.ws.WSRequestHolder;
import play.libs.ws.WSResponse;
import play.mvc.BodyParser;
import play.mvc.Result;
import play.mvc.Security;
import uk.bl.Const;
import uk.bl.configurable.BlCollectionSubsetList;
import uk.bl.documents.DocumentAnalyser;

import views.html.documents.compare;
import views.html.documents.edit;
import views.html.documents.list;

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
		
		// Record that there's been an update:
		if(document.status == Document.Status.NEW) {
			document.setStatus(Document.Status.SAVED);
		}
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
	
	private static void getARKs(Document d) {
		List<AssignableArk> arks = requestNewArks(4);
		if( arks == null || arks.size() < 4) {
			return;
		}
		Logger.info("Minted four arks. ark[0] = "+arks.get(0).ark);
		// Add these ARKs to the document:
		d.ark = arks.get(0).ark;
		d.mets_d_ark = arks.get(1).ark;
		d.d_ark = arks.get(2).ark;
		d.md_ark = arks.get(3).ark;
	}
	
	public static Result submit(final Long id) {
		// Find the document:
		Document document = Document.find.byId(id);
		
		// Mint ARKs for the document:
		if( document.hasARKs() == false ){
			Documents.getARKs(document);
		} else {
			Logger.warn("Re-using existing ARKs, e.g. doc.ark = "+ document.ark);
		}
		
		// Check it worked:
		if( document.hasARKs() == false ) {
			FlashMessage error = new FlashMessage(FlashMessage.Type.ERROR,
					"There was a problem minting ARK identifiers for this SIP!");
			error.send();
			return redirect(routes.Documents.view(id));
		} else {
			Ebean.save(document);
		}

        final String fileName = "_sip_" + id;

        // Choose the save dir:
		final File saveDir;

		if( document.isJournalArticleOrIssue() ) {
			saveDir = new File(Play.application().configuration().getString("dls.documents.ejournal.sip.dir"));
		}
		else {
            // eBooks have a holding directory for each submission. Create a temporary one until the copy is known to have succeeded.
			saveDir = new File(Play.application().configuration().getString("dls.documents.ebook.sip.dir"), fileName);
            saveDir.mkdir();
        }

        if(saveDir.exists()) {
            // Download it to a local file (hard-coded but acceptable as we are running under Docker).
            String url = "http://localhost:9000" + routes.DocumentSIPController.sip(id).url();
            Logger.info("Downloading " + url);
            final Promise<File> filePromise = WS.url(url).get().map(
                    new Function<WSResponse, File>() {
                        @Override
                        public File apply(WSResponse response) throws Throwable {
                            final File file = new File(saveDir, fileName + ".xml");
                            InputStream responseStream = null;
                            OutputStream fileStream = null;

                            try {
                                IOUtils.copy(responseStream = response.getBodyAsStream(), fileStream = new FileOutputStream(file));
                            }
                            catch(IOException e) {
                                Logger.error("Exception while downloading file: " + e.getMessage(), e);
                                return null;
                            }
                            finally {
                                try {
                                    if(responseStream != null) {
                                        responseStream.close();
                                    }
                                }
                                catch(Exception e){
                                    Logger.warn("Failed to close stream on download.", e);
                                }

                                try {
                                    if(fileStream != null) {
                                        fileStream.close();
                                    }
                                }
                                catch(Exception e) {
                                    Logger.warn("Failed to close stream on file.", e);
                                }
                            }

                            return file;
                        }
                    });

            // Wait for the file to download, up to thirty seconds:
            File file = filePromise.get(30, TimeUnit.SECONDS);

            // Check it's good:
            if(file != null && file.exists() && file.isFile() && file.length() != 0) {
                // Make a copy (in case something goes wrong:
                String copyDir = Play.application().configuration().getString("dls.documents.sip.copy.dir");

                if(!StringUtils.isEmpty(copyDir)) {
                    Calendar cal = Calendar.getInstance();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    String dateStr = dateFormat.format(cal.getTime());
                    String copyFilename = dateStr + "_" + document.type.toLowerCase().replace(' ', '_') + "_sip_" + id + ".xml";
                    File copyFile = new File(copyDir, copyFilename);
                    try {
                        FileUtils.copyFile(file, copyFile);
                        if(copyFile.length() != file.length()) {
                            throw new IOException("Copy failed - lengths to not match.");
                        }
                    }
                    catch(IOException e) {
                        Logger.error("Exception while copying SIP xml: " + e.getMessage(), e);
                        FlashMessage downloadError = new FlashMessage(FlashMessage.Type.ERROR,
                                "SIP XML could not be copied!");
                        downloadError.send();
                        return redirect(routes.Documents.view(id));
                    }
                }

                // And rename, this initiation submission:
                String newFileName = "sip_" + id;

                File targetFile = new File(saveDir, newFileName + ".xml");
                Logger.info("Renaming "+file.getAbsolutePath()+" to "+targetFile);
                Boolean renameSuccess = file.renameTo(targetFile);

                // Rename the holding directory, if an eBook submission
                if(!document.isJournalArticleOrIssue()) {
                	File targetDir = new File(Play.application().configuration().getString("dls.documents.ebook.sip.dir"), newFileName);
                	Logger.info("Attempt to rename "+saveDir.getAbsolutePath()+" to "+ targetDir);
                    renameSuccess &= saveDir.renameTo(targetDir);
                } else {
                	Logger.info("Not attemting any further rename of " + newFileName);
                }

                if(!renameSuccess){
                    FlashMessage error = new FlashMessage(FlashMessage.Type.ERROR, "Could not rename the temporary download file.");
                    error.send();
                    return redirect(routes.Documents.view(id));
                }
            }
            else {
                if(file != null && file.exists()) {
                    file.delete();
                }
                FlashMessage downloadError = new FlashMessage(FlashMessage.Type.ERROR,
                        "SIP XML could not be downloaded for submission!");
                downloadError.send();
                return redirect(routes.Documents.view(id));
            }
        }
        else {
            FlashMessage error = new FlashMessage(FlashMessage.Type.ERROR, "XML save directory does not exist.");
            error.send();
            return redirect(routes.Documents.view(id));
        }

		// Record success:
		document.setStatus(Document.Status.SUBMITTED);
		Ebean.save(document);
		
		// And tell:
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
	
	private static List<AssignableArk> requestNewArks( int numArks ) {
		String piiUrl = Play.application().configuration().getString("pii_url");
		WSRequestHolder holder = WS.url(piiUrl).setQueryParameter("arks", Integer.toString(numArks));
		Logger.info("Requesting ARKs via POST to "+holder.getUrl());

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
									Logger.debug("node "+node.getNodeName()+" "+node.getTextContent());
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
		return arks;
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
	
	private static String truncator(String in) {
		if( in != null && in.length() > 255 ) {
			Logger.info("Truncating over-long field: "+in);
			return in.substring(0, 255);
		} else {
			return in;
		}
	}
	
	@BodyParser.Of(value = BodyParser.Json.class, maxLength = 1024 * 1024)
	public static Result importJson() {
		Logger.info("documents are imported");
		JsonNode json = request().body().asJson();
		List<Document> documents = new ArrayList<>();
		for (JsonNode objNode : json) {
			// Parse:
			try {
				Document document = parseDocumentJson(objNode);
				// And save and add if not null:
				if( document != null) {
					// Attempt to extract critical data:
					DocumentAnalyser da = new DocumentAnalyser();
					da.extractMetadata(document);
					
					// Look for other documents with the same non-empty hash:
					boolean unique = true;
					if( StringUtils.isNoneEmpty(document.sha256Hash)) {
						for (Document doc2 : document.watchedTarget.documents ) {
							if( document.sha256Hash.equals( doc2.sha256Hash)) {
								Logger.warn("Already seen this document at "+doc2.documentUrl);
								unique = false;
								break;
							}
						}
					}
					
					// Record unique documents:
					if( unique ) {
						// Allow unset titles:
						if( document.title == null){
							document.title = "";
						}
						// Avoid submitting over-long fields (current 255 char limit)
						document.title = truncator(document.title);
						document.author1Ln = truncator(document.author1Ln);
						Logger.info("Saving document metadata.");
						try {
							Ebean.save(document);
							if( document.book != null ) {
								Ebean.save(document.book);
							}
						} catch( Exception ex ) {
							Logger.error("Exception when calling Ebean.save: ", ex);
							Logger.error("Document was: "+document);
							Logger.error("Document.Book was: "+document.book);
							throw ex;
						}
						// Add to list for post-import checks:
						documents.add(document);
					} else {
						Logger.warn("Dropping document from " + document.documentUrl + " based on SHA-256 hash being identical to an existing document on this target.");
					}
				}
			} catch( Exception ex ) {
				Logger.error("Problem while importing document.", ex);
				Logger.error("JSON was: "+objNode.toString());
				return badRequest("Problem during import: "+ex);
			}
		}
		if( documents.size() > 0 ) {
			Promise.promise(new DocumentAnalyser.SimilarityFunction(documents));
			return ok("Documents added");
		} else {
			return ok("No new documents added");
		}
	}
	
	protected static Document parseDocumentJson(JsonNode objNode) throws Exception {
		Document document = new Document();
		Long targetId = objNode.get("target_id").longValue();
		Logger.info("TargetID: "+targetId);
		Target target = Target.find.byId(targetId);
		if( target == null ) {
			throw new Exception("No Target with ID "+targetId);
		}
		Logger.info("Target: "+target.title);
		document.watchedTarget = target.watchedTarget;
		Logger.info("WatchedTarget: "+document.watchedTarget);
		if( document.watchedTarget == null ) {
			throw new Exception("This is not a watched target!");
		}
		document.waybackTimestamp = objNode.get("wayback_timestamp").textValue();
		Logger.info("Comparing "+document.watchedTarget.waybackTimestamp+" to "+document.waybackTimestamp);
		if (document.watchedTarget.waybackTimestamp == null ||
				document.waybackTimestamp.compareTo(document.watchedTarget.waybackTimestamp) > 0) {
			document.watchedTarget.waybackTimestamp = document.waybackTimestamp;
			Ebean.save(document.watchedTarget);
		}
		document.landingPageUrl = objNode.get("landing_page_url").textValue().replace("'", "%27");
		document.documentUrl = objNode.get("document_url").textValue().replace("'", "%27");
		document.filename = objNode.get("filename").textValue();
		document.size = objNode.get("size").longValue();
		document.setStatus(Document.Status.NEW);
		document.fastSubjects = target.watchedTarget.fastSubjects;
		if( documentAlreadyKnown(document)) {
			Logger.warn("This Document is already known to the system.");
			return null;
		} else {
			Logger.debug("attempting to add document " + document);
		}
		//
		// Optional fields
		// 
		// Title:
		if(objNode.get("title") != null) {
			document.title = objNode.get("title").textValue();				
		}
		// Publisher:
		if( objNode.get("publisher") != null) {
			if( document.book == null) document.book = new Book(document);
			document.book.publisher = objNode.get("publisher").textValue();
		}
		// Publication Date (in yyyy-MM-dd format):
		if( objNode.get("publication_date") != null ) {
			String ymd = objNode.get("publication_date").textValue();
			try {
				document.publicationDate = new SimpleDateFormat("yyyy-MM-dd").parse(ymd);
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(document.publicationDate);
				document.publicationYear = calendar.get(Calendar.YEAR);
			} catch( ParseException e ) {
				throw new Exception("Could not parse publication date (should be yyyy-MM-dd format"+ymd);
			}
			
		}
		// Authors array processing:
		if( objNode.get("authors") != null ) {
			List<String> authors = new ArrayList<String>();
			Iterator<JsonNode> it = objNode.get("authors").elements();
			while( it.hasNext()) {
				JsonNode val = it.next();
				authors.add(val.textValue());
			}
			if (authors.size() >= 1) {
				String[] a = authors.get(0).trim().split("\\s+", 2);
				document.author1Fn = a[0];
				document.author1Ln = a[1];
			}
			if (authors.size() >= 2) {
				String[] a = authors.get(1).trim().split("\\s+", 2);
				document.author2Fn = a[0];
				document.author2Ln = a[1];
			}
			if (authors.size() >= 3) {
				String[] a = authors.get(2).trim().split("\\s+", 2);
				document.author3Fn = a[0];
				document.author3Ln = a[1];
			}
		}
		// ISBN
		if( objNode.get("isbn") != null ) {
			if( document.book == null) document.book = new Book(document);
			document.book.isbn = objNode.get("isbn").textValue();
		}
		// DOI
		if( objNode.get("doi") != null ) {
			if( document.book == null) document.book = new Book(document);
			document.doi = objNode.get("doi").textValue();
		}
		
		return document;
	}
	
	private static boolean documentAlreadyKnown(Document document) {
		String urlWithoutSchema = document.documentUrl.replaceFirst("^.*://", "");
		if (Document.find.where().eq("regexp_replace(document_url,'^.*://','')", urlWithoutSchema).findRowCount() == 0) {
			return false;
		} else {
			return true;
		}
	}
	
	public static List<Document> filterNew(List<Document> documentList) {
		List<Document> newDocumentList = new ArrayList<>();
		for (Document document : documentList) {
			if (! documentAlreadyKnown(document))
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
				"id_target" + Const.CSV_SEPARATOR +
				"title" + Const.CSV_SEPARATOR +
				"landing_page_url" + Const.CSV_SEPARATOR +
				"document_url" + Const.CSV_SEPARATOR +
				"wayback_timestamp" + Const.CSV_LINE_END
		);
		
		for (Document document : query.findList()) {
			builder.append(
					document.id + Const.CSV_SEPARATOR +
					document.watchedTarget.target.id + Const.CSV_SEPARATOR +
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
    
    public static List<FastSubject> getFastSubjectList() {
        // Get all the FAST subjects:
        List<FastSubject> all = FastSubject.find.orderBy("name").findList();
        return all;
    }

	private static void setRelatedEntitiesOfModel(Document document, Form<Document> documentForm) {
        document.fastSubjects = FastSubjects
                .getFastSubjectsForDocument(documentForm);
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
