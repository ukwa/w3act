import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import models.FieldUrl;
import models.Target;

import org.junit.Before;
import org.junit.Test;

import uk.bl.exception.ActException;
import uk.bl.scope.Scope;

public class UkHostingTest {

	Target target = null;
	List<FieldUrl> fieldUrls;

	@Before
	public void setUp() throws Exception {
		target = new Target();
		fieldUrls = new ArrayList<FieldUrl>();
		fieldUrls.add(new FieldUrl("http://109.123.65.110/")); // 109.123.65.110 was www.camdentownshed.org but hosting has changed
		target.fieldUrls = fieldUrls;
	}

	@Test
	public void test() throws ActException {
		Boolean pass = Scope.INSTANCE.isUkHosting(target);
		System.out.println("fieldUrls with valid top level domains: " + target.fieldUrls + " - " + pass);
		assertTrue(pass);
		FieldUrl f1 = new FieldUrl("http://heartoftheschool.edublogs.org/"); // 104.16.0.23
		FieldUrl f2 = new FieldUrl("http://www.newleftproject.org/"); // 205.186.179.65
		target.fieldUrls.add(f1);
		target.fieldUrls.add(f2);
		Boolean fail = Scope.INSTANCE.isUkHosting(target);
		System.out.println("fieldUrls with valid top level domains: " + target.fieldUrls + " - " + fail);
		assertFalse(fail);
		assertTrue(Scope.INSTANCE.queryDb("109.123.65.110"));
		assertFalse(Scope.INSTANCE.queryDb("104.16.0.23")); // false
		assertFalse(Scope.INSTANCE.queryDb("205.186.179.65")); // US
	}
}
