import static org.fest.assertions.Assertions.assertThat;
import static play.test.Helpers.FIREFOX;
import static play.test.Helpers.running;
import static play.test.Helpers.testServer;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import pages.BasicTestPage;
import play.libs.F.Callback;
import play.test.TestBrowser;
import uk.bl.Const;
import uk.bl.scope.EmailHelper;
import static play.test.Helpers.*;


/**
 * This class comprises integration tests.
 *
 */
public class IntegrationTest {

    /**
     * in this example we just check if the welcome page is being shown
     */   
// Since we do not submit to the Github the real settings to connect with Drupal these tests cann't be run on Travis.
//    @Test
    public void test() {
        running(testServer(3333, fakeApplication(inMemoryDatabase())), HTMLUNIT, new Callback<TestBrowser>() {
            @Override
			public void invoke(TestBrowser browser) {
                browser.goTo("http://localhost:3333");
                assertThat(browser.pageSource()).contains("W3ACT");
            }
        });
    }
    
//    @Test
    /**
     * This test is for testing of the basic email functionality. 
     * You need to replace the fist parameter of the sendMessage method with the 
     * valid email address that you can check for test results.
     */
	public void sendMail(){
	    EmailHelper.sendMessage("roman.graf@ait.ac.at","Message test","Message body");	
	}    
    
	/**
	 * This method enables an expert to see the current page if required.
	 * @param duration the integer value in ms (e.g. 2000 for 2 seconds)
	 */
	private void waitOnPage(int duration) {
        try {
    	    Thread.sleep(duration);
    	} catch(InterruptedException ex) {
    	    Thread.currentThread().interrupt();
    	}
	}
	
    /**
     * This test employs Selenium WebDriver in order to run in browser different W3ACT pages
     * starting with login page.
     */
//    @Test
    public void runInBrowser() {
        running(testServer(9000), FIREFOX, new Callback<TestBrowser>() {
            @Override
			public void invoke(TestBrowser browser) {
               WebDriver webDriver = browser.getDriver();
               BasicTestPage basicTestPage = new BasicTestPage(webDriver, 9000);
               browser.manage().window().maximize();
               browser.goTo(basicTestPage);
               System.out.println("title: " + browser.title());
               WebElement elementEmail = webDriver.findElement(By.name(Const.FIELD_EMAIL));
               // Enter default email address
               elementEmail.sendKeys(Const.DEFAULT_EMAIL);
               WebElement elementPassword = webDriver.findElement(By.name(Const.FIELD_PASSWORD));
               // Enter default password for associated email
               elementPassword.sendKeys(Const.DEFAULT_PASSWORD);
               // wait 2 seconds to allow an expert to see the site
               waitOnPage(2000);
               basicTestPage.submitForm();
               // take screenshot of the test resulting page if required 
//               browser.takeScreenShot();
               assertThat(browser.pageSource()).contains("About");
               browser.goTo("http://localhost:9000/targets"); 
               waitOnPage(5000);
               browser.goTo(Const.TEST_ORGANISATIONS_URL); 
               waitOnPage(5000);
               assertThat(browser.url()).isEqualTo(Const.TEST_ORGANISATIONS_URL);
            }
        });
    }
  
}
