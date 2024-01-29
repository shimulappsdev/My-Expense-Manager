package com.expensemanager.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceHelper {
    private static final String PREF_FILE_NAME = "MyAppPrefs";
    private static final String KEY_IS_REGISTERED = "isRegistered";

    public static void setRegistered(Context context, boolean isRegistered) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(KEY_IS_REGISTERED, isRegistered);
        editor.apply();
    }

    public static boolean isRegistered(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        return preferences.getBoolean(KEY_IS_REGISTERED, false);
    }
}
