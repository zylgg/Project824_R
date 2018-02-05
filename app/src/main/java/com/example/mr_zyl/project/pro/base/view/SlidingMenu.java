package com.example.mr_zyl.project.pro.base.view;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * Created by TFHR02 on 2018/1/25.
 */
public class SlidingMenu extends FrameLayout {

    private ViewDragHelper mViewDragHelper;

    private int mHeight;// 当前控件的高度
    private int mWidth;// 当前控件的宽度
    private int mRange; // 菜单移动的距离

    private ViewGroup mMenu;// 菜单内容
    private ViewGroup mContent; // 主页面内容

    private boolean isOpen = false;// 判断是否打开菜单

    public SlidingMenu(Context context) {
        this(context, null);
    }

    public SlidingMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlidingMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        if (getChildCount() < 2) {
            throw new IllegalStateException("使用SlidingMenu中必须包含两个View");
        }
        if (!(getChildAt(0) instanceof ViewGroup && getChildAt(1) instanceof ViewGroup)) {
            throw new IllegalStateException("子View必须是ViewGroup的子类");
        }
        mMenu = (ViewGroup) getChildAt(0);
        mContent = (ViewGroup) getChildAt(1);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mHeight = getMeasuredHeight();
        mWidth = getMeasuredWidth();
        mRange = (int) (mWidth * 0.8);
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return mViewDragHelper.shouldInterceptTouchEvent(ev);
    }


    /**
     * 菜单切换
     */
    public void toggle() {
        if (isOpen) {
            close();
        } else {
            open();
        }
    }

    /**
     * 打开菜单
     */
    private void open() {
        if (mViewDragHelper.smoothSlideViewTo(mContent, mRange, 0)) {// 判断主页面是否滑动到指定位置
            ViewCompat.postInvalidateOnAnimation(this);// 会触发computeScroll
        }
        isOpen = true;
    }

    /**
     * 关闭菜单
     */
    private void close() {
        if (mViewDragHelper.smoothSlideViewTo(mContent, 0, 0)) {// 判断主页面是否滑动到指定位置
            ViewCompat.postInvalidateOnAnimation(this);// 会触发computeScroll
        }
        isOpen = false;
    }

}
