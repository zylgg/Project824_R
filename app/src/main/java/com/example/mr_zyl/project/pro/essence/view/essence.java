package com.example.mr_zyl.project.pro.essence.view;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.text.format.Formatter;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.mr_zyl.project.R;
import com.example.mr_zyl.project.pro.base.view.BaseFragment;
import com.example.mr_zyl.project.pro.base.view.residemenu.ResideTouch;
import com.example.mr_zyl.project.pro.essence.refreshEvent;
import com.example.mr_zyl.project.pro.essence.view.adapter.EssenceAdapter;
import com.example.mr_zyl.project.pro.essence.view.navigation.EssenceNavigationBuilder;
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
    @BindView(R.id.cl_essence)
    CoordinatorLayout cl_essence;
    private EssenceNavigationBuilder builder;

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

    private void initToolBar(View viewContent) {
        builder = new EssenceNavigationBuilder(getContext());
        builder.setBackground(R.drawable.toolbar_backgound_essence_shape)
                .setTitle(R.string.main_essence_text)
                .setLeftIcon(R.drawable.ic_head)
                .setRightIcon(R.drawable.ic_cleancache)
                .setLeftIconOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ResideTouch touch=new ResideTouch();
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

    }

    public static final String TAB_TAG = "@zylmove@";

    @Override
    public void initData() {
        String[] titles = getResources().getStringArray(R.array.essence_video_tab);
        EssenceAdapter adapter = new EssenceAdapter(getFragmentManager(), Arrays.asList(titles));
        this.vp_essence.setOffscreenPageLimit(1);
        this.vp_essence.setAdapter(adapter);
        //默认在左边界
        EventBus.getDefault().post(new ResideTouch(true,ResideTouch.HandleTypeTagLeftBorder));
        this.vp_essence.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
//                Log.i(TAG, "position: "+position);
                EventBus.getDefault().post(new ResideTouch(position==0?true:false,ResideTouch.HandleTypeTagLeftBorder));
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

}
