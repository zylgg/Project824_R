package com.example.mr_zyl.project824.pro.newpost.view.Activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.widget.TextViewCompat;
import android.text.Layout;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.ImageSpan;
import android.util.TypedValue;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mr_zyl.project824.R;
import com.example.mr_zyl.project824.pro.base.view.BaseActivity;
import com.example.mr_zyl.project824.utils.DensityUtil;
import com.example.mr_zyl.project824.utils.TextDrawUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class FontsActivity extends BaseActivity {
    @BindView(R.id.ll_fonts_content)
    LinearLayout ll_fonts_content;
    @BindView(R.id.tv_auto_textview)
    TextView tv_auto_textview;
    @BindView(R.id.tv_auto_textview2)
    TextView tv_auto_textview2;
    @BindView(R.id.tv_fonts_auto1)
    TextView tv_fonts_auto1;
    @BindView(R.id.tv_fonts_auto2)
    TextView tv_fonts_auto2;
    @BindView(R.id.tv_fonts_auto3)
    TextView tv_fonts_auto3;
    @BindView(R.id.tv_labeledText)
    TextView tv_labeledText;

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

        //自动缩放文字尺寸 以适应控件宽度
        TextViewCompat.setAutoSizeTextTypeUniformWithConfiguration(tv_auto_textview, 2, 40, 2, TypedValue.COMPLEX_UNIT_DIP);
        TextViewCompat.setAutoSizeTextTypeUniformWithConfiguration(tv_auto_textview2, 2, 40, 2, TypedValue.COMPLEX_UNIT_DIP);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            tv_fonts_auto1.setBreakStrategy(Layout.BREAK_STRATEGY_BALANCED);
            tv_fonts_auto2.setBreakStrategy(Layout.BREAK_STRATEGY_HIGH_QUALITY);

            tv_fonts_auto1.setHyphenationFrequency(Layout.HYPHENATION_FREQUENCY_FULL);
            tv_fonts_auto2.setHyphenationFrequency(Layout.HYPHENATION_FREQUENCY_NORMAL);
        }
        //带标签的文本上
        String tag = "自营";
        String kongGe = " ";
        String tag2 = "巨惠专区";
        String text = tv_labeledText.getText().toString();
        String source=tag + kongGe + tag2 + " " + text;

        int measuredWidth = DensityUtil.dip2px(this, 300);
        int shapePadding_LR_Space=DensityUtil.dip2px(this, 16);
        String formatText=TextDrawUtils.refreshText(tv_labeledText.getPaint(),source, measuredWidth,shapePadding_LR_Space).toString();

        SpannableString spannable = new SpannableString(formatText);
        spannable.setSpan(new ImageSpan(TextDrawUtils.getTagDrawable(this, tag, R.drawable.labeled_text_shape)), 0, tag.length(), ImageSpan.ALIGN_BASELINE);

        int tag2Start = tag.length() + kongGe.length();
        spannable.setSpan(new ImageSpan(TextDrawUtils.getTagDrawable(this, tag2, R.drawable.labeled_text_shape2)), tag2Start, tag2Start + tag2.length(), ImageSpan.ALIGN_BASELINE);
        tv_labeledText.setText(spannable);
    }


}
