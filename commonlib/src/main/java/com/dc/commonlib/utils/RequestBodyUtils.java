package com.dc.commonlib.utils;

import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class RequestBodyUtils {
    public static RequestBody getRequestBody(Map params) {
        String json = JsonUtil.toJson(params);
        return RequestBody.create(MediaType.parse("application/json;charset=utf-8"), json);
    }
/*    public static RequestBody getRequestBodyFormData(Map<String, String> params) {
        String json = JsonUtil.toJson(params);
        return RequestBody.create(MediaType.parse("multipart/form-data"), json);
    }*/
}
