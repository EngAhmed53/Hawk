package com.shouman.apps.hawk.application;

import android.app.Application;

import com.facebook.FacebookSdk;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.shouman.apps.hawk.preferences.UserPreference;

public class HAWKApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        UserPreference userPreference = UserPreference.getInstance();

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            FacebookSdk.sdkInitialize(getApplicationContext());
        }

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        database.setPersistenceEnabled(true);

        if (userPreference.isFirstStart(getApplicationContext())) {
            database.setPersistenceCacheSizeBytes(31457280); // 30 megabytes
            userPreference.setFirstStart(getApplicationContext());
        }
    }
}
