package skjinnero.com.recommendation.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import skjinnero.com.recommendation.database.DatabaseCmd;
import skjinnero.com.recommendation.entity.Item;
import skjinnero.com.recommendation.entity.ReturnObj;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
public class HistoryController {

    @Autowired
    DatabaseCmd db;

    @RequestMapping(value="/history", method=RequestMethod.GET)
    public ReturnObj getHistory(@RequestParam("user_id") String userId) {
        Set<Item> items = db.getFavoriteItems(userId);
        ReturnObj res = new ReturnObj(new ArrayList<>(items));
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
