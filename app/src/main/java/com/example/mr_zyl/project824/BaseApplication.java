package com.example.mr_zyl.project824;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.StrictMode;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.example.mr_zyl.project824.bean.MyObjectBox;
import com.example.mr_zyl.project824.bean.UILImageLoader;
import com.lqr.imagepicker.ImagePicker;
import com.lqr.imagepicker.view.CropImageView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.tencent.intervideo.nowproxy.NowLive;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.cookie.CookieJarImpl;
import com.zhy.http.okhttp.cookie.store.PersistentCookieStore;
import com.zhy.http.okhttp.log.LoggerInterceptor;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.objectbox.BoxStore;
import me.xiaopan.sketch.Configuration;
import me.xiaopan.sketch.Sketch;
import me.xiaopan.sketch.cache.LruDiskCache;
import okhttp3.OkHttpClient;

/**
 * Created by Mr_Zyl on 2016/9/18.
 */
public class BaseApplication extends MultiDexApplication {
    public static BoxStore myObjectBox;

    @Override
    public void onCreate() {
        super.onCreate();
        closeAndroidPDialog();
        // android 7.0系统解决拍照的问题
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
            builder.detectFileUriExposure();
        }

        myObjectBox = MyObjectBox.builder().androidContext(this).build();
        initUniversalImageLoader();
        initImagePicker();

        //okhttputils配置
        CookieJarImpl cookieJar = new CookieJarImpl(new PersistentCookieStore(getApplicationContext()));
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new LoggerInterceptor("OkhttpClient"))
                .connectTimeout(20 * 1000L, TimeUnit.MILLISECONDS)
                .readTimeout(20 * 1000L, TimeUnit.MILLISECONDS)
                .cookieJar(cookieJar)
                .build();
        OkHttpUtils.initClient(okHttpClient);

        Sketch.with(this).getConfiguration().getDiskCache().setDisabled(true);//允许使用磁盘缓存
        Sketch.with(this).getConfiguration().getMemoryCache().setDisabled(true);//允许使用内存缓存
        Sketch.with(this).getConfiguration().getBitmapPool().setDisabled(true);//允许使用bitmappool缓存
        // 最大容量为APP最大可用内存的十分之一
        int newMemoryCacheMaxSize = (int) (Runtime.getRuntime().maxMemory() / 10);
        Configuration configuration = Sketch.with(this).getConfiguration();
        //设置最大的内存缓存
//        configuration.setMemoryCache(new LruMemoryCache(this,newMemoryCacheMaxSize));
        //设置最大的磁盘缓存
        configuration.setDiskCache(new LruDiskCache(this, configuration, 1, 50 * 1024 * 1024));

//        //now直播
//        NowLive.start(this,NowLive.makeInitData(NowLive.NOW_SDK_DEMO_APPID,"1.0.0.1"));

    }

    private void initUniversalImageLoader() {
        //初始化ImageLoader
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(getApplicationContext()));
    }

    /**
     * 初始化仿微信控件ImagePicker
     */
    private void initImagePicker() {
        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new UILImageLoader());   //设置图片加载器
        imagePicker.setShowCamera(true);  //显示拍照按钮
        imagePicker.setCrop(false);        //允许裁剪（单选才有效）
        imagePicker.setSaveRectangle(false); //是否按矩形区域保存
        imagePicker.setSelectLimit(9);    //选中数量限制
        imagePicker.setStyle(CropImageView.Style.CIRCLE);  //裁剪框的形状
        imagePicker.setFocusWidth(800);   //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
        imagePicker.setFocusHeight(800);  //裁剪框的高度。单位像素（圆形自动取宽高最小值）
        imagePicker.setOutPutX(800);//保存文件的宽度。单位像素
        imagePicker.setOutPutY(800);//保存文件的高度。单位像素
        imagePicker.setMultiMode(false);//是否多选
    }

    /**
     * 释放资源，退出程序时候调用
     */
    private final void release() {
        try {
            for (Activity activity : mList) {
                if (activity != null) {
                    activity.finish();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 程序退出
     *
     * @param code
     */
    public final void exit(int code) {
        release();
        System.exit(code);
    }

    /**
     * 实现android 程序退出
     */
    private List<Activity> mList = new LinkedList<Activity>();


    /**
     * 在BaseActivity的onCreate方法中调用 ，维护一个activity队列
     *
     * @param activity
     */
    public void addActivity(Activity activity) {
        mList.add(activity);
    }

    /**
     * 在BaseActivity的onDestroy方法中调用 如果一个activity已经销毁了
     * 从队列中删除
     *
     * @param activity
     */
    public void removeActivity(Activity activity) {
        mList.remove(activity);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    /**
     * 关闭android P版本以上的debug模式应用 调用未开放的api 提示框
     */
    private void closeAndroidPDialog() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            try {
                Class aClass = Class.forName("android.content.pm.PackageParser$Package");
                Constructor declaredConstructor = aClass.getDeclaredConstructor(String.class);
                declaredConstructor.setAccessible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                Class cls = Class.forName("android.app.ActivityThread");
                Method declaredMethod = cls.getDeclaredMethod("currentActivityThread");
                declaredMethod.setAccessible(true);
                Object activityThread = declaredMethod.invoke(null);
                Field mHiddenApiWarningShown = cls.getDeclaredField("mHiddenApiWarningShown");
                mHiddenApiWarningShown.setAccessible(true);
                mHiddenApiWarningShown.setBoolean(activityThread, true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
