package com.example.mr_zyl.project.pro.essence.model;

import android.content.Context;
import android.text.TextUtils;

import com.example.mr_zyl.project.http.impl.RequestParam;
import com.example.mr_zyl.project.http.impl.SystemHttpCommand;
import com.example.mr_zyl.project.http.utils.HttpTask;
import com.example.mr_zyl.project.http.utils.HttpUtils;
import com.example.mr_zyl.project.pro.base.model.BaseModel;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by TFHR02 on 2017/2/22.
 * M层:数据层
 * <p>
 * 请求网络数据
 * 加载本地数据库缓存数据
 * 加载SD卡数据等等......
 */
public class EssenceVideoModel extends BaseModel {
    public EssenceVideoModel(Context context) {
        super(context);
    }

    private String getUrl() {
        return getServerUrl().concat("/api/api_open.php");
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

    public void getEssenceListByOkHttp(int type, int page, String maxtime, Callback callback) {
        Map<String, String> param = new HashMap();
        param.put("a", "list");
        param.put("c", "data");
        param.put("type", "" + type);
        param.put("page", "" + page);
        if (!TextUtils.isEmpty(maxtime)) {
            param.put("maxtime", maxtime);
        }
        OkHttpUtils.post().url(getUrl()).params(param).build().execute(callback);
    }

}
