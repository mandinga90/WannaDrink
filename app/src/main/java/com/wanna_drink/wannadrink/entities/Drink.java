package com.wanna_drink.wannadrink.entities;

import com.wanna_drink.wannadrink.R;

/**
 * Created by redischool on 02.12.17.
 */

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

    public String getName() {
        switch(ordinal()){
            case 0:     return "any";
            case 1:     return "anysoft";
            case 2:     return "anystrong";
            case 3:     return "beer";
            case 4:     return "cocktail";
            case 5:     return "wine";
            case 6:     return "tequila";
            case 7:     return "vodka";
            case 8:     return "whiskey";
            case 9:     return "jager";
            case 10:    return "hooka";
            case 11:    return "greentee";
            default:    return "no_drink_name_found";
        }
    }

    public static Drink getDrink(String drinkCode) {
        switch(drinkCode){
            case "0":     return ANY;
            case "1":     return ANYSOFT;
            case "2":     return ANYSTRONG;
            case "3":     return BEER;
            case "4":     return COCKTAIL;
            case "5":     return WINE;
            case "6":     return TEQUILA;
            case "7":     return VODKA;
            case "8":     return WHISKEY;
            case "9":     return JAGER;
            case "10":    return HOOKA;
            case "11":    return GREENTEE;
            default:      return ANY;
        }
    }
}
