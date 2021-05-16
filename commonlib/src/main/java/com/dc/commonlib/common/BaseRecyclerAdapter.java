package com.dc.commonlib.common;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dc.commonlib.R;

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
    public final int VIEW_TYPE_EMPTY = R.layout.common_empty_view;
    protected List<T> dataList;
    protected MultiTypeSupport<T> multiTypeSupport;
    private LayoutInflater layoutInflater;
    int emptyImg = -1;
    int emptyTxt = -1;
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
        if (null == dataList || dataList.size() == 0) {
            return VIEW_TYPE_EMPTY;
        } else if (multiTypeSupport != null) {
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
        if (null == list) {
            this.dataList = new ArrayList<>();

        } else {
            this.dataList = list;

        }
        this.mItemLayoutId = itemLayoutId;
        layoutInflater = LayoutInflater.from(context);
    }

    public OnEmptyClickListener onEmptyClickListener;

    public void addOnEmptyViewClickListener(OnEmptyClickListener onEmptyClickListener) {
        this.onEmptyClickListener = onEmptyClickListener;
    }

    public interface OnEmptyClickListener {
        /**
         * 空视图的点击
         */
        void onEmptyClick();
    }

    public class EmptyViewHolder extends BaseViewHolder {
        private TextView mEmptyTextView;

        public EmptyViewHolder(View itemView) {
            super(itemView);
            mEmptyTextView = (TextView) itemView.findViewById(R.id.tv_empty_text);
        }
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_EMPTY) {
            View emptyView = LayoutInflater.from(parent.getContext()).inflate(VIEW_TYPE_EMPTY, parent, false);
            return new EmptyViewHolder(emptyView);
        }
        if (multiTypeSupport != null) {
            mItemLayoutId = viewType;
        }
        Log.d("LDL", mItemLayoutId + "'...");
        View view = layoutInflater.inflate(mItemLayoutId, parent, false);
        return new BaseViewHolder(view);

    }


    public void setEmptyImg(int resId) {
        this.emptyImg = resId;
        notifyDataSetChanged();
    }


    public void setEmptyTxt(int resId) {
        emptyTxt = resId;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, final int position, List<Object> payloads) {
        int itemViewType = getItemViewType(position);
        if (itemViewType == VIEW_TYPE_EMPTY) {
            mTvEmptyText = holder.getView(R.id.tv_empty_text);
            mIvTippic = holder.getView(R.id.iv_tippic);
            if (showEmpty) {
                mTvEmptyText.setVisibility(View.VISIBLE);
                mIvTippic.setVisibility(View.VISIBLE);
            } else {
                mTvEmptyText.setVisibility(View.GONE);
                mIvTippic.setVisibility(View.GONE);
            }
            if (emptyTxt != -1) {
                mTvEmptyText.setText(emptyTxt);
            }
            if (emptyImg != -1) {
                mIvTippic.setImageResource(emptyImg);
            }

            mIvTippic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onEmptyClickListener != null) {
                        onEmptyClickListener.onEmptyClick();
                    }
                }
            });
            mTvEmptyText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onEmptyClickListener != null) {
                        onEmptyClickListener.onEmptyClick();
                    }
                }
            });
        }
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
        int itemViewType = getItemViewType(position);
        if (itemViewType != VIEW_TYPE_EMPTY) {
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

    }

    protected abstract void convert(BaseViewHolder holder, T t, int position, List<Object> payloads);

    private boolean showEmpty = true;

    public void setEmptyShow(boolean showEmpty) {
        this.showEmpty = showEmpty;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (dataList == null || dataList.size() == 0) {
            return 1;
        } else {
            return dataList.size();
        }
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
