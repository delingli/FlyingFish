package com.dc.module_bbs.labordata;

import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.dc.baselib.mvvm.AbsLifecycleActivity;
import com.dc.module_bbs.R;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.List;

/**
 * "劳务数据"
 * proj_labordata_item
 * proj_labordata_item_progress.xml
 * proj_labordata_item_lab
 */
public class LaborDataActivity extends AbsLifecycleActivity<LaborDataViewModel> {

    private RecyclerView mRecyclerView;
    private SmartRefreshLayout mRefreshLayout;
    private LaborDataAdapter mLaborDataAdapter;
    private int projectId;

    @Override
    protected int getLayout() {
        return R.layout.common_refresh_layout;
    }

    public static String PROJECT_ID = "projectId";

    public static void startActivity(Context context, int prijectId) {
        Intent intent = new Intent(context, LaborDataActivity.class);
        intent.putExtra(PROJECT_ID, prijectId);
        context.startActivity(intent);
    }

    @Override
    protected void dataObserver() {
        super.dataObserver();
        registerSubscriber(mViewModel.EVENT_TEAM, List.class).observe(this, new Observer<List>() {
            @Override
            public void onChanged(@Nullable List sstr) {
                mLaborDataAdapter.setList(sstr);
                mViewModel.toGetWorkerCount(projectId);
            }
        });
        registerSubscriber(mViewModel.EVENT_WORKER, List.class).observe(this, new Observer<List>() {
            @Override
            public void onChanged(@Nullable List sstr) {
                mLaborDataAdapter.addList(sstr);
            }
        });
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        if (getIntent() != null) {
            projectId = getIntent().getIntExtra(PROJECT_ID, 0);
        }
        setTitle(R.string.labor_data);
        mRefreshLayout = findViewById(R.id.refreshLayout);
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setBackgroundColor(getResources().getColor(R.color.bg_color_f7f8f9));
        mRefreshLayout.setEnableLoadMore(false);
        mRefreshLayout.setEnableRefresh(false);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mLaborDataAdapter = new LaborDataAdapter(this, null, -1));
        mViewModel.toGetTeamCount(projectId);
    }

    @Override
    protected void initData() {

    }
}
