package com.guangzhou.station.playinfo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.dc.baselib.constant.Constants;
import com.guangzhou.station.R;
import com.youth.banner.adapter.BannerAdapter;

import org.jetbrains.annotations.NotNull;
import org.yczbj.ycvideoplayerlib.constant.ConstantKeys;
import org.yczbj.ycvideoplayerlib.controller.VideoPlayerController;
import org.yczbj.ycvideoplayerlib.player.VideoPlayer;

import java.io.File;
import java.util.List;


public class ImgVideoAdapter extends BannerAdapter<AbsPlayInfo, ImgVideoAdapter.BasicBannerViewHolder> {

    private LayoutInflater mInflater;
    private Context context;
    private boolean mAutoPlay;
    private RequestManager requestManager;

    public ImgVideoAdapter(boolean mAutoPlay, RequestManager requestManager, Context context, List<AbsPlayInfo> datas) {
        super(datas);
        this.context = context;
        this.mAutoPlay = mAutoPlay;
        this.requestManager = requestManager;
        mInflater = LayoutInflater.from(context);
    }


    @Override
    public int getItemViewType(int position) {
        return getRealData(position).type;
    }

    @Override
    public BasicBannerViewHolder onCreateHolder(ViewGroup parent, int viewType) {
        if (viewType == 1) {
            View inflate = mInflater.inflate(R.layout.station_item_img_play, parent, false);
            return new ImgBannerViewHolder(inflate);
        } else {
            View inflate = mInflater.inflate(R.layout.station_custom_video, parent, false);
            return new VideoViewHolder(inflate);
        }
    }

    @Override
    public void onBindView(BasicBannerViewHolder holder, AbsPlayInfo data, int position, int size) {
        if (holder instanceof ImgBannerViewHolder) {
            ImgBannerViewHolder imgBannerViewHolder = (ImgBannerViewHolder) holder;
            requestManager.load(Constants.WEB_URL + File.separator + data.path).into(imgBannerViewHolder.imageViews);
        } else {
            VideoViewHolder videoViewHolder = (VideoViewHolder) holder;
            videoViewHolder.videoPlayer.setUp(Constants.WEB_URL + File.separator + data.path, null);
            videoViewHolder.videoPlayer.setTag(position);
            if (position == 0) {
                videoViewHolder.videoPlayer.start();
                videoViewHolder.videoPlayer.addOnCpmpleteListener(new VideoPlayer.OnCpmpleteListener() {
                    @Override
                    public void onComplate() {
                        if (!mAutoPlay) {
                            videoViewHolder.videoPlayer.restart();
                        }
                    }
                });
            }

        }

    }


    class ImgBannerViewHolder extends BasicBannerViewHolder {
        ImageView imageViews;

        public ImgBannerViewHolder(@NonNull View view) {
            super(view);
            imageViews = itemView.findViewById(R.id.imageView);
        }
    }

    class VideoViewHolder extends BasicBannerViewHolder {
        VideoPlayer videoPlayer;

        public VideoViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            videoPlayer = itemView.findViewById(R.id.videoPlayer);
            VideoPlayerController mController = new VideoPlayerController(context.getApplicationContext());
            mController.setCenterPlayer(true, 0);
            mController.setLoadingType(ConstantKeys.Loading.LOADING_RING);
            videoPlayer.setPlayerType(ConstantKeys.IjkPlayerType.TYPE_IJK);
//            ConstantKeys.IjkPlayerType.TYPE_NATIVE
            videoPlayer.setController(mController);
        }
    }

    class BasicBannerViewHolder extends RecyclerView.ViewHolder {


        public BasicBannerViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

        }
    }
}
