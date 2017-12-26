package com.wanna_drink.wannadrink.entities;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.ServerValue;

public class Session {
    private String name;
    private int drinkId;
    private double lat;
    private double lng;
    private Object fromTimestamp;
    private int duration;

    public String getName() {
        return name;
    }
    public int getDrinkId() {
        return drinkId;
    }

    public double getLat() {
        return lat;
    }
    public double getLng() {
        return lng;
    }

    public Object getFrom() { return fromTimestamp; }
    @Exclude //Prevents from serialization
    public Long getFromLong() { return (Long)fromTimestamp; }
    public int getDuration() { return duration; }

    public Session() {}

    /**
     * @param name - name for this session
     * @param drinkId - id of the selected drink
     * @param lat lattitude
     * @param lng longitude
     * @param duration (in minutes)
     */
    public Session(String name, int drinkId, double lat, double lng, int duration) {
        this.name = name;
        this.drinkId = drinkId;
        this.lat = lat;
        this.lng = lng;
        this.fromTimestamp = ServerValue.TIMESTAMP;
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "Session{" +
                " name=" + name +
                ", drinkId=" + drinkId +
                ", lat=" + lat +
                ", lng=" + lng +
                ", fromTimestamp=" + fromTimestamp +
                ", duration=" + duration +
                '}';
    }
}
