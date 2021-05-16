package com.dc.commonlib.commonentity.video.widget;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.dc.commonlib.R;
import com.dc.commonlib.commonentity.video.player.PlayerScreenMode;
import com.dc.commonlib.commonentity.video.player.PlayerViewAction;
import com.dc.commonlib.utils.AntiShakeUtils;
import com.dc.commonlib.utils.TimeUtils;

import java.lang.ref.WeakReference;
import java.util.Calendar;

public class PlayerControlView extends RelativeLayout implements PlayerViewAction, View.OnClickListener {
    /**
     * 屏幕模式
     */
    private PlayerScreenMode mPlayerScreenMode = PlayerScreenMode.Small;


    /**
     * 播放按钮状态
     */
    private PlayState mPlayState;

    /**
     * 整个view的显示控制
     */
    private PlayerViewAction.HideType mHideType;
    /**
     * 播放按钮
     */
    ImageView mPlayStateBtn;
    ImageView iv_centerplay;//中间播放按钮
    ImageView mBackBtn;

    /**
     * 屏幕模式切换按钮
     */
    TextView mScreenModeFullBtn;
    ImageView mScreenModeSmallBtn;
    FrameLayout mSmallClick;

    LinearLayout mHistoryModeStyle;

    TextView mLiveModeStyle;

    TextView tvSpeed;
    FrameLayout mFullClick;

    FrameLayout mSpeedClick;
    FrameLayout mPlayClick;

    SeekBar progressSeek;
    View mSpaceView;

    TextView tvPosition;
    TextView tvDuration;
    private OnPlayerControlClickListener mOnPlayerControlClickListener;
    private OnSeekbarChangeListener mOnSeekbarChangeListener;
    /**
     * 播放进度
     */
    private int mVideoPosition;
    /**
     * 是否在拖动中
     */
    private boolean isSeekbarTouching = false;
    private DisplayMode mCurrDisplayMode;
    private boolean isPlaying = true;
    private int progress;
    private boolean isInseek;
    private long duration;
    private boolean isEnabled = true;

    public PlayerControlView(Context context) {
        this(context, null);
    }

    public PlayerControlView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public void showCenterPlay() {
        if (null != iv_centerplay) {
            iv_centerplay.setVisibility(View.VISIBLE);
        }
    }

    private void initId(View view) {
        mPlayStateBtn = view.findViewById(R.id.iv_player_state);
        iv_centerplay = view.findViewById(R.id.iv_play);
        mBackBtn = view.findViewById(R.id.iv_back_vertical);
        mScreenModeFullBtn = view.findViewById(R.id.screen_mode_full);
        mScreenModeSmallBtn = view.findViewById(R.id.screen_mode_small);

        tvDuration = view.findViewById(R.id.tv_duration);
        tvPosition = view.findViewById(R.id.tv_position);
        mSpaceView = view.findViewById(R.id.space_view);
        mSmallClick = view.findViewById(R.id.fl_screen_mode_small_click);
        mHistoryModeStyle = view.findViewById(R.id.ll_mode_history);
        mPlayClick = view.findViewById(R.id.ll_player_click);
        progressSeek = view.findViewById(R.id.progress_seekbar);
        mSpeedClick = view.findViewById(R.id.fl_speed_click);
        mFullClick = view.findViewById(R.id.fl_screen_mode_full_click);
        mLiveModeStyle = view.findViewById(R.id.tv_mode_live);
        tvSpeed = view.findViewById(R.id.tv_speed);

        mFullClick.setOnClickListener(this);
        mSmallClick.setOnClickListener(this);
        mPlayClick.setOnClickListener(this);
        mBackBtn.setOnClickListener(this);
        mLiveModeStyle.setOnClickListener(this);
        mSpeedClick.setOnClickListener(this);
        iv_centerplay.setOnClickListener(this);
    }

