package com.wanna_drink.wannadrink.functional;

import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.wanna_drink.wannadrink.R;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

/**
 * Created by Maxim on 12/9/2017.
 *
 */

public class App extends Application {
    public static FirebaseAuth mAuth;

    public static boolean isUserAvailable(){
        SharedPreferences sharedPref = getDefaultSharedPreferences(getInstance());
        return sharedPref.getBoolean("available",false);
    }

    private static App singleton;

    public static App getInstance(){
        return singleton;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        singleton = this;

        FirebaseApp.initializeApp(App.getInstance());
        mAuth = FirebaseAuth.getInstance();

        Log.d("Application", "App singlton created");
    }

    public static void saveUsername(String name, boolean saveUsername) {
        SharedPreferences sharedPref = getDefaultSharedPreferences(getInstance());
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("name",name);
        editor.putBoolean("saveUsername",saveUsername);
        editor.putBoolean("available",true);
        editor.commit();
    }

    public static String getUsername() {
        SharedPreferences sharedPref = getDefaultSharedPreferences(getInstance());
        return sharedPref.getString("name","Johny Walker");
    }

    public static boolean getSaveUsername() {
        SharedPreferences sharedPref = getDefaultSharedPreferences(getInstance());
        return sharedPref.getBoolean("saveUsername", false);
    }
}