package com.dc.baselib;

import android.app.Application;
import android.content.Context;

import com.alibaba.android.arouter.launcher.ARouter;
import com.dc.baselib.http.Environment;
import com.dc.baselib.http.WebSocketResponseDispatcher;
import com.zld.websocket.WebSocketHandler;
import com.zld.websocket.WebSocketSetting;

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
        initWebSocket();

        sInstance = this.getApplicationContext();
        ARouter.init(this);
//        RNUMConfigure.init(this, "5c762686f1f556fb530001af", "Umeng", UMConfigure.DEVICE_TYPE_PHONE, "");
    }

    private void initWebSocket() {
        WebSocketSetting setting = new WebSocketSetting();
        setting.setConnectUrl(Environment.getInstance().getWSUrl());
        setting.setResponseProcessDispatcher(new WebSocketResponseDispatcher());
        setting.setReconnectWithNetworkChanged(true);
        setting.setConnectTimeout(30 * 1000);
        setting.setReconnectFrequency(100);
        setting.setConnectionLostTimeout(30);
        WebSocketHandler.init(setting).start();
        WebSocketHandler.registerNetworkChangedReceiver(this);
    }


}
