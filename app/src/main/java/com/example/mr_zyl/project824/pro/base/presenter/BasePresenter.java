package com.example.mr_zyl.project824.pro.base.presenter;

import android.content.Context;

import com.example.mr_zyl.project824.mvp.presenter.impl.MvpBasePresenter;
import com.example.mr_zyl.project824.pro.base.model.BaseModel;
import com.google.gson.Gson;

/**
 * Created by TFHR02 on 2017/2/22.
 */
public abstract class BasePresenter<M extends BaseModel> extends MvpBasePresenter {
    private Context context;
    private Gson gson;
    private M model;

    public BasePresenter(Context context) {
        this.context = context;
        this.gson = new Gson();
        this.model = bindModel();
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public Gson getGson() {
        return gson;
    }

    public void setGson(Gson gson) {
        this.gson = gson;
    }

    public M getModel() {
        return model;
    }

    public void setModel(M model) {
        this.model = model;
    }

    public abstract M bindModel();

    public interface OnUiThreadListener<T> {
        public void OnBefore();

        public void OnResult(T result);

        public void OnAfter();
    }
}
