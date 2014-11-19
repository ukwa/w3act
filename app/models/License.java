package models;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("license")
public class License extends Taxonomy {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
}
