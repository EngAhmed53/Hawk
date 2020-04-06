package com.shouman.apps.hawk.application;

import android.app.Application;
import android.util.Log;

import com.facebook.FacebookSdk;
import com.google.firebase.database.FirebaseDatabase;

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
        Log.e(TAG, "onCreate: " + SimpleDateFormat.getDateInstance(DateFormat.MEDIUM, Locale.ENGLISH).format(new Date()));
    }
}
