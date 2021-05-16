package com.dc.commonlib.commonentity.video;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class CameraInfoListBean implements Parcelable {

    private int total;
    private int pageSize;
    private int pageNo;
    private ArrayList<ListBean> list;


    protected CameraInfoListBean(Parcel in) {
        total = in.readInt();
        pageSize = in.readInt();
        pageNo = in.readInt();
        list = in.createTypedArrayList(ListBean.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(total);
        dest.writeInt(pageSize);
        dest.writeInt(pageNo);
        dest.writeTypedList(list);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CameraInfoListBean> CREATOR = new Creator<CameraInfoListBean>() {
        @Override
        public CameraInfoListBean createFromParcel(Parcel in) {
            return new CameraInfoListBean(in);
        }

        @Override
        public CameraInfoListBean[] newArray(int size) {
            return new CameraInfoListBean[size];
        }
    };

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(ArrayList<ListBean> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "CameraInfoListBean{" +
                "total=" + total +
                ", pageSize=" + pageSize +
                ", pageNo=" + pageNo +
                ", list=" + list +
                '}';
    }




    public static class ListBean implements Parcelable{
        protected ListBean(Parcel in) {
            orderNum = in.readInt();
            cameraChannelNum = in.readInt();
            projectId = in.readInt();
            unitUuid = in.readString();
            cameraType = in.readInt();
            onLineStatus = in.readInt();
            updateTime = in.readLong();
            smartSupport = in.readInt();
            encoderUuid = in.readString();
            cameraUuid = in.readString();
            keyBoardCode = in.readString();
            smartType = in.readString();
            resAuths = in.readString();
            cameraName = in.readString();
            regionUuid = in.readString();
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
        public String toString() {
            return "ListBean{" +
                    "orderNum=" + orderNum +
                    ", projectId=" + projectId +
                    ", cameraChannelNum=" + cameraChannelNum +
                    ", unitUuid='" + unitUuid + '\'' +
                    ", cameraType=" + cameraType +
                    ", onLineStatus=" + onLineStatus +
                    ", updateTime=" + updateTime +
                    ", smartSupport=" + smartSupport +
                    ", encoderUuid='" + encoderUuid + '\'' +
                    ", cameraUuid='" + cameraUuid + '\'' +
                    ", keyBoardCode='" + keyBoardCode + '\'' +
                    ", smartType='" + smartType + '\'' +
                    ", resAuths='" + resAuths + '\'' +
                    ", cameraName='" + cameraName + '\'' +
                    ", regionUuid='" + regionUuid + '\'' +
                    ", isChecked=" + isChecked +
                    '}';
        }

        private int orderNum;
        private int projectId;
        private int cameraChannelNum;
        private String unitUuid;
        private int cameraType;
        private int onLineStatus;
        private long updateTime;
        private int smartSupport;
        private String encoderUuid;
        private String cameraUuid;
        private String keyBoardCode;
        private String smartType;
        private String resAuths;
        private String cameraName;
        private String regionUuid;
        private boolean isChecked;

        public int getOrderNum() {
            return orderNum;
        }

        public void setOrderNum(int orderNum) {
            this.orderNum = orderNum;
        }

        public int getCameraChannelNum() {
            return cameraChannelNum;
        }

        public void setCameraChannelNum(int cameraChannelNum) {
            this.cameraChannelNum = cameraChannelNum;
        }

        public int getProjectId() {
            return projectId;
        }

        public void setProjectId(int projectId) {
            this.projectId = projectId;
        }

        public boolean isChecked() {
            return isChecked;
        }

        public void setChecked(boolean checked) {
            isChecked = checked;
        }

        public String getUnitUuid() {
            return unitUuid;
        }

        public void setUnitUuid(String unitUuid) {
            this.unitUuid = unitUuid;
        }

        public int getCameraType() {
            return cameraType;
        }

        public void setCameraType(int cameraType) {
            this.cameraType = cameraType;
        }

        public int getOnLineStatus() {
            return onLineStatus;
        }

        public void setOnLineStatus(int onLineStatus) {
            this.onLineStatus = onLineStatus;
        }

        public long getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(long updateTime) {
            this.updateTime = updateTime;
        }

        public int getSmartSupport() {
            return smartSupport;
        }

        public void setSmartSupport(int smartSupport) {
            this.smartSupport = smartSupport;
        }

        public String getEncoderUuid() {
            return encoderUuid;
        }

        public void setEncoderUuid(String encoderUuid) {
            this.encoderUuid = encoderUuid;
        }

        public String getCameraUuid() {
            return cameraUuid;
        }

        public void setCameraUuid(String cameraUuid) {
            this.cameraUuid = cameraUuid;
        }

        public String getKeyBoardCode() {
            return keyBoardCode;
        }

        public void setKeyBoardCode(String keyBoardCode) {
            this.keyBoardCode = keyBoardCode;
        }

        public String getSmartType() {
            return smartType;
        }

        public void setSmartType(String smartType) {
            this.smartType = smartType;
        }

        public String getResAuths() {
            return resAuths;
        }

        public void setResAuths(String resAuths) {
            this.resAuths = resAuths;
        }

        public String getCameraName() {
            return cameraName;
        }

        public void setCameraName(String cameraName) {
            this.cameraName = cameraName;
        }

        public String getRegionUuid() {
            return regionUuid;
        }

        public void setRegionUuid(String regionUuid) {
            this.regionUuid = regionUuid;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(orderNum);
            dest.writeInt(projectId);
            dest.writeInt(cameraChannelNum);
            dest.writeString(unitUuid);
            dest.writeInt(cameraType);
            dest.writeInt(onLineStatus);
            dest.writeLong(updateTime);
            dest.writeInt(smartSupport);
            dest.writeString(encoderUuid);
            dest.writeString(cameraUuid);
            dest.writeString(keyBoardCode);
            dest.writeString(smartType);
            dest.writeString(resAuths);
            dest.writeString(cameraName);
            dest.writeString(regionUuid);
        }
    }
}
