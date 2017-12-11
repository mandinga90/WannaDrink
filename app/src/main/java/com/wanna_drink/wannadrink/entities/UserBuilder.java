package com.wanna_drink.wannadrink.entities;

/**
 * Created by redischool on 02.12.17.
 */

public class UserBuilder {

    private String hours;
    private String lat;
    private String lng;
    private String availableFrom;
    private String availableTill;
    private String name;
    private String email;
    private String distance;
    private Drink drink;
    private String uId;


    public User build(){
        User user = new User();

        Available available = new Available();
        available.setHours(this.hours);
        available.setLat(this.lat);
        available.setLng(this.lng);
        available.setAvailableFrom(this.availableFrom);
        available.setAvailableTill(this.availableTill);
        available.setCustom("");

        FavoriteDrinks favoriteDrinks = new FavoriteDrinks();
        favoriteDrinks.setName(this.drink.getName());
        favoriteDrinks.setId(String.valueOf(this.drink.getValue()));

        user.setFavoriteDrinks(new FavoriteDrinks[] {favoriteDrinks});

        user.setName(this.name);
        user.setEmail(this.email);
        user.setDistance("");

        user.setAvailable(available);

        user.setUId(uId);

        return user;
    }

    public UserBuilder addName(String name) {
        this.name = name;
        return this;
    }

    public UserBuilder addEmail(String email) {
        this.email = email+"";
        return this;
    }

    public UserBuilder addDrink(Drink drink) {
        this.drink = drink;
        return this;
    }

    public UserBuilder addHours(String hours) {
        this.hours = hours;
        return this;
    }

    public UserBuilder addLat(String lat) {
        this.lat = lat;
        return this;
    }

    public UserBuilder addLng(String lng) {
        this.lng = lng;
        return this;
    }

    public UserBuilder addUId(String uId) {
        this.uId = uId;
        return this;
    }

    public UserBuilder addDistance(String distance) {
        this.distance = distance;
        return this;
    }
    public UserBuilder addAvailableFrom(String availableFrom) {
        this.availableFrom = availableFrom;
        return this;
    }

    public UserBuilder addAvailableTill(String availableTill) {
        this.availableTill = availableTill;
        return this;
    }

}