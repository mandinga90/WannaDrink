package com.wanna_drink.wannadrink.entities;

/**
 * Created by redischool on 03.12.17.
 */

public class UserInformation {
    private double lat, lon;
    private String email;
    private String fbId;
    private int radius;

//    "lat": 52.520007,
//    "lon": 13.404954,
//            "email" : email@serv.com
//            "fbId": "sdfgsdfgsdfgsfdgsdfgsdfgsdfgsdfgsdfg",
//            "radius":5000

    public UserInformation(double lat, double lon, String email, String fbId, int radius) {
        this.lat = lat;
        this.lon = lon;
        this.email = email;
        this.fbId = fbId;
        this.radius = radius;
    }
}
