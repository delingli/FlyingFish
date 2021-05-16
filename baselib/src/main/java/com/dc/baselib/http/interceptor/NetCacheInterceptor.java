package com.dc.baselib.http.interceptor;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 这里设置网络缓存，有网情况下，现在缓存时间设置0表示有网下无缓存
 * 无网络也是获取不到response的，response缓存时间是无效的
 */
public class NetCacheInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();//request缓存
        Response response = chain.proceed(originalRequest);
        int onlineCacheTime = 0;//在线的时候的缓存过期时间，如果想要不缓存，直接时间设置为0
        return response.newBuilder()
                .removeHeader("Pragma")
                .header("Cache-Control", "public, max-age=" + onlineCacheTime)

                .build();
    }


}
