package com.example.mr_zyl.project824.pro.base.view;

import android.os.Bundle;
import android.os.Environment;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mr_zyl.project824.R;
import com.example.mr_zyl.project824.mvp.presenter.impl.MvpBasePresenter;
import com.example.mr_zyl.project824.mvp.view.impl.MvpFragment;
import com.example.mr_zyl.project824.pro.essence.view.selfview.MyProgressDialog;
import com.example.mr_zyl.project824.utils.ToastUtil;

/**
 * BaseFragment是我们的项目的Fragment
 * Created by Mr_Zyl on 2016/8/24.
 */
public abstract class BaseFragment<P extends MvpBasePresenter> extends MvpFragment<P> {
    //我们自己的Fragment需要缓存试图
    private View viewContent;
    private boolean isinit;
    public AppCompatActivity Fcontext;
    public MyProgressDialog mProgressDialog, mProgressDialog_HORIZONTAL;
    public static String FilePath=Environment.getExternalStorageDirectory()+"/"+ R.string.app_name;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fcontext= (AppCompatActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (viewContent == null) {
            viewContent = inflater.inflate(getContentView(), container, false);
            initContentView(viewContent);
        }
        //判断我们的Fragment是否存在这个视图
        ViewGroup parent = (ViewGroup) viewContent.getParent();
        if (parent != null) {
            //如果存在，那么就移除掉，重新添加，这样的方式我们就可以缓存视图
            parent.removeView(viewContent);
        }
        return viewContent;
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!isinit){
            this.isinit=true;
            initData();
        }

    }

    @Override
    public void onResume() {//若fragmentstatepageradapter设置了BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT 才可用；否则用上面方法
        super.onResume();
//        if (!isinit){
//            this.isinit=true;
//            initData();
//        }
    }

    @Override
    public P bindPresenter() {
        return null;
    }

    public abstract int getContentView();

    public abstract void initContentView(View viewContent);


    public void initData() {

    }

    /**
     * 显示加载进度dialog
     *
     * @param message
     */
    protected void showProgressDialog(String message, int Style) {
        if (null == mProgressDialog_HORIZONTAL) {
            mProgressDialog_HORIZONTAL = new MyProgressDialog(getContext(), 0, message);
            mProgressDialog_HORIZONTAL.setProgressStyle(Style);
            mProgressDialog_HORIZONTAL.setProgressNumberFormat("%1s m/%2s m");
            mProgressDialog_HORIZONTAL.setMax(100);
        }
        mProgressDialog_HORIZONTAL.setCanceledOnTouchOutside(false);
        mProgressDialog_HORIZONTAL.setCancelable(false);
        mProgressDialog_HORIZONTAL.show();
    }
    /**
     * 设置进度值
     * @param value
     */
    public void setHorizontalProgressValue(int max,int value){
        if (null != mProgressDialog_HORIZONTAL) {
            mProgressDialog_HORIZONTAL.setMax(max);
            mProgressDialog_HORIZONTAL.setProgress(value);
        }
    }

    /**
     * 显示加载进度dialog
     *
     * @param message
     */
    protected void showProgressDialog(String message) {
        if (null == mProgressDialog) {
            mProgressDialog = new MyProgressDialog(getContext(), 0, message);
        }
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
    }

    /**
     * 关闭进度dialog
     */
    protected void dismissProgressDialog() {
        if (null != mProgressDialog) {
            mProgressDialog.dismiss();
        }
        if (null!=mProgressDialog_HORIZONTAL){
            mProgressDialog_HORIZONTAL.dismiss();
        }
    }

    /**
     * 吐丝dialog
     *
     * @param message
     */
    public void showToast(String message) {
        ToastUtil.showToast(getContext(),message);
    }

}
