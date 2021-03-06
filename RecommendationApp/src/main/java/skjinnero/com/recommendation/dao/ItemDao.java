package skjinnero.com.recommendation.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import skjinnero.com.recommendation.entity.ItemEntity;

@Repository
public class ItemDao {
    @Autowired
    JdbcTemplate jdbcTemplate;

    public void save(ItemEntity itemEntity) {
        try {
            String sql = "INSERT INTO items (item_id, name, rating, address, image_url, url, distance) VALUES (?,?,?,?,?,?,?)" +
                    " ON DUPLICATE KEY UPDATE item_id=item_id";
            jdbcTemplate.update(sql,
                    itemEntity.getItem_id(),
                    itemEntity.getName(),
                    itemEntity.getRating(),
                    itemEntity.getAddress(),
                    itemEntity.getImage_url(),
                    itemEntity.getUrl(),
                    itemEntity.getDistance());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ItemEntity select(String item_id) {
        String sql = "SELECT * FROM items WHERE item_id = \'" + item_id + "\'";
        RowMapper<ItemEntity> rowMapper = new BeanPropertyRowMapper<>(ItemEntity.class);
        return jdbcTemplate.queryForObject(sql, rowMapper);
    }
}
