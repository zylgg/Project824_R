package com.example.mr_zyl.project.pro.mine.view;

import android.app.ProgressDialog;
import android.content.Intent;
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
import com.example.mr_zyl.project.pro.base.view.BaseFragment;
import com.example.mr_zyl.project.pro.base.view.item.DefaultImpleItemBuilder;
import com.example.mr_zyl.project.pro.essence.view.selfview.PlayVideoIconView;
import com.example.mr_zyl.project.pro.mine.view.activity.BaiduMapActivity;
import com.example.mr_zyl.project.pro.mine.view.activity.BlurredActivity;
import com.example.mr_zyl.project.pro.mine.view.activity.CurtainActivity;
import com.example.mr_zyl.project.pro.mine.view.activity.FastBlurActivity;
import com.example.mr_zyl.project.pro.mine.view.activity.ListViewSSActivity;
import com.example.mr_zyl.project.pro.mine.view.activity.LoadGifActivity;
import com.example.mr_zyl.project.pro.mine.view.activity.MoreLevelActivity;
import com.example.mr_zyl.project.pro.mine.view.activity.QRImageActivity;
import com.example.mr_zyl.project.pro.mine.view.navigation.MineNavigationBuilder;
import com.example.mr_zyl.project.pro.mine.view.selfview.SmileyLoadingView;
import com.example.mr_zyl.project.utils.DisplayUtil;
import com.example.mr_zyl.project.utils.SystemAppUtils;
import com.example.mr_zyl.project.utils.ToastUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;
import com.zhy.http.okhttp.request.RequestCall;

import java.io.File;

import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by Mr_Zyl on 2016/8/25.
 */
public class Mine extends BaseFragment {

    public DefaultImpleItemBuilder builder;
    /**
     * 我构建的item布局
     */
    public LinearLayout ll_mine_itemview;
    /**
     * 是否停止了进度动画
     */
    boolean is_stop = true;
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

    @Override
    public int getContentView() {
        return R.layout.mine;
    }

