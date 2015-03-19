package bdd.step_definitions;

import static org.fest.assertions.Assertions.assertThat;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.inMemoryDatabase;
import static play.test.Helpers.running;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;

import models.Collection;
import models.Target;
import play.Logger;
import play.libs.Json;
import play.libs.Yaml;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class ListCategoryTargets {

	private Long categoryId;
	private List<Target> targets;
	private int targetCount = 0;
	
	@Given("^I have a collection category ID of (\\d+)$")
	public void i_have_a_collection_category_ID_of(Long id) throws Throwable {
	    this.categoryId = id;
	}

	@When("^I choose to see to get the targets$")
	public void i_choose_to_see_to_get_the_targets() throws Throwable {
		running(fakeApplication(inMemoryDatabase()), new Runnable() {
			@SuppressWarnings("unchecked")
			public void run() {
				Map<String,List<Target>> allTargets = (Map<String,List<Target>>)Yaml.load("targets.yml");		
				List<Target> targs = allTargets.get("targets");
				for (Target target : targs) {
					Logger.debug("collection categories: " + target.collections.size());
					target.save();
				}
				Collection categoryFound = Collection.findById(categoryId);
				Logger.debug("categoryFound: " + categoryFound);
				targets = categoryFound.targets;
				targetCount = categoryFound.targets.size();
				Logger.debug("targets: " + targetCount);
		    	JsonNode jsonData = Json.toJson(targets);
				Logger.debug("JSON: " + jsonData);

			}
	   });
	}

	@Then("^I should see a target with the title \"(.*?)\"$")
	public void i_should_see_a_target_with_the_title(String expected) throws Throwable {
	    // Write code here that turns the phrase above into concrete actions
		String actualTitle = this.targets.get(0).title;
		assertThat(actualTitle).isEqualTo(expected);
	}

	@Then("^should only be (\\d+) target returned$")
	public void should_only_be_target_returned(int expected) throws Throwable {
		assertThat(this.targetCount).isEqualTo(expected);
	}

}
