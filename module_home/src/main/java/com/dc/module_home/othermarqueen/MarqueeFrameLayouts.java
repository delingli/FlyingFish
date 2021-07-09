package com.dc.module_home.othermarqueen;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.dc.module_home.R;

public class MarqueeFrameLayouts extends FrameLayout {

    private FrameLayout fl_container;
    private MarqueeView marqueeView;

    public MarqueeFrameLayouts(@NonNull Context context) {
        this(context, null);
    }

    public MarqueeFrameLayouts(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public MarqueeFrameLayouts(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, -1);
    }

    public MarqueeFrameLayouts(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.marquee_marqueeframelayouts, this, true);
        fl_container = findViewById(R.id.fl_container);
        marqueeView = findViewById(R.id.mMarqueeView);
//        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.MarqueeFramelayouts);
//        int color = attributes.getColor(R.styleable.MarqueeFramelayouts_marqueeBackgroundColor, Color.BLACK);
//        attributes.recycle();

    }

    //      mMarqueeView.setText("依据赫兹接触强度计算理论，着重研究了圆柱滚子轴承内、外圈及滚动体的接触应力");
//        mMarqueeView.startScroll();
    public void setText(String str) {
        marqueeView.setText(str);
    }

    public void startScroll() {
        marqueeView.startScroll();
    }

    public void setBackground(int background) {
        if (null != fl_container) {
            fl_container.setBackgroundColor(background);
        }
    }

}
