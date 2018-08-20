package com.example.mr_zyl.project824.pro.newpost.natvigation;

import android.content.Context;
import android.view.ViewGroup;

import com.example.mr_zyl.project824.R;
import com.example.mr_zyl.project824.pro.base.view.navigation.NavigationBuilderAdapter;

/**
 * Created by TFHR02 on 2017/6/22.
 */
public class NewpostNavigationBuilder extends NavigationBuilderAdapter{
    public NewpostNavigationBuilder(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.toolbar_default_layout;
    }

    @Override
    public void createAndBind(ViewGroup parent) {
        super.createAndBind(parent);
        setTextViewStyle(R.id.tv_default_title);
    }
}
