package com.example.mr_zyl.project824.pro.essence.view.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class MyRefreshLv_Adapter extends BaseAdapter {

	List<String> lists;
	Context context;

	public MyRefreshLv_Adapter(List<String> lists, Context context) {
		super();
		this.lists = lists;
		this.context = context;
	}

	@Override
	public int getCount() {
		// TODO 自动生成的方法存根
		return lists.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO 自动生成的方法存根
		return lists.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO 自动生成的方法存根
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO 自动生成的方法存根
		View view=View.inflate(context,android.R.layout.simple_list_item_1,null);
		TextView tv= (TextView) view.findViewById(android.R.id.text1);
		tv.setText(lists.get(arg0));
		return view;
	}

}
