package com.example.mr_zyl.project.pro.essence.view.fragment;


import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.mr_zyl.project.R;
import com.example.mr_zyl.project.bean.PostsListBean;
import com.example.mr_zyl.project.mvp.presenter.impl.MvpBasePresenter;
import com.example.mr_zyl.project.pro.base.presenter.BasePresenter;
import com.example.mr_zyl.project.pro.base.view.BaseFragment;
import com.example.mr_zyl.project.pro.base.view.refreshview.XRefreshView;
import com.example.mr_zyl.project.pro.essence.presenter.EssenceVideoPresenter;
import com.example.mr_zyl.project.pro.essence.view.adapter.EssenceRecycleAdapter;
import com.example.mr_zyl.project.pro.essence.view.listener.ScrollingPauseLoadManager;
import com.example.mr_zyl.project.pro.essence.view.selfview.CustomFooterView;
import com.example.mr_zyl.project.pro.essence.view.selfview.MyDecoration;
import com.example.mr_zyl.project.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;


public class EssenceVideoFragment extends BaseFragment {

    private int mType = 0;
    private String mTitle;
    private XRefreshView refreshview_id;
    private RecyclerView rv_essence_one;
    private EssenceRecycleAdapter adapter;
    private EssenceVideoPresenter presenter;

    public void setType(int mType) {
        this.mType = mType;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }
    private List<PostsListBean.PostList> postlists=new ArrayList<>();

    @Override
    public int getContentView() {
        return R.layout.fragment_essence_video;
    }

    @Override
    public MvpBasePresenter bindPresenter() {
        presenter=new EssenceVideoPresenter(getContext());
        return presenter;
    }

    @Override
    public void initContentView(View contentView) {
        refreshview_id = (XRefreshView) contentView.findViewById(R.id.refreshview_id);
        refreshview_id.setPullRefreshEnable(true);
        refreshview_id.setPullLoadEnable(true);
        refreshview_id.setPinnedTime(1000);
        refreshview_id.setAutoLoadMore(false);
        refreshview_id.enableReleaseToLoadMore(false);//到达底部后让其点击加载asdf
        refreshview_id.enableRecyclerViewPullUp(false);//不让Recycleview到达底部继续上啦
        refreshview_id.setPreLoadCount(0);

        refreshview_id.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {
            @Override
            public void onRefresh() {
                loaddata(true);
            }

            @Override
            public void onLoadMore(boolean isSlience) {
                loaddata(false);
            }
        });
        rv_essence_one = (RecyclerView) contentView.findViewById(R.id.rv_essence_one);
        rv_essence_one.setHasFixedSize(false);//设置固定大小，提高控件性能
        rv_essence_one.setLayoutManager(new LinearLayoutManager(getContext()));//设置列表管理器(LinearLayoutManager指水平或者竖直，默认数值)
        rv_essence_one.addItemDecoration(new MyDecoration(getContext(),MyDecoration.VERTICAL_LIST));
        rv_essence_one.setOnScrollListener(new ScrollingPauseLoadManager(getContext()));

        adapter = new EssenceRecycleAdapter(getContext(), postlists);
        adapter.setCustomLoadMoreView(new CustomFooterView(getContext()));//Recycleview需要在底部控制添加footerview
        rv_essence_one.setAdapter(adapter);
    }

    /**
     * 加载数据
     * @param isDownRefresh 是否为下拉刷新
     */
    private void loaddata(final boolean isDownRefresh) {
        presenter.GetEssenceListData(mType, isDownRefresh, new BasePresenter.OnUiThreadListener<List<PostsListBean.PostList>>() {
            @Override
            public void OnResult(List<PostsListBean.PostList> result) {
                if (isDownRefresh){
                    refreshview_id.stopRefresh();
                }else {
                    refreshview_id.stopLoadMore();
                }
                if (result==null){
                    ToastUtil.showToast(getContext(),"加载失败！");
                }else{
                    //刷新Ui
                    if (isDownRefresh){
                        postlists.clear();
                    }
                    postlists.addAll(result);
                    adapter.RefreshData(postlists);
                }
            }
        });

    }

    @Override
    public void initData() {
        loaddata(true);
    }



}
