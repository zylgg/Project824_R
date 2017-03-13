package com.example.mr_zyl.project.mvp.presenter.impl;

import com.example.mr_zyl.project.mvp.presenter.MvpPresenter;
import com.example.mr_zyl.project.mvp.view.MvpView;

/**
 * Created by Mr_Zyl on 2016/8/24.
 */
public class MvpBasePresenter<V extends MvpView> implements MvpPresenter<V> {
    private V view;

    @Override
    public void attachView(V view) {
         this.view=view;
    }

    @Override
    public void detachView() {
       if (view!=null){
           view=null;
       }
    }
}
