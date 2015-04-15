package bdd.step_definitions;

import static org.fest.assertions.Assertions.assertThat;

import org.apache.commons.lang3.BooleanUtils;

import play.Logger;
import uk.bl.api.Utils;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class UrlMalformedSteps {

	private String url;
	private Boolean valid = Boolean.FALSE;
	
	@Given("^I have a malformed URL of \"(.*?)\"$")
	public void i_have_a_malformed_URL_of(String url) throws Throwable {
		this.url = url;
    	Logger.debug("original url: " + this.url);
	}

	@When("^I check to see if it is malformed$")
	public void i_check_to_see_if_it_is_malformed() throws Throwable {
		valid = Utils.INSTANCE.validUrl(url);
	}

	@Then("^I should see a result of \"(.*?)\"$")
	public void i_should_see_a_result_of(String result) throws Throwable {
		Boolean expected = BooleanUtils.toBoolean(result);
		assertThat(this.valid).isEqualTo(expected);
	}
	
}
