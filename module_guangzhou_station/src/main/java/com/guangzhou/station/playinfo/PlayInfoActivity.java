package com.guangzhou.station.playinfo;

import android.app.Activity;
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

    public static void startActivity(Activity context, List<AbsPlayInfo> list, boolean mAutuPlay, int requestCode) {
        Intent intent = new Intent(context, PlayInfoActivity.class);
        intent.putParcelableArrayListExtra(PLAYINFO_TAG, (ArrayList<? extends Parcelable>) list);
        intent.putExtra(PLAY_AUTO, mAutuPlay);
        context.startActivityForResult(intent, requestCode);
    }

    private CustomHandler mCustomHandler = new CustomHandler(PlayInfoActivity.this);
    public static int BANNER_NEXT = 1;
    public static int BANNER_PAUSE = 2;
    public static int BANNER_RESUME = 3;

    public static class CustomHandler extends Handler {
        private WeakReference<PlayInfoActivity> mActivity;

        public CustomHandler(PlayInfoActivity mActivity) {
            this.mActivity = new WeakReference<>(mActivity);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            PlayInfoActivity playInfoActivity = mActivity.get();
            if (playInfoActivity == null) {
                return;
            }
            if (playInfoActivity.mCustomHandler.hasMessages(BANNER_NEXT)) {     //如果已经有消息了，先移除消息
                playInfoActivity.mCustomHandler.removeMessages(BANNER_NEXT);
            }
            int whats = msg.what;
            if (whats == BANNER_NEXT) {
                if (playInfoActivity.mCustomPagerAdapter.getmList() != null && playInfoActivity.mCustomPagerAdapter.getmList().size() > 1) {
                    int next = (playInfoActivity.mViewPager.getCurrentItem() + 1);
                    LogUtils.d("LDL", "next:" + next);
                    playInfoActivity.mViewPager.setCurrentItem(next);
                }
                //5秒后继续轮播
//                playInfoActivity.mCustomHandler.sendEmptyMessageDelayed(BANNER_NEXT, 5000);

            } else if (whats == BANNER_PAUSE) {

            } else if (whats == BANNER_RESUME) {
                //继续轮播
                playInfoActivity.mCustomHandler.sendEmptyMessageDelayed(BANNER_NEXT, 5000);
            }
/*            if (msg.what == CONFIG_VIDEOBANNER && playInfoActivity != null) {
                LogUtils.d("LDL", "\nmViewPager.getChildCount:" + mViewPager.getChildCount() + "\nmViewPager.getCurrentItem:" + mViewPager.getCurrentItem());
                if (mViewPager.getChildCount() > 1) {
                    int next = (mViewPager.getCurrentItem() + 1);
                    LogUtils.d("LDL", "next:" + next);
                    mViewPager.setCurrentItem(next);
                }
            }*/

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

    private boolean first = true;

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
        findViewById(R.id.iv_goBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if (getIntent() != null) {
            mPlayInfoList = getIntent().getParcelableArrayListExtra(PLAYINFO_TAG);
            mAutuPlay = getIntent().getBooleanExtra(PLAY_AUTO, false);
        }
        mViewPager.setAdapter(mCustomPagerAdapter = new CustomPagerAdapter(this, mAutuPlay, mCustomHandler, mPlayInfoList));
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                int realposition = position % mCustomPagerAdapter.getmList().size();
                LogUtils.d("LDL", "当前的position" + position + "realposition:" + realposition);
                if (null != mCustomPagerAdapter) {
                    notifyItem(position, realposition);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                switch (state) {
                    case ViewPager.SCROLL_STATE_DRAGGING:
                        //用户正在滑动，暂停轮播
                        mCustomHandler.sendEmptyMessage(BANNER_PAUSE);
                        break;
                    case ViewPager.SCROLL_STATE_IDLE:
                        //滑动结束，继续轮播
                        if (mAutuPlay) {
                            if (null != mCustomPagerAdapter.getmList() && mCustomPagerAdapter.getmList().size() > 1) {
                                int realposition = mViewPager.getCurrentItem() % mCustomPagerAdapter.getmList().size();
                                AbsPlayInfo absPlayInfo = mCustomPagerAdapter.getmList().get(realposition);
                                if (first) {
                                    first = false;
                                } else {
                                    if (absPlayInfo.type == 1) {
                                        first = false;
                                        mCustomHandler.sendEmptyMessageDelayed(BANNER_NEXT, absPlayInfo.timer * 1000);
                                    } else {
                                        mCustomHandler.sendEmptyMessage(BANNER_PAUSE);
                                    }
                                }


                            } else {
                                mCustomHandler.sendEmptyMessage(BANNER_PAUSE);
                            }
                        }
                        break;
                }

            }
        });


        mViewPager.setCurrentItem(0);
        if (mAutuPlay) {
            if (null != mCustomPagerAdapter.getmList() && mCustomPagerAdapter.getmList().size() > 1) {
  /*              AbsPlayInfo absPlayInfo = mCustomPagerAdapter.getmList().get(0);
                if (absPlayInfo.type == 1) {
                    mCustomHandler.sendEmptyMessageDelayed(BANNER_NEXT, absPlayInfo.timer * 1000);
                } else {
                    mCustomHandler.sendEmptyMessage(BANNER_PAUSE);
                }*/
            } else {
                mCustomHandler.sendEmptyMessage(BANNER_PAUSE);
            }
        } else {
            mCustomHandler.sendEmptyMessage(BANNER_PAUSE);
        }
    }

    public void notifyItem(int position, int realposition) {
        View view = mViewPager.findViewWithTag(position);
        if (view instanceof VideoPlayer) {
            if (VideoPlayerManager.instance().getCurrentVideoPlayer() != null && VideoPlayerManager.instance().getCurrentVideoPlayer().isPaused()) {
                VideoPlayerManager.instance().resumeVideoPlayer();
                return;
            }
            VideoPlayer videoPlayer = (VideoPlayer) view;
            videoPlayer.start();

            mCustomHandler.sendEmptyMessage(BANNER_PAUSE);
        } else {
            VideoPlayerManager.instance().pauseVideoPlayer();
            VideoPlayerManager.instance().releaseVideoPlayer();
            if (mAutuPlay) {
                AbsPlayInfo absPlayInfo = mCustomPagerAdapter.getmList().get(realposition);
                mCustomHandler.sendEmptyMessageDelayed(BANNER_NEXT, absPlayInfo.timer * 1000);
            }
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        VideoPlayerManager.instance().pauseVideoPlayer();
        if (mAutuPlay) {
            if (mCustomHandler != null) {
                mCustomHandler.sendEmptyMessage(BANNER_PAUSE);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        VideoPlayerManager.instance().resumeVideoPlayer();
        if (mAutuPlay) {
            if (mCustomHandler != null) {
                int realposition = mViewPager.getCurrentItem() % mCustomPagerAdapter.getmList().size();
                if (mCustomPagerAdapter.getmList() != null && !mCustomPagerAdapter.getmList().isEmpty()) {
                    AbsPlayInfo absPlayInfo = mCustomPagerAdapter.getmList().get(realposition);
                    if (absPlayInfo.type == 1) {
                        mCustomHandler.sendEmptyMessageDelayed(BANNER_NEXT, absPlayInfo.timer * 1000);
                    }
                }


            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        VideoPlayerManager.instance().releaseVideoPlayer();
        if (mCustomHandler != null) {
            mCustomHandler.removeCallbacksAndMessages(null);
        }
        mCustomHandler.removeCallbacksAndMessages(null);
        mCustomHandler = null;
        setResult(Activity.RESULT_OK);
    }

    @Override

    protected void initData() {

    }
}
