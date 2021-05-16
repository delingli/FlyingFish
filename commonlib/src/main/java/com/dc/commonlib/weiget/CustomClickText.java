package com.dc.commonlib.weiget;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

public class CustomClickText extends ClickableSpan {
    private Context context;
    private String textColor = "#F99E03";

    public CustomClickText(Context context, onClickListener clickListener) {
        this.context = context;
        this.onClickListener = clickListener;
    }

    public CustomClickText(Context context, String textColor, onClickListener clickListener) {
        this.context = context;
        this.onClickListener = clickListener;
        this.textColor = textColor;
    }

    private onClickListener onClickListener;

    public interface onClickListener {
        void onClick();
    }

    @Override
    public void onClick(@NonNull View view) {
        if (null != onClickListener) {
            onClickListener.onClick();
        }
    }

    @Override
    public void updateDrawState(@NonNull TextPaint ds) {
        super.updateDrawState(ds);
        ds.setColor(Color.parseColor(textColor));
        //超链接形式的下划线，false 表示不显示下划线，true表示显示下划线
        ds.setUnderlineText(false);
    }
}
