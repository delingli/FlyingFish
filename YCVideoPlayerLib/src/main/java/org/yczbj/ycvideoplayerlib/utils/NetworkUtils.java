package org.yczbj.ycvideoplayerlib.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2019/5/21
 *     desc  : 网络状态监听工具类
 *     revise:
 * </pre>
 */
public class NetworkUtils {


    /**
     * 标记当前网络状态，分别是：移动数据、Wifi、未连接、网络状态已公布
     */
    public enum State {
        MOBILE, WIFI, UN_CONNECTED, PUBLISHED
    }

    /**
     * 为了避免因多次接收到广播反复提醒的情况而设置的标志位，用于缓存收到新的广播前的网络状态
     */
    private static State tempState;
    /**
     * 获取网络连接状态
     *
     * @return 0-不可用，1-移动连接可用，2-wifi可用, 3-其他
     */
    public static int getNetworkConnectState(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (manager == null || manager.getActiveNetworkInfo() == null) return 0;
        NetworkInfo activeNetwork = manager.getActiveNetworkInfo();
        if (activeNetwork.isConnected()) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                return 2;
            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                return 1;
            } else {
                return 3;
            }
        } else {
            return 0;
        }
    }

    /**
     * 获取当前网络连接状态
     *
     * @param context Context
     * @return 网络状态
     */
    public static State getConnectState(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (manager != null) {
            networkInfo = manager.getActiveNetworkInfo();
        }
        State state = State.UN_CONNECTED;
        if (networkInfo != null && networkInfo.isAvailable()) {
            if (isMobileConnected(context)) {
                state = State.MOBILE;
            } else if (isWifiConnected(context)) {
                state = State.WIFI;
            }
        }
        if (state.equals(tempState)) {
            return State.PUBLISHED;
        }
        tempState = state;
        return state;
    }

    private static boolean isMobileConnected(Context context) {
        return isConnected(context, ConnectivityManager.TYPE_MOBILE);
    }

    public static boolean isWifiConnected(Context context) {
        return isConnected(context, ConnectivityManager.TYPE_WIFI);
    }

    private static boolean isConnected(Context context, int type) {
        //getAllNetworkInfo() 在 API 23 中被弃用
        //getAllNetworks() 在 API 21 中才添加
        ConnectivityManager manager = (ConnectivityManager) context.
                getSystemService(Context.CONNECTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            NetworkInfo[] allNetworkInfo = new NetworkInfo[0];
            if (manager != null) {
                allNetworkInfo = manager.getAllNetworkInfo();
            }
            for (NetworkInfo info : allNetworkInfo) {
                if (info.getType() == type) {
                    return info.isAvailable();
                }
            }
        } else {
            Network[] networks = new Network[0];
            if (manager != null) {
                networks = manager.getAllNetworks();
            }
            for (Network network : networks) {
                NetworkInfo networkInfo = manager.getNetworkInfo(network);
                if (networkInfo.getType() == type) {
                    return networkInfo.isAvailable();
                }
            }
        }
        return false;
    }

}
