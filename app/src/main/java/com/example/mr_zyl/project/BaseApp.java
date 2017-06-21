package com.example.mr_zyl.project;

import android.app.Application;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.cookie.CookieJarImpl;
import com.zhy.http.okhttp.cookie.store.PersistentCookieStore;
import com.zhy.http.okhttp.log.LoggerInterceptor;

import java.util.concurrent.TimeUnit;

import me.xiaopan.sketch.Configuration;
import me.xiaopan.sketch.Sketch;
import me.xiaopan.sketch.cache.LruDiskCache;
import okhttp3.OkHttpClient;

/**
 * Created by Mr_Zyl on 2016/9/18.
 */
public class BaseApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        //imageloader配置
        ImageLoaderConfiguration config=new ImageLoaderConfiguration.Builder(this)
                .writeDebugLogs()
                .build();
        ImageLoader.getInstance().init(config);

        //okhttputils配置
        CookieJarImpl cookieJar = new CookieJarImpl(new PersistentCookieStore(getApplicationContext()));
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new LoggerInterceptor("OkhttpClient"))
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                .cookieJar(cookieJar)
                .build();
        OkHttpUtils.initClient(okHttpClient);

        Sketch.with(this).getConfiguration().getDiskCache().setDisabled(true);//允许使用磁盘缓存
        Sketch.with(this).getConfiguration().getMemoryCache().setDisabled(false);//不允许使用内存缓存
        Sketch.with(this).getConfiguration().getBitmapPool().setDisabled(true);//允许使用bitmappool缓存
        // 最大容量为APP最大可用内存的十分之一
        int newMemoryCacheMaxSize = (int) (Runtime.getRuntime().maxMemory() / 10);
        Configuration configuration = Sketch.with(this).getConfiguration();
//        configuration.setMemoryCache(new LruMemoryCache(this,newMemoryCacheMaxSize));//设置最大的内存缓存
        configuration.setDiskCache(new LruDiskCache(this,configuration, 1, 50 * 1024 * 1024));//设置最大的磁盘缓存
    }
}
