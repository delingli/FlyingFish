package com.guangzhou.station.stationmain;

import android.util.Log;

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
                .fetchStationList(serial_no) //D0844C0783796FC6E35E7C979BF78DD90541ECB3
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

    public void getKeywordListData(String serial_no, String search, ScreenCallBack callBack) {
        refreshKeywordListData(serial_no, search, callBack);//刷新数据
    }

    private void refreshKeywordListData(String serial_no, String search, ScreenCallBack callBack) {

        addDisposable(mRetrofit.create(IStationListrService.class)
                .fetchKeywordList(serial_no, search)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new AbsHttpSubscriber<KeywordListBean>() {


                    @Override
                    public void onSuccess(KeywordListBean keywordListBean) {
                        if (null != keywordListBean) {

                            if (null != callBack) {

                                callBack.onSucess(keywordListBean);
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

    public void getSearchDetailsData(String serial_no, int id, String search, ScreenCallBack callBack) {
        refreshSearchDetailsData(serial_no, id, search, callBack);//刷新数据
    }

    private void refreshSearchDetailsData(String serial_no, int id, String search, ScreenCallBack callBack) {

        addDisposable(mRetrofit.create(IStationListrService.class)
                .fetchSearchDetailsList("D0844C0783796FC6E35E7C979BF78DD90541ECB3", id, search)
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
