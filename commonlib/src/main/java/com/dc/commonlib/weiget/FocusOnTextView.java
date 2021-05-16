package com.dc.commonlib.weiget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import com.dc.commonlib.R;


public class FocusOnTextView extends android.support.v7.widget.AppCompatTextView implements View.OnClickListener {
    public FocusOnTextView(Context context) {
        super(context);
        init();
    }

    private void init() {
        this.setOnClickListener(this);
    }

    public FocusOnTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FocusOnTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private boolean focusOn = false;//关注了吗

    public void setFocusOn(boolean focusOn) {
        this.focusOn = focusOn;
        flushText();
        flushLeftDrawable();
    }

    public void flushText() {
        if (focusOn) {
            setText(R.string.focus_on);
        } else {
            setText(R.string.no_focus_on);

        }
    }

    public void flushLeftDrawable() {
        if (focusOn) {
            setCompoundDrawables(null, null, null, null);
            setBackgroundResource(R.drawable.bg_gray_bg);

        } else {
            Drawable drawable = getContext().getResources().getDrawable(R.drawable.add);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            setCompoundDrawables(drawable, null, null, null);
            setBackgroundResource(R.drawable.bg_login_bg);

        }
    }

    @Override
    public void onClick(View v) {
        if (!focusOn) { //没关注
            if (onItemClickListener != null) {
                onItemClickListener.onClick(true);
            }
        } else {
            if (onItemClickListener != null) {
                onItemClickListener.onClick(false);//取消
            }
        }
        focusOn = !focusOn;
        flushText();
        flushLeftDrawable();
    }

    private OnItemClickListener onItemClickListener;

    public void addOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onClick(boolean toFocusOn);
    }
}
