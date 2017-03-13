package com.example.mr_zyl.project.pro.mine.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by TFHR02 on 2016/11/15.
 */
public class BlurredAdapter extends BaseAdapter {
    List<String> lists;
    Context context;
    public BlurredAdapter(Context context,List<String> lists) {
        this.context=context;
        this.lists = lists;
    }

    @Override
    public int getCount() {
        return lists.size();
    }

    @Override
    public Object getItem(int position) {
        return lists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
       if (convertView==null){
           convertView=View.inflate(context,android.R.layout.simple_list_item_1,null);
       }
        TextView tv= (TextView) convertView.findViewById(android.R.id.text1);
        tv.setText("item"+position);

        return convertView;
    }
}
