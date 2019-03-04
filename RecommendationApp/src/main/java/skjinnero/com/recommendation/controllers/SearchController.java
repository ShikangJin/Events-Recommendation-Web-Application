package skjinnero.com.recommendation.controllers;

import org.springframework.web.bind.annotation.*;
import skjinnero.com.recommendation.database.DatabaseCmd;
import skjinnero.com.recommendation.entity.Item;
import skjinnero.com.recommendation.entity.ReturnObj;

import java.util.List;
import java.util.Set;

@CrossOrigin(origins = "http://localhost:8080")
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
        ReturnObj res;
        try {
            for (Item item : items) {
                if (favorite.contains(item.getItemId())) {
                    item.setFavorite();
                }
            }
            res = new ReturnObj(items);
            res.setResult("SUCCESS");
            System.out.println(items.size() + " Item found");
        } catch (Exception e) {
            res = new ReturnObj(items);
            res.setResult("ERROR");
            e.printStackTrace();
        }
        return res;
    }
}

