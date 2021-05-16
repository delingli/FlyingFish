package com.dc.commonlib.weiget.givelike;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.dc.commonlib.R;


public class DLCustomGiveLikeView extends FrameLayout {

    private TextView tvGivelike;
    private Drawable drawableSrc;
    private Drawable selectedDrawable;
    private DLCustomPopupWindow mPopupWindow;
    private boolean selected;
    private int mHeight, mWidth;
    private int count;
    BitmapDrawable selectBitmapDrawable, drawableBitmapDrawable;

    public DLCustomGiveLikeView(@NonNull Context context) {
        this(context, null);
    }

    public DLCustomGiveLikeView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public DLCustomGiveLikeView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public DLCustomGiveLikeView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr);
    }

    public void setCount(int count) {
        if (count >= 0) {
            this.count = count;
            toSelected(selected);
        }
    }

    public void addOnItemClickListener(OnItemClickListener itemClickListener) {
        this.onItemClickListener = itemClickListener;
    }

    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(boolean liked);
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
        toSelected(selected);
    }

    private boolean enable = true;

    public void setEnableClick(boolean enable) {
        this.enable = enable;
    }

    private void init(final Context context, AttributeSet attrs, int defStyleAttr)
    {
        LayoutInflater.from(context).inflate(R.layout.custom_givelike_layout, this, true);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.DLCustomGiveLikeView, defStyleAttr, 0);
        selectedDrawable = array.getDrawable(R.styleable.DLCustomGiveLikeView_drawableSelectedSrc);
        drawableSrc = array.getDrawable(R.styleable.DLCustomGiveLikeView_drawableSrc);
        mWidth = (int) array.getDimension(R.styleable.DLCustomGiveLikeView_drawableWidth, 40);
        mHeight = (int) array.getDimension(R.styleable.DLCustomGiveLikeView_drawableHeight, 30);
        array.recycle();
        tvGivelike = findViewById(R.id.tv_givelike);
        if (selectedDrawable != null) {
            Bitmap bitmap = ((BitmapDrawable) selectedDrawable).getBitmap();
            selectBitmapDrawable = new BitmapDrawable(getResources(), bitmap);
            selectBitmapDrawable.setBounds(0, 0, selectBitmapDrawable.getIntrinsicWidth(), selectBitmapDrawable.getIntrinsicHeight());

        }
        if (drawableSrc != null) {
            Bitmap drawableSrcbitmap = ((BitmapDrawable) drawableSrc).getBitmap();
            drawableBitmapDrawable = new BitmapDrawable(getResources(), drawableSrcbitmap);
            drawableBitmapDrawable.setBounds(0, 0, drawableBitmapDrawable.getIntrinsicWidth(), drawableBitmapDrawable.getIntrinsicHeight());
        }
        mPopupWindow = new DLCustomPopupWindow(context);

        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!enable) {
                    return;
                }
                if (!mPopupWindow.isShowing()) {
                    if (selected) {
                        if (onItemClickListener != null) {
                            onItemClickListener.onItemClick(false);
                        }
                        --count;
                    } else {
                        ++count;
                        if (onItemClickListener != null) {
                            onItemClickListener.onItemClick(true);
                        }
                    }
                    selected = !selected;
                    toSelected(selected);

                    mPopupWindow.show(DLCustomGiveLikeView.this);
                }

            }
        });

    }


    private void toSelected(boolean selected) {
        if (selected) {
            mPopupWindow.setText("+1");
            tvGivelike.setCompoundDrawables(selectBitmapDrawable, null, null, null);
            tvGivelike.setText(count + "");
        } else {
            mPopupWindow.setText("-1");
            if (count <= 0) {
                count = 0;
            }
            tvGivelike.setCompoundDrawables(drawableBitmapDrawable, null, null, null);
            tvGivelike.setText(count + "");
        }

    }


}
