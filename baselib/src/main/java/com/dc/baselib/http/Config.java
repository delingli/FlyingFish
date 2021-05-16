package com.dc.baselib.http;

/**
 * 全局配置，主要存储一些静态全局变量
 * <p>
 * Created by ZhangKe on 2017/12/5.
 */
public interface Config {

    String appId = "Fqa9LM1ww4qcT58MWYjfkDq8DPeycb76t4jhK7Eu9QNY";
    String sdkKey = "3yXBYs6VGfvC1voKfxB4c2jmdQBJpCZoAksNiFULw3BM";

    //全局域名
    /**
     * 测试环境地址
     */
    String DOMAIN_TEST = "zldtest.zlddata.cn";
    // http://builder.dccnet.com.cn/
    /**
     * 正式环境地址
     */
    String DOMAIN_BUILDER = "builder.zlddata.com";
    /**
     * 自动化环境地址
     */
    String DOMAIN_PERF_TEST = "perftest.zlddata.cn";
    /**
     * 预发布环境地址
     */
    String DOMAIN_PRE_BUILDER = "builder.zlddata.cn";
    /**
     * 演示环境地址
     */
    String DOMAIN_UE = "ue.zlddata.cn";


    //上传文件地址
    /**
     * 测试环境上传文件地址
     */
    String UPLOAD_FILE_DOMAIN_TEST = "zldtest-upload.zldhz.com";
    /**
     * 自动化环境地址
     */
    String UPLOAD_FILE_DOMAIN_PERF = "perftest-upload.zldhz.com";
    /**
     * 预发布环境地址
     */
    String UPLOAD_FILE_DOMAIN_PRE = "builder-upload.zldhz.com";
    /**
     * 正式环境上传文件地址
     */
    String UPLOAD_FILE_DOMAIN_BUILDER = "upload.zlddata.com";


    //HTML 更新域名
    /**
     * 测试环境 HTML 更新域名
     */
    String HTML_UPDATE_DOMAIN_TEST = "appgx.zldhz.com";
    /**
     * 正式环境 HTML 更新域名
     */
    String HTML_UPDATE_DOMAIN_BUILDER = "appupdate.zlddata.com";


    //上传 Log 文件地址
    /**
     * 上传 Log 文件域名
     */
    String UPLOAD_LOG_DOMAIN = "zldtestlog.zldhz.com";

    /**
     * 建设属性的工程字段值
     */
    int BUILD_PROJECT_ATTR = 1 << 2;
}
