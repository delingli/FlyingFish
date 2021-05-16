package com.dc.commonlib.weiget.collapslist;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import com.dc.commonlib.R;
import com.dc.commonlib.weiget.horizontalrecycle.DLHorizontalItem;
import com.dc.commonlib.weiget.horizontalrecycle.DLHorizontalRecycleView;

import java.util.List;

public class CollapsListView extends FrameLayout {

    private DLHorizontalRecycleView mDLHorizontalRecycleView;

    public CollapsListView(@NonNull Context context) {
        this(context, null);
    }

    public CollapsListView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public CollapsListView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public CollapsListView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.common_collapslist, this, true);
    }
    public void fillData(List<DLHorizontalItem> dataList, int pos) {
        mDLHorizontalRecycleView.fillData(dataList, pos);
    }
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mDLHorizontalRecycleView = findViewById(R.id.dl_HorizontalRecycleView);
        mDLHorizontalRecycleView.addOnDLHorizontalRecycleViewListener(new DLHorizontalRecycleView.OnDLHorizontalRecycleViewListener() {
            @Override
            public void onItemClick(int pos, String id) {
                if (null != onCollapsListViewListener) {
                    onCollapsListViewListener.onItemClick(pos);
                }
            }
        });
    }

    private OnCollapsListViewListener onCollapsListViewListener;

    public void addOnCollapsListViewListener(OnCollapsListViewListener onCollapsListViewListener) {
        this.onCollapsListViewListener = onCollapsListViewListener;
    }

    public interface OnCollapsListViewListener {
        void onItemClick(int pos);
    }
}
