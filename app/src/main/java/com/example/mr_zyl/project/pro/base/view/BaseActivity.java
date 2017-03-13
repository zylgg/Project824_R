package com.example.mr_zyl.project.pro.base.view;

import com.example.mr_zyl.project.mvp.presenter.impl.MvpBasePresenter;
import com.example.mr_zyl.project.mvp.view.impl.MvpActivity;

/**
 * BaseActivity是我们的项目的Activity
 * Created by Mr_Zyl on 2016/8/24.
 */
public abstract class BaseActivity<P extends  MvpBasePresenter>  extends MvpActivity<P>{

    @Override
    public P bindPresenter() {
        return null;
    }
}
