package com.example.mr_zyl.project824.pro.base.view;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.view.WindowManager;

import com.example.mr_zyl.project824.BaseApplication;
import com.example.mr_zyl.project824.R;
import com.example.mr_zyl.project824.mvp.presenter.impl.MvpBasePresenter;
import com.example.mr_zyl.project824.mvp.view.impl.MvpActivity;
import com.example.mr_zyl.project824.pro.mine.view.impl.OnActionClickListener;
import com.example.mr_zyl.project824.pro.mine.view.selfview.MySnackBarUtils;
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
        /**
         * 设置为横屏
         * android:configChanges="orientation|screenSize" 切屏不重走oncreate（）方法
         */
        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        //自动调节输入法区域
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

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

    protected abstract int initLayoutId();

    @Override
    protected void onStart() {
        super.onStart();
        //注册网络状态监听的广播
        registerBroadcastReceiver();

    }

    private NetBroadcastReceiver netBroadcastReceiver;

    /**
     * 注册网络状态广播
     */
    private void registerBroadcastReceiver() {
        //注册广播
        if (netBroadcastReceiver == null) {
            netBroadcastReceiver = new NetBroadcastReceiver();
            IntentFilter filter = new IntentFilter();
            filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
            registerReceiver(netBroadcastReceiver, filter);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public P bindPresenter() {
        return null;
    }

    @Override
    public void onNetChange(int netMobile) {
        this.netMobile = netMobile;
        boolean isHasNet = isNetConnect(this.netMobile);
        if (!isHasNet) {
            int actionColors=android.R.drawable.ic_menu_send;
            MySnackBarUtils.getBuilder(this)
                    .setBackgroundColor(R.color.white)
                    .setMessageColor(R.color.red)
                    .setMessage("网络不给力，请检查网络设置。")
                    .setActionWithIcon(actionColors, new OnActionClickListener() {
                        @Override
                        public void onClick() {
                            Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                            startActivity(intent);
                        }
                    })
                    .show();
        }
    }


    private boolean initNetType() {
        this.netMobile = NetUtils.getNetWorkState(this);
        return isNetConnect(this.netMobile);
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
        if (netBroadcastReceiver != null) {
            //注销监听网络的广播
            unregisterReceiver(netBroadcastReceiver);
        }
        getMyApplication().removeActivity(this);
    }

}
