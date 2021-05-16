package com.dc.module_bbs.videomonitoring;

import android.os.Bundle;

import com.dc.baselib.mvvm.AbsLifecycleActivity;
import com.dc.module_bbs.R;

/**
 * 视频监控
 */
public class VideoMonitoringActivity extends AbsLifecycleActivity<VideoMonitoringViewModel> {
    @Override
    protected int getLayout() {
        return R.layout.common_refresh_layout;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
    }

    @Override
    protected void initData() {

    }

}
