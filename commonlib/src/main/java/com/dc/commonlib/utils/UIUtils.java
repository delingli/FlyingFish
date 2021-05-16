package com.dc.commonlib.utils;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;


import com.dc.baselib.utils.Md5Util;
import com.dc.baselib.utils.ToastUtils;
import com.dc.baselib.utils.UserManager;
import com.dc.commonlib.R;
import com.dc.commonlib.common.ConfigUtils;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UIUtils {

    //手机号码正则表达式
    public static final String REGEX_MOBILE = "^1[0-9]{10}$";
    //用户昵称正则表达式
    //public static final String REGEX_NICK_NAME = "^[\u4e00-\u9fa5a-zA-Z].{3,9}";
    //用户名正则表达式 只能由英文、数字及\"_\"组成,并且不能为全数字组成
    public static final String telRegexUserName = "((?=.*[a-z])(?=.*\\d)|(?=[a-z])(?=.*[_])|(?=.*\\d)(?=.*[_])|(?=.*[a-z]))[a-z\\d_]{4,16}";

    public static boolean checkUserName(String userName) {
        Pattern p = Pattern.compile(telRegexUserName);
        Matcher m = p.matcher(userName);  // 最后调用m.find()方法来进行判断，
        return m.find();
    }

    //将时间转换为时间戳
    public static long dateToStamp(String s) {
        if (TextUtils.isEmpty(s)) {
            return 0;
        }
//        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = simpleDateFormat.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long ss=date.getTime();
//        long ts = date.getTime();
//        res = String.valueOf(ts);
        return date.getTime();
    }

    public static void setFlingDistance(ViewPager viewPager) {
        try {
            Field mFlingDistance = ViewPager.class.getDeclaredField("mFlingDistance");
            mFlingDistance.setAccessible(true);
            mFlingDistance.set(viewPager, 10);


            Field mMinimumVelocity = ViewPager.class.getDeclaredField("mMinimumVelocity");
            mMinimumVelocity.setAccessible(true);
            mMinimumVelocity.set(viewPager, 5);


        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static boolean checkEmail(String email) {
        String regex = "\\w+@\\w+\\.[a-z]+(\\.[a-z]+)?";
        return Pattern.matches(regex, email);
    }

    public static boolean checkPhone(String phone) {
        if (phone.length() != 11) {
            return false;
        }
        char indexChar = phone.charAt(0);
        if (indexChar != '1') {
            return false;
        }
        return true;
    }

    private static String sortData(Map<String, String> map) {

        List<Map.Entry<String, String>> entries = new ArrayList<Map.Entry<String, String>>(map.entrySet());
        Collections.sort(entries, new Comparator<Map.Entry<String, String>>() {
            @Override
            public int compare(Map.Entry<String, String> o1, Map.Entry<String, String> o2) {
                return o1.getKey().compareTo(o2.getKey());
            }
        });
        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, String> entry : entries) {
            sb.append(entry.getValue());
        }
        return sb.toString();
    }

    public static String getSign(Map<String, String> map) {
        String sign = sortData(map) + ConfigUtils.APPKEY;
        LogUtil.d("ldl", sign);
        String realSign = Md5Util.md5(sign);
        return realSign;
    }

    public static String getWeishiUrl(String weishiUrl, String timestamp, String nonce) {
        if (!TextUtils.isEmpty(weishiUrl)) {
            String sign = getWeishiSign(weishiUrl, timestamp, nonce);
            String realPlay;
            if (UserManager.getInstance().isLogin()) {
                String token = UserManager.getInstance().getToken();
                String uid = UserManager.getInstance().getUserId();
                realPlay = weishiUrl + "&timestamp=" + timestamp + "&nonce=" + nonce + "&sign=" + sign + "&token=" + token + "&uid=" + uid;
            } else {
                realPlay = weishiUrl + "&timestamp=" + timestamp + "&nonce=" + nonce + "&sign=" + sign;
            }
            return realPlay;
        } else {
            return null;
        }
    }

    public static String getWeishiSign(String weishiUrl, String timestamp, String nonce) {
        if (!TextUtils.isEmpty(weishiUrl)) {
            Map<String, String> urlParams = UrlParse.getUrlParams(weishiUrl);
            if (UserManager.getInstance().isLogin()) {
                String token = UserManager.getInstance().getToken();
                String userId = UserManager.getInstance().getUserId();
                urlParams.put("token", token);
                urlParams.put("uid", userId);
            }
            urlParams.put("timestamp", timestamp);
            urlParams.put("nonce", nonce);
            String ss = sortData(urlParams) + ConfigUtils.APPKEY;
            ;
            return Md5Util.md5(ss);
        } else {
            return null;
        }
    }

    public static final String PASSWORD_PATTERN = "^(?![0-9]+$)(?![a-z]+$)(?![A-Z]+$)(?!([^(0-9a-zA-Z)])+$).{6,18}$";

    public static boolean isPasswordChecked(CharSequence data) {
        //6-18位，数字、字母、-
        Pattern p = Pattern.compile(PASSWORD_PATTERN);
        Matcher m = p.matcher(data);
        return m.find();
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager mgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] info = mgr.getAllNetworkInfo();
        if (info != null) {
            for (int i = 0; i < info.length; i++) {
                if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            }
        }
        return false;
    }


    public static Intent getAppDetailSettingIntent(Context context) {
        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            localIntent.setData(Uri.fromParts("package", context.getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            localIntent.setAction(Intent.ACTION_VIEW);
            localIntent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
            localIntent.putExtra("com.android.settings.ApplicationPkgName", context.getPackageName());
        }
        return localIntent;
    }

//
//	public static String getDeviceInfo(Context context) {
//		try {
//			org.json.JSONObject json = new org.json.JSONObject();
//			android.telephony.TelephonyManager tm = (android.telephony.TelephonyManager) context
//					.getSystemService(Context.TELEPHONY_SERVICE);
//
//			String device_id = tm.getDeviceId();
//
//			android.net.wifi.WifiManager wifi = (android.net.wifi.WifiManager) context
//					.getSystemService(Context.WIFI_SERVICE);
//
//			String mac = wifi.getConnectionInfo().getMacAddress();
//			json.put("mac", mac);
//
//			if (TextUtils.isEmpty(device_id)) {
//				device_id = mac;
//			}
//
//			if (TextUtils.isEmpty(device_id)) {
//				device_id = android.provider.Settings.Secure.getString(
//						context.getContentResolver(),
//						android.provider.Settings.Secure.ANDROID_ID);
//			}
//
//			json.put("device_id", device_id);
//
//			return json.toString();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return null;
//	}

    /**
     * 判断是否可以请求网络
     *
     * @param activity
     * @return
     */
//	public static boolean isRequestNet(Context activity) {// 没有退出，并且网络可用
//		if (!isLogout(activity) && NetworkUtils.isNetworkAvailable(activity)) {
//			return true;
//		}
//		return false;
//	}


    /**
     * dipתpx
     */
    public static int dip2px(Context context, int dip) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dip * scale + 0.5f);
    }

    /**
     * pxzתdip
     */
    public static int px2dip(Context context, int px) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }

    public static boolean isMobileNO(String mobiles) {
        String regExp = "^[1]([3][0-9]{1}|59|58|88|89)[0-9]{8}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(mobiles);
        return m.find();//boolean
    }

    /**
     * 获取资源
     */
    public static Resources getResources(Context context) {
        return context.getApplicationContext().getResources();
    }

    /**
     * 获取文字
     */
    public static String getString(Context context, int resId) {
        return context.getApplicationContext().getResources().getString(resId);
    }

    public static String fen2Yuan(int fen) {
        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        numberFormat.setMaximumFractionDigits(2);
        return "￥ " + numberFormat.format(fen / 100.0);
    }

    public static String tranSize(int size) {
        return size + "字节";
    }

    /**
     * 获取文字数组
     */
    public static String[] getStringArray(Context context, int resId) {
        return context.getApplicationContext().getResources().getStringArray(resId);
    }

    /**
     * 获取dimen
     */
    public static int getDimens(Context context, int resId) {
        return context.getApplicationContext().getResources().getDimensionPixelSize(resId);
    }

    /**
     * 获取drawable
     */
    public static Drawable getDrawable(Context context, int resId) {
        return context.getApplicationContext().getResources().getDrawable(resId);
    }

    /**
     * 获取颜色
     */
    public static int getColor(Context context, int resId) {
        return context.getApplicationContext().getResources().getColor(resId);
    }


    /**
     * 获取颜色选择�?
     */
    public static ColorStateList getColorStateList(Context context, int resId) {
        return context.getApplicationContext().getResources().getColorStateList(resId);
    }


