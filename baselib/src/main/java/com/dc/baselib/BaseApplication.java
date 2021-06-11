package com.dc.baselib;

import android.app.Application;
import android.content.Context;

import com.alibaba.android.arouter.launcher.ARouter;

public class BaseApplication extends Application {
    private static Context sInstance;


    public static Context getsInstance() {
        return sInstance;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG) {
            //开启InstantRun之后，一定要在ARouter.init之前调用openDebug
            ARouter.openDebug();
            ARouter.openLog();
        }
        sInstance = this.getApplicationContext();
        ARouter.init(this);
//        RNUMConfigure.init(this, "5c762686f1f556fb530001af", "Umeng", UMConfigure.DEVICE_TYPE_PHONE, "");
    }



}
