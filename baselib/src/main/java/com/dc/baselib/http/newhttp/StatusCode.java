package com.dc.baselib.http.newhttp;

public class StatusCode {
    public static final String SUCESSCODE = "C100000";//success
    public static final String USERNAME_NOTEFOUND = "C100004";//用户不存在
    public static final String PASSWORD_ERRORCODE = "C100005";//密码错误
    public static final String SERVER_FAIL = "C100001";//fail
    public static final String NO_LOGIN_CODE = "C100002";//用户未登录
    public static final String NOLL_FILE_CODE = "C100003";//上传文件为空
    public static final String DELETE_PROMISE_CODE = "C120000";//诺言被删除


    public static final String C110000 = "C110000";// 请按要求填写注册内容
    public static final String C110001 = "C110001";//注册方式暂不支持
    public static final String C110002 = "C110002";//手机号不能为空
    public static final String C110003 = "C110003";//验证码不能为空
    public static final String C110004 = "C110004";//密码不能为空
    public static final String C110005 = "C110005";//微信号不能为空

    public static final String C110006 = "C110006";//请启用Cookie
    public static final String C110007 = "C110007";//验证码不符
    public static final String C110008 = "C110008";//该手机号已经注册，不能重复注册
    public static final String C110009 = "C110009";//注册失败

    public static final String C220000 = "C220000";//无参数
    public static final String C220001 = "C220001";//该手机号已存在
    public static final String C220002 = "C220002";//无效手机号
    public static final String C220003 = "C220003";//原密码有误


}
