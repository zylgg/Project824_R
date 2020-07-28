package com.example.mr_zyl.project824.pro.mine.view;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.view.View;

import com.example.mr_zyl.project824.R;
import com.example.mr_zyl.project824.pro.base.view.BaseFragment;
import com.example.mr_zyl.project824.utils.SystemAppUtils;

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
        viewContent.setPadding(0, SystemAppUtils.getStatusHeight(Fcontext), 0, 0);
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
