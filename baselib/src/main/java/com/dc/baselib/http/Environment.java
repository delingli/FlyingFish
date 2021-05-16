package com.dc.baselib.http;


import android.util.Log;

import com.dc.baselib.BuildConfig;
import com.zld.websocket.util.WSLogUtil;

/**
 * 运行环境相关的配置
 * <p>
 * Created by ZhangKe on 2019/3/21.
 */
public class Environment {

    private static final String TAG = "Environment";

    private static volatile Environment instance;

    /**
     * 域名
     */
    private Domain domain;

    private EnvironmentType curEnvironmentType;

    public static Environment getInstance() {
        if (instance == null) {
            synchronized (Environment.class) {
                if (instance == null) {
                    instance = new Environment();
                }
            }
        }
        return instance;
    }

    private Environment() {
        init();
    }

    private void init() {
        curEnvironmentType = initEnv();
        domain = getDomain(curEnvironmentType);
        Log.i(TAG, domain.toString());
    }

    private EnvironmentType initEnv() {
        if (BuildConfig.DEBUG) {
//            return EnvironmentType.BUILDER;
            return EnvironmentType.TEST;
//            return EnvironmentType.PRE_BUILDER;
//           return EnvironmentType.PERF_TEST;
        } else {
            return EnvironmentType.BUILDER;
        }
    }

    private Domain getDomain(EnvironmentType type) {
        Domain domain = new Domain();
        switch (type) {
            case TEST:
                domain.rootDomain = Config.DOMAIN_TEST;
                domain.uploadFileDomain = Config.UPLOAD_FILE_DOMAIN_TEST;
                domain.uploadLogDomain = Config.UPLOAD_LOG_DOMAIN;
                domain.htmlUpdateDomain = Config.HTML_UPDATE_DOMAIN_TEST;
                break;
            case PERF_TEST:
                domain.rootDomain = Config.DOMAIN_PERF_TEST;
                domain.uploadFileDomain = Config.UPLOAD_FILE_DOMAIN_PERF;
                domain.uploadLogDomain = Config.UPLOAD_LOG_DOMAIN;
                domain.htmlUpdateDomain = Config.HTML_UPDATE_DOMAIN_TEST;
                break;
            case PRE_BUILDER:
                domain.rootDomain = Config.DOMAIN_PRE_BUILDER;
                domain.uploadFileDomain = Config.UPLOAD_FILE_DOMAIN_PRE;
                domain.uploadLogDomain = Config.UPLOAD_LOG_DOMAIN;
                domain.htmlUpdateDomain = Config.HTML_UPDATE_DOMAIN_TEST;
                break;
            case BUILDER:
            default:
                domain.rootDomain = Config.DOMAIN_BUILDER;
                domain.uploadFileDomain = Config.UPLOAD_FILE_DOMAIN_BUILDER;
                domain.uploadLogDomain = Config.UPLOAD_LOG_DOMAIN;
                domain.htmlUpdateDomain = Config.HTML_UPDATE_DOMAIN_BUILDER;
                break;
        }
        return domain;
    }

    /**
     * 获取全局域名
     */
    public String getDomain() {
        checkAndInitDomain();
        return domain.rootDomain;
    }

    /**
     * 获取 WebSocket 连接地址
     */
    public String getWSUrl() {
        checkAndInitDomain();
        String wsurl = String.format("wss://%s/wsapi", domain.rootDomain);
        WSLogUtil.d("WSURL", wsurl);
        return String.format("wss://%s/wsapi", domain.rootDomain);
    }

    /**
     * 获取 HTTP 地址
     */
    public String getHttpUrl() {
        checkAndInitDomain();
        return String.format("https://%s", domain.rootDomain);
    }

    /**
     * 获取上传文件URL
     */
    public String getUploadFileUrl() {
        checkAndInitDomain();
        return String.format("https://%s/upfile/", domain.uploadFileDomain);
    }

    /**
     * 获取 HTML 更新相关地址前缀
     */
    public String getHtmlUpdateUrl() {
        checkAndInitDomain();
        return String.format("https://%s", domain.htmlUpdateDomain);
    }

    /**
     * 获取上传日志地址
     */
    public String getUploadLogUrl() {
        checkAndInitDomain();
        return String.format("https://%s/uplog", domain.uploadLogDomain);
    }

    private void checkAndInitDomain() {
        if (domain == null) {
            if (curEnvironmentType == null) {
                curEnvironmentType = initEnv();
            }
            domain = getDomain(curEnvironmentType);
        }
    }

    /**
     * 获取当前环境
     */
    public EnvironmentType getCurrentEnvType() {
        return curEnvironmentType;
    }

    /**
     * 环境
     */
    public enum EnvironmentType {
        TEST,//测试
        BUILDER,//线上
        PERF_TEST,//自动化测试
        PRE_BUILDER,//预发布
    }

    /**
     * 存储域名
     */
    private class Domain {

        /**
         * 全局域名
         */
        String rootDomain;
        /**
         * 上传文件地址
         */
        String uploadFileDomain;
        /**
         * 上传日志地址
         */
        String uploadLogDomain;
        /**
         * HTML 更新域名
         */
        String htmlUpdateDomain;

        @Override
        public String toString() {
            return String.format("rootDomain:%s; uploadFileDomain:%s, uploadLogDomain:%s; htmlUpdateDomain:%s",
                    rootDomain,
                    uploadFileDomain,
                    uploadLogDomain,
                    htmlUpdateDomain);

        }
    }
}
