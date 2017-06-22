package com.example.mr_zyl.project.pro.attention.view;

import android.view.View;
import android.view.ViewGroup;

import com.example.mr_zyl.project.R;
import com.example.mr_zyl.project.pro.base.view.BaseFragment;
import com.example.mr_zyl.project.pro.newpost.natvigation.NewpostNavigationBuilder;

/**
 * Created by Mr_Zyl on 2016/8/25.
 */
public class Attention extends BaseFragment {
    @Override
    public int getContentView() {
        return R.layout.attention;
    }

    @Override
    public void initContentView(View viewContent) {
        initToolBar(viewContent);
    }
    private void initToolBar(View viewContent) {
        NewpostNavigationBuilder builder = new NewpostNavigationBuilder(Fcontext);
        builder.setTitle("关注")
                .setBackground(R.drawable.toolbar_background_attention_shape)
                .createAndBind((ViewGroup) viewContent);
    }
}
