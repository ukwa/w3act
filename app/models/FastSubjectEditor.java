/**
 * 
 */
package models;

import java.beans.PropertyEditorSupport;

/**
 * This class is used when parsing submission forms, and allows FastSubject
 * objects to be constructed from simple identifier strings used in forms.
 * 
 * As per
 * https://docs.spring.io/spring/docs/4.2.4.RELEASE/spring-framework-reference/html/validation.html#beans-beans-conversion-customeditor-registration
 * 
 * @author Andrew Jackson <Andrew.Jackson@bl.uk>
 *
 */
public class FastSubjectEditor extends PropertyEditorSupport {

    @Override
    public void setAsText(String text) {
        long id = Long.parseLong(text);
        setValue(FastSubject.findById(id));
    }
}
