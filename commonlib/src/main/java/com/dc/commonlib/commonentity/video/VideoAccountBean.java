package com.dc.commonlib.commonentity.video;

import android.content.Context;
import android.content.SharedPreferences;

import com.dc.baselib.BaseApplication;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class VideoAccountBean implements Serializable {
    private static final String VIDEO_FILE_NAME = "videoAccountInfo";
    private static String spVideoAccountInfoJsonKey;
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;

    @SerializedName("project_id")
    private int projectId;
    @SerializedName("region_uuid")
    private String regionUuid;
    @SerializedName("login_account")
    private String loginAccount;
    private String password;
    @SerializedName("server_ip")
    private String serverIp;
    @SerializedName("mobile_port")
    private String mobilePort;
    boolean isLogin;

    public boolean isLogin() {
        return isLogin;
    }

    public void setLogin(boolean login) {
        isLogin = login;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public String getRegionUuid() {
        return regionUuid;
    }

    public void setRegionUuid(String regionUuid) {
        this.regionUuid = regionUuid;
    }

    public String getLoginAccount() {
        return loginAccount;
    }

    public void setLoginAccount(String loginAccount) {
        this.loginAccount = loginAccount;
    }

    public String getServerIp() {return serverIp;}

    public String getMobilePort() {return mobilePort;}

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public static void setVideoInfo(VideoAccountBean info) {
        getSharedPreferencesEditorInstance(BaseApplication.getsInstance());
        String gson = new Gson().toJson(info);
        editor.putString(spVideoAccountInfoJsonKey, gson);
        editor.commit();
    }

    public static String getVideoInfo() {
        getSharedPreferencesInstance(BaseApplication.getsInstance());
        return sharedPreferences.getString(spVideoAccountInfoJsonKey, "");
    }

    @Override
    public String toString() {
        return "VideoAccountBean{" +
                "projectId=" + projectId +
                ", regionUuid='" + regionUuid + '\'' +
                ", loginAccount='" + loginAccount + '\'' +
                ", password='" + password + '\'' +
                ", serverIp='" + serverIp + '\'' +
                ", mobilePort='" + mobilePort + '\'' +
                '}';
    }

    public static SharedPreferences getSharedPreferencesInstance(Context context) {
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences(VIDEO_FILE_NAME, Context.MODE_PRIVATE);
        }
        return sharedPreferences;
    }

    public static SharedPreferences.Editor getSharedPreferencesEditorInstance(Context context) {
        if (editor == null) {
            getSharedPreferencesInstance(context);
            editor = sharedPreferences.edit();
        }
        return editor;
    }
}
