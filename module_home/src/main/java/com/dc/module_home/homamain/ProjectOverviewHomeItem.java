package com.dc.module_home.homamain;


public class ProjectOverviewHomeItem extends AbsHomeItem {
    public ProjectOverviewHomeItemBean projectAll;
    public ProjectOverviewHomeItemBean projectUnderConstruction;
    public ProjectOverviewHomeItemBean noWorkProject;

    public static class ProjectOverviewHomeItemBean extends AbsHomeItem {
        public int projectCount;
        public String projectTitle;
    }
}
