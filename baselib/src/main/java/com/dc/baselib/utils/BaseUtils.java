package com.dc.baselib.utils;


public class BaseUtils {
    public static String getString(int resId) {
        return BaseApplication.getsInstance().getResources().getString(resId);
    }
}
