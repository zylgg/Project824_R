package com.example.mr_zyl.project824.pro.mine.view.activity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.core.graphics.ColorUtils;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mr_zyl.project824.R;
import com.example.mr_zyl.project824.pro.base.view.BaseActivity;
import com.example.mr_zyl.project824.pro.mine.view.fragment.BlurredTabfragment;
import com.example.mr_zyl.project824.pro.mine.view.selfview.MySnackBarUtils;
import com.example.mr_zyl.project824.pro.mine.view.selfview.SlideView;
import com.example.mr_zyl.project824.utils.StatusBarUtils;
import com.example.mr_zyl.project824.utils.ToastUtil;

import butterknife.BindView;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
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
    Window mwindow;
    public enum ShopAppBarLayoutOpenStatus {
        wholeOpen, other,wholeClose;
    }
    @Override
    protected int initLayoutId() {
        return R.layout.activity_blurred;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtils.setTransparent(this);
        // 初始化视图
        mwindow=getWindow();
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
                if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
                    mwindow.setStatusBarColor(ColorUtils.blendARGB(Color.TRANSPARENT,getResources().getColor(R.color.white),expandedPercentage));
//                    mwindow.setNavigationBarColor(ColorUtils.blendARGB(Color.TRANSPARENT,Color.WHITE,expandedPercentage));


                    if (expandedPercentage==1){
                        EventBus.getDefault().post(ShopAppBarLayoutOpenStatus.wholeClose);
                        mwindow.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                    }else if (expandedPercentage==0){
                        EventBus.getDefault().post(ShopAppBarLayoutOpenStatus.wholeOpen);
                        mwindow.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                    }else{
                        EventBus.getDefault().post(ShopAppBarLayoutOpenStatus.other);
                        mwindow.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                    }
                }
                tb_blurred_navigation.setBackgroundColor(ColorUtils.blendARGB(Color.TRANSPARENT,Color.WHITE,expandedPercentage));
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
                ToastUtil.showToast(this,"点击了头像！");
                break;
            case R.id.iv_blurred_title:
                new MySnackBarUtils.Builder(BlurredActivity.this)
                        .setCoverStatusBar(true)
                        .setLayoutGravity(Gravity.TOP)
                        .setIcon(R.mipmap.ic_launcher)
                        .setMessage("点击了标题！")
                        .setMessageColor(R.color.default_bg_color)
                        .setBackgroundColor(R.color.white)
                        .show();
                break;
        }
    }

    private void initdata() {
        civ_blurred_head.setImageResource(R.drawable.resource_icon);
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

//        @Override
//        public CharSequence getPageTitle(int position) {
//            return "Custom-" + position;
//        }
    }
}
