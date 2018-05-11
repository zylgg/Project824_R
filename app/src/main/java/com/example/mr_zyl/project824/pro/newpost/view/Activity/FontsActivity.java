package com.example.mr_zyl.project824.pro.newpost.view.Activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.example.mr_zyl.project824.R;
import com.example.mr_zyl.project824.pro.base.view.BaseActivity;
import com.example.mr_zyl.project824.pro.mine.bean.CustomTabEntity;
import com.example.mr_zyl.project824.pro.mine.bean.TabEntity;
import com.example.mr_zyl.project824.pro.mine.view.selfview.CommonTabLayout;

import java.util.ArrayList;

import butterknife.BindView;

public class FontsActivity extends BaseActivity {
    @BindView(R.id.ctl_tablist)
    CommonTabLayout ctl_tablist;

    private String[] mTitles = {"首页", "消息", "联系人", "更多","首页2", "消息2", "联系人2", "更多2"};
    private int[] mIconUnselectIds = {
            R.mipmap.tab_home_unselect, R.mipmap.tab_speech_unselect,
            R.mipmap.tab_contact_unselect, R.mipmap.tab_more_unselect,
            R.mipmap.tab_home_unselect, R.mipmap.tab_speech_unselect,
            R.mipmap.tab_contact_unselect, R.mipmap.tab_more_unselect};
    private int[] mIconSelectIds = {
            R.mipmap.tab_home_select, R.mipmap.tab_speech_select,
            R.mipmap.tab_contact_select, R.mipmap.tab_more_select,
            R.mipmap.tab_home_select, R.mipmap.tab_speech_select,
            R.mipmap.tab_contact_select, R.mipmap.tab_more_select};
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    private ArrayList<Fragment> fragments = new ArrayList<>();
    @Override
    protected int initLayoutId() {
        return R.layout.activity_fonts;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        for (int i=0;i<mTitles.length;i++){
            fragments.add(Font_Fragment1.getInstance(mTitles[i]));
        }
        for (int i = 0; i < mTitles.length; i++) {
            mTabEntities.add(new TabEntity(mTitles[i], mIconSelectIds[i], mIconUnselectIds[i]));
        }
        ctl_tablist.setTabData(mTabEntities,this,R.id.ll_fonts_content,fragments);
    }

}
