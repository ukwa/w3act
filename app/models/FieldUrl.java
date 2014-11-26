package models;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "field_url")
public class FieldUrl extends ActModel {

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "target_id")
	public Target target;

	
	public FieldUrl(String url) {
		super();
		this.url = url;
	}


	@Override
	public String toString() {
		return "FieldUrl [url=" + url + "]";
	}
}
