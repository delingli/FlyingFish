package com.dc.module_bbs.projshow;

import android.text.TextUtils;

import com.dc.baselib.http.newhttp.StrAbsHttpSubscriber;
import com.dc.baselib.mvvm.BaseRespository;
import com.dc.baselib.mvvm.EventUtils;
import com.dc.baselib.utils.ToastUtils;
import com.dc.commonlib.utils.JsonUtil;
import com.dc.commonlib.utils.LogUtil;
import com.dc.commonlib.utils.MoneyUtils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ProjectShowRespository extends BaseRespository {
    public String EVENT_SUCESS;

    public ProjectShowRespository() {
        EVENT_SUCESS = EventUtils.getEventKey();
    }

    public void getProjectList(int project) {

        addDisposable(mRetrofit.create(IprojectService.class)
                .getItemProject(project)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new StrAbsHttpSubscriber() {
                    @Override
                    public void onSuccess(String s) {
                        if (!TextUtils.isEmpty(s)) {
                            ProjectItemBean projItemsBean = JsonUtil.fromJson(s, ProjectItemBean.class);
                            conversionData(projItemsBean);
                        }
                    }

                    @Override
                    public void onFailure(String msg, String code) {
                        ToastUtils.showToast(msg);
                    }
                }));
    }

    private void conversionData(ProjectItemBean projItemsBean) {
        if (null != projItemsBean) {
            List<AbsProjectInfo> list = new ArrayList<>();
            BannerProjectInfo bannerProjectInfo = new BannerProjectInfo();
            bannerProjectInfo.urls = projItemsBean.getEffect_pics();
            bannerProjectInfo.projectItemBean = projItemsBean;
            list.add(bannerProjectInfo);
            ProjectInfoDetail projectInfoDetail = new ProjectInfoDetail();
            projectInfoDetail.projectAdress = projItemsBean.getAddress();
            projectInfoDetail.projectAdressName = projItemsBean.getName();
            projectInfoDetail.startsTime = projItemsBean.getPlan_start_day();
            projectInfoDetail.endTime = projItemsBean.getPlan_end_day();
            projectInfoDetail.actualConstructionTime = projItemsBean.getReal_start_day();
            projectInfoDetail.project__status=projItemsBean.getProject__status();
            projectInfoDetail.projectItemBean = projItemsBean;
            list.add(projectInfoDetail);

            ProjectInvestmentInfo projectInvestmentInfo = new ProjectInvestmentInfo();
            projectInvestmentInfo.invested = (float) MoneyUtils.yuanToHundredMillion(projItemsBean.getInvested());
            projectInvestmentInfo.investment =(float) MoneyUtils.yuanToHundredMillion(projItemsBean.getInvestment());

            String s = MoneyUtils.subtract(projItemsBean.getInvestment(), projItemsBean.getInvested());
            projectInvestmentInfo.noWorkInvestment =(float) MoneyUtils.yuanToHundredMillion(s);
            LogUtil.d("YUAN:", "已投资：" + projItemsBean.getInvested() + "转换亿后:" + projectInvestmentInfo.invested
                    + "总投资:" + projItemsBean.getInvestment() + "转换亿后:" + projectInvestmentInfo.investment
                    + "未投资:" + s + "转换亿后:"
                    + projectInvestmentInfo.noWorkInvestment);
            list.add(projectInvestmentInfo);



            List<ProjectItemBean.MileStonesBean> mile_stones = projItemsBean.getMile_stones();
            List<ProjectInvestmentItem.ProjectInvestmentItemBean> projectInvestmentItemList = null;
            if (null != mile_stones) {
                projectInvestmentItemList = new ArrayList<>();
                for (ProjectItemBean.MileStonesBean mileStonesBean : mile_stones) {
                    ProjectInvestmentItem.ProjectInvestmentItemBean beanItem = new ProjectInvestmentItem.ProjectInvestmentItemBean();
                    beanItem.finished = mileStonesBean.isFinished();
                    beanItem.plane_date = mileStonesBean.getPlane_date();
                    beanItem.real_date = mileStonesBean.getReal_date();
                    beanItem.name = mileStonesBean.getName();
                    beanItem.phase = true;
                    projectInvestmentItemList.add(beanItem);
                    if (mileStonesBean.getSubs() == null || mileStonesBean.getSubs().isEmpty()) {
                        //todo 添加假数据
                        if (mileStonesBean.equals(mile_stones.get(mile_stones.size() - 1))) {
                            break;
                        }
                        for (int x = 0; x < 3; ++x) {
                            ProjectInvestmentItem.ProjectInvestmentItemBean beanItemz = new ProjectInvestmentItem.ProjectInvestmentItemBean();
                            beanItemz.isFalseData = true;
                            projectInvestmentItemList.add(beanItemz);
                        }


                    }
                    for (ProjectItemBean.MileStonesBean.SubsBean sub : mileStonesBean.getSubs()) {
                        ProjectInvestmentItem.ProjectInvestmentItemBean beanItemz = new ProjectInvestmentItem.ProjectInvestmentItemBean();
                        beanItemz.finished = sub.isFinished();
                        beanItemz.plane_date = sub.getPlane_date();
                        beanItemz.real_date = sub.getReal_date();
                        beanItemz.name = sub.getName();
                        beanItemz.phase = false;
                        projectInvestmentItemList.add(beanItemz);
                    }

                }
            }

            ProjLab projLab=new ProjLab();
            list.add(projLab);
            if (null != projectInvestmentItemList) {
                list.addAll(projectInvestmentItemList);
            }
            postData(EVENT_SUCESS, list);

        }


    }
}
