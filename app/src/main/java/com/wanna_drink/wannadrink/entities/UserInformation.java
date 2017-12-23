package com.wanna_drink.wannadrink.entities;

public class UserInformation {
    private double lat, lon;
    private String email;
    private String fbId;
    private int radius;

    public UserInformation(double lat, double lon, String email, String fbId, int radius) {
        this.lat = lat;
        this.lon = lon;
        this.email = email;
        this.fbId = fbId;
        this.radius = radius;
    }
}
