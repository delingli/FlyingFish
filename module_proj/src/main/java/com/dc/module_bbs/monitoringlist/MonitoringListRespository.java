package com.dc.module_bbs.monitoringlist;

import android.text.TextUtils;

import com.dc.baselib.BaseApplication;
import com.dc.baselib.http.newhttp.StrAbsHttpSubscriber;
import com.dc.baselib.mvvm.BaseRespository;
import com.dc.baselib.mvvm.EventUtils;
import com.dc.baselib.utils.UserManager;
import com.dc.commonlib.commonentity.video.CameraInfoListBean;
import com.dc.commonlib.commonentity.video.VideoAccountBean;
import com.dc.commonlib.commonentity.video.VideoAccountInfoManager;
import com.dc.commonlib.utils.JsonUtil;
import com.dc.commonlib.utils.LogUtil;
import com.dc.commonlib.utils.video.PlayerManager;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class MonitoringListRespository extends BaseRespository {
    public static String TAG = MonitoringListRespository.class.getSimpleName();
    public String EVENT_NO_DATA;
    public String EVENT_VIDEOLIST;

    public MonitoringListRespository() {
        this.EVENT_NO_DATA = EventUtils.getEventKey();
        this.EVENT_VIDEOLIST = EventUtils.getEventKey();
    }

    int page = 1;

    private void checkPage() {
        if (page < 1) {
            page = 1;
        }
    }

    public void getVideoListInfo(boolean refresh, final int projectId) {
        checkPage();
        if (refresh) {
            page = 1;
        } else {
            page++;
        }

        addDisposable(mRetrofit.create(ImonitorService.class)
                .getVideoListInfo(projectId, page, 10)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new StrAbsHttpSubscriber() {
                    @Override
                    public void onSuccess(String s) {
                        if (!TextUtils.isEmpty(s)) {
                            CameraInfoListBean response = JsonUtil.fromJson(s, CameraInfoListBean.class);
                            LogUtil.e(TAG, "onSuccessResponse...response data .. " + response);
                            if (null != response && response.getList() != null && !response.getList().isEmpty()) {
                                postData(EVENT_VIDEOLIST, response.getList());
                            } else {
                                postData(EVENT_NO_DATA, "finsh");
                            }
                        } else {
                            page--;
                            postData(EVENT_NO_DATA, "finsh");
                        }

                    }

                    @Override
                    public void onFailure(String msg, String code) {
//                        ToastUtils.showToast(msg);
                        postData(EVENT_NO_DATA, "finsh");
                        page--;
                    }
                }));
    }


    public void getHkPlayerAccount(final int projectId) {
        addDisposable(mRetrofit.create(ImonitorService.class)
                .getVideoLoginInfo(projectId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new StrAbsHttpSubscriber() {
                    @Override
                    public void onSuccess(String s) {
                        if (!TextUtils.isEmpty(s)) {
                            List<VideoAccountBean> response = JsonUtil.fromJson(s, new TypeToken<List<VideoAccountBean>>() {
                            }.getType());
                            if (response == null
                                    || response.isEmpty()
                                    || (TextUtils.isEmpty(response.get(0).getLoginAccount()) && TextUtils.isEmpty(response.get(0).getPassword()))) {
                                postData(EVENT_NO_DATA, "nodata");
//                                startActivity(new Intent(getActivity(), VideoIntroduceActivity.class));
                            } else {
                                VideoAccountBean videoAccountBean = response.get(0);
                                VideoAccountInfoManager.getInstance().saveVideoAccountInfo(BaseApplication.getsInstance(), projectId + "", videoAccountBean);
                                VideoAccountBean.setVideoInfo(videoAccountBean);
                                PlayerManager.getPlayerInstance().login(videoAccountBean, new PlayerManager.LoginResultCallBack() {
                                    @Override
                                    public void onSuccess() {
                                        VideoAccountBean videoAccountInfo = VideoAccountInfoManager.getInstance().getVideoAccountInfo(BaseApplication.getsInstance(), projectId + "");
                                        if (null != videoAccountInfo) {
                                            videoAccountInfo.setLogin(true);
                                            VideoAccountInfoManager.getInstance().saveVideoAccountInfo(BaseApplication.getsInstance(), projectId + "", videoAccountInfo);
                                        }

                                        getVideoListInfo(true, projectId);
                                    }

                                    @Override
                                    public void onFailure() {
                                        postData(EVENT_NO_DATA, "finsh");
                                        VideoAccountBean videoAccountInfo = VideoAccountInfoManager.getInstance().getVideoAccountInfo(BaseApplication.getsInstance(), projectId + "");
                                        if (null != videoAccountInfo) {
                                            videoAccountInfo.setLogin(false);
                                            VideoAccountInfoManager.getInstance().saveVideoAccountInfo(BaseApplication.getsInstance(), projectId + "", videoAccountInfo);
                                        }

                                    }
                                });
                            }

                        } else {
                            postData(EVENT_NO_DATA, "finsh");
                        }

                    }

                    @Override
                    public void onFailure(String msg, String code) {
//                        ToastUtils.showToast(msg);
                        postData(EVENT_NO_DATA, "finsh");
                    }
                }));

    }
}
