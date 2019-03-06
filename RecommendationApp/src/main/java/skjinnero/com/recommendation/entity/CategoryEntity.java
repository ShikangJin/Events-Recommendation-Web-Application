package skjinnero.com.recommendation.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="categories")
@Data
@IdClass(PrimaryKey.class)
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

    public Set<String> getCategory() {
        Set<String> categories = new HashSet<>();
        categories.add(category);
        return categories;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
