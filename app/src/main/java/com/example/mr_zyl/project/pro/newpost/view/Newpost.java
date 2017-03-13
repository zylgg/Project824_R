package com.example.mr_zyl.project.pro.newpost.view;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.example.mr_zyl.project.R;
import com.example.mr_zyl.project.pro.base.view.BaseFragment;
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
        tv_newpost= (TextView) viewContent.findViewById(R.id.tv_newpost) ;
        tv_newpost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fcontext.startActivity(new Intent(getActivity(), TestMeasureActivity.class));
            }
        });
    }

}
