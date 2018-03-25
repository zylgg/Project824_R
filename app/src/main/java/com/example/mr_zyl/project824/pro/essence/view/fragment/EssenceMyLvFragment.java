package com.example.mr_zyl.project824.pro.essence.view.fragment;

import android.os.Handler;
import android.view.View;

import com.example.mr_zyl.project824.R;
import com.example.mr_zyl.project824.pro.base.view.BaseFragment;
import com.example.mr_zyl.project824.pro.essence.view.adapter.MyRefreshLv_Adapter;
import com.example.mr_zyl.project824.pro.essence.view.selfview.PullDownRefresh_ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TFHR02 on 2016/11/30.
 */
public class EssenceMyLvFragment extends BaseFragment {
    private List<String> lists = null;
    private PullDownRefresh_ListView prl_s;
    private MyRefreshLv_Adapter adapter = null;
    private int Refreshing_count = 0;

    @Override
    public int getContentView() {
        return R.layout.essence_mylv_fragment_layout;
    }

    @Override
    public void initContentView(View viewContent) {
        prl_s = (PullDownRefresh_ListView) viewContent.findViewById(R.id.prl_s);
        prl_s.setonRefreshListener(new PullDownRefresh_ListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Refreshing_count += 1;
                        lists.add("新item" + Refreshing_count);
                        adapter.notifyDataSetChanged();

                        prl_s.onRefreshComplete();
                    }
                }, 2000);
            }
        });
    }

    @Override
    public void initData() {
        if (lists == null) {
            lists = new ArrayList<String>();
        }
        if (adapter == null) {
            adapter = new MyRefreshLv_Adapter(lists, getActivity());
        }
        for (int i = 0; i < 30; i++) {
            lists.add("item:" + (i + 1));
        }
        prl_s.setAdapter(adapter);
    }
}
