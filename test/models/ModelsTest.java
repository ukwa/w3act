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
    public void createAndRetrieveTargetWithBody() {
        Target targetNew = new Target("My title", "http://target.at");
        targetNew.nid = Long.valueOf(777L);
        targetNew.save();
        Target target = (Target) Target.find.where().eq("title", "My title").findUnique();
        Body body = new Body();
        body.value = "body value";
        body.format = "body format";
        body.summary = "body summary";
        body.id = Long.valueOf(11112L);
        target.bodies.add(body);
        Logger.info("Target test object: " + target.toString() + ", bodies: " + target.bodies.size());
        target.saveManyToManyAssociations("bodies");
        Ebean.update(target);
        Ebean.save(target);
        target.save();
        Target res = (Target) Target.find.where().eq("title", "My title").findUnique();
        Logger.info("target retrieved from db: " + res.toString() + ", bodies: " + res.bodies.size());
        Body bodyRes = (Body) res.bodies.get(0);
        Logger.info("body res: " + bodyRes.toString() + ", value: " + bodyRes.value + ", format: " + 
        		bodyRes.format + ", id: " + bodyRes.id);
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