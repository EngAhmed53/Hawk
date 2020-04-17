package com.shouman.apps.hawk.preferences;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

public class UserPreference {

    private static final String USER_CUID = "company_uid";
    private static final String USER_BUID = "sales_member_branch_uid";
    private static final String USER_COMPANY_NAME = "company_name";
    private static final String USER_BRANCH_NAME = "branch_name";
    private static final String FIRST_START = "first_start";


    public static void setBranchUID(Context context, String branchUID) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putString(USER_BUID, branchUID).apply();
    }

    public static String getBranchUID(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(USER_BUID, "null");
    }

    public static void setFirstStart(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putBoolean(FIRST_START, false).apply();
    }

    public static boolean isFirstStart(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean(FIRST_START, true);
    }


    public static void setCompanyUID(Context context, String CUID) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putString(USER_CUID, CUID).apply();
    }

    public static String getCompanyUID(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(USER_CUID, "null");
    }


    public static void setCompanyName(Context context, String cName) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putString(USER_COMPANY_NAME, cName).apply();
    }

    public static String getUserCompanyName(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(USER_COMPANY_NAME, "null");
    }

    public static void setBranchName(Context context, String branchName) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putString(USER_BRANCH_NAME, branchName).apply();
    }

    public static String getBranchName(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(USER_BRANCH_NAME, "null");
    }

    public static void clearAllPreference(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().clear().apply();
    }
}
