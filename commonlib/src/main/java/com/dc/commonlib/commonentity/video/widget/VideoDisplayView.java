package com.dc.commonlib.commonentity.video.widget;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.dc.commonlib.R;
import com.dc.commonlib.commonentity.ThreadPool;
import com.dc.commonlib.commonentity.video.CameraInfoListBean;
import com.dc.commonlib.commonentity.video.CameraType;
import com.dc.commonlib.commonentity.video.DisplayVideoPlayerManager;
import com.dc.commonlib.commonentity.video.NetWatchdog;
import com.dc.commonlib.commonentity.video.OrientationWatchDog;
import com.dc.commonlib.commonentity.video.player.PlayerScreenMode;
import com.dc.commonlib.commonentity.video.player.VideoCloudControlManager;
import com.dc.commonlib.utils.LogUtil;
import com.dc.commonlib.utils.video.PlayerManager;
import com.dc.commonlib.utils.video.VideoPlayBackManager;
import com.hikvision.sdk.VMSNetSDK;
import com.hikvision.sdk.consts.SDKConstant;
import com.hikvision.sdk.net.bean.CustomRect;
import com.hikvision.sdk.net.bean.PlaybackSpeed;
import com.hikvision.sdk.net.business.OnVMSNetSDKBusiness;

import org.yczbj.ycvideoplayerlib.constant.ConstantKeys;
import org.yczbj.ycvideoplayerlib.manager.VideoPlayerManager;
import org.yczbj.ycvideoplayerlib.utils.NetworkUtils;
import org.yczbj.ycvideoplayerlib.utils.VideoPlayerUtils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Executors;


/**
 * 视频播放
 * 负责展示视频画面，以及sdk的调用
 */
public class VideoDisplayView extends RelativeLayout implements View.OnLongClickListener {


    private static final int PLAY_WINDOW_ONE = 1;
    private static final String TAG = VideoDisplayView.class.getSimpleName();
    SurfaceView mDisplayView;
    PlayerControlView mPlayerControlView;

    private PlayerControlView.PlayState mCurrPlaystate = PlayerControlView.PlayState.Playing;

    /**
     * 播放清晰度, 默认高清
     */
    private int mStreamType = SDKConstant.LiveSDKConstant.MAIN_HIGH_STREAM;
    /**
     * 回放的开始时间和结束时间
     */
    private Calendar mStartTime, mEndTime;

    private PlayerScreenMode mCurrScreenMode = PlayerScreenMode.Small;
    private OrientationWatchDog mOrientationWatchDog;

    private Calendar mFirstStartTime;

    private DisplayMode mDisplayMode;

    SpeedControlMenu speedControlMenu;

    View mMenuBlankView;

    ArcMenu mArcMenu;

    RelativeLayout llArcMenuLayout;

    LinearLayout mMenuRootLayout;

    TipsView mTipsView;
    private RelativeLayout mContainer;
    private ImageView iv_placeholder;
    private Button btn_big;
    private Button btn_small;

    private void initId(View v) {
        mDisplayView = v.findViewById(R.id.display_surface);
        btn_big = v.findViewById(R.id.btn_big);
        btn_small = v.findViewById(R.id.btn_small);
        btn_big.setOnLongClickListener(this);

        btn_small.setOnLongClickListener(this);

        mPlayerControlView = v.findViewById(R.id.player_control);
        speedControlMenu = v.findViewById(R.id.speed_view);
        mTipsView = v.findViewById(R.id.tips_view);
        mMenuRootLayout = v.findViewById(R.id.ll_menu_root);
        llArcMenuLayout = v.findViewById(R.id.rl_arc_menu);
        mArcMenu = v.findViewById(R.id.arc_menu);
        mMenuBlankView = v.findViewById(R.id.speed_blank_click);
        iv_placeholder = v.findViewById(R.id.iv_placeholder);
    }

    public void hiddenPlaceHolder() {
        iv_placeholder.setVisibility(View.GONE);
    }

    public void hiddenCenterPlay() {
        mPlayerControlView.hideCenterPlay();
    }

    public void showCenterPlay() {
        if (!mTipsView.isShow()) {
            mPlayerControlView.showCenterPlay();
        }
    }

    /**
     * 判断视频是否播放全屏
     *
     * @return true表示播放全屏
     */
    public boolean isFullScreen() {
        return mCurrScreenMode == PlayerScreenMode.Full;
    }


