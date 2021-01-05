package com.example.mr_zyl.project824.pro.essence.dataservice;

import com.example.mr_zyl.project824.bean.PostsListBean;
import com.example.mr_zyl.project824.pro.essence.model.EssenceVideoModel;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.adapter.rxjava2.Result;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface EssenceService {

    //            param.put("a", "list");
//        param.put("c", "data");
//        param.put("type", "" + type);
//        param.put("page", "" + page);
//        if (!TextUtils.isEmpty(maxtime)) {
//            param.put("maxtime", maxtime);
//        }
    @Headers({
            "Content-Type:application/json",
            "Charset:UTF-8",
            "Connection:keep-alive",
            "Content-Encoding:gzip"
    })
    @FormUrlEncoded
    @POST("api/api_open.php")
    Observable<Result<PostsListBean>> getEssenceData(@FieldMap Map<String, String> maps);


    @FormUrlEncoded
    @POST("/")
    Observable<Result<NowWeatherBean>> getNowWeather(@FieldMap Map<String, Object> map);

}
