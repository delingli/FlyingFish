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

import com.dc.baselib.BaseApplication;
import com.dc.baselib.utils.SPUtils;
import com.itc.screen_saver.ScreenSaverActivity;
import com.itc.screen_saver.rabbit.RabbitService;

import java.util.Date;

public class ScreenSaverService extends Service {

    private MyHandler myHandler;
    private HandlerThread mHandlerThread;

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
//                Intent i = new Intent();
//                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//
//                i.setClass(context, ScreenSaverActivity.
//                        class
//                );
//                context.startActivity(i);
            } catch
            (Exception e) {
                e.printStackTrace();
            }
        }
    };
    private int WART = 11;
    public static final String CURRENT_TIME = "current_time";
    public static final String SCREEN_DATE = "screen_time";

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

            myHandler.sendEmptyMessageDelayed(WART, DELARYTIME);
        }
    }

    private void detectionBusiness() {
        //探测下
        long current = System.currentTimeMillis();//当前时间
        long lastTouchTime = SPUtils.getLongData(getApplicationContext(), CURRENT_TIME);//最后触摸时间
        if (lastTouchTime != -1) {
            //有数据
            Date curDate = new Date(current);
            Date endDate = new Date(lastTouchTime);
            long diff = endDate.getTime() - curDate.getTime();//ms毫秒
            if (diff > 0) {
                Long diffMiniutes = ((diff / (60 * 1000)));//以分钟为单位取整
                long screenDate = SPUtils.getLongData(getApplicationContext(), SCREEN_DATE);//屏保配置时间
                if (screenDate != -1) {//有 做个对比
                    if (diffMiniutes >= screenDate) {
                        //todo 开页面，调用屏保页面,关闭服务
                        Intent intent = new Intent(getApplicationContext(), ScreenSaverActivity.class);
                        startActivity(intent);
                        stopSelf();
                    }
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
