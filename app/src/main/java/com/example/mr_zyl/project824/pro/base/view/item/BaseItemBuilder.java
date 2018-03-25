package com.example.mr_zyl.project824.pro.base.view.item;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Mr_Zyl on 2016/9/16.
 */
public abstract class BaseItemBuilder implements ItemBuilder {
    Context context;
    int contentview;

    public BaseItemBuilder(Context context) {
        this.context=context;
    }

    public Context getContext() {
        return context;
    }

    public abstract int getContentview();

    @Override
    public View BindParentView(ViewGroup parent) {
        View view= LayoutInflater.from(getContext()).inflate(getContentview(),parent,false);
        ViewGroup viewp= (ViewGroup) view.getParent();
        if (viewp!=null){
            viewp.removeView(view);
        }
        parent.addView(view);
        return view;
    }
}
