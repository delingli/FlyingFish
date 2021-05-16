package com.dc.baselib.http.interceptor;

import android.text.TextUtils;

import com.dc.baselib.utils.UserManager;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 添加共有参数
 */
public class CommonParamsInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        //1.获取到request
        Request request;
        Request originalRequest = chain.request();
        //2.获取到方法
        String method = originalRequest.method();
        return chain.proceed(originalRequest);
  /*      if (!TextUtils.isEmpty(UserManager.getInstance().getToken())) {
            HttpUrl modifiedUrl = originalRequest.url().newBuilder()
                    .addQueryParameter("sid", UserManager.getInstance().getToken())
                    .build();
            request = originalRequest.newBuilder().url(modifiedUrl).build();//构建request
            return chain.proceed(request);

        } else {
            return chain.proceed(originalRequest);
        }*/
        //            Response response = chain.proceed(request);//进行网络请求并获得返回结果
//            String content = response.body().string();//拿到返回结果，进行分析


    }
}
