package com.example.mr_zyl.project824.pro.essence.view.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.mr_zyl.project824.pro.essence.OnVisibilityTitleListener;
import com.example.mr_zyl.project824.pro.essence.view.fragment.EssenceMyLvFragment;
import com.example.mr_zyl.project824.pro.essence.view.fragment.EssenceMyTestFragment;
import com.example.mr_zyl.project824.pro.essence.view.fragment.EssenceVideoFragment;

import org.jetbrains.annotations.NotNull;

import java.util.List;


public class EssenceAdapter extends FragmentPagerAdapter {

    public static final String TAB_TAG = "@zylmove@";

    private List<String> mTitles;
    private OnVisibilityTitleListener onVisibilityTitleListener;

    public EssenceAdapter(FragmentManager fm, List<String> titles,OnVisibilityTitleListener listener) {
        super(fm);
        mTitles = titles;
        this.onVisibilityTitleListener=listener;
    }

    @Override
    public Fragment getItem(int position) {
        return getFragment(position);
    }

    @NotNull
    private Fragment getFragment(int position) {
        EssenceVideoFragment fragment = new EssenceVideoFragment();
        String[] title = mTitles.get(position).split(TAB_TAG);
        fragment.setType(Integer.parseInt(title[1]));
        fragment.setTitle(title[0]);
        if (position == 5) {
            return new EssenceMyLvFragment();
        }else if(position==6){
            EssenceMyTestFragment fragment6 = new EssenceMyTestFragment();
            String[] title6 = mTitles.get(4).split(TAB_TAG);
            fragment6.setType(Integer.parseInt(title6[1]));
            fragment6.setTitle(title6[0]);
            return fragment6;
        } else {
            fragment.setOnVisibilityTitleListener(onVisibilityTitleListener);
            return fragment;
        }
    }

    @Override
    public int getCount() {
        return mTitles.size();
    }

}
