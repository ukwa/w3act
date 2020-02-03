package models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import play.db.ebean.Model;

import javax.persistence.*;
import java.util.List;

@Entity
@DiscriminatorValue("collection_areas")
public class CollectionArea extends Taxonomy {


    public static Finder<Long,CollectionArea> find = new Finder<Long, CollectionArea>(Long.class, CollectionArea.class);

    public static List<CollectionArea> findAllCollectionAreas() {
        return find.orderBy("name asc").findList();
    }

}
