package com.dc.commonlib.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;

public class HiddenAnimUtils {

    private int mHeight;//伸展高度

    private View hideView, down;//需要展开隐藏的布局，开关控件

    private RotateAnimation animation;//旋转动画

    private int degrees = 180; // 旋转角度
    private ObjectAnimator objectAnimator;

    /**
     * 构造器(可根据自己需要修改传参)
     *
     * @param context  上下文
     * @param hideView 需要隐藏或显示的布局view
     * @param down     按钮开关的view
     * @param height   布局展开的高度(根据实际需要传)
     */
    public static HiddenAnimUtils newInstance(Context context, View hideView, View down, int height) {
        return new HiddenAnimUtils(context.getApplicationContext(), hideView, down, height);
    }

    public static HiddenAnimUtils newInstance(Context context, View hideView, View down, int height, int degrees) {
        return new HiddenAnimUtils(context.getApplicationContext(), hideView, down, height, degrees);
    }

    private HiddenAnimUtils(Context context, View hideView, View down, int height) {
        this.hideView = hideView;
        this.down = down;
        float mDensity = context.getResources().getDisplayMetrics().density;
        mHeight = (int) (mDensity * height + 0.5);//伸展高度
    }

    private HiddenAnimUtils(Context context, View hideView, View down, int height, int degrees) {
        this.hideView = hideView;
        this.down = down;
        float mDensity = context.getResources().getDisplayMetrics().density;
        mHeight = (int) (mDensity * height + 0.5);//伸展高度
        this.degrees = degrees;
    }

    /**
     * 开关
     */
    public void toggle() {
        startAnimation();
        if (View.VISIBLE == hideView.getVisibility()) {
            closeAnimate(hideView);//布局隐藏
        } else {
            openAnim(hideView);//布局铺开
        }
    }

    public void valueToggle() {
        startValueAnimation();
        if (View.VISIBLE == hideView.getVisibility()) {
            closeAnimate(hideView);//布局隐藏
        } else {
            openAnim(hideView);//布局铺开
        }
    }

    private void startValueAnimation() {
        if (null != down) {
            if (View.VISIBLE == hideView.getVisibility()) {
                Log.d("startAnimation", "startAnimation:90-0");
                objectAnimator = ObjectAnimator.ofFloat(down, "rotation", degrees, 0);

            } else {
                Log.d("startAnimation", "startAnimation:0-90");
                objectAnimator = ObjectAnimator.ofFloat(down, "rotation", 0, degrees);
            }
            objectAnimator.setDuration(30);//设置动画持续时间
            objectAnimator.setInterpolator(new LinearInterpolator());
            objectAnimator.start();
        }
    }

    /**
     * 开关旋转动画
     */
    private void startAnimation() {
        if (null != down) {
            if (View.VISIBLE == hideView.getVisibility()) {
                Log.d("startAnimation", "startAnimation:90-0");
                animation = new RotateAnimation(degrees, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            } else {
                Log.d("startAnimation", "startAnimation:0-90");
                animation = new RotateAnimation(0, degrees, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            }
            animation.setDuration(30);//设置动画持续时间
            animation.setInterpolator(new LinearInterpolator());
            animation.setRepeatMode(Animation.REVERSE);//设置反方向执行
            animation.setFillAfter(true);//动画执行完后是否停留在执行完的状态
            down.startAnimation(animation);
        }
    }

    private void openAnim(View v) {
        v.setVisibility(View.VISIBLE);
        ValueAnimator animator = createDropAnimator(v, 0, mHeight);
        animator.start();
    }

    private void closeAnimate(final View view) {
        int origHeight = view.getHeight();
        ValueAnimator animator = createDropAnimator(view, origHeight, 0);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                view.setVisibility(View.GONE);
            }
        });
        animator.start();
    }

    private ValueAnimator createDropAnimator(final View v, int start, int end) {
        ValueAnimator animator = ValueAnimator.ofInt(start, end);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator arg0) {
                int value = (int) arg0.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = v.getLayoutParams();
                layoutParams.height = value;
                v.setLayoutParams(layoutParams);
            }
        });
        return animator;
    }
}
