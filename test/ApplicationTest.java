import models.User;

import org.junit.Test;

import play.mvc.Content;
import play.mvc.*;

import play.test.*;
import play.data.DynamicForm;
import play.data.validation.ValidationError;
import play.data.validation.Constraints.RequiredValidator;
import play.i18n.Lang;
import play.libs.F;
import play.libs.F.*;

import static play.test.Helpers.*;
import static org.fest.assertions.Assertions.*;

/**
*
* Simple (JUnit) tests that can call all parts of a play app.
* If you are interested in mocking a whole application, see the wiki for more details.
*
*/
public class ApplicationTest {

    @Test 
    public void simpleCheck() {
        int a = 1 + 1;
        assertThat(a).isEqualTo(2);
    }
    
    @Test
    public void renderTemplate() {
        Content html = views.html.about.render("W3ACT", new User("Ross King")); 
        assertThat(contentType(html)).isEqualTo("text/html");
        assertThat(contentAsString(html)).contains("W3ACT");
    }
  
   
}
