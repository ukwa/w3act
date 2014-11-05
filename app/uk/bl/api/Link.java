package uk.bl.api;

public class Link {
	public String source;
	public String target;
	
	public Link(String s, String t) {
		source = s;
		target = t;
	}
	
	public boolean equals(Object o) {
		if (o instanceof Link)
			return target.equals(((Link) o).target);
		else
			return false;
	}
	
	public int hashCode() {
		return target.hashCode();
	}
}
