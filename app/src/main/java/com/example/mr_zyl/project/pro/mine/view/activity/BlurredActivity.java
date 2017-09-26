package com.example.mr_zyl.project.pro.mine.view.activity;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mr_zyl.project.R;
import com.example.mr_zyl.project.pro.base.view.BaseActivity;
import com.example.mr_zyl.project.pro.mine.view.fragment.BlurredTabfragment;
import com.example.mr_zyl.project.pro.mine.view.selfview.MySnackbarUtils;
import com.example.mr_zyl.project.pro.mine.view.selfview.SlideView;
import com.example.mr_zyl.project.utils.SnackbarUtils;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class BlurredActivity extends BaseActivity {
    @BindView(R.id.iv_blurred_bg)
    ImageView iv_blurred_bg;

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
    @BindView(R.id.vp_blurred_fragment)
    ViewPager vp_blurred_fragment;
    @BindView(R.id.tl_blurred_tablist)
    TabLayout tl_blurred_tablist;

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

    }

    private void initlistener() {
        slv_image_title.setOnRatiolistener(new SlideView.onRatioChangedListener() {
            @Override
            public void onRatioChanged(float expandedPercentage) {
                iv_blurred_bg.setAlpha((int) (255 - (100 * expandedPercentage) * 2.55 * 0.5));
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
                new MySnackbarUtils.Builder(BlurredActivity.this)
                        .setMessage("点击了标题！")
                        .setLayoutGravity(Gravity.TOP)
                        .show();
                break;
        }
    }

    private void initdata() {
        Picasso.with(this).load(R.drawable.resource_icon)
                .error(R.drawable.transparent_corner_bg).placeholder(R.drawable.transparent_corner_bg)
                .into(civ_blurred_head);
        vp_blurred_fragment.setAdapter(new VpPagerAdpater(getSupportFragmentManager()));
        tl_blurred_tablist.setupWithViewPager(vp_blurred_fragment);
        //自定义tablayout布局
        for (int i = 0; i < 4; i++) {
            TabLayout.Tab tabAt = tl_blurred_tablist.getTabAt(i);

            View view = LayoutInflater.from(this).inflate(R.layout.marketchecketsituation_tl_item, null, false);
            TextView tv_checksituationrecord_tabtext = (TextView) view.findViewById(R.id.tv_checksituationrecord_tabtext);
            tv_checksituationrecord_tabtext.setText("Custom-" + i);
            tabAt.setCustomView(view);
        }
        //选中第一个tab
        tl_blurred_tablist.getTabAt(tl_blurred_tablist.getSelectedTabPosition()).getCustomView().findViewById(R.id.tv_checksituationrecord_tabtext).setSelected(true);
        //设置选中监听器
        tl_blurred_tablist.addOnTabSelectedListener(new tabselectionlistener(vp_blurred_fragment));

    }

    /**
     * 自定义的tablayout的切换监听
     */
    class tabselectionlistener extends TabLayout.ViewPagerOnTabSelectedListener {

        public tabselectionlistener(ViewPager viewPager) {
            super(viewPager);
        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {
            super.onTabReselected(tab);
        }

        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            super.onTabSelected(tab);
            View tabview = tab.getCustomView();
            TextView tv = (TextView) tabview.findViewById(R.id.tv_checksituationrecord_tabtext);
            tv.setSelected(true);
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {
            super.onTabUnselected(tab);
            View tabview = tab.getCustomView();
            TextView tv = (TextView) tabview.findViewById(R.id.tv_checksituationrecord_tabtext);
            tv.setSelected(false);
        }
    }

    class VpPagerAdpater extends FragmentStatePagerAdapter {

        public VpPagerAdpater(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return BlurredTabfragment.getInstance(position);
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "page" + position;
        }
    }
}
