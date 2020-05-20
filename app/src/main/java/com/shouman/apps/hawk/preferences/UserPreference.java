package com.shouman.apps.hawk.preferences;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

public class UserPreference {

    private enum SavedInfo {
        USER_CUID("company_uid"),
        USER_BUID("sales_member_branch_uid"),
        USER_COMPANY_NAME("company_name"),
        USER_BRANCH_NAME("branch_name"),
        FIRST_START("first_start"),
        SALESMAN_STATUS("salesman_status"),
        USER_NAME("user_name");

        private String value;

        SavedInfo(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    private static final Object LOCK = new Object();
    private static UserPreference mInstance;

    public static UserPreference getInstance() {
        if (mInstance == null) {
            synchronized (LOCK) {
                mInstance = new UserPreference();
            }
        }
        return mInstance;
    }

    private UserPreference() {
    }

    public void setBranchUID(Context context, String branchUID) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putString(SavedInfo.USER_BUID.getValue(), branchUID).apply();
    }

    public String getBranchUID(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(SavedInfo.USER_BUID.getValue(), null);
    }

    public void setFirstStart(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putBoolean(SavedInfo.FIRST_START.getValue(), false).apply();
    }

    public boolean isFirstStart(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean(SavedInfo.FIRST_START.getValue(), true);
    }


    public void setCompanyUID(Context context, String CUID) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putString(SavedInfo.USER_CUID.getValue(), CUID).apply();
    }

    public String getCompanyUID(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(SavedInfo.USER_CUID.getValue(), null);
    }


    public void setCompanyName(Context context, String cName) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putString(SavedInfo.USER_COMPANY_NAME.getValue(), cName).apply();
    }

    public String getUserCompanyName(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(SavedInfo.USER_COMPANY_NAME.getValue(), null);
    }

    public void setBranchName(Context context, String branchName) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putString(SavedInfo.USER_BRANCH_NAME.getValue(), branchName).apply();
    }

    public String getBranchName(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(SavedInfo.USER_BRANCH_NAME.getValue(), null);
    }

    public void setSalesmanStatus(Context context, boolean status) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putBoolean(SavedInfo.SALESMAN_STATUS.getValue(), status).apply();
    }

    public boolean getSalesmanStatus(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean(SavedInfo.SALESMAN_STATUS.getValue(), true);

    }

    public void clearAllPreference(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().clear().apply();
    }

    public void setUserName(Context context, String userName) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putString(SavedInfo.USER_NAME.getValue(), userName).apply();
    }

    public String getUserName(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(SavedInfo.USER_NAME.getValue(), null);
    }
}
