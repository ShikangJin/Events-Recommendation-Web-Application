package skjinnero.com.recommendation.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
public class DBInitialController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @RequestMapping(
            value = "/createdb",
            method = RequestMethod.POST)
    public void process(@RequestBody Map<String, Object> payload) {
        if (payload.get("code").equals("secretCodeToCreateDB")) {
            String sql = "DROP TABLE IF EXISTS categories";
            jdbcTemplate.update(sql);
            sql = "DROP TABLE IF EXISTS history";
            jdbcTemplate.update(sql);
            sql = "DROP TABLE IF EXISTS items";
            jdbcTemplate.update(sql);
            sql = "DROP TABLE IF EXISTS users";
            jdbcTemplate.update(sql);

            sql = "CREATE TABLE items " + "(item_id VARCHAR(255) NOT NULL, " + " name VARCHAR(255), " + "rating FLOAT,"
                    + "address VARCHAR(255), " + "image_url VARCHAR(255), " + "url VARCHAR(255), " + "distance FLOAT, "
                    + " PRIMARY KEY ( item_id ))";
            jdbcTemplate.update(sql);

            sql = "CREATE TABLE categories " + "(item_id VARCHAR(255) NOT NULL, " + " category VARCHAR(255) NOT NULL, "
                    + " PRIMARY KEY ( item_id, category), " + "FOREIGN KEY (item_id) REFERENCES items(item_id))";
            jdbcTemplate.update(sql);

            sql = "CREATE TABLE users " + "(user_id VARCHAR(255) NOT NULL, " + " password VARCHAR(255) NOT NULL, "
                    + " first_name VARCHAR(255), last_name VARCHAR(255), " + " PRIMARY KEY ( user_id ))";
            jdbcTemplate.update(sql);

            sql = "CREATE TABLE history " + "(user_id VARCHAR(255) NOT NULL , " + " item_id VARCHAR(255) NOT NULL, "
                    + "last_favor_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP, " + " PRIMARY KEY (user_id, item_id),"
                    + "FOREIGN KEY (item_id) REFERENCES items(item_id),"
                    + "FOREIGN KEY (user_id) REFERENCES users(user_id))";
            jdbcTemplate.update(sql);

            sql = "INSERT INTO users VALUES (\"1111\", \"3229c1097c00d497a0fd282d586be050\", \"John\", \"Smith\")";
            jdbcTemplate.update(sql);
        }
    }
}