    public ImageView getIv_placeholder() {
        return iv_placeholder;
    }

    public void showPlaceHolder() {
        iv_placeholder.setVisibility(View.VISIBLE);
        iv_placeholder.setVisibility(View.GONE);
    }

    private List<ArcMenu.ArcAreaData> menuSubView = new ArrayList<>();
    private NetWatchdog mNetWatchdog;
    private int mPlayPosition;
    //进度更新计时器
    private ProgressUpdateTimer mProgressUpdateTimer = new ProgressUpdateTimer(this);
    /**
     * 是否正在拖动
     */
    private boolean inSeek = false;
    /**
     * 当前云台控制命令
     */
    int mCurrenControlCommand = -2;

    /**
     * 是否被销毁
     */
    private boolean isDestroy = false;
    private int mCameraType;
    private CameraInfoListBean.ListBean mCurrPreviewCamera;
    private String playBackUrl;
    private boolean isContinue;
    /**
     * 切换速度时，修改进度时间
     */
    private long mCurentProgressSpeed = 1000;
    private SurfaceHolder mSurfaceHolder;
    private Context context;

    public void onResume() {
        if (mNetWatchdog != null) {
            mNetWatchdog.startWatch();
        }
        if (!isDestroy && DisplayMode.HISTORY == mDisplayMode) {
            // 退出时时暂停状态，回来时也应该时暂停状态
            // 如果回到前台要自动播放，则可取消注释
            resumePlayBack(PLAY_WINDOW_ONE);
        }
        if (mDisplayMode == DisplayMode.LIVE) {
            startPreview(mCurrPreviewCamera);
        }
    }

    private void resumePlayBack(int playWindowOne) {
        mCurrPlaystate = PlayerControlView.PlayState.Playing;
        mPlayerControlView.updatePlayStateBtn(mCurrPlaystate);
        startProgressUpdateTimer();
        PlayerManager.getPlayerInstance().resumePlayBackOpt(playWindowOne);
    }

    public void onStop() {
        if (mNetWatchdog != null) {
            mNetWatchdog.stopWatch();
        }
        mCurrPlaystate = PlayerControlView.PlayState.Stop;
        if (!isDestroy && DisplayMode.HISTORY.equals(mDisplayMode)) {
            pausePlayBack(PLAY_WINDOW_ONE);
        }
    }

    private void pausePlayBack(int playWindowOne) {
        mCurrPlaystate = PlayerControlView.PlayState.Stop;
        mPlayerControlView.updatePlayStateBtn(mCurrPlaystate);
        stopProgressUpdateTimer();
        PlayerManager.getPlayerInstance().pausePlayBack(playWindowOne);
    }

    public VideoDisplayView(Context context) {
        this(context, null);
    }

    public VideoDisplayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

