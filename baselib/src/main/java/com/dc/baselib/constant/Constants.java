package com.dc.baselib.constant;

import android.text.TextUtils;

import com.blankj.utilcode.util.LogUtils;
import com.tencent.mmkv.MMKV;

public class Constants {
    public static final String CURRENT_TIME = "current_time";
    public static final String SCREEN_DATE = "screen_time";


    public static final String DEFAULT_SERVER_HOST = "10.10.20.240";
    public static final String DEFAULT_RABBIT_PORT = "5577";
    public static final String DEFAULT_SERVER_PORT = "9997";
    public static final String DEFAULT_RABBIT_NAME = "itc";
    public static final String DEFAULT_RABBIT_PASSWORD = "itc.pass";

    public  String WEB_URL = "http://" + GET_SERVER_HOST() + ":" + GET_SERVER_PORT();

    public static Constants mConstants;
    private static MMKV mmkv;

    public static Constants getmConstants() {
        if (mConstants == null) {
            mConstants = new Constants();
        }
        return mConstants;
    }

    private Constants() {
        mmkv = MMKV.defaultMMKV();
    }

    public static String SERVERHOST = "serverHost";
    public static String RABBITPORT = "rabbitPort";
    public static String SERVERPORT = "serverport";
    public static String RABBITNAME = "rabbitname";
    public static String RABBITPASSWORD = "rabbit_password";

    public  String GET_SERVER_HOST() {
        String str = mmkv.decodeString(SERVERHOST, DEFAULT_SERVER_HOST);
        LogUtils.dTag("Constants", str);
        return str;
    }

    public  void SET_SERVER_HOST(String host) {
        if (!TextUtils.isEmpty(host)) {
            mmkv.removeValueForKey(SERVERHOST);
            mmkv.encode(SERVERHOST, host);
            LogUtils.dTag("Constants", host);
        }
    }


    public  String GET_SERVER_PORT() {
        LogUtils.dTag("Constants",mmkv.decodeString(SERVERPORT, DEFAULT_SERVER_PORT));
        return mmkv.decodeString(SERVERPORT, DEFAULT_SERVER_PORT);
    }

    public  void SET_SERVER_PORT(String port) {
       mmkv.removeValueForKey(SERVERPORT);
       mmkv.encode(SERVERPORT, port);
    }

    public  String GET_RABBIT_PORT() {
        LogUtils.dTag("Constants",mmkv.decodeString(RABBITPORT, DEFAULT_RABBIT_PORT));
        return mmkv.decodeString(RABBITPORT, DEFAULT_RABBIT_PORT);
    }

    public  void SET_RABBIT_PORT(String rabbit_port) {
       mmkv.removeValueForKey(RABBITPORT);
       mmkv.encode(RABBITPORT, rabbit_port);
    }

    public  String GET_RABBIT_NAME() {
        return mmkv.decodeString(RABBITNAME, DEFAULT_RABBIT_NAME);
    }

    public  void SET_RABBIT_NAME(String rabbitname) {
        if (!TextUtils.isEmpty(rabbitname)) {
           mmkv.removeValueForKey(RABBITNAME);
           mmkv.encode(RABBITNAME, rabbitname);
        }
    }

    public  String GET_RABBIT_PASSWORD() {
        return mmkv.decodeString(RABBITPASSWORD, DEFAULT_RABBIT_PASSWORD);
    }

    public  void SET_RABBIT_PASSWORD(String rabbitpassword) {
        if (!TextUtils.isEmpty(rabbitpassword)) {
           mmkv.removeValueForKey(RABBITPASSWORD);
           mmkv.encode(RABBITPASSWORD, rabbitpassword);
        }
    }
///api/diffusion/Directory/directoryDetail
}
