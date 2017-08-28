package com.example.mr_zyl.project.pro.mine.view.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.mr_zyl.project.R;
import com.example.mr_zyl.project.pro.mine.Adapter.BlurredAdapter;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;
import java.util.List;

public class BlurredActivity extends AppCompatActivity {

//    private BlurredView mBlurredView;

    private RecyclerView lv;
    private Toolbar toolbar;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private FloatingActionButton fab_blurred;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blurred);
        // 初始化视图
        initViews();
        initlistener();
        initdata();
    }

    boolean listview_state;

    /**
     * 初始化视图
     */
    private void initViews() {
        fab_blurred = (FloatingActionButton) findViewById(R.id.fab_blurred);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {//加个返回按键
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
//        mBlurredView = (BlurredView) findViewById(R.id.activity_main_blurredview);
        fab_blurred = (FloatingActionButton) findViewById(R.id.fab_blurred);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbarLayout);
        lv = (RecyclerView) findViewById(R.id.lv);
        lv.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initlistener() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        fab_blurred.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "你点击了！", Snackbar.LENGTH_LONG)
                        .setAction("确定", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        })
                        .show();
            }
        });
//        lv.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int scrollState) {
//                super.onScrollStateChanged(recyclerView, scrollState);
//                if (scrollState == 1 || scrollState == 2) {
//                    listview_state = true;
//                } else {
//                    listview_state = false;
//                }
//            }
//
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                if (listview_state) {
//                    if (dy>100){
//                        dy=100;
//                    }
//                    //设置图片上移的距离
//                    mBlurredView.setBlurredTop(dy);
//                    //设置模糊程度
//                    mBlurredView.setBlurredLevel(dy);
//                }
//            }
//        });
    }
//    AbsListView.OnScrollListener scrollint= new AbsListView.OnScrollListener() {
//        @Override
//        public void onScrollStateChanged(AbsListView view, int scrollState) {
//            //SCROLL_STATE_IDLE是当屏幕停止滚动时
//            //是当用户在以触屏方式滚动屏幕并且手指仍然还在屏幕上时 ||当用户由于之前划动屏幕并抬起手指，屏产生惯性滑动时
//            if (scrollState == SCROLL_STATE_TOUCH_SCROLL || scrollState == SCROLL_STATE_FLING) {
//                listview_state = true;
//            } else {
//                listview_state = false;
//            }
//        }
//
//        @Override
//        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//            if (listview_state) {
//                //设置图片上移的距离
//                mBlurredView.setBlurredTop((firstVisibleItem + 1) * 10);
//                //设置模糊程度
//                mBlurredView.setBlurredLevel((firstVisibleItem + 1) * 10);
//            }
//        }
//    };

    private void initdata() {
        collapsingToolbarLayout.setTitle("滚动toolbar");
        collapsingToolbarLayout.setExpandedTitleColor(Color.WHITE); // 设置还没收缩时状态下字体颜色
        collapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE); // 设置收缩后Toolbar上字体的颜色

        List<String> lists = new ArrayList<String>();
        for (int i = 0; i < 20; i++) {
            lists.add(i + "");
        }
        lv.setAdapter(new BlurredAdapter(this, lists));
        Picasso.with(this).load(R.drawable.resource_icon)
                .transform(new CircleTransform())
                .error(R.drawable.transparent_corner_bg).placeholder(R.drawable.transparent_corner_bg)
                .into(fab_blurred);
    }

    /**
     * 圆角转换
     */
    class CircleTransform implements Transformation {

        @Override
        public Bitmap transform(Bitmap source) {
            int size = Math.min(source.getWidth(), source.getHeight());

            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;

            Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, size, size);
            if (squaredBitmap != source) {
                source.recycle();
            }

            Bitmap bitmap = Bitmap.createBitmap(size, size, source.getConfig());

            Canvas canvas = new Canvas(bitmap);
            Paint paint = new Paint();
            BitmapShader shader = new BitmapShader(squaredBitmap,
                    BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
            paint.setShader(shader);
            paint.setAntiAlias(true);

            float r = size / 2f;
            canvas.drawCircle(r, r, r, paint);

            squaredBitmap.recycle();
            return bitmap;
        }

        @Override
        public String key() {
            return "circle";
        }

    }
}
