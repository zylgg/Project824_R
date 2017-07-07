package com.example.mr_zyl.project.pro.essence.view;

import android.os.AsyncTask;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.mr_zyl.project.R;
import com.example.mr_zyl.project.pro.base.view.BaseFragment;
import com.example.mr_zyl.project.pro.essence.view.adapter.EssenceAdapter;
import com.example.mr_zyl.project.pro.essence.view.navigation.EssenceNavigationBuilder;
import com.example.mr_zyl.project.utils.DisplayUtil;
import com.example.mr_zyl.project.utils.Settings;
import com.example.mr_zyl.project.utils.ToastUtil;

import java.util.Arrays;

import de.greenrobot.event.EventBus;
import me.xiaopan.sketch.Configuration;
import me.xiaopan.sketch.Sketch;
import me.xiaopan.sketch.cache.BitmapPool;
import me.xiaopan.sketch.cache.DiskCache;
import me.xiaopan.sketch.cache.MemoryCache;

/**
 * Created by Mr_Zyl on 2016/8/25.
 */
public class essence extends BaseFragment implements EssenceAdapter.ShowCloseToolbarListener {

    private TabLayout tab_essence;
    private ViewPager vp_essence;
    private EssenceNavigationBuilder builder;
    private int mToolbarHeight;

    @Override
    public int getContentView() {
        return R.layout.essence;
    }

    @Override
    public void initContentView(View viewContent) {
        initToolBar(viewContent);
        initbaseview(viewContent);
        mToolbarHeight= DisplayUtil.dip2px(Fcontext,75);
    }

    private void initbaseview(View viewContent) {
        this.tab_essence = (TabLayout) viewContent.findViewById(R.id.tab_essence);
        this.vp_essence = (ViewPager) viewContent.findViewById(R.id.vp_essence);
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
                        boolean is_load = Settings.getBoolean(getContext(), Settings.PREFERENCE_SCROLLING_PAUSE_LOAD);

                        Settings.putBoolean(getContext(), Settings.PREFERENCE_SCROLLING_PAUSE_LOAD, !is_load);
                        ToastUtil.showToast(getContext(), is_load ? "滑动时加载！" : "滑动时不加载！", 0);
                    }
                }).createAndBind((ViewGroup) viewContent);

    }

    @Override
    public void initData() {
        String[] titles = getResources().getStringArray(R.array.essence_video_tab);
        EssenceAdapter adapter = new EssenceAdapter(this, getFragmentManager(), Arrays.asList(titles));
        this.vp_essence.setAdapter(adapter);
        this.tab_essence.setupWithViewPager(this.vp_essence);
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
                                showProgressDialog("清理中...", false);
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

    @Override
    public void show() {
        builder.getLl_toolbar_essence_contentlayou().animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
    }

    @Override
    public void Hide() {
        builder.getLl_toolbar_essence_contentlayou().animate().translationY(-mToolbarHeight).setInterpolator(new AccelerateInterpolator(2)).start();
    }

    @Override
    public void onMoved(int distance) {
        builder.getLl_toolbar_essence_contentlayou().setTranslationY(-distance);
    }
}
