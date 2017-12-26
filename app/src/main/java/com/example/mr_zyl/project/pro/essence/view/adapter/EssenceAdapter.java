package com.example.mr_zyl.project.pro.essence.view.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.mr_zyl.project.pro.essence.view.fragment.EssenceMyLvFragment;
import com.example.mr_zyl.project.pro.essence.view.fragment.EssenceMyTestFragment;
import com.example.mr_zyl.project.pro.essence.view.fragment.EssenceVideoFragment;

import java.util.List;


public class EssenceAdapter extends FragmentStatePagerAdapter {

    public static final String TAB_TAG = "@zylmove@";

    private List<String> mTitles;

    public EssenceAdapter(FragmentManager fm, List<String> titles) {
        super(fm);
        mTitles = titles;
    }

    @Override
    public Fragment getItem(int position) {
        EssenceVideoFragment fragment = new EssenceVideoFragment();
        String[] title = mTitles.get(position).split(TAB_TAG);
        fragment.setType(Integer.parseInt(title[1]));
        fragment.setTitle(title[0]);
        if (position==5){
            return new EssenceMyLvFragment();
        }else if (position==6){
            EssenceMyTestFragment fragment2 = new EssenceMyTestFragment();
            String[] title2 = mTitles.get(4).split(TAB_TAG);
            fragment2.setType(Integer.parseInt(title2[1]));
            fragment2.setTitle(title2[0]);
            return fragment2;
        }else {
            return fragment;
        }
    }

    @Override
    public int getCount() {
        return mTitles.size();
    }

//    @Override
//    public CharSequence getPageTitle(int position) {
//        return mTitles.get(position).split(TAB_TAG)[0];
//    }
}
