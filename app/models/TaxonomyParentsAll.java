package models;


import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import play.db.ebean.*;

import java.util.List;


/**
 * Taxonomy vocabulary entity managed by Ebean
 */
@Entity
@Table(name = "public.taxonomy_parents_all")
public class TaxonomyParentsAll extends Model {

    private static final long serialVersionUID = 4407791639097588113L;

    //serialVersionUID

    public TaxonomyParentsAll() { super(); }


    @Column(columnDefinition = "taxonomy_id")
    public Long taxonomyId;

    @Column(columnDefinition = "parent_id")
    public Long parentId;

/*
    @JsonIgnore
    @ManyToMany(cascade = CascadeType.REFRESH)
    @JoinTable(name = "taxonomy", joinColumns = { @JoinColumn(name = "taxonomy_id", referencedColumnName="id") },
            inverseJoinColumns = { @JoinColumn(name = "id", referencedColumnName="parent_id") })
    public List<TaxonomyParentsAll> taxonomyParentAll;
*/

    public static Model.Finder<Long,TaxonomyParentsAll> findTaxonomyParentsAll = new Model.Finder<>(Long.class, TaxonomyParentsAll.class);

    /**
     * Get TaxonomyParentsAll Taxonomy IDs by Parent ID
     * For example get Collections by Collection Area
     * @param parent_id
     * @return
     */
    public static List<TaxonomyParentsAll> findByParentId(Long parent_id) { //get taxonomy ids by parent id
        List<TaxonomyParentsAll> taxonomyParentAll = findTaxonomyParentsAll.where().eq("taxonomy_id", parent_id).findList();
        return taxonomyParentAll;
    }

}
