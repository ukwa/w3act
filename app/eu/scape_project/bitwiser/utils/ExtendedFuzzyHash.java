/**
 * 
 */
package eu.scape_project.bitwiser.utils;

import org.apache.commons.lang3.StringUtils;

/**
 * 
 * Parses the Fuzzy Hash format:
 * 
 * ssdeep,1.1--blocksize:hash:hash,filename
 * 96:Axd6wa9IrXjodTkt8yJksfGwr9+grnw8LoCQBkhrmsl7OnsCYBaAcIjz:c6wvodwt5AkHLoDOhrUnsbBaEjz,"/Users/andy/Documents/workspace/w3act/README.md"
 * 96:qxd6wa9IrXjodTkt8yJksfGwr9+grnw8LoCQBkhrmsl7OnsCYBaI:q6wvodwt5AkHLoDOhrUnsbBaI,"/Users/andy/Documents/workspace/w3act/README-2.md"
 * 
 * @author andy
 *
 */
public class ExtendedFuzzyHash extends FuzzyHash {
	
	private static SSDeep ssd = new SSDeep();
	
	public static FuzzyHash fromString(String fhs) {
		if( StringUtils.isBlank(fhs) ) return null;
		String[] parts = fhs.split("[:,]+",4);
		if( parts.length != 3 && parts.length != 4 ) return null;
		FuzzyHash fh = new FuzzyHash(Integer.parseInt(parts[0]),parts[1],parts[2]);
		if( parts.length == 4)
			fh.filename = parts[3].replaceAll("^\"", "").replaceAll("\"$", "");
		return fh;
	}
	
	public static int compare(FuzzyHash h, FuzzyHash h2) {
		return ssd.fuzzy_compare(h,h2);
	}

	public static int compare(String h1, String h2) {
		FuzzyHash fh1 = ExtendedFuzzyHash.fromString(h1);
		FuzzyHash fh2 = ExtendedFuzzyHash.fromString(h2);
		if( fh1 == null || fh2 == null ) {
			return 0;
		}
		return ExtendedFuzzyHash.compare(fh1, fh2);
	}
}
