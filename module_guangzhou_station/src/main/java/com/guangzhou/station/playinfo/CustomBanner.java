package com.guangzhou.station.playinfo;

import android.content.Context;
import android.util.AttributeSet;

import com.youth.banner.Banner;

public class CustomBanner extends Banner {
    public CustomBanner(Context context) {
        super(context);
    }

    public CustomBanner(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomBanner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onAttachedToWindow() {
        //解bug
//        isAutoLoop(false);
        super.onAttachedToWindow();
        stop();
    }


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }
}
