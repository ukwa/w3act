package uk.bl.scope;

import java.util.List;

import com.avaje.ebean.SqlRow;

import models.Target;

public class WhoIsData {
	
	private List<Target> targets;
	private List<SqlRow> results;
	private int ukRegistrantCount;
	private int nonUKRegistrantCount;
	private int failedCount;
	
	public WhoIsData() {}
	
	public WhoIsData(List<Target> targets, List<SqlRow> results, int ukRegistrantCount,
			int nonUKRegistrantCount, int failedCount) {
		this.targets = targets;
		this.results = results;
		this.ukRegistrantCount = ukRegistrantCount;
		this.nonUKRegistrantCount = nonUKRegistrantCount;
		this.failedCount = failedCount;
	}

	public List<Target> getTargets() {
		return targets;
	}

	public void setTargets(List<Target> targets) {
		this.targets = targets;
	}

	public List<SqlRow> getResults() {
		return results;
	}

	public void setResults(List<SqlRow> results) {
		this.results = results;
	}

	public int getUkRegistrantCount() {
		return ukRegistrantCount;
	}

	public void setUkRegistrantCount(int ukRegistrantCount) {
		this.ukRegistrantCount = ukRegistrantCount;
	}

	public int getNonUKRegistrantCount() {
		return nonUKRegistrantCount;
	}

	public void setNonUKRegistrantCount(int nonUKRegistrantCount) {
		this.nonUKRegistrantCount = nonUKRegistrantCount;
	}

	public int getFailedCount() {
		return failedCount;
	}

	public void setFailedCount(int failedCount) {
		this.failedCount = failedCount;
	}
	
	

	
}
