package com.example.mr_zyl.project824.mvp.presenter;

import com.example.mr_zyl.project824.mvp.view.MvpView;

/**
 * Created by Mr_Zyl on 2016/8/24.
 */
public interface MvpPresenter<V extends MvpView> {
    //绑定view
    public void attachView(V view);
    //解除view绑定
    public void detachView();
}
