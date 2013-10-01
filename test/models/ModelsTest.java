package models;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import play.test.WithApplication;
import static play.test.Helpers.inMemoryDatabase;
import static play.test.Helpers.fakeApplication;

public class ModelsTest extends WithApplication {
    @Before
    public void setUp() {
        start(fakeApplication(inMemoryDatabase()));
    }
    
    @Test
    public void createAndRetrieveSite() {
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
    public void tryAuthenticateUser() {
        new User("ross.king@ait.ac.at", "Ross King", "secret").save();
        
        assertNotNull(User.authenticate("ross.king@ait.ac.at", "secret"));
        assertNull(User.authenticate("ross.king@ait.ac.at", "badpassword"));
        assertNull(User.authenticate("peter.king@ait.ac.at", "secret"));
    }
    
}