    @Override
    public void initContentView(View viewContent) {
        //初始化自定义的构造者模式的toolbar
        initToolBar(viewContent);
        //设备屏幕信息
        tv_fenbianlv = (TextView) viewContent.findViewById(R.id.tv_fenbianlv);
        String screeninfo = "完整高度" +
                SystemAppUtils.getDpi(getContext())
                + "\n状态栏"
                + SystemAppUtils.getStatusHeight(getContext())
                + "\n宽度"
                + DisplayUtil.Width(getContext())
                + "\n内容高度"
                + DisplayUtil.Height(getContext())
                + "\n虚拟按键"
                + SystemAppUtils.getBottomStatusHeight(getContext());
        tv_fenbianlv.append(screeninfo);
        //分辨率
        mRatingbar = (RatingBar) viewContent.findViewById(R.id.ratingBar);
        mRatingbar.setIsIndicator(false);//是否 不允许用户操作
        mRatingbar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                ToastUtil.showToast(getContext(), rating + "--" + fromUser, Toast.LENGTH_SHORT);
            }
        });
        //测试自定义控件（微笑进度圈，播放按钮）
        slv_loading_view = (SmileyLoadingView) viewContent.findViewById(R.id.slv_loading_view);
        pb_mine_progressbar = (ProgressBar) viewContent.findViewById(R.id.pb_mine_progressbar);
        pviv_mine_test = (PlayVideoIconView) viewContent.findViewById(R.id.pviv_mine_test);
        pviv_mine_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pviv_mine_test.getPlayStatus() == PlayVideoIconView.STATUS.pause) {//暂停时
                    pviv_mine_test.setPlayStatus(PlayVideoIconView.STATUS.playing);
                    slv_loading_view.startSmile();
                    DownQQMusicApk();
                } else {//正在播放时
                    pviv_mine_test.setPlayStatus(PlayVideoIconView.STATUS.pause);
                    slv_loading_view.stopSmile(false);
                }
            }
        });
        initBuilderItems(viewContent);
    }

    /**
     * 初始化自定义的构造者模式的item布局
     *
     * @param viewContent
     */
    private void initBuilderItems(View viewContent) {
        ll_mine_itemview = (LinearLayout) viewContent.findViewById(R.id.ll_mine_itemview);

        builder = new DefaultImpleItemBuilder(getActivity());
        builder.setLeftIcons(R.drawable.login_unlogin_header)
                .setTitleText("位置")
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivityForResult(new Intent(getActivity(), MoreLevelActivity.class), 111);
                    }
                });
        builder.BindParentView((ViewGroup) ll_mine_itemview);

        builder = new DefaultImpleItemBuilder(getActivity());
        builder.setLeftIcons(R.drawable.login_unlogin_header)
                .setTitleText("地图")
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(getActivity(), BaiduMapActivity.class));
                    }
                });
        builder.BindParentView((ViewGroup) ll_mine_itemview);

        builder = new DefaultImpleItemBuilder(getActivity());
        builder.setLeftIcons(R.drawable.login_unlogin_header)
                .setTitleText("新界面")
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FragmentManager Frmanager = getActivity().getSupportFragmentManager();
                        Fragment mingf = Frmanager.findFragmentByTag("我");

                        if (mingf != null && !mingf.isDetached()) {
                            FragmentTransaction ft = Frmanager.beginTransaction();
                            ft.hide(mingf);
                            ft.add(android.R.id.tabcontent, new NewFragment(), "newfragment");
                            ft.commit();
                        }

                    }
                });
        builder.BindParentView((ViewGroup) ll_mine_itemview);

        builder = new DefaultImpleItemBuilder(getActivity());
        builder.setLeftIcons(R.drawable.login_unlogin_header)
                .setTitleText("窗帘效果界面")
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(getActivity(), CurtainActivity.class));
                    }
                });
        builder.BindParentView(ll_mine_itemview);

        builder = new DefaultImpleItemBuilder(getActivity());
        builder.setLeftIcons(R.drawable.login_unlogin_header)
                .setTitleText("各种高斯样式")
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(getActivity(), FastBlurActivity.class));
                    }
                });
        builder.BindParentView(ll_mine_itemview);

        builder = new DefaultImpleItemBuilder(getActivity());
        builder.setLeftIcons(R.drawable.login_unlogin_header)
                .setTitleText("动态模糊--")
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(getActivity(), BlurredActivity.class));
                    }
                });
        builder.BindParentView(ll_mine_itemview);

        builder = new DefaultImpleItemBuilder(getActivity());
        builder.setLeftIcons(R.drawable.login_unlogin_header)
                .setTitleText("listview单条刷新")
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), ListViewSSActivity.class);
//                        intent.putExtra("time",5000);
                        startActivity(intent);
                    }
                });
        builder.BindParentView(ll_mine_itemview);

        builder = new DefaultImpleItemBuilder(getActivity());
        builder.setLeftIcons(R.drawable.login_unlogin_header)
                .setTitleText("本地Gif加载")
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), LoadGifActivity.class);
//                        intent.putExtra("time",5000);
                        startActivity(intent);
                    }
                });
        builder.BindParentView(ll_mine_itemview);

        builder = new DefaultImpleItemBuilder(getActivity());
        builder.setLeftIcons(R.drawable.login_unlogin_header)
                .setTitleText("生成二维码")
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), QRImageActivity.class);
                        startActivity(intent);
                    }
                });
        builder.BindParentView(ll_mine_itemview);
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
     * 测试下载
     */
    private RequestCall call;

    private void DownQQMusicApk() {
        String filepath = BaseFragment.FilePath;
        File files = new File(filepath);
        if (!files.exists()) {
            files.mkdirs();
        }
        //okhttputils不支持断点续传。。。
        FileCallBack fileCallBack = new FileCallBack(filepath, "qqmusic.apk") {
            @Override
            public void onBefore(Request request, int id) {
                showProgressDialog("下载中。。。", false, ProgressDialog.STYLE_HORIZONTAL);
            }

            @Override
            public void inProgress(float progress, long total, int id) {
                int progress_value = (int) (progress * total);
                int total_value = (int) total;
                setHorizontalProgressValue(total_value, progress_value);
//                pb_mine_progressbar.setMax(progress_value);
//                pb_mine_progressbar.setProgress(total_value);
//                Log.i("FileCallBack", "progress: " + progress_value);
//                Log.i("FileCallBack", "total: " + total_value);
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
            builder.getTv_title().setText("位置:" + address);
        }
    }
}
