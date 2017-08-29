package com.example.mr_zyl.project.pro.base.view;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.example.mr_zyl.project.BaseApplication;
import com.example.mr_zyl.project.mvp.presenter.impl.MvpBasePresenter;
import com.example.mr_zyl.project.mvp.view.impl.MvpActivity;

/**
 * BaseActivity是我们的项目的Activity
 * Created by Mr_Zyl on 2016/8/24.
 */
public abstract class BaseActivity<P extends  MvpBasePresenter>  extends MvpActivity<P>{

    public BaseApplication getMyApplication() {
        return (BaseApplication) this.getApplication();
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //记录当前的activity
        getMyApplication().addActivity(this);
    }

    @Override
    public P bindPresenter() {
        return null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getMyApplication().removeActivity(this);
    }
}
