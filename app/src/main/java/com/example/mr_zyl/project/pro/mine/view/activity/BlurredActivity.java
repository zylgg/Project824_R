package com.example.mr_zyl.project.pro.mine.view.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AbsListView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.mr_zyl.project.R;
import com.example.mr_zyl.project.pro.mine.Adapter.BlurredAdapter;
import com.example.mr_zyl.project.pro.mine.view.selfview.MyListView;
import com.qiushui.blurredview.BlurredView;

import java.util.ArrayList;
import java.util.List;

public class BlurredActivity extends AppCompatActivity {
    /**
     * 进度条SeekBar
     */
    private SeekBar mSeekBar;

    /**
     * 显示进度的文字
     */
    private TextView mProgressTv;

    /**
     * Blurredview
     */
    private BlurredView mBlurredView;

    private MyListView lv;
    private Toolbar toolbar;
    private CollapsingToolbarLayout collapsingToolbarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blurred);

        // 初始化视图
        initViews();

        // 处理seekbar滑动事件
        setSeekBar();
    }


    boolean listview_state;
    /**
     * 初始化视图
     */
    private void initViews() {
        toolbar= (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {//加个返回按键
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbarLayout);
        collapsingToolbarLayout.setTitle("滚动toolbar");
        collapsingToolbarLayout.setExpandedTitleColor(Color.WHITE); // 设置还没收缩时状态下字体颜色
        collapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE); // 设置收缩后Toolbar上字体的颜色
        mSeekBar = (SeekBar) findViewById(R.id.activity_main_seekbar);
        mProgressTv = (TextView) findViewById(R.id.activity_main_progress_tv);
        mBlurredView = (BlurredView) findViewById(R.id.activity_main_blurredview);
        lv = (MyListView) findViewById(R.id.lv);
        // 可以在代码中使用setBlurredImg()方法指定需要模糊的图片
//        mBlurredView.setBlurredImg(getResources().getDrawable(R.mipmap.bg_2));
        List<String> lists = new ArrayList<String>();
        for (int i = 0; i < 20; i++) {
            lists.add(i + "");
        }
        lv.setAdapter(new BlurredAdapter(this, lists));
        lv.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                //SCROLL_STATE_IDLE是当屏幕停止滚动时
                //是当用户在以触屏方式滚动屏幕并且手指仍然还在屏幕上时 ||当用户由于之前划动屏幕并抬起手指，屏产生惯性滑动时
               if (scrollState==SCROLL_STATE_TOUCH_SCROLL||scrollState==SCROLL_STATE_FLING){
                   listview_state=true;
               }else {
                   listview_state=false;
               };
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (listview_state){
                    //设置图片上移的距离
                    mBlurredView.setBlurredTop((firstVisibleItem + 1) * 10);
                    //设置模糊程度
                    mBlurredView.setBlurredLevel((firstVisibleItem + 1) * 10);
                }
            }
        });
    }

    /**
     * 处理seekbar滑动事件
     */
    private void setSeekBar() {
        mSeekBar.setMax(100);
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mBlurredView.setBlurredLevel(progress);
                mProgressTv.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
}
