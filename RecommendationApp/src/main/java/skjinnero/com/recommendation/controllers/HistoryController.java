package skjinnero.com.recommendation.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import skjinnero.com.recommendation.service.DatabaseService;
import skjinnero.com.recommendation.entity.Item;
import skjinnero.com.recommendation.entity.ReturnObj;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
public class HistoryController {

    @Autowired
    DatabaseService db;

    @RequestMapping(value="/history", method=RequestMethod.GET)
    public ReturnObj getHistory(@RequestParam("user_id") String userId) {
        List<Item> items = db.getFavoriteItems(userId);
        for (Item item : items) {
            item.setFavorite();
        }
        ReturnObj res = new ReturnObj(items);
        res.setResult("SUCCESS");
        return res;
    }

    @RequestMapping(value="/history", method=RequestMethod.POST)
    public ReturnObj postHistory(@RequestParam("user_id") String userId,
                            @RequestParam("favorite") String favorite) {
        ReturnObj res = new ReturnObj(null);
        try {
            List<String> itemId = new ArrayList<>();
            itemId.add(favorite);
            db.setFavoriteItems(userId, itemId);
            res.setResult("SUCCESS");

        } catch (Exception e) {
            e.printStackTrace();
            res.setResult("ERROR");
        }
        return res;
    }

    @RequestMapping(value="/history", method=RequestMethod.DELETE)
    public ReturnObj deleteHistory(@RequestParam("user_id") String userId,
                              @RequestParam("favorite") String favorite) {
        ReturnObj res = new ReturnObj(null);
        try {
            List<String> itemId = new ArrayList<>();
            itemId.add(favorite);
            db.unsetFavoriteItems(userId, itemId);
            res.setResult("SUCCESS");
        } catch (Exception e) {
            e.printStackTrace();
            res.setResult("ERROR");
        }
        return res;
    }

}
