package com.example.mr_zyl.project.pro.mine.view.navigation;

import android.content.Context;
import android.view.ViewGroup;

import com.example.mr_zyl.project.R;
import com.example.mr_zyl.project.pro.base.view.navigation.NavigationBuilderAdapter;

/**
 * Created by Mr_Zyl on 2016/9/19.
 */
public class MineNavigationBuilder extends NavigationBuilderAdapter {
    public MineNavigationBuilder(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.toolbar_mine_layout;
    }
    @Override
    public void createAndBind(ViewGroup parent) {
        super.createAndBind(parent);
        setImageViewStyle(R.id.iv_mine_left,getLeftIconRes(),getLeftIconOnClickListener());
        setTextViewStyle(R.id.tv_mine_title);
        setImageViewStyle(R.id.iv_mine_right,getRightIconRes(),getRightIconOnClickListener());
    }
}
