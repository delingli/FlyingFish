package com.dc.commonlib.commonentity.video;

import android.content.Context;

import com.dc.baselib.baseEntiry.User;
import com.dc.baselib.utils.SPUtils;
import com.dc.baselib.utils.UserManager;
import com.dc.baselib.utils.base64;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class VideoAccountInfoManager {
    private VideoAccountInfoManager() {
    }

    private volatile static VideoAccountInfoManager mInstance;

    public static VideoAccountInfoManager getInstance() {
        if (mInstance == null) {
            synchronized (VideoAccountInfoManager.class) {
                if (mInstance == null) {
                    mInstance = new VideoAccountInfoManager();
                }
            }
        }
        return mInstance;
    }

    public static String ACCOUNT_INFO = "account_info";

    public void saveVideoAccountInfo(Context context, String projectId, VideoAccountBean videoaccountbean) {
        if (null != videoaccountbean) {
            String jsonString = new Gson().toJson(videoaccountbean);
            try {
                String miStr = base64.encryptBASE64(jsonString);
                SPUtils.saveData(context, ACCOUNT_INFO + "_" + projectId, miStr);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public VideoAccountBean getVideoAccountInfo(Context context, String projectId) {

        try {
            String data = SPUtils.getData(context.getApplicationContext(), ACCOUNT_INFO + "_" + projectId);
            String minStr = base64.decryptBASE64(data);//解密
            if (null != minStr) {
                VideoAccountBean videoaccountbean = new Gson().fromJson(minStr, new TypeToken<VideoAccountBean>() {
                }.getType());
                return videoaccountbean;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
