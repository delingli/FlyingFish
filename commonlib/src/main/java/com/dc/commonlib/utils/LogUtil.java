package com.dc.commonlib.utils;

import android.text.TextUtils;
import android.util.Log;

import java.util.Locale;

public class LogUtil {
    private static final String MATCH = "%s:%s:%d:";
    private static final String TAG = "DB_DF";

    private static boolean sLogout = true;

    private LogUtil() {
    }

    public static void logout(boolean logout) {
        sLogout = logout;
    }

    public static void v() {
        if (sLogout) Log.v(TAG, buildHeader());
    }

    public static void v(Object msg) {
        if (sLogout) Log.v(TAG, buildHeader() + msg.toString());
    }

    public static void v(Object tag, Object msg) {
        if (sLogout) Log.v(tag.toString(), buildHeader() + msg.toString());
    }

    public static void d() {
        if (sLogout) Log.d(TAG, buildHeader());
    }

    public static void d(Object msg) {
        if (sLogout) Log.d(TAG, buildHeader() + msg.toString());
    }

    public static void d(Object tag, Object msg) {
        if (sLogout) Log.d(tag.toString(), buildHeader() + msg.toString());
    }

    public static void i() {
        if (sLogout) Log.i(TAG, buildHeader());
    }

    public static void i(Object msg) {
        if (sLogout) Log.i(TAG, buildHeader() + msg.toString());
    }

    public static void i(Object tag, Object msg) {
        if (sLogout) Log.i(tag.toString(), buildHeader() + msg.toString());
    }

    public static void w() {
        if (sLogout) Log.w(TAG, buildHeader());
    }

    public static void w(Object msg) {
        if (sLogout) Log.w(TAG, buildHeader() + msg.toString());
    }

    public static void w(Object tag, Object msg) {
        if (sLogout) Log.w(tag.toString(), buildHeader() + msg.toString());
    }

    public static void e() {
        if (sLogout) Log.e(TAG, buildHeader());
    }

    public static void e(Object msg) {
        if (sLogout) Log.e(TAG, buildHeader() + msg.toString());
    }

    public static void e(Object tag, Object msg) {
        if (sLogout) Log.e(tag.toString(), buildHeader() + msg.toString());
    }

    private static String buildHeader() {
        StackTraceElement stack = Thread.currentThread().getStackTrace()[4];
        if (null == stack) return "UNKNOWN_STACK_TRACE_ELEMENT";
        String filename = stack.getFileName();
        if (TextUtils.isEmpty(filename)) filename = "UNKNOWN_FILENAME";
        else if (filename.contains(".java")) filename = filename.replace(".java", "");
        return String.format(Locale.getDefault(), MATCH, filename, Thread.currentThread().getName(),
                stack.getLineNumber());
    }
}
