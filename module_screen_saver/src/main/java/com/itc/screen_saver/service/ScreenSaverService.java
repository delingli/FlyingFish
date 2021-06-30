package com.itc.screen_saver.service;

import android.app.KeyguardManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blankj.utilcode.util.LogUtils;
import com.dc.baselib.BaseApplication;
import com.dc.baselib.constant.Constants;
import com.dc.baselib.utils.SPUtils;
import com.dc.baselib.utils.TimeUtils;
import com.itc.screen_saver.screensaver.ScreenSaverActivity;

public class ScreenSaverService extends Service {

    private MyHandler myHandler;
    private HandlerThread mHandlerThread;
    private static final String TAG = "ScreenSaverService";

    public static void startScreenSaverService(Context context) {
        Intent intent = new Intent(context.getApplicationContext(), ScreenSaverService.class);
        context.startService(intent);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private BroadcastReceiver mMasterResetReciever = new BroadcastReceiver() {
        public void
        onReceive(Context context, Intent intent) {
            try {
                //屏蔽系统的屏保
                KeyguardManager mKeyguardManager = (KeyguardManager) BaseApplication.Companion.getInstances().getSystemService(Context.KEYGUARD_SERVICE);
                KeyguardManager.KeyguardLock mKeyguardLock = mKeyguardManager.newKeyguardLock("ScreenSaverService");
                mKeyguardLock.disableKeyguard();
            } catch
            (Exception e) {
                e.printStackTrace();
            }
        }
    };
    private int WART = 11;

    @Override
    public void onCreate() {
        super.onCreate();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_SCREEN_ON);
        registerReceiver(mMasterResetReciever,
                filter);
        mHandlerThread = new HandlerThread("ScreenSaverService");
        mHandlerThread.start();
        myHandler = new MyHandler(mHandlerThread.getLooper());
        Message msg = myHandler.obtainMessage();
        msg.what = WART;
        //将msg发送到自己的handler中，这里指的是my_handler,调用该handler的HandleMessage方法来处理该mug
        msg.sendToTarget();//直接开启
    }

    private int DELARYTIME = 5000 * 2;
    private boolean toFetch = true;

    class MyHandler extends Handler {
        public MyHandler() {
        }

        public MyHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == WART) {
                detectionBusiness();
            }
            if (toFetch) {
                myHandler.sendEmptyMessageDelayed(WART, DELARYTIME);
            }

        }
    }

    private void detectionBusiness() {
        LogUtils.dTag(TAG, "探测下");
        //探测下
        long current = System.currentTimeMillis();//当前时间
        long lastTouchTime = SPUtils.getLongData(Constants.CURRENT_TIME);//最后触摸时间
        if (lastTouchTime == 0) {
            return;
        }
        LogUtils.dTag(TAG, "当前时间戳:" + current + "最后触摸时间戳：" + lastTouchTime);
        String lastDate = TimeUtils.getDateToString(lastTouchTime);
        String currentDate = TimeUtils.getDateToString(current);
        LogUtils.dTag(TAG, "当前时间:" + currentDate + "最后触摸时间：" + lastDate);
        long diff = TimeUtils.getGapMinutes(lastDate, currentDate);//分钟
        if (diff > 0) {
            long screenDate = SPUtils.getLongData(Constants.SCREEN_DATE);//屏保配置时间
            LogUtils.dTag(TAG, "屏保配置时间: " + screenDate + "分钟 时间差：" + diff + " 分钟");
            if (screenDate != 0) {//有 做个对比
                if (diff >= screenDate) {
                    //todo 开页面，调用屏保页面,关闭服务
                    LogUtils.dTag(TAG, "符合时机了开页面:停止服务");
                    Intent intent = new Intent(getApplicationContext(), ScreenSaverActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    toFetch = false;
                    stopSelf();
                }
            }


        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mMasterResetReciever);
        myHandler.removeCallbacksAndMessages(null);
        myHandler = null;
        mHandlerThread.quit();
        mHandlerThread = null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }
}
