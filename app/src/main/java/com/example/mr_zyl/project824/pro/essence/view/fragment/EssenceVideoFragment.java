package com.example.mr_zyl.project824.pro.essence.view.fragment;


import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.example.mr_zyl.project824.R;
import com.example.mr_zyl.project824.bean.PostsListBean;
import com.example.mr_zyl.project824.mvp.presenter.impl.MvpBasePresenter;
import com.example.mr_zyl.project824.pro.base.presenter.SimpleOnUiThreadListener;
import com.example.mr_zyl.project824.pro.base.view.BaseFragment;
import com.example.mr_zyl.project824.pro.base.view.refreshview.XRefreshView;
import com.example.mr_zyl.project824.pro.essence.OnVisibilityTitleListener;
import com.example.mr_zyl.project824.pro.essence.RvScrollListener;
import com.example.mr_zyl.project824.pro.essence.presenter.EssenceVideoPresenter;
import com.example.mr_zyl.project824.pro.essence.refreshEvent;
import com.example.mr_zyl.project824.pro.essence.view.adapter.EssenceRecycleAdapter;
import com.example.mr_zyl.project824.pro.essence.view.selfview.CustomFooterView;
import com.example.mr_zyl.project824.pro.essence.view.selfview.MyDecoration;
import com.example.mr_zyl.project824.utils.ToastUtil;
import com.example.mr_zyl.project824.view.LoadView;
import com.example.zylsmallvideolibrary.JCVideoPlayer;
import com.example.zylsmallvideolibrary.VideoEvents;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;


public class EssenceVideoFragment extends BaseFragment implements View.OnClickListener {

    private static final String TAG = "EssenceVideoFragment";
    private int mType = 0;
    private String mTitle;
    private EssenceRecycleAdapter adapter;
    private EssenceVideoPresenter presenter;

    @BindView(R.id.refreshview_id)
    XRefreshView refreshview_id;
    @BindView(R.id.rv_essence_one)
    RecyclerView rv_essence_one;
    @BindView(R.id.fab_scrollTop)
    FloatingActionButton fab_scrollTop;
    @BindView(R.id.lv_essence_floatingLoad)
    LoadView lv_essence_floatingLoad;
    boolean isFirstLoad=true;

    public EssenceVideoFragment() {
    }

    public void setType(int mType) {
        this.mType = mType;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    private List<PostsListBean.PostList> postlists = new ArrayList<>();
    private RecyclerView.OnScrollListener scrollListener;
    private XRefreshView.XRefreshViewListener xRefreshListener;
    private OnVisibilityTitleListener onVisibilityTitleListener;

    public void setOnVisibilityTitleListener(OnVisibilityTitleListener onVisibilityTitleListener) {
        this.onVisibilityTitleListener = onVisibilityTitleListener;
    }

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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void initContentView(View contentView) {
        ButterKnife.bind(this, contentView);
        initListener();

        fab_scrollTop.setOnClickListener(this);
        fab_scrollTop.setVisibility(View.GONE);

        refreshview_id.setPullRefreshEnable(true);
        refreshview_id.setPullLoadEnable(true);
        refreshview_id.setPinnedTime(1000);
        refreshview_id.setAutoRefresh(false);
        refreshview_id.setAutoLoadMore(false);
//        refreshview_id.enableReleaseToLoadMore(false);//到达底部后让其点击加载asdf
//        refreshview_id.enableRecyclerViewPullUp(false);//不让Recycleview到达底部继续上啦
//        refreshview_id.setPreLoadCount(0);//预加载数量
        refreshview_id.setXRefreshViewListener(xRefreshListener);

        adapter = new EssenceRecycleAdapter(getContext(), postlists);
        adapter.setCustomLoadMoreView(new CustomFooterView(getContext()));//Recycleview需要在底部控制添加footerview

        rv_essence_one.addItemDecoration(new MyDecoration(Fcontext, MyDecoration.VERTICAL_LIST));
        rv_essence_one.setHasFixedSize(true);
        rv_essence_one.setLayoutManager(new LinearLayoutManager(getContext()));//设置列表管理器(LinearLayoutManager指水平或者竖直，默认数值)
        rv_essence_one.addOnScrollListener(scrollListener);
        rv_essence_one.addOnScrollListener(RvScrollListener.getInstance(onVisibilityTitleListener));
        rv_essence_one.setAdapter(adapter);
    }


    private void initListener() {
        scrollListener = new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }

            @SuppressLint("RestrictedApi")
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
                View firstview = layoutManager.findViewByPosition(firstVisibleItemPosition);
                if (firstVisibleItemPosition == 0 && firstview.getTop() == 0) {//到达顶部了
                    fab_scrollTop.setVisibility(View.GONE);
                } else {
                    fab_scrollTop.setVisibility(View.VISIBLE);
                }
            }
        };

        xRefreshListener = new XRefreshView.SimpleXRefreshListener() {
            @Override
            public void onRefresh() {
                loaddata(true);
            }

            @Override
            public void onLoadMore(boolean isSlience) {
                loaddata(false);
            }
        };
    }

    /**
     * 设置是否支持下拉刷新
     *
     * @param can
     */
    public void setPullRefresh(boolean can) {
        //设置是否拦截子view，
        refreshview_id.disallowInterceptTouchEvent(!can);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_scrollTop:
                LinearLayoutManager layoutManager = (LinearLayoutManager) rv_essence_one.getLayoutManager();
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

//                ToastUtil.showToast(getContext(),"firstVisibleItemPosition:"+firstVisibleItemPosition);
                if (firstVisibleItemPosition > 20 - 1) {
                    rv_essence_one.scrollToPosition(0);
                } else {
                    rv_essence_one.smoothScrollToPosition(0);
                }
                break;
        }
    }

    @Override
    public void initData() {
        loaddata(true);
    }

    /**
     * 加载数据
     *
     * @param isDownRefresh 是否为下拉刷新
     */
    private void loaddata(final boolean isDownRefresh) {
        presenter.GetEssenceListData(mType, isDownRefresh, new SimpleOnUiThreadListener<List<PostsListBean.PostList>>() {
            @Override
            public void OnBefore() {
            }

            @Override
            public void OnResult(List<PostsListBean.PostList> result) {
                if (result == null) {
                    ToastUtil.showToast(getContext(), "加载失败！");
                } else {
                    //刷新Ui
                    if (isDownRefresh) {//下拉刷新（清理一遍数据）
                        postlists.clear();
                    }
                    postlists.addAll(result);
                    adapter.RefreshData(postlists);
                }
            }

            @Override
            public void OnAfter() {
                if (isDownRefresh) {
                    if (isFirstLoad){//如果是第一次加载
                        lv_essence_floatingLoad.splashAndDisappear();
                        isFirstLoad=false;
                    }
                    refreshview_id.stopRefresh();
                } else {
                    refreshview_id.stopLoadMore();
                }
            }
        },true);
    }


    /**
     * 首页底部菜单，点击回调刷新
     * @param event
     */
    public void onEventMainThread(refreshEvent event) {
        //点击菜单，刷新当前tab分类
        boolean refreshCurrent = event.is_RefreshCurrent();
        if (refreshCurrent) {
            refreshview_id.startRefresh();
            return;
        }
//        // 接受essence协调布局监听器 发送的消息：判断是否可以下拉rv
//        setPullRefresh(event.isCan());
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}
