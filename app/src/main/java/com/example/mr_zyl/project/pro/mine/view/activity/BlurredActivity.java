package com.example.mr_zyl.project.pro.mine.view.activity;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.example.mr_zyl.project.R;
import com.example.mr_zyl.project.pro.base.view.BaseActivity;
import com.example.mr_zyl.project.pro.mine.Adapter.BlurredAdapter;
import com.example.mr_zyl.project.pro.mine.view.selfview.SlideView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;

public class BlurredActivity extends BaseActivity {
    @BindView(R.id.imageView_bg)
    ImageView imageView_bg;
    @BindView(R.id.lv)
    RecyclerView lv;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.iv_blurred)
    CircleImageView iv_blurred;
    @BindView(R.id.slv_image_title)
    SlideView slv_image_title;

    @Override
    protected int initLayoutId() {
        return R.layout.activity_blurred;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 初始化视图
        initViews();
        initlistener();
        initdata();
    }

    /**
     * 初始化视图
     */
    private void initViews() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {//加个返回按键
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }


        lv.setLayoutManager(new LinearLayoutManager(this));

        slv_image_title.setOnRatiolistener(new SlideView.onRatioChangedListener() {
            @Override
            public void onRatioChanged(float expandedPercentage) {
                imageView_bg.setAlpha(255 - (int) (100 * expandedPercentage) * 2.55f * 0.5f);
            }
        });
    }

    private void initlistener() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        iv_blurred.setOnClickListener(new View.OnClickListener() {
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
    }

    private void initdata() {
        List<String> lists = new ArrayList<String>();
        for (int i = 0; i < 20; i++) {
            lists.add(i + "");
        }
        lv.setAdapter(new BlurredAdapter(this, lists));
        Picasso.with(this).load(R.drawable.resource_icon)
                .error(R.drawable.transparent_corner_bg).placeholder(R.drawable.transparent_corner_bg)
                .into(iv_blurred);
    }

}
