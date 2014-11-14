package uk.bl.api.models;

public class Author {

	private String uri;
	private String id;
	private String resource;
	
	public String getUri() {
		return uri;
	}
	
	public void setUri(String uri) {
		this.uri = uri;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public String getResource() {
		return resource;
	}
	public void setResource(String resource) {
		this.resource = resource;
	}

	@Override
	public String toString() {
		return "Author [uri=" + uri + ", id=" + id + ", resource=" + resource
				+ "]";
	}
}
