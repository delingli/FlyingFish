package com.itc.screen_saver.screensaver;

import android.text.TextUtils;
import android.util.Log;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.dc.baselib.constant.Constants;
import com.dc.baselib.http.newhttp.AbsHttpSubscriber;
import com.dc.baselib.mvvm.BaseRespository;
import com.dc.baselib.utils.SPUtils;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

public class ScreenResponstory extends BaseRespository {
    private static final String TAG = "ScreenResponstory";
    private static final String SCREEN_SAVE_KEY = "screen_save_key";

    public void toFetchScreenSaverData(String serial_no, ScreenCallBack callBack) {
        refreshScreenData(serial_no, callBack);//刷新数据
    }

    private void refreshScreenData(String serial_no, ScreenCallBack callBack) {
        LogUtils.d(TAG, serial_no);
        addDisposable(mRetrofit.create(IScreenSaverService.class)
                .fetchScreenSaver(serial_no)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new AbsHttpSubscriber<ScreenSaverEntiry>() {
                    @Override
                    public void onSuccess(ScreenSaverEntiry screenSaverEntiry) {
                        if (null != screenSaverEntiry) {
                            LogUtils.dTag(TAG, screenSaverEntiry.toString());
                            String json = GsonUtils.toJson(screenSaverEntiry);
                            LogUtils.d(TAG, json);
                            SPUtils.putConfigString(serial_no + SCREEN_SAVE_KEY, json);
                            SPUtils.putLongData(Constants.SCREEN_DATE, screenSaverEntiry.getLaunch_time());//更新屏保日期
                            if (null != callBack) {
                                callBack.onSucess(screenSaverEntiry);
                            }
                        }
                    }

                    @Override
                    public void onFailure(String msg, int code) {
                        LogUtils.e(TAG, msg + code);
                        if (null != callBack) {
                            callBack.onError(msg);
                        }
                    }
                }));
    }

    interface ScreenCallBack<T> {
        void onSucess(T t);

        void onError(String msg);
    }

    private ScreenSaverEntiry getScreenSaver(String serial_no) {
        String json = SPUtils.getConfigString(serial_no + SCREEN_SAVE_KEY);
        ScreenSaverEntiry screenSaverEntiry = null;
        if (!TextUtils.isEmpty(json)) {
            screenSaverEntiry = GsonUtils.fromJson(json, ScreenSaverEntiry.class);
        }
        return screenSaverEntiry;
    }
}