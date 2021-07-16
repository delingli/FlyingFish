package com.guangzhou.station.stationmain;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class KeywordListBean implements Parcelable, AbsStationData {


    public List<ListBean> list;

    public KeywordListBean(){

    }

    protected KeywordListBean(Parcel in) {
        list = in.createTypedArrayList(ListBean.CREATOR);
    }

    public static final Creator<KeywordListBean> CREATOR = new Creator<KeywordListBean>() {
        @Override
        public KeywordListBean createFromParcel(Parcel in) {
            return new KeywordListBean(in);
        }

        @Override
        public KeywordListBean[] newArray(int size) {
            return new KeywordListBean[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeTypedList(list);
    }

    public static class ListBean implements Parcelable, AbsStationData{
        /**
         * id : 165
         * name : 8号测试
         * type : 3
         */

        public int id;
        public String name;
        public int type;

        protected ListBean(Parcel in) {
            id = in.readInt();
            name = in.readString();
            type = in.readInt();
        }

        public static final Creator<ListBean> CREATOR = new Creator<ListBean>() {
            @Override
            public ListBean createFromParcel(Parcel in) {
                return new ListBean(in);
            }

            @Override
            public ListBean[] newArray(int size) {
                return new ListBean[size];
            }
        };



        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeInt(id);
            parcel.writeString(name);
            parcel.writeInt(type);
        }
    }
}
