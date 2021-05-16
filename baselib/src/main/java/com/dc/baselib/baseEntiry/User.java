package com.dc.baselib.baseEntiry;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class User implements Serializable {
/*    "username": "18969886105@29",
            "is_owner": true,
            "user_id": 7573,
            "realname": "陶辉",
            "captcha": false,
            "auth_pass": true,
            "company_id": 29,
            "pt_admin": false,
            "is_superuser": false,
            "company_manager": "银湖开发区主任",
            "worker_id": 7573,
            "company_name": "杭州光谷置业有限公司",
            "sid": "cli7573:72428ef88d620f5fcd4374fb052bce27",
            "groups_list": ["已审核职员帐号"],
            "company_role": "管理层",
    */


    public String username;
    public boolean is_owner;
    public String user_id;
    public boolean captcha;
    public String realname;
    public boolean auth_pass;
    public int company_id;
    public boolean pt_admin;
    public boolean is_superuser;
    public String company_manager;
    public int worker_id;
    public String company_name;
    public String sid;//token
    public List<String> groups_list;
    public String company_role;
    public List<String> company_permissions;
    public List<ChildrenBean> children;
    public String face_image;


    public class ChildrenBean implements Serializable {
        public String id;
        public boolean employee__company__is_owner;
        public String employee__company__name;
    }


    public User() {
    }

}
