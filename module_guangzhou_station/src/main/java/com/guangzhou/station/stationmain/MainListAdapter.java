package com.guangzhou.station.stationmain;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.dc.commonlib.common.BaseRecyclerAdapter;
import com.dc.commonlib.common.BaseViewHolder;
import com.guangzhou.station.R;

import java.util.List;

public class MainListAdapter extends BaseRecyclerAdapter<AbsStationData> {

    private final Typeface typeFace;

    /**
     * @param context
     * @param list
     * @param itemLayoutId
     */
    public MainListAdapter(Context context, @Nullable @org.jetbrains.annotations.Nullable List<AbsStationData> list, int itemLayoutId) {
        super(context, list, R.layout.station_item_main);
        typeFace = Typeface.createFromAsset(getContext().getAssets(), "fonts/MSYH.TTC");
    }

    public ProjectListBean.DirectoryListBean notifySelect(int position) {
        if (getList() != null) {
            AbsStationData ll = getList().get(position);
            if (ll instanceof ProjectListBean.DirectoryListBean) {
                ProjectListBean.DirectoryListBean lp = (ProjectListBean.DirectoryListBean) ll;
                lp.selected = true;
                notifyDataSetChanged();
                return lp;
            }

        }
        return null;
    }

    public interface OnItemClickListener {
        void onItemsClick( ProjectListBean.DirectoryListBean directoryListBean);
    }

    private OnItemClickListener onItemClickListener;

    public void addOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    protected void convert(BaseViewHolder holder, AbsStationData absStationData, int position, List<Object> payloads) {
        if (absStationData instanceof ProjectListBean.DirectoryListBean) {
            ProjectListBean.DirectoryListBean mainListData = (ProjectListBean.DirectoryListBean) absStationData;
            TextView tv_txt = holder.getView(R.id.tv_txt);
            LinearLayout ll_item = holder.getView(R.id.ll_item);
            if (mainListData.selected) {
                ll_item.setBackgroundResource(R.drawable.station_main_selector_selector);
            } else {
                ll_item.setBackgroundResource(R.drawable.station_main_selector_default);
            }
            tv_txt.setTypeface(typeFace);
            tv_txt.setText(mainListData.name);
            ll_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    refreshSelectList();
                    mainListData.selected = true;
                    notifyDataSetChanged();
                    if(null!=onItemClickListener){
                        onItemClickListener.onItemsClick(mainListData);
                    }

                }
            });

        }
//        station_item_three
    }

    void refreshSelectList() {
        if (getList() != null) {
            for (AbsStationData ll : getList()) {
                if (ll instanceof ProjectListBean.DirectoryListBean) {
                    ProjectListBean.DirectoryListBean lp = (ProjectListBean.DirectoryListBean) ll;
                    lp.selected = false;
                }

            }
        }
    }
}
