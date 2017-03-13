package com.example.mr_zyl.project.pro.newpost.view.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mr_zyl.project.R;

public class TestMeasureActivity extends AppCompatActivity {

    TextView tv_newpost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_measure);

        ImageView iv_newpost = (ImageView) findViewById(R.id.iv_newpost);

        tv_newpost = (TextView) findViewById(R.id.tv_newpost);

    }

    @Override
    protected void onResume() {
        super.onResume();

        tv_newpost.measure(0,0);
        Log.i("width", "" + tv_newpost.getMeasuredWidth());
        Log.i("height", "" + tv_newpost.getMeasuredHeight());
    }
}
