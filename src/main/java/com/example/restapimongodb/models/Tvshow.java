package com.example.restapimongodb.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("tvshows")
public class Tvshow {

    @Id
    private String _id;
    private String title;
    private String description;
    private String category;
    private int rent;
    private int buy;
    private int rating;
    private String smallPoster;
    private String largePoster;

    public Tvshow() {
    }

    public Tvshow(String title, String description, String category, int rent, int buy, int rating, String smallPoster, String largePoster) {
        this.title = title;
        this.description = description;
        this.category = category;
        this.rent = rent;
        this.buy = buy;
        this.rating = rating;
        this.smallPoster = smallPoster;
        this.largePoster = largePoster;
    }

    public Tvshow(String _id, String title, String description, String category, int rent, int buy, int rating, String smallPoster, String largePoster) {
        this._id = _id;
        this.title = title;
        this.description = description;
        this.category = category;
        this.rent = rent;
        this.buy = buy;
        this.rating = rating;
        this.smallPoster = smallPoster;
        this.largePoster = largePoster;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getRent() {
        return rent;
    }

    public void setRent(int rent) {
        this.rent = rent;
    }

    public int getBuy() {
        return buy;
    }

    public void setBuy(int buy) {
        this.buy = buy;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getSmallPoster() {
        return smallPoster;
    }

    public void setSmallPoster(String smallPoster) {
        this.smallPoster = smallPoster;
    }

    public String getLargePoster() {
        return largePoster;
    }

    public void setLargePoster(String largePoster) {
        this.largePoster = largePoster;
    }
}
