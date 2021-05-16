package com.dc.commonlib.utils.video;

public class VideoConfig {
    /**
     * 父节点
     */
    public static class Root {

        /**
         * 当前页码
         */
        public static int CTRL_CURR_PAGE = 1;

        /**
         * 每页返回的数据量
         */
        public static final int CTRL_NUM_PER_PAGER = 10;
        public static int PARENT_NODE_TYPE = -1;
        public static  int PARENT_ID = -1;
    }

    /**
     * 子节点
     */
    public static class SubNode {
        /**
         * 当前页码
         */
        public static int CURR_PAGE = 1;
        /**
         * 每页返回的数据量
         */
        public static int NUM_PER_PAGE = 10;
    }
}
