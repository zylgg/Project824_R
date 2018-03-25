package com.example.mr_zyl.project824.pro.attention.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mr_zyl.project824.R;
import com.example.mr_zyl.project824.pro.attention.bean.attention;
import com.example.mr_zyl.project824.pro.base.view.CommonAdapter;
import com.example.mr_zyl.project824.pro.base.view.CommonViewHolder;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by TFHR02 on 2018/1/31.
 */
public class attentionListAdapter extends CommonAdapter<attention> {

    public attentionListAdapter(Context context,List<attention> Data) {
        super(context, R.layout.attention_item_layout);
        super.lists=Data;
    }

    @Override
    public void setLists(List<attention> datas) {
        super.lists.clear();
        super.lists.addAll(datas);
        notifyDataSetChanged();
    }

    @Override
    public void fillData(CommonViewHolder holder, int position) {
        attention bean = lists.get(position);

        ((TextView)holder.findView(R.id.tv_name)).setText(bean.getTheme_name());
        ((TextView)holder.findView(R.id.tv_attention_count)).setText(bean.getSub_number());

        ImageLoader.getInstance().displayImage(bean.getImage_list(), ((ImageView) holder.findView(R.id.iv_header)));
    }
}
