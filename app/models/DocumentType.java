package models;

public enum DocumentType {
	BOOK ("Book"),
	BOOK_CHAPTER ("Book Chapter"),
	JOURNAL_ARTICLE ("Journal Article"),
	JOURNAL_ISSUE ("Journal Issue");
	
	private final String name;
	
	DocumentType(String name) {
		this.name = name;
	}
	
	public String toString() {
		return name;
	}
}