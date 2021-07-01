package com.guangzhou.station.playinfo;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;

import com.blankj.utilcode.util.LogUtils;
import com.bumptech.glide.Glide;
import com.dc.baselib.constant.Constants;
import com.guangzhou.station.R;

import org.jetbrains.annotations.NotNull;
import org.yczbj.ycvideoplayerlib.constant.ConstantKeys;
import org.yczbj.ycvideoplayerlib.controller.VideoPlayerController;
import org.yczbj.ycvideoplayerlib.player.VideoPlayer;

import java.io.File;
import java.util.List;


public class CustomPagerAdapter extends PagerAdapter {
    private List<AbsPlayInfo> mList;
    private Context context;

    public CustomPagerAdapter(Context context, List<AbsPlayInfo> mList) {
        this.mList = mList;
        this.context = context;
    }

    @NonNull
    @NotNull
    @Override
    public Object instantiateItem(@NonNull @NotNull ViewGroup container, int position) {
        LogUtils.d("CustomPagerAdapter", position);
        AbsPlayInfo absPlayInfo = mList.get(position);
        if (absPlayInfo != null) {
            if (absPlayInfo.type == 1) {
                View view = View.inflate(context, R.layout.station_item_img_play, null);
                ImageView imageView = view.findViewById(R.id.imageView);
                Glide.with(context).load(Constants.WEB_URL + File.separator + absPlayInfo.path).into(imageView);
                container.addView(view);
                return view;
            } else {
                VideoPlayer videoplayer = (VideoPlayer) View.inflate(context, R.layout.station_custom_video, null);
                VideoPlayerController mController = new VideoPlayerController(context.getApplicationContext());
                mController.setCenterPlayer(true, 0);
                videoplayer.setTag(position);
                mController.setLoadingType(ConstantKeys.Loading.LOADING_RING);
                videoplayer.setPlayerType(ConstantKeys.IjkPlayerType.TYPE_IJK);
                videoplayer.setController(mController);
                videoplayer.setUp(Constants.WEB_URL + File.separator + absPlayInfo.path, null);
                container.addView(videoplayer);
                return videoplayer;
            }
        }
        return null;
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public void destroyItem(@NonNull @NotNull ViewGroup container, int position, @NonNull @NotNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(@NonNull @NotNull View view, @NonNull @NotNull Object object) {
        return view == object;
    }



}
