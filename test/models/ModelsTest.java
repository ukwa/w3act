package models;

import org.junit.Before;
import org.junit.Test;

import com.avaje.ebean.Ebean;

import static org.junit.Assert.*;
import play.Logger;
import play.test.WithApplication;
import static play.test.Helpers.inMemoryDatabase;
import static play.test.Helpers.fakeApplication;

public class ModelsTest extends WithApplication {
    @Before
    public void setUp() {
        start(fakeApplication(inMemoryDatabase()));
    }
    
    @Test
    public void createAndRetrieveTarget() {
        Target targetNew = new Target("My title", "http://target.at");
        targetNew.nid = Long.valueOf(777);
        targetNew.save();
        Target target = (Target) Target.find.where().eq("title", "My title").findUnique();
        assertNotNull(target);
        assertEquals("http://target.at", target.url);
        Target.find.ref(777L).delete();
        assertNull(Target.find.where().eq("title", "My title").findUnique());
    }
    
    @Test
    public void createAndRetrieveTargetWithItem() {
        Target targetNew = new Target("My title", "http://target.at");
        targetNew.nid = Long.valueOf(777L);
        targetNew.save();
        Target target = (Target) Target.find.where().eq("title", "My title").findUnique();
        Item item = new Item();
        item.value = "item value";
        target.field_url.add(item);
        Logger.info("Target test object: " + target.toString());
//        target.saveManyToManyAssociations("field_url");
        Ebean.update(target);
        Ebean.save(target);
        target.save();
        Target res = (Target) Target.find.where().eq("title", "My title").findUnique();
        Logger.info("target retrieved from db: " + res.toString());
        Item fieldUrlRes = (Item) res.field_url.get(0);
        Logger.info("Item res: " + fieldUrlRes.toString() + ", value: " + fieldUrlRes.value);
        assertNotNull(res);
        assertEquals("http://target.at", res.url);
        Target.find.ref(777L).delete();
        assertNull(Target.find.where().eq("title", "My title").findUnique());
    }
    
    @Test
    public void tryAuthenticateUser() {
        new User("ross.king@ait.ac.at", "Ross King", "secret").save();
        
        assertNotNull(User.authenticate("ross.king@ait.ac.at", "secret"));
        assertNull(User.authenticate("ross.king@ait.ac.at", "badpassword"));
        assertNull(User.authenticate("peter.king@ait.ac.at", "secret"));
    }
    
}