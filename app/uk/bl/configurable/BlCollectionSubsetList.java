package uk.bl.configurable;

import java.util.List;

import models.BlCollectionSubset;

public class BlCollectionSubsetList extends ConfigurableList<BlCollectionSubset> {
	
	@Override
	public String getFilePath() {
		return "conf/blCollectionSubsets.txt";
	}

	@Override
	public BlCollectionSubset createElement(String title) {
		return new BlCollectionSubset(title);
	}

	@Override
	public List<BlCollectionSubset> getCurrentElements() {
		return BlCollectionSubset.find.all();
	}

	@Override
	public List<BlCollectionSubset> getActiveElements() {
		return BlCollectionSubset.find.where().eq("active", true).findList();
	}
		
	
}
