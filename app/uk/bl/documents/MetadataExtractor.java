package uk.bl.documents;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.jsoup.select.Elements;

import play.Logger;
import models.Document;

public class MetadataExtractor {
	
	String nameSelector;
	String datePublishedSelector;
	String authorSelector;
	
	public MetadataExtractor(String nameSelector, String datePublishedSelector, String authorSelector) {
		this.nameSelector = nameSelector;
		this.datePublishedSelector = datePublishedSelector;
		this.authorSelector = authorSelector;
	}
	
	public void extract(Document document, org.jsoup.nodes.Document doc) {
		Elements name = doc.select(nameSelector);		
		if (!name.isEmpty()) {
			document.title = name.get(0).text();
			if (datePublishedSelector != null) {
				Elements datePublished = doc.select(datePublishedSelector);
				if (!datePublished.isEmpty()) {
					try {
						document.publicationDate = new SimpleDateFormat("yyyy-MM-dd").parse(datePublished.get(0).attr("content"));
						Calendar calendar = Calendar.getInstance();
						calendar.setTime(document.publicationDate);
						document.publicationYear = calendar.get(Calendar.YEAR);
					} catch (ParseException e) {}
				}
			}
			if (authorSelector != null) {
				Elements author = doc.select(authorSelector);
				if (!author.isEmpty()) {
					String authorsString = author.get(0).text();
					String[] authors = authorsString.split(",|and");
					if (authors.length >= 1) {
						String[] a = authors[0].trim().split("\\s+", 2);
						document.author1Fn = a[0];
						document.author1Ln = a[1];
					}
					if (authors.length >= 2) {
						String[] a = authors[1].trim().split("\\s+", 2);
						document.author2Fn = a[0];
						document.author2Ln = a[1];
					}
					if (authors.length >= 3) {
						String[] a = authors[2].trim().split("\\s+", 2);
						document.author3Fn = a[0];
						document.author3Ln = a[1];
					}
				}
			}
		} else {
			Logger.error("No "+nameSelector+" found!");
		}
	}
	
}
