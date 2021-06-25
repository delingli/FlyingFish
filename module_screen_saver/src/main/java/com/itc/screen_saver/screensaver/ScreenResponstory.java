package com.itc.screen_saver.screensaver;

import android.util.Log;

import com.dc.baselib.http.newhttp.AbsHttpSubscriber;
import com.dc.baselib.mvvm.BaseRespository;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ScreenResponstory extends BaseRespository {
    public void toFetchScreenSaverData(String serial_no) {
        addDisposable(mRetrofit.create(IScreenSaverService.class)
                .fetchScreenSaver(serial_no)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new AbsHttpSubscriber<ScreenSaverEntiry>() {

                    @Override
                    public void onSuccess(ScreenSaverEntiry screenSaverEntiry) {

                    }

                    @Override
                    public void onFailure(String msg, int code) {

                    }
                }));
    }
}