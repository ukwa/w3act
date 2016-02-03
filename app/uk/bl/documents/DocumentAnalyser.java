/**
 * 
 */
package uk.bl.documents;

import java.net.URI;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.Document;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.metadata.TikaCoreProperties;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.sax.BodyContentHandler;
import org.apache.tika.sax.WriteOutContentHandler;
import org.jsoup.Jsoup;

import play.Logger;
import play.libs.F.Function0;

import com.avaje.ebean.Ebean;

import controllers.WaybackController;
import eu.scape_project.bitwiser.utils.FuzzyHash;
import eu.scape_project.bitwiser.utils.SSDeep;

/**
 * 
 * Cleaned up and extended document analyser.
 * 
 * Relies on content being present in Wayback for analysis.
 * 
 * @author andy
 *
 */
public class DocumentAnalyser {

	private static String waybackUrl = WaybackController.getWaybackEndpoint();
	
	private Map<String, MetadataExtractor> metadataExtractors;
	
	public DocumentAnalyser() {
		metadataExtractors = new HashMap<>();
		metadataExtractors.put("www.ifs.org.uk", new MetadataExtractor("*[itemtype=http://schema.org/CreativeWork] *[itemprop=name]",
				"*[itemtype=http://schema.org/CreativeWork] *[itemprop=datePublished]",
				"*[itemtype=http://schema.org/CreativeWork] *[itemprop=author]"));
		metadataExtractors.put("www.gov.uk", new MetadataExtractor("h1", null, null));
	}
	
	public void extractMetadata(Document document) {
		// Get the Wayback URL:
		String wbu = waybackReplayUrl(document.documentUrl, document.waybackTimestamp);
		
		// Get the binary hash
		try {
			URL wburl = new URL(wbu);
			byte[] digest = DigestUtils.sha256(wburl.openStream());
			document.sha256Hash = String.format("%064x", new java.math.BigInteger(1, digest));
			Logger.info("Recorded sha256Hash "+document.sha256Hash+" for "+document.documentUrl);
		} catch (Exception e) {
			Logger.error("Failure while SHA256 hashing "+document.documentUrl);
			e.printStackTrace();
		}
		
		// Extended metadata and text:
		String text = null;
		try {
			Logger.info("Running extraction process...");
			String domain = new URI(document.landingPageUrl).getHost();
			if (metadataExtractors.containsKey(domain)) {
				MetadataExtractor metadataExtractor = metadataExtractors.get(domain);
				String wblpu = waybackReplayUrl(document.landingPageUrl, document.waybackTimestamp);
				org.jsoup.nodes.Document doc = Jsoup.connect(wblpu).get();
				metadataExtractor.extract(document, doc);
			}
			// Use Tika on it:
			AutoDetectParser parser = new AutoDetectParser();
			Metadata metadata = new Metadata();
			BodyContentHandler handler = new BodyContentHandler();
			URL wburl = new URL(wbu);
			try {
				parser.parse(wburl.openStream(), handler, metadata);
			} catch( Exception e) {
				Logger.error("Exception while running Tika on "+document.documentUrl);
			}
			// Pull in the text:
			text = handler.toString();
			// Use anything we find:
			if( StringUtils.isBlank(document.title) && 
					StringUtils.isNotBlank(metadata.get(TikaCoreProperties.TITLE)) ) {
				document.title = metadata.get(TikaCoreProperties.TITLE);
			}
			if( metadata.get(TikaCoreProperties.CREATED) != null ) {
				Date created = metadata.getDate(TikaCoreProperties.CREATED);
				if( document.publicationDate == null ) {
					document.publicationDate = created;
				}
				if( document.publicationYear == null ) {
					SimpleDateFormat df = new SimpleDateFormat("yyyy");
					document.publicationYear = Integer.parseInt(df.format(created));
				}
			}
			if( StringUtils.isBlank(document.author1Fn) && 
					StringUtils.isNotBlank(metadata.get(Metadata.AUTHOR)) ) {
				document.author1Fn = metadata.get(Metadata.AUTHOR);
			}
			// Ouput all for debugging:
			for( String k : metadata.names()) {
				Logger.debug("Found "+k+" -> "+metadata.get(k));
			}
		} catch (Exception e) {
			Logger.error("Failure while parsing "+document.documentUrl);
			e.printStackTrace();
		}
		
		// Use the text from Tika to make a fuzzy hash:
		Logger.info("Attempting ssdeep hash generation...");
		if( StringUtils.isNoneBlank(text)) {
			SSDeep ssd = new SSDeep();
			FuzzyHash fh = ssd.fuzzy_hash_buf(text.getBytes());
			document.ctpHash = fh.getBlocksize()+":"+fh.getHash()+":"+fh.getHash2()+":\""+document.filename+"\"";
			Logger.info("Recorded ctpHash "+document.ctpHash+" for "+document.documentUrl);
		}
	}
	
	private String waybackReplayUrl(String url, String timestamp) {
		return waybackUrl + "replay?url=" + url + "&date=" + timestamp;
	}
	

	
	/**
	 * 
	 * @author andy
	 *
	 */
	public static class ExtractFunction implements Function0<Boolean> {
		
		public List<Document> documents;
		public ExtractFunction(List<Document> documents) {
			this.documents = documents;
		}
		
		@Override
		public Boolean apply() {
			Logger.info("Extracting from "+documents.size()+" documents.");
			for (Document document : documents) {
				DocumentAnalyser da = new DocumentAnalyser();
				da.extractMetadata(document);
				Logger.info("Saving updated document metadata.");
				Ebean.save(document);
			}
			return true;
		}
	}
}
