package com.dc.module_bbs.labordata;

import java.util.List;

public class WokerDataBean {

    public List<TotalBean> total;
    public List<Actual> actual;

    public static class Actual {
        public int worktype_id__count;
        public int worktype_id;
    }

    public static class TotalBean {
        /**
         * worktype_id : 59
         * worktype_id__count : 14
         * worktype__name : 古建筑传统瓦工
         */

        public int worktype_id;
        public int worktype_id__count;
        public String worktype__name;
    }
}
