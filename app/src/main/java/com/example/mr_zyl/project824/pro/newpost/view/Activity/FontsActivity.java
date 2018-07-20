package com.example.mr_zyl.project824.pro.newpost.view.Activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.example.mr_zyl.project824.R;
import com.example.mr_zyl.project824.pro.base.view.BaseActivity;

import java.util.ArrayList;

import butterknife.BindView;

public class FontsActivity extends BaseActivity {
    @BindView(R.id.ctl_tablist)

    private String[] mTitles = {"首页", "消息", "联系人", "更多"};
    private int[] mIconUnselectIds = {
            R.mipmap.tab_home_unselect, R.mipmap.tab_speech_unselect,
            R.mipmap.tab_contact_unselect, R.mipmap.tab_more_unselect};
    private int[] mIconSelectIds = {
            R.mipmap.tab_home_select, R.mipmap.tab_speech_select,
            R.mipmap.tab_contact_select, R.mipmap.tab_more_select};
    private ArrayList<Fragment> fragments = new ArrayList<>();
    @Override
    protected int initLayoutId() {
        return R.layout.activity_fonts;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

}
