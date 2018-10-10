package com.example.standard.firebasestructure;

import java.util.concurrent.TimeUnit;

public class Utils {

    public static String calculateTimeSince(Long timeMillis){
        long minutes = TimeUnit.MILLISECONDS.toMinutes(System.currentTimeMillis() - timeMillis);
        if(minutes < 60){
            return minutes + "m";
        } else {
            double d = Math.floor(minutes/ 60);
            Long l = (long) d;
            return l + "h";
        }
    }

}
