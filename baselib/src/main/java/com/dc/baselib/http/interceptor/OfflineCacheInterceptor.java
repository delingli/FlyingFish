package com.dc.baselib.http.interceptor;

import com.dc.baselib.BaseApplication;
import com.dc.baselib.utils.NetWorkUtils;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 这里设置网络缓存，无有网情况下，
 * 无网络无限期缓存
 */
public class OfflineCacheInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();//request缓存
        if (!NetWorkUtils.isNetworkAvailable(BaseApplication.getsInstance())) {
            int CACHEDAY = 365;//离线的时候的缓存的过期时间
            request = request.newBuilder()
                    .cacheControl(new CacheControl
                            .Builder()
                            .maxStale(CACHEDAY, TimeUnit.DAYS)
//                            .onlyIfCached()
                            .build()).build();

//        两种方式结果是一样的，写法不同
//                    .header("Cache-Control", "public, only-if-cached, max-stale=" + offlineCacheTime)
//                    .build();
        }
        return chain.proceed(request);
    }


}
