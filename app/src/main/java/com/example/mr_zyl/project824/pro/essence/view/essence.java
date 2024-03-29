package com.example.mr_zyl.project824.pro.essence.view;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;

import androidx.annotation.NonNull;

import com.example.mr_zyl.project824.pro.essence.view.adapter.EssenceAdapter2;
import com.example.mr_zyl.project824.pro.essence.view.adapter.EssenceAdapter3;
import com.google.android.material.tabs.TabLayout;

import androidx.core.graphics.ColorUtils;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.text.format.Formatter;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.mr_zyl.project824.R;
import com.example.mr_zyl.project824.pro.base.view.BaseFragment;
import com.example.mr_zyl.project824.pro.base.view.residemenu.EventEntity.ResideTouch;
import com.example.mr_zyl.project824.pro.essence.OnVisibilityTitleListener;
import com.example.mr_zyl.project824.pro.essence.view.adapter.EssenceAdapter;
import com.example.mr_zyl.project824.pro.essence.view.navigation.EssenceNavigationBuilder;
import com.example.mr_zyl.project824.utils.ToastUtil;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import me.xiaopan.sketch.Configuration;
import me.xiaopan.sketch.Sketch;
import me.xiaopan.sketch.cache.BitmapPool;
import me.xiaopan.sketch.cache.DiskCache;
import me.xiaopan.sketch.cache.MemoryCache;

/**
 * Created by Mr_Zyl on 2016/8/25.
 */
public class essence extends BaseFragment implements OnVisibilityTitleListener {

    private static final String TAG = "essence";

    @BindView(R.id.ll_essence_title)
    LinearLayout ll_essence_title;
    @BindView(R.id.ll_essence_tabcontainer)
    LinearLayout ll_essence_tabcontainer;
    @BindView(R.id.tab_essence)
    TabLayout tab_essence;
    @BindView(R.id.vp_essence)
    ViewPager2 vp_essence;
    private EssenceNavigationBuilder builder;

    @Override
    public int getContentView() {
        return R.layout.essence;
    }

    @Override
    public void initContentView(View viewContent) {
        ButterKnife.bind(this, viewContent);
        initToolBar(ll_essence_tabcontainer);
    }

