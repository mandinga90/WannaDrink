package com.wanna_drink.wannadrink.entities;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.wanna_drink.wannadrink.functional.SafeConversions;

import static com.wanna_drink.wannadrink.functional.SafeConversions.toDouble;
import static com.wanna_drink.wannadrink.functional.SafeConversions.toInt;

/**
 * Created by redischool on 02.12.17.
 */

public class User {
    private Available Available;

    private String Name;

    private String Email;

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
        return "User [Available = "+Available+", Name = "+Name+", Email = "+Email+", FavoriteDrinks = "+FavoriteDrinks+"]";
    }
}
