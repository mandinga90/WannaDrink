package com.wanna_drink.wannadrink.entities;

/**
 * Created by redischool on 02.12.17.
 */

public class Available {
    private String lng;

    private String hours;

    private String lat;

    private String custom;

    public String getLng ()
    {
        return lng;
    }

    public void setLng (String Lng)
    {
        this.lng = Lng;
    }

    public String getHours ()
    {
        return hours;
    }

    public void setHours (String hours)
    {
        this.hours = hours;
    }

    public String getLat ()
    {
        return lat;
    }

    public void setLat (String lat)
    {
        this.lat = lat;
    }

    public String getCustom ()
    {
        return custom;
    }

    public void setCustom (String custom)
    {
        this.custom = custom;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [lng = "+ lng +", hours = "+ hours +", lat = "+ lat +", custom = "+ custom +"]";
    }
}
