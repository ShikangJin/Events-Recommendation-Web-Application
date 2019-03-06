package skjinnero.com.recommendation.external;

import org.springframework.stereotype.Service;
import skjinnero.com.recommendation.algorithm.GeoHash;
import skjinnero.com.recommendation.entity.Item;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.client.RestTemplate;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class TicketMasterAPI {
    private static final String URL = "https://app.ticketmaster.com/discovery/v2/events.json";
    private static final String DEFAULT_KEYWORD = ""; // no restriction
    private static final String API_KEY = "QfilLb9fIujP5jakAzASfvD1Sw9O1g6K";

    public List<Item> search(double lat, double lon, String keyword) {
        if (keyword == null) {
            keyword = DEFAULT_KEYWORD;
        }
        try {
            keyword= java.net.URLEncoder.encode(keyword, "UTF-8");
        } catch(Exception e) {
            e.printStackTrace();
        }

        String geoHash = GeoHash.encodeGeohash(lat, lon, 8);

        // query eg: "apikey=12345&geoPoint=abcd&keyword=music&radius=50"
        String query = String.format("apikey=%s&geoPoint=%s&keyword=%s&radius=%s", API_KEY, geoHash, keyword, 50);
        try {
            RestTemplate restTemplate = new RestTemplate();
            String response = restTemplate.getForObject(URL + "?" + query, String.class);
            System.out.println(response);
            JSONObject obj = new JSONObject(response);
            if (obj.isNull("_embedded")) {
                System.out.println("embedded is null");
                return new ArrayList();
            }
            JSONObject embedded = obj.getJSONObject("_embedded");
            JSONArray events = embedded.getJSONArray("events");
            return getItemList(events);
        } catch(Exception e) {
            System.out.println("TicketMasterAPI Error");
            e.printStackTrace();
        }
        return new ArrayList();
    }

    // Convert JSONArray to a list of item objects.
    private List<Item> getItemList(JSONArray events) throws JSONException {
        List<Item> itemList = new ArrayList<>();

        for (int i = 0; i < events.length(); ++i) {
            JSONObject event = events.getJSONObject(i);

            Item.ItemBuilder builder = new Item.ItemBuilder();

            if (!event.isNull("name")) {
                builder.setName(event.getString("name"));
            }
            if (!event.isNull("id")) {
                builder.setItemId(event.getString("id"));
            }
            if (!event.isNull("url")) {
                builder.setUrl(event.getString("url"));
            }
            if (!event.isNull("rating")) {
                builder.setRating(event.getDouble("rating"));
            }
            if (!event.isNull("distance")) {
                builder.setDistance(event.getDouble("distance"));
            }

            builder.setCategories(getCategories(event));
            builder.setAddress(getAddress(event));
            builder.setImageUrl(getImageUrl(event));

            itemList.add(builder.build());
        }

        return itemList;
    }

    // iterate venues, find the first item that has address, return it
    private String getAddress(JSONObject event) throws JSONException {
        if (!event.isNull("_embedded")) {
            JSONObject embedded = event.getJSONObject("_embedded");

            if (!embedded.isNull("venues")) {
                JSONArray venues = embedded.getJSONArray("venues");
                for (int i = 0; i < venues.length(); ++i) {
                    JSONObject venue = venues.getJSONObject(i);

                    StringBuilder sb = new StringBuilder();

                    if (!venue.isNull("address")) {
                        JSONObject address = venue.getJSONObject("address");
                        if (!address.isNull("line1")) {
                            sb.append(address.getString("line1"));
                        }
                        if (!address.isNull("line2")) {
                            sb.append(address.getString("line2"));
                        }
                        if (!address.isNull("line3")) {
                            sb.append(address.getString("line3"));
                        }
                        sb.append(",");
                    }

                    if (!venue.isNull("city")) {
                        JSONObject city = venue.getJSONObject("city");
                        if (!city.isNull("name")) {
                            sb.append(city.getString("name"));
                        }
                    }

                    if (!sb.toString().equals("")) {
                        return sb.toString();
                    }
                }
            }
        }
        return "";
    }

    // Iterate images tag, find the first valid url, return it
    private String getImageUrl(JSONObject event) throws JSONException {
        if (!event.isNull("images")) {
            JSONArray array = event.getJSONArray("images");
            for (int i = 0; i < array.length(); i++) {
                JSONObject image = array.getJSONObject(i);
                if (!image.isNull("url")) {
                    return image.getString("url");
                }
            }
        }
        return "";
    }

    // iterate classifications tag, find segment tag, find valid name in segment
    private Set<String> getCategories(JSONObject event) throws JSONException {
        Set<String> categories = new HashSet<>();
        if (!event.isNull("classifications")) {
            JSONArray classifications = event.getJSONArray("classifications");
            for (int i = 0; i < classifications.length(); i++) {
                JSONObject classification = classifications.getJSONObject(i);
                if (!classification.isNull("segment")) {
                    JSONObject segment = classification.getJSONObject("segment");
                    if (!segment.isNull("name")) {
                        String name = segment.getString("name");
                        categories.add(name);
                    }
                }
            }
        }
        return categories;
    }

}
