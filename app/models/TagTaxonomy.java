package models;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("tags")
public class TagTaxonomy extends Taxonomy {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2775721952460456022L;

}
