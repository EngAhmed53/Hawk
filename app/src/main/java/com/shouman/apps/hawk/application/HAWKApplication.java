package com.shouman.apps.hawk.application;

import android.app.Application;
import android.util.Log;

import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

public class HAWKApplication extends Application {
    private static final String TAG = "HAWKApplication";
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        database.setPersistenceEnabled(true);
        Log.e(TAG, "onCreate: " + SimpleDateFormat.getDateInstance().format(new Date()) );
    }
}
