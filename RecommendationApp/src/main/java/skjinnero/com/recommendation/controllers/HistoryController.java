package skjinnero.com.recommendation.controllers;

import skjinnero.com.recommendation.database.DatabaseCmd;
import skjinnero.com.recommendation.entity.Item;
import skjinnero.com.recommendation.entity.ReturnObj;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RestController
public class HistoryController {

    @RequestMapping(value="/history", method=RequestMethod.GET)
    public ReturnObj getHistory(@RequestParam("user_id") String userId) {
        DatabaseCmd db = DatabaseCmd.getDB();
        Set<Item> items = db.getFavoriteItems(userId);
        return new ReturnObj(new ArrayList<>(items));
    }

    @RequestMapping(value="/history", method=RequestMethod.POST)
    public String postHistory(@RequestParam("user_id") String userId,
                            @RequestParam("favorite") String favorite) {
        try {
            List<String> itemId = new ArrayList<>();
            itemId.add(favorite);
            DatabaseCmd.getDB().setFavoriteItems(userId, itemId);
            return "result: SUCCESS";
        } catch (Exception e) {
            e.printStackTrace();
            return "result: ERROR";
        }
    }

    @RequestMapping(value="/history", method=RequestMethod.DELETE)
    public String deleteHistory(@RequestParam("user_id") String userId,
                              @RequestParam("favorite") String favorite) {
        try {
            List<String> itemId = new ArrayList<>();
            itemId.add(favorite);
            DatabaseCmd.getDB().unsetFavoriteItems(userId, itemId);
            return "result: SUCCESS";
        } catch (Exception e) {
            e.printStackTrace();
            return "result: Error";
        }
    }

}
