package com.dc.baselib.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Sp处理类，
 */
public class SPUtils {
    public static final String FILE_NAME = "ZHILIANDA_DATA";
    public static final String CACHE_FILE_NAME = "CACHE_DATA";
    public static final String CONFIG = "CONFIG";
    public static final String ISWIFIPLAY = "isWificonfig";
    public static final String OLD_REFRESH_TIME = "old_refresh_time";
    public static void saveData(Context context, String key, String txt) {
        SharedPreferences preferences = context.getApplicationContext().getSharedPreferences(FILE_NAME, 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, txt);
        editor.apply();
        Log.d("User", "#保存User成功#" + txt);
    }

    public static String getData(Context context, String key) {
        SharedPreferences preferences = context.getSharedPreferences(FILE_NAME, 0);
        return preferences.getString(key, null);
    }

    public static void putConfigBool(Context context, String key, boolean wifi) {
        SharedPreferences preferences = context.getSharedPreferences(CONFIG, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key, wifi);
        editor.apply();
    }

    public static boolean getConfigBool(Context context, String key) {
        SharedPreferences preferences = context.getSharedPreferences(CONFIG, Context.MODE_PRIVATE);
        return preferences.getBoolean(key, false);
    }

    /**
     * 取
     *
     * @param context
     * @param key
     * @param
     * @return
     */
    public static String getConfigString(Context context, String key) {
        SharedPreferences preferences = context.getSharedPreferences(CONFIG, Context.MODE_PRIVATE);
        return preferences.getString(key, null);
    }

    public static Long getConfigLong(Context context, String key) {
        SharedPreferences preferences = context.getSharedPreferences(CONFIG, Context.MODE_PRIVATE);
        return preferences.getLong(key, 0);
    }

    public static void putConfigLong(Context context, String key, long lon) {
        SharedPreferences preferences = context.getSharedPreferences(CONFIG, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong(key, lon);
        editor.apply();
    }

    /**
     * 存
     *
     * @param context
     * @param key
     * @param cont
     */
    public static void putConfigString(Context context, String key, String cont) {
        SharedPreferences preferences = context.getSharedPreferences(CONFIG, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, cont);
        editor.apply();
    }

    public static void saveData(Context context, String key, boolean content) {
        SharedPreferences preferences = context.getSharedPreferences(FILE_NAME, 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key, content);
        editor.apply();
    }

    public static boolean getData(Context context, String key, boolean defValue) {
        SharedPreferences preferences = context.getSharedPreferences(FILE_NAME, 0);
        return preferences.getBoolean(key, defValue);
    }

    public static void clearData(Context context, String key) {
        SharedPreferences preferences = context.getApplicationContext().getSharedPreferences(FILE_NAME, 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, "");
        editor.apply();
    }

    public static void clearAllData(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(FILE_NAME, 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
    }

    public static void saveCacheData(Context context, String key, String txt) {
        SharedPreferences preferences = context.getApplicationContext().getSharedPreferences(CACHE_FILE_NAME, 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, txt);
        editor.apply();
        Log.d("Cache", "#保存Cache成功#" + txt);
    }

    public static String getCacheData(Context context, String key) {
        SharedPreferences preferences = context.getSharedPreferences(CACHE_FILE_NAME, 0);
        return preferences.getString(key, null);
    }

    public static void clearCacheData(Context context, String key) {
        SharedPreferences preferences = context.getApplicationContext().getSharedPreferences(CACHE_FILE_NAME, 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, "");
        editor.apply();
    }

}
