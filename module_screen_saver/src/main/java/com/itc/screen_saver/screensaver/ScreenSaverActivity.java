package com.itc.screen_saver.screensaver;

import android.os.Bundle;
import android.os.PowerManager;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.LogUtils;
import com.dc.baselib.mvvm.BaseActivity;
import com.itc.screen_saver.R;
import com.itc.screen_saver.service.ScreenSaverService;


public class ScreenSaverActivity extends BaseActivity {

    private PowerManager.WakeLock mWakeLock;
    private static String TAG = "ScreenSaverActivity";

    @Override
    protected int getLayout() {
        return R.layout.screen_activity_acreensaver;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE); //去除title
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //去状态栏
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        setToolBarlheadHide(true);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        //去掉最上面时间、电量等
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //有按下动作时取消定时
                LogUtils.d(TAG, "dispatchTouchEvent" + "ACTION_DOWN");

                break;
            case MotionEvent.ACTION_UP: {
                LogUtils.d(TAG, "dispatchTouchEvent" + "ACTION_UP");
                ScreenSaverService.startScreenSaverService(ScreenSaverActivity.this);
                finish();
            }

            break;

        }
        return super.dispatchTouchEvent(ev);
    }
}




