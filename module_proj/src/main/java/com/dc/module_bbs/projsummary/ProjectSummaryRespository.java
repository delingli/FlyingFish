package com.dc.module_bbs.projsummary;

import android.text.TextUtils;

import com.dc.baselib.http.newhttp.StrAbsHttpSubscriber;
import com.dc.baselib.mvvm.BaseRespository;
import com.dc.baselib.mvvm.EventUtils;
import com.dc.baselib.utils.ToastUtils;
import com.dc.commonlib.commonentity.HomeBean;
import com.dc.commonlib.utils.JsonUtil;
import com.dc.commonlib.utils.MoneyUtils;
import com.google.gson.annotations.SerializedName;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ProjectSummaryRespository extends BaseRespository {
    public String EVENT_AREA_PROJECT;

    public ProjectSummaryRespository() {
        EVENT_AREA_PROJECT = EventUtils.getEventKey();
    }

    public void toGetownerCompanyBoard(int company_id, final String region) {
        addDisposable(mRetrofit.create(IProjectSummaryService.class)
                .getownerCompanyBoard(company_id, region)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new StrAbsHttpSubscriber() {
                    @Override
                    public void onSuccess(String s) {
                        if (!TextUtils.isEmpty(s)) {
                            HomeBean homebean = JsonUtil.fromJson(s, HomeBean.class);
                            conversionData(homebean, region);
                        }
                    }

                    @Override
                    public void onFailure(String msg, String code) {
                        ToastUtils.showToast(msg);
                    }
                }));
    }

    private void conversionData(HomeBean homebean, String region) {
        if (null != homebean) {
            ProjectAreaItem projectAreaItem = new ProjectAreaItem();
            projectAreaItem.construction_project_cnt = homebean.construction_project_cnt;
            projectAreaItem.invested = (float) MoneyUtils.yuanToHundredMillion(homebean.invested + "");
            projectAreaItem.investment =(float) MoneyUtils.yuanToHundredMillion(homebean.investment + "");


            projectAreaItem.Noinvestment = (float) MoneyUtils.yuanToHundredMillion((homebean.investment - homebean.invested) + "");


            projectAreaItem.  prepare_investment=(float) MoneyUtils.yuanToHundredMillion(homebean.prepare_investment+"");
            projectAreaItem.  construction_investment=(float) MoneyUtils.yuanToHundredMillion(homebean.construction_investment+"");
            if (homebean.region_projects != null) {
                if (TextUtils.equals(region, "东洲")) {
                    projectAreaItem.prepare_project_cnt = homebean.region_projects.dongzhou.get(0);
                    projectAreaItem.construction_project_cnt = homebean.region_projects.dongzhou.get(1);
                    projectAreaItem.project_cnt = homebean.region_projects.dongzhou.get(0) + homebean.region_projects.dongzhou.get(1);
                } else if (TextUtils.equals(region, "银湖")) {
                    projectAreaItem.prepare_project_cnt = homebean.region_projects.yinhu.get(0);
                    projectAreaItem.construction_project_cnt = homebean.region_projects.yinhu.get(1);
                    projectAreaItem.project_cnt = homebean.region_projects.yinhu.get(0) + homebean.region_projects.yinhu.get(1);

                } else if (TextUtils.equals(region, "新登")) {
                    projectAreaItem.prepare_project_cnt = homebean.region_projects.xindeng.get(0);
                    projectAreaItem.construction_project_cnt = homebean.region_projects.xindeng.get(1);
                    projectAreaItem.project_cnt = homebean.region_projects.yinhu.get(0) + homebean.region_projects.xindeng.get(1);

                } else if (TextUtils.equals(region, "场口")) {
                    projectAreaItem.prepare_project_cnt = homebean.region_projects.changkou.get(0);
                    projectAreaItem.construction_project_cnt = homebean.region_projects.changkou.get(1);
                    projectAreaItem.project_cnt = homebean.region_projects.yinhu.get(0) + homebean.region_projects.changkou.get(1);
                } else if (TextUtils.equals(region, "金桥")) {
                    projectAreaItem.prepare_project_cnt = homebean.region_projects.jinqiao.get(0);
                    projectAreaItem.construction_project_cnt = homebean.region_projects.jinqiao.get(1);
                    projectAreaItem.project_cnt = homebean.region_projects.yinhu.get(0) + homebean.region_projects.jinqiao.get(1);
                } else if (TextUtils.equals(region, "鹿山")) {
                    projectAreaItem.prepare_project_cnt = homebean.region_projects.lushan.get(0);
                    projectAreaItem.construction_project_cnt = homebean.region_projects.lushan.get(1);
                    projectAreaItem.project_cnt = homebean.region_projects.yinhu.get(0) + homebean.region_projects.lushan.get(1);
                }

            }
            //最后格式化并输出

            if (homebean.schedule_summary != null && !homebean.schedule_summary.isEmpty()) {
                ProjectAreaItem.ScheduleSummary scheduleSummary;
                List<ProjectAreaItem.ScheduleSummary> ll = new ArrayList<>();
                for (HomeBean.ScheduleSummaryBean item : homebean.schedule_summary) {
                    scheduleSummary = new ProjectAreaItem.ScheduleSummary();
                    scheduleSummary.count = item.count;
                    scheduleSummary.projectextrainfo__schedule = item.projectextrainfo__schedule;
                    ll.add(scheduleSummary);
                }
                projectAreaItem.schedule_summary = ll;

            }
            postData(EVENT_AREA_PROJECT, projectAreaItem);
        }
    }


}

