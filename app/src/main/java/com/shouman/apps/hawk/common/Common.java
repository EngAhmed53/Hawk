package com.shouman.apps.hawk.common;

import android.content.Context;

import com.shouman.apps.hawk.R;

import java.util.Random;


public class Common {
    private static final String TAG = "Common class";
    public static int MANAGER_POSITION = 0;
    public static int SALES_POSITION = 1;
    public static final String BASE = "0123456789abcdefghijklmnopqrstuvwxyz";



    public static String[] getAllPositions(Context context) {
        return context.getResources().getStringArray(R.array.positions_array);
    }

    public static String generateRandomCompanyID() {
        Random r = new Random();
        StringBuilder generatedId = new StringBuilder();

        for (int i = 0; i < 5; i++) {
            generatedId.append(BASE.charAt(r.nextInt(BASE.length())));
        }

        return generatedId.toString();
    }
}
