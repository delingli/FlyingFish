package com.dc.commonlib.weiget.horizontalrecycle;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.dc.commonlib.R;
import com.dc.commonlib.common.BaseRecyclerAdapter;
import com.dc.commonlib.common.BaseViewHolder;

import java.util.List;

public class DLHorizontalRecycleAdapter extends BaseRecyclerAdapter<DLHorizontalItem> {
    private int selectPosition;

    public void setSelectPosition(int selectPosition) {
        if(selectPosition<getItemCount()&&selectPosition>-1){
            this.selectPosition = selectPosition;
        }

        notifyDataSetChanged();
    }

    public int getSelectPosition() {
        return selectPosition;
    }

    /**
     * @param context
     * @param list
     * @param itemLayoutId
     */
    public DLHorizontalRecycleAdapter(Context context, @Nullable List<DLHorizontalItem> list, int itemLayoutId) {
        super(context, list, R.layout.common_horizontal_recycleview_item);
    }

    private OnItemClickListener onItemClickListener;

    public void addOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(int pos);

    }

    @Override
    protected void convert(BaseViewHolder holder, DLHorizontalItem dlHorizontalItem, final int position, List<Object> payloads) {
        if (null != dlHorizontalItem) {
            TextView tv_item = holder.getView(R.id.tv_item);
            tv_item.setText(dlHorizontalItem.name);
            if (selectPosition == position) {
                tv_item.setBackgroundResource(R.drawable.press_button);
                tv_item.setTextColor(Color.parseColor("#FFFF0225"));
            } else {
                tv_item.setBackgroundResource(R.color.transparent);
                tv_item.setTextColor(Color.parseColor("#FF1D2023"));

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
            });
        }
    }
}
