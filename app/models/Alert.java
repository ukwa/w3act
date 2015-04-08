package models;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.avaje.ebean.Page;

import play.db.ebean.Model;

@Entity
public class Alert extends Model {
	
	@Id
	public Long id;
	@ManyToOne
	@JoinColumn(name="id_creator")
	public User user;
	@Column(columnDefinition="text")
	public String text;
	public Date createdAt;
	public boolean read;
	
	public static final Model.Finder<Long, Alert> find = new Model.Finder<>(Long.class, Alert.class);
	
	public static Page<Alert> page(int page, int pageSize, String sortBy, String order) {
        return find.where().orderBy(sortBy + " " + order)
        		.findPagingList(pageSize)
        		.setFetchAhead(false)
        		.getPage(page);
    }
	
	public String formattedDateString() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
		return dateFormat.format(createdAt);
	}
	
}