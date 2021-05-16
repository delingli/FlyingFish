package com.dc.module_bbs.projectlist;

import android.content.Context;
import android.text.TextUtils;

import com.dc.baselib.http.newhttp.StrAbsHttpSubscriber;
import com.dc.baselib.mvvm.BaseRespository;
import com.dc.baselib.mvvm.EventUtils;
import com.dc.baselib.utils.ToastUtils;
import com.dc.commonlib.utils.JsonUtil;
import com.dc.commonlib.weiget.horizontalrecycle.DLHorizontalItem;
import com.dc.module_bbs.R;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ProjectListRespository extends BaseRespository {
    public String EVENT_PROJECTLIST_SUCESS;
    public String EVENT_FINISH_REFRESH_LOAD;

    public ProjectListRespository() {
        EVENT_PROJECTLIST_SUCESS = EventUtils.getEventKey();
        EVENT_FINISH_REFRESH_LOAD = EventUtils.getEventKey();
    }

    private int page = 1;

    private void checkPage() {
        if (page < 1) {
            page = 1;
        }
    }

    public void getSearchProjectList(boolean refresh, final String project__status, String region,String search) {
        if (refresh) {
            page = 1;
        } else {
            ++page;
        }
        addDisposable(mRetrofit.create(IProjectListService.class)
                .getSearchProjectList(page, project__status, region,search)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new StrAbsHttpSubscriber() {
                    @Override
                    public void onSuccess(String s) {
                        if (!TextUtils.isEmpty(s)) {
                            ProjItemsBean projItemsBean = JsonUtil.fromJson(s, ProjItemsBean.class);
                            conversionData(projItemsBean);
                        } else {
                            --page;
                            checkPage();
                            postData(EVENT_FINISH_REFRESH_LOAD, "finsh");
                        }

                    }

                    @Override
                    public void onFailure(String msg, String code) {
//                        ToastUtils.showToast(msg);
                        postData(EVENT_FINISH_REFRESH_LOAD, "finsh");
                        --page;
                        checkPage();
                    }
                }));
    }

    public void getProjectList(boolean refresh, final String project__status, String region) {
        if (refresh) {
            page = 1;
        } else {
            ++page;
        }
        addDisposable(mRetrofit.create(IProjectListService.class)
                .getProjectList(page, project__status, region)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new StrAbsHttpSubscriber() {
                    @Override
                    public void onSuccess(String s) {
                        if (!TextUtils.isEmpty(s)) {
                            ProjItemsBean projItemsBean = JsonUtil.fromJson(s, ProjItemsBean.class);
                            conversionData(projItemsBean);
                        } else {
                            --page;
                            checkPage();
                            postData(EVENT_FINISH_REFRESH_LOAD, "finsh");
                        }

                    }

                    @Override
                    public void onFailure(String msg, String code) {
//                        ToastUtils.showToast(msg);
                        postData(EVENT_FINISH_REFRESH_LOAD, "finsh");
                        --page;
                        checkPage();
                    }
                }));
    }


    private void conversionData(ProjItemsBean projItemsBean) {
        if (projItemsBean != null && projItemsBean.getResults() != null) {
            List<ProjItems> projItemsList = new ArrayList<>();
            ProjItems projItems;
            for (ProjItemsBean.ResultsBean resultsBean : projItemsBean.getResults()) {
                projItems = new ProjItems();
                projItems.project__name = resultsBean.getProject__name();
                projItems.project__status = resultsBean.getProject__status();
                projItems.project_id = resultsBean.getProject_id();
                if (resultsBean.getEffect_pics() != null && !resultsBean.getEffect_pics().isEmpty()) {
                    projItems.pic_url = resultsBean.getEffect_pics().get(0);
                }

                projItemsList.add(projItems);
            }
            postData(EVENT_PROJECTLIST_SUCESS, projItemsList);
        }
    }
}
