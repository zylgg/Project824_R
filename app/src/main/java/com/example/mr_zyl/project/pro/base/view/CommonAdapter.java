package com.example.mr_zyl.project.pro.base.view;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by TFHR02 on 2016/10/10.
 */
public abstract class CommonAdapter<T> extends BaseAdapter {
    //上下文
    public Context context;
    //数据源头
    public List<T> lists;
    //内容布局
    public int contentId;


    public CommonAdapter(Context context, int contentId) {
        this.context = context;
        this.contentId = contentId;
    }

    @Override
    public int getCount() {
        return lists.size();
    }

    @Override
    public Object getItem(int i) {
        return lists.get(i);
    }


    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        //调用公共的viewholder处理子view
        CommonViewHolder holder = CommonViewHolder.GetCommonViewHolder(context, view, viewGroup, contentId);
        //抽象方法，用于子类实现，填充数据
        fillData(holder, i);
        return holder.getContentView();
    }

    public abstract void setLists(List<T> datas);

    public abstract void fillData(CommonViewHolder holder, int i);
}