        View inflate = LayoutInflater.from(context).inflate(R.layout.view_display, this, true);
        mContainer = inflate.findViewById(R.id.el_container);
        initId(inflate);
        initNetWatch();
        initSurfaceView();
        initPlayerSDK();
        initControlView();
        initSpeedView();
        initVideoControlView();
        initTipsView();

    }

    private void initSpeedView() {
        speedControlMenu.setOnSpeedChangeListener(new SpeedControlMenu.OnSpeedChangeListener() {
            @Override
            public void onSpeedChanged(SpeedControlMenu.PlaySpeed speed) {
                setPlaySpeed(speed);
            }
        });
        if (speedControlMenu != null && speedControlMenu.isShowing()) {
            speedControlMenu.hide();
        }

        mMenuBlankView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (speedControlMenu != null && speedControlMenu.isShowing()) {
                    speedControlMenu.hide();
                }
                mMenuRootLayout.setVisibility(GONE);
            }
        });
    }


    private void initVideoControlView() {
        for (int i = 0; i < 8; i++) {
            ArcMenu.ArcAreaData arcAreaData = new ArcMenu.ArcAreaData();
            menuSubView.add(arcAreaData);
        }
        if (mCurrScreenMode == PlayerScreenMode.Small) {
            mPlayerControlView.hideCloudControl();
        }
        mArcMenu.addArcData(menuSubView);
        llArcMenuLayout.setVisibility(GONE);
        mArcMenu.setOnKeyClickLstener(new ArcMenu.OnKeyDownLstener() {
            @Override
            public void onKeyDown(int position) {
                switch (position) {
                    case ArcMenu.KEY_UP:
                        mCurrenControlCommand = VideoCloudControlManager.CommandConstant.CUSTOM_CMD_UP;
                        break;
                    case ArcMenu.KEY_DOWN:
                        mCurrenControlCommand = VideoCloudControlManager.CommandConstant.CUSTOM_CMD_DOWN;
                        break;
                    case ArcMenu.KEY_LEFT:
                        mCurrenControlCommand = VideoCloudControlManager.CommandConstant.CUSTOM_CMD_LEFT;
                        break;
                    case ArcMenu.KEY_RIGHT:
                        mCurrenControlCommand = VideoCloudControlManager.CommandConstant.CUSTOM_CMD_RIGHT;
                        break;
                    case ArcMenu.KEY_LEFT_UP:
                        mCurrenControlCommand = VideoCloudControlManager.CommandConstant.CUSTOM_CMD_LEFT_UP;
                        break;
                    case ArcMenu.KEY_LEFT_DOWN:
                        mCurrenControlCommand = VideoCloudControlManager.CommandConstant.CUSTOM_CMD_LEFT_DOWN;
                        break;
                    case ArcMenu.KEY_RIGHT_UP:
                        mCurrenControlCommand = VideoCloudControlManager.CommandConstant.CUSTOM_CMD_RIGHT_UP;
                        break;
                    case ArcMenu.KEY_RIGHT_DOWN:
                        mCurrenControlCommand = VideoCloudControlManager.CommandConstant.CUSTOM_CMD_RIGHT_DOWN;
                        break;
                    default:
                        break;
                }
                LogUtil.e(TAG, "发送命令 开始..." + position);
                VideoCloudControlManager.getInstance().sendCommand(PLAY_WINDOW_ONE, mCurrenControlCommand, VideoCloudControlManager.CommandConstant.ACTION_START);
            }

            @Override
            public void onKeyUp(int position) {
                LogUtil.e(TAG, "发送命令 停止..." + position);
                VideoCloudControlManager.getInstance().sendCommand(PLAY_WINDOW_ONE, mCurrenControlCommand, VideoCloudControlManager.CommandConstant.ACTION_STOP);
            }
        });
    }

    /**
     * 初始化网络监听
     */
    private void initNetWatch() {
        Context context = getContext();
        mNetWatchdog = new NetWatchdog(context);
        mNetWatchdog.setNetChangeListener(new MyNetChangeListener(this));
    }

    private void initSurfaceView() {
        mSurfaceHolder = mDisplayView.getHolder();
        mSurfaceHolder.addCallback(mSurfaceHolderCallback);
        mDisplayView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mPlayerControlView.show();
                return true;
            }
        });

    }

    private SurfaceHolder.Callback mSurfaceHolderCallback = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            LogUtil.d("LDL","surfaceChanged创建");

            if (mDisplayMode == DisplayMode.HISTORY) {
                VMSNetSDK.getInstance().resumePlayBackOpt(PLAY_WINDOW_ONE);
                VMSNetSDK.getInstance().setVideoWindowOpt(PLAY_WINDOW_ONE, holder);
            }else {
                startPreview(mCurrPreviewCamera);

            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        LogUtil.d("LDL","surfaceChanged改变");
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            if (mDisplayMode == DisplayMode.HISTORY) {
                VMSNetSDK.getInstance().pausePlayBackOpt(PLAY_WINDOW_ONE);
                VMSNetSDK.getInstance().setVideoWindowOpt(PLAY_WINDOW_ONE, null);
            } else {
                VMSNetSDK.getInstance().stopLiveOpt(PLAY_WINDOW_ONE);
                LogUtil.d("LDL","surfaceChanged销毁");

            }
        }
    };

    private void initPlayerSDK() {
    }

    private OnItemClickListener onItemClickListener;

    public void addOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;

    }

    /**
     * 必须全屏状态下调用
     *
     * @return
     */
    public boolean exitFullScreen() {
        mCurrScreenMode = PlayerScreenMode.Small;
        return changeScreenMode(mCurrScreenMode);
    }

    @Override
    public boolean onLongClick(View v) {
        if (v.getId() == R.id.btn_big) {
            CustomRect customRect1=new CustomRect();
            customRect1.setValue(0f,0f,0f,0f);
            CustomRect customRect2=new CustomRect();
            customRect2.setValue(40f,40f,40f,40f);
            VMSNetSDK.getInstance().zoomLiveOpt(PLAY_WINDOW_ONE, true,customRect1, customRect2);



        } else if (v.getId() == R.id.btn_small) {
            CustomRect customRect1=new CustomRect();
            customRect1.setValue(0f,0f,0f,0f);
            CustomRect customRect2=new CustomRect();
            customRect2.setValue(40f,40f,40f,40f);
            VMSNetSDK.getInstance().zoomLiveOpt(PLAY_WINDOW_ONE, false,customRect2, customRect1);

        }
        return false;
    }

    public interface OnItemClickListener {
        void OnItemClick();
    }

    private void initControlView() {
        if (mMenuRootLayout != null) {
            mMenuRootLayout.setVisibility(GONE);
        }
        mPlayerControlView.setScreenModeStatus(PlayerScreenMode.Small);

        if (mCurrScreenMode == PlayerScreenMode.Small) {
            mPlayerControlView.hideCloudControl();
            mPlayerControlView.showCenterPlay();
        } else if (mCurrScreenMode == PlayerScreenMode.Full) {
            mPlayerControlView.hideCenterPlay();

        }
        mPlayerControlView.setOnPlayerControlClickListener(new PlayerControlView.OnPlayerControlClickListener() {
            @Override
            public void onChangePlayState(PlayerControlView.PlayState playState) {
                if (PlayerControlView.PlayState.Playing == playState) {
                    startProgressUpdateTimer();
                    resumePlayBack(PLAY_WINDOW_ONE);
                } else {
                    stopProgressUpdateTimer();
                    pausePlayBack(PLAY_WINDOW_ONE);
                }
            }

            @Override
            public void onChangeScreenMode(PlayerScreenMode screenMode) {
                if (screenMode == PlayerScreenMode.Small) {
                    mCurrScreenMode = PlayerScreenMode.Full;
                } else {
                    mCurrScreenMode = PlayerScreenMode.Small;
                }
                changeScreenMode(mCurrScreenMode);
            }

            @Override
            public void onClickCenterPlay(boolean click) {
                if (click) {
                    if (null != onItemClickListener) {
                        onItemClickListener.OnItemClick();
                    }
                }
            }

            @Override
            public void onBackToVertical() {
                mCurrScreenMode = PlayerScreenMode.Small;
                changeScreenMode(PlayerScreenMode.Small);
                mPlayerControlView.setScreenModeStatus(PlayerScreenMode.Small);
            }

            @Override
            public void onShowControl() {
                if (mCurrScreenMode == PlayerScreenMode.Small) {
                    // 当前小窗不可以调整
                    // 小屏时隐藏云台控制按钮
                    mPlayerControlView.hideCloudControl();
                    return;
                }
                mMenuRootLayout.setVisibility(VISIBLE);
                mPlayerControlView.hide();
                if (mDisplayMode == DisplayMode.HISTORY) {
                    llArcMenuLayout.setVisibility(GONE);
                    speedControlMenu.show();
                } else {
                    speedControlMenu.hide();
                    llArcMenuLayout.setVisibility(VISIBLE);
                }
            }
        });


        mPlayerControlView.setOnSeekbarChangeListener(new PlayerControlView.OnSeekbarChangeListener() {
            @Override
            public void onStartSeek() {
                LogUtil.e(TAG, "start seek.....");
                stopProgressUpdateTimer();
            }

            @Override
            public void onProgressChanged(int progress, boolean fromUser) {
                if (fromUser) {
                    inSeek = true;
                }
            }

            @Override
            public void onStopSeek(int progress) {
                VMSNetSDK.getInstance().stopPlayBackOpt(PLAY_WINDOW_ONE);
                inSeek = false;
                mTipsView.setShowType(TipsView.Type.Connect, "正在获取录像");
                long end = mEndTime.getTimeInMillis();
                Calendar track = Calendar.getInstance();
                long l = mFirstStartTime.getTimeInMillis() + progress;
                track.setTimeInMillis(l);
                mStartTime = track;
                mPlayPosition = progress;
                mSeekProgress = progress;
                if (progress < end) {
                    startPlayBack(playBackUrl, mStartTime, mEndTime);
                    mPlayerControlView.setVideoPosition(progress);
                    mPlayerControlView.setProgress(progress);
                    startProgressUpdateTimer();
                } else if (mPlayerControlView.getVideoPosition() >= end || progress >= end) {
                    stopProgressUpdateTimer();
                    mPlayerControlView.updatePlayStateBtn(PlayerControlView.PlayState.Stop);
                    VMSNetSDK.getInstance().stopPlayBackOpt(PLAY_WINDOW_ONE);
                }
            }
        });
    }

    /**
     * 初始化屏幕方向旋转。用来监听屏幕方向。结果通过OrientationListener回调出去。
     */
    private void initOrientationWatchdog() {
        final Context context = getContext();
        mOrientationWatchDog = new OrientationWatchDog(context);
        mOrientationWatchDog.setOnOrientationListener(new MyOrientationChangeListener(this));
    }

    private void initTipsView() {
        mTipsView.hideAll();
        mTipsView.setOnTipsOperatorClickListener(new TipsView.onTipsOperatorClickListener() {
            @Override
            public void onRefresh() {
                mTipsView.hideAll();
                if (mDisplayMode == DisplayMode.HISTORY) {
                    startPlayBack(playBackUrl, mFirstStartTime, mEndTime);
                    mPlayPosition = 0;
                    mSeekProgress = 0;
                } else {
                    startPreview(mCurrPreviewCamera);
                }
            }

            @Override
            public void onContinue() {
                // 继续播放按钮的点击事件
                mTipsView.hideAll();
                isContinue = true;
                if (mDisplayMode == DisplayMode.LIVE) {
                    startPreview(mCurrPreviewCamera);
                } else {
                    startPlayBack(playBackUrl, mStartTime, mEndTime);
                }
            }
        });
    }

    /**
     * 开启实时预览
     */
    public void startPreview(final CameraInfoListBean.ListBean mCameraInfo) {
        DisplayVideoPlayerManager.instance().setCurrentVideoPlayer(VideoDisplayView.this);
        if (mCameraInfo == null) {
            LogUtil.e(TAG, "startPreview failure ... mCameraInfo is null");
            return;
        }
        if (mCurrPreviewCamera != mCameraInfo) {
            showPlaceHolder();
        }
        this.mCurrPreviewCamera = mCameraInfo;
        checkCloudControlState(mCameraInfo);
        if (NetworkUtils.getNetworkConnectState(context) == 1 && !isContinue) {
            mTipsView.setShowType(TipsView.Type.Net);
            return;
        }
        mTipsView.setShowType(TipsView.Type.Connect, String.format("正在连接%s, 请稍等", mCameraInfo.getCameraName()));
        VMSNetSDK.getInstance().stopLiveOpt(PLAY_WINDOW_ONE);
        Executors.newCachedThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
//                VMSNetSDK.getInstance().startPlayBackOpt();
                VMSNetSDK.getInstance().startLiveOpt(1, mCameraInfo.getCameraUuid(), mDisplayView, mStreamType, new OnVMSNetSDKBusiness() {
                    @Override
                    public void onFailure() {
                        mTipsView.post(new Runnable() {
                            @Override
                            public void run() {
                                mTipsView.hideAll();
                                LogUtil.d("LDL", "播放失败错误?");
                                mTipsView.setShowType(TipsView.Type.Error);
                                showPlaceHolder();
                                hiddenCenterPlay();
                            }
                        });
                    }

                    @Override
                    public void onSuccess(Object obj) {
                        LogUtil.d("LDL", "播放成功?" + obj);
                        // 回调是子线程
                        mTipsView.post(new Runnable() {
                            @Override
                            public void run() {
                                mTipsView.hideAll();
                                hiddenPlaceHolder();
                                hiddenCenterPlay();
                            }
                        });
                    }
                });
                Looper.loop();
            }
        });
    }

    /**
     * 检查云台控制按钮是否显示
     *
     * @param mCameraInfo
     */
    private void checkCloudControlState(CameraInfoListBean.ListBean mCameraInfo) {
        mCameraType = mCameraInfo.getCameraType();
        if (mCameraType == CameraType.GUN.ordinal() || mCameraType == CameraType.GUN_MIRROR.ordinal() || mCurrScreenMode == PlayerScreenMode.Small) {
            mPlayerControlView.hideCloudControl();
        } else if (mCameraType == CameraType.GLOBE_HALF.ordinal() || mCameraType == CameraType.GLOBE.ordinal() && mCurrScreenMode == PlayerScreenMode.Full) {
            mPlayerControlView.showCloudControl();
        }
    }

    public void startPlayBack(String url, Calendar startTime, Calendar endTime) {
        this.mStartTime = startTime;
        this.mEndTime = endTime;
        this.playBackUrl = url;
        if (TextUtils.isEmpty(url)) {
            Toast.makeText(getContext(), "没有录像信息", Toast.LENGTH_SHORT).show();
            return;
        }

        if (NetworkUtils.getNetworkConnectState(context) == 1 && !isContinue) {
            mTipsView.setShowType(TipsView.Type.Net);
            return;
        }

        if (mTipsView != null) {
            mTipsView.setShowType(TipsView.Type.Connect, "正在获取录像信息");
        }
        VMSNetSDK.getInstance().stopPlayBackOpt(PLAY_WINDOW_ONE);
        VideoPlayBackManager.getInstance().startPlayback(PLAY_WINDOW_ONE, mDisplayView, url, startTime, endTime, new VideoPlayBackManager.OnStarPlayCallBack() {
            @Override
            public void onFailure() {
                mTipsView.post(new Runnable() {
                    @Override
                    public void run() {
                        mTipsView.setShowType(TipsView.Type.Error, "录像播放失败");
                    }
                });
                mCurrPlaystate = PlayerControlView.PlayState.Stop;
                mPlayerControlView.post(new Runnable() {
                    @Override
                    public void run() {
                        mPlayerControlView.updatePlayStateBtn(mCurrPlaystate);
                        mPlayerControlView.setEnable(false);
                    }
                });
                stopProgressUpdateTimer();
                mSeekProgress = 0;
                mPlayPosition = 0;
            }

            @Override
            public void onSuccess(Object object) {
                mTipsView.post(new Runnable() {
                    @Override
                    public void run() {
                        mTipsView.hideAll();
                    }
                });
                mCurrPlaystate = PlayerControlView.PlayState.Playing;
                mPlayerControlView.post(new Runnable() {
                    @Override
                    public void run() {
                        mPlayerControlView.updatePlayStateBtn(mCurrPlaystate);
                        mPlayerControlView.setEnable(true);
                    }
                });
                startProgressUpdateTimer();
            }

            @Override
            public void onFinish() {
                mTipsView.post(new Runnable() {
                    @Override
                    public void run() {
                        mTipsView.setShowType(TipsView.Type.Connect, "录像播放结束");
                    }
                });
                mCurrPlaystate = PlayerControlView.PlayState.Stop;
                mPlayerControlView.post(new Runnable() {
                    @Override
                    public void run() {
                        mPlayerControlView.updatePlayStateBtn(mCurrPlaystate);
                    }
                });
                stopProgressUpdateTimer();
                VMSNetSDK.getInstance().stopPlayBackOpt(PLAY_WINDOW_ONE);
            }
        });
    }


    /**
     * 释放资源
     */
    public void release() {
        isDestroy = true;
        stopProgressUpdateTimer();
        if (mProgressUpdateTimer != null) {
            mProgressUpdateTimer.removeMessages(0);
            mProgressUpdateTimer = null;
        }
        if (mSurfaceHolder != null) {
            mSurfaceHolder.removeCallback(mSurfaceHolderCallback);
            mSurfaceHolder = null;
        }
    }

    public void setCurrentPreview(String cameraUuid) {
        if (mCurrPreviewCamera.getCameraUuid().equals(cameraUuid)) {
            mTipsView.hideAll();
        }
    }

    public void setDuration(Calendar mStartTime, Calendar mEndTime) {
        this.mFirstStartTime = mStartTime;
        mPlayerControlView.setDuration(mStartTime, mEndTime);
    }

    /**
     * 进度更新计时器
     */
    private static class ProgressUpdateTimer extends Handler {

        private WeakReference<VideoDisplayView> viewWeakReference;

        ProgressUpdateTimer(VideoDisplayView aliyunVodPlayerView) {
            viewWeakReference = new WeakReference<>(aliyunVodPlayerView);
        }

        @Override
        public void handleMessage(Message msg) {
            VideoDisplayView view = viewWeakReference.get();
            if (view != null) {
                view.handleProgressUpdateMessage(msg);
            }
            super.handleMessage(msg);
        }
    }

    private void handleProgressUpdateMessage(Message msg) {
        if (!inSeek) {
            if (mPlayPosition < mPlayerControlView.getVideoDuration()) {
                mPlayerControlView.post(new Runnable() {
                    @Override
                    public void run() {
                        mPlayerControlView.setVideoPosition(mPlayPosition += 1000);
                    }
                });
                mPlayerControlView.post(new Runnable() {
                    @Override
                    public void run() {
                        mPlayerControlView.setProgress(mSeekProgress += 1000);
                    }
                });
            } else if (mPlayPosition >= mPlayerControlView.getDuration() || mPlayerControlView.getVideoPosition() >= mPlayerControlView.getDuration()) {
                stopProgressUpdateTimer();
                mPlayerControlView.updatePlayStateBtn(PlayerControlView.PlayState.Stop);
                VMSNetSDK.getInstance().stopPlayBackOpt(PLAY_WINDOW_ONE);
                if (mTipsView != null) {
                    mTipsView.setShowType(TipsView.Type.Connect, "播放结束");
                }
            }
        }

        startProgressUpdateTimer();
    }

    int mSeekProgress = 0;

    /**
     * 开始进度条更新计时器
     */
    private void startProgressUpdateTimer() {
        if (mProgressUpdateTimer != null) {
            mProgressUpdateTimer.removeMessages(0);
            mProgressUpdateTimer.sendEmptyMessageDelayed(0, mCurentProgressSpeed);
        }
    }

    /**
     * 停止进度条更新计时器
     */
    private void stopProgressUpdateTimer() {
        if (mProgressUpdateTimer != null) {
            mProgressUpdateTimer.removeMessages(0);
        }
    }

    public void setDisplayMode(DisplayMode displayMode) {
        this.mDisplayMode = displayMode;
        mPlayerControlView.setDisplayMode(displayMode);
    }

    /**
     * 是否是暂停状态
     */
    boolean mIsPause;

    /**
     * 切换播放速度
     */
    public void setPlaySpeed(SpeedControlMenu.PlaySpeed speed) {
        if (mDisplayMode == DisplayMode.LIVE) {
            return;
        }
        if (mIsPause) {
            Toast.makeText(getContext(), "非播放状态不能调节倍速", Toast.LENGTH_SHORT).show();
            return;
        }
        int playbackSpeed = PlaybackSpeed.NORMAL;
        if (speed == SpeedControlMenu.PlaySpeed.Half) {
            playbackSpeed = PlaybackSpeed.HALF;
            mCurentProgressSpeed = 2000;
        } else if (speed == SpeedControlMenu.PlaySpeed.Normal) {
            playbackSpeed = PlaybackSpeed.NORMAL;
            mCurentProgressSpeed = 1000;
        } else if (speed == SpeedControlMenu.PlaySpeed.Twice) {
            playbackSpeed = PlaybackSpeed.DOUBLE;
            mCurentProgressSpeed = 500;
        } else if (speed == SpeedControlMenu.PlaySpeed.Four) {
            playbackSpeed = PlaybackSpeed.FOUR;
            mCurentProgressSpeed = 250;
        } else {
            mCurentProgressSpeed = 1000;
            playbackSpeed = PlaybackSpeed.NORMAL;
        }

        stopProgressUpdateTimer();
        startProgressUpdateTimer();

        mPlayerControlView.setSpeedChanged(playbackSpeed);
        VMSNetSDK.getInstance().setPlaybackSpeed(PLAY_WINDOW_ONE, playbackSpeed);

    }

    private class MyOrientationChangeListener implements OrientationWatchDog.OnOrientationListener {
        private WeakReference<VideoDisplayView> weakReference;

        public MyOrientationChangeListener(VideoDisplayView videoDisplayView) {
            weakReference = new WeakReference<>(videoDisplayView);
        }

        @Override
        public void changedToLandScape(boolean fromPort) {
            VideoDisplayView videoDisplayView = weakReference.get();
            if (videoDisplayView == null) {
                return;
            }
            //屏幕由竖屏转为横屏
            if (mCurrScreenMode == PlayerScreenMode.Full) {
                //全屏情况转到了横屏

            } else if (mCurrScreenMode == PlayerScreenMode.Small) {
                changeScreenMode(PlayerScreenMode.Full);
            }
        }

        @Override
        public void changedToPortrait(boolean fromLand) {
            VideoDisplayView videoDisplayView = weakReference.get();
            if (videoDisplayView == null) {
                return;
            }
        }
    }

    /**
     * 改变屏幕模式(横竖屏) 传什么进什么屏
     *
     * @param screenMode
     */
    @SuppressLint("SourceLockedOrientationActivity")
    public boolean changeScreenMode(PlayerScreenMode screenMode) {
        boolean exitFull = false;
        Context context = getContext();
        ViewGroup contentView = VideoPlayerUtils.scanForActivity(context)
                .findViewById(android.R.id.content);
        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            if (screenMode == PlayerScreenMode.Full) {
                //进入全屏
                //不是固定竖屏播放。
                VideoPlayerUtils.hideActionBar(context);
                VideoPlayerUtils.scanForActivity(activity).setRequestedOrientation(
                        ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                this.removeView(mContainer);
                if (mPlayerControlView != null && mDisplayMode == DisplayMode.LIVE && (mCameraType == CameraType.GLOBE.ordinal() || mCameraType == CameraType.GLOBE_HALF.ordinal())) {
                    // 大屏时显示云台控制按钮
                    mPlayerControlView.showCloudControl();
                }
                if (mPlayerControlView != null) {
                    mPlayerControlView.setScreenModeStatus(mCurrScreenMode);
                }

                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
                contentView.addView(mContainer, params);
                mCurrScreenMode = PlayerScreenMode.Full;
                //todo
                exitFull = false;
            } else if (screenMode == PlayerScreenMode.Small) {
                    //要进去小屏幕
                VideoPlayerUtils.showActionBar(context);
                VideoPlayerUtils.scanForActivity(context).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                if (speedControlMenu != null) {
                    speedControlMenu.hide();
                }
                // 小屏时隐藏云台控制按钮
                if (mPlayerControlView != null) {
                    mPlayerControlView.hideCloudControl();
                }
                if (mPlayerControlView != null) {
                    mPlayerControlView.setScreenModeStatus(mCurrScreenMode);
//                    mPlayerControlView.show();
                }
                //将视图移除
                contentView.removeView(mContainer);
                //重新添加到当前视图
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                this.addView(mContainer, params);
                mCurrScreenMode = PlayerScreenMode.Small;
                //todo
                exitFull = true;
            }
            startPreview(mCurrPreviewCamera); //tofo

        }
        return exitFull;
    }

    private class MyNetChangeListener implements NetWatchdog.NetChangeListener {
        private WeakReference<VideoDisplayView> viewWeakReference;

        MyNetChangeListener(VideoDisplayView videoDisplayView) {
            viewWeakReference = new WeakReference<>(videoDisplayView);
        }

        @Override
        public void onWifiTo4G() {
            VideoDisplayView view = viewWeakReference.get();
            if (view != null) {
                view.onWifiTo4G();
            }
        }

        @Override
        public void on4GToWifi() {
            VideoDisplayView view = viewWeakReference.get();
            if (view != null) {
                view.on4GToWifi();
            }
        }

        @Override
        public void onNetDisconnected() {
            VideoDisplayView view = viewWeakReference.get();
            if (view != null) {
                view.onNetDisconnected();
            }
        }
    }

    private void on4GToWifi() {
        if (mTipsView.isErrorShowing()) {
            return;
        }
        isContinue = true;
        mTipsView.hideNetTips();
        if (mDisplayMode == DisplayMode.LIVE) {
            startPreview(mCurrPreviewCamera);
//            mTipsView.setShowType(TipsView.Type.Connect, String.format("正在连接%s, 请稍等", mCurrBackCameraInfo != null? mCurrBackCameraInfo.getCameraName():""));
        } else {
//            mTipsView.setShowType(TipsView.Type.Connect, "正在获取录像");
        }
    }

    private void onWifiTo4G() {
        if (mTipsView.isErrorShowing()) {
            return;
        }
        isContinue = false;
        if (mDisplayMode == DisplayMode.LIVE) {
            pausePreview();
        } else {
            pausePlayBack();
        }
        mTipsView.setShowType(TipsView.Type.Net);
    }

    private void pausePreview() {
        VMSNetSDK.getInstance().stopLiveOpt(PLAY_WINDOW_ONE);
    }

    private void pausePlayBack() {
        stopProgressUpdateTimer();
        PlayerManager.getPlayerInstance().pausePlayBack(PLAY_WINDOW_ONE);
    }

    private void onNetDisconnected() {
        // 由于Android 在切换网络时也会调用该回调，所以该方法不准确
        LogUtil.e(TAG, "current net state is disconnected.....");
    }
}
