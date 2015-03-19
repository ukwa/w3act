package bdd.step_definitions;

import static org.fest.assertions.Assertions.assertThat;
import org.apache.commons.lang3.BooleanUtils;

import uk.bl.api.Utils;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class UrlCheckSteps {
	
	private String url;
	private Boolean duplicate = Boolean.FALSE;
	private String dbUrl = "https://www.bbc.co.uk/test1&query=1&terri=2";


	@Given("^I have a URL of \"(.*?)\"$")
	public void i_have_a_URL_of(String url) throws Throwable {
		this.url = url;
	}

	@When("^I check to see if it exists in the DB$")
	public void i_check_to_see_if_it_exists_in_the_DB() throws Throwable {
        this.duplicate = Utils.INSTANCE.isDuplicate(this.url, this.dbUrl);
	}

	@Then("^I should see a \"(.*?)\"$")
	public void i_should_see_a(String result) throws Throwable {
		Boolean expected = BooleanUtils.toBoolean(result);
		assertThat(this.duplicate).isEqualTo(expected);
	}

}
