package com.dc.commonlib.weiget.horizontalrecycle;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.dc.commonlib.weiget.courselist.DLCourseListView;

import java.util.List;

public class DLHorizontalRecycleView extends RecyclerView {
    public DLHorizontalRecycleView(@NonNull Context context) {
        super(context);
        init();
    }

    private OnDLHorizontalRecycleViewListener ondlhorizontalrecycleviewlistener;

    public void addOnDLHorizontalRecycleViewListener(OnDLHorizontalRecycleViewListener ondlhorizontalrecycleviewlistener) {
        this.ondlhorizontalrecycleviewlistener = ondlhorizontalrecycleviewlistener;
    }

    public interface OnDLHorizontalRecycleViewListener {
        void onItemClick(int pos, String cid);
    }

    private void init() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        this.setLayoutManager(linearLayoutManager);
        this.setAdapter(mDLHorizontalRecycleAdapter = new DLHorizontalRecycleAdapter(getContext(), null, 0));

        mDLHorizontalRecycleAdapter.addOnItemClickListener(new DLHorizontalRecycleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int pos) {
                if (ondlhorizontalrecycleviewlistener != null) {
                    if (mDLHorizontalRecycleAdapter.getList() != null) {
                        DLHorizontalItem horizontalItem = mDLHorizontalRecycleAdapter.getList().get(pos);
                        ondlhorizontalrecycleviewlistener.onItemClick(pos, horizontalItem.id);
                    }

                }
            }
        });
    }

    public DLHorizontalRecycleView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();

    }

    public DLHorizontalRecycleView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public int getSelectPosition() {
        return mDLHorizontalRecycleAdapter.getSelectPosition();
    }

    private DLHorizontalRecycleAdapter mDLHorizontalRecycleAdapter;

    public void fillData(List<DLHorizontalItem> dlHorizontalItems, int position) {
        if (dlHorizontalItems == null || dlHorizontalItems.isEmpty()) {
            return;
        }
        if (null != mDLHorizontalRecycleAdapter) {
            mDLHorizontalRecycleAdapter.addList(dlHorizontalItems);
            if (position != -1 && position < dlHorizontalItems.size()) {
                mDLHorizontalRecycleAdapter.setSelectPosition(position);
            } else {
                mDLHorizontalRecycleAdapter.setSelectPosition(0);

            }
        }

    }

    public List<DLHorizontalItem> getList() {
        return mDLHorizontalRecycleAdapter.getList();
    }

    public void setSelection(int pos) {
        if (null != mDLHorizontalRecycleAdapter) {
            mDLHorizontalRecycleAdapter.setSelectPosition(pos);
        }
    }
}
