package com.example.mr_zyl.project824.pro.mine.view.SlidingLib;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.mr_zyl.project824.R;

import java.util.List;


public class SlideListAdapter extends BaseAdapter {

    private static final String TAG = "SlideListAdapter";
    private Context mContext;
    private LayoutInflater mInflater;

    private SlideManager slideManager;

    private List<listviewbean> lists2;

    public SlideListAdapter(Context mContext, List<listviewbean> lists) {
        super();
        this.mContext = mContext;
        this.lists2 = lists;
        mInflater = LayoutInflater.from(mContext);
        slideManager = new SlideManager();
    }

    public SlideManager getSlideManager() {
        return slideManager;
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

    private SlidingContentView.OnItemClickListener itemClickListener;

    public void setOnItemClickListener(SlidingContentView.OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder mHolder;
        listviewbean beans = lists2.get(position);
        if (convertView == null) {
            mHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.list_item, null, false);
            mHolder.mCancelCall = (Button) convertView.findViewById(R.id.bt_call);
            mHolder.mDeleteCell = (Button) convertView.findViewById(R.id.bt_delete);
            mHolder.tv_listviewss_name = (TextView) convertView.findViewById(R.id.tv_listviewss_name);
            mHolder.tv_listviewss_status = (TextView) convertView.findViewById(R.id.tv_listviewss_status);
            convertView.setTag(mHolder);
        } else {
            mHolder = (ViewHolder) convertView.getTag();
        }
        mHolder.tv_listviewss_name.setText(beans.getName());
        mHolder.tv_listviewss_status.setText(beans.getStatus());

        // 14、第一个问题处理：当滑动视图处于打开状态，点击条目之外其他的位置需要将原来条目关闭？
        // 解决方案：需要在Adapter中给每一个Item绑定点击事件
        final SlidingItemLayout view = (SlidingItemLayout) convertView;
        //默认关闭
        view.closeSlidingLayout(false, false);

        //给我们的滑动视图绑定回调监听（监听生命周期）
        view.setOnSlideItemListener(slideManager.getOnSlideItemListener());
        //一旦你点击了contentView我们立马将原来的已打开的视图关闭
        view.getContentView().setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (slideManager.getUnClosedCount() == 0) {
                    itemClickListener.onItemClick(position);
                    return;
                }
                slideManager.closeAllLayout();
            }
        });
        mHolder.mCancelCall.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                view.close();
            }
        });
        mHolder.mDeleteCell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lists2.remove(position);
                notifyDataSetChanged();
            }
        });
        return view;
    }

    //单行更新，
    public void updateSingleRow(ListView listView, String name) {
        if (listView != null) {
            int start = listView.getFirstVisiblePosition()+1;
            int j = listView.getLastVisiblePosition();
//            Log.i(TAG, "updateSingleRow: " + start + "," + j);
            for (int i = start; i <= j; i++) {
//                Log.i(TAG, "updateSingleRow: " + listView.getItemAtPosition(i));
                if (name == ((listviewbean) listView.getItemAtPosition(i)).getName()) {
                    View view = listView.getChildAt(i - start+1);
                    getView(i-1, view, listView);
                    break;
                }
            }
        }
    }

    class ViewHolder {
        private TextView tv_listviewss_name;
        private TextView tv_listviewss_status;
        public Button mCancelCall;
        public Button mDeleteCell;
    }

}