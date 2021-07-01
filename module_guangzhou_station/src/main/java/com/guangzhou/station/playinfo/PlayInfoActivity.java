package com.guangzhou.station.playinfo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;

import androidx.viewpager.widget.ViewPager;


import com.dc.baselib.mvvm.AbsLifecycleActivity;
import com.guangzhou.station.R;

import org.yczbj.ycvideoplayerlib.player.VideoPlayer;

import java.util.ArrayList;
import java.util.List;


public class PlayInfoActivity extends AbsLifecycleActivity<PlayInfoViewModel> {
    public static String PLAYINFO_TAG = "playInfo_tag";

    private ViewPager mViewPager;
    private List<AbsPlayInfo> mPlayInfoList;
    private CustomPagerAdapter mCustomPagerAdapter;

    public static void startActivity(Context context, List<AbsPlayInfo> list) {
        Intent intent = new Intent(context, PlayInfoActivity.class);
        intent.putParcelableArrayListExtra(PLAYINFO_TAG, (ArrayList<? extends Parcelable>) list);
        context.startActivity(intent);
    }

    @Override
    protected Class<PlayInfoViewModel> getViewModel() {
        return PlayInfoViewModel.class;
    }

    @Override
    protected int getLayout() {
        return R.layout.station_activity_playinfo;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        mViewPager = findViewById(R.id.viewPager);
        if (getIntent() != null) {
            mPlayInfoList = getIntent().getParcelableArrayListExtra(PLAYINFO_TAG);
        }
        mViewPager.setAdapter(mCustomPagerAdapter = new CustomPagerAdapter(this, mPlayInfoList));
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (null != mCustomPagerAdapter) {
                    notifyItem();
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void notifyItem() {
        View view = mViewPager.findViewWithTag(mViewPager.getCurrentItem());
        if (view instanceof VideoPlayer) {
            VideoPlayer videoPlayer = (VideoPlayer) view;
            videoPlayer.start();
        }
    }

    @Override

    protected void initData() {

    }
}
