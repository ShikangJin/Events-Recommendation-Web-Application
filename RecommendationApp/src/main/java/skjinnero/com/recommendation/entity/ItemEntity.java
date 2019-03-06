package skjinnero.com.recommendation.entity;

import javax.persistence.*;

@Entity
@Table(name="items")
public class ItemEntity {

    @Id
    @GeneratedValue
    @Column(name="item_id")
    private String item_id;

    @Column(name="name")
    private String name;

    @Column(name="rating")
    private double rating;

    @Column(name="address")
    private String address;

    @Column(name="image_url")
    private String image_url;

    @Column(name="url")
    private String url;

    @Column(name="distance")
    private double distance;

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }
}
