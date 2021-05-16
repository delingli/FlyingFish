package com.dc.baselib.http.newhttp;

import android.text.TextUtils;
import android.util.Log;

import com.dc.baselib.BaseApplication;
import com.dc.baselib.BuildConfig;
import com.dc.baselib.http.Environment;
import com.dc.baselib.http.interceptor.CacheInterceptor;
import com.dc.baselib.http.interceptor.CommonParamsInterceptor;
import com.dc.baselib.http.interceptor.HeaderInterceptor;
import com.dc.baselib.http.interceptor.NetCacheInterceptor;
import com.dc.baselib.http.interceptor.OfflineCacheInterceptor;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Cookie;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RetrofitClient {
    private int DEFAULT_TIME_OUT = 30;
    private volatile static RetrofitClient instance;
    private Retrofit retrofit;
    private PersistentCookieJar cookieJar;
    private final OkHttpClient.Builder mOkHttpBuilder;
    private File httpCacheDirectory;//缓存目录
    private final Gson mGson;

    private RetrofitClient() {
        //Okhttp配置
        mGson = new GsonBuilder()
                .setLenient()  // 设置GSON的非严格模式setLenient()
                .create();
        cookieJar = new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(BaseApplication.getsInstance()));
        NetCacheInterceptor netCacheInterceptor = new NetCacheInterceptor();
        OfflineCacheInterceptor offlineCacheInterceptor = new OfflineCacheInterceptor();
//        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
//            @Override
//            public void log(String message) {
//                //打印retrofit日志
//                Log.i("RetrofitLog", "retrofitBack = " + message);
//            }
//        });
//        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.HEADERS);
        httpCacheDirectory = new File(BaseApplication.getsInstance().getCacheDir(), "promiseCache");
        Log.d("httpCacheDirectory", httpCacheDirectory.getAbsolutePath());
        int cacheSize = 666 * 1024 * 1024; // 666 MiB
//        Cache cache = new Cache(httpCacheDirectory, cacheSize);
        //错误重连
        mOkHttpBuilder = new OkHttpClient.Builder()
                .connectTimeout(DEFAULT_TIME_OUT, TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_TIME_OUT, TimeUnit.SECONDS)
                .readTimeout(DEFAULT_TIME_OUT, TimeUnit.SECONDS)
//                .cache(cache)
                .cookieJar(cookieJar)
                .addInterceptor(new HeaderInterceptor())
//                .addInterceptor(new CommonParamsInterceptor())
                .addNetworkInterceptor(netCacheInterceptor)
                .addInterceptor(offlineCacheInterceptor)
                .retryOnConnectionFailure(true);
        retrofit = new Retrofit.Builder()
                .baseUrl(Environment.getInstance().getHttpUrl())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(mGson))
                .addConverterFactory(ScalarsConverterFactory.create())//支持字符串
                .client(mOkHttpBuilder.build()).build();
    }

    public static RetrofitClient getInstance() {

        if (instance == null) {
            synchronized (RetrofitClient.class) {
                if (instance == null) {
                    instance = new RetrofitClient();
                }
            }
        }
        return instance;
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }


    public List<Cookie> getCookie(String url) {
        if (TextUtils.isEmpty(url)) {
            return null;
        }
        return cookieJar.loadForRequest(HttpUrl.parse(url));
    }
}
