package com.dc.commonlib.commonentity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class HomeBean {

    /**
     * id : 29
     * project_cnt : 10
     * construction_project_cnt : 7
     * prepare_project_cnt : 3
     * investment : 321221100
     * invested : 22788888
     * covered_area : 3120600
     * construction_area : 2766664
     * pm25_top5 : {"project_pm25_top5":[{"project_name":"杭州光谷国际中心","pm25":"34.0"},{"project_name":"测试圆的工程","pm25":"34.0"}],"pm25":78}
     * schedule_summary : [{"projectextrainfo__schedule":"基础阶段","count":1},{"projectextrainfo__schedule":"主体阶段","count":1},{"projectextrainfo__schedule":"装饰阶段","count":0},{"projectextrainfo__schedule":"竣工验收","count":1}]
     * projects : [{"latitude":30.048692,"status":"003","id":5,"longitude":119.960076,"name":"杭州银湖公园工程"},{"latitude":30.142037,"status":"003","id":18,"longitude":119.979759,"name":"杭州光谷国际中心"},{"latitude":30.142552,"status":"003","id":19,"longitude":119.980747,"name":"浙江建东医药投资管理有限公司新建研发办公大楼"},{"latitude":30.159091,"status":"003","id":20,"longitude":119.975304,"name":"富政储出[2018]23号地块"},{"latitude":30.048869,"status":"003","id":26,"longitude":119.958553,"name":"测试圆的工程"},{"latitude":30.172918,"status":"003","id":28,"longitude":120.008577,"name":"智联达汤她再府工程智联达汤她再府工程智联达汤她再府工程智联达汤她再府工程智联达汤她再府工程"},{"latitude":30.050114,"status":"003","id":29,"longitude":119.975619,"name":"建设年产500万只新型高强度钢制轮毂和200万只车轮生产线项目"},{"latitude":30.052045,"status":"001","id":30,"longitude":119.963817,"name":"智联达汪急块响工程"},{"latitude":30.046232,"status":"001","id":31,"longitude":119.947788,"name":"智联达花班作国工程"},{"latitude":30.05043,"status":"001","id":32,"longitude":119.955856,"name":"智联达郑善收正工程"}]
     * region_projects : {"东洲":[0,1],"银湖":[3,6]}
     * prepare_investment : 110111100
     * prepare_invested : 6600000
     * construction_investment : 211110000
     * construction_invested : 16188888
     * video_urls : [{"url":"https://video2.zldhz.com:6800/mag/hls/1f770353080140ab98c04246a048b108/0/live.m3u8","project":18,"name":"银湖南区"},{"url":"https://video2.zldhz.com:6800/mag/hls/1f770353080140ab98c04246a048b108/0/live.m3u8","project":5,"name":"北区全景"}]
     */

    public int id;
    public int project_cnt;
    public int construction_project_cnt;
    public int prepare_project_cnt;
    public int investment;
    public int invested;
    public int covered_area;
    public int construction_area;
    public Pm25Top5Bean pm25_top5;
    public RegionProjectsBean region_projects;
    public int prepare_investment;
    public int prepare_invested;
    public int construction_investment;
    public int construction_invested;
    public List<ScheduleSummaryBean> schedule_summary;
    public List<ProjectsBean> projects;
    public List<VideoUrlsBean> video_urls;


    public static class Pm25Top5Bean {
        /**
         * project_pm25_top5 : [{"project_name":"杭州光谷国际中心","pm25":"34.0"},{"project_name":"测试圆的工程","pm25":"34.0"}]
         * pm25 : 78
         */

        public int pm25;
        public List<ProjectPm25Top5Bean> project_pm25_top5;


        public static class ProjectPm25Top5Bean {
            /**
             * project_name : 杭州光谷国际中心
             * pm25 : 34.0
             */

            public String project_name;
            public String pm25;


        }
    }

    public static class RegionProjectsBean {
        @SerializedName("东洲")
        public List<Integer> dongzhou;
        @SerializedName("银湖")
        public List<Integer> yinhu;
        @SerializedName("新登")
        public List<Integer> xindeng;
        @SerializedName("场口")
        public List<Integer> changkou;

        @SerializedName("金桥")
        public List<Integer> jinqiao;
        @SerializedName("鹿山")
        public List<Integer> lushan;
    }

    public static class ScheduleSummaryBean {
        /**
         * projectextrainfo__schedule : 基础阶段
         * count : 1
         */

        public String projectextrainfo__schedule;
        public int count;

    }

    public static class ProjectsBean {
        /**
         * latitude : 30.048692
         * status : 003
         * id : 5
         * longitude : 119.960076
         * name : 杭州银湖公园工程
         */

        public double latitude;
        public String status;
        public int id;
        public double longitude;
        public String name;
    }

    public static class VideoUrlsBean {
        /**
         * url : https://video2.zldhz.com:6800/mag/hls/1f770353080140ab98c04246a048b108/0/live.m3u8
         * project : 18
         * name : 银湖南区
         */

        public String url;
        public int project;
        public String name;


    }
}
