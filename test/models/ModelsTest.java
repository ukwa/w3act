package models;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.avaje.ebean.Ebean;

import static org.junit.Assert.*;
import play.Logger;
import play.test.FakeApplication;
import play.test.Helpers;
import play.test.WithApplication;
import static play.test.Helpers.inMemoryDatabase;
import static play.test.Helpers.fakeApplication;

public class ModelsTest extends WithApplication {
	
	private final Long TEST_NID = 7777L;
	private final String TEST_EMAIL = "max.muster@ait.ac.at";
	private final String TEST_PASSWORD = "secret2";

	
    @Override
	protected FakeApplication provideFakeApplication() {
        return Helpers.fakeApplication(inMemoryDatabase());
    }
    
    
//    @Test
    public void createAndRetrieveTarget() {
//        Target targetNew = new Target("My title", "http://target.at");
//        targetNew.id = Long.valueOf(TEST_NID);
//        targetNew.save();
//        Target target = (Target) Target.find.where().eq("title", "My title").findUnique();
//        assertNotNull(target);
//        assertEquals("http://target.at", target.url);
//        Target.find.ref(TEST_NID).delete();
//        assertNull(Target.find.where().eq("title", "My title").findUnique());
    }

//
    @Test
    public void createAndRetrieveTargetWithItem() {
//        Target targetNew = new Target("My title", "http://target.at");
//        targetNew.id = Long.valueOf(TEST_NID);
//        targetNew.save();
//        Target target = (Target) Target.find.where().eq("title", "My title").findUnique();
//        target.fieldUrl = "test field_url";
//        Logger.info("Target test object: " + target.toString());
//        Ebean.update(target);
//        target.save();
//        Target res = (Target) Target.find.where().eq("title", "My title").findUnique();
//        Logger.info("target retrieved from db: " + res.toString());
//        String field_url = res.fieldUrl;
//        Logger.info("field_url res: " + res.toString() + ", value: " + field_url);
//        assertNotNull(res);
//        assertEquals("http://target.at", res.url);
//        Target.find.ref(TEST_NID).delete();
//        assertNull(Target.find.where().eq("title", "My title").findUnique());
    }

//
    @Test
    public void tryAuthenticateUser() {
//        User user = new User("Max Muster", TEST_EMAIL, TEST_PASSWORD);
//        user.save();
//        assertNotNull(User.authenticate(TEST_EMAIL, TEST_PASSWORD));
//        assertNull(User.authenticate(TEST_EMAIL, "badpassword"));
//        assertNull(User.authenticate("peter.king@ait.ac.at", TEST_PASSWORD));
//        user.delete();
    }
    
}