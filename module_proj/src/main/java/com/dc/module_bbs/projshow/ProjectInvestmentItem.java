package com.dc.module_bbs.projshow;

import java.util.List;

public class ProjectInvestmentItem extends AbsProjectInfo {
    public List<ProjectInvestmentItemBean> projectInvestmentItemList;

    public static class ProjectInvestmentItemBean extends AbsProjectInfo {
        public boolean phase;//是一个阶段性主题，否则就是阶段二级

        public boolean finished;//是否结束
        public String plane_date;
        public String real_date;
        public String name;
        public boolean isFalseData;
    }


}
