package com.example.mr_zyl.project.pro.mine.view.activity;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mr_zyl.project.R;
import com.example.mr_zyl.project.pro.base.view.BaseActivity;
import com.example.mr_zyl.project.pro.mine.Adapter.BlurredAdapter;
import com.example.mr_zyl.project.pro.mine.view.selfview.MySnackbarBar;
import com.example.mr_zyl.project.pro.mine.view.selfview.SlideView;
import com.example.mr_zyl.project.utils.SnackbarUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class BlurredActivity extends BaseActivity {
    @BindView(R.id.iv_blurred_bg)
    ImageView iv_blurred_bg;

    @BindView(R.id.rv_blurred_list)
    RecyclerView rv_blurred_list;

    @BindView(R.id.tb_blurred_navigation)
    Toolbar tb_blurred_navigation;

    @BindView(R.id.civ_blurred_head)
    CircleImageView civ_blurred_head;

    @BindView(R.id.slv_image_title)
    SlideView slv_image_title;

    @BindView(R.id.cl_blurred)
    CoordinatorLayout cl_blurred;

    @BindView(R.id.iv_blurred_title)
    TextView iv_blurred_title;

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
        setSupportActionBar(tb_blurred_navigation);
        if (getSupportActionBar() != null) {//加个返回按键
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        rv_blurred_list.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initlistener() {
        slv_image_title.setOnRatiolistener(new SlideView.onRatioChangedListener() {
            @Override
            public void onRatioChanged(float expandedPercentage) {
                iv_blurred_bg.setAlpha((int) (255 - (100 * expandedPercentage) * 2.55*0.5));
            }
        });
        tb_blurred_navigation.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @OnClick({R.id.civ_blurred_head, R.id.iv_blurred_title})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.civ_blurred_head:
                SnackbarUtils.Long(cl_blurred, "点击了头像！")
                        .gravityCoordinatorLayout(Gravity.TOP).paddingTop(100)
                        .backColor(getResources().getColor(R.color.green))
                        .show();
                break;
            case R.id.iv_blurred_title:
                new MySnackbarBar.Builder(BlurredActivity.this)
                        .setMessage("点击了标题！")
                        .setLayoutGravity(Gravity.BOTTOM)
                        .show();
                break;
        }
    }

    private void initdata() {
        List<String> lists = new ArrayList<String>();
        for (int i = 0; i < 20; i++) {
            lists.add(i + "");
        }
        rv_blurred_list.setAdapter(new BlurredAdapter(this, lists));
        Picasso.with(this).load(R.drawable.resource_icon)
                .error(R.drawable.transparent_corner_bg).placeholder(R.drawable.transparent_corner_bg)
                .into(civ_blurred_head);
    }
}
