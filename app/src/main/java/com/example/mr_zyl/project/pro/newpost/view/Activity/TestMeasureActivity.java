package com.example.mr_zyl.project.pro.newpost.view.Activity;

import android.content.Context;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mr_zyl.project.R;
import com.example.mr_zyl.project.pro.base.view.BaseActivity;

public class TestMeasureActivity extends BaseActivity {

    private static final String TAG = "TestMeasureActivity";
    TextView tv_newpost;
    ImageView mImageView;
    private static final String guanxi_url = "http://wimg.spriteapp.cn/ugc/2017/10/14/59e22bc05c765_1.jpg";

    private Context context;

    @Override
    protected int initLayoutId() {
        return R.layout.activity_test_measure;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        tv_newpost = (TextView) findViewById(R.id.tv_newpost);
        mImageView = (ImageView) findViewById(R.id.id_imageview);
    }

}
