package com.example.mr_zyl.project.pro.mine.view;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.example.mr_zyl.project.R;
import com.example.mr_zyl.project.pro.base.view.BaseFragment;

/**
 * Created by Mr_Zyl on 2016/10/10.
 */
public class NewFragment extends BaseFragment {
    @Override
    public int getContentView() {
        return R.layout.newfragmentlayout;
    }

    @Override
    public void initContentView(View viewContent) {
          viewContent.findViewById(R.id.textView).setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  FragmentManager Frmanager = getActivity().getSupportFragmentManager();
                  Fragment newfragment = Frmanager.findFragmentByTag("newfragment");
                  Fragment mineFragment = Frmanager.findFragmentByTag("我");

                  if (newfragment != null && !newfragment.isDetached()) {
                      FragmentTransaction ft = Frmanager.beginTransaction();
                      ft.detach(newfragment);
                      ft.show(mineFragment);
                      ft.commit();
                  }
              }
          });
    }
}
