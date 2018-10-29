package com.example.mr_zyl.project824.pro.mine.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.mr_zyl.project824.R;
import com.example.mr_zyl.project824.pro.base.view.BaseFragment;
import com.example.mr_zyl.project824.pro.base.view.refreshview.XNestedScrollView;
import com.example.mr_zyl.project824.pro.base.view.refreshview.XRefreshView;
import com.example.mr_zyl.project824.pro.base.view.refreshview.XRefreshViewFooter;
import com.example.mr_zyl.project824.pro.base.view.refreshview.XScrollView;
import com.example.mr_zyl.project824.pro.mine.Adapter.BlurredAdapter;
import com.example.mr_zyl.project824.pro.mine.view.activity.BlurredActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

/**
 * Created by TFHR02 on 2017/9/8.
 */
public class BlurredTabfragment extends BaseFragment {
    @BindView(R.id.xrv_blurred_tab_fragment)
    XRefreshView xrv_blurred_tab_fragment;
    @BindView(R.id.tv_Completed_log)
    TextView tv_Completed_log;

    @BindView(R.id.rv_blurred_list)
    RecyclerView rv_blurred_list;
//    @BindView(R.id.tv_blurred_tab_fragment_title)
//    TextView tv_blurred_tab_fragment_title;

    private static final String POSITION_KEY = "postion";

    public static BlurredTabfragment getInstance(int pos) {
        BlurredTabfragment blurredTabfragment = new BlurredTabfragment();
        Bundle bundle = new Bundle();
        bundle.putInt(POSITION_KEY, pos);
        blurredTabfragment.setArguments(bundle);
        return blurredTabfragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public int getContentView() {
        return R.layout.blurred_tab_fragment_layout;
    }
    /**
     * 下拉刷新拦截
     *
     * @param statu
     */
    public void onEventMainThread(BlurredActivity.ShopAppBarLayoutOpenStatus statu) {
        switch (statu) {
            case wholeOpen:
                xrv_blurred_tab_fragment.disallowInterceptTouchEvent(false,XRefreshView.AppbarLayoutStatus.isWholeOpenStatus);
//                xrv_blurred_tab_fragment.setIs_jump_DownEvent(true);
                break;
            case other:
                xrv_blurred_tab_fragment.disallowInterceptTouchEvent(true,XRefreshView.AppbarLayoutStatus.isOther);
                break;
            case wholeClose:
                xrv_blurred_tab_fragment.disallowInterceptTouchEvent(false,XRefreshView.AppbarLayoutStatus.isWholeOpenClose);
                break;
        }
    }
    private int POS;
    private  XRefreshViewFooter xRefreshViewFooter;
    @Override
    public void initContentView(View viewContent) {
        ButterKnife.bind(this, viewContent);
        Bundle arguments = getArguments();
        POS = arguments.getInt(POSITION_KEY);

//        tv_blurred_tab_fragment_title.setText("tab分类："+POS);
        xrv_blurred_tab_fragment.setPullLoadEnable(true);
        xrv_blurred_tab_fragment.setAutoLoadMore(true);
        tv_Completed_log.setVisibility(View.GONE);
         xRefreshViewFooter = new XRefreshViewFooter(getContext());
        xrv_blurred_tab_fragment.setCustomFooterView(xRefreshViewFooter);
        xrv_blurred_tab_fragment.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener(){
            @Override
            public void onRefresh(boolean isPullDown) {
                xrv_blurred_tab_fragment.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (xrv_blurred_tab_fragment.hasLoadCompleted()){
                            tv_Completed_log.setVisibility(View.GONE);
                            xrv_blurred_tab_fragment.setLoadComplete(false);
                        }
                        xrv_blurred_tab_fragment.stopRefresh();
                    }
                },1500);
            }

            @Override
            public void onLoadMore(boolean isSilence) {
                xrv_blurred_tab_fragment.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (lists.size()>50){
                            tv_Completed_log.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    tv_Completed_log.setVisibility(View.VISIBLE);
                                }
                            },1000);

                            xrv_blurred_tab_fragment.setLoadComplete(true);
                        }else{
                            int start_i=lists.size();
                            for (int i = start_i; i < start_i+20; i++) {
                                lists.add(i + "-Custom_load");
                            }
                            rv_blurred_list.getAdapter().notifyDataSetChanged();
                            xrv_blurred_tab_fragment.stopLoadMore();
                        }

                    }
                },1500);
            }
        });
//
        rv_blurred_list.setHasFixedSize(true);
        rv_blurred_list.setNestedScrollingEnabled(false);
        rv_blurred_list.setLayoutManager(new LinearLayoutManager(Fcontext));
    }

    List<String> lists = new ArrayList<String>();
    @Override
    public void initData() {
        for (int i = 0; i < 20; i++) {
            lists.add(i + "-Custom");
        }
        rv_blurred_list.setAdapter(new BlurredAdapter(Fcontext, lists));
//        rv_blurred_list.setFocusable(false);

    }
}
