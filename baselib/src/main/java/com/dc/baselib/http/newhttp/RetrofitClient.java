package com.dc.baselib.http.newhttp;

import com.dc.baselib.BaseApplication;
import com.dc.baselib.BuildConfig;
import com.dc.baselib.constant.Constants;
import com.dc.baselib.http.interceptor.CommonParamsInterceptor;
import com.dc.baselib.http.interceptor.HeaderInterceptor;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RetrofitClient {
    private int DEFAULT_TIME_OUT = 30;
    private volatile static RetrofitClient instance;
    private Retrofit retrofit;
    private Retrofit changeRetrofit;
    private Retrofit cacheRetrofit;
    private final OkHttpClient.Builder mOkHttpBuilder;

    private RetrofitClient() {
        //Okhttp配置
        //错误重连
        mOkHttpBuilder = new OkHttpClient.Builder()
                .connectTimeout(DEFAULT_TIME_OUT, TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_TIME_OUT, TimeUnit.SECONDS)
                .readTimeout(DEFAULT_TIME_OUT, TimeUnit.SECONDS)
                .addInterceptor(new HeaderInterceptor())
                .addInterceptor(new CommonParamsInterceptor())
                .retryOnConnectionFailure(true);

        retrofit = new Retrofit.Builder()
                .baseUrl(Constants.WEB_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
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


    public Retrofit getChangeRetrofit(String baseUrl) {
        changeRetrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())//支持字符串
                .client(mOkHttpBuilder.build()).build();
        return changeRetrofit;
    }
}
