package skjinnero.com.recommendation.controllers;

import org.springframework.web.bind.annotation.*;
import skjinnero.com.recommendation.algorithm.GeoRecommendation;
import skjinnero.com.recommendation.entity.ReturnObj;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
public class RecommendController {

    @RequestMapping(value="/recommendation", method= RequestMethod.GET)
    public ReturnObj getRecommendation(@RequestParam("user_id") String userId,
                                  @RequestParam("lat") double lat,
                                  @RequestParam("lon") double lon) {
        ReturnObj res = new ReturnObj(new GeoRecommendation().recommendItems(userId, lat, lon));
        res.setResult("SUCCESS");
        return res;
    }
}
