package com.example.mr_zyl.project.pro.base.view;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by TFHR02 on 2016/10/10.
 */
public class CommonViewHolder {
    //缓存子view的集合
    private SparseArray<View> mViewArrays;
    //item主布局
    private View ContentView;
    public CommonViewHolder(Context context, ViewGroup parent, int contentId) {
        mViewArrays=new SparseArray<View>();
        ContentView= LayoutInflater.from(context).inflate(contentId,null);
        ContentView.setTag(this);
    }
    public static CommonViewHolder GetCommonViewHolder(Context context,View adapterview,ViewGroup parent,int contentId){
        if (adapterview==null){
            CommonViewHolder holder=new CommonViewHolder(context,parent,contentId);
            return  holder;
        }else {
            return (CommonViewHolder) adapterview.getTag();
        }
    }

    //根据holder获取子view
    public <V extends View>V findView(int viewid){
        View catchview= mViewArrays.get(viewid);
        if (catchview==null){
            catchview=getContentView().findViewById(viewid);
            mViewArrays.put(viewid,catchview);
        }
        return (V) catchview;
    }


    public View getContentView() {
        return ContentView;
    }
}
