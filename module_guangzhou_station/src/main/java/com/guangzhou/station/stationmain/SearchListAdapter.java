package com.guangzhou.station.stationmain;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.dc.commonlib.common.BaseRecyclerAdapter;
import com.dc.commonlib.common.BaseViewHolder;
import com.guangzhou.station.R;

import java.util.ArrayList;
import java.util.List;

public class SearchListAdapter extends BaseRecyclerAdapter<KeywordListBean.ListBean> {

    /**
     * @param context
     * @param list
     * @param itemLayoutId
     */
    public SearchListAdapter(Context context, @Nullable @org.jetbrains.annotations.Nullable List<KeywordListBean.ListBean> list, int itemLayoutId) {
        super(context, list, R.layout.search_item_list);
    }


    public interface OnItemClickListener {
        void onItemsClick(KeywordListBean.ListBean listBeans, int position);
    }

    private OnItemClickListener onItemClickListener;

    public void addOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    protected void convert(BaseViewHolder holder, KeywordListBean.ListBean absStationData, int position, List<Object> payloads) {
        if (null != absStationData) {
            TextView tv_text = holder.getView(R.id.tv_text);
            tv_text.setText(absStationData.name);
            tv_text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (null != onItemClickListener) {
                        onItemClickListener.onItemsClick(absStationData, position);
                    }
                }
            });
        }

    }

    public void clearData() {
        if (getList() != null) {
            getList().clear();
        }
        notifyDataSetChanged();
    }

}
