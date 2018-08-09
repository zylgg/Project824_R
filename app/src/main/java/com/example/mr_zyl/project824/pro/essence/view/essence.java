package com.example.mr_zyl.project824.pro.essence.view;

import android.animation.ValueAnimator;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.graphics.ColorUtils;
import android.support.v4.view.ViewPager;
import android.text.format.Formatter;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.mr_zyl.project824.R;
import com.example.mr_zyl.project824.pro.base.view.BaseFragment;
import com.example.mr_zyl.project824.pro.base.view.residemenu.ResideDispatch;
import com.example.mr_zyl.project824.pro.base.view.residemenu.ResideTouch;
import com.example.mr_zyl.project824.pro.essence.OnVisibilityTitleListener;
import com.example.mr_zyl.project824.pro.essence.view.adapter.EssenceAdapter;
import com.example.mr_zyl.project824.pro.essence.view.navigation.EssenceNavigationBuilder;
import com.example.mr_zyl.project824.utils.ToastUtil;

import java.util.Arrays;

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
    ViewPager vp_essence;
    private EssenceNavigationBuilder builder;
    private int color1, color2;
    @Override
    public int getContentView() {
        return R.layout.essence;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        EventBus.getDefault().register(this);
    }

    @Override
    public void initContentView(View viewContent) {
        ButterKnife.bind(this, viewContent);
        color1 = getResources().getColor(R.color.colorAccent);
        color2 = getResources().getColor(R.color.white);
        initToolBar(ll_essence_tabcontainer);

        tab_essence.setBackgroundColor(color1);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public void setStatusBarView(View view) {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {//如果系统不支持透明状态栏
            view.setVisibility(View.GONE);
        } else {
            view.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 主页侧滑回调处理
     *
     * @param touch
     */
    public void onEventMainThread(ResideDispatch touch) {

        int currentColor = ColorUtils.blendARGB(color1, color2, touch.getRadio());

        tab_essence.setBackgroundColor(currentColor);
        builder.getContentView().setBackgroundColor(currentColor);
    }

    private void initToolBar(View viewContent) {
        builder = new EssenceNavigationBuilder(getContext());
        builder.setTitle(R.string.main_essence_text)
                .setLeftIcon(R.drawable.ic_head)
                .setRightIcon(R.drawable.ic_cleancache)
                .setLeftIconOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ResideTouch touch = new ResideTouch();
                        touch.setHandleType(ResideTouch.HandleTypeTagToggle);
                        EventBus.getDefault().post(touch);
                    }
                })
                .setRightIconOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ClearMemoryCache();
                    }
                }).createAndBind((ViewGroup) viewContent);

        builder.getContentView().setBackgroundColor(color1);

    }

    public static final String TAB_TAG = "@zylmove@";

    @Override
    public void initData() {
        String[] titles = getResources().getStringArray(R.array.essence_video_tab);
        EssenceAdapter adapter = new EssenceAdapter(getFragmentManager(), Arrays.asList(titles), this);
        this.vp_essence.setOffscreenPageLimit(1);
        this.vp_essence.setAdapter(adapter);
        this.vp_essence.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                ResideTouch resideTouch = new ResideTouch(position == 0 ? true : false, ResideTouch.HandleTypeTagLeftBorder);
                EventBus.getDefault().post(resideTouch);
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }
        });
        this.tab_essence.setupWithViewPager(this.vp_essence);

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
        this.tab_essence.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(this.vp_essence) {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                super.onTabSelected(tab);
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
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
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
    private ImageView iv_essence_left,iv_essence_right;

    private void createTranslate(final boolean is_open, final int scroll_max_height) {
        final int colorAccent = getContext().getResources().getColor(R.color.colorAccent);
        tv_essence_title = ButterKnife.findById(builder.getContentView(), R.id.tv_essence_title);
        iv_essence_left=ButterKnife.findById(builder.getContentView(), R.id.iv_essence_left);
        iv_essence_right=ButterKnife.findById(builder.getContentView(), R.id.iv_essence_right);


        ValueAnimator animation = ValueAnimator.ofInt(0, scroll_max_height);
        animation.setDuration(250);
        animation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float animatedValue = animation.getAnimatedFraction();
                if (is_open) {//向下 打开
                    ll_essence_title.setTranslationY(scroll_max_height * (animatedValue - 1));
                    tv_essence_title.setTextColor(ColorUtils.blendARGB(colorAccent, Color.WHITE, animatedValue));
                    iv_essence_left.setAlpha(255*animatedValue);
                    iv_essence_right.setAlpha(255*animatedValue);

                    LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) vp_essence.getLayoutParams();
                    layoutParams.topMargin = (int) (scroll_max_height * (animatedValue - 1));
                    vp_essence.setLayoutParams(layoutParams);
                } else {//向上 隐藏
                    ll_essence_title.setTranslationY(-scroll_max_height * animatedValue);
                    tv_essence_title.setTextColor(ColorUtils.blendARGB(Color.WHITE, colorAccent, animatedValue));
                    iv_essence_left.setAlpha(255*(1-animatedValue));
                    iv_essence_right.setAlpha(255*(1-animatedValue));

                    LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) vp_essence.getLayoutParams();
                    layoutParams.topMargin = (int) (-scroll_max_height * animatedValue);
                    vp_essence.setLayoutParams(layoutParams);
                }

            }
        });
        animation.start();
    }

}
