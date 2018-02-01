package com.example.mr_zyl.project.pro.attention.view;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.mr_zyl.project.R;
import com.example.mr_zyl.project.mvp.presenter.impl.MvpBasePresenter;
import com.example.mr_zyl.project.pro.attention.adapter.attentionListAdapter;
import com.example.mr_zyl.project.pro.attention.bean.attention;
import com.example.mr_zyl.project.pro.attention.presenter.AttentionListPresenter;
import com.example.mr_zyl.project.pro.base.presenter.SimpleOnUiThreadListener;
import com.example.mr_zyl.project.pro.base.view.BaseFragment;
import com.example.mr_zyl.project.pro.newpost.natvigation.NewpostNavigationBuilder;
import com.example.mr_zyl.project.utils.VolleyUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Mr_Zyl on 2016/8/25.
 */
public class Attention extends BaseFragment {
    private static final String TAG = "Attention";
    private String listRequestTAG = "listRequestTAG";
    @BindView(R.id.lv_attention)
    ListView lv_attention;
    private AttentionListPresenter attentionListPresenter;
    ArrayList<attention> attentions = new ArrayList<>();

    public Attention(){

    }
    @Override
    public int getContentView() {
        return R.layout.attention;
    }

    @Override
    public MvpBasePresenter bindPresenter() {
        attentionListPresenter = new AttentionListPresenter(Fcontext);
        return attentionListPresenter;
    }

    @Override
    public void initContentView(View viewContent) {
        ButterKnife.bind(this, viewContent);
        initToolBar(viewContent);
        lv_attention.setAdapter(new attentionListAdapter(Fcontext, attentions));
    }

    @Override
    public void initData() {
        requestData();
    }

    private void initToolBar(View viewContent) {
        NewpostNavigationBuilder builder = new NewpostNavigationBuilder(Fcontext);
        builder.setTitle("关注")
                .setBackground(R.drawable.toolbar_background_attention_shape)
                .createAndBind((ViewGroup) viewContent);
    }

    private void requestData() {
        JSONObject params = new JSONObject();
        try {
            params.put("a", "tag_recommend");
            params.put("action", "sub");
            params.put("c", "topic");
            params.put("type", "1");//(0,1)
        } catch (JSONException e) {
            e.printStackTrace();
        }
        attentionListPresenter.GetAttentionListByModel(listRequestTAG, params, new SimpleOnUiThreadListener<List<attention>>() {
            @Override
            public void OnResult(List<attention> list) {
                if (list == null) {
                    return;
                }
                attentions.clear();
                attentions.addAll(list);
                ((attentionListAdapter) lv_attention.getAdapter()).notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        VolleyUtils.getQueue(Fcontext).cancelAll(listRequestTAG);
    }
}
