package com.guangzhou.station.playinfo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Parcelable;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;


import com.blankj.utilcode.util.LogUtils;
import com.bumptech.glide.Glide;
import com.dc.baselib.mvvm.AbsLifecycleActivity;
import com.guangzhou.station.R;

import org.yczbj.ycvideoplayerlib.manager.VideoPlayerManager;
import org.yczbj.ycvideoplayerlib.player.VideoPlayer;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;


public class PlayInfoActivity extends AbsLifecycleActivity<PlayInfoViewModel> {
    public static String PLAYINFO_TAG = "playInfo_tag";
    public static String PLAY_AUTO = "playInfo_auto";

    private ViewPager mViewPager;
    private List<AbsPlayInfo> mPlayInfoList;
    private CustomPagerAdapter mCustomPagerAdapter;
    public boolean mAutuPlay = false;//默认手动播放

    public static void startActivity(Context context, List<AbsPlayInfo> list, boolean mAutuPlay) {
        Intent intent = new Intent(context, PlayInfoActivity.class);
        intent.putParcelableArrayListExtra(PLAYINFO_TAG, (ArrayList<? extends Parcelable>) list);
        intent.putExtra(PLAY_AUTO, mAutuPlay);
        context.startActivity(intent);
    }

    private CustomHandler mCustomHandler = new CustomHandler(this);

    private class CustomHandler extends Handler {
        private WeakReference<PlayInfoActivity> mActivity;

        public CustomHandler(PlayInfoActivity mActivity) {
            this.mActivity = new WeakReference<>(mActivity);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            PlayInfoActivity playInfoActivity = mActivity.get();
            if (msg.what == CONFIG_VIDEOBANNER && playInfoActivity != null) {
                LogUtils.d("LDL", "\nmViewPager.getChildCount:" + mViewPager.getChildCount() + "\nmViewPager.getCurrentItem:" + mViewPager.getCurrentItem());
                if (mViewPager.getChildCount() > 1) {
                    int next = (mViewPager.getCurrentItem() + 1) % mCustomPagerAdapter.getmList().size();
                    LogUtils.d("LDL", "next:" + next);
                    mViewPager.setCurrentItem(next);
//                    AbsPlayInfo absPlayInfo = mCustomPagerAdapter.getmList().get(next);
//                    if (absPlayInfo != null) {
//                        mCustomHandler.sendEmptyMessageDelayed(CONFIG_VIDEOBANNER, absPlayInfo.timer * 1000);//延迟
//                    }
                }
            }

        }
    }


    @Override
    protected Class<PlayInfoViewModel> getViewModel() {
        return PlayInfoViewModel.class;
    }

    @Override
    protected int getLayout() {
        return R.layout.station_activity_playinfo;
    }

    public static int CONFIG_VIDEOBANNER = 10;

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        setToolBarlheadHide(true);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        //去掉最上面时间、电量等
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mViewPager = findViewById(R.id.viewPager);
        if (getIntent() != null) {
            mPlayInfoList = getIntent().getParcelableArrayListExtra(PLAYINFO_TAG);
            mAutuPlay = getIntent().getBooleanExtra(PLAY_AUTO, false);
        }
        mViewPager.setAdapter(mCustomPagerAdapter = new CustomPagerAdapter(this, mPlayInfoList));
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (mAutuPlay) {
                    if (null != mCustomPagerAdapter) {
                        notifyItem();
                    }
                    LogUtils.d("LDL", "当前的position" + position);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == 1) {//手指触摸
                    if (mAutuPlay) {
                        mCustomHandler.removeMessages(CONFIG_VIDEOBANNER);
                        LogUtils.d("LDL", "移除");

                    }
                } else if (state == 0) {//滑动结束
                    if (mAutuPlay) {
                        int currentItemPos = mViewPager.getCurrentItem() % mCustomPagerAdapter.getmList().size();
                        AbsPlayInfo absPlayInfo = mCustomPagerAdapter.getmList().get(currentItemPos);
                        mCustomHandler.sendEmptyMessageDelayed(CONFIG_VIDEOBANNER, absPlayInfo.timer * 1000);
                        LogUtils.d("LDL", "onPageScrollStateChanged:滑动结束:" + currentItemPos + "mViewPager.getCurrentItem() :" + mViewPager.getCurrentItem());
                    }

                }

            }
        });

    }

    public void notifyItem() {
        View view = mViewPager.findViewWithTag(mViewPager.getCurrentItem());
        if (view instanceof VideoPlayer) {
            VideoPlayer videoPlayer = (VideoPlayer) view;
            videoPlayer.start();
            if (!mAutuPlay) {
                videoPlayer.setlooping(true);
            }

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        VideoPlayerManager.instance().pauseVideoPlayer();
        if (mAutuPlay) {
            if (mCustomHandler != null) {
                mCustomHandler.removeMessages(CONFIG_VIDEOBANNER);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        VideoPlayerManager.instance().resumeVideoPlayer();
        if (mAutuPlay) {//注意自动播放才开始哦
            int currentItem = mViewPager.getCurrentItem();
            AbsPlayInfo absPlayInfo = mCustomPagerAdapter.getmList().get(currentItem);
            mCustomHandler.sendEmptyMessageDelayed(CONFIG_VIDEOBANNER, absPlayInfo.timer * 1000);//延迟
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        VideoPlayerManager.instance().releaseVideoPlayer();
        if (mCustomHandler != null) {
            mCustomHandler.removeCallbacksAndMessages(null);
        }
    }

    @Override

    protected void initData() {

    }
}