//
//	public static void runInMainThread(Runnable runnable) {
//		if (isRunInMainThread()) {
//			runnable.run();
//		} else {
//			post(runnable);
////		}
//	}


    /**
     * ��ȡͼƬ·��
     *
     * @param context
     * @param uri
     * @return
     */
    @SuppressLint("NewApi")
    public static String getPath(final Context context, final Uri uri) {
        // Build.VERSION_CODES.KITKAT
        final boolean isKitKat = Build.VERSION.SDK_INT >= 19;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/"
                            + split[1];
                }
            }// DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"),
                        Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }// MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};

                return getDataColumn(context, contentUri, selection,
                        selectionArgs);
            }
        }// MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // Return the remote address
            if (isGooglePhotosUri(uri)) {
                return uri.getLastPathSegment();
            }

            return getDataColumn(context, uri, null, null);
        }// File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * �Ƿ�Ϊgoogle photo
     *
     * @param uri
     * @return
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri
                .getAuthority());
    }

    /**
     * �Ƿ�ΪMediaDocument
     *
     * @param uri
     * @return
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri
                .getAuthority());
    }

    /**
     * �Ƿ�Ϊdownloads.documents
     *
     * @param uri
     * @return
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri
                .getAuthority());
    }

    /**
     * �Ƿ�Ϊexternalstorage.documents
     *
     * @param uri
     * @return
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri
                .getAuthority());
    }

    /**
     * ��ȡͼƬ·��
     *
     * @param context
     * @param uri
     * @param selection     ��ѯ����
     * @param selectionArgs ��ѯ����
     * @return ͼƬ·��
     */
    public static String getDataColumn(Context context, Uri uri,
                                       String selection, String[] selectionArgs) {

        Cursor cursor = null;
        final String column = MediaStore.Images.Media.DATA;
        final String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, projection,
                    selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;
    }

    // 第一眼我看到这段代码，靠，太粗暴了，但是回头一想，要的就是这么简单粗暴，不要把一些简单的东西搞的那么复杂。
    private static void unregisterReceiverSafe(Context context, BroadcastReceiver receiver) {
        try {
            context.unregisterReceiver(receiver);
        } catch (IllegalArgumentException e) {
            // ignore
        }
    }

    public static boolean verifyPhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.length() == 0) {
            throw new IllegalArgumentException("parameter is null");
        }
        if (phoneNumber.matches(REGEX_MOBILE)) {
            return true;
        }
        return false;
    }

    @SuppressLint("NewApi")
    public static boolean verifyNickName(String nickName) {
        if (nickName == null || nickName.length() == 0) {
            throw new IllegalArgumentException("parameter is null");
        }
        int weight = 0;
        for (int i = 0; i < nickName.codePointCount(0, nickName.length()); i++) {
            int var = nickName.codePointAt(i);
            if (var < 128) {
                weight++;
            } else if (Character.isIdeographic(var)) {
                weight = weight + 2;
            } else {
                weight = weight + 2;
            }
        }
        if (weight > 16) {
            return false;
        } else {
            return true;
        }
    }

    public static String omitFileName(String fileName, int scope) {
        if (TextUtils.isEmpty(fileName)) {
            return null;
        }
        if (scope <= 0) {
            scope = 20;
        }
        char[] chars = fileName.toCharArray();
        int weight = 0;
        int index = 0;
        for (int i = 0; i < chars.length; i++) {
            int var = chars[i];
            if (var < 128 && var > 0) {
                weight++;
            } else if (Character.isIdeographic(var)) {
                weight += 2;
            } else {
                weight += 2;
            }
            if (weight > scope) {
                index = i;
                break;
            }
        }
        if (weight > scope) {
            return fileName.substring(0, index - 1) + "...";
        } else {
            return fileName;
        }
    }


    public static int getScreenWidth(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return displayMetrics.widthPixels;
    }

    public static int getScreenHeight(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return displayMetrics.heightPixels;
    }


    public static int fetchFileTotalSize(String[] path) {
        if (path == null || path.length == 0) {
            return 0;
        }
        int total = 0;
        for (int i = 0; i < path.length; i++) {
            File file = new File(path[i]);
            total += file.length();
        }
        return total;
    }

    public static String omitMidStringOverLength(String value, int scope) {
        if (value == null || value.length() == 0) {
            return value;
        }
        if (scope <= 0) {
            scope = 20;
        }
        if (value.length() >= scope) {
            String prvious = value.substring(0, scope / 2);
            String bebind = value.substring(value.length() - scope / 2);
            return prvious.concat("...").concat(bebind);
        } else {
            return value;
        }
    }


    /**
     * AppBarLayout滚动到指定位置
     *
     * @param appBar
     * @param offset
     */
    public static void setAppBarLayoutOffset(AppBarLayout appBar, int offset) {
        ViewGroup.LayoutParams params = appBar.getLayoutParams();
        if (params instanceof CoordinatorLayout.LayoutParams) {
            try {
                CoordinatorLayout.Behavior behavior = ((CoordinatorLayout.LayoutParams) params).getBehavior();
                Class cls = Class.forName("android.support.design.widget.HeaderBehavior");
                //滑到指定位置
                Method animateOffsetTo = cls.getDeclaredMethod("setHeaderTopBottomOffset", CoordinatorLayout.class, View.class, int.class);
                animateOffsetTo.setAccessible(true);
                animateOffsetTo.invoke(behavior, appBar.getParent(), appBar, offset);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
