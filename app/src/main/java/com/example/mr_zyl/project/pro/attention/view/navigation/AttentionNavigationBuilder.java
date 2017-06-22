package com.example.mr_zyl.project.pro.attention.view.navigation;

import android.content.Context;

import com.example.mr_zyl.project.R;
import com.example.mr_zyl.project.pro.base.view.navigation.NavigationBuilderAdapter;

/**
 * Created by TFHR02 on 2017/6/22.
 */
public class AttentionNavigationBuilder extends NavigationBuilderAdapter {
    public AttentionNavigationBuilder(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.toolbar_default_layout;
    }
}
