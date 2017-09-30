package com.example.mr_zyl.project.pro.mine.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mr_zyl.project.R;
import com.example.mr_zyl.project.bean.BuilderItemEntity;
import com.example.mr_zyl.project.pro.base.view.BaseFragment;
import com.example.mr_zyl.project.pro.base.view.item.DefaultImpleItemBuilder;
import com.example.mr_zyl.project.pro.essence.view.selfview.PlayVideoIconView;
import com.example.mr_zyl.project.pro.essence.view.selfview.RingView;
import com.example.mr_zyl.project.pro.mine.bean.CustomTabEntity;
import com.example.mr_zyl.project.pro.mine.bean.TabEntity;
import com.example.mr_zyl.project.pro.mine.view.activity.BaiduMapActivity;
import com.example.mr_zyl.project.pro.mine.view.activity.BlurredActivity;
import com.example.mr_zyl.project.pro.mine.view.activity.CurtainActivity;
import com.example.mr_zyl.project.pro.mine.view.activity.FastBlurActivity;
import com.example.mr_zyl.project.pro.mine.view.activity.ListViewSSActivity;
import com.example.mr_zyl.project.pro.mine.view.activity.LoadGifActivity;
import com.example.mr_zyl.project.pro.mine.view.activity.MoreLevelActivity;
import com.example.mr_zyl.project.pro.mine.view.activity.QRImageActivity;
import com.example.mr_zyl.project.pro.mine.view.navigation.MineNavigationBuilder;
import com.example.mr_zyl.project.pro.mine.view.selfview.CommonTabLayout;
import com.example.mr_zyl.project.pro.mine.view.selfview.SmileyLoadingView;
import com.example.mr_zyl.project.utils.AutoRefresh;
import com.example.mr_zyl.project.utils.DisplayUtil;
import com.example.mr_zyl.project.utils.SystemAppUtils;
import com.example.mr_zyl.project.utils.ToastUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;
import com.zhy.http.okhttp.request.RequestCall;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by Mr_Zyl on 2016/8/25.
 */
public class Mine extends BaseFragment implements View.OnClickListener, RatingBar.OnRatingBarChangeListener {

    public DefaultImpleItemBuilder builder;
    /**
     * 我构建的item布局
     */
    public LinearLayout ll_mine_itemview;
    /**
     * 是否停止了进度动画
     */
    boolean is_stop = true;
    @BindView(R.id.ctl_tablist)
    CommonTabLayout ctl_tablist;
    @BindView(R.id.rv_mine_loadprogress)
    RingView rv_mine_loadprogress;
    /**
     * 自定义的评分控件
     */
    private RatingBar mRatingbar;
    /**
     * 分辨率设备信息
     */
    private TextView tv_fenbianlv;
    /**
     * 自定义的播放图标
     */
    private PlayVideoIconView pviv_mine_test;
    /**
     * 微笑进度圈
     */
    private SmileyLoadingView slv_loading_view;
    /**
     * 进度条
     */
    private ProgressBar pb_mine_progressbar;
    private List<BuilderItemEntity> itemlists = new ArrayList<>();
    private Intent intent = null;

    private String[] mTitles = {"首页", "消息", "联系人", "更多"};
    private int[] mIconUnselectIds = {
            R.mipmap.tab_home_unselect, R.mipmap.tab_speech_unselect,
            R.mipmap.tab_contact_unselect, R.mipmap.tab_more_unselect};
    private int[] mIconSelectIds = {
            R.mipmap.tab_home_select, R.mipmap.tab_speech_select,
            R.mipmap.tab_contact_select, R.mipmap.tab_more_select};
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();

    @Override
    public int getContentView() {
        return R.layout.mine;
    }

    @Override
    public void initContentView(View viewContent) {
        ButterKnife.bind(this, viewContent);
        //初始化自定义的构造者模式的toolbar
        initToolBar(viewContent);
        initview(viewContent);
        initlistener();
        initBuilderItems(viewContent);
        for (int i = 0; i < mTitles.length; i++) {
            mTabEntities.add(new TabEntity(mTitles[i], mIconSelectIds[i], mIconUnselectIds[i]));
        }
        ctl_tablist.setTabData(mTabEntities);
    }

