package com.shouman.apps.hawk.common;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.ColorRes;

import com.shouman.apps.hawk.R;

import java.util.Calendar;
import java.util.Date;


public class Common {
    private static final String TAG = "Common class";
    public static final int COMPANY_ACCOUNT = 0;
    public static final int SALES_ACCOUNT = 1;
    public static final String ACTIVITY_NEW_CUSTOMER = "New_Customer";
    public static final String ACTIVITY_NEW_VISIT = "New_Visit";
    public static final String ACTIVITY_NEW_SALESMAN = "New_Salesman";

    public static String[] getAllPositions(Context context) {
        return context.getResources().getStringArray(R.array.positions_array);
    }

    public static String getRandomColor(int position) {
        String[] colors;
        colors = new String[]{"#EF5350", "#EC407A", "#AB47BC", "#42A5F5", "#26C6DA", "#26A69A", "#FFEE58", "#FFA726", "#D4E157", "#FF7043"};
        if (position < colors.length) return colors[position];
        else {
            position = position % 10;
            return colors[position];
        }
    }

    public static Date getCurrentDateWithoutTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public static void setSystemBarColor(Activity act, @ColorRes int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = act.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(act.getResources().getColor(color));
        }
    }
}
