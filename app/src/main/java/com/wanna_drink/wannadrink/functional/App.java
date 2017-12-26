package com.wanna_drink.wannadrink.functional;

import android.app.Application;
import android.graphics.drawable.Drawable;

import com.wanna_drink.wannadrink.entities.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.wanna_drink.wannadrink.R.drawable.any;
import static com.wanna_drink.wannadrink.R.drawable.anysoft;
import static com.wanna_drink.wannadrink.R.drawable.anystrong;
import static com.wanna_drink.wannadrink.R.drawable.beer;
import static com.wanna_drink.wannadrink.R.drawable.cocktail;
import static com.wanna_drink.wannadrink.R.drawable.greentee;
import static com.wanna_drink.wannadrink.R.drawable.hooka;
import static com.wanna_drink.wannadrink.R.drawable.jager;
import static com.wanna_drink.wannadrink.R.drawable.tequila;
import static com.wanna_drink.wannadrink.R.drawable.vodka;
import static com.wanna_drink.wannadrink.R.drawable.whiskey;
import static com.wanna_drink.wannadrink.R.drawable.wine;


public class App extends Application {
    public static User mUser;
    public static String currentUserId;
    public static Map<String, User> buddyList = new HashMap<>();
    public static Drawable[] mDrinkImages;

    //For ChatActivity
    public static User chatBuddy;
    public static User lastChatBuddy;

    private static App singleton;

    public static App getInstance() {
        return singleton;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        singleton = this;
        mDrinkImages = new Drawable[]{
                getResources().getDrawable(any),
                getResources().getDrawable(anysoft),
                getResources().getDrawable(anystrong),
                getResources().getDrawable(beer),
                getResources().getDrawable(cocktail),
                getResources().getDrawable(wine),
                getResources().getDrawable(tequila),
                getResources().getDrawable(vodka),
                getResources().getDrawable(whiskey),
                getResources().getDrawable(jager),
                getResources().getDrawable(hooka),
                getResources().getDrawable(greentee)};
    }
}