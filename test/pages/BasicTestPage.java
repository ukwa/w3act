package pages;

import org.fluentlenium.core.FluentPage;
import org.openqa.selenium.WebDriver;

// Although Eclipse marks the following two methods as deprecated,
// the no-arg versions of the methods used here are not deprecated. (as of May, 2013).

/**
* This page is a test for login page of W3ACT project
*/
public class BasicTestPage extends FluentPage {
  private String url;
  
  public BasicTestPage (WebDriver webDriver, int port) {
    super(webDriver);
    this.url = "http://localhost:" + port;
  }
  
  @Override
  public String getUrl() {
    return this.url;
  }
  
  @Override
  public void isAt() {
    assert(title().equals("W3ACT"));
  }
  
  /**
   * Submit the form whose id is "submit"
   */
  public void submitForm() {
      submit("#submit");
  }
  
  
}