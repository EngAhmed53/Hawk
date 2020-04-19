package com.shouman.apps.hawk.common;

import android.content.Context;

import com.shouman.apps.hawk.R;

import java.util.Calendar;
import java.util.Date;


public class Common {
    private static final String TAG = "Common class";
    public static int MANAGER_POSITION = 0;
    public static int SALES_POSITION = 1;

    //last visible fragment in companyMainActivity;
    public static String LAST_ACTIVE_FRAGMENT = "";

    public static String[] getAllPositions(Context context) {
        return context.getResources().getStringArray(R.array.positions_array);
    }

    //convert the user email to UID to put it as key in User database
    public static String EmailToUID(String email) {
        StringBuilder stringBuilder = new StringBuilder();
        for (char c : email.toCharArray()) {

            if (c != '@' && c != '.') {
                stringBuilder.append(c);
            }
        }
        return stringBuilder.toString();
    }

    public static String getRandomColor(int position) {
        String[] colors;
        colors = new String[]{"#4267B2", "#AF3782", "#73B5DF", "#F70298", "#9A0861", "#5C67B7", "#253964", "#8B2E27", "#03699B", "#E91E63"};
        if (position < colors.length) return colors[position];
        else {
            position = position % 10;
            return colors[position];
        }
    }

    public static Date getCurrentDateWithoutTime() {
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }
}
