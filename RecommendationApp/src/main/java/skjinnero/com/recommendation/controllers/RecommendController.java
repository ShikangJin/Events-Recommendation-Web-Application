package skjinnero.com.recommendation.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import skjinnero.com.recommendation.algorithm.GeoRecommendation;
import skjinnero.com.recommendation.entity.ReturnObj;


@RestController
public class RecommendController {

    @RequestMapping(value="/recommendation", method= RequestMethod.GET)
    public ReturnObj getRecommendation(@RequestParam("user_id") String userId,
                                  @RequestParam("lat") double lat,
                                  @RequestParam("lon") double lon) {
        return new ReturnObj(new GeoRecommendation().recommendItems(userId, lat, lon));
    }
}
