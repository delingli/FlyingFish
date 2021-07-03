package com.guangzhou.station.playinfo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;

import com.blankj.utilcode.util.LogUtils;
import com.dc.baselib.mvvm.AbsLifecycleActivity;
import com.guangzhou.station.R;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnPageChangeListener;

import org.yczbj.ycvideoplayerlib.manager.VideoPlayerManager;
import org.yczbj.ycvideoplayerlib.player.VideoPlayer;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;


public class PlayInfosActivity extends AbsLifecycleActivity<PlayInfoViewModel> {
    public static String PLAYINFO_TAG = "playInfo_tag";
    public static String PLAY_AUTO = "playInfo_auto";

    private List<AbsPlayInfo> mPlayInfoList;
    private CustomPagerAdapter mCustomPagerAdapter;
    public boolean mAutuPlay = false;//默认手动播放
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
        mBanner = findViewById(R.id.banner);
        if (getIntent() != null) {
            mPlayInfoList = getIntent().getParcelableArrayListExtra(PLAYINFO_TAG);
            mAutuPlay = getIntent().getBooleanExtra(PLAY_AUTO, false);
        }


        mBanner.setAdapter(mImgVideoAdapter = new ImgVideoAdapter(this, mPlayInfoList)).addBannerLifecycleObserver(this).addOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                LogUtils.d("xiaoman", position);
//                if(mImgVideoAdapter.getRealCount()>)
                if (mAutuPlay && mImgVideoAdapter.getRealData(position) != null) {
                    AbsPlayInfo data = mImgVideoAdapter.getRealData(position);
                        mBanner.setLoopTime(data.timer * 1000);

                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mBanner.isAutoLoop(mAutuPlay);
        if (mAutuPlay&&mImgVideoAdapter.getRealCount()>1) {
            if(mImgVideoAdapter.getRealData(0)!=null){
                AbsPlayInfo realData = mImgVideoAdapter.getRealData(0);
                mBanner.setLoopTime(realData.timer * 1000);
            }

        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        VideoPlayerManager.instance().resumeVideoPlayer();
        //播
        if (mAutuPlay) {
            mBanner.start();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        VideoPlayerManager.instance().pauseVideoPlayer();
        if (mAutuPlay) {
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