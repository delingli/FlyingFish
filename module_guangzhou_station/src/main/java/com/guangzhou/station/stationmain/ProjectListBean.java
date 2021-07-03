package com.guangzhou.station.stationmain;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.List;

public class ProjectListBean implements Parcelable, AbsStationData {


    /**
     * id : 104
     * name : 反攻倒算
     * type : 1
     * directoryList : [{"id":108,"name":"sdf","type":2,"showList":[{"id":109,"name":"文艺晚会","description":[{"name":"水果派对","id":55,"timer":5,"path":"uploads/20210702/b981ee637eb165d084b31ae78c15f0e0.jpg","type":1},{"name":"这是柠檬还是橙子","id":4,"timer":5,"path":"uploads/20210625/07c63a52f8c700ac2d61241ea59d8d4f.jpg","type":1},{"name":"草莓","id":1,"timer":5,"path":"uploads/20210625/2e65b5f28db8260005103d3e270a075b.jpg","type":1}],"play_type":2,"type":3}]}]
     */

    public int id;
    public String name;
    public int type;
    public List<DirectoryListBean> directoryList;

    public ProjectListBean() {
    }

    protected ProjectListBean(Parcel in) {
        id = in.readInt();
        name = in.readString();
        type = in.readInt();
        directoryList = in.createTypedArrayList(DirectoryListBean.CREATOR);
    }

    public static final Creator<ProjectListBean> CREATOR = new Creator<ProjectListBean>() {
        @Override
        public ProjectListBean createFromParcel(Parcel in) {
            return new ProjectListBean(in);
        }

        @Override
        public ProjectListBean[] newArray(int size) {
            return new ProjectListBean[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeInt(type);
        dest.writeTypedList(directoryList);
    }

    public static class DirectoryListBean implements Parcelable, AbsStationData {
        /**
         * id : 108
         * name : sdf
         * type : 2
         * showList : [{"id":109,"name":"文艺晚会","description":[{"name":"水果派对","id":55,"timer":5,"path":"uploads/20210702/b981ee637eb165d084b31ae78c15f0e0.jpg","type":1},{"name":"这是柠檬还是橙子","id":4,"timer":5,"path":"uploads/20210625/07c63a52f8c700ac2d61241ea59d8d4f.jpg","type":1},{"name":"草莓","id":1,"timer":5,"path":"uploads/20210625/2e65b5f28db8260005103d3e270a075b.jpg","type":1}],"play_type":2,"type":3}]
         */
        public boolean selected;
        public int id;
        public String name;
        public int type;
        public List<ShowListBean> showList;

        public static class ShowListBean implements Parcelable, AbsStationData {
            /**
             * id : 109
             * name : 文艺晚会
             * description : [{"name":"水果派对","id":55,"timer":5,"path":"uploads/20210702/b981ee637eb165d084b31ae78c15f0e0.jpg","type":1},{"name":"这是柠檬还是橙子","id":4,"timer":5,"path":"uploads/20210625/07c63a52f8c700ac2d61241ea59d8d4f.jpg","type":1},{"name":"草莓","id":1,"timer":5,"path":"uploads/20210625/2e65b5f28db8260005103d3e270a075b.jpg","type":1}]
             * play_type : 2
             * type : 3
             */

            public int id;
            public String name;
            public int play_type;
            public int type;
            public List<DescriptionBean> description;

            protected ShowListBean(Parcel in) {
                id = in.readInt();
                name = in.readString();
                play_type = in.readInt();
                type = in.readInt();
                description = in.createTypedArrayList(DescriptionBean.CREATOR);
            }

            public static final Creator<ShowListBean> CREATOR = new Creator<ShowListBean>() {
                @Override
                public ShowListBean createFromParcel(Parcel in) {
                    return new ShowListBean(in);
                }

                @Override
                public ShowListBean[] newArray(int size) {
                    return new ShowListBean[size];
                }
            };

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeInt(id);
                dest.writeString(name);
                dest.writeInt(play_type);
                dest.writeInt(type);
                dest.writeTypedList(description);
            }

            public static class DescriptionBean implements Parcelable, AbsStationData {
                /**
                 * name : 水果派对
                 * id : 55
                 * timer : 5
                 * path : uploads/20210702/b981ee637eb165d084b31ae78c15f0e0.jpg
                 * type : 1
                 */

                public String name;
                public int id;
                public int timer;
                public String path;
                public int type;

                protected DescriptionBean(Parcel in) {
                    name = in.readString();
                    id = in.readInt();
                    timer = in.readInt();
                    path = in.readString();
                    type = in.readInt();
                }

                public static final Creator<DescriptionBean> CREATOR = new Creator<DescriptionBean>() {
                    @Override
                    public DescriptionBean createFromParcel(Parcel in) {
                        return new DescriptionBean(in);
                    }

                    @Override
                    public DescriptionBean[] newArray(int size) {
                        return new DescriptionBean[size];
                    }
                };

                @Override
                public int describeContents() {
                    return 0;
                }

                @Override
                public void writeToParcel(Parcel dest, int flags) {
                    dest.writeString(name);
                    dest.writeInt(id);
                    dest.writeInt(timer);
                    dest.writeString(path);
                    dest.writeInt(type);
                }
            }
        }


        protected DirectoryListBean(Parcel in) {
            selected = in.readByte() != 0;
            id = in.readInt();
            name = in.readString();
            type = in.readInt();
            showList = in.createTypedArrayList(ShowListBean.CREATOR);
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeByte((byte) (selected ? 1 : 0));
            dest.writeInt(id);
            dest.writeString(name);
            dest.writeInt(type);
            dest.writeTypedList(showList);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<DirectoryListBean> CREATOR = new Creator<DirectoryListBean>() {
            @Override
            public DirectoryListBean createFromParcel(Parcel in) {
                return new DirectoryListBean(in);
            }

            @Override
            public DirectoryListBean[] newArray(int size) {
                return new DirectoryListBean[size];
            }
        };
    }
}
