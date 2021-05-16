package com.dc.module_bbs.labordata;

import java.util.List;

public class TeamDataBean {

    public List<TotalBean> total;
    public List<Actual> actual;


    public static class Actual {
        /*        "team_id": 1,
                        "team_id__count": 4,
                        "team__name": "12492394395班组"*/
        public int team_id;
        public int team_id__count;
        public String team__name;
    }

    public static class TotalBean {
        /**
         * team_id : 129
         * team_id__count : 122
         * team__name : 杂工
         */

        public int team_id;
        public int team_id__count;
        public String team__name;
    }
}
