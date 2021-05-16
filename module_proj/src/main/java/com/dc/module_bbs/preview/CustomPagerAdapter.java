package com.dc.module_bbs.preview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.dc.commonlib.utils.GlideUtils;
import com.dc.module_bbs.R;
import com.github.chrisbanes.photoview.PhotoView;

import java.util.List;

public class CustomPagerAdapter extends PagerAdapter {
    private List<String> urls;
    private Context context;

    public CustomPagerAdapter(List<String> urls, Context context) {
        this.urls = urls;
        this.context = context;
    }

    @Override
    public int getCount() {
        return urls == null ? 0 : urls.size();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        String itemUrl = urls.get(position);
        View view = View.inflate(context, R.layout.proj_item_preview, null);
        PhotoView photoView = view.findViewById(R.id.photoView);
        GlideUtils.loadUrl(context, itemUrl, photoView);
        container.addView(view);
        return view;
    }
//    public void setCurrentItem(int position) {
//        if (position != RecyclerView.NO_POSITION) {
//            mViewpager.setCurrentItem(position);
//        }
//    }
    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
