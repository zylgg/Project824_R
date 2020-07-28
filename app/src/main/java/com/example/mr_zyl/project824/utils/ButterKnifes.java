package com.example.mr_zyl.project824.utils;

import android.view.View;

import com.example.mr_zyl.project824.pro.publish.view.Publish;

public class ButterKnifes {
    public static <T extends View>T findById(View view,int ids){
        return view.findViewById(ids);
    }
}
