package com.example.mr_zyl.project.mvp.view.impl;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.example.mr_zyl.project.mvp.presenter.impl.MvpBasePresenter;
import com.example.mr_zyl.project.mvp.view.MvpView;

/**
 * 将我们的MVP架构集成到我们的Activity
 * Created by Mr_Zyl on 2016/8/24.
 */
public abstract class MvpActivity<P extends MvpBasePresenter> extends AppCompatActivity implements MvpView {

    //MVP中的P是动态的的
    protected P presenter;


    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        presenter = bindPresenter();
        //我们的presenter中介我是来管理view的--关联
        if (presenter != null) {
            presenter.attachView(this);
        }
    }
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

    //具体不知道是谁，我给别人实现
    public abstract P bindPresenter();

    //当view不再需要present（即，用户不再需要通过中介找房子了）就要解除绑定
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (presenter != null) {
            presenter.detachView();
        }
    }
}
