package skjinnero.com.recommendation.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import skjinnero.com.recommendation.algorithm.GeoRecommendation;
import skjinnero.com.recommendation.entity.ReturnObj;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
public class RecommendController {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    GeoRecommendation geoRecommendation;

    @RequestMapping(value="/recommendation", method= RequestMethod.GET)
    public ReturnObj getRecommendation(@RequestParam("user_id") String userId,
                                  @RequestParam("lat") double lat,
                                  @RequestParam("lon") double lon) {
        ReturnObj res = new ReturnObj(geoRecommendation.recommendItems(userId, lat, lon));
        res.setResult("SUCCESS");
        return res;
    }
}
