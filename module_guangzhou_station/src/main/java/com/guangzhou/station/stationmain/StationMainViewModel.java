package com.guangzhou.station.stationmain;

import android.app.Application;

import androidx.annotation.NonNull;


import com.dc.baselib.mvvm.AbsViewModel;
import com.dc.baselib.mvvm.EventUtils;
import com.dc.commonlib.DeviceIdUtil;
import com.guangzhou.station.playinfo.ProjectListBean;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class StationMainViewModel extends AbsViewModel<StationMainRepository> {
    public static String EVENT_KEY_GETLIST;
    public static String EVENT_ERROR_DATA;

    public StationMainViewModel(@NonNull @NotNull Application application) {
        super(application);
        EVENT_KEY_GETLIST = EventUtils.getEventKey();
        EVENT_ERROR_DATA = EventUtils.getEventKey();

    }

    public void toFetchListSaverData() {
        mRepository.toFetchListSaverData(DeviceIdUtil.getDeviceId(getApplication()), new StationMainRepository.ScreenCallBack<List<ProjectListBean>>() {

            @Override
            public void onSucess(List<ProjectListBean> projectListBeans) {
                postData(EVENT_KEY_GETLIST, projectListBeans);
            }

            @Override
            public void onError(String msg) {
                postData(EVENT_ERROR_DATA, msg);
            }
        });
    }

    @Override
    protected StationMainRepository getRepository() {
        return new StationMainRepository();
    }
}
