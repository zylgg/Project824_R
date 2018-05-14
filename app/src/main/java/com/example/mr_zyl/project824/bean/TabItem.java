package com.example.mr_zyl.project824.bean;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mr_zyl.project824.R;

public class TabItem {
    private int imagedefault;
    private int imageselected;
    private int titleid;
    private String titleString;

    private Class<? extends Fragment> fragmentclass;
    private View view;
    private ImageView imageView;
    private TextView textView;
    private Bundle bundle;
    private Context context;

    public TabItem(int imagedefault, int imageselected, int titleid, Class<? extends Fragment> fragmentclass,Context context) {
        this.imagedefault = imagedefault;
        this.imageselected = imageselected;
        this.titleid = titleid;
        this.fragmentclass = fragmentclass;
        this.context=context;
    }

    public int getImagedefault() {
        return imagedefault;
    }

    public int getImageselected() {
        return imageselected;
    }

    public int getTitleid() {
        return titleid;
    }

    public String getTitleString() {
        if (titleid == 0) {
            return "";
        }
        if (TextUtils.isEmpty(titleString)) {
            titleString = context.getResources().getString(titleid);
        }
        return titleString;
    }

    public Class<? extends Fragment> getFragmentclass() {
        return fragmentclass;
    }

    public Bundle getBundle() {
        if (bundle == null) {
            bundle = new Bundle();
        }
        bundle.putString("title", getTitleString());
        return bundle;
    }

    public void setChecked(boolean ischecked) {
        if (imageView != null) {
            if (ischecked) {//选中切换图案背景
                imageView.setImageResource(imageselected);
            } else {
                imageView.setImageResource(imagedefault);
            }
        }
        if (textView != null && titleid != 0) {
            if (ischecked) {//选中切换Tv颜色
                textView.setTextColor(context.getResources().getColor(R.color.main_bottom_text_select));
            } else {
                textView.setTextColor(context.getResources().getColor(R.color.main_bottom_text_normal));
            }
        }
    }

    public View getView() {
        if (this.view == null) {
            this.view = LayoutInflater.from(context).inflate(R.layout.view_tab_indicator, null);
            View ll_tab_indicator_content = view.findViewById(R.id.ll_tab_indicator_content);
            ll_tab_indicator_content.setTag(getTitleString());
            this.imageView = (ImageView) this.view.findViewById(R.id.iv_tab);
            this.textView = (TextView) this.view.findViewById(R.id.tv_tab);
            if (this.titleid == 0) {//目前只针对那个（加号tab）文字隐藏，以后。。
                this.textView.setVisibility(View.GONE);
            } else {
                this.textView.setVisibility(View.VISIBLE);
                this.textView.setText(getTitleString());
            }
            this.imageView.setImageResource(imagedefault);
        }
        return this.view;
    }
}
