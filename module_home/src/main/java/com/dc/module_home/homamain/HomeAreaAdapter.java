package com.dc.module_home.homamain;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.text.style.TextAppearanceSpan;
import android.widget.TextView;

import com.dc.commonlib.common.BaseRecyclerAdapter;
import com.dc.commonlib.common.BaseViewHolder;
import com.dc.commonlib.utils.UIUtils;
import com.dc.module_home.R;

import java.util.List;

public class HomeAreaAdapter extends BaseRecyclerAdapter<ProjectAreaHomeItem.ProjectAreaItems> {
    /**
     * @param context
     * @param list
     * @param itemLayoutId
     */
    public HomeAreaAdapter(Context context, @Nullable List<ProjectAreaHomeItem.ProjectAreaItems> list, int itemLayoutId) {
        super(context, list, R.layout.home_item_area_item);
    }

    @Override
    protected void convert(BaseViewHolder holder, ProjectAreaHomeItem.ProjectAreaItems projectAreaItems, int position, List<Object> payloads) {
        if (null != projectAreaItems) {
            TextView tv_area_progress = holder.getView(R.id.tv_area_progress);
            TextView tv_proj_adress = holder.getView(R.id.tv_proj_adress);
            String str = projectAreaItems.completedProjects + "/" + projectAreaItems.totalProjects;
            tv_area_progress.setText(getSpannable(str));
            tv_proj_adress.setText(projectAreaItems.projectAdress);
        }
    }

    private Spannable getSpannable(String s) {

        SpannableStringBuilder spanBuilder = new SpannableStringBuilder(s);//text：文字

        spanBuilder.setSpan(new TextAppearanceSpan(null, 0, UIUtils.dip2px(getContext(), 25), null, null),//修改的字体大小

                0, s.lastIndexOf("/"), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        spanBuilder.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, s.lastIndexOf("/"), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);


        return spanBuilder;
/*
        Spannable spannable = new SpannableString(s);

        spannable.setSpan(new AbsoluteSizeSpan(25), 0, s.lastIndexOf("/"), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

//        spannable.setSpan(new ForegroundColorSpan(Color.RED), 11, 16, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        spannable.setSpan(new BackgroundColorSpan(Color.YELLOW), 11, 16, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannable;*/
    }


}
