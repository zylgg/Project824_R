package com.example.mr_zyl.project.pro.base.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

/**
 * Created by TFHR02 on 2018/1/25.
 */
public class MyFrameLayout extends FrameLayout {
    public MyFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public boolean isIntercept;

    public void setIntercept(boolean intercept) {
        isIntercept = intercept;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    private float down_x,down_y;
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (isIntercept) {
            return true;
        }
//        switch (ev.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                down_x = ev.getX();
//                down_y=ev.getY();
//                break;
//            case MotionEvent.ACTION_MOVE:
//                float move_x=ev.getX();
//                float move_y=ev.getY();
//                if ((move_y-down_y)<(move_x-down_x)){
//                    return true;
//                }else{
//                    return false;
//                }
//            case MotionEvent.ACTION_UP:
//                return false;
//        }
        return false;
    }
}
