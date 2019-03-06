package skjinnero.com.recommendation.database;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import skjinnero.com.recommendation.entity.CategoryEntity;
import skjinnero.com.recommendation.entity.HistoryEntity;
import skjinnero.com.recommendation.entity.Item;
import skjinnero.com.recommendation.entity.ItemEntity;
import skjinnero.com.recommendation.external.TicketMasterAPI;

import javax.annotation.Resource;
import javax.persistence.Table;
import java.sql.ResultSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
public class DatabaseDao {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    TicketMasterAPI ticketMasterAPI;

    @Resource
    ItemDao itemDao;

    @Resource
    CategoryDao categoryDao;

    @Resource
    HistoryDao historyDao;

    public List<Item> searchItems(double lat, double lon, String term) {
        List<Item> items = ticketMasterAPI.search(lat, lon, term);
        for (Item item : items) {
            saveItem(item);
        }
        return items;
    }

    public void saveItem(Item item) {
        try {
            // First, insert into items table

            System.out.println("insert "+ item.getName());
            ItemEntity itemEntity = new ItemEntity();
            itemEntity.setAddress(item.getAddress());
            itemEntity.setDistance(item.getDistance());
            itemEntity.setImage_url(item.getImageUrl());
            itemEntity.setItem_id(item.getItemId());
            itemEntity.setName(item.getName());
            itemEntity.setRating(item.getRating());
            itemEntity.setUrl(item.getUrl());
            itemDao.save(itemEntity);

            // Second, update categories table for each category.
            CategoryEntity categoryEntity = new CategoryEntity();
            categoryEntity.setItem_id(item.getItemId());
            Set<String> set = item.getCategories();
            String category = "";
            if (set.size() > 0) {
                for(String c: set) {
                    category = c;
                    break;
                }
            }
            categoryEntity.setCategory(category);
            categoryDao.save(categoryEntity);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Set<Item> getFavoriteItems(String userId) {
        Set<Item> items = new HashSet<>();
        Set<String> itemIds = getFavoriteItemIds(userId);
        try {
            for (String itemId : itemIds) {
                String sql = "SELECT * FROM items WHERE item_id = \'" + itemId + "\'";
                String sql2 = "SELECT category FROM categories WHERE item_id = \'" + itemId + "\'";
                RowMapper<ItemEntity> rowMapper1 = new BeanPropertyRowMapper<>(ItemEntity.class);
                RowMapper<CategoryEntity> rowMapper2 = new BeanPropertyRowMapper<>(CategoryEntity.class);
                Item item = new Item(jdbcTemplate.queryForObject(sql, rowMapper1), jdbcTemplate.queryForObject(sql2, rowMapper2));
                items.add(item);
                // [ {“name”: “abcd”, “rating”: 0, “address”:”abcd”, ...},  ]
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return items;
    }

    public Set<String> getFavoriteItemIds(String userId) {
        String sql = "SELECT item_id FROM history WHERE user_id = \'" + userId + "\'";
        try {

            List<String> itemlist = jdbcTemplate.query(sql,
                    (ResultSet rs, int rowNuw) -> rs.getString("item_id")
            );
            return new HashSet<>(itemlist);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new HashSet<>();
    }

    public void unsetFavoriteItems(String userId, List<String> itemIds) {
        for (String itemId : itemIds) {
            HistoryEntity historyEntity = new HistoryEntity();
            historyEntity.setItem_id(itemId);
            historyEntity.setUser_id(userId);
            historyDao.delete(historyEntity);
        }
    }

    public void setFavoriteItems(String userId, List<String> itemIds) {
        for (String itemId : itemIds) {
            HistoryEntity historyEntity = new HistoryEntity();
            historyEntity.setItem_id(itemId);
            historyEntity.setUser_id(userId);
            historyDao.save(historyEntity);
        }
    }
}
