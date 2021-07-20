package com.guangzhou.station.playinfo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.blankj.utilcode.util.LogUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.dc.baselib.mvvm.AbsLifecycleActivity;
import com.guangzhou.station.R;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.listener.OnPageChangeListener;

import org.yczbj.ycvideoplayerlib.manager.VideoPlayerManager;
import org.yczbj.ycvideoplayerlib.player.VideoPlayer;

import java.util.ArrayList;
import java.util.List;


public class PlayInfosActivity extends AbsLifecycleActivity<PlayInfoViewModel> {
    public static String PLAYINFO_TAG = "playInfo_tag";
    public static String PLAY_AUTO = "playInfo_auto";
    public static String TAG = "PlayInfosActivity";
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
    public int CURRENTPOSITION = 0;
    boolean check = false;

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
//        mBanner.getViewPager2().setOffscreenPageLimit(1);
        LogUtils.d(TAG, "是否自动播放:?" + mAutoPlay);
        RequestManager requestManager = Glide.with(this);
        mBanner.setAdapter(mImgVideoAdapter = new ImgVideoAdapter(mAutoPlay, mBanner, requestManager, this, mPlayInfoList)).addBannerLifecycleObserver(this).addOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                LogUtils.d(TAG, "CURRENTPOSITION:" + position + " mBanner.getCurrentItem():" + mBanner.getCurrentItem());
                if (mAutoPlay && mImgVideoAdapter.getData(position) != null) {//自动播放

                    mBanner.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            AbsPlayInfo data = mImgVideoAdapter.getData(position);
                            if (null != mImgVideoAdapter) {
                                notifyItem(position, data);
                            }
                        }
                    }, 600);

                }


            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mBanner.stop();
        mBanner.isAutoLoop(mAutoPlay);
        if (mAutoPlay && mImgVideoAdapter.getRealCount() > 1) {
            if (mImgVideoAdapter.getData(0) != null) {
                AbsPlayInfo realData = mImgVideoAdapter.getData(0);
                if (realData.type == 1) {
                    mBanner.isAutoLoop(mAutoPlay);
                    mBanner.setLoopTime(realData.timer * 1000);
                    mBanner.start();
                } else {
                    mBanner.stop();
                    mBanner.isAutoLoop(false);
                }
            }

        }
    }

    public void notifyItem(int pos, AbsPlayInfo data) {
        LogUtils.d("BACK", "notifyItem:" + pos);
        View view = mBanner.findViewWithTag(pos);
        if (view instanceof VideoPlayer) {
            mBanner.stop();
            mBanner.isAutoLoop(false);
            LogUtils.d(TAG, "notifyItem:" + pos + "视频");
            VideoPlayer videoPlayer = (VideoPlayer) view;
            videoPlayer.pause();
            videoPlayer.postDelayed(new Runnable() {
                @Override
                public void run() {
                    videoPlayer.start(0);
                    LogUtils.d(TAG, "notifyItem:" + pos + "视频");
                }
            },500);
        } else if (view instanceof ImageView) {
            LogUtils.d(TAG, "notifyItem:" + pos + "图片");
            mBanner.stop();
            mBanner.isAutoLoop(mAutoPlay);
            mBanner.setLoopTime(data.timer * 1000);//图片
            mBanner.start();

        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        VideoPlayerManager.instance().resumeVideoPlayer();//播放走监听
        //播
//        if (mAutoPlay /*&& mImgVideoAdapter.getRealData(CURRENTPOSITION) != null*/) {
//            mBanner.start();
///*            AbsPlayInfo realData = mImgVideoAdapter.getRealData(CURRENTPOSITION);
//            if (realData.type == 1) {//图片
//                mBanner.stop();
////                mBanner.setLoopTime(realData.timer * 1000);
////                mBanner.isAutoLoop(true);
////                mBanner.start();
//            } else {
//                mBanner.stop();
//                mBanner.isAutoLoop(false);//暂停
//            }*/
//
//        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        VideoPlayerManager.instance().pauseVideoPlayer();
//        if (mAutoPlay) {
//            mBanner.stop();
//        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        VideoPlayerManager.instance().releaseVideoPlayer();
        if (mBanner != null) {
            mBanner.destroy();
        }

    }

    @Override

    protected void initData() {

    }
}
