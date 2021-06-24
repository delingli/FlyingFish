package com.dc.baselib.utils;


import com.dc.baselib.BaseApplication;

public class BaseUtils {
    public static String getString(int resId) {
        return BaseApplication.Companion.getInstances().getString(resId);
    }
}
