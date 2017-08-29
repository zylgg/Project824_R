package com.example.mr_zyl.project.pro.base.view;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.WindowManager;

import com.example.mr_zyl.project.BaseApplication;
import com.example.mr_zyl.project.mvp.presenter.impl.MvpBasePresenter;
import com.example.mr_zyl.project.mvp.view.impl.MvpActivity;

import butterknife.ButterKnife;

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
        //设置布局
        setContentView(initLayoutId());
        //记录当前的activity
        getMyApplication().addActivity(this);
        //初始化butterknife注解
        ButterKnife.bind(this);
    }
    protected abstract int initLayoutId();
    @Override
    protected void onResume() {
        /**
         * 设置为横屏
         * android:configChanges="orientation|screenSize" 切屏不重走oncreate（）方法
         */
        if(getRequestedOrientation()!= ActivityInfo.SCREEN_ORIENTATION_PORTRAIT){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        //自动调节输入法区域
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        super.onResume();
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
