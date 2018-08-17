package com.example.mr_zyl.project824.pro.base.view.residemenu;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.support.v4.graphics.ColorUtils;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.example.mr_zyl.project824.R;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.view.ViewHelper;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class ResideMenu extends FrameLayout {

    public static final int DIRECTION_LEFT = 0;
    public static final int DIRECTION_RIGHT = 1;
    private static final int PRESSED_MOVE_HORIZONTAL = 2;
    private static final int PRESSED_DOWN = 3;
    private static final int PRESSED_DONE = 4;
    private static final int PRESSED_MOVE_VERTICAL = 5;
    private static final String TAG = "ResideMenu";

    public View scrollViewLeftMenu;
    private Activity activity;
    private TouchDisableView viewActivity;
    private boolean isOpened;
    private List<View> ignoredViews;
    private DisplayMetrics displayMetrics = new DisplayMetrics();
    private OnMenuListener menuListener;
    private float lastRawX;
    private boolean isInIgnoredView = false;
    private int pressedState = PRESSED_DOWN;
    private float mScaleValue = 0.6f;

    private static final int ROTATE_Y_ANGLE = 10;
    private DecimalFormat decimalFormat = new DecimalFormat("#.0000");

    public ResideMenu(Context context) {
        super(context);
        initViews(context, null);
    }

    public ResideMenu(Context context, View customLeftMenuId) {
        super(context);
        initViews(context, customLeftMenuId);
    }

    private void initViews(Context context, View customLeftMenuId) {

        if (customLeftMenuId != null) {
            addView(customLeftMenuId);
            scrollViewLeftMenu = customLeftMenuId;
        } else {
            scrollViewLeftMenu = new View(context);
        }
    }

    public View getLeftMenuView() {
        return scrollViewLeftMenu;
    }

    /**
     * @param activity
     */
    public void attachToActivity(Activity activity, View ll_main_content) {
        this.activity = activity;
        ignoredViews = new ArrayList<>();
        viewActivity = new TouchDisableView(this.activity);
        ViewGroup parent1 = (ViewGroup) ll_main_content.getParent();
        parent1.removeViewAt(0);

        viewActivity.setContent(ll_main_content);

        addView(viewActivity);

        parent1.addView(this);
    }

    /**
     * @return
     */
    public void setMenuListener(OnMenuListener menuListener) {
        this.menuListener = menuListener;
    }


    public OnMenuListener getMenuListener() {
        return menuListener;
    }

    /**
     * Show the menu;
     */
    public void openMenu() {

        isOpened = true;
        AnimatorSet animationOpen_activity = buildOpenAnimation(viewActivity);
        animationOpen_activity.addListener(animationListener);
        animationOpen_activity.start();
    }

    /**
     * Close the menu;
     */
    public void closeMenu() {

        isOpened = false;
        AnimatorSet animationUp_activity = buildUpAnimation(viewActivity);
        animationUp_activity.addListener(animationListener);
        animationUp_activity.start();
    }

    /**
     * 是不是左边边界(当是左边界时可滑动)
     */
    private boolean isLeftBorder=true;

    public void setIsLeftBorder(boolean isBorder) {
        this.isLeftBorder = isBorder;
    }

    /**
     * @return
     */
    public boolean isOpened() {
        return isOpened;
    }

    private Animator.AnimatorListener animationListener = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animation) {
            if (isOpened()) {
                if (menuListener != null)
                    menuListener.openMenu();
            }
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            if (isOpened()) {
                viewActivity.setTouchDisable(TouchDisableView.touchStatusIntercept);
                viewActivity.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (isOpened()) closeMenu();
                    }
                });
                if (menuListener != null) {
                    menuListener.transProgressRadio(1.0f);
                }
            } else {
                viewActivity.setTouchDisable(TouchDisableView.touchStatusNoIntercept);
                viewActivity.setOnClickListener(null);
                if (menuListener != null)
                    menuListener.closeMenu();
                if (menuListener != null) {
                    menuListener.transProgressRadio(0.0f);
                }
            }
        }

        @Override
        public void onAnimationCancel(Animator animation) {

        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }
    };

    /**
     * @param target
     * @return
     */
    private AnimatorSet buildOpenAnimation(View target) {
        final float startFraction = ViewHelper.getTranslationX(target) / (getScreenWidth() * mScaleValue);

        AnimatorSet scaleDown = new AnimatorSet();
        //给位移动画添加更新监听，让背景色渐变
        ObjectAnimator targetTranslationX = ObjectAnimator.ofFloat(target, "translationX", getScreenWidth() * mScaleValue);
        targetTranslationX.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float animatedFraction = valueAnimator.getAnimatedFraction();
                float ratio = startFraction + (1 - startFraction) * animatedFraction;
//                    Log.i(TAG, "ratio: " + ratio);
                if (menuListener != null) {
                    menuListener.transProgressRadio(Float.valueOf(decimalFormat.format(ratio)));
                }
            }
        });
        ObjectAnimator menuTranslationX = ObjectAnimator.ofFloat(scrollViewLeftMenu, "translationX", 0);

        scaleDown.playTogether(targetTranslationX, menuTranslationX);
        scaleDown.setInterpolator(AnimationUtils.loadInterpolator(activity,
                android.R.anim.decelerate_interpolator));

        scaleDown.setDuration(250);
        return scaleDown;
    }

    /**
     * @param target
     * @return
     */
    private AnimatorSet buildUpAnimation(View target) {
        final float startFraction = ViewHelper.getTranslationX(target) / (getScreenWidth() * mScaleValue);

        AnimatorSet scaleClose = new AnimatorSet();
        ObjectAnimator targetTranslationX = ObjectAnimator.ofFloat(target, "translationX", 0);
        targetTranslationX.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float animatedFraction = valueAnimator.getAnimatedFraction();
                float ratio = startFraction * (1 - animatedFraction);
//                    Log.i(TAG, "ratio: " +ratio);
                if (menuListener != null) {
                    menuListener.transProgressRadio(Float.valueOf(decimalFormat.format(ratio)));
                }
            }
        });
        ObjectAnimator menuTranslationX = ObjectAnimator.ofFloat(scrollViewLeftMenu, "translationX", -getScreenWidth() * (1 - mScaleValue));
        scaleClose.playTogether(targetTranslationX, menuTranslationX);
        scaleClose.setInterpolator(AnimationUtils.loadInterpolator(activity,
                android.R.anim.decelerate_interpolator));
        scaleClose.setDuration(250);

        return scaleClose;
    }

    /**
     * @param v
     */
    public void addIgnoredView(View v) {
        ignoredViews.add(v);
    }

    /**
     * @param v
     */
    public void removeIgnoredView(View v) {
        ignoredViews.remove(v);
    }

    /**
     * @param ev
     * @return
     */
    private boolean isInIgnoredView(MotionEvent ev) {
        Rect rect = new Rect();
        for (View v : ignoredViews) {
            v.getGlobalVisibleRect(rect);
            if (rect.contains((int) ev.getX(), (int) ev.getY()))
                return true;
        }
        return false;
    }

    private float getTargetScale(float currentRawX) {
        float scaleFloatX = ((currentRawX - lastRawX) / getScreenWidth()) * 0.75f;

        float targetScale = ViewHelper.getScaleX(viewActivity) - scaleFloatX;
        targetScale = targetScale > 1.0f ? 1.0f : targetScale;
        targetScale = targetScale < (1 - mScaleValue) ? (1 - mScaleValue) : targetScale;
        return targetScale;
    }

    private float getTargetTrans(float currentRawX) {
        float transFloatX = (currentRawX - lastRawX);

        float targetTrans = ViewHelper.getTranslationX(viewActivity) + transFloatX;

        targetTrans = targetTrans < 0 ? 0 : targetTrans;
        targetTrans = targetTrans > getScreenWidth() * mScaleValue ? getScreenWidth() * mScaleValue : targetTrans;
        return targetTrans;
    }

    //按下的坐标
    private float lastActionDownX, lastActionDownY;
    //Velocity
    VelocityTracker mVelocityTracker;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        float currentActivityTransX = 0;
        currentActivityTransX = ViewHelper.getTranslationX(viewActivity);

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastActionDownX = ev.getX();
                lastActionDownY = ev.getY();
                //为了让“忽略布局”在左边菜单关闭时（也可以无论什么时候都自己消费），触摸事件自己消费
                isInIgnoredView = isInIgnoredView(ev) && !isOpened();
                pressedState = PRESSED_DOWN;
                mVelocityTracker = VelocityTracker.obtain();
                if (mVelocityTracker != null) {
                    mVelocityTracker.addMovement(ev);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                //如果是忽略布局，或者是到达左边界，也不执行后续移动判断。
                if (isInIgnoredView || !isLeftBorder){
                    return false;
                }
                if (pressedState != PRESSED_DOWN && pressedState != PRESSED_MOVE_HORIZONTAL)
                    break;
                int xOffset = (int) (ev.getX() - lastActionDownX);
                int yOffset = (int) (ev.getY() - lastActionDownY);

                if (pressedState == PRESSED_DOWN) {
                    if (xOffset <= -8 || xOffset >= 8) {
                        if (Math.abs(xOffset) > Math.abs(yOffset)) {
                            pressedState = PRESSED_MOVE_HORIZONTAL;
                            float targetTrans = getTargetTrans(ev.getRawX());
                            float ratio = targetTrans / (getScreenWidth() * mScaleValue);
                            if (menuListener != null) {
                                menuListener.transProgressRadio(Float.valueOf(decimalFormat.format(ratio)));
                            }
                            viewActivity.setTouchDisable(TouchDisableView.touchStatusIntercept);
                            if (!isOpened() && xOffset <= -8) {//向左滑
                                viewActivity.setTouchDisable(TouchDisableView.touchStatusNoIntercept);
                            }
                            viewActivity.setOnClickListener(null);
                        }
                    }
                } else if (pressedState == PRESSED_MOVE_HORIZONTAL) {
                    if (mVelocityTracker != null) {
                        mVelocityTracker.addMovement(ev);
                    }
                    if (currentActivityTransX > 0.05f * getScreenWidth()) {
//                        showScrollViewMenu(scrollViewMenu);
                    }
                    float targetScale = getTargetScale(ev.getRawX());
                    float targetTrans = getTargetTrans(ev.getRawX());
                    float ratio = targetTrans / (getScreenWidth() * mScaleValue);
//                    Log.i(TAG, "ratio: " + ratio);
                    if (menuListener != null) {
                        menuListener.transProgressRadio(Float.valueOf(decimalFormat.format(ratio)));
                    }

                    ViewHelper.setTranslationX(viewActivity, targetTrans);
                    ViewHelper.setTranslationX(scrollViewLeftMenu, targetTrans * ((1 - mScaleValue) / mScaleValue) - getScreenWidth() * (1 - mScaleValue));
                    lastRawX = ev.getRawX();
                }

                break;

            case MotionEvent.ACTION_UP:
                //如果是忽略布局，也不执行后续回弹
                if (isInIgnoredView) break;
                //如果不是自己滑动抬起，不在继续后续回弹。否则执行后续回弹。
                if (pressedState != PRESSED_MOVE_HORIZONTAL) break;

                pressedState = PRESSED_DONE;
                float slidingBorderValue = 1 * 0.50f;//滑动边界值(大于屏幕一半或者左边布局的一半，随意多一点即可,)

                if (isOpened()) {
                    mVelocityTracker.addMovement(ev);
                    mVelocityTracker.computeCurrentVelocity(1000);
                    float xVelocity = mVelocityTracker.getXVelocity();//如果抬起时速度够快
                    if (currentActivityTransX < slidingBorderValue * getScreenWidth() || xVelocity < -1000) {
                        closeMenu();
                    } else {
                        openMenu();
                    }
                } else {
                    mVelocityTracker.addMovement(ev);
                    mVelocityTracker.computeCurrentVelocity(1000);
                    float xVelocity = mVelocityTracker.getXVelocity();//如果抬起时速度够快
                    if (currentActivityTransX > slidingBorderValue * getScreenWidth() || xVelocity > 1000) {
                        openMenu();
                    } else {
                        closeMenu();
                    }
                }
                releaseVelocityTracker();
                break;
        }
        lastRawX = ev.getRawX();
        return super.onInterceptTouchEvent(ev);
    }

    /**
     * 释放VelocityTracker
     */
    private void releaseVelocityTracker() {
        if (null != mVelocityTracker) {
            mVelocityTracker.clear();
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }

    /**
     * 侧滑菜单切换
     */
    public void toggleMenu() {
        if (isOpened()) {
            closeMenu();
        } else {
            openMenu();
            isLeftBorder=true;
        }
    }

    public int getScreenWidth() {
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    public void setScaleValue(float scaleValue) {
        this.mScaleValue = scaleValue;
    }

    public interface OnMenuListener {
        void openMenu();

        void closeMenu();

        void transProgressRadio(float ratio);
    }
    public static class SimpleOnMenuListener implements  OnMenuListener{

        @Override
        public void openMenu() {

        }

        @Override
        public void closeMenu() {

        }

        @Override
        public void transProgressRadio(float ratio) {

        }
    }
}
