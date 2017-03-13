package com.example.mr_zyl.project.pro.mine.view.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.mr_zyl.project.R;
import com.example.mr_zyl.project.pro.mine.Adapter.BlurredAdapter;
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

    private ListView lv;

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
        mSeekBar = (SeekBar) findViewById(R.id.activity_main_seekbar);
        mProgressTv = (TextView) findViewById(R.id.activity_main_progress_tv);
        mBlurredView = (BlurredView) findViewById(R.id.activity_main_blurredview);
        lv = (ListView) findViewById(R.id.lv);

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
               if (scrollState==SCROLL_STATE_TOUCH_SCROLL||scrollState==SCROLL_STATE_FLING){
                   listview_state=true;
               }else {
                   listview_state=false;
               };
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (listview_state){
                    mBlurredView.setBlurredTop((firstVisibleItem + 1) * 10);
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
