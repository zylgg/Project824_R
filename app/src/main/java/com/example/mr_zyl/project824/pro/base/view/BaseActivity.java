package com.example.mr_zyl.project824.pro.base.view;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.WindowManager;

import com.example.mr_zyl.project824.BaseApplication;
import com.example.mr_zyl.project824.mvp.presenter.impl.MvpBasePresenter;
import com.example.mr_zyl.project824.mvp.view.impl.MvpActivity;
import com.example.mr_zyl.project824.utils.NetUtils;

import butterknife.ButterKnife;

/**
 * BaseActivity是我们的项目的Activity
 * Created by Mr_Zyl on 2016/8/24.
 */
public abstract class BaseActivity<P extends MvpBasePresenter> extends MvpActivity<P> implements NetBroadcastReceiver.NetEvevt {

    public static NetBroadcastReceiver.NetEvevt netEvevt;
    /**
     * 网络类型
     */
    private int netMobile;

    public BaseApplication getMyApplication() {
        return (BaseApplication) this.getApplication();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        netEvevt = this;
        initNetType();
        //设置布局
        setContentView(initLayoutId());
        //记录当前的activity
        getMyApplication().addActivity(this);
        //初始化butterknife注解
        ButterKnife.bind(this);
    }

    private boolean initNetType() {
        this.netMobile = NetUtils.getNetWorkState(this);
        return isNetConnect(this.netMobile);
    }

    protected abstract int initLayoutId();

    @Override
    protected void onResume() {
        /**
         * 设置为横屏
         * android:configChanges="orientation|screenSize" 切屏不重走oncreate（）方法
         */
        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
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
    public void onNetChange(int netMobile) {
        this.netMobile=netMobile;
//        isNetConnect(this.netMobile);
    }

    /**
     * 判断有无网络 。
     *
     * @return true 有网, false 没有网络.
     */
    public boolean isNetConnect(int state) {
        if (state == 1) {//wifi
            return true;
        } else if (state == 0) {//mobile
            return true;
        } else if (state == -1) {//没网
            return false;
        }
        return false;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        getMyApplication().removeActivity(this);
    }

}
