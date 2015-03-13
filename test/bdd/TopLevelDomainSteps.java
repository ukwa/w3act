package bdd;

import static org.fest.assertions.Assertions.assertThat;

import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.BooleanUtils;

import play.Logger;
import models.Target;
import models.FieldUrl;
import uk.bl.scope.Scope;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class TopLevelDomainSteps {
	
	private String url;
	private Boolean topLevelDomain = Boolean.FALSE;
	
	@Given("^I have a Target domain of \"(.*?)\"$")
	public void i_have_a_Target_domain_of(String url) throws Throwable {
		this.url = url;
        assertThat(this.url).isNotEmpty();
	}

	@When("^I check to see if it is a top-level domain$")
	public void i_check_to_see_if_it_is_a_top_level_domain() throws Throwable {
		Target target = new Target();
		List<FieldUrl> fieldUrls = new ArrayList<FieldUrl>();
        URL uri = new URI(url).normalize().toURL();
		String url = uri.toExternalForm();
        Logger.debug("Normalised " + url);
		
        String domain = Scope.INSTANCE.getDomainFromUrl(url);
        Logger.debug("domain " + domain);
		FieldUrl fieldUrl = new FieldUrl(url);
		fieldUrl.domain = domain;
		fieldUrls.add(fieldUrl);
		target.fieldUrls = fieldUrls;
		this.topLevelDomain = Scope.INSTANCE.isTopLevelDomain(target);
	}

	@Then("^I should see it is \"(.*?)\"$")
	public void i_should_see_it_is(String topLevelDomain) throws Throwable {
		Boolean expected = BooleanUtils.toBoolean(topLevelDomain);
		assertThat(this.topLevelDomain).isEqualTo(expected);
	}


}
