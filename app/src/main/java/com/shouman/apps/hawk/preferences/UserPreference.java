package com.shouman.apps.hawk.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.preference.PreferenceManager;

public class UserPreference {

    public static final String IS_USER_TYPE_SELECTED = "is_user_type_selected";
    public static final String IS_USER_INFO_TYPED = "is_user_info_typed";
    public static final String USER_TYPE = "user_type";
    public static final String USER_EMAIL = "user_email";
    public static final String USER_MAP_UID = "user_uid";

    public static String getUserType(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(USER_TYPE, "not_defined");
    }

    public static void setUserType(Context context,int type ) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String userType = "not_defined";
        if (type == 0) {
            userType = "company";
        } else if (type == 1){
            userType = "sales_member";
        }
         preferences.edit().putString(USER_TYPE, userType).apply();
    }

//    public static boolean isUserTypeSelected(Context context) {
//        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
//        return preferences.getBoolean(IS_USER_TYPE_SELECTED, false);
//    }

    public static void setUserEmail(Context context, String userEmail) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            preferences.edit().putString(USER_EMAIL, userEmail).apply();
    }

    public static String getUserEmail(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(USER_EMAIL, "null");
    }

    public static boolean isUserInfoSetted(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean(IS_USER_INFO_TYPED, false);
    }

    public static void setUserTypeTrue(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putBoolean(IS_USER_TYPE_SELECTED, true).apply();
    }

    public static void setUserInfoSettedTrue(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putBoolean(IS_USER_INFO_TYPED, true).apply();
    }


    public static void setUserUID(Context context, String UID) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putString(USER_MAP_UID, UID).apply();
    }

    public static String getUserUID(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(USER_MAP_UID, "null");
    }

}