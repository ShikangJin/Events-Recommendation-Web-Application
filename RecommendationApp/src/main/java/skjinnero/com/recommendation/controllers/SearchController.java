package skjinnero.com.recommendation.controllers;

import skjinnero.com.recommendation.database.DatabaseCmd;
import skjinnero.com.recommendation.entity.Item;
import skjinnero.com.recommendation.entity.ReturnObj;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@RestController
public class SearchController {

    @RequestMapping(value="/search", method= RequestMethod.GET)
    public ReturnObj search(@RequestParam("user_id") String userId,
                  @RequestParam("lat") double lat,
                  @RequestParam("lon") double lon,
                  @RequestParam("term") String keyword) {
        DatabaseCmd db = DatabaseCmd.getDB();
        Set<String> favorite = db.getFavoriteItemIds(userId);
        List<Item> items = db.searchItems(lat, lon, keyword);
        try {
            for (Item item : items) {
                if (favorite.contains(item.getItemId())) {
                    item.setFavorite();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        ReturnObj res = new ReturnObj(items);
        System.out.println(items.size() + " Item found");
        return res;
    }
}