    private void initview(View viewContent) {
        //设备屏幕信息
        tv_fenbianlv = (TextView) viewContent.findViewById(R.id.tv_fenbianlv);
        //分辨率
        mRatingbar = (RatingBar) viewContent.findViewById(R.id.ratingBar);
        //测试自定义控件（微笑进度圈，播放按钮）
        slv_loading_view = (SmileyLoadingView) viewContent.findViewById(R.id.slv_loading_view);
        pb_mine_progressbar = (ProgressBar) viewContent.findViewById(R.id.pb_mine_progressbar);
        pviv_mine_test = (PlayVideoIconView) viewContent.findViewById(R.id.pviv_mine_test);
    }

    private void initlistener() {
        mRatingbar.setOnRatingBarChangeListener(this);
        pviv_mine_test.setOnClickListener(this);
    }

    /**
     * 初始化自定义的构造者模式的toolbar
     *
     * @param viewContent 父view
     */
    private void initToolBar(View viewContent) {
        MineNavigationBuilder builder = new MineNavigationBuilder(getContext());
        builder.setTitle(R.string.main_mine_text)
                .setBackground(R.drawable.toolbar_background_mine_shape)
                .setLeftIcon(R.drawable.main_essence_btn_selector)
                .setRightIcon(R.drawable.main_essence_btn_more_selector)
                .setLeftIconOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ToastUtil.showToast(getContext(), "left", 0);
                    }
                })
                .setRightIconOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ToastUtil.showToast(getContext(), "right", 0);
                    }
                }).createAndBind((ViewGroup) viewContent);
    }

    /**
     * 初始化自定义的构造者模式的item布局
     *
     * @param viewContent
     */
    private void initBuilderItems(View viewContent) {
        ll_mine_itemview = (LinearLayout) viewContent.findViewById(R.id.ll_mine_itemview);
        itemlists.add(new BuilderItemEntity(R.drawable.login_unlogin_header, "地图", BaiduMapActivity.class, -1));
        itemlists.add(new BuilderItemEntity(R.drawable.login_unlogin_header, "位置", MoreLevelActivity.class, 111));
        itemlists.add(new BuilderItemEntity(R.drawable.login_unlogin_header, "新界面", null, -1));
        itemlists.add(new BuilderItemEntity(R.drawable.login_unlogin_header, "窗帘效果界面", CurtainActivity.class, -1));
        itemlists.add(new BuilderItemEntity(R.drawable.login_unlogin_header, "各种高斯样式", FastBlurActivity.class, -1));
        itemlists.add(new BuilderItemEntity(R.drawable.login_unlogin_header, "协调布局", BlurredActivity.class, -1));
        itemlists.add(new BuilderItemEntity(R.drawable.login_unlogin_header, "listview单条刷新", ListViewSSActivity.class, -1));
        itemlists.add(new BuilderItemEntity(R.drawable.login_unlogin_header, "本地Gif加载", LoadGifActivity.class, -1));
        itemlists.add(new BuilderItemEntity(R.drawable.login_unlogin_header, "生成二维码", QRImageActivity.class, -1));
        for (int i = 0; i < itemlists.size(); i++) {
            BuilderItemEntity entity = itemlists.get(i);
            builder = new DefaultImpleItemBuilder(getActivity());
            builder.setLeftIcons(entity.left_resid);
            builder.setTitleText(entity.text);
            builder.setOnClickListener(this).BindParentView(ll_mine_itemview);
        }
    }

    int progress = 0;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            progress = progress + 36;
            if (progress <= 360) {
                if (rv_mine_loadprogress.getVisibility()!=View.VISIBLE){
                    rv_mine_loadprogress.setVisibility(View.VISIBLE);
                }
                rv_mine_loadprogress.setAngle(progress);
            } else {
                AutoRefresh.stop(1);
            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pviv_mine_test:
                if (pviv_mine_test.getPlayStatus() == PlayVideoIconView.STATUS.pause) {//暂停时
                    pviv_mine_test.setPlayStatus(PlayVideoIconView.STATUS.playing);
                    slv_loading_view.startSmile();
//                    DownQQMusicApk();
                    progress=0;
                    AutoRefresh.start(handler, 1);
                } else {//正在播放时
                    pviv_mine_test.setPlayStatus(PlayVideoIconView.STATUS.pause);
                    slv_loading_view.stopSmile(false);
                }
                break;
        }
        if (intent == null) {
            intent = new Intent();
        }
        BuilderItemEntity entity = null;
        if (v.getTag() == null) {
            return;
        }
        String text = v.getTag().toString();
        //根据text,获取BuilderItemEntity
        for (int i = 0; i < itemlists.size(); i++) {
            entity = itemlists.get(i);
            if (text.equals(entity.text)) {
                break;
            }
        }
        //处理跳转逻辑
        if (entity.classname != null) {
            intent.setClass(getActivity(), entity.classname);
            if (entity.requestcode == -1) {
                startActivity(intent);
            } else {
                startActivityForResult(intent, entity.requestcode);
            }
        } else {
            switch (entity.text) {
                case "新界面":
                    //处理非跳转逻辑
                    FragmentManager Frmanager = getActivity().getSupportFragmentManager();
                    Fragment mingf = Frmanager.findFragmentByTag("我");

                    if (mingf != null && !mingf.isDetached()) {
                        FragmentTransaction ft = Frmanager.beginTransaction();
                        ft.hide(mingf);
                        ft.add(android.R.id.tabcontent, new NewFragment(), "newfragment");
                        ft.commit();
                    }
                    break;
            }
        }
    }

    @Override
    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
        switch (ratingBar.getId()) {
            case R.id.ratingBar:
                ToastUtil.showToast(getContext(), rating + "--" + fromUser, Toast.LENGTH_SHORT);
                break;
        }
    }

    @Override
    public void initData() {
        String screeninfo = "\n完整高度" +
                SystemAppUtils.getDpi(getContext())
                + "\n宽度"
                + DisplayUtil.Width(getContext())
                + "\n状态栏"
                + SystemAppUtils.getStatusHeight(getContext())
                + "\n内容高度"
                + DisplayUtil.Height(getContext())
                + "\n虚拟按键"
                + SystemAppUtils.getBottomStatusHeight(getContext());
        tv_fenbianlv.append(screeninfo);
        mRatingbar.setIsIndicator(false);//是否 不允许用户操作
    }

    /**
     * 测试下载
     */
    private RequestCall call;

    private void DownQQMusicApk() {
        String filepath = BaseFragment.FilePath;
        File files = new File(filepath);
        if (!files.exists()) {
            files.mkdirs();
        }
        //okhttputils不支持断点下载。。。
        FileCallBack fileCallBack = new FileCallBack(filepath, "qqmusic.apk") {
            @Override
            public void onBefore(Request request, int id) {
                showProgressDialog("下载中。。。", ProgressDialog.STYLE_HORIZONTAL);
            }

            @Override
            public void inProgress(float progress, long total, int id) {
                int progress_value = (int) (progress * total);
                int total_value = (int) total;
                setHorizontalProgressValue(total_value, progress_value);
            }

            @Override
            public void onResponse(File response, int id) {
                ToastUtil.showToast(Fcontext, "path " + response.getAbsolutePath());
                pviv_mine_test.performClick();
            }

            @Override
            public void onError(Call call, Exception e, int id) {
                ToastUtil.showToast(Fcontext, "下载失败！");
            }

            @Override
            public void onAfter(int id) {
                dismissProgressDialog();
            }
        };
        call = OkHttpUtils.get().url("http://dldir1.qq.com/music/clntupate/QQMusic72282.apk").build();
        call.execute(fileCallBack);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 112) {
            String address = data.getExtras().getString("address");
            ToastUtil.showToast(Fcontext, address);
        }
    }
}
