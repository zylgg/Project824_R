package com.example.mr_zyl.project.pro.essence.view.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;


import com.example.mr_zyl.project.pro.essence.view.fragment.EssenceMyLvFragment;
import com.example.mr_zyl.project.pro.essence.view.fragment.EssenceVideoFragment;

import java.util.List;


public class EssenceAdapter extends FragmentStatePagerAdapter {

    public static final String TAB_TAG = "@zylmove@";

    private List<String> mTitles;
    private ShowCloseToolbarListener listener;

    public EssenceAdapter(ShowCloseToolbarListener listener,FragmentManager fm, List<String> titles) {
        super(fm);
        this.listener=listener;
        mTitles = titles;
    }

    @Override
    public Fragment getItem(int position) {
        EssenceVideoFragment fragment = new EssenceVideoFragment(listener);
        String[] title = mTitles.get(position).split(TAB_TAG);
        fragment.setType(Integer.parseInt(title[1]));
        fragment.setTitle(title[0]);
        if (position>=5){
            return new EssenceMyLvFragment();
        }else {
            return fragment;
        }
    }

    @Override
    public int getCount() {
        return mTitles.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles.get(position).split(TAB_TAG)[0];
    }
    public interface ShowCloseToolbarListener{
        void show();
        void Hide();
    }
}
