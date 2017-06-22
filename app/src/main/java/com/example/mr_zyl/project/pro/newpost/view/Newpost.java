package com.example.mr_zyl.project.pro.newpost.view;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mr_zyl.project.R;
import com.example.mr_zyl.project.pro.base.view.BaseFragment;
import com.example.mr_zyl.project.pro.base.view.item.DefaultImpleItemBuilder;
import com.example.mr_zyl.project.pro.newpost.natvigation.NewpostNavigationBuilder;
import com.example.mr_zyl.project.pro.newpost.view.Activity.TestMeasureActivity;

/**
 * Created by Mr_Zyl on 2016/8/25.
 */
public class Newpost extends BaseFragment {
    TextView tv_newpost;

    @Override
    public int getContentView() {
        return R.layout.newpost;
    }

    @Override
    public void initContentView(View viewContent) {
        initToolBar(viewContent);
        DefaultImpleItemBuilder builder = new DefaultImpleItemBuilder(Fcontext);
        builder.setLeftIcons(R.drawable.login_unlogin_header)
                .setTitleText("测试页面")
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(getActivity(), TestMeasureActivity.class));
                    }
                }).BindParentView((ViewGroup) viewContent);
    }

    private void initToolBar(View viewContent) {
        NewpostNavigationBuilder builder = new NewpostNavigationBuilder(Fcontext);
        builder.setTitle("新项")
                .setBackground(R.drawable.toolbar_background_newpost_shape)
                .createAndBind((ViewGroup) viewContent);
    }
}
