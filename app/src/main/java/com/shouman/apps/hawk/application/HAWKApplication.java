package com.shouman.apps.hawk.application;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

public class HAWKApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        database.setPersistenceEnabled(true);
    }
}
