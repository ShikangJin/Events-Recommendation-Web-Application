package skjinnero.com.recommendation.database;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import skjinnero.com.recommendation.entity.CategoryEntity;

@Repository
public class CategoryDao {
    @Autowired
    JdbcTemplate jdbcTemplate;

    public void save(CategoryEntity categoryEntity) {
        String sql = "INSERT IGNORE INTO categories VALUES (?,?)";

        for (String category : categoryEntity.getCategory()) {
            jdbcTemplate.update(sql, categoryEntity.getItem_id(), category);
        }
    }
}
