package models;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import play.data.validation.Constraints.Required;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@MappedSuperclass
abstract class MappedSuperClass extends ActModel {

    /**
	 * Needed as "Properties from superclasses not mapped as @MappedSuperclass are ignored"
	 */
	private static final long serialVersionUID = 8159795832730112892L;

	@Required(message="Name is required")
    @JsonProperty
    public String name; 
    // additional field to make a difference between collection, subject, license and quality issue. 

    @Column(columnDefinition = "text")
    @JsonProperty
    public String description;

    @JsonProperty(value="field_publish")
    public Boolean publish;
    
    @Column(columnDefinition = "text") 
    @JsonIgnore
    public String parentsAll;

	@Column(columnDefinition = "text")
	@JsonProperty
	public String revision;
	
}
