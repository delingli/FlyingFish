package com.dc.commonlib.rabbit;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.LogUtils;


/**
 * Created by ${zml} on 2019/4/17.
 */
public class RabbitService extends Service implements RabbitDispatcher.RabbitEventListener {


    private static String tag = "RabbitService";
    private static RabbitDispatcher dispatcher;
    private static RabbitService service;
    private static Handler dispatcherHandler;
    private HandlerThread dispatcherThread;
    private static Runnable runner;

    public static void startRabbitService(Context context) {
        Intent intent = new Intent(context.getApplicationContext(), RabbitService.class);
        context.startService(intent);
    }

    public static void stopRabbitService(Context context) {
        Intent intent = new Intent(context.getApplicationContext(), RabbitService.class);
        context.stopService(intent);
    }

    @Override
    public void onMessageReceived(String message) {
        LogUtils.dTag(tag, "onMessageReceived: " + message);
    }

    @Override
    public void onShutdownSignaled(String consumerTag, String sig) {
        LogUtils.dTag(tag, "onShutdownSignaled: " + consumerTag + " " + sig);
        LogUtils.dTag(tag, "On shutdown signaled，recreate dispatcher resources.");
        dispatcherHandler.postDelayed(runner, 5000);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //如果没有配置，显示默认ip
        service = this;
        if (runner == null) {
            runner = new Runnable() {
                @Override
                public void run() {
                    LogUtils.dTag(tag, "Call startRabbitDispatcher in runner ... ");
                    startRabbitDispatcher();
                }
            };
        }
        dispatcherThread = new HandlerThread("dispatcher");
        //开启一个线程
        dispatcherThread.start();
        //在这个线程中创建一个handler对象
        dispatcherHandler = new Handler(dispatcherThread.getLooper());
        dispatcherHandler.postDelayed(runner, 0);
    }


    public static void restartRabbitDispatcher() {
        LogUtils.iTag(tag, "Restart Rabbit dispatcher ... ");
        if (dispatcherHandler == null) return;
        dispatcherHandler.removeCallbacksAndMessages(null);
        dispatcherHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                LogUtils.dTag(tag, "Delayed destroy of dispatcher resources.");
                if (dispatcher != null) {
                    dispatcher.destroyDispatcher();
                    dispatcher = null;
                    dispatcherHandler.postDelayed(runner, 1000);
                }
            }
        }, 100);
    }

    public static boolean startRabbitDispatcher() {
        //ip = ips;
        LogUtils.iTag(tag, "Start Rabbit dispatcher creation ... ");
        if (dispatcher == null) {
            dispatcher = new RabbitDispatcher(service);
        }

        boolean ret = dispatcher.reCreateRabbitConnection();
        if (!ret) {
            LogUtils.iTag(tag, "Start Rabbit dispatcher failed, recreate it after 5s.");
            dispatcherHandler.postDelayed(runner, 5000);
        } else {
            LogUtils.iTag(tag, "Start Rabbit dispatcher successful.");
        }
        return ret;

    }

    public static void publish(final String deviceInfo) {
        if (dispatcherHandler == null) return;
        dispatcherHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                LogUtils.dTag(tag, "publish info in runner.");
                if (dispatcher != null) {
                    dispatcher.publishReport(deviceInfo);
                }
            }
        }, 0);
    }

    public static void publishScreen(final String screens) {
        if (dispatcherHandler == null) return;
        dispatcherHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                LogUtils.dTag(tag, "publish screen info in runner." + screens);
                if (dispatcher != null) {
                    dispatcher.publishReport(screens);
                }
            }
        }, 0);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        LogUtils.dTag(tag, "RabbitService's onDestroy called");
        super.onDestroy();
        if (dispatcher != null) {
            dispatcher.destroyDispatcher();
            dispatcher = null;
        }
        dispatcherHandler.removeCallbacksAndMessages(null);
        dispatcherHandler = null;
        runner = null;
        dispatcherThread.quit();
        dispatcherThread = null;

    }


    //会务 人脸信息上报
    public static boolean publishFace(final String deviceInfo) {
        if (dispatcherHandler == null) return false;
        dispatcherHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                LogUtils.dTag(tag, "publishFaceReport  in runner.");
                if (dispatcher != null) {
                    dispatcher.publishFaceReport(deviceInfo);
                }
            }
        }, 0);
        return true;
    }
}


