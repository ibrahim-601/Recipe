package com.iriad11.recipes.internet;

import android.content.Context;
import android.net.ConnectivityManager;
import android.support.annotation.NonNull;

public class Internet {

    /** CHECK WHETHER INTERNET CONNECTION IS AVAILABLE OR NOT */
    public static boolean checkConnection(@NonNull Context context) {
        return  ((ConnectivityManager) context.getSystemService
                (Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null;
    }
}