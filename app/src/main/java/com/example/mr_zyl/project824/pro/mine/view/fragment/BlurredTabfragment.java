package com.example.mr_zyl.project824.pro.mine.view.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.mr_zyl.project824.R;
import com.example.mr_zyl.project824.pro.base.view.BaseFragment;
import com.example.mr_zyl.project824.pro.mine.Adapter.BlurredAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by TFHR02 on 2017/9/8.
 */
public class BlurredTabfragment extends BaseFragment {
    @BindView(R.id.rv_blurred_list)
    RecyclerView rv_blurred_list;
    private static final String POSITION_KEY = "postion";

    public static BlurredTabfragment getInstance(int pos) {
        BlurredTabfragment blurredTabfragment = new BlurredTabfragment();
        Bundle bundle = new Bundle();
        bundle.putInt(POSITION_KEY, pos);
        blurredTabfragment.setArguments(bundle);
        return blurredTabfragment;
    }

    @Override
    public int getContentView() {
        return R.layout.blurred_tab_fragment_layout;
    }

    @Override
    public void initContentView(View viewContent) {
        ButterKnife.bind(this, viewContent);
        rv_blurred_list.setLayoutManager(new LinearLayoutManager(Fcontext));
    }

    @Override
    public void initData() {
        Bundle arguments = getArguments();
        int POS = arguments.getInt(POSITION_KEY);
        List<String> lists = new ArrayList<String>();
        for (int i = 0; i < 20; i++) {
            lists.add(i + "-Custom" + POS);
        }
        rv_blurred_list.setAdapter(new BlurredAdapter(Fcontext, lists));
    }
}
