package models;

public class WatchedTarget {
	public String url;
	public boolean deepDocumentSearch;
	
	public WatchedTarget (String url, boolean deepDocumentSearch) {
		this.url = url;
		this.deepDocumentSearch = deepDocumentSearch;
	}
}
