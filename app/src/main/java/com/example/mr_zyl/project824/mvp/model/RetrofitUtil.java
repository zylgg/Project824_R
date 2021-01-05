package com.example.mr_zyl.project824.mvp.model;

import android.util.Log;


import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitUtil {
    private Retrofit mRetrofit;


    //构造器私有，这个工具类只有一个实例
    private RetrofitUtil() {
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        httpClientBuilder.connectTimeout(15, TimeUnit.SECONDS);
        mRetrofit = new Retrofit.Builder()
                .client(httpClientBuilder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl("http://api.budejie.com/")
//                .baseUrl("http://api.k780.com")
                .build();
    }


    /**
     * 静态内部类单例模式
     *
     * @return
     */
    public static RetrofitUtil getInstance() {
        return Inner.retrofitManager;
    }

    private static class Inner {
        private static final RetrofitUtil retrofitManager = new RetrofitUtil();
    }


    /**
     * 利用泛型传入接口class返回接口实例
     */

    public <T> T create(Class<T> service) {
        return mRetrofit.create(service);
    }
}