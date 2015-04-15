package bdd.step_definitions;

import static org.fest.assertions.Assertions.assertThat;

import java.util.List;
import java.util.Map;

import play.Logger;
import play.libs.Yaml;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.running;
import static play.test.Helpers.*;

import uk.bl.api.Utils;
import models.Target;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class ExportTargetsSteps {

//	http://localhost:9000/actdev/targets/list?s=title&f=bbc.co.uk
	private List<Target> targets;
	private String results;
	
	@Given("^I have a list of Target result on view$")
	public void i_have_a_list_of_Target_result_on_view() throws Throwable {

		running(fakeApplication(inMemoryDatabase()), new Runnable() {
			public void run() {
				Map<String,List<Target>> allTargets = (Map<String,List<Target>>)Yaml.load("targets.yml");		
				targets = allTargets.get("targets");
				Logger.debug("targets: " + targets);
			}
	   });

	}

	@When("^I choose the export function$")
	public void i_choose_the_export_function() throws Throwable {
		results = Utils.INSTANCE.export(targets);
		Logger.debug("results: " + results);
	}

	@Then("^I should see headings with \"(.*?)\" \"(.*?)\" \"(.*?)\" \"(.*?)\" \"(.*?)\" \"(.*?)\"$")
	public void i_should_see_headings_with(String nid, String title, String fieldUrl, String author, String field_crawl_frequency, String created) throws Throwable {
		assertThat(results).contains(nid);
		assertThat(results).contains(title);
		assertThat(results).contains(fieldUrl);
		assertThat(results).contains(author);
		assertThat(results).contains(field_crawl_frequency);
		assertThat(results).contains(created);
	}
	
	@Then("^data with \"(.*?)\" \"(.*?)\" \"(.*?)\" \"(.*?)\" \"(.*?)\" \"(.*?)\"$")
	public void data_with(String arg1, String arg2, String arg3, String arg4, String arg5, String arg6) throws Throwable {
		assertThat(results).contains(arg1);
		assertThat(results).contains(arg2);
		assertThat(results).contains(arg3);
		assertThat(results).contains(arg4);
		assertThat(results).contains(arg5);
		assertThat(results).contains(arg6);
	}


}
