package com.example.mr_zyl.project824.pro.mine.view.selfview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

public class MapContainer extends RelativeLayout {

    private ViewGroup scrollView;

    public MapContainer(Context context) {

        super(context);

    }

    public MapContainer(Context context, AttributeSet attrs) {

        super(context, attrs);

    }

    public void setScrollView(ViewGroup scrollView) {

        this.scrollView = scrollView;

    }

    @Override

    public boolean onInterceptTouchEvent(MotionEvent ev) {

        if (ev.getAction() == MotionEvent.ACTION_UP) {

            scrollView.requestDisallowInterceptTouchEvent(false);

        } else {

            scrollView.requestDisallowInterceptTouchEvent(true);

        }

        return false;

    }

    @Override

    public boolean onTouchEvent(MotionEvent event) {

        return true;

    }

}
