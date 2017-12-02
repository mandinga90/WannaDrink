package com.wanna_drink.wannadrink.entities;

/**
 * Created by redischool on 02.12.17.
 */

public class Available {
    private String Lng;

    private String Hours;

    private String Lat;

    private String Custom;

    public String getLng ()
    {
        return Lng;
    }

    public void setLng (String Lng)
    {
        this.Lng = Lng;
    }

    public String getHours ()
    {
        return Hours;
    }

    public void setHours (String Hours)
    {
        this.Hours = Hours;
    }

    public String getLat ()
    {
        return Lat;
    }

    public void setLat (String Lat)
    {
        this.Lat = Lat;
    }

    public String getCustom ()
    {
        return Custom;
    }

    public void setCustom (String Custom)
    {
        this.Custom = Custom;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [Lng = "+Lng+", Hours = "+Hours+", Lat = "+Lat+", Custom = "+Custom+"]";
    }
}
