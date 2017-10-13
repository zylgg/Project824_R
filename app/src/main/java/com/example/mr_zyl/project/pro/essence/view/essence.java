package com.example.mr_zyl.project.pro.essence.view;

import android.os.AsyncTask;
import android.os.Build;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.mr_zyl.project.R;
import com.example.mr_zyl.project.pro.base.view.BaseFragment;
import com.example.mr_zyl.project.pro.essence.refreshEvent;
import com.example.mr_zyl.project.pro.essence.view.adapter.EssenceAdapter;
import com.example.mr_zyl.project.pro.essence.view.navigation.EssenceNavigationBuilder;
import com.example.mr_zyl.project.utils.DensityUtil;
import com.example.mr_zyl.project.utils.ToastUtil;

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
public class essence extends BaseFragment {

    private static final String TAG = "essence";
    @BindView(R.id.tv_fitssystemwindows_view)
    TextView tv_fitssystemwindows_view;
    @BindView(R.id.ll_essence_tabcontainer)
    LinearLayout ll_essence_tabcontainer;
    @BindView(R.id.tab_essence)
    TabLayout tab_essence;
    @BindView(R.id.vp_essence)
    ViewPager vp_essence;
    @BindView(R.id.abl_essence)
    AppBarLayout abl_essence;
    private EssenceNavigationBuilder builder;
    private int mToolbarHeight;

    @Override
    public int getContentView() {
        return R.layout.essence;
    }

    @Override
    public void initContentView(View viewContent) {
        ButterKnife.bind(this, viewContent);
        setStatusBarView(tv_fitssystemwindows_view);
        initToolBar(ll_essence_tabcontainer);

        tv_fitssystemwindows_view.setBackgroundResource(R.drawable.toolbar_backgound_essence_shape);
        tab_essence.setBackgroundResource(R.drawable.toolbar_backgound_essence_shape);

        mToolbarHeight = DensityUtil.dip2px(Fcontext, 50);
        abl_essence.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                refreshEvent event = new refreshEvent();
                if (verticalOffset == 0) {//完全展开
                    event.setCan(true);
                } else {//有折叠
                    event.setCan(false);
                }
                EventBus.getDefault().post(event);
            }
        });

    }

    public void setStatusBarView(View view) {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {//如果系统不支持透明状态栏
            view.setVisibility(View.GONE);
        } else {
            view.setVisibility(View.VISIBLE);
        }
    }

    private void initToolBar(View viewContent) {
        builder = new EssenceNavigationBuilder(getContext());
        builder.setBackground(R.drawable.toolbar_backgound_essence_shape)
                .setTitle(R.string.main_essence_text)
                .setLeftIcon(R.drawable.main_essence_btn_selector)
                .setRightIcon(R.drawable.main_essence_btn_more_selector)
                .setLeftIconOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ClearMemoryCache();
                    }
                })
                .setRightIconOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        boolean is_load = Settings.getBoolean(getContext(), Settings.PREFERENCE_SCROLLING_PAUSE_LOAD);

//                        Settings.putBoolean(getContext(), Settings.PREFERENCE_SCROLLING_PAUSE_LOAD, !is_load);

//                        boolean is_load2 = Settings.getBoolean(getContext(), Settings.PREFERENCE_SCROLLING_PAUSE_LOAD);
//                        ToastUtil.showToast(getContext(), is_load2 ? "滑动时不加载！" : "滑动时加载！", 0);
                    }
                }).createAndBind((ViewGroup) viewContent);

    }

    @Override
    public void initData() {
        String[] titles = getResources().getStringArray(R.array.essence_video_tab);
        ScrollHideListener scrollHideListener = new ScrollHideListener() {

            @Override
            public void onMoved(int distance) {
                ll_essence_tabcontainer.setTranslationY(-distance);
            }

            @Override
            public void onShow() {
                ll_essence_tabcontainer.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
            }

            @Override
            public void onHide() {
                ll_essence_tabcontainer.animate().translationY(-mToolbarHeight).setInterpolator(new AccelerateInterpolator(2)).start();
            }
        };
        EssenceAdapter adapter = new EssenceAdapter(getFragmentManager(), Arrays.asList(titles), null);
        this.vp_essence.setAdapter(adapter);
        this.vp_essence.setCurrentItem(4);
        this.tab_essence.setupWithViewPager(this.vp_essence);
    }

    public interface ScrollHideListener {
        void onMoved(int distance);

        void onShow();

        void onHide();
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
                .title("设置")
                .content("已使用：" + all_usercatch)
                .callback(new MaterialDialog.SimpleCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
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
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
