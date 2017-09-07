package com.example.mr_zyl.project.pro.newpost.view.Activity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.mr_zyl.project.R;
import com.example.mr_zyl.project.pro.base.view.BaseActivity;

public class TestMeasureActivity extends BaseActivity {

    TextView tv_newpost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tv_newpost = (TextView) findViewById(R.id.tv_newpost);

    }

    @Override
    protected int initLayoutId() {
        return R.layout.activity_test_measure;
    }

}
