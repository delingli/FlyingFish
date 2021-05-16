package com.dc.module_bbs.preview;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.dc.baselib.mvvm.AbsLifecycleActivity;
import com.dc.baselib.statusBar.StarusBarUtils;
import com.dc.module_bbs.R;

import java.util.Arrays;

public class PreViewActivity extends AbsLifecycleActivity<PreViewViewModel> {

    private String[] picUrls;
    private int pos;
    private CustomViewPager mViewPager;
    private CustomPagerAdapter mCustomPagerAdapter;

    @Override
    protected int getLayout() {
        return R.layout.proj_activity_preview;
    }

    public static void startActivity(Context context, String[] pics, int pos) {
        Intent intent = new Intent(context, PreViewActivity.class);
        intent.putExtra(KEY_PIC_POS, pos);
        intent.putExtra(KEY_PIC_URL, pics);
        context.startActivity(intent);
    }

    public static String KEY_PIC_URL = "key_pic_url";
    public static String KEY_PIC_POS = "key_pic_pos";

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        setmToolBarlheadHide(true);
        if (getIntent() != null) {
            picUrls = getIntent().getStringArrayExtra(KEY_PIC_URL);
            pos = getIntent().getIntExtra(KEY_PIC_POS, 0);
        }
        mViewPager = findViewById(R.id.viewPager);
        mCustomPagerAdapter = new CustomPagerAdapter(Arrays.asList(picUrls), PreViewActivity.this);
        mViewPager.setAdapter(mCustomPagerAdapter);
        mViewPager.setCurrentItem(pos);
        mViewPager.startLoop();
        StarusBarUtils.setStatusBarColor(this, R.color.black);
    }

    @Override
    protected void initData() {

    }
}
