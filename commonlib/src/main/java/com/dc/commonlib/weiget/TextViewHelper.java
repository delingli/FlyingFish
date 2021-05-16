package com.dc.commonlib.weiget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.text.Spanned;

import com.dc.commonlib.R;

public class TextViewHelper {
    /**
     * @param context 上下文
     * @param msg     商品名称
     * @return
     */
    public static SpannableString setLeftImage(Context context, int id, String msg) {

        SpannableString spannableString = new SpannableString("  " + msg);
        if(id==0){
            return spannableString;
        }
        Drawable rightDrawable = context.getResources().getDrawable(id);
        rightDrawable.setBounds(0, 0, rightDrawable.getIntrinsicWidth(), rightDrawable.getIntrinsicHeight());
        spannableString.setSpan(new MyImageSpan(rightDrawable), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }
}
