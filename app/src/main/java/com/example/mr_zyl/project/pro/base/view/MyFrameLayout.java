package com.example.mr_zyl.project.pro.base.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import com.example.mr_zyl.project.pro.essence.view.slidingEvent;
import com.example.mr_zyl.project.utils.DensityUtil;

import de.greenrobot.event.EventBus;

/**
 * Created by TFHR02 on 2018/1/25.
 */
public class MyFrameLayout extends FrameLayout {
    private static final String TAG = "MyFrameLayout";

    private float downX, downy;
    private int left, top, right, bottom;
    boolean is_downSuccess;

    public MyFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        left = DensityUtil.dip2px(getContext(), 12);
        top = DensityUtil.dip2px(getContext(), 12.5f+25);
        right = DensityUtil.dip2px(getContext(), 37);
        bottom = DensityUtil.dip2px(getContext(), 37.5f+25);
    }

    public boolean isIntercept = false;

    public void setIntercept(boolean intercept) {
        isIntercept = intercept;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = ev.getX();
                downy = ev.getY();
                //处理头像事件
                if (downX > left && downX <right&&downy>top&&downy<bottom&&isIntercept) {
                    is_downSuccess = true;
                    return false;
                }
                break;
            case MotionEvent.ACTION_MOVE:

            break;
            case MotionEvent.ACTION_UP:
                //处理头像事件
                if (downX > left && downX <right&&downy>top&&downy<bottom && is_downSuccess) {
                    is_downSuccess = false;
                    slidingEvent event = new slidingEvent();
                    event.setDone(true);
                    EventBus.getDefault().post(event);

                    return true;
                }
        }
        if (isIntercept) {
            return true;
        }else{
            return false;
        }
    }
}
