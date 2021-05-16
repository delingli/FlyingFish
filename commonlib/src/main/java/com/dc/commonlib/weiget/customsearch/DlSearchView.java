package com.dc.commonlib.weiget.customsearch;

import android.content.Context;
import android.support.v7.widget.SearchView;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.dc.commonlib.R;

public class DlSearchView extends SearchView {

    private SearchAutoComplete mEdit;

    public DlSearchView(Context context) {
        this(context, null);
    }

    public DlSearchView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DlSearchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        final int editViewId = context.getResources().getIdentifier("search_src_text", "id", context.getPackageName());
        mEdit = (SearchAutoComplete) this.findViewById(editViewId);
        if (mEdit != null) {
            mEdit.setHintTextColor(getResources().getColor(R.color.text_color3_999999));
            mEdit.setTextColor(getResources().getColor(R.color.black));
            mEdit.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
//            mEdit.setHint(String.format(getResources().getString(R.string.search_hint_tip), MemoryData.departmentList.get(mPosition).getMembers().size()));
        }
        View mView = this.findViewById(R.id.search_plate);
        if (null != mView) {
            mView.setBackgroundResource(R.color.transparent);
        }
    }

    public SearchAutoComplete getmEdit() {
        return mEdit;
    }
}
