package bdd;

import static org.fest.assertions.Assertions.assertThat;

import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.BooleanUtils;

import models.FieldUrl;
import models.Target;
import play.Logger;
import uk.bl.scope.Scope;
import cucumber.api.PendingException;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class TopLevelDomainPassedSteps {
	
	private String url;
	private Scope scope = Scope.INSTANCE;
	private Boolean topLevelDomain = Boolean.FALSE;
	
	@Given("^I have a Welsh Target domain of \"(.*?)\"$")
	public void i_have_a_Welsh_Target_domain_of(String url) throws Throwable {
		this.url = "http://www.competitions.wales/test.uk";
        assertThat(this.url).isEqualTo(url);
        String actual = scope.getDomainFromUrl(this.url);
        String expected = scope.getDomainFromUrl(url);
        assertThat(actual).isEqualTo(expected);
	}

	@When("^I check to see if it passes a top-level domain$")
	public void i_check_to_see_if_it_passes_a_top_level_domain() throws Throwable {
		Target target = new Target();
		List<FieldUrl> fieldUrls = new ArrayList<FieldUrl>();
        URL uri = new URI(url).normalize().toURL();
		String url = uri.toExternalForm();
        Logger.debug("Normalised " + url);
		
        String domain = scope.getDomainFromUrl(url);
        Logger.debug("domain " + domain);
		FieldUrl fieldUrl = new FieldUrl(url);
		fieldUrl.domain = domain;
		fieldUrls.add(fieldUrl);
		target.fieldUrls = fieldUrls;
		this.topLevelDomain = scope.isTopLevelDomain(target);
	}

	@Then("^I should see it has passed with a \"(.*?)\"$")
	public void i_should_see_it_has_passed_with_a(String arg1) throws Throwable {
		Boolean expected = BooleanUtils.toBoolean(topLevelDomain);
		assertThat(this.topLevelDomain).isEqualTo(expected);
	}

}
