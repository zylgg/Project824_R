package com.example.mr_zyl.project824.pro.attention.presenter;

import android.content.Context;

import com.example.mr_zyl.project824.pro.attention.bean.attention;
import com.example.mr_zyl.project824.pro.attention.model.AttentionListModel;
import com.example.mr_zyl.project824.pro.base.presenter.BasePresenter;
import com.example.mr_zyl.project824.utils.ToastUtil;
import com.example.mr_zyl.project824.utils.VolleyUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by TFHR02 on 2018/2/1.
 */
public class AttentionListPresenter extends BasePresenter<AttentionListModel> {
    public AttentionListPresenter(Context context) {
        super(context);
    }

    @Override
    public AttentionListModel bindModel() {
        return new AttentionListModel(getContext());
    }

    public void GetAttentionListByModel(String requestTag,JSONObject jsonObject, final OnUiThreadListener<List<attention>> listener) {
        getModel().getAttentionList(requestTag,jsonObject, new VolleyUtils.Callback() {
            @Override
            public void onError(String error) {
                ToastUtil.showToast(getContext(),"网络异常！");
                listener.OnResult(null);
            }

            @Override
            public void onSuccess(String result) {
                List<attention> o = new Gson().fromJson(result, new TypeToken<List<attention>>() {
                }.getType());
                listener.OnResult(o);
            }
        });
    }
}
