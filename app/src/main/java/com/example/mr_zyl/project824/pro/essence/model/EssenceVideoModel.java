package com.example.mr_zyl.project824.pro.essence.model;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.example.mr_zyl.project824.bean.PostsListBean;
import com.example.mr_zyl.project824.http.impl.RequestParam;
import com.example.mr_zyl.project824.http.impl.SystemHttpCommand;
import com.example.mr_zyl.project824.http.utils.HttpTask;
import com.example.mr_zyl.project824.http.utils.HttpUtils;
import com.example.mr_zyl.project824.mvp.model.RetrofitUtil;
import com.example.mr_zyl.project824.pro.base.model.BaseModel;
import com.example.mr_zyl.project824.pro.essence.dataservice.EssenceService;
import com.example.mr_zyl.project824.pro.essence.dataservice.NowWeatherBean;
import com.google.gson.Gson;
//import com.zhy.http.okhttp.OkHttpUtils;
//import com.zhy.http.okhttp.callback.Callback;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.Result;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by TFHR02 on 2017/2/22.
 * M层:数据层
 * <p>
 * 请求网络数据
 * 加载本地数据库缓存数据
 * 加载SD卡数据等等......
 */
public class EssenceVideoModel extends BaseModel {
    private String TAG=this.getClass().getSimpleName();

    public EssenceVideoModel(Context context) {
        super(context);
    }

    private String getUrl() {
        return getServerUrl().concat("api/api_open.php");
    }

    /**
     * 获取精华列表
     *
     * @param type---数据类型(例如:图片             视频  音频  段子等等)
     * @param page---页码
     * @param maxtime--用户加载更多
     * @param onhttpResultlistener---数据回调监听
     */
    public void getEssenceList(int type, int page, String maxtime, HttpUtils.OnHttpResultListener onhttpResultlistener) {
        RequestParam param = new RequestParam();
        param.put("a", "list");
        param.put("c", "data");
        param.put("type", type);
        param.put("page", page);
        if (!TextUtils.isEmpty(maxtime)) {
            param.put("maxtime", maxtime);
        }
        //发送请求
        HttpTask task = new HttpTask(getUrl(), param, new SystemHttpCommand(), onhttpResultlistener);
        task.execute();
    }

//    public void getEssenceListByOkHttp(int type, int page, String maxtime, Callback callback) {
//        Map<String, String> param = new HashMap();
//        param.put("a", "list");
//        param.put("c", "data");
//        param.put("type", "" + type);
//        param.put("page", "" + page);
//        if (!TextUtils.isEmpty(maxtime)) {
//            param.put("maxtime", maxtime);
//        }
//        OkHttpUtils.post().url(getUrl()).params(param).build().execute(callback);
//    }

    public void getEssenceListByRetrofit(int type, int page, String maxtime, retrofit2.Callback callback) {

        Map<String, String> param = new HashMap();
        param.put("a", "list");
        param.put("c", "data");
        param.put("type", "" + type);
        param.put("page", "" + page);
        if (!TextUtils.isEmpty(maxtime)) {
            param.put("maxtime", maxtime);
        }

        EssenceService essenceService = RetrofitUtil.getInstance().create(EssenceService.class);
        essenceService.getEssenceData(param)

                .subscribeOn(Schedulers.io())
                .subscribeOn(Schedulers.newThread())//子线程访问网络
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Result<PostsListBean>>() {

                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.i(TAG,"onSubscribe_"+d.toString());
                    }

                    @Override
                    public void onNext(@NonNull Result<PostsListBean> res) {
                         Log.i(TAG,"onNext_"+res.toString());
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.i(TAG,"onError_"+e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Log.i(TAG,"onComplete");
                    }
                });

//        HashMap<String, Object> map = new HashMap<>();
//        map.put("app", "weather.today");
//        map.put("weaid", 1);
//        map.put("appkey", 10003);
//        map.put("sign", "b59bc3ef6191eb9f747dd4e83c99f2a4");
//        map.put("format", "json");
//        essenceService.getNowWeather(map)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<Result<NowWeatherBean>>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//
//                    }
//
//                    @Override
//                    public void onNext(Result<NowWeatherBean> bean) {
//                        Log.e(TAG, "onNext: " + bean.response().body().result.toString());
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        Log.e(TAG, "onError: " + e.getMessage());
//                    }
//
//                    @Override
//                    public void onComplete() {
//
//                    }
//                });



    }

}
