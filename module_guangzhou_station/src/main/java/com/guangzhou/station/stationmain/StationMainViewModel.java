package com.guangzhou.station.stationmain;

import android.app.Application;

import androidx.annotation.NonNull;


import com.dc.baselib.mvvm.AbsViewModel;
import com.dc.baselib.mvvm.EventUtils;
import com.dc.commonlib.util.DeviceIdUtil;
import com.guangzhou.station.playinfo.AbsPlayInfo;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class StationMainViewModel extends AbsViewModel<StationMainRepository> {
    public static String EVENT_KEY_GETLIST;
    public static String EVENT_ERROR_DATA;
    public static String EVENT_CONVERSATION;

    public StationMainViewModel(@NonNull @NotNull Application application) {
        super(application);
        EVENT_KEY_GETLIST = EventUtils.getEventKey();
        EVENT_ERROR_DATA = EventUtils.getEventKey();
        EVENT_CONVERSATION = EventUtils.getEventKey();

    }

    public void toFetchListSaverData() {
        mRepository.toFetchListSaverData(DeviceIdUtil.getDeviceId(getApplication()), new StationMainRepository.ScreenCallBack<List<ProjectListBean.DirectoryListBean>>() {

            @Override
            public void onSucess(List<ProjectListBean.DirectoryListBean> projectListBeans) {
                postData(EVENT_KEY_GETLIST, projectListBeans);
            }

            @Override
            public void onError(String msg) {
                postData(EVENT_ERROR_DATA, msg);
            }
        });
    }

    public void conversionData(ThreeListAdapter mthreelistadapter, int position) {
        if (null != mthreelistadapter && mthreelistadapter.getList() != null && !mthreelistadapter.getList().isEmpty()) {
            AbsStationData absStationData = mthreelistadapter.getList().get(position);
            List<AbsPlayInfo> list = null;
            if (absStationData instanceof ProjectListBean.DirectoryListBean.ShowListBean) {
                ProjectListBean.DirectoryListBean.ShowListBean showListBean = (ProjectListBean.DirectoryListBean.ShowListBean) absStationData;
                list = new ArrayList<>();
                AbsPlayInfo absPlayInfo;
                for (ProjectListBean.DirectoryListBean.ShowListBean.DescriptionBean descriptionBean : showListBean.description) {
                    absPlayInfo = new AbsPlayInfo();
                    absPlayInfo.play_type = showListBean.play_type;
                    absPlayInfo.timer = descriptionBean.timer;
                    absPlayInfo.path = descriptionBean.path;
                    absPlayInfo.id = descriptionBean.id;
                    absPlayInfo.type = descriptionBean.type;
                    list.add(absPlayInfo);
                }

            }
            if (null != list) {
                postData(EVENT_CONVERSATION, list);

            }
        }


    }

    @Override
    protected StationMainRepository getRepository() {
        return new StationMainRepository();
    }
}
