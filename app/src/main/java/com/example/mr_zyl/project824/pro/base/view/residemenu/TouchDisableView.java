package com.example.mr_zyl.project824.pro.base.view.residemenu;


import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

/**
 * 控制MainActivity 是否可触摸的布局
 */
public class TouchDisableView extends FrameLayout {

    private View mContent;

    public static final int touchStatusIntercept=1;
    public static final int touchStatusNoIntercept=0;
    public static final int touchStatusBySuper=-1;

    private int  mTouchDisabled = 0;

    public TouchDisableView(Context context) {
        this(context, null);
    }

    public TouchDisableView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    public void setContent(View v) {
        if (mContent != null) {
            this.removeView(mContent);
        }
        mContent = v;
        addView(mContent);
    }

    public View getContent() {
        return mContent;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int width = getDefaultSize(0, widthMeasureSpec);
        int height = getDefaultSize(0, heightMeasureSpec);
        setMeasuredDimension(width, height);

        final int contentWidth = getChildMeasureSpec(widthMeasureSpec, 0, width);
        final int contentHeight = getChildMeasureSpec(heightMeasureSpec, 0, height);
        mContent.measure(contentWidth, contentHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int width = r - l;
        final int height = b - t;
        mContent.layout(0, 0, width, height);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (mTouchDisabled==1){
            return true;
        }else if (mTouchDisabled==0){
            return false;
        }
        return super.onInterceptTouchEvent(ev);
    }

    public void setTouchDisable(int disableTouch) {
        mTouchDisabled = disableTouch;
    }

    public int getTouchDisabled() {
        return mTouchDisabled;
    }
}
