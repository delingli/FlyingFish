package module_guangzhou_station.playinfo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class ProjectListBean implements Parcelable {

    /**
     * id : 1
     * name : 静夜思
     * type : 1
     * directoryList : [{"id":2,"type":2,"name":"床前明月光","showList":[{"id":3,"name":"测试节目1","description":[{"id":1,"timer":5,"path":"uploads/20210625/2e65b5f28db8260005103d3e270a075b.jpg","type":1},{"id":2,"timer":23,"path":"uploads/video/1.mp4","type":2}],"type":3,"play_type":1}]}]
     */

    public int id;
    public String name;
    public int type;
    public List<DirectoryListBean> directoryList;

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

    public static class DirectoryListBean implements Parcelable {
        /**
         * id : 2
         * type : 2
         * name : 床前明月光
         * showList : [{"id":3,"name":"测试节目1","description":[{"id":1,"timer":5,"path":"uploads/20210625/2e65b5f28db8260005103d3e270a075b.jpg","type":1},{"id":2,"timer":23,"path":"uploads/video/1.mp4","type":2}],"type":3,"play_type":1}]
         */

        public int id;
        public int type;
        public String name;
        public List<ShowListBean> showList;

        protected DirectoryListBean(Parcel in) {
            id = in.readInt();
            type = in.readInt();
            name = in.readString();
            showList = in.createTypedArrayList(ShowListBean.CREATOR);
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

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(id);
            dest.writeInt(type);
            dest.writeString(name);
            dest.writeTypedList(showList);
        }

        public static class ShowListBean implements Parcelable {
            /**
             * id : 3
             * name : 测试节目1
             * description : [{"id":1,"timer":5,"path":"uploads/20210625/2e65b5f28db8260005103d3e270a075b.jpg","type":1},{"id":2,"timer":23,"path":"uploads/video/1.mp4","type":2}]
             * type : 3
             * play_type : 1
             */

            public int id;
            public String name;
            public int type;
            public int play_type;
            public List<DescriptionBean> description;

            protected ShowListBean(Parcel in) {
                id = in.readInt();
                name = in.readString();
                type = in.readInt();
                play_type = in.readInt();
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
                dest.writeInt(type);
                dest.writeInt(play_type);
                dest.writeTypedList(description);
            }

            public static class DescriptionBean implements Parcelable {
                /**
                 * id : 1
                 * timer : 5
                 * path : uploads/20210625/2e65b5f28db8260005103d3e270a075b.jpg
                 * type : 1
                 */

                public int id;
                public int timer;
                public String path;
                public int type;

                protected DescriptionBean(Parcel in) {
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
                    dest.writeInt(id);
                    dest.writeInt(timer);
                    dest.writeString(path);
                    dest.writeInt(type);
                }
            }
        }
    }
}
