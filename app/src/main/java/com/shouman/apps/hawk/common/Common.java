package com.shouman.apps.hawk.common;

import android.content.Context;

import com.shouman.apps.hawk.R;
import com.shouman.apps.hawk.model.UserMap;


public class Common {
    private static final String TAG = "Common class";
    public static int MANAGER_POSITION = 0;
    public static int SALES_POSITION = 1;

    public static UserMap userMap;

    public static String[] getAllPositions(Context context) {
        return context.getResources().getStringArray(R.array.positions_array);
    }

    //convert the user email to UID to put it as key in UserMap database
    public static String EmailToUID(String email) {
        if (email != null) {
            StringBuilder stringBuilder = new StringBuilder();
            for (char c : email.toCharArray()) {

                if (Character.isLetterOrDigit(c)) {
                    stringBuilder.append(c);
                }
            }
            return stringBuilder.toString();
        }
        return null;
    }
}
