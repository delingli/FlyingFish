package com.dc.commonlib.common;

import android.support.annotation.IdRes;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;

//基类ViewHolder用于RecycleView
public class BaseViewHolder extends RecyclerView.ViewHolder {

    private final SparseArray<View> views;
    private View Tag;

    public View getTag() {
        return Tag;
    }

    public void setTag(View tag) {
        Tag = tag;
    }

    public View convertView;

    public BaseViewHolder(final View view) {
        super(view);
        this.views = new SparseArray<>();
        this.convertView = view;
    }

    @SuppressWarnings("unchecked")
    public <T extends View> T getView(@IdRes int viewId) {
        View view = views.get(viewId);
        if (view == null) {
            view = itemView.findViewById(viewId);
            views.put(viewId, view);
        }
        return (T) view;
    }
}
