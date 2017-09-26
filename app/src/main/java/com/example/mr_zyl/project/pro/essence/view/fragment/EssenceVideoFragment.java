package com.example.mr_zyl.project.pro.essence.view.fragment;


import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;

import com.example.mr_zyl.project.R;
import com.example.mr_zyl.project.bean.PostsListBean;
import com.example.mr_zyl.project.mvp.presenter.impl.MvpBasePresenter;
import com.example.mr_zyl.project.pro.base.presenter.BasePresenter;
import com.example.mr_zyl.project.pro.base.view.BaseFragment;
import com.example.mr_zyl.project.pro.base.view.refreshview.XRefreshView;
import com.example.mr_zyl.project.pro.essence.presenter.EssenceVideoPresenter;
import com.example.mr_zyl.project.pro.essence.refreshEvent;
import com.example.mr_zyl.project.pro.essence.view.adapter.EssenceRecycleAdapter;
import com.example.mr_zyl.project.pro.essence.view.essence;
import com.example.mr_zyl.project.pro.essence.view.listener.ScrollingPauseLoadManager;
import com.example.mr_zyl.project.pro.essence.view.selfview.CustomFooterView;
import com.example.mr_zyl.project.pro.essence.view.selfview.MyDecoration;
import com.example.mr_zyl.project.utils.DisplayUtil;
import com.example.mr_zyl.project.utils.SystemAppUtils;
import com.example.mr_zyl.project.utils.ToastUtil;
import com.example.zylsmallvideolibrary.JCVideoPlayer;
import com.example.zylsmallvideolibrary.VideoEvents;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;


public class EssenceVideoFragment extends BaseFragment {

    private static final String TAG ="EssenceVideoFragment" ;
    private int mType = 0;
    private String mTitle;
    private FrameLayout fl_essence_list;
    private XRefreshView refreshview_id;
    private RecyclerView rv_essence_one;
    private EssenceRecycleAdapter adapter;
    private EssenceVideoPresenter presenter;
    private essence.ScrollHideListener listener;

    public EssenceVideoFragment(essence.ScrollHideListener listener) {
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
        int paddingTop = DisplayUtil.dip2px(Fcontext, 98) + SystemAppUtils.getStatusHeight(getContext());

        fl_essence_list = (FrameLayout) contentView.findViewById(R.id.fl_essence_list);
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
        rv_essence_one.setHasFixedSize(true);
        rv_essence_one.setLayoutManager(new LinearLayoutManager(getContext()));//设置列表管理器(LinearLayoutManager指水平或者竖直，默认数值)
        rv_essence_one.addItemDecoration(new MyDecoration(getContext(), MyDecoration.VERTICAL_LIST));

        ScrollingPauseLoadManager loadManager = new ScrollingPauseLoadManager(getContext());
        loadManager.setOnScrollListener(new HidingScrollListener(Fcontext) {
            @Override
            public void onMoved(int distance) {
                if (listener!=null){
                    listener.onMoved(distance);
                }
            }

            @Override
            public void onShow() {
                if (listener!=null){
                    listener.onShow();
                }
            }

            @Override
            public void onHide() {
                if (listener!=null){
                    listener.onHide();
                }
            }
        });
        rv_essence_one.addOnScrollListener(loadManager);

        adapter = new EssenceRecycleAdapter(getContext(), postlists);
        adapter.setCustomLoadMoreView(new CustomFooterView(getContext()));//Recycleview需要在底部控制添加footerview

        rv_essence_one.setAdapter(adapter);
    }

    /**
     *  设置是否支持下拉刷新
     * @param can
     */
    public void setPullRefresh(boolean can){
        //设置是否拦截子view，
        refreshview_id.disallowInterceptTouchEvent(!can);
    }

    public abstract class HidingScrollListener extends RecyclerView.OnScrollListener {

        private static final float HIDE_THRESHOLD = 10;
        private static final float SHOW_THRESHOLD = 70;

        private int mToolbarOffset = 0;
        private boolean mControlsVisible = true;
        private int mToolbarHeight;
        private int mTotalScrolledDistance;

        public HidingScrollListener(Context context) {
            mToolbarHeight = DisplayUtil.dip2px(context, 50);
        }

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);

            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                if (mTotalScrolledDistance < mToolbarHeight) {
                    setVisible();
                } else {
                    if (mControlsVisible) {
                        if (mToolbarOffset > HIDE_THRESHOLD) {
                            setInvisible();
                        } else {
                            setVisible();
                        }
                    } else {
                        if ((mToolbarHeight - mToolbarOffset) > SHOW_THRESHOLD) {
                            setVisible();
                        } else {
                            setInvisible();
                        }
                    }
                }
            }

        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            clipToolbarOffset();
            onMoved(mToolbarOffset);

            if ((mToolbarOffset < mToolbarHeight && dy > 0) || (mToolbarOffset > 0 && dy < 0)) {
                mToolbarOffset += dy;
            }
            if (mTotalScrolledDistance < 0) {
                mTotalScrolledDistance = 0;
            } else {
                mTotalScrolledDistance += dy;
            }
        }

        private void clipToolbarOffset() {
            if (mToolbarOffset > mToolbarHeight) {
                mToolbarOffset = mToolbarHeight;
            } else if (mToolbarOffset < 0) {
                mToolbarOffset = 0;
            }
        }

        private void setVisible() {
            if (mToolbarOffset > 0) {
                onShow();
                mToolbarOffset = 0;
            }
            mControlsVisible = true;
        }

        private void setInvisible() {
            if (mToolbarOffset < mToolbarHeight) {
                onHide();
                mToolbarOffset = mToolbarHeight;
            }
            mControlsVisible = false;
        }

        public abstract void onMoved(int distance);

        public abstract void onShow();

        public abstract void onHide();

    }

    public void onEventMainThread(refreshEvent event){
        setPullRefresh(event.isCan());
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
                    if (isDownRefresh) {//下拉刷新（清理一遍数据）
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