    private void initToolBar(View viewContent) {
        builder = new EssenceNavigationBuilder(getContext());
        builder.setTitle(R.string.main_essence_text)
                .setBackground(R.color.colorAccent)
                .setLeftIcon(R.drawable.ic_head)
                .setRightIcon(R.drawable.ic_cleancache)
                .setLeftIconOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ResideTouch touch = new ResideTouch();
                        touch.setHandleType(ResideTouch.HandleTypeTagToggle);
                        EventBus.getDefault().post(touch);

                        //stationbuy.ejoy.sinopec.com://?stnCode
                        //activity.ejoy.sinopec.com://?openWalletRecharge=1   scheme1://host1:8080/path1?query1=1&query2=true
//                        Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse("activity.ejoy.sinopec.com://?openWalletRecharge=1"));
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("stationbuy.ejoy.sinopec.com://?stnCode=32550160"));
                        try {
                            startActivity(intent);
                        }catch (Exception e){
                            ToastUtil.showToast(getActivity(),"应用找不到！");
                        }

                    }
                })
                .setRightIconOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ClearMemoryCache();
                    }
                }).createAndBind((ViewGroup) viewContent);
    }

    public static final String TAB_TAG = "@zylmove@";

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onStart() {
        super.onStart();
//        this.vp_essence.getAdapter().notifyItemChanged(this.vp_essence.getCurrentItem());
    }

    @Override
    public void initData() {
        String[] titles = getResources().getStringArray(R.array.essence_video_tab);
        List<String> strings = Arrays.asList(titles);
        EssenceAdapter3 adapter = new EssenceAdapter3(getActivity(),strings , this);
        this.vp_essence.setAdapter(adapter);
        this.vp_essence.setSaveEnabled(false);
        this.vp_essence.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                ResideTouch resideTouch = new ResideTouch(position == 0 ? true : false, ResideTouch.HandleTypeTagLeftBorder);
                EventBus.getDefault().post(resideTouch);
            }
        });

        new TabLayoutMediator(this.tab_essence, this.vp_essence, true, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
            }
        }).attach();

        for (int i = 0; i < titles.length; i++) {
            String title = titles[i];
            TabLayout.Tab tabAt = this.tab_essence.getTabAt(i);
            View tab_view = LayoutInflater.from(Fcontext).inflate(R.layout.essence_tablayout_item_layout, null);
            TextView tv_essence_tablayout_item_text = (TextView) tab_view.findViewById(R.id.tv_essence_tablayout_item_text);
            String[] split = title.split(TAB_TAG);
            String text_content = split[0];
            tv_essence_tablayout_item_text.setText(text_content);
            tabAt.setCustomView(tab_view);
        }

        int selectedTabPosition = this.tab_essence.getSelectedTabPosition();
        TextView text = (TextView) this.tab_essence.getTabAt(selectedTabPosition).getCustomView().findViewById(R.id.tv_essence_tablayout_item_text);
        text.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        text.setTextColor(Color.WHITE);
        this.tab_essence.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                TextView text = (TextView) tab.getCustomView().findViewById(R.id.tv_essence_tablayout_item_text);
                text.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                text.setTextColor(Color.WHITE);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                TextView text = (TextView) tab.getCustomView().findViewById(R.id.tv_essence_tablayout_item_text);
                text.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                text.setTextColor(Fcontext.getResources().getColor(R.color.essence_tab_text_color_normal));
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    /**
     * 清理磁盘缓存
     */
    private void ClearMemoryCache() {
        Configuration configuration = Sketch.with(getContext()).getConfiguration();
        //磁盘
        DiskCache diskCache = configuration.getDiskCache();
        long usedSizeFormat = diskCache.getSize();
        long maxSizeFormat = diskCache.getMaxSize();
        //内存
        MemoryCache memoryCache = configuration.getMemoryCache();
        long memoryusedSizeFormat = memoryCache.getSize();
        long memorymaxSizeFormat = memoryCache.getMaxSize();
        //BitMapPool
        BitmapPool bitmapPool = configuration.getBitmapPool();
        long poolusedSizeFormat = bitmapPool.getSize();
        long poolmaxSizeFormat = bitmapPool.getMaxSize();

        //总使用内存
        String all_usercatch = Formatter.formatFileSize(getContext(), usedSizeFormat + memoryusedSizeFormat + poolusedSizeFormat);
        //总内存
        String all_catch = Formatter.formatFileSize(getContext(), maxSizeFormat + memorymaxSizeFormat + poolmaxSizeFormat);

        MaterialDialog dialog = new MaterialDialog.Builder(getActivity())
                .title("清理图片缓存")
                .content("已使用：" + all_usercatch)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        new AsyncTask<Integer, Integer, Integer>() {
                            @Override
                            protected void onPreExecute() {
                                showProgressDialog("清理中...");
                            }

                            @Override
                            protected Integer doInBackground(Integer... params) {
                                Sketch.with(getContext()).getConfiguration().getDiskCache().clear();
                                Sketch.with(getContext()).getConfiguration().getMemoryCache().clear();
                                Sketch.with(getContext()).getConfiguration().getBitmapPool().clear();
                                return null;
                            }

                            @Override
                            protected void onPostExecute(Integer integer) {
                                dismissProgressDialog();
                                ToastUtil.showToast(getContext(), "清理完成！");
                            }
                        }.execute();
                    }
                })
                .positiveText("清理磁盘")
                .build();
        dialog.show();
    }

    @Override
    public void hide() {
        if (ll_essence_title.getTranslationY() == 0)
            createTranslate(false, builder.getTitleMeasureHeigth());
    }

    @Override
    public void open() {
        if (ll_essence_title.getTranslationY() == (-builder.getTitleMeasureHeigth()))
            createTranslate(true, builder.getTitleMeasureHeigth());
    }

    private TextView tv_essence_title;
    private ImageView iv_essence_left, iv_essence_right;

    private void createTranslate(final boolean is_open, final int scroll_max_height) {
        final int colorAccent = getContext().getResources().getColor(R.color.colorAccent);
        tv_essence_title = builder.getContentView().findViewById(R.id.tv_essence_title);

        iv_essence_left = builder.getContentView().findViewById(R.id.iv_essence_left);
        iv_essence_right = builder.getContentView().findViewById(R.id.iv_essence_right);


        ValueAnimator animation = ValueAnimator.ofInt(0, scroll_max_height);
        animation.setDuration(250);
        animation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float animatedValue = animation.getAnimatedFraction();
                if (is_open) {//向下 打开
                    ll_essence_title.setTranslationY(scroll_max_height * (animatedValue - 1));
                    tv_essence_title.setTextColor(ColorUtils.blendARGB(colorAccent, Color.WHITE, animatedValue));
                    iv_essence_left.setAlpha(255 * animatedValue);
                    iv_essence_right.setAlpha(255 * animatedValue);

                    LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) vp_essence.getLayoutParams();
                    layoutParams.topMargin = (int) (scroll_max_height * (animatedValue - 1));
                    vp_essence.setLayoutParams(layoutParams);
                } else {//向上 隐藏
                    ll_essence_title.setTranslationY(-scroll_max_height * animatedValue);
                    tv_essence_title.setTextColor(ColorUtils.blendARGB(Color.WHITE, colorAccent, animatedValue));
                    iv_essence_left.setAlpha(255 * (1 - animatedValue));
                    iv_essence_right.setAlpha(255 * (1 - animatedValue));

                    LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) vp_essence.getLayoutParams();
                    layoutParams.topMargin = (int) (-scroll_max_height * animatedValue);
                    vp_essence.setLayoutParams(layoutParams);
                }

            }
        });
        animation.start();
    }

}
