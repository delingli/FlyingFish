package com.itc.screen_saver.screensaver;

import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;

import com.blankj.utilcode.util.LogUtils;
import com.bumptech.glide.Glide;
import com.dc.baselib.constant.Constants;
import com.dc.baselib.mvvm.AbsLifecycleActivity;
import com.itc.screen_saver.R;
import com.itc.screen_saver.service.ScreenSaverService;

import org.jetbrains.annotations.NotNull;

import java.io.File;


public class ScreenSaverActivity extends AbsLifecycleActivity<ScreenViewModul> {

    private PowerManager.WakeLock mWakeLock;
    private static String TAG = "ScreenSaverActivity";
    private ImageView mIvBg;
    private FrameLayout mFlContainer;

    @Override
    protected int getLayout() {
        return R.layout.screen_activity_acreensaver;
    }

    @Override
    protected void initData() {

        registerSubscriber(ScreenViewModul.EVENT_KEY_SCREEN_SAVER, ScreenSaverEntiry.class).observe(this, new Observer<ScreenSaverEntiry>() {
            @Override
            public void onChanged(@Nullable ScreenSaverEntiry screensaverentiry) {
                if (screensaverentiry != null) {
                    fillData(screensaverentiry);
                }
            }
        });

        registerSubscriber(ScreenViewModul.EVENT_KEY_SCREEN_SAVER_ERROR, String.class).observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String str) {
                if (str != null) {
                    LogUtils.eTag(TAG, str);
                }
            }
        });

    }

    private void fillData(ScreenSaverEntiry screensaverentiry) {
        if (screensaverentiry.getType() == 1) {
            LogUtils.dTag(TAG, screensaverentiry.getPath());
            String url = getRealUrl(screensaverentiry).trim();
            Glide.with(this).load(url).placeholder(R.drawable.word).into(mIvBg);
        } else {
            //视频
            String videoUrl = getRealUrl(screensaverentiry).trim();
            if (!TextUtils.isEmpty(videoUrl)) {
                String VLC_FRAGMENTTAG = "VlcFragment";
                getSupportFragmentManager().beginTransaction().replace(R.id.fl_container, ScreenSaverVlcFragment.getInstance(videoUrl), VLC_FRAGMENTTAG)
                .commit();

            }


        }
    }

    @NotNull
    private String getRealUrl(ScreenSaverEntiry screensaverentiry) {
        return Constants.WEB_URL + File.separator + screensaverentiry.getPath();
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
        super.initView(savedInstanceState);
        setToolBarlheadHide(true);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        //去掉最上面时间、电量等
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mIvBg = findViewById(R.id.iv_bg);
        mFlContainer = findViewById(R.id.fl_container);
        mViewModel.toFetchScreenSaverData();
    }

    @Override
    protected Class<ScreenViewModul> getViewModel() {
        return ScreenViewModul.class;
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}




