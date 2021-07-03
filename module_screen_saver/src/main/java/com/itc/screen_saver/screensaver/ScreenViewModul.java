package com.itc.screen_saver.screensaver;

import android.app.Application;

import androidx.annotation.NonNull;

import com.dc.baselib.mvvm.AbsViewModel;
import com.dc.baselib.mvvm.EventUtils;
import com.dc.commonlib.util.DeviceIdUtil;

import org.jetbrains.annotations.NotNull;

public class ScreenViewModul extends AbsViewModel<ScreenResponstory> {
    public static String EVENT_KEY_SCREEN_SAVER;
    public static String EVENT_KEY_SCREEN_SAVER_ERROR;


    public ScreenViewModul(@NonNull @NotNull Application application) {
        super(application);
        EVENT_KEY_SCREEN_SAVER = EventUtils.getEventKey();
        EVENT_KEY_SCREEN_SAVER_ERROR = EventUtils.getEventKey();
    }

    public void toFetchScreenSaverData() {
        mRepository.toFetchScreenSaverData(DeviceIdUtil.getDeviceId(getApplication()), new ScreenResponstory.ScreenCallBack<ScreenSaverEntiry>() {

            @Override
            public void onSucess(ScreenSaverEntiry screenSaverEntiry) {
                if (null != screenSaverEntiry) {
                    postData(EVENT_KEY_SCREEN_SAVER, screenSaverEntiry);
                }
            }

            @Override
            public void onError(String msg) {
                postData(EVENT_KEY_SCREEN_SAVER_ERROR, msg);
            }
        });


    }


    @Override
    protected ScreenResponstory getRepository() {
        return new ScreenResponstory();
    }
}
