package models;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("tags")
public class TagTaxonomy extends Taxonomy {

}
