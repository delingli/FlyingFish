package com.dc.baselib.utils;

import android.content.Context;
import android.text.TextUtils;

import com.dc.baselib.BaseApplication;
import com.dc.baselib.baseEntiry.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * User管理类，操作User
 */
public class UserManager {
    private static final String USER_INFO = "user_info";//USER文件key

    private UserManager() {

    }

    /**
     * 保存User信息
     *
     * @param context
     * @param user
     */
    public void saveUserInfo(Context context, User user) {
        if (null != user) {
            String jsonString = new Gson().toJson(user);
            try {
                String miStr = base64.encryptBASE64(jsonString);
                SPUtils.saveData(context, USER_INFO, miStr);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public String getUserId() {
        try {
            String data = SPUtils.getData(BaseApplication.getsInstance(), USER_INFO);
            if (TextUtils.isEmpty(data)) {
                return null;
            }
            String minStr = base64.decryptBASE64(data);//解密
            if (null != minStr) {
                User u = new Gson().fromJson(minStr, new TypeToken<User>() {
                }.getType());
                return u.user_id;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public String getToken() {
        try {
            String data = SPUtils.getData(BaseApplication.getsInstance(), USER_INFO);
            if (TextUtils.isEmpty(data)) {
                return null;
            }
            String minStr = base64.decryptBASE64(data);//解密
            if (null != minStr) {
                User u = new Gson().fromJson(minStr, new TypeToken<User>() {
                }.getType());
                return u.sid;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public User getUserInfo(Context context) {

        try {
            String data = SPUtils.getData(context.getApplicationContext(), USER_INFO);
            String minStr = base64.decryptBASE64(data);//解密
            if (null != minStr) {
                User u = new Gson().fromJson(minStr, new TypeToken<User>() {
                }.getType());
                return u;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public void clearUser(Context context) {
        SPUtils.clearData(context, USER_INFO);
    }

    private volatile static UserManager mInstance;

    public static UserManager getInstance() {
        if (mInstance == null) {
            synchronized (UserManager.class) {
                if (mInstance == null) {
                    mInstance = new UserManager();
                }
            }
        }
        return mInstance;
    }

    public boolean isLogin() {
        if (getUserInfo(BaseApplication.getsInstance()) == null) {
            return false;
        }
        return true;
    }
}
