package com.example.mr_zyl.project824.pro.attention.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.mr_zyl.project824.R;
import com.example.mr_zyl.project824.pro.attention.bean.attention;
import com.example.mr_zyl.project824.pro.base.view.CommonAdapter;
import com.example.mr_zyl.project824.pro.base.view.CommonViewHolder;
import com.example.mr_zyl.project824.pro.mine.view.SlidingLib.SlideManager;
import com.example.mr_zyl.project824.pro.mine.view.SlidingLib.SlidingContentView;
import com.example.mr_zyl.project824.pro.mine.view.SlidingLib.SlidingItemLayout;
import com.example.mr_zyl.project824.pro.mine.view.SlidingLib.listviewbean;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by TFHR02 on 2018/1/31.
 */
public class attentionListAdapter extends CommonAdapter<attention> {

    private SlideManager slideManager;

    public attentionListAdapter(Context context, List<attention> Data) {
        super(context, R.layout.attention_item_layout);
        super.lists = Data;
        slideManager = new SlideManager();
    }

    public SlideManager getSlideManager() {
        return slideManager;
    }

    @Override
    public void setLists(List<attention> datas) {
        super.lists.clear();
        super.lists.addAll(datas);

        notifyDataSetChanged();
    }

    private SlidingContentView.OnItemClickListener itemClickListener;

    public void setOnItemClickListener(SlidingContentView.OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void fillData(CommonViewHolder holder, final int position) {
        attention bean = lists.get(position);

        ((TextView) holder.findView(R.id.tv_name)).setText(bean.getTheme_name());
        ((TextView) holder.findView(R.id.tv_attention_count)).setText(bean.getSub_number());

        ImageLoader.getInstance().displayImage(bean.getImage_list(), ((ImageView) holder.findView(R.id.iv_header)));

        final SlidingItemLayout view = (SlidingItemLayout) holder.getContentView();
        view.setTag(R.id.open_item_tagid,position);

        holder.findView(R.id.sil_attention).setBackgroundResource(bean.isIs_onClicked()?R.drawable.item_build_default_press:R.drawable.item_build_default_normal);

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
                }else{
                    slideManager.closeAllLayout();
                }
            }
        });
        holder.findView(R.id.bt_call).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                view.close();
            }
        });
        holder.findView(R.id.bt_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lists.remove(position);
                notifyDataSetChanged();
            }
        });
    }

    //单行更新，
    public void updateSingleRow(ListView listView, String name) {
        if (listView != null) {
            int start = listView.getFirstVisiblePosition();
            int j = listView.getLastVisiblePosition();
            for (int i = start; i <= j; i++) {
                if (name == ((attention) listView.getItemAtPosition(i)).getTheme_name()) {
                    View view = listView.getChildAt(i - start);
                    getView(i, view, listView);
                    break;
                }
            }
        }
    }
}
