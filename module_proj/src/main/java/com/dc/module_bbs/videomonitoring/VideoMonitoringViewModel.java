package com.dc.module_bbs.videomonitoring;

import android.app.Application;
import android.support.annotation.NonNull;

import com.dc.baselib.mvvm.AbsViewModel;

public class VideoMonitoringViewModel extends AbsViewModel<VideoMonitorinitoringRespository> {
    public VideoMonitoringViewModel(@NonNull Application application) {
        super(application);
    }
}
