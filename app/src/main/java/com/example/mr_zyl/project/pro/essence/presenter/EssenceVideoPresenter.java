package com.example.mr_zyl.project.pro.essence.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.example.mr_zyl.project.bean.PostsListBean;
import com.example.mr_zyl.project.http.utils.HttpUtils;
import com.example.mr_zyl.project.pro.base.presenter.BasePresenter;
import com.example.mr_zyl.project.pro.essence.model.EssenceVideoModel;

import java.util.List;

/**
 * Created by TFHR02 on 2017/2/22.
 */
public class EssenceVideoPresenter extends BasePresenter<EssenceVideoModel> {
    private int page=0;
    private String maxtime=null;
    public EssenceVideoPresenter(Context context) {
        super(context);
    }

    @Override
    public EssenceVideoModel bindModel() {
        return new EssenceVideoModel(getContext());
    }

    public void GetEssenceListData(int type, final boolean isDownRefresh, final OnUiThreadListener<List<PostsListBean.PostList>> onhttpResultlistener){
        if (isDownRefresh){
            maxtime=null;
        }
        getModel().getEssenceList(type,page,maxtime, new HttpUtils.OnHttpResultListener() {
            @Override
            public void onResult(String result) {
                if (TextUtils.isEmpty(result)){
                    //等于空---通知UI线程---刷新UI界面
                    onhttpResultlistener.OnResult(null);
                }else {
                    //不等于null
                    //解析数据
                   PostsListBean lists=getGson().fromJson(result,PostsListBean.class);
                    //处理分页逻辑---UI层只负责现实数据,不要做任何与网络相关的逻辑处理
                    if (lists!=null&&lists.getInfo()!=null){
                        maxtime=lists.getInfo().getMaxtime();
                    }
                    if (isDownRefresh){
                        page=0;
                    }else {
//                        page++;
                    }
                    onhttpResultlistener.OnResult(lists.getList());
                }
            }
        });
    }
}
