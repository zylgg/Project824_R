package com.example.mr_zyl.project.mvp.view.impl;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import com.example.mr_zyl.project.mvp.presenter.impl.MvpBasePresenter;
import com.example.mr_zyl.project.mvp.view.MvpView;

/**
 * Created by Mr_Zyl on 2016/8/24.
 */
public abstract class MvpFragment<P extends MvpBasePresenter> extends Fragment implements MvpView {
    protected P presenter;
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //绑定视图
        presenter=bindPresenter();
    }
    //抽象出去给别人实现
    public abstract P bindPresenter();

    @Override
    public void onDestroy() {
        super.onDestroy();
        //解绑
        if (presenter!=null){
            presenter.detachView();
        }
    }
}
