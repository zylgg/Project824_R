package com.example.mr_zyl.project824.pro.newpost.view.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class Font_Fragment1 extends Fragment {
    public static Font_Fragment1 getInstance(String title){
        Font_Fragment1 font_fragment1=new Font_Fragment1();
        Bundle bundle=new Bundle();
        bundle.putString("title",""+title);
        font_fragment1.setArguments(bundle);
        return font_fragment1;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(android.R.layout.simple_list_item_1,null);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        TextView text1 = view.findViewById(android.R.id.text1);
        text1.setText(arguments.getString("title"));
    }
}
