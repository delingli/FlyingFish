package com.itc.screen_saver.screensaver;

import android.app.Application;

import androidx.annotation.NonNull;

import com.dc.baselib.mvvm.AbsViewModel;
import com.itc.screen_saver.utils.DeviceIdUtil;

import org.jetbrains.annotations.NotNull;

public class ScreenViewModul extends AbsViewModel<ScreenResponstory> {
    public ScreenViewModul(@NonNull @NotNull Application application) {
        super(application);
    }

    public void toFetchScreenSaverData() {
        mRepository.toFetchScreenSaverData(DeviceIdUtil.getDeviceId(getApplication()));
    }

    @Override
    protected ScreenResponstory getRepository() {
        return new ScreenResponstory();
    }
}
