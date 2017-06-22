package com.example.mr_zyl.project.pro.base.view.item;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mr_zyl.project.R;

/**
 * Created by Mr_Zyl on 2016/9/16.
 */
public class DefaultImpleItemBuilder extends BaseItemBuilder {

    /**
     * 左边的图标资源ID
     */
    private int LeftIconRes;
    /**
     * 文本内容
     */
    private String Text;
    /**
     * 标题
     */
    private TextView tv_title;
    /**
     * 左边的图标
     */
    private ImageView iv_left;
    private View.OnClickListener OnClickListener;
    public DefaultImpleItemBuilder(Context context) {
        super(context);
    }

    @Override
    public int getContentview() {
        return R.layout.item_build_default;
    }

    public DefaultImpleItemBuilder setLeftIcons(int iconid) {
        this.LeftIconRes = iconid;
        return this;
    }

    public DefaultImpleItemBuilder setTitleText(String string) {
        this.Text = string;
        return this;
    }

    public DefaultImpleItemBuilder setOnClickListener(View.OnClickListener OnClickListener) {
        this.OnClickListener = OnClickListener;
        return this;
    }
    public TextView getTv_title() {
        return tv_title;
    }
    public ImageView getIv_left() {
        return iv_left;
    }

    @Override
    public View BindParentView(ViewGroup parent) {
        View contentview = super.BindParentView(parent);
        iv_left = (ImageView) contentview.findViewById(R.id.iv_left);
        tv_title = (TextView) contentview.findViewById(R.id.tv_title);

        if (LeftIconRes >= 0) {
            iv_left.setImageResource(LeftIconRes);
            iv_left.setVisibility(View.VISIBLE);
        } else {
            iv_left.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(Text)) {
            tv_title.setText(Text);
            tv_title.setVisibility(View.VISIBLE);
            contentview.setTag(Text);
        } else {
            tv_title.setVisibility(View.INVISIBLE);
        }
        if (OnClickListener != null) {
            contentview.setOnClickListener(OnClickListener);
        }
        return contentview;
    }
}
