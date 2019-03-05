package skjinnero.com.recommendation.database;

import org.springframework.context.annotation.ComponentScan;
import skjinnero.com.recommendation.entity.Item;
import skjinnero.com.recommendation.external.TicketMasterAPI;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@ComponentScan("skjinnero.com.recommendation.database")
public class DatabaseCmd {

    //不能在这里autowire 因为这个类会被别的类new autowire会失效
    private JdbcTemplate jdbcTemplate;

    private static DatabaseCmd dbcmd;


    private DatabaseCmd(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public static DatabaseCmd getDB(JdbcTemplate jdbcTemplate) {
        if (dbcmd == null) {
            dbcmd = new DatabaseCmd(jdbcTemplate);
        }
        return dbcmd;
    }

    public Set<String> getFavoriteItemIds(String userId) {
        if (jdbcTemplate == null) {
            System.out.println("jdbc null");
        }
        String sql = "SELECT item_id FROM history WHERE user_id = \'" + userId + "\'";
        try {

            List<String> itemlist = jdbcTemplate.query(sql, new RowMapper<String>(){
                @Override
                public String mapRow(ResultSet rs, int rowNuw) throws SQLException {
                    String res = rs.getString("item_id");
                    return res;
                }
            });
            return new HashSet<String>(itemlist);
        } catch (Exception e) {
            e.printStackTrace();
            return new HashSet<String>();
        }
    }

    public List<Item> searchItems(double lat, double lon, String term) {
        TicketMasterAPI tmAPI = new TicketMasterAPI();
        List<Item> items = tmAPI.search(lat, lon, term);
        for (Item item : items) {
            saveItem(item);
        }
        return items;
    }


    public void saveItem(Item item) {
        try {
            // First, insert into items table
            String sql = "INSERT INTO items (item_id, name, rating, address, image_url, url, distance) VALUES (?,?,?,?,?,?,?)" +
                    " ON DUPLICATE KEY UPDATE item_id=item_id";
            System.out.println("insert "+ item.getName());
            jdbcTemplate.update(sql,
                    item.getItemId(),
                    item.getName(),
                    item.getRating(),
                    item.getAddress(),
                    item.getImageUrl(),
                    item.getUrl(),
                    item.getDistance());

            // Second, update categories table for each category.
            sql = "INSERT IGNORE INTO categories VALUES (?,?)";
            for (String category : item.getCategories()) {
                jdbcTemplate.update(sql, item.getItemId(), category);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void setFavoriteItems(String userId, List<String> itemIds) {
        try {
            for (String itemId : itemIds) {
                String sql = "INSERT IGNORE INTO history (user_id, item_id) VALUES (?,?)";
                jdbcTemplate.update(sql, userId, itemId);
            }
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
                Item item = (Item)jdbcTemplate.queryForObject(sql, new RowMapper<Item>(){
                    @Override
                    public Item mapRow(ResultSet rs, int rowNuw) throws SQLException {
                        Item.ItemBuilder builder = new Item.ItemBuilder();
                        builder.setItemId(rs.getString("item_id"));
                        builder.setName(rs.getString("name"));
                        builder.setRating(rs.getDouble("rating"));
                        builder.setAddress(rs.getString("address"));
                        builder.setImageUrl(rs.getString("image_url"));
                        builder.setUrl(rs.getString("url"));
                        builder.setDistance(rs.getDouble("distance"));
                        builder.setCategories(getCategories(itemId));
                        Item item = builder.build();
                        item.setFavorite();
                        return item;
                    }
                });
                items.add(item);
                // [ {“name”: “abcd”, “rating”: 0, “address”:”abcd”, ...},  ]
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return items;
    }

    public Set<String> getCategories(String itemId) {
        Set<String> categories = new HashSet<>();

        String sql = "SELECT category FROM categories WHERE item_id = \'" + itemId + "\'";
        try {
            List<String> itemlist = jdbcTemplate.query(sql, new RowMapper<String>(){
                @Override
                public String mapRow(ResultSet rs, int rowNuw) throws SQLException {
                    String res = rs.getString("category");
                    return res;
                }
            });
            categories = new HashSet<>(itemlist);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return categories;
    }

    public void unsetFavoriteItems(String userId, List<String> itemIds) {
        String sql = "DELETE FROM history WHERE user_id = ? AND item_id = ?";
        try {
            for (String itemId : itemIds) {
                jdbcTemplate.update(sql, userId, itemId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
