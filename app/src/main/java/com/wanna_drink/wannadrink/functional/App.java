package com.wanna_drink.wannadrink.functional;

import android.app.Application;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.github.bassaer.chatmessageview.views.ChatView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.wanna_drink.wannadrink.activities.ChatActivity;
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
    public static User mUser;
    public static List<Map> userList = new ArrayList<>();
    public static List<User> buddies = new ArrayList<>();
//For ChatActivity
    public static User talkBuddy;
    public static User previousTalkBuddy;
    public static String currentUserUId;

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

        //TODO: Check on a new device - how it behaves on offline phone, when it can't get Firebase Authentication
        //May be we should just hang a modal window to make user connect to internet

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