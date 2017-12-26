package com.wanna_drink.wannadrink.firebase;

import android.location.Location;
import com.wanna_drink.wannadrink.entities.User;
import java.util.List;

public class dbTools {
    public static boolean saveUser(User user){
        return true;
    }

    public static User getUserByID(String ID){
        return new User();
    }

    public static List<User> getUsersNear(User user, int radius){
        return null;
    }
}
