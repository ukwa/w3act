package eu.scape_project.bitwiser.utils;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.*;
import static play.test.Helpers.contentAsString;

import org.junit.Test;

public class ExtendedFuzzyHashTest {

	@Test
	public void testComparison() {
		FuzzyHash fh1 = ExtendedFuzzyHash.fromString("96:Axd6wa9IrXjodTkt8yJksfGwr9+grnw8LoCQBkhrmsl7OnsCYBaAcIjz:c6wvodwt5AkHLoDOhrUnsbBaEjz,\"README.md\"");
		FuzzyHash fh2 = ExtendedFuzzyHash.fromString("96:qxd6wa9IrXjodTkt8yJksfGwr9+grnw8LoCQBkhrmsl7OnsCYBaI:q6wvodwt5AkHLoDOhrUnsbBaI,\"README-2.md\"");
		System.out.println("FH1: "+fh1.toString());
		System.out.println("FH2: "+fh2.toString());
		int comparison =  ExtendedFuzzyHash.compare(fh1, fh2);
		System.out.println("Comparison = "+comparison);
        assertThat(comparison).isEqualTo(97);
	}

	@Test
	public void testComparisonWithoutNames() {
		FuzzyHash fh1 = ExtendedFuzzyHash.fromString("96:Axd6wa9IrXjodTkt8yJksfGwr9+grnw8LoCQBkhrmsl7OnsCYBaAcIjz:c6wvodwt5AkHLoDOhrUnsbBaEjz");
		FuzzyHash fh2 = ExtendedFuzzyHash.fromString("96:qxd6wa9IrXjodTkt8yJksfGwr9+grnw8LoCQBkhrmsl7OnsCYBaI:q6wvodwt5AkHLoDOhrUnsbBaI");
		System.out.println("FH1: "+fh1.toString());
		System.out.println("FH2: "+fh2.toString());
		int comparison =  ExtendedFuzzyHash.compare(fh1, fh2);
		System.out.println("Comparison = "+comparison);
        assertThat(comparison).isEqualTo(97);
	}

}
