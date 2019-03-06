package skjinnero.com.recommendation.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class CategoryPrimaryKey implements Serializable {

    private String item_id;

    private String category;
}
