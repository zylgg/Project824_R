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
import com.example.mr_zyl.project.pro.essence.view.adapter.EssenceAdapter;
import com.example.mr_zyl.project.pro.essence.view.adapter.EssenceRecycleAdapter;
import com.example.mr_zyl.project.pro.essence.view.listener.ScrollingPauseLoadManager;
import com.example.mr_zyl.project.pro.essence.view.selfview.CustomFooterView;
import com.example.mr_zyl.project.pro.essence.view.selfview.MyDecoration;
import com.example.mr_zyl.project.utils.ToastUtil;
import com.example.zylsmallvideolibrary.JCVideoPlayer;
import com.example.zylsmallvideolibrary.VideoEvents;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;


public class EssenceVideoFragment extends BaseFragment {

    private int mType = 0;
    private String mTitle;
    private XRefreshView refreshview_id;
    private RecyclerView rv_essence_one;
    private EssenceRecycleAdapter adapter;
    private EssenceVideoPresenter presenter;
    private EssenceAdapter.ShowCloseToolbarListener listener;

    public EssenceVideoFragment(EssenceAdapter.ShowCloseToolbarListener listener) {
        this.listener = listener;
    }

    public void setType(int mType) {
        this.mType = mType;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    private List<PostsListBean.PostList> postlists = new ArrayList<>();

    @Override
    public int getContentView() {
        return R.layout.fragment_essence_video;
    }

    @Override
    public MvpBasePresenter bindPresenter() {
        presenter = new EssenceVideoPresenter(getContext());
        return presenter;
    }

    @Override
    public void initContentView(View contentView) {
        EventBus.getDefault().register(this);
        refreshview_id = (XRefreshView) contentView.findViewById(R.id.refreshview_id);
        refreshview_id.setPullRefreshEnable(true);
        refreshview_id.setPullLoadEnable(true);
        refreshview_id.setPinnedTime(1000);
        refreshview_id.setAutoLoadMore(false);
        refreshview_id.enableReleaseToLoadMore(false);//到达底部后让其点击加载asdf
        refreshview_id.enableRecyclerViewPullUp(false);//不让Recycleview到达底部继续上啦
        refreshview_id.setPreLoadCount(0);//预加载数量

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
        rv_essence_one.addItemDecoration(new MyDecoration(getContext(), MyDecoration.VERTICAL_LIST));
        //初始化滚动监听
        ScrollingPauseLoadManager scrollmanager = new ScrollingPauseLoadManager(getContext());
        scrollmanager.setOnScrollListener(new HidingScrollListener() {
            @Override
            public void onHide() {
                if (listener!=null){
                    listener.Hide();
                }
            }

            @Override
            public void onShow() {
                if (listener!=null){
                    listener.show();
                }
            }
        });
        rv_essence_one.addOnScrollListener(scrollmanager);
        adapter = new EssenceRecycleAdapter(getContext(), postlists);
        adapter.setCustomLoadMoreView(new CustomFooterView(getContext()));//Recycleview需要在底部控制添加footerview
        rv_essence_one.setAdapter(adapter);
    }

    public abstract class HidingScrollListener extends RecyclerView.OnScrollListener {
        private static final int HIDE_THRESHOLD = 20;
        private int scrolledDistance = 0;
        private boolean controlsVisible = true;

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            int firstVisibleItem = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
            //show views if first item is first visible position and views are hidden
            if (firstVisibleItem == 0) {
                if (!controlsVisible) {
                    onShow();
                    controlsVisible = true;
                }
            } else {
                if (scrolledDistance > HIDE_THRESHOLD && controlsVisible) {
                    onHide();
                    controlsVisible = false;
                    scrolledDistance = 0;
                } else if (scrolledDistance < -HIDE_THRESHOLD && !controlsVisible) {
                    onShow();
                    controlsVisible = true;
                    scrolledDistance = 0;
                }
            }
            if ((controlsVisible && dy > 0) || (!controlsVisible && dy < 0)) {
                scrolledDistance += dy;
            }
        }

        public abstract void onHide();

        public abstract void onShow();

    }

