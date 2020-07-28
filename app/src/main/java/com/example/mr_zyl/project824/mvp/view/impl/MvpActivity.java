package com.example.mr_zyl.project824.mvp.view.impl;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;

import com.example.mr_zyl.project824.mvp.presenter.impl.MvpBasePresenter;
import com.example.mr_zyl.project824.mvp.view.MvpView;

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
