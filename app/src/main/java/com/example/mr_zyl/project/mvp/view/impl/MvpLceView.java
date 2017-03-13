package com.example.mr_zyl.project.mvp.view.impl;

import com.example.mr_zyl.project.mvp.view.MvpView;

/**
 * 请求数据，刷新UI界面监听（标准）-即loading页
 * Created by Mr_Zyl on 2016/8/24.
 */
public interface MvpLceView extends MvpView {
    //显示加载中的视图（监听加载类型，例如：下拉刷新或者上拉加载）
    public void showLoading(boolean pullToRefresh);

    //显示contentview
    public void showContent();

    //加载错误
    public void showError(Exception e, boolean pullToRefresh);

    //绑定数据
    public void showData();
}
