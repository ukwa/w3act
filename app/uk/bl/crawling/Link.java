package uk.bl.crawling;

public class Link {
	public String source;
	public String target;
	
	public Link(String s, String t) {
		source = s;
		target = t;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof Link)
			return target.equals(((Link) o).target);
		else
			return false;
	}
	
	@Override
	public int hashCode() {
		return target.hashCode();
	}
}
