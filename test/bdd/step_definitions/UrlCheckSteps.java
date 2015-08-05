package bdd.step_definitions;

import static org.fest.assertions.Assertions.assertThat;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.inMemoryDatabase;
import static play.test.Helpers.running;

import java.util.List;
import java.util.Map;

import models.FieldUrl;
import models.Target;

import org.apache.commons.lang3.BooleanUtils;

import play.Logger;
import play.libs.Yaml;

import uk.bl.exception.ActException;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class UrlCheckSteps {
	
	private String url;
	private Boolean duplicate = Boolean.FALSE;
//	private String dbUrl = "https://www.bbc.co.uk/test1&query=1&terri=2";
//	private List<FieldUrl> fieldUrls =  null;


	@Given("^I have a URL of \"(.*?)\"$")
	public void i_have_a_URL_of(String url) throws Throwable {
		this.url = url;
    	Logger.debug("original url: " + this.url);
	}

	@When("^I check to see if it exists in the DB$")
	public void i_check_to_see_if_it_exists_in_the_DB() throws Throwable {
		running(fakeApplication(inMemoryDatabase()), new Runnable() {
			@Override
			@SuppressWarnings("unchecked")
			public void run() {
				Map<String,List<Target>> allTargets = (Map<String,List<Target>>)Yaml.load("targets.yml");		
				List<Target> targs = allTargets.get("targets");
				for (Target target : targs) {
					Logger.debug("Target target: " + target.fieldUrl());
					target.save();
				}
		        try {
		        	duplicate = (FieldUrl.hasDuplicate(url) != null);
				} catch (ActException e) {
					e.printStackTrace();
				}

			}
	   });
//        this.duplicate = Utils.INSTANCE.isDuplicate(this.url, this.dbUrl);
	}

	@Then("^I should see a \"(.*?)\"$")
	public void i_should_see_a(String result) throws Throwable {
//		assertThat(fieldUrls.size()).isGreaterThan(0);
		Boolean expected = BooleanUtils.toBoolean(result);
		assertThat(this.duplicate).isEqualTo(expected);
	}

}
