package com.shouman.apps.hawk.application;

import android.app.Application;
import android.util.Log;

import com.facebook.FacebookSdk;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.shouman.apps.hawk.preferences.UserPreference;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class HAWKApplication extends Application {
    private static final String TAG = "HAWKApplication";

    @Override
    public void onCreate() {
        super.onCreate();

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            FacebookSdk.sdkInitialize(getApplicationContext());
        }

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        database.setPersistenceEnabled(true);

        if (UserPreference.isFirstStart(getApplicationContext())) {
            database.setPersistenceCacheSizeBytes(31457280); // 30 megabytes
            UserPreference.setFirstStart(getApplicationContext());
            Log.e(TAG, "onCreate: first time setting app ");
        }
        Log.e(TAG, "onCreate: " + SimpleDateFormat.getDateInstance(DateFormat.MEDIUM, Locale.ENGLISH).format(new Date()));

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        Date date = calendar.getTime();
        Log.e(TAG, "onCreate: " + date.getTime());
        Log.e(TAG, "onCreate: " + SimpleDateFormat.getDateInstance(DateFormat.MEDIUM, Locale.ENGLISH).format(date));

    }
}