    private void initView() {
        View inflate = LayoutInflater.from(getContext()).inflate(R.layout.view_player_control, this, true);
        initId(inflate);
        progressSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (mOnSeekbarChangeListener != null) {
                    mOnSeekbarChangeListener.onProgressChanged(progress, fromUser);
                }
                if (fromUser) {
                    isInseek = true;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                if (mOnSeekbarChangeListener != null) {
                    mOnSeekbarChangeListener.onStartSeek();
                }
                mHideHandler.removeMessages(WHAT_HIDE);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                isInseek = false;
                if (mOnSeekbarChangeListener != null) {
                    mOnSeekbarChangeListener.onStopSeek(seekBar.getProgress());
                }

                mHideHandler.removeMessages(WHAT_HIDE);
                mHideHandler.sendEmptyMessageDelayed(WHAT_HIDE, DELAY_TIME);
            }
        });
        updateAllViews();
    }


    private void updateAllViews() {
        updateScreenModeBtn();
        updatePlayStateBtn();
    }

    @Override
    public void reset() {
        mHideType = null;
        mVideoPosition = 0;
        mPlayState = PlayState.Stop;
        updateAllViews();
    }

    @Override
    public void show() {
        if (isEnabled) {
            setVisibility(VISIBLE);
        } else {
            setVisibility(GONE);
        }
    }


    /**
     * 更新视频进度
     *
     * @param position 位置，ms
     */
    public void setVideoPosition(int position) {
        mVideoPosition = position;
        tvPosition.setText(TimeUtils.formatMs(position));
    }

    public void setProgress(int progress) {
        this.progress = progress;
//        if (!isInseek) {
        progressSeek.setProgress(progress);
//        }
    }

    public int getVideoPosition() {
        return mVideoPosition;
    }

    public long getVideoDuration() {
        return duration;
    }

    @Override
    public void hide(HideType hideType) {
        mHideType = hideType;
        setVisibility(GONE);
    }

    public void hide() {
        setVisibility(GONE);
    }

    @Override
    public void setScreenModeStatus(PlayerScreenMode mode) {
        mPlayerScreenMode = mode;
        updateScreenModeBtn();
    }

    /**
     * 更新切换大小屏按钮的信息
     */
    private void updateScreenModeBtn() {
        if (mPlayerScreenMode == PlayerScreenMode.Full) {
            mSmallClick.setVisibility(GONE);
            mFullClick.setVisibility(VISIBLE);
            mScreenModeFullBtn.setText("退出全屏");
            mPlayerScreenMode = PlayerScreenMode.Full;
            mBackBtn.setVisibility(VISIBLE);
            mSpeedClick.setVisibility(VISIBLE);
        } else {
            mSmallClick.setVisibility(VISIBLE);
            mFullClick.setVisibility(GONE);
            mSpeedClick.setVisibility(GONE);
            mPlayerScreenMode = PlayerScreenMode.Small;
            mBackBtn.setVisibility(GONE);
        }
    }

    /**
     * 更新播放按钮的状态
     */
    public void updatePlayStateBtn(PlayState playState) {
        this.mPlayState = playState;
        updatePlayStateBtn();
    }

    private void updatePlayStateBtn() {
        if (mPlayState == PlayState.Stop) {
            mPlayStateBtn.setImageResource(R.drawable.play_state_stop);
            iv_centerplay.setVisibility(View.VISIBLE);

            iv_centerplay.setImageResource(R.drawable.play_state_stop);
            mPlayState = PlayState.Stop;
        } else if (mPlayState == PlayState.Playing) {
            mPlayStateBtn.setImageResource(R.drawable.play_state_playing);
            iv_centerplay.setVisibility(View.GONE);

            mPlayState = PlayState.Playing;
        }
    }

    public void setDisplayMode(DisplayMode displayMode) {
        mCurrDisplayMode = displayMode;
        if (displayMode == DisplayMode.HISTORY) {
            mHistoryModeStyle.setVisibility(VISIBLE);
            mLiveModeStyle.setVisibility(GONE);
            mSpaceView.setVisibility(GONE);
        } else if (displayMode == DisplayMode.LIVE) {
            mHistoryModeStyle.setVisibility(GONE);
            mLiveModeStyle.setVisibility(VISIBLE);
            mSpaceView.setVisibility(VISIBLE);
        }
    }


    /**
     * 设置播放时间显示
     *
     * @param startTime
     * @param endTime
     */
    public void setDuration(Calendar startTime, Calendar endTime) {
        if (endTime == null || startTime == null) {
            return;
        }
        duration = endTime.getTimeInMillis() - startTime.getTimeInMillis();
        String s = TimeUtils.formatMs(duration);
        progressSeek.setMax((int) duration);
        tvDuration.setText(s);
    }

    public long getDuration() {
        return duration;
    }

    private HideHandler mHideHandler = new HideHandler(this);

    private static final int WHAT_HIDE = 0;
    private static final int DELAY_TIME = 5 * 1000; //5秒后隐藏

    private void hideDelayed() {
        mHideHandler.removeMessages(WHAT_HIDE);
        mHideHandler.sendEmptyMessageDelayed(WHAT_HIDE, DELAY_TIME);
    }

    @Override
    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (visibility == VISIBLE) {
            hideDelayed();
        }
    }


    /**
     * 速度显示
     */
    public static final int HALF = -1;
    public static final int NORMAL = 0;
    public static final int DOUBLE = 1;
    public static final int FOUR = 2;

    public void setSpeedChanged(int speed) {
        if (speed == HALF) {
            tvSpeed.setText("0.5X");
        } else if (speed == NORMAL) {
            tvSpeed.setText("1.0X");
        } else if (speed == DOUBLE) {
            tvSpeed.setText("2.0X");
        } else if (speed == FOUR) {
            tvSpeed.setText("4.0X");
        } else {
            tvSpeed.setText("1.0X");
        }
    }

    public void hideCenterPlay() {
        if (iv_centerplay != null) {
            iv_centerplay.setVisibility(GONE);
        }
    }



    /**
     * 隐藏云台控制按钮
     */
    public void hideCloudControl() {
        if (mLiveModeStyle != null) {
            mLiveModeStyle.setVisibility(GONE);
        }
    }

    /**
     * 显示云台控制按钮
     */
    public void showCloudControl() {
        if (mLiveModeStyle != null) {
            mLiveModeStyle.setVisibility(VISIBLE);
        }
    }

    public void setEnable(boolean isEnable) {
//        setOnTouchListener((v, event) -> isEnable);
//        mBackBtn.setClickable(isEnable);
//        mBackBtn.setOnTouchListener((v, event) -> isEnable);
        this.isEnabled = isEnable;
        if (isEnable) {
            setVisibility(VISIBLE);
        } else {
            setVisibility(GONE);
        }
    }

    @Override
    public void onClick(View view) {
        if (!AntiShakeUtils.isInvalidClick(view)) {
            int id = view.getId();
            if (id == R.id.iv_play) {
                if (mOnPlayerControlClickListener != null) {
                    iv_centerplay.setVisibility(View.GONE);
                    mOnPlayerControlClickListener.onClickCenterPlay(true);
                }
            } else if (id == R.id.fl_screen_mode_full_click) {
                if (mOnPlayerControlClickListener != null) {
                    mOnPlayerControlClickListener.onChangeScreenMode(PlayerScreenMode.Full);
                }
            } else if (id == R.id.fl_screen_mode_small_click) {
                if (mOnPlayerControlClickListener != null) {
                    mOnPlayerControlClickListener.onChangeScreenMode(PlayerScreenMode.Small);
                }
            } else if (id == R.id.ll_player_click) {
                isPlaying = !isPlaying;
                mPlayState = isPlaying ? PlayState.Playing : PlayState.Stop;
                if (mOnPlayerControlClickListener != null) {
                    mOnPlayerControlClickListener.onChangePlayState(mPlayState);
                }
                updatePlayStateBtn();
            } else if (id == R.id.iv_back_vertical) {
                if (mOnPlayerControlClickListener != null) {
                    mOnPlayerControlClickListener.onBackToVertical();
                }
            } else if (id == R.id.tv_mode_live) {
                if (mOnPlayerControlClickListener != null) {
                    mOnPlayerControlClickListener.onShowControl();
                }
            } else if (id == R.id.fl_speed_click) {
                if (mOnPlayerControlClickListener != null) {
                    mOnPlayerControlClickListener.onShowControl();
                }
            }
        }
    }


    /**
     * 隐藏类
     */
    private static class HideHandler extends Handler {
        private WeakReference<PlayerControlView> controlViewWeakReference;

        public HideHandler(PlayerControlView controlView) {
            controlViewWeakReference = new WeakReference<>(controlView);
        }

        @Override
        public void handleMessage(Message msg) {

            PlayerControlView controlView = controlViewWeakReference.get();
            if (controlView != null) {
                if (!controlView.isSeekbarTouching) {
                    controlView.hide(HideType.Normal);
                }
            }
            super.handleMessage(msg);
        }
    }


    /**
     * 播放状态
     */
    public enum PlayState {
        /**
         * Playing:正在播放
         * NotPlaying: 停止播放
         */
        Playing, Stop
    }

    public interface OnPlayerControlClickListener {
        /**
         * 切换播放状态
         */
        void onChangePlayState(PlayState playState);

        /**
         * 切换屏幕模式
         */
        void onChangeScreenMode(PlayerScreenMode screenMode);

        /**
         * 中间播放
         */
        void onClickCenterPlay(boolean click);

        /**
         * 返回竖屏模式
         */
        void onBackToVertical();

        /**
         * 云台控制菜单
         */
        void onShowControl();
    }

    public void setOnPlayerControlClickListener(OnPlayerControlClickListener listener) {
        this.mOnPlayerControlClickListener = listener;
    }

    /**
     * seekbar 改变监听
     */
    public interface OnSeekbarChangeListener {
        /**
         * 开始seek
         */
        void onStartSeek();

        /**
         * 进度改变
         *
         * @param progress
         * @param fromUser
         */
        void onProgressChanged(int progress, boolean fromUser);

        /**
         * 结束seek
         *
         * @param progress
         */
        void onStopSeek(int progress);
    }

    public void setOnSeekbarChangeListener(OnSeekbarChangeListener listener) {
        this.mOnSeekbarChangeListener = listener;
    }
}
