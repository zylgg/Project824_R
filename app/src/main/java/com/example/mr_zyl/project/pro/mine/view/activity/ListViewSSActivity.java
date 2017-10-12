package com.example.mr_zyl.project.pro.mine.view.activity;

import android.os.Bundle;
import android.widget.AbsListView;
import android.widget.ListView;

import com.example.mr_zyl.project.R;
import com.example.mr_zyl.project.pro.base.view.BaseActivity;
import com.example.mr_zyl.project.pro.mine.view.SlidingLib.SlideListAdapter;
import com.example.mr_zyl.project.pro.mine.view.SlidingLib.SlidingContentView;
import com.example.mr_zyl.project.pro.mine.view.SlidingLib.listviewbean;
import com.example.mr_zyl.project.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

public class ListViewSSActivity extends BaseActivity {

    ListView lv_listviewss;
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
        lv_listviewss = (ListView) findViewById(R.id.lv_listviewss);

        lists = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            lists.add(new listviewbean("item" + i, "点击"));
        }
        adapter = new SlideListAdapter(this, lists);
        adapter.setOnItemClickListener(new SlidingContentView.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                ToastUtil.showToast(ListViewSSActivity.this, "我点击了");
                listviewbean b = lists.get(position);
                b.setStatus("我已经点击了！");
                adapter.updateSingleRow(lv_listviewss, b.getName());
            }
        });
        lv_listviewss.setAdapter(adapter);
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
