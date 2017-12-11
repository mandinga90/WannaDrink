package com.wanna_drink.wannadrink.entities;

/**
 * Created by redischool on 03.12.17.
 */

public class UpdateUserData {
    private String Email, Lat, Lng, Hours, DrinkId, FbId, Distance;

    public UpdateUserData(String email, String lat, String lng, String hours, String drinkId, String fbId, String distance) {
        Email = email;
        Lat = lat;
        Lng = lng;
        Hours = hours;
        DrinkId = drinkId;
        FbId = fbId;
        Distance = distance;
    }
}
