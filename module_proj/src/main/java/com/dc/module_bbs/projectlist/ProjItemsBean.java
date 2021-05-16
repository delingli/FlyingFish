package com.dc.module_bbs.projectlist;

import java.util.List;

public class ProjItemsBean {
    /**
     * sys_time : 2020-05-25 11:22:35
     * count : 3
     * next : null
     * previous : null
     * results : [{"project_id":32,"project__name":"智联达郑善收正工程","project__status":"001","effect_pics":["https://zldtestfile.zlddata.cn/31/f0/09/moveTowercrane.png"]},{"project_id":31,"project__name":"智联达花班作国工程","project__status":"001","effect_pics":[]},{"project_id":30,"project__name":"智联达汪急块响工程","project__status":"001","effect_pics":[]}]
     */

    private String sys_time;
    private int count;
    private Object next;
    private Object previous;
    private List<ResultsBean> results;

    public String getSys_time() {
        return sys_time;
    }

    public void setSys_time(String sys_time) {
        this.sys_time = sys_time;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Object getNext() {
        return next;
    }

    public void setNext(Object next) {
        this.next = next;
    }

    public Object getPrevious() {
        return previous;
    }

    public void setPrevious(Object previous) {
        this.previous = previous;
    }

    public List<ResultsBean> getResults() {
        return results;
    }

    public void setResults(List<ResultsBean> results) {
        this.results = results;
    }

    public static class ResultsBean {
        /**
         * project_id : 32
         * project__name : 智联达郑善收正工程
         * project__status : 001
         * effect_pics : ["https://zldtestfile.zlddata.cn/31/f0/09/moveTowercrane.png"]
         */

        private int project_id;
        private String project__name;
        private String project__status;
        private List<String> effect_pics;

        public int getProject_id() {
            return project_id;
        }

        public void setProject_id(int project_id) {
            this.project_id = project_id;
        }

        public String getProject__name() {
            return project__name;
        }

        public void setProject__name(String project__name) {
            this.project__name = project__name;
        }

        public String getProject__status() {
            return project__status;
        }

        public void setProject__status(String project__status) {
            this.project__status = project__status;
        }

        public List<String> getEffect_pics() {
            return effect_pics;
        }

        public void setEffect_pics(List<String> effect_pics) {
            this.effect_pics = effect_pics;
        }
    }
}
