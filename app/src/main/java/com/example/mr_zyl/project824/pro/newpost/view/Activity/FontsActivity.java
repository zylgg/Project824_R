package com.example.mr_zyl.project824.pro.newpost.view.Activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.widget.TextViewCompat;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mr_zyl.project824.R;
import com.example.mr_zyl.project824.pro.base.view.BaseActivity;
import com.example.mr_zyl.project824.utils.DensityUtil;

import java.util.ArrayList;

import butterknife.BindView;

public class FontsActivity extends BaseActivity {
    @BindView(R.id.ll_fonts_content)
    LinearLayout ll_fonts_content;
    @BindView(R.id.tv_auto_textview)
    TextView tv_auto_textview;
    @BindView(R.id.tv_auto_textview2)
    TextView tv_auto_textview2;

    private String[] mTitles = {"首页", "消息", "联系人", "更多", "首页2", "消息2", "联系人2", "更多2"};
    private int[] mIconUnselectIds = {
            R.mipmap.tab_home_unselect, R.mipmap.tab_speech_unselect,
            R.mipmap.tab_contact_unselect, R.mipmap.tab_more_unselect
//    };
//    private int[] mIconSelectIds = {
            , R.mipmap.tab_home_select, R.mipmap.tab_speech_select,
            R.mipmap.tab_contact_select, R.mipmap.tab_more_select};
    private ArrayList<Fragment> fragments = new ArrayList<>();

    @Override
    protected int initLayoutId() {
        return R.layout.activity_fonts;
    }

    @TargetApi(Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        String price = "＄999.99";
//        String[] newPrice = price.split("\\.");
//
//        SpannableString spannableString = new SpannableString(price);
//        spannableString.setSpan(new AbsoluteSizeSpan(40, true), 0, 1, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
//        spannableString.setSpan(new AbsoluteSizeSpan(40, true), 1, newPrice[0].length(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
//        spannableString.setSpan(new AbsoluteSizeSpan(40, true), newPrice[0].length(), price.length(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
//
//        tv_auto_textview.setText(spannableString);

        TextViewCompat.setAutoSizeTextTypeUniformWithConfiguration(tv_auto_textview, 2, 40, 2, TypedValue.COMPLEX_UNIT_DIP);
        TextViewCompat.setAutoSizeTextTypeUniformWithConfiguration(tv_auto_textview2, 2, 40, 2, TypedValue.COMPLEX_UNIT_DIP);

    }

}
