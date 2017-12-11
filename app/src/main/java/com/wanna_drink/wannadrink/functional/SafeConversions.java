package com.wanna_drink.wannadrink.functional;

import android.util.Log;

/**
 * Created by Maxim on 12/9/2017.
 * just some short and safe static conversion functions
 */

public class SafeConversions {

    public static int toInt(String str){
        int i = -1;
        try {
            i = Integer.parseInt(str);
        }
        catch (Exception e) {
            Log.e ("Conversion", "Failed to convert String to int.");
        }
        return i;
    }

    public static int toInt(Object object){
        int i = -1;
        try {
            i = Integer.parseInt(object.toString());
        }
        catch (Exception e) {
            Log.e ("Conversion", "Failed to convert Object to int.");
        }
        return i;
    }

    public static double toDouble(String str){
        double d = -1.0;
        try {
            d = Double.parseDouble(str);
        }
        catch (Exception e) {
            Log.e ("Conversion", "Failed to convert String to double.");
        }
        return d;
    }

    public static double toDouble(Object object){
        double d = -1.0;
        try {
            d = Double.parseDouble(object.toString());
        }
        catch (Exception e) {
            Log.e ("Conversion", "Failed to convert Object to double.");
        }
        return d;
    }

    public static String toStr(int i){
        return Integer.toString(i);
    }


}
