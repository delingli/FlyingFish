package com.guangzhou.station.stationmain;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.LogUtils;
import com.dc.baselib.mvvm.AbsLifecycleActivity;
import com.dc.commonlib.common.BaseRecyclerAdapter;
import com.dc.commonlib.common.RefreshMessage;
import com.guangzhou.station.R;
import com.guangzhou.station.playinfo.AbsPlayInfo;
import com.guangzhou.station.playinfo.PlayInfoActivity;
import com.guangzhou.station.playinfo.PlayInfosActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;


public class StationMainActivity extends AbsLifecycleActivity<StationMainViewModel> {

    private RecyclerView mRecycleView, mRecycleContext;
    private MainListAdapter mMainListAdapter;
    private ThreeListAdapter mThreeListAdapter;

    @Override
    protected Class<StationMainViewModel> getViewModel() {
        return StationMainViewModel.class;
    }

    @Override
    protected int getLayout() {
        return R.layout.station_activity_stationmain;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        setToolBarlheadHide(true);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        EventBus.getDefault().register(this);
        //去掉最上面时间、电量等
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mRecycleView = findViewById(R.id.recycleView);
        mRecycleContext = findViewById(R.id.recycle_context);
        mRecycleView.setLayoutManager(new LinearLayoutManager(this));
        mRecycleView.setAdapter(mMainListAdapter = new MainListAdapter(this, null, -1));


        mRecycleContext.setLayoutManager(new LinearLayoutManager(this));
        mRecycleContext.setAdapter(mThreeListAdapter = new ThreeListAdapter(this, null, -1));
        mMainListAdapter.addOnItemClickListener(new MainListAdapter.OnItemClickListener() {
            @Override
            public void onItemsClick(ProjectListBean.DirectoryListBean absStationData) {
                if (null != absStationData) {
                    List<AbsStationData> threeDescList = (List<AbsStationData>) ((Object) absStationData.showList);
                    if (null != mThreeListAdapter && null != threeDescList) {
                        mThreeListAdapter.addListBeanList(threeDescList);
                    }
                }

            }
        });

        mViewModel.toFetchListSaverData();
        mThreeListAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(View v, int position) {
                mViewModel.conversionData(mThreeListAdapter, position);
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessage(RefreshMessage refreshMessage) {
        if (refreshMessage != null && refreshMessage.refresh) {
            mViewModel.toFetchListSaverData();
        }
    }

    @Override
    protected void initData() {
        registerSubscriber(StationMainViewModel.EVENT_KEY_GETLIST, List.class).observe(this, new Observer<List>() {

            @Override
            public void onChanged(List list) {
                if (list != null) {
                    fillData(list);
                }


            }
        });

        registerSubscriber(StationMainViewModel.EVENT_CONVERSATION, List.class).observe(this, new Observer<List>() {

            @Override
            public void onChanged(List list) {
                if (list != null && !list.isEmpty()) {
                    Object o = list.get(0);
                    if (o instanceof AbsPlayInfo) {
                        AbsPlayInfo absPlayInfo = (AbsPlayInfo) o;
                        boolean auto = absPlayInfo.play_type == 1;
//                        PlayInfoActivity.startActivity(StationMainActivity.this, list, auto);
                        PlayInfosActivity.startActivity(StationMainActivity.this, list, auto);
                    }
                }
            }
        });

    }


    private void fillData(List<ProjectListBean.DirectoryListBean> listBeans) {
        LogUtils.dTag("StationMainActivity", listBeans.toString());
        if (null != mMainListAdapter && null != listBeans && !listBeans.isEmpty()) {
            List<AbsStationData> ll = (List<AbsStationData>) (Object) listBeans;
            mMainListAdapter.setList(ll);
            ProjectListBean.DirectoryListBean directoryListBean = mMainListAdapter.notifySelect(0);
            if (null != directoryListBean) {
                List<AbsStationData> threeDescList = (List<AbsStationData>) ((Object) directoryListBean.showList);
                if (null != mThreeListAdapter && null != threeDescList) {
                    mThreeListAdapter.addListBeanList(threeDescList);

                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    EventBus.getDefault().unregister(this);
    }
}
