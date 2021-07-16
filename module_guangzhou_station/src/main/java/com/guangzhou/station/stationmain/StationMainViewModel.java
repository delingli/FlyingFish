package com.guangzhou.station.stationmain;

import android.app.Application;
import android.util.Log;

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
    public static String EVEVT_KEYWORD_GETLIST;
    public static String EVENT_SEARCH_DETAILS;


    public StationMainViewModel(@NonNull @NotNull Application application) {
        super(application);
        EVENT_KEY_GETLIST = EventUtils.getEventKey();
        EVENT_ERROR_DATA = EventUtils.getEventKey();
        EVENT_CONVERSATION = EventUtils.getEventKey();
        EVEVT_KEYWORD_GETLIST = EventUtils.getEventKey();
        EVENT_SEARCH_DETAILS = EventUtils.getEventKey();
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

    public void getKeywordListData(String search){
        mRepository.getKeywordListData(DeviceIdUtil.getDeviceId(getApplication()), search, new StationMainRepository.ScreenCallBack<KeywordListBean>() {

            @Override
            public void onSucess(KeywordListBean listBeans) {

                postData(EVEVT_KEYWORD_GETLIST, listBeans.list);
            }

            @Override
            public void onError(String msg) {
                postData(EVENT_ERROR_DATA, msg);
            }
        });
    }

    public void getSearchDetailsData(int id, String search) {
        mRepository.getSearchDetailsData(DeviceIdUtil.getDeviceId(getApplication()), id, search, new StationMainRepository.ScreenCallBack<List<ProjectListBean.DirectoryListBean>>() {

            @Override
            public void onSucess(List<ProjectListBean.DirectoryListBean> projectListBeans) {

                postData(EVENT_SEARCH_DETAILS, projectListBeans);
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
