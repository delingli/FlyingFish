package com.dc.module_home.homamain;

import java.util.List;

public class ProjectAreaHomeItem extends AbsHomeItem {
    public List<ProjectAreaItems> projectAreas;

    public static class ProjectAreaItems extends AbsHomeItem {
        public String projectAdress;
        public int totalProjects;
        public int completedProjects;
        public boolean click;
    }

}
