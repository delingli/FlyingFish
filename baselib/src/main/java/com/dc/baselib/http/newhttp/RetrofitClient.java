package com.dc.baselib.http.newhttp;

import android.util.Log;

import com.dc.baselib.BaseApplication;
import com.dc.baselib.BuildConfig;
import com.dc.baselib.constant.Constants;
import com.dc.baselib.http.interceptor.CommonParamsInterceptor;
import com.dc.baselib.http.interceptor.HeaderInterceptor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RetrofitClient {
    private int DEFAULT_TIME_OUT = 30;
    private volatile static RetrofitClient instance;
    private Retrofit retrofit;
    private final OkHttpClient.Builder mOkHttpBuilder;

    private RetrofitClient() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                //打印retrofit日志
                Log.i("RetrofitLog", "retrofitBack = " + message);
            }
        });
//        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
//        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
//        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.HEADERS);


        //Okhttp配置
        //错误重连
        mOkHttpBuilder = new OkHttpClient.Builder()
                .connectTimeout(DEFAULT_TIME_OUT, TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_TIME_OUT, TimeUnit.SECONDS)
                .readTimeout(DEFAULT_TIME_OUT, TimeUnit.SECONDS)
                .addInterceptor(new HeaderInterceptor())
                .addInterceptor(loggingInterceptor)
                .addInterceptor(new CommonParamsInterceptor())
                .retryOnConnectionFailure(true);
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        retrofit = new Retrofit.Builder()
                .baseUrl(Constants.getmConstants().WEB_URL.trim())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
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

}
