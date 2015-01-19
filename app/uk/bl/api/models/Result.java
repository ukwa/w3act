package uk.bl.api.models;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="result")
public class Result {
	
	private Long compressedoffset;
	private String mimetype;
	private String file;
	private String redirecturl;
	private String urlkey;
	private String digest;
	private String httpresponsecode;
	private String robotflags;
	private Long capturedate;
	
	public Long getCompressedoffset() {
		return compressedoffset;
	}

	public void setCompressedoffset(Long compressedoffset) {
		this.compressedoffset = compressedoffset;
	}

	public String getMimetype() {
		return mimetype;
	}

	public void setMimetype(String mimetype) {
		this.mimetype = mimetype;
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

	public String getRedirecturl() {
		return redirecturl;
	}

	public void setRedirecturl(String redirecturl) {
		this.redirecturl = redirecturl;
	}

	public String getUrlkey() {
		return urlkey;
	}

	public void setUrlkey(String urlkey) {
		this.urlkey = urlkey;
	}

	public String getDigest() {
		return digest;
	}

	public void setDigest(String digest) {
		this.digest = digest;
	}

	public String getHttpresponsecode() {
		return httpresponsecode;
	}

	public void setHttpresponsecode(String httpresponsecode) {
		this.httpresponsecode = httpresponsecode;
	}

	public String getRobotflags() {
		return robotflags;
	}

	public void setRobotflags(String robotflags) {
		this.robotflags = robotflags;
	}

	public Long getCapturedate() {
		return capturedate;
	}

	public void setCapturedate(Long capturedate) {
		this.capturedate = capturedate;
	}

	@Override
	public String toString() {
		return "Result [compressedoffset=" + compressedoffset
				+ ", mimetype=" + mimetype + ", file=" + file
				+ ", redirecturl=" + redirecturl + ", urlkey=" + urlkey
				+ ", digest=" + digest + ", httpresponsecode="
				+ httpresponsecode + ", robotflags=" + robotflags
				+ ", capturedate=" + capturedate + "]";
	}
}
