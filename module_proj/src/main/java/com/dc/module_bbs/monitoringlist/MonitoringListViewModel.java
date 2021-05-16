package com.dc.module_bbs.monitoringlist;

import android.app.Application;
import android.support.annotation.NonNull;

import com.dc.baselib.mvvm.AbsViewModel;

public class MonitoringListViewModel extends AbsViewModel<MonitoringListRespository> {
    public MonitoringListViewModel(@NonNull Application application) {
        super(application);
    }

    /**
     * 获取账号密码
     */
    public void getHkPlayerAccount(int projectId) { mRepository.getHkPlayerAccount(projectId);
    }

    public void getVideoListInfo(boolean refresh,final int projectId) {
        mRepository.getVideoListInfo(refresh,projectId);
    }
}