    public void onEventMainThread(VideoEvents event) {
//        if (event.type == VideoEvents.POINT_START_ICON) {
//            Log.i("Video Event", "POINT_START_ICON" + " title is : " + event.obj + " url is : " + event.obj1);
//        } else if (event.type == VideoEvents.POINT_START_THUMB) {
//            Log.i("Video Event", "POINT_START_THUMB" + " title is : " + event.obj + " url is : " + event.obj1);
//        } else if (event.type == VideoEvents.POINT_STOP) {
//            Log.i("Video Event", "POINT_STOP" + " title is : " + event.obj + " url is : " + event.obj1);
//        } else if (event.type == VideoEvents.POINT_STOP_FULLSCREEN) {
//            Log.i("Video Event", "POINT_STOP_FULLSCREEN" + " title is : " + event.obj + " url is : " + event.obj1);
//        } else if (event.type == VideoEvents.POINT_RESUME) {
//            Log.i("Video Event", "POINT_RESUME" + " title is : " + event.obj + " url is : " + event.obj1);
//        } else if (event.type == VideoEvents.POINT_RESUME_FULLSCREEN) {
//            Log.i("Video Event", "POINT_RESUME_FULLSCREEN" + " title is : " + event.obj + " url is : " + event.obj1);
//        } else if (event.type == VideoEvents.POINT_CLICK_BLANK) {
//            Log.i("Video Event", "POINT_CLICK_BLANK" + " title is : " + event.obj + " url is : " + event.obj1);
//        } else if (event.type == VideoEvents.POINT_CLICK_BLANK_FULLSCREEN) {
//            Log.i("Video Event", "POINT_CLICK_BLANK_FULLSCREEN" + " title is : " + event.obj + " url is : " + event.obj1);
//        } else if (event.type == VideoEvents.POINT_CLICK_SEEKBAR) {
//            Log.i("Video Event", "POINT_CLICK_SEEKBAR" + " title is : " + event.obj + " url is : " + event.obj1);
//        } else if (event.type == VideoEvents.POINT_CLICK_SEEKBAR_FULLSCREEN) {
//            Log.i("Video Event", "POINT_CLICK_SEEKBAR_FULLSCREEN" + " title is : " + event.obj + " url is : " + event.obj1);
//        } else if (event.type == VideoEvents.POINT_AUTO_COMPLETE) {
//            Log.i("Video Event", "POINT_AUTO_COMPLETE" + " title is : " + event.obj + " url is : " + event.obj1);
//        } else if (event.type == VideoEvents.POINT_AUTO_COMPLETE_FULLSCREEN) {
//            Log.i("Video Event", "POINT_AUTO_COMPLETE_FULLSCREEN" + " title is : " + event.obj + " url is : " + event.obj1);
//        } else if (event.type == VideoEvents.POINT_ENTER_FULLSCREEN) {
//            Log.i("Video Event", "POINT_ENTER_FULLSCREEN" + " title is : " + event.obj + " url is : " + event.obj1);
//        } else if (event.type == VideoEvents.POINT_QUIT_FULLSCREEN) {
//            Log.i("Video Event", "POINT_QUIT_FULLSCREEN" + " title is : " + event.obj + " url is : " + event.obj1);
//        }
    }

    @Override
    public void onPause() {
        super.onPause();
        JCVideoPlayer.releaseAllVideos();
    }

    /**
     * 加载数据
     *
     * @param isDownRefresh 是否为下拉刷新
     */
    private void loaddata(final boolean isDownRefresh) {
        presenter.GetEssenceListData(mType, isDownRefresh, new BasePresenter.OnUiThreadListener<List<PostsListBean.PostList>>() {
            @Override
            public void OnResult(List<PostsListBean.PostList> result) {
                if (isDownRefresh) {
                    refreshview_id.stopRefresh();
                } else {
                    refreshview_id.stopLoadMore();
                }
                if (result == null) {
                    ToastUtil.showToast(getContext(), "加载失败！");
                } else {
                    //刷新Ui
                    if (isDownRefresh) {
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
