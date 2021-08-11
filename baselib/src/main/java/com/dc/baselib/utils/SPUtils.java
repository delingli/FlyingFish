package com.dc.baselib.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Sp处理类，
 */
public class SPUtils {
    public static final String CACHE_FILE_NAME = "CACHE_DATA";
    public static void putConfigBool(String key, boolean wifi) {
        SharedPreferences preferences = contexts.getApplicationContext().getSharedPreferences(CACHE_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key, wifi);
        editor.apply();
    }

    public static boolean getConfigBool( String key) {
        SharedPreferences preferences = contexts.getApplicationContext().getSharedPreferences(CACHE_FILE_NAME, Context.MODE_PRIVATE);
        return preferences.getBoolean(key, false);
    }


    /**
     * 存
     *
     * @param key
     * @param cont
     */
    public static void putConfigString(String key, String cont) {
        SharedPreferences preferences = contexts.getApplicationContext().getSharedPreferences(CACHE_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, cont);
        editor.apply();
    }
    public static String getConfigString( String key) {
        SharedPreferences preferences = contexts.getApplicationContext().getSharedPreferences(CACHE_FILE_NAME, Context.MODE_PRIVATE);
        return preferences.getString(key, null);
    }
    public static String getConfigString( String key,String defaultStr) {
        SharedPreferences preferences = contexts.getApplicationContext().getSharedPreferences(CACHE_FILE_NAME, Context.MODE_PRIVATE);
        return preferences.getString(key, defaultStr);
    }
    public static Context contexts;

    public static void initSpUtils(Context context) {
        contexts = context;
    }


    public static void clearData(String key) {
        SharedPreferences preferences = contexts.getApplicationContext().getSharedPreferences(CACHE_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, "");
        editor.apply();
    }

    public static void clearAllData() {
        SharedPreferences preferences = contexts.getSharedPreferences(CACHE_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
    }

    public static void putLongData(String key, long txt) {
        SharedPreferences preferences = contexts.getApplicationContext().getSharedPreferences(CACHE_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong(key, txt);
        editor.apply();
        Log.d("Cache", "#保存Cache成功#" + txt);
    }
    public static long getLongData(String key) {
        SharedPreferences preferences = contexts.getApplicationContext().getSharedPreferences(CACHE_FILE_NAME, Context.MODE_PRIVATE);
        return preferences.getLong(key, 0);
    }




}
