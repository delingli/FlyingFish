package com.dc.commonlib.commonentity.video;

import com.dc.commonlib.commonentity.video.widget.VideoDisplayView;

import org.yczbj.ycvideoplayerlib.player.VideoPlayer;

public class DisplayVideoPlayerManager {

    private VideoDisplayView mVideoDisplay;
    private static volatile DisplayVideoPlayerManager sInstance;

    private DisplayVideoPlayerManager() {
    }

    /**
     * 一定要使用单例模式，保证同一时刻只有一个视频在播放，其他的都是初始状态
     * 单例模式
     *
     * @return VideoPlayerManager对象
     */
    public static DisplayVideoPlayerManager instance() {
        if (sInstance == null) {
            synchronized (DisplayVideoPlayerManager.class) {
                if (sInstance == null) {
                    sInstance = new DisplayVideoPlayerManager();
                }
            }
        }
        return sInstance;
    }


    /**
     * 获取对象
     *
     * @return VideoPlayerManager对象
     */
    public VideoDisplayView getCurrentVideoPlayer() {
        return mVideoDisplay;
    }


    /**
     * 设置VideoPlayer
     *
     * @param videoPlayer VideoPlayerManager对象
     */
    public void setCurrentVideoPlayer(VideoDisplayView videoPlayer) {
        if (mVideoDisplay != videoPlayer) {
            releaseVideoPlayer();
            mVideoDisplay = videoPlayer;
        }

    }


    /**
     * 当视频正在播放或者正在缓冲时，调用该方法暂停视频
     */
    public void suspendVideoPlayer() {
        if (mVideoDisplay != null) {
            mVideoDisplay.onStop();

        }
    }


    /**
     * 当视频暂停时或者缓冲暂停时，调用该方法重新开启视频播放
     */
    public void resumeVideoPlayer() {
        if (mVideoDisplay != null) {
            mVideoDisplay.onResume();
        }
    }


    /**
     * 释放，内部的播放器被释放掉，同时如果在全屏、小窗口模式下都会退出
     */
    public void releaseVideoPlayer() {
        if (mVideoDisplay != null) {
            mVideoDisplay.showCenterPlay();
            mVideoDisplay.showPlaceHolder();
            mVideoDisplay.release();
            mVideoDisplay = null;
        }
    }


    /**
     * 处理返回键逻辑
     * 如果是全屏，则退出全屏
     * 如果是小窗口，则退出小窗口
     */
    public boolean onBackPressed() {
        if (mVideoDisplay != null) {
            if (mVideoDisplay.isFullScreen()) {
                return mVideoDisplay.exitFullScreen();
            }
        }
        return false;
    }


}
