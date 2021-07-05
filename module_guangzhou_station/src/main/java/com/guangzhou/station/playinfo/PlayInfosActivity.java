package com.guangzhou.station.playinfo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.view.WindowManager;

import com.blankj.utilcode.util.LogUtils;
import com.dc.baselib.mvvm.AbsLifecycleActivity;
import com.guangzhou.station.R;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnPageChangeListener;

import org.yczbj.ycvideoplayerlib.manager.VideoPlayerManager;
import org.yczbj.ycvideoplayerlib.player.VideoPlayer;

import java.util.ArrayList;
import java.util.List;


public class PlayInfosActivity extends AbsLifecycleActivity<PlayInfoViewModel> {
    public static String PLAYINFO_TAG = "playInfo_tag";
    public static String PLAY_AUTO = "playInfo_auto";

    private List<AbsPlayInfo> mPlayInfoList;
    private CustomPagerAdapter mCustomPagerAdapter;
    public boolean mAutoPlay = false;//默认手动播放
    private Banner mBanner;

    public static void startActivity(Context context, List<AbsPlayInfo> list, boolean mAutuPlay) {
        Intent intent = new Intent(context, PlayInfosActivity.class);
        intent.putParcelableArrayListExtra(PLAYINFO_TAG, (ArrayList<? extends Parcelable>) list);
        intent.putExtra(PLAY_AUTO, mAutuPlay);
        context.startActivity(intent);
    }

    @Override
    protected Class<PlayInfoViewModel> getViewModel() {
        return PlayInfoViewModel.class;
    }

    @Override
    protected int getLayout() {
        return R.layout.station_activity_playinfos;
    }

    private ImgVideoAdapter mImgVideoAdapter;

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        setToolBarlheadHide(true);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        //去掉最上面时间、电量等
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        findViewById(R.id.iv_goBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mBanner = findViewById(R.id.banner);
        if (getIntent() != null) {
            mPlayInfoList = getIntent().getParcelableArrayListExtra(PLAYINFO_TAG);
            mAutoPlay = getIntent().getBooleanExtra(PLAY_AUTO, false);
        }


        mBanner.setAdapter(mImgVideoAdapter = new ImgVideoAdapter(mAutoPlay, this, mPlayInfoList)).addBannerLifecycleObserver(this).addOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                LogUtils.d("xiaoman", position);
//                if(mImgVideoAdapter.getRealCount()>)
                if (mAutoPlay && mImgVideoAdapter.getRealData(position) != null) {
                    AbsPlayInfo data = mImgVideoAdapter.getRealData(position);
                    mBanner.setLoopTime(data.timer * 1000);
                    mBanner.post(new Runnable() {
                        @Override
                        public void run() {
                            if (null != mImgVideoAdapter) {
                                notifyItem(position);
                            }
                        }
                    });

                } else if (!mAutoPlay) {
                    mBanner.post(new Runnable() {
                        @Override
                        public void run() {
                            if (null != mImgVideoAdapter) {
                                notifyItem(position);
                            }
                        }
                    });
                }


            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mBanner.isAutoLoop(mAutoPlay);
        if (mAutoPlay && mImgVideoAdapter.getRealCount() > 1) {
            if (mImgVideoAdapter.getRealData(0) != null) {
                AbsPlayInfo realData = mImgVideoAdapter.getRealData(0);
                mBanner.setLoopTime(realData.timer * 1000);
            }

        }

    }

    public void notifyItem(int pos) {
        LogUtils.d("BACK", "notifyItem:" + pos);
        View view = mBanner.findViewWithTag(pos);
        if (view instanceof VideoPlayer) {
            VideoPlayer videoPlayer = (VideoPlayer) view;
            videoPlayer.addOnCpmpleteListener(new VideoPlayer.OnCpmpleteListener() {
                @Override
                public void onComplate() {
                    if (!mAutoPlay) {
                        videoPlayer.restart();
                    }
                }
            });

            videoPlayer.start();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        VideoPlayerManager.instance().resumeVideoPlayer();
        //播
        if (mAutoPlay) {
            mBanner.start();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        VideoPlayerManager.instance().pauseVideoPlayer();
        if (mAutoPlay) {
            mBanner.stop();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        VideoPlayerManager.instance().releaseVideoPlayer();
        if (mBanner != null) {
            mBanner.stop();
        }

    }

    @Override

    protected void initData() {

    }
}
