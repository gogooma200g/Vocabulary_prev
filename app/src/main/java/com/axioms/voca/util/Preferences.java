package com.axioms.voca.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by 김민정 on 2018-04-03.
 */

public class Preferences {
    private static final String PREFERENCE_NAME             = "APPADULT_PREFERENCE";
    public static final String P_KEY_ISTABLE		        = "P_KEY_IS_TABLE";
    private static final String P_KEY_SCREEN_WIDTH		    = "P_KEY_SCREEN_WIDTH";
    private static final String P_KEY_SCREEN_HIGHT		    = "P_KEY_SCREEN_HIGHT";


    //get SharedPreference
    public static SharedPreferences getPreferences(Context ctx){
        return ctx.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

    private static SharedPreferences.Editor getPreferencesEditor(Context ctx) {
        SharedPreferences pref = getPreferences(ctx);
        return pref.edit();
    }

    public void setScreenWidth(Context context, int width) {
        SharedPreferences.Editor editor = getPreferencesEditor(context);
        editor.putInt(P_KEY_SCREEN_WIDTH, width);
        editor.commit();
    }

    public void setScreenHight(Context context, int height) {
        SharedPreferences.Editor editor = getPreferencesEditor(context);
        editor.putInt(P_KEY_SCREEN_HIGHT, height);
        editor.commit();
    }

    public static void putPref(Context context, String key, String value) {
        SharedPreferences.Editor editor = getPreferencesEditor(context);
        editor.putString(key, value);
        editor.commit();
    }

    public static void putPref(Context context, String key, int value) {
        SharedPreferences.Editor editor = getPreferencesEditor(context);
        editor.putInt(key, value);
        editor.commit();
    }

    public static void putPref(Context context, String key, boolean value) {
        SharedPreferences.Editor editor = getPreferencesEditor(context);
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static void putPref(Context context, String key, long value) {
        SharedPreferences.Editor editor = getPreferencesEditor(context);
        editor.putLong(key, value);
        editor.commit();
    }

    public static String getPref(Context context, String key, String defaultValue) {
        try {
            return getPreferences(context).getString(key, defaultValue);
        }catch (Exception e) {
            return defaultValue;
        }
    }

    public static int getPref(Context context, String key, int defaultValue) {
        try {
            return getPreferences(context).getInt(key, defaultValue);
        }catch (Exception e) {
            return defaultValue;
        }
    }

    public static boolean getPref(Context context, String key, boolean defaultValue) {
        try {
            return getPreferences(context).getBoolean(key, defaultValue);
        }catch (Exception e) {
            return defaultValue;
        }
    }

    public static long getPref(Context context, String key, long defaultValue) {
        try {
            return getPreferences(context).getLong(key, defaultValue);
        }catch (Exception e) {
            return defaultValue;
        }
    }

    public static void preClear(Context ctx) {
        SharedPreferences.Editor editor = getPreferencesEditor(ctx);
        editor.clear();
        editor.commit();
    }


}
