package com.shouman.apps.hawk.application;

import android.app.Application;
import android.util.Log;

import com.facebook.FacebookSdk;
import com.google.firebase.database.FirebaseDatabase;
import com.shouman.apps.hawk.network.NetworkUtils;
import com.shouman.apps.hawk.preferences.UserPreference;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class HAWKApplication extends Application {
    private static final String TAG = "HAWKApplication";

    @Override
    public void onCreate() {
        super.onCreate();
        FacebookSdk.sdkInitialize(getApplicationContext());

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        database.setPersistenceEnabled(true);

        if (UserPreference.isFirstStart(getApplicationContext())) {
            database.setPersistenceCacheSizeBytes(31457280); // 30 megabytes
            UserPreference.setFirstStart(getApplicationContext());
        }
NetworkUtils.isConnectedToInternet(getApplicationContext());
//        FirebaseDatabase.getInstance().goOffline();
//        if (NetworkUtils.isConnectedToInternet(getApplicationContext())) {
//            Log.e(TAG, "onCreate: found internet connection");
//            FirebaseDatabase.getInstance().goOnline();
//        }

        Log.e(TAG, "onCreate: " + SimpleDateFormat.getDateInstance(DateFormat.MEDIUM, Locale.ENGLISH).format(new Date()));
    }
}
