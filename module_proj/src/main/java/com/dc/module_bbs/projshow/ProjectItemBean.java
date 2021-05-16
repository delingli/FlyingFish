package com.dc.module_bbs.projshow;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class ProjectItemBean implements Parcelable {

    /**
     * id : 1
     * mile_stones : [{"subs":[{"finished":true,"plane_date":null,"name":"土地证","real_date":null},{"finished":true,"plane_date":null,"name":"用地规划许可证","real_date":null},{"finished":true,"plane_date":null,"name":"建设工程规划许可证","real_date":null},{"finished":true,"plane_date":null,"name":"消防审批，人防审批，绿化，环保审批，档案馆备案，质监部门签订质监合同","real_date":null},{"finished":true,"plane_date":null,"name":"三通一平","real_date":null}],"finished":true,"name":"前期审批","plane_date":"2020-01-12","real_date":"2020-01-12"},{"subs":[],"finished":true,"name":"施工许可","plane_date":"2020-01-23","real_date":"2020-01-30"},{"subs":[{"finished":true,"plane_date":null,"name":"桩基础","real_date":null},{"finished":true,"plane_date":null,"name":"土方开挖、钎探验槽","real_date":null},{"finished":true,"plane_date":null,"name":"垫层","real_date":null},{"finished":true,"plane_date":null,"name":"地下卷材防水施工","real_date":null},{"finished":true,"plane_date":null,"name":"地下室施工","real_date":null},{"finished":true,"plane_date":"2019-05-08","name":"回填土","real_date":"2019-05-10"},{"finished":false,"plane_date":"2019-05-20","name":"验收","real_date":null}],"finished":false,"name":"基础验收","plane_date":"2020-05-20","real_date":null},{"subs":[],"finished":false,"name":"主体验收","plane_date":"2020-09-30","real_date":null},{"subs":[],"finished":false,"name":"竣工验收","plane_date":"2020-12-10","real_date":null},{"subs":[],"finished":false,"name":"监督报告","plane_date":"2020-12-30","real_date":null}]
     * delay_day : 6
     * project__status : 003
     * effect_pics : ["https://zldtestfile.zlddata.cn/67/15/a6/20180802LED%E6%80%BB%E9%83%A8%E5%8A%9E%E5%85%AC%E6%A5%BC%E6%96%B9%E6%A1%88%E8%AE%BE%E8%AE%A1%E6%96%87%E6%9C%AC%5F%E9%A1%B5%E9%9D%A2%5F23.png","https://zldtestfile.zlddata.cn/a9/7b/ae/A190128mjc-ts%E5%A5%97%5FPro-zch%E5%89%AF%E6%9C%AC.jpg","https://zldtestfile.zlddata.cn/e9/3e/ca/%E9%A6%96%E9%A1%B5.png"]
     * address : 浙江省杭州市富阳区浙江省杭州市富阳区银湖街道环形路
     * name : 杭州光谷国际中心
     * investment : 100000000
     * invested : 8888888
     * employer : 杭州光谷置业有限公司
     * designer : 杭州富阳设计公司
     * contractor : 杭州北洲建设有限公司
     * supervisor : 浙江中新建筑工程监理有限公司
     * employer_contact : 孙杰伟
     * employer_phone : 15058199315
     * contractor_contact : 凌善富
     * contractor_phone : 13968163521
     * plan_start_day : 2019-05-01
     * plan_end_day : 2021-05-10
     * real_start_day : 2020-05-13
     * covered_area : 1000000
     * construction_area : 888888
     * region : 银湖
     * schedule : 主体阶段
     * project : 18
     */

    private int id;
    private int delay_day;
    private String project__status;
    private String address;
    private String name;
    private String investment;
    private String invested;
    private String employer;
    private String designer;
    private String contractor;
    private String supervisor;
    private String employer_contact;
    private String employer_phone;
    private String contractor_contact;
    private String contractor_phone;
    private String plan_start_day;
    private String plan_end_day;
    private String real_start_day;
    private int covered_area;
    private int construction_area;
    private String region;
    private String schedule;
    private int project;
    private List<MileStonesBean> mile_stones;
    private List<String> effect_pics;

    protected ProjectItemBean(Parcel in) {
        id = in.readInt();
        delay_day = in.readInt();
        project__status = in.readString();
        address = in.readString();
        name = in.readString();
        investment = in.readString();
        invested = in.readString();
        employer = in.readString();
        designer = in.readString();
        contractor = in.readString();
        supervisor = in.readString();
        employer_contact = in.readString();
        employer_phone = in.readString();
        contractor_contact = in.readString();
        contractor_phone = in.readString();
        plan_start_day = in.readString();
        plan_end_day = in.readString();
        real_start_day = in.readString();
        covered_area = in.readInt();
        construction_area = in.readInt();
        region = in.readString();
        schedule = in.readString();
        project = in.readInt();
        effect_pics = in.createStringArrayList();
    }

    public static final Creator<ProjectItemBean> CREATOR = new Creator<ProjectItemBean>() {
        @Override
        public ProjectItemBean createFromParcel(Parcel in) {
            return new ProjectItemBean(in);
        }

        @Override
        public ProjectItemBean[] newArray(int size) {
            return new ProjectItemBean[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDelay_day() {
        return delay_day;
    }

    public void setDelay_day(int delay_day) {
        this.delay_day = delay_day;
    }

    public String getProject__status() {
        return project__status;
    }

    public void setProject__status(String project__status) {
        this.project__status = project__status;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInvestment() {
        return investment;
    }

    public void setInvestment(String investment) {
        this.investment = investment;
    }

    public String getInvested() {
        return invested;
    }

    public void setInvested(String invested) {
        this.invested = invested;
    }

    public String getEmployer() {
        return employer;
    }

    public void setEmployer(String employer) {
        this.employer = employer;
    }

    public String getDesigner() {
        return designer;
    }

    public void setDesigner(String designer) {
        this.designer = designer;
    }

    public String getContractor() {
        return contractor;
    }

    public void setContractor(String contractor) {
        this.contractor = contractor;
    }

    public String getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(String supervisor) {
        this.supervisor = supervisor;
    }

    public String getEmployer_contact() {
        return employer_contact;
    }

    public void setEmployer_contact(String employer_contact) {
        this.employer_contact = employer_contact;
    }

    public String getEmployer_phone() {
        return employer_phone;
    }

    public void setEmployer_phone(String employer_phone) {
        this.employer_phone = employer_phone;
    }

    public String getContractor_contact() {
        return contractor_contact;
    }

    public void setContractor_contact(String contractor_contact) {
        this.contractor_contact = contractor_contact;
    }

    public String getContractor_phone() {
        return contractor_phone;
    }

    public void setContractor_phone(String contractor_phone) {
        this.contractor_phone = contractor_phone;
    }

    public String getPlan_start_day() {
        return plan_start_day;
    }

    public void setPlan_start_day(String plan_start_day) {
        this.plan_start_day = plan_start_day;
    }

    public String getPlan_end_day() {
        return plan_end_day;
    }

    public void setPlan_end_day(String plan_end_day) {
        this.plan_end_day = plan_end_day;
    }

    public String getReal_start_day() {
        return real_start_day;
    }

    public void setReal_start_day(String real_start_day) {
        this.real_start_day = real_start_day;
    }

    public int getCovered_area() {
        return covered_area;
    }

    public void setCovered_area(int covered_area) {
        this.covered_area = covered_area;
    }

    public int getConstruction_area() {
        return construction_area;
    }

    public void setConstruction_area(int construction_area) {
        this.construction_area = construction_area;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public int getProject() {
        return project;
    }

    public void setProject(int project) {
        this.project = project;
    }

    public List<MileStonesBean> getMile_stones() {
        return mile_stones;
    }

    public void setMile_stones(List<MileStonesBean> mile_stones) {
        this.mile_stones = mile_stones;
    }

    public List<String> getEffect_pics() {
        return effect_pics;
    }

    public void setEffect_pics(List<String> effect_pics) {
        this.effect_pics = effect_pics;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(delay_day);
        dest.writeString(project__status);
        dest.writeString(address);
        dest.writeString(name);
        dest.writeString(investment);
        dest.writeString(invested);
        dest.writeString(employer);
        dest.writeString(designer);
        dest.writeString(contractor);
        dest.writeString(supervisor);
        dest.writeString(employer_contact);
        dest.writeString(employer_phone);
        dest.writeString(contractor_contact);
        dest.writeString(contractor_phone);
        dest.writeString(plan_start_day);
        dest.writeString(plan_end_day);
        dest.writeString(real_start_day);
        dest.writeInt(covered_area);
        dest.writeInt(construction_area);
        dest.writeString(region);
        dest.writeString(schedule);
        dest.writeInt(project);
        dest.writeStringList(effect_pics);
    }

    public static class MileStonesBean implements Parcelable{
        /**
         * subs : [{"finished":true,"plane_date":null,"name":"土地证","real_date":null},{"finished":true,"plane_date":null,"name":"用地规划许可证","real_date":null},{"finished":true,"plane_date":null,"name":"建设工程规划许可证","real_date":null},{"finished":true,"plane_date":null,"name":"消防审批，人防审批，绿化，环保审批，档案馆备案，质监部门签订质监合同","real_date":null},{"finished":true,"plane_date":null,"name":"三通一平","real_date":null}]
         * finished : true
         * name : 前期审批
         * plane_date : 2020-01-12
         * real_date : 2020-01-12
         */

        private boolean finished;
        private String name;
        private String plane_date;
        private String real_date;
        private List<SubsBean> subs;

        protected MileStonesBean(Parcel in) {
            finished = in.readByte() != 0;
            name = in.readString();
            plane_date = in.readString();
            real_date = in.readString();
        }

        public static final Creator<MileStonesBean> CREATOR = new Creator<MileStonesBean>() {
            @Override
            public MileStonesBean createFromParcel(Parcel in) {
                return new MileStonesBean(in);
            }

            @Override
            public MileStonesBean[] newArray(int size) {
                return new MileStonesBean[size];
            }
        };

        public boolean isFinished() {
            return finished;
        }

        public void setFinished(boolean finished) {
            this.finished = finished;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPlane_date() {
            return plane_date;
        }

        public void setPlane_date(String plane_date) {
            this.plane_date = plane_date;
        }

        public String getReal_date() {
            return real_date;
        }

        public void setReal_date(String real_date) {
            this.real_date = real_date;
        }

        public List<SubsBean> getSubs() {
            return subs;
        }

        public void setSubs(List<SubsBean> subs) {
            this.subs = subs;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeByte((byte) (finished ? 1 : 0));
            dest.writeString(name);
            dest.writeString(plane_date);
            dest.writeString(real_date);
        }

        public static class SubsBean implements Parcelable{
            /**
             * finished : true
             * plane_date : null
             * name : 土地证
             * real_date : null
             */

            private boolean finished;
            private String plane_date;
            private String name;
            private String real_date;

            protected SubsBean(Parcel in) {
                finished = in.readByte() != 0;
                plane_date = in.readString();
                name = in.readString();
                real_date = in.readString();
            }

            public static final Creator<SubsBean> CREATOR = new Creator<SubsBean>() {
                @Override
                public SubsBean createFromParcel(Parcel in) {
                    return new SubsBean(in);
                }

                @Override
                public SubsBean[] newArray(int size) {
                    return new SubsBean[size];
                }
            };

            public boolean isFinished() {
                return finished;
            }

            public void setFinished(boolean finished) {
                this.finished = finished;
            }

            public String getPlane_date() {
                return plane_date;
            }

            public void setPlane_date(String plane_date) {
                this.plane_date = plane_date;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getReal_date() {
                return real_date;
            }

            public void setReal_date(String real_date) {
                this.real_date = real_date;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeByte((byte) (finished ? 1 : 0));
                dest.writeString(plane_date);
                dest.writeString(name);
                dest.writeString(real_date);
            }
        }
    }
}
