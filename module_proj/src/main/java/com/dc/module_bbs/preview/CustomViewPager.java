package com.dc.module_bbs.preview;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class CustomViewPager extends ViewPager implements View.OnTouchListener {
    public CustomViewPager(@NonNull Context context) {
        super(context);
    }

    private long interval = 3000;
    private int currentPosition = 0;

    public CustomViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setOnTouchListener(this);
    }

    private Handler mHandler = new Handler(Looper.getMainLooper());
    Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            if (getChildCount() > 1) {
                mHandler.postDelayed(this, interval);
                currentPosition++;
                if (currentPosition >= getChildCount()) {
                    currentPosition = 0;
                }
                setCurrentItem(currentPosition, true);
            }
        }
    };

    private boolean isLoop = false;

    public void stopLoop() {
        if (isLoop) {
            mHandler.removeCallbacks(mRunnable);
            isLoop = false;
        }
    }


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mHandler.removeCallbacksAndMessages(null);
        mHandler = null;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                isLoop = true;
                stopLoop();
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                isLoop = false;
                startLoop();
            default:
                break;
        }
        return false;
    }

    public void startLoop() {
        if (!isLoop) {
            mHandler.postDelayed(mRunnable, interval);// 每两秒执行一次runnable.
            isLoop = true;
        }
    }

}
