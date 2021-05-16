package com.dc.commonlib.weiget;


import android.content.Context;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;
import android.view.Gravity;

public class DLEditText extends AppCompatEditText {
    public DLEditText(Context context) {
        this(context, null);
    }

    public DLEditText(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    private Context context;

    public DLEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    private void init() {
//        this.setCompoundDrawables(null, null, null, context.getResources().getDrawable(R.drawable.bg_editext_bg));
   this.setGravity(Gravity.LEFT|Gravity.CENTER);
    }
}
