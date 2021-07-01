package com.guangzhou.station;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;


import org.yczbj.ycvideoplayerlib.constant.ConstantKeys;
import org.yczbj.ycvideoplayerlib.controller.VideoPlayerController;
import org.yczbj.ycvideoplayerlib.player.VideoPlayer;


public class TestActivity extends AppCompatActivity {

    private VideoPlayer mVideoPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.me_activity_test);
        mVideoPlayer = findViewById(R.id.videoPlayer);
        VideoPlayerController mController = new VideoPlayerController(this);
        mController.setCenterPlayer(true, 0);
        mController.setLoadingType(ConstantKeys.Loading.LOADING_RING);
        mVideoPlayer.setPlayerType(ConstantKeys.IjkPlayerType.TYPE_IJK);
        mVideoPlayer.setController(mController);
        String urls = "http://10.10.20.240:9997/uploads/video/1.mp4";

        mVideoPlayer.setUp(urls, null);
//        String url = "http://10.10.20.240:9997" +/uploads/video/1.mp4

    }
}
