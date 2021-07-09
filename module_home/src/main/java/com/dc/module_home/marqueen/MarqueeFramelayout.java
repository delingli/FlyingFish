package com.dc.module_home.marqueen;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextClock;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.dc.module_home.R;

import java.util.List;

public class MarqueeFramelayout extends FrameLayout {

    private FrameLayout fl_container;
    private SimpleMarqueeView marqueeView;

    public MarqueeFramelayout(@NonNull Context context) {
        this(context, null);
    }

    public MarqueeFramelayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public MarqueeFramelayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, -1);
    }

    public MarqueeFramelayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.marquee_marqueeframelayout, this, true);
        fl_container = findViewById(R.id.fl_container);
        marqueeView = findViewById(R.id.marqueeView);
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.MarqueeFramelayouts);
        int color = attributes.getColor(R.styleable.MarqueeFramelayouts_marqueeBackgroundColor, Color.BLACK);
        attributes.recycle();
        fl_container.setBackgroundColor(color);
    }

    public void setBackground(int background) {
        if (null != fl_container) {
            fl_container.setBackgroundColor(background);
        }
//        marqueeView6 = findViewById(R.id.marqueeView6);
//        TextClock textclock=  findViewById(R.id.tv_time);
//        final SimpleMF<String> marqueeFactory = new SimpleMF<>(this);
//        marqueeFactory.setData(datas);
////        marqueeView6.setOnItemClickListener(onSimpleItemClickListener);
//        marqueeView6.setMarqueeFactory(marqueeFactory);
//        marqueeView6.startFlipping();
    }

    public void startMarquee(List<String> datas) {
        final SimpleMF<String> marqueeFactory = new SimpleMF<>(getContext());
        if (null != datas) {
            marqueeFactory.setData(datas);
        }
        marqueeView.setMarqueeFactory(marqueeFactory);
        marqueeView.startFlipping();
    }
}
