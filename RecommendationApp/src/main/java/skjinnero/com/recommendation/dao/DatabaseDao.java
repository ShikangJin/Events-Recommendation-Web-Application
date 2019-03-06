package skjinnero.com.recommendation.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import skjinnero.com.recommendation.entity.CategoryEntity;
import skjinnero.com.recommendation.entity.HistoryEntity;
import skjinnero.com.recommendation.entity.Item;
import skjinnero.com.recommendation.entity.ItemEntity;
import skjinnero.com.recommendation.external.TicketMasterAPI;

import javax.annotation.Resource;
import java.util.ArrayList;
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
            for (String category : item.getCategories()) {
                CategoryEntity categoryEntity = new CategoryEntity();
                categoryEntity.setItem_id(item.getItemId());
                categoryEntity.setCategory(category);
                categoryDao.save(categoryEntity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Item> getFavoriteItems(String userId) {
        List<Item> items = new ArrayList<>();
        Set<String> itemIds = getFavoriteItemIds(userId);
        try {
            for (String itemId : itemIds) {
                ItemEntity itemEntity = itemDao.select(itemId);
                Set<String> categories = categoryDao.getCategories(itemId);
                Item.ItemBuilder builder = new Item.ItemBuilder();
                builder.setItemId(itemEntity.getItem_id());
                builder.setName(itemEntity.getName());
                builder.setAddress(itemEntity.getAddress());
                builder.setCategories(categories);
                builder.setDistance(itemEntity.getDistance());
                builder.setImageUrl(itemEntity.getImage_url());
                builder.setRating(itemEntity.getRating());
                builder.setUrl(itemEntity.getUrl());
                Item item = builder.build();
//                item.setFavorite();
                items.add(item);
                // [ {“name”: “abcd”, “rating”: 0, “address”:”abcd”, ...},  ]
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return items;
    }

    public Set<String> getFavoriteItemIds(String userId) {
        return historyDao.select(userId);
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

    public Set<String> getCategories(String item_id) {
        return categoryDao.getCategories(item_id);
    }
}
