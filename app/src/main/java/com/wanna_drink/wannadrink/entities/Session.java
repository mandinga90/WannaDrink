package com.wanna_drink.wannadrink.entities;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.ServerValue;

public class Session {
    private String name;
    private int drinkId;
    private double lat;
    private double lng;
    private Object timestamp;
    private int duration;

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
        this.timestamp = ServerValue.TIMESTAMP;
        this.duration = duration;
    }

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

    public Object getTimestamp() { return timestamp; }

    @Exclude //Prevents from serialization
    public Long getTimestampLong() { return (Long)timestamp; }

    public void setTimestamp(Object timestamp) {
        this.timestamp = timestamp;
    }

    public int getDuration() { return duration; }

    @Override
    public String toString() {
        return "Session{" +
                "name='" + name + '\'' +
                ", drinkId=" + drinkId +
                ", lat=" + lat +
                ", lng=" + lng +
                ", timestamp=" + timestamp +
                ", duration=" + duration +
                '}';
    }
}
