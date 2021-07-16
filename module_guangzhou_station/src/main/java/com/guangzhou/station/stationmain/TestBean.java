package com.guangzhou.station.stationmain;

import java.util.List;

public class TestBean implements AbsStationData {


    /**
     * id : 140
     * name : 旅游局
     * type : 1
     * directoryList : [{"id":145,"name":"政数局","type":2,"showList":[{"id":173,"name":"002","description":[{"name":"这是柠檬还是橙子","id":4,"timer":5,"path":"uploads/20210625/07c63a52f8c700ac2d61241ea59d8d4f.jpg","type":1}],"play_type":1,"type":3},{"id":147,"name":"大厅图片","description":[{"name":"图片14.南站商务区智慧政务大厅_20210706160045","id":66,"timer":5,"path":"uploads/20210706/baa733a8d53a7d949d868e5c1c82a185.jpg","type":1},{"name":"图片13.番禺智慧政务大厅_20210706160045","id":65,"timer":5,"path":"uploads/20210706/ea706fe5f9b46d8969943c5554650cb7.jpg","type":1}],"play_type":1,"type":3},{"id":146,"name":"政数局风采","description":[{"name":"图片15.南站商务区智慧政务大厅_20210706160050","id":74,"timer":5,"path":"uploads/20210706/652ad346ad81f5646316ad4809f7c04d.jpg","type":1},{"name":"图片7.区政务服务中心大党委_20210706160050","id":73,"timer":5,"path":"uploads/20210706/adce1a6c0de17b9b92842dcc8a76fc4b.jpg","type":1},{"name":"图片20.容缺容错受理_20210706160047","id":71,"timer":5,"path":"uploads/20210706/500b774f65b6b1ed6120600fce05731c.png","type":1},{"name":"图片18.建筑许可_20210706160047","id":70,"timer":5,"path":"uploads/20210706/909d1c04aeba15b350b3783dfcf4460d.png","type":1},{"name":"图片12.政务服务事项标准化应用系统_20210706160045","id":67,"timer":5,"path":"uploads/20210706/13703146222f17450a936ad1a6747f0f.png","type":1},{"name":"图片17.5G基站建设_20210706160045","id":68,"timer":5,"path":"uploads/20210706/fda340333b934653937ef748c719e21c.jpg","type":1},{"name":"图片19.亲商服务平台_20210706160046","id":69,"timer":5,"path":"uploads/20210706/bbfc9eceee52fbcd6e8e161bc9cd79ef.jpg","type":1}],"play_type":1,"type":3}]},{"id":141,"name":"番禺旅游","type":2,"showList":[{"id":144,"name":"番禺地图","description":[{"name":"番禺地图","id":61,"timer":5,"path":"uploads/20210706/ab4113f385cc1d52f4c86dbc83899139.jpg","type":1}],"play_type":1,"type":3},{"id":143,"name":"宣传视频","description":[{"name":"宣传片1","id":59,"timer":78,"path":"uploads/video/7.mp4","type":2},{"name":"旅游宣传片2","id":60,"timer":187,"path":"uploads/video/8.mp4","type":2}],"play_type":1,"type":3}]}]
     */

    private int id;
    private String name;
    private int type;
    private List<DirectoryListBean> directoryList;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<DirectoryListBean> getDirectoryList() {
        return directoryList;
    }

    public void setDirectoryList(List<DirectoryListBean> directoryList) {
        this.directoryList = directoryList;
    }

    public static class DirectoryListBean {
        /**
         * id : 145
         * name : 政数局
         * type : 2
         * showList : [{"id":173,"name":"002","description":[{"name":"这是柠檬还是橙子","id":4,"timer":5,"path":"uploads/20210625/07c63a52f8c700ac2d61241ea59d8d4f.jpg","type":1}],"play_type":1,"type":3},{"id":147,"name":"大厅图片","description":[{"name":"图片14.南站商务区智慧政务大厅_20210706160045","id":66,"timer":5,"path":"uploads/20210706/baa733a8d53a7d949d868e5c1c82a185.jpg","type":1},{"name":"图片13.番禺智慧政务大厅_20210706160045","id":65,"timer":5,"path":"uploads/20210706/ea706fe5f9b46d8969943c5554650cb7.jpg","type":1}],"play_type":1,"type":3},{"id":146,"name":"政数局风采","description":[{"name":"图片15.南站商务区智慧政务大厅_20210706160050","id":74,"timer":5,"path":"uploads/20210706/652ad346ad81f5646316ad4809f7c04d.jpg","type":1},{"name":"图片7.区政务服务中心大党委_20210706160050","id":73,"timer":5,"path":"uploads/20210706/adce1a6c0de17b9b92842dcc8a76fc4b.jpg","type":1},{"name":"图片20.容缺容错受理_20210706160047","id":71,"timer":5,"path":"uploads/20210706/500b774f65b6b1ed6120600fce05731c.png","type":1},{"name":"图片18.建筑许可_20210706160047","id":70,"timer":5,"path":"uploads/20210706/909d1c04aeba15b350b3783dfcf4460d.png","type":1},{"name":"图片12.政务服务事项标准化应用系统_20210706160045","id":67,"timer":5,"path":"uploads/20210706/13703146222f17450a936ad1a6747f0f.png","type":1},{"name":"图片17.5G基站建设_20210706160045","id":68,"timer":5,"path":"uploads/20210706/fda340333b934653937ef748c719e21c.jpg","type":1},{"name":"图片19.亲商服务平台_20210706160046","id":69,"timer":5,"path":"uploads/20210706/bbfc9eceee52fbcd6e8e161bc9cd79ef.jpg","type":1}],"play_type":1,"type":3}]
         */

        private int id;
        private String name;
        private int type;
        private List<ShowListBean> showList;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public List<ShowListBean> getShowList() {
            return showList;
        }

        public void setShowList(List<ShowListBean> showList) {
            this.showList = showList;
        }

        public static class ShowListBean {
            /**
             * id : 173
             * name : 002
             * description : [{"name":"这是柠檬还是橙子","id":4,"timer":5,"path":"uploads/20210625/07c63a52f8c700ac2d61241ea59d8d4f.jpg","type":1}]
             * play_type : 1
             * type : 3
             */

            private int id;
            private String name;
            private int play_type;
            private int type;
            private List<DescriptionBean> description;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getPlay_type() {
                return play_type;
            }

            public void setPlay_type(int play_type) {
                this.play_type = play_type;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public List<DescriptionBean> getDescription() {
                return description;
            }

            public void setDescription(List<DescriptionBean> description) {
                this.description = description;
            }

            public static class DescriptionBean {
                /**
                 * name : 这是柠檬还是橙子
                 * id : 4
                 * timer : 5
                 * path : uploads/20210625/07c63a52f8c700ac2d61241ea59d8d4f.jpg
                 * type : 1
                 */

                private String name;
                private int id;
                private int timer;
                private String path;
                private int type;

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public int getId() {
                    return id;
                }

                public void setId(int id) {
                    this.id = id;
                }

                public int getTimer() {
                    return timer;
                }

                public void setTimer(int timer) {
                    this.timer = timer;
                }

                public String getPath() {
                    return path;
                }

                public void setPath(String path) {
                    this.path = path;
                }

                public int getType() {
                    return type;
                }

                public void setType(int type) {
                    this.type = type;
                }
            }
        }
    }
}
