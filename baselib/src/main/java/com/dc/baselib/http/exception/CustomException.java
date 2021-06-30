package com.dc.baselib.http.exception;

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
    public static final int JSONCODE = 101;
    public static final int NETWORKCODE = 102;
    public static final int CONNECTCODE = 103;


    public static ApiException handlerServerException(Throwable e) {

        ApiException exception;
        if (e instanceof HttpException) {
            //网络错误
            exception = new ApiException(((HttpException) e).code(), e.getMessage());
            return exception;
        } else if (e instanceof JsonParseException
                || e instanceof JSONException
                || e instanceof ParseException) {
            //解析错误
            exception = new ApiException(JSONCODE, e.getMessage());
            return exception;
        } else if (e instanceof ConnectException) {
            //网络错误
            exception = new ApiException(NETWORKCODE, e.getMessage());
            return exception;
        } else if (e instanceof UnknownHostException || e instanceof SocketTimeoutException) {
            //连接错误
            exception = new ApiException(CONNECTCODE, e.getMessage());
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
        return e;
    }
}
