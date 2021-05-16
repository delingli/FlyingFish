package com.dc.baselib.http.exception;

import com.dc.baselib.BaseApplication;
import com.dc.baselib.R;
import com.dc.baselib.http.newhttp.StatusCode;
import com.google.gson.JsonParseException;

import org.json.JSONException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.text.ParseException;

import retrofit2.HttpException;

/*
处理异常类
 */
public class CustomException {


    /**
     * 未知错误
     */
    public static final int UNKNOWN = 1000;

    /**
     * 解析错误
     */
    public static final int PARSE_ERROR = 1001;

    /**
     * 网络错误
     */
    public static final int NETWORK_ERROR = 1002;
    /**
     * 服务器错误
     */
    public static final int SERVER_ERROR = 1003;

    public static ApiException handlerServerException(Throwable e) {

        ApiException exception;
        if (e instanceof HttpException) {
            //网络错误
            exception = new ApiException(SERVER_ERROR, e.getMessage());
            return exception;
        } else if (e instanceof JsonParseException
                || e instanceof JSONException
                || e instanceof ParseException) {
            //解析错误
            exception = new ApiException(PARSE_ERROR, e.getMessage());
            return exception;
        } else if (e instanceof ConnectException) {
            //网络错误
            exception = new ApiException(NETWORK_ERROR, e.getMessage());
            return exception;
        } else if (e instanceof UnknownHostException || e instanceof SocketTimeoutException) {
            //连接错误
            exception = new ApiException(NETWORK_ERROR, e.getMessage());
            return exception;
        } else {
            //未知错误
            exception = new ApiException(UNKNOWN, e.getMessage());
            return exception;
        }
    }

    /**
     * 统一处理一下服务器返回错误码提示
     *
     * @param e
     * @return
     */
    public static ApiException handlerCustomException(ApiException e) {
        switch (e.getCode()) {
/*            case -11:// 验证码错误
                e.setMes(BaseApplication.getsInstance().getResources().getString(R.string.tip_verification_code_error));
                break;
            case -12:// 验证码失效
                e.setMes(BaseApplication.getsInstance().getResources().getString(R.string.tip_verification_code_failure));
                break;
            case -13://  微信绑定失败
                e.setMes(BaseApplication.getsInstance().getResources().getString(R.string.tip_wechar_bind_failure));
                break;
            case -14:// QQ绑定失败
                e.setMes(BaseApplication.getsInstance().getResources().getString(R.string.tip_qq_bind_failure));
                break;
            case -16:// 邮箱已经注册
                e.setMes(BaseApplication.getsInstance().getResources().getString(R.string.email_already_regist));
                break;
            case -17:// 邮箱格式有误
                e.setMes(BaseApplication.getsInstance().getResources().getString(R.string.email_format_error));
                break;
            case -21:// 用户名已经存在
                e.setMes(BaseApplication.getsInstance().getResources().getString(R.string.tip_username_already));
                break;

            default://不知道错误返回服务给的错误
                return e;*/

        }
        return e;
    }
}
