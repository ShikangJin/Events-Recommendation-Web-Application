package skjinnero.com.recommendation.entity;

import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@Table(name="categories")
@Data
@IdClass(CategoryPrimaryKey.class)
@DynamicUpdate
public class CategoryEntity {
    @Id
    @GeneratedValue
    @Column(name="item_id")
    private String item_id;

    @Id
    @GeneratedValue
    @Column(name="category")
    private String category;

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
