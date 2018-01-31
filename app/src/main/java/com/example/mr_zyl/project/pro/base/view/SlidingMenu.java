package com.example.mr_zyl.project.pro.base.view;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.support.v4.widget.ViewDragHelper.Callback;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.nineoldandroids.view.ViewHelper;

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

        // 初始化ViewDragHelper
        mViewDragHelper = ViewDragHelper.create(this, callback);
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
//
//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        return mViewDragHelper.shouldInterceptTouchEvent(ev);
//    }

    @Override
    public boolean onInterceptHoverEvent(MotionEvent event) {
        // 把触摸事件传递给ViewDragHelper
        return mViewDragHelper.shouldInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        try {
            mViewDragHelper.processTouchEvent(event);// 让ViewDrageHelper处理触摸事件
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    private Callback callback = new Callback() {

        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return true;// child：当前被拖拽的view.返回true表示当前view可以被拖拽
        }

        @Override
        public int getViewHorizontalDragRange(View child) {
            return mRange;// 返回拖拽的距离，并不对拖拽进行限制，决定了动画的执行速度
        }

        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) { // 根据建议值，修订水平方向移动的距离
            if (child == mContent) { // ①滑动主页面内容，当超过屏幕预留宽度时，不再滑动。②向左滑动不能为负
                left = fixContentSlidRange(left);
            }
            return left;
        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {//当View的位置改变时调用，可以在此方法中添加一些View特效
            super.onViewPositionChanged(changedView, left, top, dx, dy);
            // 当我们滑动菜单内容时，保持菜单内容不动，转化为主页面内容移动
            int moveContentLeft = left;// 主页面内容左边的距离
            if (changedView == mMenu) {
                moveContentLeft = mContent.getLeft() + left;
                mMenu.layout(0, 0, mMenu.getHeight(), mMenu.getHeight());// 强制菜单不移动
            }
            moveContentLeft = fixContentSlidRange(moveContentLeft);
            mContent.layout(moveContentLeft, 0, moveContentLeft + mContent.getWidth(), mContent.getHeight());

            animShow(moveContentLeft);// 菜单打开时，一些动画

            invalidate();// 重绘界面，兼容低版本
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {// 当view松手时触发，处理自动平滑动画
            super.onViewReleased(releasedChild, xvel, yvel);
            if (xvel > 0) { // 水平速度+
                open();
            } else if (xvel == 0 && mContent.getLeft() > mRange / 2.0f) { // 手指在菜单滑出一半多时抬起
                open();
            } else {
                close();
            }
        }
    };

    /**
     * 修订主页面的滑动距离
     */
    private int fixContentSlidRange(int left) {
        if (left > mRange) {
            return mRange;
        } else if (left < 0) {
            return 0;
        }
        return left;
    }

    /**
     * 伴随动画
     */
    private void animShow(int moveContentLeft) {
        float percent = moveContentLeft * 1.0f / mRange;// 0~1
        /**
         * 分析：
         *  菜单区域：位移动画，缩放动画，渐变动画
         *  内容区域：缩放动画
         *  背景区域：亮度变化
         */
        ViewHelper.setTranslationX(mMenu, evaluate(percent, -mRange / 1.2f, 0));// 位移动画
//        ViewHelper.setScaleX(mMenu, evaluate(percent, 0.6f, 1.0f));// 缩放动画
//        ViewHelper.setScaleY(mMenu, evaluate(percent, 0.6f, 1.0f));
        ViewHelper.setAlpha(mMenu, evaluate(percent, 0.1f, 1.0f));// 渐变动画

//        ViewHelper.setPivotX(mContent, 0);// 缩放中心
//        ViewHelper.setPivotY(mContent, mHeight / 2);
//        ViewHelper.setScaleX(mContent, evaluate(percent, 1.0f, 0.9f));// 缩放动画
//        ViewHelper.setScaleY(mContent, evaluate(percent, 1.0f, 0.9f));

//        getBackground().setColorFilter((Integer)(colorEvaluate(percent, Color.BLACK, Color.TRANSPARENT)), PorterDuff.Mode.SRC_OVER);// 亮度变化
    }

    /**
     * 估值器，根据开始数字和结束数字，随着百分比的变化得到一个数值，详情见FloatEvaluator
     */
    private Float evaluate(float fraction, Number startValue, Number endValue) {
        float startFloat = startValue.floatValue();
        return startFloat + fraction * (endValue.floatValue() - startFloat);
    }

    /**
     * 颜色取值，根据初始颜色和结束颜色，随着百分的变化取出不同的颜色，详情见ArgbEvaluator
     */
    private Object colorEvaluate(float fraction, Object startValue, Object endValue) {
        int startInt = (Integer) startValue;
        int startA = (startInt >> 24) & 0xff;
        int startR = (startInt >> 16) & 0xff;
        int startG = (startInt >> 8) & 0xff;
        int startB = startInt & 0xff;

        int endInt = (Integer) endValue;
        int endA = (endInt >> 24) & 0xff;
        int endR = (endInt >> 16) & 0xff;
        int endG = (endInt >> 8) & 0xff;
        int endB = endInt & 0xff;

        return (int) ((startA + (int) (fraction * (endA - startA))) << 24) |
                (int) ((startR + (int) (fraction * (endR - startR))) << 16) |
                (int) ((startG + (int) (fraction * (endG - startG))) << 8) |
                (int) ((startB + (int) (fraction * (endB - startB))));
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

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mViewDragHelper.continueSettling(true)) {// 持续执行动画，如果返回则表示动画还没有执行完
            ViewCompat.postInvalidateOnAnimation(this);
            if (isOpen) {
                MyLinearLayout mMenu = (MyLinearLayout) this.mMenu;
                mMenu.setIntercept(false);
                MyFrameLayout viewById = (MyFrameLayout) mContent.findViewById(android.R.id.tabcontent);
                viewById.setIntercept(true);
            } else {
                MyLinearLayout mMenu = (MyLinearLayout) this.mMenu;
                mMenu.setIntercept(true);
                MyFrameLayout viewById = (MyFrameLayout) mContent.findViewById(android.R.id.tabcontent);
                viewById.setIntercept(false);
            }
        }
    }
}
