package com.wanna_drink.wannadrink.entities;

import com.wanna_drink.wannadrink.R;

public enum Drink {
    ANY,
    ANYSOFT,
    ANYSTRONG,
    BEER,
    COCKTAIL,
    WINE,
    TEQUILA,
    VODKA,
    WHISKEY,
    JAGER,
    HOOKA,
    GREENTEE
    ;

    public int getValue() {
        return this.ordinal();
    }

    public static int getImage(int drinkCode) {
        switch(drinkCode){
            case 0:     return R.drawable.any;
            case 1:     return R.drawable.anysoft;
            case 2:     return R.drawable.anystrong;
            case 3:     return R.drawable.beer;
            case 4:     return R.drawable.cocktail;
            case 5:     return R.drawable.wine;
            case 6:     return R.drawable.tequila;
            case 7:     return R.drawable.vodka;
            case 8:     return R.drawable.whiskey;
            case 9:     return R.drawable.jager;
            case 10:    return R.drawable.hooka;
            case 11:    return R.drawable.greentee;
            default:    return 0;
        }
    }
}
