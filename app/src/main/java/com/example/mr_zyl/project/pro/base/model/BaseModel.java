package com.example.mr_zyl.project.pro.base.model;

import android.content.Context;

import com.example.mr_zyl.project.mvp.model.MvpModel;

/**
 * Created by TFHR02 on 2017/2/22.
 */
public class BaseModel implements MvpModel {
    private Context context;

    public BaseModel(Context context) {
        this.context = context;
    }

    public String getServerUrl() {
        return "http://api.budejie.com";
    }
}
