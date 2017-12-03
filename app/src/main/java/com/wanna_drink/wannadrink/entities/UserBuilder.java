package com.wanna_drink.wannadrink.entities;

/**
 * Created by redischool on 02.12.17.
 */

public class UserBuilder {

    private String hours;
    private String lat;
    private String lng;
    private String name;
    private String email;
    private Drink drink;

    public User build(){
        User user = new User();

        Available available = new Available();
        available.setHours(this.hours);
        available.setLat(this.lat);
        available.setLng(this.lng);
        available.setCustom("");

        FavoriteDrinks favoriteDrinks = new FavoriteDrinks();
        favoriteDrinks.setName(this.drink.getName());
        favoriteDrinks.setId(String.valueOf(this.drink.getValue()));

        user.setFavoriteDrinks(new FavoriteDrinks[] {favoriteDrinks});

        user.setName(this.name);
        user.setEmail(this.email);

        user.setAvailable(available);

        return user;
    }

    public UserBuilder addName(String name) {
        this.name = name;
        return this;
    }

    public UserBuilder addEmail(String email) {
        this.email = email;
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
}
