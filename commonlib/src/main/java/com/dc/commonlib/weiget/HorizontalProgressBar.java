package com.dc.commonlib.weiget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.dc.commonlib.R;

/**
 * TODO: document your custom view class.
 */
public class HorizontalProgressBar extends View {

    private int BgColor = Color.GRAY;
    private Paint paint;
    private RectF rectF;
    private float roundedCornersRadius = 6f;
    private Paint progressPaint;
    private int progressColor = Color.RED;
    private int progress = 0;
    private int height;
    private int maxProgress = 100;
    private int width;

    protected int dp2px(int dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, getResources().getDisplayMetrics());
    }

    public HorizontalProgressBar(Context context) {
        super(context);
        init(null, 0);
    }

    public HorizontalProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public HorizontalProgressBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    public void setBgColor(int bgColor) {
        this.BgColor = bgColor;
        invalidate();
    }
    public void setProgressColor(int progressColor) {
        this.progressColor = progressColor;
        invalidate();

    }

    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.HorizontalProgressBar, defStyle, 0);

        BgColor = a.getColor(
                R.styleable.HorizontalProgressBar_BgColor,
                BgColor);
        progressColor = a.getColor(
                R.styleable.HorizontalProgressBar_ProgressColor,
                progressColor);


        // Use getDimensionPixelSize or getDimensionPixelOffset when dealing with
        // values that should fall on pixel boundaries.
        roundedCornersRadius = a.getDimension(
                R.styleable.HorizontalProgressBar_RoundedCornersRadius,
                roundedCornersRadius);

        progress = a.getInt(
                R.styleable.HorizontalProgressBar_Progress,
                progress);
        maxProgress = a.getInt(
                R.styleable.HorizontalProgressBar_MaxProgress,
                maxProgress);

        a.recycle();

        paint = new Paint();
        paint.setAntiAlias(true);

        progressPaint = new Paint();
        progressPaint.setAntiAlias(true);

        rectF = new RectF();
    }

    public void setProgress(int progress) {
        if (progress > maxProgress) {
            this.progress = maxProgress;
        } else {
            this.progress = progress;
        }
        invalidate();
    }

    public int getProgress() {
        return progress;
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        height = h - getPaddingTop() - getPaddingBottom();
        width = w - getPaddingRight() - getPaddingLeft();
        rectF.set(0, 0, w, h);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setColor(BgColor);
        progressPaint.setColor(progressColor);
        drawBg(canvas);
        canvas.save();
        canvas.restore();
        drawProgress(canvas);
    }


    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        width = MeasureSpec.getSize(widthMeasureSpec);
        height = measureHeight(heightMeasureSpec);
        setMeasuredDimension(width, height);//必须调用该方法来存储view经过测量的到
    }

    private int measureHeight(int measureSpec) {
        int result = dp2px(20);
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else if (specMode == MeasureSpec.AT_MOST) {
            result = Math.min(result, specSize);
        }
        return result;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void drawProgress(Canvas canvas) {
        float radio = progress * 1.0f / maxProgress;
        float currentProgress = radio * width;
        canvas.drawRoundRect(0, 0, currentProgress, height, dp2px((int) roundedCornersRadius), dp2px((int) roundedCornersRadius), progressPaint);
    }

    private void drawBg(Canvas canvas) {
        canvas.drawRoundRect(rectF, dp2px((int) roundedCornersRadius), dp2px((int) roundedCornersRadius), paint);
    }
}
