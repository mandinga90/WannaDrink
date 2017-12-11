package com.wanna_drink.wannadrink.functional;

import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.wanna_drink.wannadrink.entities.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

/**
 * Created by Maxim on 12/9/2017.
 *
 */

public class App extends Application {
    public static FirebaseAuth mAuth;
    public static String uId;
    public static DatabaseReference fireDB;
    public static User mUser;
    public static List<Map> userList = new ArrayList<>();
    public static String fbToken;

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
        uId = mAuth.getUid();
        fireDB = FirebaseDatabase.getInstance().getReference();
        fbToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("Firebase App.OnCreate", "fbToken: " + fbToken);
        Log.d("Firebase App.OnCreate", "mAuth.getUid: " + mAuth.getUid());
        fireDB.child("tokens").child(uId).setValue(fbToken);
        fireDB.child("tokens").child(fbToken).setValue(uId);
    }

    public static void saveUsername(String name, boolean saveUsername) {
        SharedPreferences sharedPref = getDefaultSharedPreferences(getInstance());
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("name",name);
        editor.putString("email",name+"@gmail.com");
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