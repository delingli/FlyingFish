package com.dc.module_bbs.monitoringlist;

import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.dc.baselib.BaseApplication;
import com.dc.baselib.mvvm.AbsLifecycleActivity;
import com.dc.commonlib.commonentity.video.CameraInfoListBean;
import com.dc.commonlib.commonentity.video.DisplayVideoPlayerManager;
import com.dc.commonlib.commonentity.video.VideoAccountInfoManager;
import com.dc.module_bbs.R;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.List;

public class MonitoringListActivity extends AbsLifecycleActivity<MonitoringListViewModel> implements OnLoadMoreListener, OnRefreshListener {

    private RecyclerView recyclerView;
    private int projectId;
    private SmartRefreshLayout mRefreshLayout;

    @Override
    protected int getLayout() {
        return R.layout.common_refresh_layout;
    }

    MonitoringAdapter monitoringAdapter;
    public static String MONITOR_LIST = "Monitor_list";

    public static void startActivity(Context context, int projectId) {
        Intent intent = new Intent(context, MonitoringListActivity.class);
        intent.putExtra(MONITOR_LIST, projectId);
        context.startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if (DisplayVideoPlayerManager.instance().onBackPressed()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        setTitle(R.string.monitoring);
        if (getIntent() != null) {
            projectId = getIntent().getIntExtra(MONITOR_LIST, -1);
        }
        mRefreshLayout = findViewById(R.id.refreshLayout);
        mRefreshLayout.setEnableLoadMore(true);
        mRefreshLayout.setEnableRefresh(true);
        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.setOnLoadMoreListener(this);
        recyclerView = findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(monitoringAdapter = new MonitoringAdapter(this, null, -1));
        monitoringAdapter.setEmptyImg(R.drawable.no_data);
        if (projectId != -1) {
            mRefreshLayout.autoRefresh();
        }
    }

    @Override
    protected void dataObserver() {
        super.dataObserver();
        registerSubscriber(mViewModel.mRepository.EVENT_VIDEOLIST, List.class).observe(this, new Observer<List>() {
            @Override
            public void onChanged(@Nullable List sstr) {
                monitoringAdapter.setList(sstr);
                mRefreshLayout.finishLoadMore();
                mRefreshLayout.finishRefresh();
            }
        });
        registerSubscriber(mViewModel.mRepository.EVENT_NO_DATA, String.class).observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String sstr) {
                mRefreshLayout.finishLoadMore();
                mRefreshLayout.finishRefresh();
            }
        });
//
    }

    @Override
    protected void initData() {

    }


    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        if (VideoAccountInfoManager.getInstance().getVideoAccountInfo(BaseApplication.getsInstance(), projectId + "") != null) {
            if (VideoAccountInfoManager.getInstance().getVideoAccountInfo(BaseApplication.getsInstance(), projectId + "").isLogin()) {
                mViewModel.getVideoListInfo(false, projectId);
            } else {
                mViewModel.getHkPlayerAccount(projectId);
            }
        }
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        if (VideoAccountInfoManager.getInstance().getVideoAccountInfo(BaseApplication.getsInstance(), projectId + "") != null) {
            if (VideoAccountInfoManager.getInstance().getVideoAccountInfo(BaseApplication.getsInstance(), projectId + "").isLogin()) {
                mViewModel.getVideoListInfo(true, projectId);
            } else {
                mViewModel.getHkPlayerAccount(projectId);
            }
        } else {
            mViewModel.getHkPlayerAccount(projectId);
        }
    }
}
