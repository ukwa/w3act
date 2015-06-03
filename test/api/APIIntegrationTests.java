package api;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import play.Configuration;
import play.libs.F.Callback;
import play.libs.WS;
import play.test.*;
import static play.test.Helpers.*;

public class APIIntegrationTests {

    private long timeout_ms = 10*1000; // in milliseconds
	private Configuration additionalConfigurations;

	@Before
	public void initialize(){
	    Config additionalConfig = ConfigFactory.parseFile(new File("conf/dev.conf"));
	    additionalConfigurations = new Configuration(additionalConfig);
	}

    @Test
    public void testInServer() {
        running(testServer(3333, fakeApplication(additionalConfigurations.asMap())), new Runnable() {
			public void run() {
                assertThat(
                    WS.url("http://localhost:3333/act").get().get(timeout_ms).getStatus()
                ).isEqualTo(OK);
            }
        });
    }
    

    /**
     * Example from:
     * 
     * https://www.playframework.com/documentation/2.2.x/JavaFunctionalTest
     * 
     */
    //@Test
    public void runInBrowser() {
        running(testServer(3333, fakeApplication(additionalConfigurations.asMap())), HTMLUNIT, new Callback<TestBrowser>() {
            public void invoke(TestBrowser browser) {
               browser.goTo("http://localhost:3333"); 
               assertThat(browser.$("#title").getTexts().get(0)).isEqualTo("Hello Guest");
               browser.$("a").click();
               assertThat(browser.url()).isEqualTo("http://localhost:3333/Coco");
               assertThat(browser.$("#title", 0).getText()).isEqualTo("Hello Coco");
            }
        });
    }
}
