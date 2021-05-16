package com.dc.commonlib.commonentity.video.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;


import com.dc.commonlib.R;
import com.dc.commonlib.utils.UIUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 控制台按钮
 *
 * @author xlx
 */
public class ArcMenu extends View {

    /**
     * 弧形按钮点击时颜色
     */
    private static final String BTN_ARC_CLICK_COLOR = "#D1EBFF";
    /**
     * 弧形按钮非点击时颜色
     */
    private static final String BTN_ARC_NORMAL_COLOR = "#e5f5ff";

    /**
     * 中心按钮点击时颜色
     */
    private static final String BTN_CENTER_CLICK_COLOR = "#D1EBFF";

    /**
     * 中心按钮非点击时颜色
     */
    private static final String BTN_CENTER_NORMAL_COLOR = "#d7edff";

    private static final String TAG = ArcAreaData.class.getSimpleName();
    private Paint mPaint;

    private List<ArcAreaData> lists = new ArrayList<>();
    private RectF oval;
    private GestureDetector gestureDetector;

    /**
     * 起始角度
     */
    private float mStartAngle = -112.5f;
    /**
     * 扇形绘制角度
     */
    private static final int ANGLE_ARC_SWEEP = 60;
    private float arcCenterX;
    private float arcCenterY;

    private float deviationDegree;
    private int onClickState = -2;

    private int circleCenterX;
    private int circleCenterY;


    /**
     * 中心按钮半径
     * dp
     */
    private float centerBtnRadius;
    /**
     * 中心按钮比例
     */
    private float centerRatio = 0.13f;

    private Paint mBitmapPaint;
    private Bitmap mArrowNormal;
    private Bitmap mArrowClick;
    /**
     * 中心按钮外边框比例
     */
    private float centerOutSideRatio = 0.14f;
    /**
     * 中心按钮外边框半径
     * dp
     */
    private float centerOutSideRadius;
    private int finalWidth;
    /**
     * 当前点击的按钮索引
     */
    private int onClickPosition;


    public ArcMenu(Context context) {
        super(context, null);
    }

    public ArcMenu(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
        gestureDetector = new GestureDetector(context, gestureListener);
    }


