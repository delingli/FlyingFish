
package com.dc.baselib.utils;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.widget.Toast;

import com.dc.baselib.BaseApplication;

/**
 * @Description Toast提醒
 */
public class ToastUtils {

//    private ToastUtils() {
//        throw new AssertionError();
//    }
//
//    public static void show(Context context, int resId) {
//        show(context, context.getResources().getText(resId), Toast.LENGTH_SHORT);
//    }
//
//    public static void show(Context context, int resId, int duration) {
//        show(context, context.getResources().getText(resId), duration);
//    }
//
//    public static void show(Context context, CharSequence text) {
//        show(context, text, Toast.LENGTH_SHORT);
//    }
//
//    public static void show(Context context, CharSequence text, int duration) {
//        Toast.makeText(context, text, duration).show();
//    }
//
//    public static void show(Context context, int resId, Object... args) {
//        show(context, String.format(context.getResources().getString(resId), args),
//                Toast.LENGTH_SHORT);
//    }
//
//    public static void show(Context context, String format, Object... args) {
//        show(context, String.format(format, args), Toast.LENGTH_SHORT);
//    }
//
//    public static void show(Context context, int resId, int duration, Object... args) {
//        show(context, String.format(context.getResources().getString(resId), args), duration);
//    }
//
//    public static void show(Context context, String format, int duration, Object... args) {
//        show(context, String.format(format, args), duration);
//    }



    private static String oldMsg;

    private static Toast toast = null;

    private static long oneTime = 0;

    private static long twoTime = 0;

    public static void showToast(String s) {
        if (null == s) {
            return;
        }
        if (toast == null) {
            toast = Toast.makeText(BaseApplication.getsInstance(), s, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            oneTime = System.currentTimeMillis();
        } else {
            twoTime = System.currentTimeMillis();
            if (s.equals(oldMsg)) {
                if (twoTime - oneTime > Toast.LENGTH_SHORT) {
                    toast.show();
                }
            } else {
                oldMsg = s;
                toast.setText(s);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        }
        oneTime = twoTime;
    }

    public static void showToast(Context context, String s, int duration) {
        if (TextUtils.isEmpty(s)) {
            return;
        }
        if (toast == null) {
            toast = Toast.makeText(context, s, duration);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            oneTime = System.currentTimeMillis();
        } else {
            twoTime = System.currentTimeMillis();

            if (s.equals(oldMsg)) {
                if (twoTime - oneTime > duration) {
                    toast.show();
                }
            } else {
                oldMsg = s;
                toast.setText(s);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        }
        oneTime = twoTime;
    }
}
