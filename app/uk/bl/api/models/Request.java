package uk.bl.api.models;

public class Request {
	
	private String startdate;
	private Long numreturned;
	private String type;
	private String enddate;
	private Long numresults;
	private Long firstreturned;
	private String url;
	private Long resultsrequested;
	private String resultstype;
	
	public String getStartdate() {
		return startdate;
	}
	
	public void setStartdate(String startdate) {
		this.startdate = startdate;
	}
	
	public Long getNumreturned() {
		return numreturned;
	}
	
	public void setNumreturned(Long numreturned) {
		this.numreturned = numreturned;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public String getEnddate() {
		return enddate;
	}
	
	public void setEnddate(String enddate) {
		this.enddate = enddate;
	}
	
	public Long getNumresults() {
		return numresults;
	}
	
	public void setNumresults(Long numresults) {
		this.numresults = numresults;
	}
	
	public Long getFirstreturned() {
		return firstreturned;
	}
	
	public void setFirstreturned(Long firstreturned) {
		this.firstreturned = firstreturned;
	}
	
	public String getUrl() {
		return url;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
	
	public Long getResultsrequested() {
		return resultsrequested;
	}
	
	public void setResultsrequested(Long resultsrequested) {
		this.resultsrequested = resultsrequested;
	}
	
	public String getResultstype() {
		return resultstype;
	}
	
	public void setResultstype(String resultstype) {
		this.resultstype = resultstype;
	}

	@Override
	public String toString() {
		return "Request [startdate=" + startdate + ", numreturned="
				+ numreturned + ", type=" + type + ", enddate=" + enddate
				+ ", numresults=" + numresults + ", firstreturned="
				+ firstreturned + ", url=" + url + ", resultsrequested="
				+ resultsrequested + ", resultstype=" + resultstype + "]";
	}
}
