package skjinnero.com.recommendation.entity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Set;

//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;

public class Item {
    private String itemId;
    private String name;
    private double rating;
    private String address;
    private Set<String> categories;
    private String imageUrl;
    private String url;
    private double distance;
    private boolean favorite = false;

    public String getItemId() {
        return itemId;
    }
    public String getName() {
        return name;
    }
    public double getRating() {
        return rating;
    }
    public String getAddress() {
        return address;
    }
    public Set<String> getCategories() {
        return categories;
    }
    public String getImageUrl() {
        return imageUrl;
    }
    public String getUrl() {
        return url;
    }
    public double getDistance() {
        return distance;
    }

    public boolean getFavorite() {
        return this.favorite;
    }

    public void setFavorite() {
        this.favorite = true;
    }
    // singular pattern
    // can only use ItemBuilder instance.build() to get the Item instance once
    private Item(ItemBuilder builder) {
        this.itemId = builder.itemId;
        this.name = builder.name;
        this.rating = builder.rating;
        this.address = builder.address;
        this.categories = builder.categories;
        this.imageUrl = builder.imageUrl;
        this.url = builder.url;
        this.distance = builder.distance;
    }

    public Item(ItemEntity itemEntity, CategoryEntity categoryEntity) {
        this.itemId = itemEntity.getItem_id();
        this.name = itemEntity.getName();
        this.rating = itemEntity.getRating();
        this.address = itemEntity.getAddress();
        this.categories = categoryEntity.getCategory();
        this.imageUrl = itemEntity.getImage_url();
        this.url = itemEntity.getUrl();
        this.distance = itemEntity.getDistance();
    }

    // transfer Item to JSONObject
    public JSONObject toJSONObject() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("item_id", itemId);
            obj.put("name", name);
            obj.put("rating", rating);
            obj.put("address", address);
            obj.put("categories", new JSONArray(categories));
            obj.put("image_url", imageUrl);
            obj.put("url", url);
            obj.put("distance", distance);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj;
    }

    public static class ItemBuilder {
        private String itemId;
        private String name;
        private double rating;
        private String address;
        private Set<String> categories;
        private String imageUrl;
        private String url;
        private double distance;

        public void setItemId(String itemId) {
            this.itemId = itemId;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setRating(double rating) {
            this.rating = rating;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public void setCategories(Set<String> categories) {
            this.categories = categories;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public void setDistance(double distance) {
            this.distance = distance;
        }


        public Item build() {
            return new Item(this);
        }
    }



}
