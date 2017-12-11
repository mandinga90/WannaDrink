package com.wanna_drink.wannadrink.entities;

import com.google.android.gms.maps.model.LatLng;

import static com.wanna_drink.wannadrink.functional.SafeConversions.toDouble;
import static com.wanna_drink.wannadrink.functional.SafeConversions.toInt;

/**
 * Created by redischool on 02.12.17.
 */

public class User {
    private Available Available;

    private String Name;

    private String UId;

    private String Email;

    public String getDistance() {
        return Distance;
    }

    public void setDistance(String distance) {
        Distance = distance;
    }

    private String Distance;

    private FavoriteDrinks[] FavoriteDrinks;

    public Available getAvailable ()
    {
        return Available;
    }

    public void setAvailable (Available Available)
    {
        this.Available = Available;
    }

    public boolean isAvailableNow(){
        // to implement later
        return true;
    }

    public int getDrinkID(){
        return toInt(FavoriteDrinks[0].getId());
    }

    public String getDrinkName(){
        return FavoriteDrinks[0].getName();
    }

    public LatLng getLastLatLng() {
        return new LatLng( toDouble(Available.getLat()), toDouble(Available.getLng()));
    };

    public String getName ()
    {
        return Name;
    }

    public void setName (String Name)
    {
        this.Name = Name;
    }

    public String getUId() {
        return UId;
    }

    public void setUId(String uId) {
        this.UId = uId;
    }

    public String getEmail ()
    {
        return Email;
    }

    public void setEmail (String Email)
    {
        this.Email = Email;
    }

    public FavoriteDrinks[] getFavoriteDrinks ()
    {
        return FavoriteDrinks;
    }

    public void setFavoriteDrinks (FavoriteDrinks[] FavoriteDrinks)
    {
        this.FavoriteDrinks = FavoriteDrinks;
    }

    @Override
    public String toString()
    {
        return "User [Available = "+Available+", Name = "+Name+", Email = "+Email+", FavoriteDrinks = "+FavoriteDrinks+", UId = "+UId+"]";
    }
}
