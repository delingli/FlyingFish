package com.guangzhou.station.playinfo;

import android.os.Parcel;
import android.os.Parcelable;

public class AbsPlayInfo implements Parcelable {
    public int id;
    public int timer;
    public int play_type;////播放方式：1手动播放、2自动播放
    public int type;      //素材类型：1图片、2视频
    public String path;


    public AbsPlayInfo() {
    }

    protected AbsPlayInfo(Parcel in) {
        id = in.readInt();
        timer = in.readInt();
        play_type = in.readInt();
        type = in.readInt();
        path = in.readString();
    }

    public static final Creator<AbsPlayInfo> CREATOR = new Creator<AbsPlayInfo>() {
        @Override
        public AbsPlayInfo createFromParcel(Parcel in) {
            return new AbsPlayInfo(in);
        }

        @Override
        public AbsPlayInfo[] newArray(int size) {
            return new AbsPlayInfo[size];
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
        dest.writeInt(play_type);
        dest.writeInt(type);
        dest.writeString(path);
    }
}
