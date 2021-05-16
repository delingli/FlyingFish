package com.dc.module_main.splash;

import android.app.Application;
import android.support.annotation.NonNull;

import com.dc.baselib.mvvm.AbsViewModel;
import com.dc.commonlib.common.ConfigUtils;
import com.dc.commonlib.utils.UIUtils;
import com.dc.commonlib.utils.UUIDUtils;

import java.util.HashMap;
import java.util.Map;

public class SplashViewModel extends AbsViewModel<SplashRespository> {
    public SplashViewModel(@NonNull Application application) {
        super(application);
    }

}
