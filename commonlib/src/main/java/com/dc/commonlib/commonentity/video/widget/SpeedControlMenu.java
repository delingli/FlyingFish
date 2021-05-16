package com.dc.commonlib.commonentity.video.widget;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.dc.commonlib.R;

public class SpeedControlMenu extends LinearLayout {
    RadioGroup mSpeedList;

    private OnSpeedChangeListener mOnSpeedChangeListener;

    public SpeedControlMenu(Context context, String displayMode) {
        super(context);
        initView();
    }

    public SpeedControlMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        View inflate = LayoutInflater.from(getContext()).inflate(R.layout.view_cotrol_menu, this, true);
        mSpeedList = inflate.findViewById(R.id.speed_radio);
        setBackgroundColor(Color.parseColor("#aa000000"));
        mSpeedList.check(R.id.speed_normal);
        changeCheckStyle(R.id.speed_normal);
        mSpeedList.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (mOnSpeedChangeListener != null) {
                    PlaySpeed playSpeed = PlaySpeed.Normal;
                    if (checkedId == R.id.speed_half) {
                        playSpeed = PlaySpeed.Half;
                    } else if (checkedId == R.id.speed_normal) {
                        playSpeed = PlaySpeed.Normal;
                    } else if (checkedId == R.id.speed_twice) {
                        playSpeed = PlaySpeed.Twice;
                    } else if (checkedId == R.id.speed_four) {
                        playSpeed = PlaySpeed.Four;
                    } else {
                        playSpeed = PlaySpeed.Normal;
                    }
                    SpeedControlMenu.this.changeCheckStyle(checkedId);
                    mOnSpeedChangeListener.onSpeedChanged(playSpeed);
                }
            }
        });
    }

    private void changeCheckStyle(int checkedId) {
        int childCount = mSpeedList.getChildCount();
        for (int i = 0; i < childCount; i++) {
            RadioButton radioButton = (RadioButton) mSpeedList.getChildAt(i);
            if (radioButton.getId() == checkedId) {
                radioButton.setTextColor(ContextCompat.getColor(getContext(), R.color.text_color_0d7aff));
            } else {
                radioButton.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
            }
        }
    }

    public void show() {
        setVisibility(VISIBLE);
    }

    public void hide(){
        setVisibility(GONE);
    }

    public boolean isShowing() {
        return getVisibility() == VISIBLE;
    }

    /**
     * 速度切换
     */
    public interface OnSpeedChangeListener {
        void onSpeedChanged(PlaySpeed speed);
    }

    public void setOnSpeedChangeListener(OnSpeedChangeListener listener) {
        this.mOnSpeedChangeListener = listener;
    }

    /**
     * 播放速度
     */
    public enum PlaySpeed {
        /**
         * 0.5倍
         */
        Half,
        /**
         * 1倍
         */
        Normal,
        /**
         * 2倍
         */
        Twice,
        /**
         * 4倍
         */
        Four
    }
}
