package com.example.mr_zyl.project.pro.mine.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.mr_zyl.project.R;
import com.example.mr_zyl.project.pro.base.view.BaseActivity;

import java.util.ArrayList;
import java.util.List;

public class ListViewSSActivity extends BaseActivity {

    ListView lv_listviewss;
    List<listviewbean> lists;
    listviewadapter adapter;
    String TAG = "ListViewSSActivity";

    @Override
    protected int initLayoutId() {
        return R.layout.activity_list_view_ss;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        int time = intent.getIntExtra("time", 2000);
        Log.i(TAG, "time:" + time);
        lv_listviewss = (ListView) findViewById(R.id.lv_listviewss);

        lists = new ArrayList<listviewbean>();
        for (int i = 0; i < 20; i++) {
            lists.add(new listviewbean("item" + i, "已经点击"));
        }
        adapter = new listviewadapter(this, lists);
        lv_listviewss.setAdapter(adapter);


        lv_listviewss.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                updatecount = 0;
                listviewbean b = lists.get(position);
                b.setStatus("我已经点击了！");
                adapter.updateSingleRow(lv_listviewss, b.getName());
            }
        });


    }

    int updatecount = 0;

    class listviewadapter extends BaseAdapter {

        private List<listviewbean> lists2;
        private Context contexts;

        listviewadapter(Context context, List<listviewbean> lists) {
            this.contexts = context;
            this.lists2 = lists;
        }

        @Override
        public int getCount() {
            return lists2.size();
        }

        @Override
        public Object getItem(int position) {
            return lists2.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Log.i("demo", "更新次数：" + (updatecount++));
            listviewbean beans = lists2.get(position);
            if (convertView == null) {
                convertView = LayoutInflater.from(contexts).inflate(R.layout.listviewsss_items, parent, false);
            }
            TextView names = (TextView) convertView.findViewById(R.id.tv_listviewss_name);
            TextView status = (TextView) convertView.findViewById(R.id.tv_listviewss_status);

            names.setText(beans.getName());
            status.setText(beans.getStatus());
            return convertView;
        }

        //单行更新，
        private void updateSingleRow(ListView listView, String name) {

            if (listView != null) {
                int start = listView.getFirstVisiblePosition();
                for (int i = start, j = listView.getLastVisiblePosition(); i <= j; i++)
                    if (name == ((listviewbean) listView.getItemAtPosition(i)).getName()) {
                        View view = listView.getChildAt(i - start);
                        getView(i, view, listView);
                        break;
                    }
            }
        }
    }

    class listviewbean {
        private String name;
        private String status;

        public listviewbean(String name, String status) {
            this.name = name;
            this.status = status;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
