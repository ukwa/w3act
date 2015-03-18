package bdd;

import static org.fest.assertions.Assertions.assertThat;

import java.net.URI;

import org.apache.commons.lang3.BooleanUtils;

import play.Logger;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class UrlCheckSteps {
	
	private String url;
	private String path;
	private Boolean duplicate = Boolean.TRUE;


	@Given("^I have a URL of \"(.*?)\"$")
	public void i_have_a_URL_of(String url) throws Throwable {
		this.url = url;
	}

	@When("^I check to see if it exists in the DB$")
	public void i_check_to_see_if_it_exists_in_the_DB() throws Throwable {
        URI uri = new URI(url).normalize();
        this.path = uri.getHost();
        if (this.path.startsWith("www.")) {
        	this.path = this.path.replace("www.", "");
        }
        Logger.debug("Normalised " + uri);
        Logger.debug("Path: " + this.path);
	}

	@Then("^I should see a \"(.*?)\"$")
	public void i_should_see_a(String result) throws Throwable {
		Boolean expected = BooleanUtils.toBoolean(result);
		assertThat(this.duplicate).isEqualTo(expected);
	}

}
