package skjinnero.com.recommendation.database;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import skjinnero.com.recommendation.entity.HistoryEntity;

@Repository
public class HistoryDao {
    @Autowired
    JdbcTemplate jdbcTemplate;

    public void save(HistoryEntity historyEntity) {
        String sql = "INSERT IGNORE INTO history (user_id, item_id) VALUES (?,?)";
        jdbcTemplate.update(sql, historyEntity.getUser_id(), historyEntity.getItem_id());
    }

    public void delete(HistoryEntity historyEntity) {
        String sql = "DELETE FROM history WHERE user_id = ? AND item_id = ?";
        jdbcTemplate.update(sql, historyEntity.getUser_id(), historyEntity.getItem_id());
    }
}
