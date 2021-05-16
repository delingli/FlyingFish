package com.dc.commonlib.weiget;

import android.annotation.SuppressLint;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.AppCompatButton;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class CountDownButton extends AppCompatButton implements LifecycleObserver, View.OnClickListener {

    public static final String SHARED_PREFERENCES_FILE = "CountDownButton";
    public static final String SHARED_PREFERENCES_FIELD_TIME = "last_count_time";
    public static final String SHARED_PREFERENCES_FIELD_TIMESTAMP = "last_count_timestamp";
    public static final String SHARED_PREFERENCES_FIELD_INTERVAL = "count_interval";
    public static final String SHARED_PREFERENCES_FIELD_COUNTDOWN = "is_countdown";

    private CountDownTimer mCountDownTimer;
    private OnCountDownStartListener mOnCountDownStartListener;
    private OnCountDownTickListener mOnCountDownTickListener;
    private OnCountDownFinishListener mOnCountDownFinishListener;
    private String mNormalText;
    private String mCountDownText;
    private OnClickListener mOnClickListener;

    private boolean isCounting = false;


    /**
     * 倒计时期间是否允许点击
     */
    private boolean mClickable = false;

    /**
     * 页面关闭后倒计时是否保持，再次开启倒计时继续
     */
    private boolean mCloseKeepCountDown = false;
    private int textColor = Color.parseColor("#ababbb");
    private int defaultColor = Color.GREEN;

    /**
     * 是否把时间格式化成时分秒
     */
    private boolean mShowFormatTime = false;

    /**
     * 倒计时间隔
     */
    private TimeUnit mIntervalUnit = TimeUnit.SECONDS;

    public CountDownButton(Context context) {
        this(context, null);
    }

    public CountDownButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CountDownButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        autoBindLifecycle(context);
    }

    /**
     * 控件自动绑定生命周期，宿主可以是activity或者fragment
     */
    private void autoBindLifecycle(Context context) {
        if (context instanceof FragmentActivity) {
            FragmentActivity activity = (FragmentActivity) context;
            FragmentManager fm = activity.getSupportFragmentManager();
            List<Fragment> fragments = fm.getFragments();
            for (Fragment fragment : fragments) {
                View parent = fragment.getView();
                if (parent != null) {
                    View find = parent.findViewById(getId());
                    if (find == this) {
                        fragment.getLifecycle().addObserver(this);
                        return;
                    }
                }
            }
        }
        if (context instanceof LifecycleOwner) {
            ((LifecycleOwner) context).getLifecycle().addObserver(this);
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    private void onResume() {
        if (mCountDownTimer == null) {
            checkLastCountTimestamp();
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    private void onDestroy() {
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
            mCountDownTimer = null;
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        onDestroy();
    }

    /**
     * 检查持久化参数
     *
     * @return 是否要保持持久化计时
     */
    private boolean checkLastCountTimestamp() {
/*        SharedPreferences sp = getContext().getSharedPreferences(SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE);
        long lastCountTimestamp = sp.getLong(SHARED_PREFERENCES_FIELD_TIMESTAMP + getId(), -1);
        long nowTimeMillis = Calendar.getInstance().getTimeInMillis();
        long diff = lastCountTimestamp - nowTimeMillis;
        if (diff < 0) {
            return false;
        }
        long time = sp.getLong(SHARED_PREFERENCES_FIELD_TIME + getId(), -1);
        long interval = sp.getLong(SHARED_PREFERENCES_FIELD_INTERVAL + getId(), -1);
        boolean isCountDown = sp.getBoolean(SHARED_PREFERENCES_FIELD_COUNTDOWN + getId(), true);
        for (TimeUnit timeUnit : TimeUnit.values()) {
            long covert = timeUnit.convert(interval, TimeUnit.MILLISECONDS);
            if (covert == 1) {
                long last = timeUnit.convert(diff, TimeUnit.MILLISECONDS);
                long offset = time - diff;
                count(last, offset, timeUnit, isCountDown);
                return true;
            }
        }*/
        return false;
    }

    /**
     * 非倒计时状态文本
     *
     * @param normalText 文本
     */
    public CountDownButton setNormalText(String normalText) {
        mNormalText = normalText;
        setText(normalText);
        return this;
    }

    /**
     * 设置倒计时文本内容
     *
     * @param front  倒计时文本前部分
     * @param latter 倒计时文本后部分
     */
    public CountDownButton setCountDownText(String front, String latter) {
        mCountDownText = front + "%1$s" + latter;
        return this;
    }
    public CountDownButton setCountDownText(String end) {
        mCountDownText =   "%1$s" + end;
        return this;
    }
    /**
     * 关闭页面是否保持倒计时
     *
     * @param keep 是否保持
     */
    public CountDownButton setCloseKeepCountDown(boolean keep) {
        mCloseKeepCountDown = keep;
        return this;
    }

    public CountDownButton setCountdownColor(int color) {
        this.textColor = color;
        return this;
    }

    public CountDownButton setCountDefaultColor(int color) {
        this.defaultColor = color;
        return this;
    }

    /**
     * 倒计时期间，点击事件是否响应
     */
    public CountDownButton setCountDownClickable(boolean clickable) {
        mClickable = clickable;
        return this;
    }

    /**
     * 设置倒计时间隔
     *
     * @param intervalUnit
     */
    public CountDownButton setIntervalUnit(TimeUnit intervalUnit) {
        mIntervalUnit = intervalUnit;
        return this;
    }

    /**
     * 是否格式化时间
     *
     * @param formatTime 是否格式化
     */
    public CountDownButton setShowFormatTime(boolean formatTime) {
        mShowFormatTime = formatTime;
        return this;
    }

    /**
     * 顺序计时，非倒计时
     *
     * @param second 计时时间秒
     */
    public void startCount(long second) {
        startCount(second, TimeUnit.SECONDS);
    }

    public void startCount(long time, final TimeUnit timeUnit) {
        if (mCloseKeepCountDown && checkLastCountTimestamp()) {
            return;
        }
        count(time, 0, timeUnit, false);
    }

    /**
     * 默认按秒倒计时
     *
     * @param second 多少秒
     */
    public void startCountDown(long second) {
        startCountDown(second, TimeUnit.SECONDS);
    }


    public void startCountDown(long time, final TimeUnit timeUnit) {
        if (mCloseKeepCountDown && checkLastCountTimestamp()) {
            return;
        }
        this.setTextColor(textColor);
        count(time, 0, timeUnit, true);
    }

    /**
     * 计时方案
     *
     * @param time        计时时长
     * @param timeUnit    时间单位
     * @param isCountDown 是否是倒计时，false正向计时
     */
    private void count(final long time, final long offset, final TimeUnit timeUnit, final boolean isCountDown) {
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
            mCountDownTimer = null;
        }
        CountDownButton.super.setEnabled(mClickable);
        isCounting = true;
        final long millisInFuture = timeUnit.toMillis(time) + 500;
        long interval = TimeUnit.MILLISECONDS.convert(1, mIntervalUnit);
        if (mCloseKeepCountDown && offset == 0) {
            setLastCountTimestamp(millisInFuture, interval, isCountDown);
        }
        if (offset == 0 && mOnCountDownStartListener != null) {
            mOnCountDownStartListener.onStart();
        }
        if (TextUtils.isEmpty(mCountDownText)) {
            mCountDownText = getText().toString();
        }
        mCountDownTimer = new CountDownTimer(millisInFuture, interval) {
            @Override
            public void onTick(long millisUntilFinished) {
                long count = isCountDown ? millisUntilFinished : (millisInFuture - millisUntilFinished + offset);
                long l = timeUnit.convert(count, TimeUnit.MILLISECONDS);
                String showTime;
                if (mShowFormatTime) {
                    showTime = generateTime(count);
                } else {
                    showTime = String.valueOf(l);
                }
                setText(String.format(mCountDownText, showTime));
                setTextColor(textColor);
                if (mOnCountDownTickListener != null) {
                    mOnCountDownTickListener.onTick(l);
                }
            }

            @Override
            public void onFinish() {
                isCounting = false;
                mCountDownTimer = null;
                setText(mNormalText);
                if (mOnCountDownFinishListener != null) {
                    mOnCountDownFinishListener.onFinish();
                }
                CountDownButton.super.setEnabled(true);
                CountDownButton.super.setTextColor(defaultColor);
                CountDownButton.super.setText("重新发送");

            }
        };
        mCountDownTimer.start();
    }

    /**
     * 将毫秒转时分秒
     *
     * @param time
     * @return
     */
    @SuppressLint("DefaultLocale")
    private String generateTime(long time) {
        String format;
        int totalSeconds = (int) (time / 1000);
        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;
        if (hours > 0) {
            format = String.format("%02d时%02d分%02d秒", hours, minutes, seconds);
        } else if (minutes > 0) {
            format = String.format("%02d分%02d秒", minutes, seconds);
        } else {
            format = String.format("%2d秒", seconds);
        }
        return format;
    }

    /**
     * 持久化
     *
     * @param time        倒计时时长
     * @param interval    倒计时间隔
     * @param isCountDown 是否是倒计时而不是正向计时
     */
    @SuppressLint("ApplySharedPref")
    private void setLastCountTimestamp(long time, long interval, boolean isCountDown) {
        getContext().getSharedPreferences(SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE)
                .edit()
                .putLong(SHARED_PREFERENCES_FIELD_TIME + getId(), time)
                .putLong(SHARED_PREFERENCES_FIELD_TIMESTAMP + getId(), Calendar.getInstance().getTimeInMillis() + time)
                .putLong(SHARED_PREFERENCES_FIELD_INTERVAL + getId(), interval)
                .putBoolean(SHARED_PREFERENCES_FIELD_COUNTDOWN + getId(), isCountDown)
                .commit();
    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {
        mOnClickListener = l;
        super.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (mCountDownTimer != null && !mClickable) {
            return;
        }
        if (mOnClickListener != null) {
            mOnClickListener.onClick(view);
        }
    }

    public interface OnCountDownStartListener {
        /**
         * 计时开始回调;反序列化时不会回调
         */
        void onStart();
    }

    public interface OnCountDownTickListener {
        /**
         * 计时回调
         *
         * @param untilFinished 剩余时间，单位为开始计时传入的单位
         */
        void onTick(long untilFinished);
    }

    public interface OnCountDownFinishListener {
        /**
         * 计时结束回调
         */
        void onFinish();
    }

    public CountDownButton setOnCountDownStartListener(OnCountDownStartListener onCountDownStartListener) {
        mOnCountDownStartListener = onCountDownStartListener;
        return this;
    }

    public CountDownButton setOnCountDownTickListener(OnCountDownTickListener onCountDownTickListener) {
        mOnCountDownTickListener = onCountDownTickListener;
        return this;
    }

    public CountDownButton setOnCountDownFinishListener(OnCountDownFinishListener onCountDownFinishListener) {
        mOnCountDownFinishListener = onCountDownFinishListener;
        return this;
    }

    @Override
    public void setEnabled(boolean enabled) {
        if (isCounting) {
            return;
        }
        if(enabled){
            setTextColor(defaultColor);
        }else {
            setTextColor(textColor);
        }
        super.setEnabled(enabled);

    }
}
