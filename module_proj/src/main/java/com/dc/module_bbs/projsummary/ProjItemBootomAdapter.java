package com.dc.module_bbs.projsummary;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.dc.commonlib.common.BaseRecyclerAdapter;
import com.dc.commonlib.common.BaseViewHolder;
import com.dc.module_bbs.R;

import java.util.List;

public class ProjItemBootomAdapter extends BaseRecyclerAdapter<String> {
    /**
     * @param context
     * @param list
     * @param itemLayoutId
     */
    public ProjItemBootomAdapter(Context context, @Nullable List<String> list, int itemLayoutId) {
        super(context, list, R.layout.proj_litem_progress);
    }

    @Override
    protected void convert(BaseViewHolder holder, String bottomProcessItem, int position, List<Object> payloads) {
        TextView tv_title = holder.getView(R.id.tv_title);
        tv_title.setText(bottomProcessItem);
    }
}
