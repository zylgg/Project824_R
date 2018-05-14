package com.example.mr_zyl.project824.pro.attention.view;

import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import com.example.mr_zyl.project824.R;
import com.example.mr_zyl.project824.mvp.presenter.impl.MvpBasePresenter;
import com.example.mr_zyl.project824.pro.attention.adapter.attentionListAdapter;
import com.example.mr_zyl.project824.pro.attention.bean.attention;
import com.example.mr_zyl.project824.pro.attention.presenter.AttentionListPresenter;
import com.example.mr_zyl.project824.pro.base.presenter.SimpleOnUiThreadListener;
import com.example.mr_zyl.project824.pro.base.view.BaseFragment;
import com.example.mr_zyl.project824.pro.mine.view.SlidingLib.SlideManager;
import com.example.mr_zyl.project824.pro.mine.view.SlidingLib.SlidingContentView;
import com.example.mr_zyl.project824.pro.mine.view.SlidingLib.SlidingItemLayout;
import com.example.mr_zyl.project824.pro.newpost.natvigation.NewpostNavigationBuilder;
import com.example.mr_zyl.project824.utils.VolleyUtils;

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
    private ArrayList<attention> attentions = new ArrayList<>();
    private  attentionListAdapter mListAdapter;
    public Attention() {

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
    int last_click_position = -1;

    @Override
    public void initContentView(View viewContent) {
        ButterKnife.bind(this, viewContent);
        initToolBar(viewContent);
        if (mListAdapter==null)mListAdapter= new attentionListAdapter(Fcontext, attentions);
        mListAdapter.setOnItemClickListener(new SlidingContentView.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (last_click_position != -1) {
                    attention last_b = (attention) lv_attention.getItemAtPosition(last_click_position);
                    last_b.setIs_onClicked(false);
                    mListAdapter.updateSingleRow(lv_attention, last_b.getTheme_name());
                }
                attention b = (attention) lv_attention.getItemAtPosition(position);
                b.setIs_onClicked(true);
                mListAdapter.updateSingleRow(lv_attention, b.getTheme_name());
                
                last_click_position = position;
            }
        });
        lv_attention.setAdapter(mListAdapter);
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
