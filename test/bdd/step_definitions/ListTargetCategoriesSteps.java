package bdd.step_definitions;

import static org.fest.assertions.Assertions.assertThat;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.inMemoryDatabase;
import static play.test.Helpers.running;

import java.util.List;
import java.util.Map;

import models.Collection;
import models.Target;
import play.Logger;
import play.libs.Yaml;
import play.test.WithApplication;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class ListTargetCategoriesSteps extends WithApplication {

	private Long targetId;
	private List<Collection> categories;
	private int categoryCount = 0;
	
	@Before
	public void setup() {
	}
	
	@Given("^I have a target ID of (\\d+)$")
	public void i_have_a_target_ID_of(Long id) throws Throwable {
		this.targetId = id;
	}

	@When("^I choose to see the collection categories$")
	public void i_choose_to_see_the_collection_categories() throws Throwable {
		running(fakeApplication(inMemoryDatabase()), new Runnable() {
			@Override
			public void run() {
				Target targetFound = Target.findById(targetId);
				Logger.debug("t: " + targetFound);
				categories = targetFound.getCollectionCategories();
				categoryCount = targetFound.getCollectionCategories().size();
			}
	   });
	}

	@Then("^I should see a collection category with the name \"(.*?)\"$")
	public void i_should_see_a_collection_category_with_the_name(String expected) throws Throwable {
		String actualName = this.categories.get(0).name;
		assertThat(actualName).isEqualTo(expected);
	}
	
	@Then("^should only be (\\d+) category returned$")
	public void should_only_be_category_returned(int expected) throws Throwable {
		assertThat(this.categoryCount).isEqualTo(expected);
	}


}
