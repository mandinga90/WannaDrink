package com.wanna_drink.wannadrink.entities;

import android.location.Location;

import com.google.android.gms.maps.model.Marker;
import com.google.firebase.database.Exclude;


public class User {
    private String id;
    private String email;
    private Session session;

    @Exclude
    private Marker marker;

    public User() {}

    public User(String id, String email, Session session) {
        this.id = id;
        this.email = email;
        this.session = session;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public Session getSession() {
        return session;
    }
    public void setSession(Session session) {
        this.session = session;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", email='" + email + '\'' +
                ", session=" + session +
                '}';
    }

    @Exclude
    public String getCurrentName(){ return session.getName();}
    @Exclude
    public int getCurrentDrinkId(){ return session.getDrinkId();}
    @Exclude
    public Location getCurrentLocation(){
        Location location = new Location("");
        location.setLatitude(session.getLat());
        location.setLongitude(session.getLng());
        return location; }
    @Exclude
    public double getCurrentLat(){ return session.getLat();}
    @Exclude
    public double getCurrentLng(){ return session.getLng();}

    @Exclude
    public Marker getMarker() { return marker; }

    @Exclude
    public void setMarker(Marker marker) { this.marker = marker; }

    @Exclude
    public boolean isInDrinkMode(){
        Long until = session.getTimestampLong() + (session.getDuration() * 60 * 1000);
        return System.currentTimeMillis() < until;
    }

    /**
     * Within last 24H
     * @return
     */
    @Exclude
    public boolean isVisible(){
        Long until = session.getTimestampLong() + (24 * 60 * 60 * 1000);
        return System.currentTimeMillis() < until;
    }

}
