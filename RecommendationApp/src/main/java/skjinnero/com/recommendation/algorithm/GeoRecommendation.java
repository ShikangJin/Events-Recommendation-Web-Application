package skjinnero.com.recommendation.algorithm;

import skjinnero.com.recommendation.database.DatabaseCmd;
import skjinnero.com.recommendation.entity.Item;

import java.util.*;

public class GeoRecommendation {
    public List<Item> recommendItems(String userId, double lat, double lon) {
        List<Item> recommendedItems = new ArrayList<>();

        DatabaseCmd db = DatabaseCmd.getDB();

        // Step 1 Get all favorited itemIds
        Set<String> favoritedItemIds = db.getFavoriteItemIds(userId);

        // Step 2 Get all categories of favorited items
        Map<String, Integer> allCategories = new HashMap<>();
        for (String itemId : favoritedItemIds) {
            Set<String> categories = db.getCategories(itemId);
            for (String category : categories) {
                if (allCategories.containsKey(category)) {
                    allCategories.put(category, allCategories.get(category) + 1);
                } else {
                    allCategories.put(category, 1);
                }
            }
        }

        // sort categories by occurrence number
        List<Map.Entry<String, Integer>> categoryList = new ArrayList<>(allCategories.entrySet());
        categoryList.sort(
                (Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) ->
                 Integer.compare(o2.getValue(), o1.getValue())
        );

        // Step 3 Do search based on category, filter out favorited items, sort by
        // distance

        // different categories may find the same item
        Set<String> visitedItemIds = new HashSet<>();
        for (Map.Entry<String, Integer> entry : categoryList) {
            List<Item> items = db.searchItems(lat, lon, entry.getKey());
            List<Item> filteredItems = new ArrayList<>();
            for (Item item : items) {
                if (!visitedItemIds.contains(item.getItemId()) && !favoritedItemIds.contains(item.getItemId())) {
                    filteredItems.add(item);
                    visitedItemIds.add(item.getItemId());
                }
            }

            filteredItems.sort(
                    Comparator.comparing(Item :: getDistance)
            );

            // returned list: items ordered by categories firstly, then in each category, items ordered by distance
            recommendedItems.addAll(filteredItems);

        }

        return recommendedItems;

    }
}
