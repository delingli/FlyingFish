package com.dc.commonlib.weiget.courselist;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dc.commonlib.R;
import com.dc.commonlib.common.BaseRecyclerAdapter;
import com.dc.commonlib.common.BaseViewHolder;
import com.dc.commonlib.weiget.horizontalrecycle.DLHorizontalItem;

import java.util.List;

public class DlPopupWindowAdapter extends BaseRecyclerAdapter<DLHorizontalItem> {
    private int selection;

    public void setSelection(int selection) {
        this.selection = selection;
        notifyDataSetChanged();
    }

    /**
     * @param context
     * @param list
     * @param itemLayoutId
     */
    public DlPopupWindowAdapter(Context context, @Nullable List<DLHorizontalItem> list, int itemLayoutId) {
        super(context, list, R.layout.common_dl_popupwindow_item);
    }

    @Override
    protected void convert(BaseViewHolder holder, DLHorizontalItem dlHorizontalItem, int position, List<Object> payloads) {
        if (null != dlHorizontalItem) {
            TextView tv_item = holder.getView(R.id.tv_item);
            tv_item.setText(dlHorizontalItem.name);
            if (selection != position) {
                tv_item.setTextColor(Color.parseColor("#333333"));
               tv_item.setBackgroundResource(R.drawable.press_button);
            } else {
                tv_item.setBackgroundResource(R.drawable.bg_login_bg);
                tv_item.setTextColor(Color.parseColor("#ffffff"));
            }
/*            if (selectPosition == position) {
                tv_item.setBackgroundResource(R.drawable.press_button);
            } else {
                tv_item.setBackgroundResource(R.color.transparent);
            }
            tv_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != onItemClickListener) {
                        onItemClickListener.onItemClick(position);
                    }
                    selectPosition = position;
                    notifyDataSetChanged();

                }
            });*/
        }
    }
}
