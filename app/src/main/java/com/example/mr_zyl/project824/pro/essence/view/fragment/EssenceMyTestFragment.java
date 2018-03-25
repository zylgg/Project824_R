package com.example.mr_zyl.project824.pro.essence.view.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.example.mr_zyl.project824.R;
import com.example.mr_zyl.project824.bean.PostsListBean;
import com.example.mr_zyl.project824.mvp.presenter.impl.MvpBasePresenter;
import com.example.mr_zyl.project824.pro.base.presenter.SimpleOnUiThreadListener;
import com.example.mr_zyl.project824.pro.base.view.BaseFragment;
import com.example.mr_zyl.project824.pro.essence.presenter.EssenceVideoPresenter;
import com.example.mr_zyl.project824.pro.essence.view.adapter.EssenceRecycleAdapter;
import com.example.mr_zyl.project824.pro.essence.view.selfview.MyDecoration;
import com.example.mr_zyl.project824.utils.DensityUtil;
import com.example.mr_zyl.project824.utils.ToastUtil;
import com.example.zylsmallvideolibrary.JCVideoPlayer;
import com.example.zylsmallvideolibrary.VideoEvents;
import com.loopeer.shadow.ShadowView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

/**
 * Created by TFHR02 on 2017/12/26.
 */
public class EssenceMyTestFragment extends BaseFragment  {
    private static final String TAG = "EssenceMyTestFragment";
    private int mType = 0;
    private String mTitle;
    private EssenceRecycleAdapter adapter;
    private EssenceVideoPresenter presenter;


    public void setType(int mType) {
        this.mType = mType;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    private List<PostsListBean.PostList> postlists = new ArrayList<>();

    @Override
    public int getContentView() {
        return R.layout.fragment_essence_mytest;
    }

    @Override
    public MvpBasePresenter bindPresenter() {
        presenter = new EssenceVideoPresenter(getContext());
        return presenter;
    }

    RecyclerView rv_essence_one;
    ShadowView sv_bottom_menu;

    @Override
    public void initContentView(View contentView) {
        EventBus.getDefault().register(this);
        rv_essence_one = ButterKnife.findById(contentView, R.id.rv_essence_one);
        sv_bottom_menu=ButterKnife.findById(contentView,R.id.sv_bottom_menu);

        adapter = new EssenceRecycleAdapter(getContext(), postlists);
        initRecyclerListener();
        rv_essence_one.addItemDecoration(new MyDecoration(Fcontext, MyDecoration.VERTICAL_LIST));
        rv_essence_one.setHasFixedSize(true);
        rv_essence_one.setLayoutManager(new LinearLayoutManager(getContext()));//设置列表管理器(LinearLayoutManager指水平或者竖直，默认数值)
        rv_essence_one.addOnScrollListener(scrollListener);
        rv_essence_one.setAdapter(adapter);
    }

    /**
     * recyclerview高度
     */
    private int scollYFirstDistance;
    /**
     * ShadowVie最大内间距
     */
    private int maxMargin = 0,maxRadius=0;
    private RecyclerView.OnScrollListener scrollListener;

    private void initRecyclerListener(){
        maxRadius = DensityUtil.getpxByDimensize(Fcontext, R.dimen.x90);
        maxMargin = DensityUtil.getpxByDimensize(Fcontext, R.dimen.x45);
        scrollListener = new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = (LinearLayoutManager) rv_essence_one.getLayoutManager();
                int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();

                if (lastVisibleItemPosition == layoutManager.getItemCount() - 1) {//从倒数第一个开始

                    View lastVisiableChildView = layoutManager.findViewByPosition(lastVisibleItemPosition);

                    float height=lastVisiableChildView.getHeight();
                    int top = lastVisiableChildView.getTop();

                    int distance =scollYFirstDistance- top;

                    if (distance < 0) {
                        return;
                    }
                    float scale = distance / height;

                    int radius = (int) (maxRadius * (1-scale));
                    int margin = (int) (maxMargin * (1-scale));

                    sv_bottom_menu.setCornerRadius(radius,radius,radius,radius);
                    sv_bottom_menu.setShadowMargin(margin, margin, margin, margin);

                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        };
        rv_essence_one.addOnScrollListener(scrollListener);
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
                if (isDownRefresh) {
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
                    scollYFirstDistance=rv_essence_one.getHeight();
                    Log.i(TAG, "scollYFirstDistance: "+scollYFirstDistance);
                }
            }

            @Override
            public void OnAfter() {
            }
        });
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
