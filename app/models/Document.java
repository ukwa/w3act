package models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import play.data.format.Formats.DateTime;
import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

@Entity
public class Document extends Model {
	
    @Id
    public Long id;
    public String url;
    @Required
    @Column(columnDefinition = "TEXT")
	public String title;
	public String doi;
	@DateTime(pattern="dd/MM/yyyy")
	public Date publication_date;
	
}