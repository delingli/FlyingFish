package com.dc.module_bbs.projectoverview;

public class ProjectOverviewItem {
    public String landArea;
    public String constructionArea;
    public ConstructionUnit constructionunit;
    public Contractors contractors;
    public SupervisionUnit supervisionunit;
    public DesignUnit designunit;

    public static class ConstructionUnit {
        public String contact;
        public String contactPhone;
        public String company;
    }

    public static class Contractors {
        public String contact;
        public String contactPhone;
        public String company;
    }

    public static class SupervisionUnit {
        public String contact;
        public String contactPhone;
        public String company;
    }

    public static class DesignUnit {
        public String contact;
        public String contactPhone;
        public String company;
    }

}
