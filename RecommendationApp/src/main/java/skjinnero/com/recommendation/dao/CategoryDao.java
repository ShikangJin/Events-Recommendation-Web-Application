package skjinnero.com.recommendation.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import skjinnero.com.recommendation.entity.CategoryEntity;

import java.sql.ResultSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
public class CategoryDao {
    @Autowired
    JdbcTemplate jdbcTemplate;

    public void save(CategoryEntity categoryEntity) {
        String sql = "INSERT IGNORE INTO categories VALUES (?,?)";
        jdbcTemplate.update(sql, categoryEntity.getItem_id(), categoryEntity.getCategory());
    }

    public CategoryEntity select(String item_id) {
        String sql = "SELECT category FROM categories WHERE item_id = \'" + item_id + "\'";;
        RowMapper<CategoryEntity> rowMapper = new BeanPropertyRowMapper<>(CategoryEntity.class);
        return jdbcTemplate.queryForObject(sql, rowMapper);
    }

    public Set<String> getCategories(String item_id) {
        Set<String> categories = new HashSet<>();

        String sql = "SELECT category FROM categories WHERE item_id = \'" + item_id + "\'";
        try {
            List<String> itemlist = jdbcTemplate.query(sql, (ResultSet rs, int rowNuw) -> (
                    rs.getString("category"))
            );
            categories = new HashSet<>(itemlist);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return categories;
    }
}
