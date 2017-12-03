package com.wanna_drink.wannadrink.entities;

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