    private GestureDetector.SimpleOnGestureListener gestureListener = new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onDown(MotionEvent e) {
            float downX = e.getX();
            float downY = e.getY();
            int distance = (int) getDisForTwoSpot(circleCenterX, circleCenterY, downX, downY);
            distance = UIUtils.dip2px(getContext(), distance);
            if (distance <= centerBtnRadius) {
                // 如果点击距离小于中心圆半径, 则点击的是中心圆
                onClickState = -1;
                onClickPosition = -1;
                Log.e(TAG, "onSingleTapConfirmed......中心圆点击..");
            } else if (distance <= getWidth() / 2) {
                // 非点击区域
                float sweepAngle = BigDecimal.valueOf(360).divide(BigDecimal.valueOf(lists.size()), 2).floatValue();//每个弧形的角度
                deviationDegree = BigDecimal.valueOf(sweepAngle).divide(BigDecimal.valueOf(2), 2).floatValue();
                int angle = getRotationBetweenLines(circleCenterX, circleCenterY, downX, downY);
                //这个angle的角度是从正Y轴开始，而我们的扇形是从正X轴开始，再加上偏移角度，所以需要计算一下
                angle = (int) ((angle + 360 - 90 - deviationDegree) % 360);
                onClickState = (int) (angle / sweepAngle);//根据角度得出点击的是那个扇形
                for (int i = 0; i < lists.size(); i++) {
                    if (onClickState == i) {
                        onClickPosition = i;
                        Log.e(TAG, "onSingleTapConfirmed......扇形点击.." + i);
                    }
                }
            } else {
                Log.e(TAG, "onSingleTapConfirmed......非点击区域..");
                onClickState = -2;
            }

            postInvalidate();
            return true;
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            if (mOnKeyDownLstener != null) {
                mOnKeyDownLstener.onKeyDown(onClickPosition);
            }
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            if (mOnKeyDownLstener != null) {
                mOnKeyDownLstener.onKeyDown(onClickPosition);
            }
        }
    };

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        if (event.getAction() == MotionEvent.ACTION_UP) {
            onClickState = onClickPosition = -2;
            if (mOnKeyDownLstener != null) {
                mOnKeyDownLstener.onKeyUp(onClickPosition);
            }
            postInvalidate();
        }
        return true;
    }

    private void initView() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBitmapPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mArrowNormal = BitmapFactory.decodeResource(getResources(), R.drawable.btn_arrow_normal);
        mArrowClick = BitmapFactory.decodeResource(getResources(), R.drawable.btn_arrow_click);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        arcCenterX = (getWidth() - getHeight() / 2) / 2;
        arcCenterY = getHeight() / 4;
        centerBtnRadius = getWidth() / 2 * centerRatio;
        centerOutSideRadius = getWidth() / 2 * centerOutSideRatio;
        oval = new RectF(arcCenterX, arcCenterY, getWidth() - arcCenterX, getHeight() - arcCenterY);
        circleCenterX = getWidth() / 2;
        circleCenterY = getHeight() / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawArcButton(canvas);
        drawOutSideCircle(canvas);
        drawCenterButton(canvas);
        drawArrow(canvas);
    }


    /**
     * 绘制箭头图片
     */
    private void drawArrow(Canvas canvas) {
        canvas.save();
        int size = lists.size();
        for (int i = 0; i < size; i++) {
            canvas.rotate(45, circleCenterX, circleCenterY);
            Matrix matrix = new Matrix();
            matrix.postTranslate(circleCenterX + UIUtils.dip2px(getContext(), 50), circleCenterY - mArrowNormal.getHeight() / 2);
            matrix.postRotate(0);//顺时针旋转45度
            if (onClickState == i) {
                canvas.drawBitmap(mArrowClick, matrix, mBitmapPaint);
            } else {
                canvas.drawBitmap(mArrowNormal, matrix, mBitmapPaint);
            }
        }
        canvas.restore();
    }

    /**
     * 绘制中心圆按钮
     */
    private void drawCenterButton(Canvas canvas) {
        if (onClickState == -1) {
            // 中心圆点击
            mPaint.setColor(Color.parseColor(BTN_CENTER_CLICK_COLOR));
        } else {
            mPaint.setColor(Color.parseColor(BTN_CENTER_NORMAL_COLOR));
        }
        int i = UIUtils.dip2px(getContext(), (int)centerBtnRadius);
        canvas.drawCircle(circleCenterX, circleCenterY, UIUtils.dip2px(getContext(), (int)centerBtnRadius), mPaint);
    }

    /**
     * 绘制中心圆的外边框
     */
    private void drawOutSideCircle(Canvas canvas) {
        mPaint.setColor(Color.WHITE);
        canvas.drawCircle(circleCenterX, circleCenterY, UIUtils.dip2px(getContext(), (int)centerOutSideRadius), mPaint);
    }

    /**
     * 绘制弧形按钮
     */
    private void drawArcButton(Canvas canvas) {
        if (lists != null && lists.size() > 0) {
            int size = lists.size();
            for (int i = 0; i < size; i++) {
                float sweepAngle = BigDecimal.valueOf(360).divide(BigDecimal.valueOf(size), 2).floatValue();//每个弧形的角度
                deviationDegree = BigDecimal.valueOf(sweepAngle).divide(BigDecimal.valueOf(2), 2).floatValue();
                ArcAreaData arcAreaData = lists.get(i);
                mPaint.setColor(Color.parseColor(BTN_ARC_NORMAL_COLOR));
                if (onClickState == i) {
                    //选中
                    mPaint.setColor(Color.parseColor(BTN_ARC_CLICK_COLOR));
                } else {
                    //未选中
                    mPaint.setColor(Color.parseColor(BTN_ARC_NORMAL_COLOR));
                }
                canvas.drawArc(oval, deviationDegree + (i * sweepAngle), ANGLE_ARC_SWEEP, true, mPaint);
                mStartAngle += ANGLE_ARC_SWEEP;
                arcAreaData.startAngle = mStartAngle;
                arcAreaData.sweepAngle = ANGLE_ARC_SWEEP;
                lists.set(i, arcAreaData);
            }
        }
    }

    public void addArcData(List<ArcAreaData> lists) {
        this.lists.addAll(lists);
        postInvalidate();
    }

    public void hide() {
        setVisibility(GONE);
    }

    public void show() {
        setVisibility(VISIBLE);
    }

    public boolean isShowing() {
        return getVisibility() == VISIBLE;
    }

    public static class ArcAreaData {
        String normalColor;
        String clickColor;
        float startAngle;
        int sweepAngle;

        public ArcAreaData() {
        }

        public ArcAreaData(String normalColor, String clickColor, float startAngle, int sweepAngle) {
            this.normalColor = normalColor;
            this.clickColor = clickColor;
            this.startAngle = startAngle;
            this.sweepAngle = sweepAngle;
        }
    }

    /**
     * 求两个点之间的距离
     */
    public static double getDisForTwoSpot(float x1, float y1, float x2, float y2) {
        float width = x1 > x2 ? x1 - x2 : x2 - x1;
        float height = y1 > y2 ? y1 - y2 : y2 - y1;
        return Math.sqrt((width * width) + (height * height));
    }

    /**
     * 获取两条线的夹角
     *
     * @param centerX
     * @param centerY
     * @param xInView
     * @param yInView
     * @return
     */
    public static int getRotationBetweenLines(float centerX, float centerY, float xInView,
                                              float yInView) {
        double rotation = 0;

//        double k1 = (double) (centerY - centerY) / (centerX * 2 - centerX);
        double k1 = 0;
        double k2 = (double) (yInView - centerY) / (xInView - centerX);
        double tmpDegree = Math.atan((Math.abs(k1 - k2)) / (1 + k1 * k2)) / Math.PI * 180;

        if (xInView > centerX && yInView < centerY) {  //第一象限
            rotation = 90 - tmpDegree;
        } else if (xInView > centerX && yInView > centerY) //第二象限
        {
            rotation = 90 + tmpDegree;
        } else if (xInView < centerX && yInView > centerY) { //第三象限
            rotation = 270 - tmpDegree;
        } else if (xInView < centerX && yInView < centerY) { //第四象限
            rotation = 270 + tmpDegree;
        } else if (xInView == centerX && yInView < centerY) {
            rotation = 0;
        } else if (xInView == centerX && yInView > centerY) {
            rotation = 180;
        }
        return (int) rotation;
    }

//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        int arcHeight = (int) (oval.top - oval.bottom);
//        super.onMeasure(widthMeasureSpec, arcHeight);
//    }

    OnKeyDownLstener mOnKeyDownLstener;

    /**
     * 按钮的点击监听
     */
    public interface OnKeyDownLstener {
        void onKeyDown(int position);

        void onKeyUp(int position);
    }

    public void setOnKeyClickLstener(OnKeyDownLstener listener) {
        this.mOnKeyDownLstener = listener;
    }
//    /** 中心按钮*/
//    public static final int KEY_CENTER = -1;
    /**
     * 右上
     */
    public static final int KEY_RIGHT_DOWN = 0;
    /**
     * 下
     */
    public static final int KEY_DOWN = 1;
    /**
     * 左上
     */
    public static final int KEY_LEFT_DOWN = 2;
    /**
     * 左
     */
    public static final int KEY_LEFT = 3;
    /**
     * 左下
     */
    public static final int KEY_LEFT_UP = 4;
    /**
     * 上
     */
    public static final int KEY_UP = 5;
    /**
     * 右上
     */
    public static final int KEY_RIGHT_UP = 6;
    /**
     * 右
     */
    public static final int KEY_RIGHT = 7;

}
