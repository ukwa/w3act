package uk.bl.api.models;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="wayback")
public class Wayback {
	
	private Request request;
	private Results results;
	
	public Request getRequest() {
		return request;
	}

	public void setRequest(Request request) {
		this.request = request;
	}

	public Results getResults() {
		return results;
	}

	public void setResults(Results results) {
		this.results = results;
	}

	@Override
	public String toString() {
		return "Wayback [request=" + request + ", results=" + results + "]";
	}
}

//<startdate>19910806145620</startdate>
//<numreturned>10</numreturned>
//<type>urlquery</type>
//<enddate>20150119100220</enddate>
//<numresults>10</numresults>
//<firstreturned>0</firstreturned>
//<url>bl.uk/bibliographic/ukmarc.html</url>
//<resultsrequested>10000</resultsrequested>
//<resultstype>resultstypecapture</resultstype>

//<result>
//<compressedoffset>80341769</compressedoffset>
//<mimetype>text/html</mimetype>
//<file>/data/102148/59179990/WARCS/BL-59179990-20110209175755-00038-safari.bl.uk.warc.gz?user.name=hadoop&bogus=.warc.gz</file>
//<redirecturl>-</redirecturl>
//<urlkey>bl.uk/bibliographic/ukmarc.html</urlkey>
//<digest>EUSVGFEDA2M7KSTFLPGGNQ5NSMP2EANF</digest>
//<httpresponsecode>200</httpresponsecode>
//<robotflags>-</robotflags>
//<url>http://www.bl.uk/bibliographic/ukmarc.html</url>
//<capturedate>20110209180259</capturedate>
//</result>