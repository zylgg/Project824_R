package com.example.mr_zyl.project.pro.mine.view;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mr_zyl.project.R;
import com.example.mr_zyl.project.pro.base.view.BaseFragment;
import com.example.mr_zyl.project.pro.base.view.item.DefaultImpleItemBuilder;
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

/**
 * Created by Mr_Zyl on 2016/8/25.
 */
public class Mine extends BaseFragment {

    public DefaultImpleItemBuilder builder, mapbuilder, GoNewFragment;
    /**
     * 我构建的item布局
     */
    public LinearLayout ll_mine_itemview;
    /**
     * 是否停止了进度动画
     */
    boolean is_stop = true;

    @Override
    public int getContentView() {
        return R.layout.mine;
    }

    @Override
    public void initContentView(View viewContent) {
        RatingBar mRatingbar = (RatingBar) viewContent.findViewById(R.id.ratingBar);
        TextView tv_fenbianlv = (TextView) viewContent.findViewById(R.id.tv_fenbianlv);
        String screeninfo="完整高度" +
                SystemAppUtils.getDpi(getContext())
                + "-状态栏"
                + SystemAppUtils.getStatusHeight(getContext())
                + "-宽度"
                + DisplayUtil.Width(getContext())
                + "\n内容高度"
                + DisplayUtil.Height(getContext())
                + "-物理按键"
                + SystemAppUtils.getBottomStatusHeight(getContext());
        tv_fenbianlv.setText(screeninfo);
        mRatingbar.setIsIndicator(false);//是否 不允许用户操作
        ll_mine_itemview = (LinearLayout) viewContent.findViewById(R.id.ll_mine_itemview);
        final SmileyLoadingView slv_loading_view = (SmileyLoadingView) viewContent.findViewById(R.id.slv_loading_view);
        final Button bt_smile_stop = (Button) viewContent.findViewById(R.id.bt_smile_stop);
        bt_smile_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (is_stop) {
                    slv_loading_view.startSmile();
                } else {
                    slv_loading_view.stopSmile(false);
                }
                is_stop = !is_stop;
                bt_smile_stop.setText(is_stop ? "开始笑" : "停止笑");
            }
        });

        mRatingbar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                ToastUtil.showToast(getContext(), rating + "--" + fromUser, Toast.LENGTH_SHORT);
            }
        });
        initToolBar(viewContent);
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

        mapbuilder = new DefaultImpleItemBuilder(getActivity());
        mapbuilder.setLeftIcons(R.drawable.login_unlogin_header)
                .setTitleText("地图")
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(getActivity(), BaiduMapActivity.class));
                    }
                });
        mapbuilder.BindParentView((ViewGroup) ll_mine_itemview);

        GoNewFragment = new DefaultImpleItemBuilder(getActivity());
        GoNewFragment.setLeftIcons(R.drawable.login_unlogin_header)
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
        GoNewFragment.BindParentView((ViewGroup) ll_mine_itemview);

        mapbuilder = new DefaultImpleItemBuilder(getActivity());
        mapbuilder.setLeftIcons(R.drawable.login_unlogin_header)
                .setTitleText("窗帘效果界面")
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(getActivity(), CurtainActivity.class));
                    }
                });
        mapbuilder.BindParentView((ViewGroup) ll_mine_itemview);

        mapbuilder = new DefaultImpleItemBuilder(getActivity());
        mapbuilder.setLeftIcons(R.drawable.login_unlogin_header)
                .setTitleText("各种高斯样式")
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(getActivity(), FastBlurActivity.class));
                    }
                });
        mapbuilder.BindParentView((ViewGroup) ll_mine_itemview);

        mapbuilder = new DefaultImpleItemBuilder(getActivity());
        mapbuilder.setLeftIcons(R.drawable.login_unlogin_header)
                .setTitleText("动态模糊--")
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(getActivity(), BlurredActivity.class));
                    }
                });
        mapbuilder.BindParentView((ViewGroup) ll_mine_itemview);

        mapbuilder = new DefaultImpleItemBuilder(getActivity());
        mapbuilder.setLeftIcons(R.drawable.login_unlogin_header)
                .setTitleText("listview单条刷新")
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), ListViewSSActivity.class);
//                        intent.putExtra("time",5000);
                        startActivity(intent);
                    }
                });
        mapbuilder.BindParentView((ViewGroup) ll_mine_itemview);

        mapbuilder = new DefaultImpleItemBuilder(getActivity());
        mapbuilder.setLeftIcons(R.drawable.login_unlogin_header)
                .setTitleText("本地Gif加载")
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), LoadGifActivity.class);
//                        intent.putExtra("time",5000);
                        startActivity(intent);
                    }
                });
        mapbuilder.BindParentView((ViewGroup) ll_mine_itemview);

        mapbuilder = new DefaultImpleItemBuilder(getActivity());
        mapbuilder.setLeftIcons(R.drawable.login_unlogin_header)
                .setTitleText("生成二维码")
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), QRImageActivity.class);
                        startActivity(intent);
                    }
                });
        mapbuilder.BindParentView((ViewGroup) ll_mine_itemview);
    }

    private void initToolBar(View viewContent) {
        MineNavigationBuilder builder = new MineNavigationBuilder(getContext());
        builder.setTitle(R.string.main_mine_text)
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 112) {
            String address = data.getExtras().getString("address");
            builder.getTv_title().setText("位置:" + address);
        }
    }
}
