package com.dc.commonlib.common;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * 公用列表适配器
 *
 * @param <T>
 */
public abstract class BaseRecyclerAdapter<T> extends RecyclerView.Adapter<BaseViewHolder> {

    private OnItemClickListener onItemClickListener;

    private OnItemLongClickListener onItemLongClickListener;
    protected List<T> dataList;
    protected MultiTypeSupport<T> multiTypeSupport;
    private LayoutInflater layoutInflater;
    protected int mItemLayoutId;

    private Context context;
    private ImageView mIvTippic;
    private TextView mTvEmptyText;

    protected Context getContext() {
        return context;
    }

    public List<T> getList() {
        return dataList;
    }

    @Override
    public int getItemViewType(int position) {
        if (multiTypeSupport != null) {
            return multiTypeSupport.getLayoutId(dataList.get(position), position);
        }
        return super.getItemViewType(position);
    }

    public void setList(List<T> dataList) {
        this.dataList = dataList;
        notifyDataSetChanged();
    }

    public void addList(List<T> itemlist) {
        if (dataList == null) {
            dataList = new ArrayList<>();
        }
        if (null != itemlist) {
            dataList.addAll(itemlist);
            notifyDataSetChanged();
        }

    }

    public void addList(T item) {
        if (dataList == null) {
            dataList = new ArrayList<>();
        }
        if (null != item) {
            dataList.add(item);
            notifyDataSetChanged();
        }

    }

    /**
     * @param context
     * @param list
     * @param itemLayoutId
     */
    public BaseRecyclerAdapter(Context context, @Nullable List<T> list, int itemLayoutId) {
        this.context = context;
        this.dataList = list;
        this.mItemLayoutId = itemLayoutId;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (multiTypeSupport != null) {
            mItemLayoutId = viewType;
        }
        Log.d("LDL", mItemLayoutId + "'...");
//        View view=  layoutInflater.inflate(mItemLayoutId,null,false);
        View view = layoutInflater.inflate(mItemLayoutId, parent, false);
        return new BaseViewHolder(view);

    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, final int position, List<Object> payloads) {
        int itemViewType = getItemViewType(position);
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position);
        } else {
            if (dataList != null && dataList.size() > 0) {
                convert(holder, dataList.get(position), position, payloads);
            }
        }


    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, final int position) {
//        int itemViewType = getItemViewType(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClickListener(v, position);
                }
            }

        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (onItemLongClickListener != null) {
                    onItemLongClickListener.onItemLongClickListener(v, position);
                }

                return true;
            }
        });
        List list = new ArrayList();
        if (dataList != null && dataList.size() > 0) {
            convert(holder, dataList.get(position), position, list);

        }


    }

    protected abstract void convert(BaseViewHolder holder, T t, int position, List<Object> payloads);

    private boolean showEmpty = true;

    @Override
    public int getItemCount() {
        return dataList == null ? 0 : dataList.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    public interface OnItemClickListener {
        void onItemClickListener(View v, int position);
    }

    public interface OnItemLongClickListener {
        void onItemLongClickListener(View v, int position);
    }
}
