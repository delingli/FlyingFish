package com.guangzhou.station.stationmain;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.dc.commonlib.common.BaseRecyclerAdapter;
import com.dc.commonlib.common.BaseViewHolder;
import com.guangzhou.station.R;

import java.util.ArrayList;
import java.util.List;

public class ThreeListAdapter extends BaseRecyclerAdapter<AbsStationData> {
    /**
     * @param context
     * @param list
     * @param itemLayoutId
     */
    public ThreeListAdapter(Context context, @Nullable @org.jetbrains.annotations.Nullable List<AbsStationData> list, int itemLayoutId) {
        super(context, list, R.layout.station_item_three);
    }

    @Override
    protected void convert(BaseViewHolder holder, AbsStationData absStationData, int position, List<Object> payloads) {
        if (absStationData instanceof ProjectListBean.DirectoryListBean.ShowListBean) {
            ProjectListBean.DirectoryListBean.ShowListBean threelistdata = (ProjectListBean.DirectoryListBean.ShowListBean) absStationData;
            TextView tv_txt = holder.getView(R.id.tv_txt);
            Typeface typeFace = Typeface.createFromAsset(getContext().getAssets(), "fonts/MSYH.TTC");
            tv_txt.setTypeface(typeFace);
            tv_txt.setText(threelistdata.name);
        }
    }

    public void addListBeanList(List<AbsStationData> itemlist) {
        if (dataList != null) {
            dataList.clear();
        } else {
            dataList = new ArrayList<>();
        }
        dataList.addAll(itemlist);
        notifyDataSetChanged();


    }

}
