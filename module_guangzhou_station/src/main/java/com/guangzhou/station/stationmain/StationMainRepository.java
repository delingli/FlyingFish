package com.guangzhou.station.stationmain;

import com.blankj.utilcode.util.LogUtils;
import com.dc.baselib.http.newhttp.AbsHttpSubscriber;
import com.dc.baselib.mvvm.BaseRespository;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class StationMainRepository extends BaseRespository {
    public static String TAG = "StationMainRepository";

    public void toFetchListSaverData(String serial_no, ScreenCallBack callBack) {
        refreshListData(serial_no, callBack);//刷新数据
    }

    private void refreshListData(String serial_no, ScreenCallBack callBack) {
        addDisposable(mRetrofit.create(IStationListrService.class)
                .fetchStationList(serial_no)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new AbsHttpSubscriber<ProjectListBean>() {
                    @Override
                    public void onSuccess(ProjectListBean projectListBeans) {
                        if (null != projectListBeans) {

                            if (null != callBack) {
                                callBack.onSucess(projectListBeans.directoryList);
                            }
                        }
                    }

                    @Override
                    public void onFailure(String msg, int code) {
                        LogUtils.e(TAG, msg + code);
                        if (null != callBack) {
                            callBack.onError(msg);
                        }
                    }
                }));
    }

    interface ScreenCallBack<T> {
        void onSucess(T t);

        void onError(String msg);
    }

}
