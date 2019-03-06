package skjinnero.com.recommendation.entity;

import java.util.Set;

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
        return favorite;
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
