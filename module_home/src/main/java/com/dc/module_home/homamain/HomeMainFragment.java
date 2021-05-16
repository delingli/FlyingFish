package com.dc.module_home.homamain;

import android.arch.lifecycle.Observer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.dc.baselib.mvvm.AbsLifecycleFragment;
import com.dc.baselib.statusBar.StarusBarUtils;
import com.dc.commonlib.common.CommonConstant;
import com.dc.commonlib.commonentity.video.CameraInfoListBean;
import com.dc.commonlib.commonentity.video.VideoAccountInfoManager;
import com.dc.commonlib.utils.ArounterManager;
import com.dc.commonlib.utils.LogUtil;
import com.dc.module_home.R;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Route(path = ArounterManager.HOME_HOMEMAINFRAGMENT_URL)
public class HomeMainFragment extends AbsLifecycleFragment<HomeMainViewModel> implements OnRefreshListener {

    private SmartRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private HomeMainAdapter mHomeMainAdapter;

    @Override
    public void dataObserver() {

        registerSubscriber(mViewModel.mRepository.EVENT_LOGIN_SUCESS, String.class).observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String sucess) {
                // todo 去重新设置数据
                if (mHomeMainAdapter != null) {
                    mHomeMainAdapter.notifyDataSetChanged();
                }
            }
        });
        registerSubscriber(mViewModel.mRepository.EVENT_VIDEOLIST, CameraInfoListBean.ListBean.class).observe(this, new Observer<CameraInfoListBean.ListBean>() {
            @Override
            public void onChanged(@Nullable CameraInfoListBean.ListBean listBean) {
                // todo 去重新设置数据
            }
        });
        registerSubscriber(mViewModel.mRepository.EVENT_KEY_RELEASE_FAILURE, List.class).observe(this, new Observer<List>() {
            @Override
            public void onChanged(@Nullable List sstr) {
                mHomeMainAdapter.setList(sstr);
                refreshLayout.finishRefresh();//todo 获取到列表，去登陆去获取视频列表更新原来数据
                conversationData();
//                mViewModel.getHkPlayerAccount();
            }
        });
        registerSubscriber(mViewModel.mRepository.EVENT_FINISHREFRESH, String.class).observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String sstr) {
                refreshLayout.finishRefresh();
            }
        });
    }

    private void toRefreshData(CameraInfoListBean.ListBean listBean) {
        Log.d("listBean", listBean.toString());
        if (mHomeMainAdapter != null && mHomeMainAdapter.getList() != null) {
            Iterator<IAbsHomeItem> iterator = mHomeMainAdapter.getList().iterator();
            while (iterator.hasNext()) {
                IAbsHomeItem next1 = iterator.next();
                if (next1 instanceof VideoMonitoringHomeItem) {
                    VideoMonitoringHomeItem next = (VideoMonitoringHomeItem) next1;
                    if (next.projectId == listBean.getProjectId()) {
                        next.listBean = listBean;
                        next.name = listBean.getCameraName();
                        LogUtil.d("LDL", "数据改变了几次 ？");
                    }
                }

            }
            mHomeMainAdapter.notifyDataSetChanged();
        }
    }

    private void conversationData() {
        if (null != mHomeMainAdapter && mHomeMainAdapter.getList() != null) {
            List<IAbsHomeItem> list = mHomeMainAdapter.getList();
            for (IAbsHomeItem lb : list) {
                if (lb instanceof VideoMonitoringHomeItem) {
                    VideoMonitoringHomeItem videoMonitoringHomeItem = (VideoMonitoringHomeItem) lb;
                    mViewModel.getHkPlayerAccount(videoMonitoringHomeItem.projectId);
                    mViewModel.getVideoListInfo(videoMonitoringHomeItem.projectId, new HomeMainRespositorys.OnVideoInfoCallBackListener() {
                        @Override
                        public void onVideoList(CameraInfoListBean.ListBean listBean) {
                            toRefreshData(listBean);
                            LogUtil.d("LDL", "数据刷新调用了几次 ？");

                        }
                    });

              /*      //请求登陆了
                    if (VideoAccountInfoManager.getInstance().getVideoAccountInfo(getContext(), String.valueOf(videoMonitoringHomeItem.projectId)) != null &&
                            VideoAccountInfoManager.getInstance().getVideoAccountInfo(getContext(), String.valueOf(videoMonitoringHomeItem.projectId)).isLogin()) {
                        //已经登陆，直接请求
                        mViewModel.getVideoListInfo(videoMonitoringHomeItem.projectId);
                    } else {
                        //去登陆
                        mViewModel.getHkPlayerAccount(videoMonitoringHomeItem.projectId);
                    }*/
                }
            }
        }

    }

    @Override
    public void initView(View view) {
        super.initView(view);
        recyclerView = view.findViewById(R.id.recyclerView);
        refreshLayout = view.findViewById(R.id.refreshLayout);
        refreshLayout.setBackgroundColor(getResources().getColor(R.color.white));
        recyclerView.setBackgroundColor(getResources().getColor(R.color.white));
        refreshLayout.setEnableRefresh(true);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setEnableLoadMore(false);
        refreshLayout.autoRefresh();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        recyclerView.setAdapter(mHomeMainAdapter = new HomeMainAdapter(getContext(), null, -1));
        mHomeMainAdapter.addOnAreaItemClickListener(new HomeMainAdapter.OnAreaItemClickListener() {
            @Override
            public void onAreaItemClick(String areaAdress) {
                if (!TextUtils.isEmpty(areaAdress)) {
                    ARouter.getInstance().build(ArounterManager.PROJECTSUMMARYACTIVITY_URL).withString(CommonConstant.PRPJECT_ARTEA, areaAdress).navigation();
                }

            }
        });

//        mViewModel.getHkPlayerAccount();

    }


    @Override
    protected void initData() {

    }

    @Override
    protected int getLayout() {
        return R.layout.home_activity;
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        mViewModel.toGetownerCompanyBoard();
    }
}
