package models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.avaje.ebean.Expr;
import com.avaje.ebean.Page;

import play.db.ebean.Model;

@Entity
@Table(name = "fast_subject_by_priority")
public class FastSubjectByPriority extends Model {
	
	/** */
    private static final long serialVersionUID = -2734834285059204305L;

	@Id
	public Long id;
	
    @OneToOne
    @JoinColumn(name = "id_document")
    public Document document;

    @ManyToOne
    @JoinColumn(name = "id_fast_subject")
    public FastSubject fastSubject;
	
    public Long priority;
	
	public static final Model.Finder<Long, FastSubjectByPriority> find = new Model.Finder<>(Long.class, FastSubjectByPriority.class);
	
}