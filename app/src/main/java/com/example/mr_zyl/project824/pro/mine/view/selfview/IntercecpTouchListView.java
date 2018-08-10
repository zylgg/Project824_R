package com.example.mr_zyl.project824.pro.mine.view.selfview;

import android.content.Context;
import android.support.v4.view.GestureDetectorCompat;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ListView;

/**
 * Created by TFHR02 on 2017/8/24.
 */
public class IntercecpTouchListView extends ListView {

    public IntercecpTouchListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private float DownX, DownY;
    private boolean is_intercept=true;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                DownX = ev.getX();
                DownY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                int xOffset = (int) (ev.getX() - DownX);
                int yOffset = (int) (ev.getY() - DownY);
                if (Math.abs(xOffset)>Math.abs(yOffset)){
                    is_intercept=false;
                }
                break;
            case MotionEvent.ACTION_UP:
                is_intercept=true;
                break;
        }
        if (is_intercept==false){
            return false;
        }
        return super.dispatchTouchEvent(ev);
    }
}
