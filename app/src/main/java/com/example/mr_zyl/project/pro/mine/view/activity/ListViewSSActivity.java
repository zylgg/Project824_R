package com.example.mr_zyl.project.pro.mine.view.activity;

import android.os.Bundle;
import android.os.Handler;
import android.widget.AbsListView;

import com.example.mr_zyl.project.R;
import com.example.mr_zyl.project.pro.base.view.BaseActivity;
import com.example.mr_zyl.project.pro.essence.view.selfview.PullDownRefresh_ListView;
import com.example.mr_zyl.project.pro.mine.view.SlidingLib.SlideListAdapter;
import com.example.mr_zyl.project.pro.mine.view.SlidingLib.SlidingContentView;
import com.example.mr_zyl.project.pro.mine.view.SlidingLib.listviewbean;
import com.example.mr_zyl.project.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

public class ListViewSSActivity extends BaseActivity {

    PullDownRefresh_ListView lv_listviewss;
    List<listviewbean> lists;
    SlideListAdapter adapter;
    String TAG = "ListViewSSActivity";

    @Override
    protected int initLayoutId() {
        return R.layout.activity_list_view_ss;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lv_listviewss = (PullDownRefresh_ListView) findViewById(R.id.lv_listviewss);

        lists = new ArrayList<>();
        for (int i = 0; i < 26; i++) {
            lists.add(new listviewbean("item" + i, "点击"));
        }
        adapter = new SlideListAdapter(this, lists);
        adapter.setOnItemClickListener(new SlidingContentView.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                ToastUtil.showToast(ListViewSSActivity.this, position+"我点击了");
                listviewbean b = lists.get(position);
                b.setStatus("我已经点击了！");
                adapter.updateSingleRow(lv_listviewss, b.getName());
            }
        });
        lv_listviewss.setonRefreshListener(new PullDownRefresh_ListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        lv_listviewss.onRefreshComplete();
                    }
                }, 2000);
            }
        });
        lv_listviewss.setAdapters(adapter);
        lv_listviewss.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                //正在滑动，立马将之前的已打开的视图关闭
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    adapter.getSlideManager().closeAllLayout();
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
            }
        });


    }

}
