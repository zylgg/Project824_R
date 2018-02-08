package com.example.mr_zyl.project.pro.essence.view.navigation;

import android.content.Context;
import android.view.ViewGroup;

import com.example.mr_zyl.project.R;
import com.example.mr_zyl.project.pro.base.view.navigation.NavigationBuilderAdapter;


public class EssenceNavigationBuilder extends NavigationBuilderAdapter {

    public EssenceNavigationBuilder(Context context){
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.toolbar_essence_layout;
    }

    @Override
    public void createAndBind(ViewGroup parent) {
        super.createAndBind(parent);
        setImageViewStyle(R.id.iv_essence_left,getLeftIconRes(),getLeftIconOnClickListener());
        setTextViewStyle(R.id.tv_essence_title);
        setImageViewStyle(R.id.iv_essence_right,getRightIconRes(),getRightIconOnClickListener());
    }
}
