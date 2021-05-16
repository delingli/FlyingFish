package com.dc.commonlib.weiget.courselist;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.PopupWindow;

public class DLCoursePopupWindow extends PopupWindow {
    private Context context;

    public DLCoursePopupWindow(Context context ) {
        super(context);
        this.context = context;
        initView();
    }

    private void initView() {
    }


}
