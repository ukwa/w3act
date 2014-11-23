package models;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("collection_areas")
public class CollectionArea extends Taxonomy {

}
