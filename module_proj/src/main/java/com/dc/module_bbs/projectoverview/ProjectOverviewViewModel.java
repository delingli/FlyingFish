package com.dc.module_bbs.projectoverview;

import android.app.Application;
import android.support.annotation.NonNull;

import com.dc.baselib.mvvm.AbsViewModel;
import com.dc.commonlib.utils.MoneyUtils;
import com.dc.module_bbs.projshow.ProjectItemBean;

public class ProjectOverviewViewModel extends AbsViewModel<ProjectOverviewRespository> {
    public ProjectOverviewViewModel(@NonNull Application application) {
        super(application);
    }

    public ProjectOverviewItem getProjectOverviewItem(ProjectItemBean itemBean) {
        if (null != itemBean) {
            return conversionData(itemBean);
        }
        return null;
    }

    private ProjectOverviewItem conversionData(ProjectItemBean itemBean) {
        ProjectOverviewItem projectOverviewItem = new ProjectOverviewItem();
        projectOverviewItem.landArea = MoneyUtils.percentagefor2(itemBean.getCovered_area())+"";

        projectOverviewItem.constructionArea = MoneyUtils.percentagefor2(itemBean.getConstruction_area())+"";
        ProjectOverviewItem.ConstructionUnit constructionUnit = new ProjectOverviewItem.ConstructionUnit();
        constructionUnit.company = itemBean.getEmployer();
        constructionUnit.contact = itemBean.getEmployer_contact();
        constructionUnit.contactPhone = itemBean.getEmployer_phone();
        projectOverviewItem.constructionunit = constructionUnit;

        ProjectOverviewItem.Contractors contractors = new ProjectOverviewItem.Contractors();
        contractors.company = itemBean.getContractor();
        contractors.contact = itemBean.getContractor_contact();
        contractors.contactPhone = itemBean.getContractor_phone();
        projectOverviewItem.contractors = contractors;


        ProjectOverviewItem.SupervisionUnit supervisionunit = new ProjectOverviewItem.SupervisionUnit();
        supervisionunit.company = itemBean.getSupervisor();


        projectOverviewItem.supervisionunit = supervisionunit;


        ProjectOverviewItem.DesignUnit designunit = new ProjectOverviewItem.DesignUnit();
        designunit.company = itemBean.getDesigner();
        projectOverviewItem.designunit = designunit;
        return projectOverviewItem;
    }

    //测试
    public ProjectOverviewItem getProjectOverviewItem() {
        ProjectOverviewItem projectOverviewItem = new ProjectOverviewItem();
        projectOverviewItem.landArea = "16.66";
        projectOverviewItem.constructionArea = "324545.45";
        ProjectOverviewItem.ConstructionUnit constructionUnit = new ProjectOverviewItem.ConstructionUnit();
        constructionUnit.company = "测试公司";
        constructionUnit.contact = "达令";
        constructionUnit.contactPhone = "18001332871";
        projectOverviewItem.constructionunit = constructionUnit;

        ProjectOverviewItem.Contractors contractors = new ProjectOverviewItem.Contractors();
        contractors.company = "测试公司";
        contractors.contact = "达令";
        contractors.contactPhone = "18001332871";
        projectOverviewItem.contractors = contractors;


        ProjectOverviewItem.SupervisionUnit supervisionunit = new ProjectOverviewItem.SupervisionUnit();
        supervisionunit.company = "测试公司";
        supervisionunit.contact = "达令";
        supervisionunit.contactPhone = "18001332871";
        projectOverviewItem.supervisionunit = supervisionunit;


        ProjectOverviewItem.DesignUnit designunit = new ProjectOverviewItem.DesignUnit();
        designunit.company = "设计单位";
        designunit.contact = "达令";
        designunit.contactPhone = "18001332871";
        projectOverviewItem.designunit = designunit;
        return projectOverviewItem;
    }
}
