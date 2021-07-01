package com.guangzhou.station.stationmain;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;

import com.blankj.utilcode.util.LogUtils;
import com.dc.baselib.mvvm.AbsLifecycleActivity;
import com.dc.baselib.utils.SPUtils;
import com.guangzhou.station.R;
import com.guangzhou.station.playinfo.AbsPlayInfo;
import com.guangzhou.station.playinfo.PlayInfoActivity;
import com.guangzhou.station.playinfo.ProjectListBean;

import java.util.ArrayList;
import java.util.List;


public class StationMainActivity extends AbsLifecycleActivity<StationMainViewModel> {
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
        SPUtils.initSpUtils(this);
        findViewById(R.id.btn_get).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewModel.toFetchListSaverData();

            }
        });
        findViewById(R.id.btn_jump).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != listBeansz) {
                    ProjectListBean projectListBean = listBeansz.get(0);
                    List<ProjectListBean.DirectoryListBean> directoryListBeans = projectListBean.directoryList;
                    ProjectListBean.DirectoryListBean directoryListBean = directoryListBeans.get(0);
                    List<ProjectListBean.DirectoryListBean.ShowListBean.DescriptionBean> descList = directoryListBean.showList.get(0).description;
                    List<AbsPlayInfo> llist = new ArrayList<>();
                    for (ProjectListBean.DirectoryListBean.ShowListBean.DescriptionBean showListBean : descList) {
                        AbsPlayInfo absPlayInfo = new AbsPlayInfo();
                        absPlayInfo.id = showListBean.id;
                        absPlayInfo.timer = showListBean.timer;
                        absPlayInfo.path = showListBean.path;
                        absPlayInfo.play_type = 1;
                        llist.add(absPlayInfo);

                    }
                    PlayInfoActivity.startActivity(StationMainActivity.this, llist);
                }
            }
        });
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
    }

    private List<ProjectListBean> listBeansz;

    private void fillData(List<ProjectListBean> listBeans) {
        listBeansz = listBeans;
        LogUtils.dTag("StationMainActivity", listBeans.toString());
    }
}
