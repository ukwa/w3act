package models;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "subject")
@DiscriminatorValue("subject")
public class Subject extends Taxonomy {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
