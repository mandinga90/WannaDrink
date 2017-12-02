package com.wanna_drink.wannadrink.entities;

/**
 * Created by redischool on 02.12.17.
 */

public class FavoriteDrinks {
    private String Name;

    private String Id;

    public String getName ()
    {
        return Name;
    }

    public void setName (String Name)
    {
        this.Name = Name;
    }

    public String getId ()
    {
        return Id;
    }

    public void setId (String Id)
    {
        this.Id = Id;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [Name = "+Name+", Id = "+Id+"]";
    }
}
