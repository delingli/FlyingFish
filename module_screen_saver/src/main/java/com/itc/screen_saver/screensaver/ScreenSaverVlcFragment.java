package com.itc.screen_saver.screensaver;

import android.annotation.TargetApi;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.itc.screen_saver.R;


import org.jetbrains.annotations.NotNull;
import org.videolan.libvlc.IVLCVout;
import org.videolan.libvlc.LibVLC;
import org.videolan.libvlc.Media;
import org.videolan.libvlc.MediaPlayer;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class ScreenSaverVlcFragment extends Fragment implements IVLCVout.OnNewVideoLayoutListener {

    //private static final String TAG = "JavaActivity";
    private static final int SURFACE_BEST_FIT = 0;
    private static final int SURFACE_FIT_SCREEN = 1;
    private static final int SURFACE_FILL = 2;
    private static final int SURFACE_16_9 = 3;
    private static final int SURFACE_4_3 = 4;
    private static final int SURFACE_ORIGINAL = 5;
    private static int CURRENT_SIZE = SURFACE_BEST_FIT;
    // private static int CURRENT_SIZE = SURFACE_FIT_SCREEN;
    private FrameLayout mVideoSurfaceFrame = null;
    private SurfaceView mVideoSurface = null;
    private final Handler mHandler = new Handler();
    private View.OnLayoutChangeListener mOnLayoutChangeListener = null;
    private LibVLC mLibVLC = null;
    private MediaPlayer mMediaPlayer = null;
    private int mVideoHeight = 0;
    private int mVideoWidth = 0;
    private int mVideoVisibleHeight = 0;
    private int mVideoVisibleWidth = 0;
    private int mVideoSarNum = 0;
    private int mVideoSarDen = 0;
    /***
     * 是否在绘制:用于关闭子线程:true则表示一直循环
     */
    private boolean isDrawing = true;
    private SurfaceHolder holder;


    private boolean isError = false;//标识当前是否有error
    private float positionChanged;

    private int errorTimer = 60 * 1000; // 一分钟反复一次
    private Timer timerErrors;
    private View rootv;
    private String sample_url;

    public static ScreenSaverVlcFragment getInstance(String playurl) {
        ScreenSaverVlcFragment screenSaverVlcFragment = new ScreenSaverVlcFragment();
        Bundle bundle = new Bundle();
        bundle.putString("videoUrl", playurl);
        screenSaverVlcFragment.setArguments(bundle);
        return screenSaverVlcFragment;
    }

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        rootv = inflater.inflate(R.layout.screen_vlcfragment, null);

        return rootv;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final ArrayList<String> args = new ArrayList<>();
        args.add("--aout=opensles");
        args.add("--audio-time-stretch"); // time stretching
        args.add("-vvv");

        mLibVLC = new LibVLC(getActivity(), args);
        mMediaPlayer = new MediaPlayer(mLibVLC);
        mVideoSurfaceFrame = view.findViewById(R.id.video_surface_frame);
        mVideoSurface = view.findViewById(R.id.video_surface);
        mVideoSurface.setZOrderOnTop(true);
        mVideoSurface.getHolder().setFormat(PixelFormat.TRANSLUCENT);
        mVideoSurface.setZOrderMediaOverlay(true);
        holder = mVideoSurface.getHolder();
        if (getArguments() != null) {
            sample_url = getArguments().getString("videoUrl", null);
        } else {
            getActivity().finish();
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mOnLayoutChangeListener != null) {
            mVideoSurfaceFrame.removeOnLayoutChangeListener(mOnLayoutChangeListener);
            mOnLayoutChangeListener = null;
        }

        final IVLCVout vout = mMediaPlayer.getVLCVout();
        vout.detachViews();
        mMediaPlayer.stop();
        mLibVLC.release();
        mMediaPlayer.release();
        mLibVLC = null;
        if (timerErrors != null) {
            timerErrors.cancel();
            timerErrors = null;
        }


    }


    @Override
    public void onStart() {
        super.onStart();
        new Thread(new Runnable() {
            @Override
            public void run() {

                    initVideo();
                    startLayout();

            }
        }).start();
    }

    private void startLayout() {
        mMediaPlayer.setEventListener(new MediaPlayer.EventListener() {
            @Override
            public void onEvent(MediaPlayer.Event event) {
                switch (event.type) {
                    case MediaPlayer.Event.EndReached:

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                if (mMediaPlayer != null) {
                                    mMediaPlayer.stop();
                                    mMediaPlayer.getVLCVout().detachViews();
                                }

                                initVideo();
                            }
                        }).start();

                        break;
                    case MediaPlayer.Event.PositionChanged:
                        positionChanged = event.getPositionChanged();

                        break;
                    case MediaPlayer.Event.EncounteredError:

                        isError = true;

                        //视频播放出错启动一个定时器
                        startTimerError();
                        break;
                    case MediaPlayer.Event.Playing:
                        isError = false;
                        if (timerErrors != null) {
                            timerErrors.cancel();
                            timerErrors = null;
                        }


                        break;

                    default:
                        //Log.d(TAG, "Encounter VlcMediaPlayer event:" + event.type);
                }

            }
        });

        if (mOnLayoutChangeListener == null) {
            mOnLayoutChangeListener = new View.OnLayoutChangeListener() {
                private final Runnable mRunnable = new Runnable() {
                    @Override
                    public void run() {
                        updateVideoSurfaces();
                    }
                };

                @Override
                public void onLayoutChange(View v, int left, int top, int right,
                                           int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                    if (left != oldLeft || top != oldTop || right != oldRight || bottom != oldBottom) {
                        mHandler.removeCallbacks(mRunnable);
                        mHandler.post(mRunnable);
                    }
                }
            };
        }
        mVideoSurfaceFrame.addOnLayoutChangeListener(mOnLayoutChangeListener);
    }

    private void startTimerError() {
        if (timerErrors == null) {
            timerErrors = new Timer();
            timerErrors.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (isError) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (mMediaPlayer != null) {
                                            mMediaPlayer.stop();
                                            mMediaPlayer.getVLCVout().detachViews();
                                        }

                                        initVideo();
                                    }
                                }).start();
                            }
                        });

                    } else {

                        timerErrors.cancel();
                        timerErrors = null;

                    }

                }
            }, 0, errorTimer);
        }

    }


    private void initVideo() {

        if (TextUtils.isEmpty(sample_url)) {
            getActivity().finish();
        }
        Log.d("xxxxx->", sample_url);
        final IVLCVout vlcVout = mMediaPlayer.getVLCVout();
        try {
            vlcVout.setVideoView(mVideoSurface);
            vlcVout.attachViews(this);
        } catch (Exception e) {

        }
        final Media media = new Media(mLibVLC, Uri.parse(sample_url));
        media.setHWDecoderEnabled(true, false);//false 关闭硬件加速
        mMediaPlayer.setMedia(media);
        media.release();
        mMediaPlayer.play();

//        mVideoSurface.getRootView().setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent event) {
//                Log.d(TAG, "encounter onTouch with event: " + event.toString());
//                if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
//                    Display display = getActivity().getWindowManager().getDefaultDisplay();
//                    Point size = new Point();
//                    display.getSize(size);
//
//                    Float p = event.getX() / size.x;
//                    Long pos = (long) (mMediaPlayer.getLength() / p);
//                    Log.d(TAG, "seek to " + p + " / " + pos + " state is " + mMediaPlayer.getPlayerState());
//                    if (mMediaPlayer.isSeekable()) {
//                        //mLibVLC.setTime( pos );
//                        mMediaPlayer.setPosition(p);
//                    } else {
//                        Log.w(TAG, "Non-seekable input");
//                    }
//                }
//
//                return true;
//            }
//        });

    }

    @Override
    public void onStop() {
        super.onStop();


    }

    @Override
    public void onPause() {
        super.onPause();
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
        }
        if (timerErrors != null) {
            timerErrors.cancel();
            timerErrors = null;
        }


    }

    @Override
    public void onResume() {
        super.onResume();
        if (mMediaPlayer != null) {
            mMediaPlayer.play();
        }


    }

    private void changeMediaPlayerLayout(int displayW, int displayH) {
        /* Change the video placement using the MediaPlayer API */
        switch (CURRENT_SIZE) {
            case SURFACE_BEST_FIT:
//                mMediaPlayer.setAspectRatio(null);
//                mMediaPlayer.setScale(0);
                break;
            case SURFACE_FIT_SCREEN:
            case SURFACE_FILL: {
                Media.VideoTrack vtrack = mMediaPlayer.getCurrentVideoTrack();
                if (vtrack == null)
                    return;
                final boolean videoSwapped = vtrack.orientation == Media.VideoTrack.Orientation.LeftBottom
                        || vtrack.orientation == Media.VideoTrack.Orientation.RightTop;
                if (CURRENT_SIZE == SURFACE_FIT_SCREEN) {
                    int videoW = vtrack.width;
                    int videoH = vtrack.height;

                    if (videoSwapped) {
                        int swap = videoW;
                        videoW = videoH;
                        videoH = swap;
                    }
                    if (vtrack.sarNum != vtrack.sarDen)
                        videoW = videoW * vtrack.sarNum / vtrack.sarDen;

                    float ar = videoW / (float) videoH;
                    float dar = displayW / (float) displayH;

                    float scale;
                    if (dar >= ar)
                        scale = displayW / (float) videoW; /* horizontal */
                    else
                        scale = displayH / (float) videoH; /* vertical */
                    mMediaPlayer.setScale(scale);
                    mMediaPlayer.setAspectRatio(null);
                } else {
                    mMediaPlayer.setScale(0);
                    mMediaPlayer.setAspectRatio(!videoSwapped ? "" + displayW + ":" + displayH
                            : "" + displayH + ":" + displayW);
                }
                break;
            }
            case SURFACE_16_9:
                mMediaPlayer.setAspectRatio("16:9");
                mMediaPlayer.setScale(0);
                break;
            case SURFACE_4_3:
                mMediaPlayer.setAspectRatio("4:3");
                mMediaPlayer.setScale(0);
                break;
            case SURFACE_ORIGINAL:
                mMediaPlayer.setAspectRatio(null);
                mMediaPlayer.setScale(1);
                break;
        }
    }

    private void updateVideoSurfaces() {
        int sw = rootv.getWidth();
        int sh = rootv.getHeight();

        // sanity check
        if (sw * sh == 0) {

            return;
        }

        mMediaPlayer.getVLCVout().setWindowSize(sw, sh);

        ViewGroup.LayoutParams lp = mVideoSurface.getLayoutParams();
        if (mVideoWidth * mVideoHeight == 0) {
            /* Case of OpenGL vouts: handles the placement of the video using MediaPlayer API */
            lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
            lp.height = ViewGroup.LayoutParams.MATCH_PARENT;
            mVideoSurface.setLayoutParams(lp);
            lp = mVideoSurfaceFrame.getLayoutParams();
            lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
            lp.height = ViewGroup.LayoutParams.MATCH_PARENT;
            mVideoSurfaceFrame.setLayoutParams(lp);
            changeMediaPlayerLayout(sw, sh);
            return;
        }

        if (lp.width == lp.height && lp.width == ViewGroup.LayoutParams.MATCH_PARENT) {
            /* We handle the placement of the video using Android View LayoutParams */
            mMediaPlayer.setAspectRatio(null);
            mMediaPlayer.setScale(0);
        }

        double dw = sw, dh = sh;
        final boolean isPortrait = getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;

        if (sw > sh && isPortrait || sw < sh && !isPortrait) {
            dw = sh;
            dh = sw;
        }

        // compute the aspect ratio
        double ar, vw;
        if (mVideoSarDen == mVideoSarNum) {
            /* No indication about the density, assuming 1:1 */
            vw = mVideoVisibleWidth;
            ar = (double) mVideoVisibleWidth / (double) mVideoVisibleHeight;
        } else {
            /* Use the specified aspect ratio */
            vw = mVideoVisibleWidth * (double) mVideoSarNum / mVideoSarDen;
            ar = vw / mVideoVisibleHeight;
        }

        // compute the display aspect ratio
        double dar = dw / dh;

        switch (CURRENT_SIZE) {
            case SURFACE_BEST_FIT:
                if (dar < ar)
                    dh = dw / ar;
                else
                    dw = dh * ar;
                break;
            case SURFACE_FIT_SCREEN:
                if (dar >= ar)
                    dh = dw / ar; /* horizontal */
                else
                    dw = dh * ar; /* vertical */
                break;
            case SURFACE_FILL:
                break;
            case SURFACE_16_9:
                ar = 16.0 / 9.0;
                if (dar < ar)
                    dh = dw / ar;
                else
                    dw = dh * ar;
                break;
            case SURFACE_4_3:
                ar = 4.0 / 3.0;
                if (dar < ar)
                    dh = dw / ar;
                else
                    dw = dh * ar;
                break;
            case SURFACE_ORIGINAL:
                dh = mVideoVisibleHeight;
                dw = vw;
                break;
        }


        // set frame size (crop if necessary)
        lp = mVideoSurfaceFrame.getLayoutParams();
        lp.width = (int) Math.floor(dw);
        lp.height = (int) Math.floor(dh);
        mVideoSurfaceFrame.setLayoutParams(lp);
        mVideoSurface.invalidate();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onNewVideoLayout(IVLCVout vlcVout, int width, int height, int visibleWidth, int visibleHeight, int sarNum, int sarDen) {
        mVideoWidth = width;
        mVideoHeight = height;
        mVideoVisibleWidth = visibleWidth;
        mVideoVisibleHeight = visibleHeight;
        mVideoSarNum = sarNum;
        mVideoSarDen = sarDen;
        updateVideoSurfaces();
    }
}

