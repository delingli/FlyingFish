package com.dc.commonlib.common;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.dc.baselib.mvvm.BaseFragment;

import java.util.List;

public class RecordPagerAdapter extends FragmentStatePagerAdapter {
    private List<BaseFragment> fragments;
    private String[] titles;

    public RecordPagerAdapter(String[] titles, List<BaseFragment> fragments, FragmentManager fm) {
        super(fm);
        this.titles = titles;
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int i) {
        return fragments.get(i);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

    @Override
    public int getCount() {
        return fragments == null ? 0 : fragments.size();
    }
}
