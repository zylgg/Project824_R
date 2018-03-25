package com.example.mr_zyl.project824.pro.mine.view.selfview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by TFHR02 on 2017/8/24.
 */
public class MyListView  extends ListView{

    public MyListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        heightMeasureSpec=MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE>>2,MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
