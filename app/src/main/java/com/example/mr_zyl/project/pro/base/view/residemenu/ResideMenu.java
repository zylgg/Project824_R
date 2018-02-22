package com.example.mr_zyl.project.pro.base.view.residemenu;

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

import com.example.mr_zyl.project.R;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.view.ViewHelper;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * User: special
 * Date: 13-12-10
 * Time: 下午10:44
 * Mail: specialcyci@gmail.com
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
    private int scaleDirection = DIRECTION_LEFT;
    private int pressedState = PRESSED_DOWN;
    private List<Integer> disabledSwipeDirection = new ArrayList<Integer>();
    private float mScaleValue = 0.6f;

    private boolean mUse3D;
    private static final int ROTATE_Y_ANGLE = 10;
    private int color1, color2;
    private DecimalFormat decimalFormat = new DecimalFormat("#.0000");

    public ResideMenu(Context context) {
        super(context);
        initViews(context, null);
    }

    public ResideMenu(Context context, View customLeftMenuId) {
        super(context);
        initViews(context, customLeftMenuId);
        color1 = getResources().getColor(R.color.transparent);
        color2 = getResources().getColor(R.color.transparent2);
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

    private ImageView iv_main_background;

    /**
     * @param activity
     */
    public void attachToActivity(Activity activity, View ll_main_content) {
        this.activity = activity;
        ignoredViews = new ArrayList<>();
        viewActivity = new TouchDisableView(this.activity);
        ViewGroup parent1 = (ViewGroup) ll_main_content.getParent();
        parent1.removeViewAt(0);

        iv_main_background = (ImageView) ll_main_content.findViewById(R.id.iv_main_background);
        iv_main_background.setBackgroundColor(color1);

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
    public void openMenu(int direction) {
        setScaleDirection(direction);

        isOpened = true;
        AnimatorSet animationOpen_activity = buildOpenAnimation(viewActivity, mScaleValue, mScaleValue);
        animationOpen_activity.addListener(animationListener);
//        scaleDown_activity.playTogether(scaleDown_shadow);
        animationOpen_activity.start();
    }

    /**
     * Close the menu;
     */
    public void closeMenu() {

        isOpened = false;
        AnimatorSet animationUp_activity = buildUpAnimation(viewActivity, 1.0f);
        animationUp_activity.addListener(animationListener);
        animationUp_activity.start();
    }

    public void setSwipeDirectionDisable(int direction) {
        disabledSwipeDirection.add(direction);
    }

    private boolean isInDisableDirection(int direction) {
        return disabledSwipeDirection.contains(direction);
    }

    /**
     * 是不是左边边界
     */
    private boolean isLeftBorder;

    public void setIsLeftBorder(boolean isBorder) {
        this.isLeftBorder = isBorder;
    }

    private void setScaleDirection(int direction) {

        int screenWidth = getScreenWidth();
        float pivotX;
        float pivotY = getScreenHeight() * 0.5f;

        if (direction == DIRECTION_LEFT) {
            pivotX = screenWidth * 1.5f;
        } else {
            pivotX = screenWidth * -0.5f;
        }
        if (mUse3D) {
            ViewHelper.setPivotX(viewActivity, pivotX);
            ViewHelper.setPivotY(viewActivity, pivotY);
        }
        scaleDirection = direction;
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
                viewActivity.setTouchDisable(true);
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
                viewActivity.setTouchDisable(false);
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
     * @param targetScaleX
     * @param targetScaleY
     * @return
     */
    private AnimatorSet buildOpenAnimation(View target, float targetScaleX, float targetScaleY) {
        final float startFraction = ViewHelper.getTranslationX(target) / (getScreenWidth() * mScaleValue);
        final int startColor = ColorUtils.blendARGB(color1, color2, startFraction);

        AnimatorSet scaleDown = new AnimatorSet();
        if (mUse3D) {
            scaleDown.playTogether(
                    ObjectAnimator.ofFloat(target, "scaleX", targetScaleX),
                    ObjectAnimator.ofFloat(target, "scaleY", targetScaleY)
            );
            int angle = scaleDirection == DIRECTION_LEFT ? -ROTATE_Y_ANGLE : ROTATE_Y_ANGLE;
            scaleDown.playTogether(ObjectAnimator.ofFloat(target, "rotationY", angle));
        } else {
            //给位移动画添加更新监听，让背景色渐变
            ObjectAnimator targetTranslationX = ObjectAnimator.ofFloat(target, "translationX", getScreenWidth() * mScaleValue);
            targetTranslationX.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    float animatedFraction = valueAnimator.getAnimatedFraction();
                    float ratio=startFraction + (1 - startFraction) * animatedFraction;
//                    Log.i(TAG, "ratio: " + ratio);
                    if (menuListener != null) {
                        menuListener.transProgressRadio(Float.valueOf(decimalFormat.format(ratio)));
                    }
                    iv_main_background.setBackgroundColor(ColorUtils.blendARGB(startColor, color2, animatedFraction));
                }
            });
            ObjectAnimator menuTranslationX = ObjectAnimator.ofFloat(scrollViewLeftMenu, "translationX", 0);

            scaleDown.playTogether(targetTranslationX, menuTranslationX);
        }
        scaleDown.setInterpolator(AnimationUtils.loadInterpolator(activity,
                android.R.anim.decelerate_interpolator));

        scaleDown.setDuration(250);
        return scaleDown;
    }

    /**
     * @param target
     * @param targetScale
     * @return
     */
    private AnimatorSet buildUpAnimation(View target, float targetScale) {
        final float startFraction = ViewHelper.getTranslationX(target) / (getScreenWidth() * mScaleValue);
        final int startColor = ColorUtils.blendARGB(color1, color2, startFraction);

        AnimatorSet scaleClose = new AnimatorSet();
        if (mUse3D) {
            scaleClose.playTogether(ObjectAnimator.ofFloat(target, "rotationY", 0));
            scaleClose.playTogether(
                    ObjectAnimator.ofFloat(target, "scaleX", targetScale),
                    ObjectAnimator.ofFloat(target, "scaleY", targetScale));
        } else {
            ObjectAnimator targetTranslationX = ObjectAnimator.ofFloat(target, "translationX", 0);
            targetTranslationX.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    float animatedFraction = valueAnimator.getAnimatedFraction();
                    float ratio=startFraction * (1 - animatedFraction);
//                    Log.i(TAG, "ratio: " +ratio);
                    if (menuListener != null) {
                        menuListener.transProgressRadio(Float.valueOf(decimalFormat.format(ratio)));
                    }
                    iv_main_background.setBackgroundColor(ColorUtils.blendARGB(startColor, color1, animatedFraction));
                }
            });
            ObjectAnimator menuTranslationX = ObjectAnimator.ofFloat(scrollViewLeftMenu, "translationX", -getScreenWidth() * (1 - mScaleValue));
            scaleClose.playTogether(targetTranslationX, menuTranslationX);
        }
        scaleClose.setInterpolator(AnimationUtils.loadInterpolator(activity,
                android.R.anim.decelerate_interpolator));
        scaleClose.setDuration(250);

        iv_main_background.setBackgroundColor(color1);
        return scaleClose;
    }

    private AnimatorSet buildMenuAnimation(View target, float alpha) {

        AnimatorSet alphaAnimation = new AnimatorSet();
        alphaAnimation.playTogether(
                ObjectAnimator.ofFloat(target, "alpha", alpha)
        );

        alphaAnimation.setDuration(250);
        return alphaAnimation;
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

    private void setDirectionByRawX(float currentRawX) {
        if (currentRawX < lastRawX || !isLeftBorder)//如果是向左滑动 或 viewpager不是在第一项
            setScaleDirection(DIRECTION_RIGHT);
        else
            setScaleDirection(DIRECTION_LEFT);
    }

    private float getTargetScale(float currentRawX) {
        float scaleFloatX = ((currentRawX - lastRawX) / getScreenWidth()) * 0.75f;
        scaleFloatX = scaleDirection == DIRECTION_RIGHT ? -scaleFloatX : scaleFloatX;

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
    public boolean dispatchTouchEvent(MotionEvent ev) {
        float currentActivityScaleX = 0, currentActivityTransX = 0;
        if (mUse3D) {
            currentActivityScaleX = ViewHelper.getScaleX(viewActivity);
            if (currentActivityScaleX == 1.0f)
                setDirectionByRawX(ev.getRawX());
        } else {
            currentActivityTransX = ViewHelper.getTranslationX(viewActivity);
            if (currentActivityTransX == 0)
                setDirectionByRawX(ev.getRawX());
        }
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
                //如果是忽略布局，也不执行后续移动判断。
                if (isInIgnoredView || isInDisableDirection(scaleDirection))
                    break;
                if (pressedState != PRESSED_DOWN && pressedState != PRESSED_MOVE_HORIZONTAL)
                    break;
                int xOffset = (int) (ev.getX() - lastActionDownX);
                int yOffset = (int) (ev.getY() - lastActionDownY);

                if (pressedState == PRESSED_DOWN) {
                    if (yOffset >= 8 || yOffset <= -8) {
                        pressedState = PRESSED_MOVE_VERTICAL;
                        if (isOpened() && lastActionDownX > getScreenWidth() * mScaleValue) {//如果触摸了空白区域
                            ev.setAction(MotionEvent.ACTION_CANCEL);//取消事件
                        }
                    }
                    if (xOffset <= -8 || xOffset >= 8) {
                        if (Math.abs(xOffset) > Math.abs(yOffset)) {
                            pressedState = PRESSED_MOVE_HORIZONTAL;
                            float targetTrans = getTargetTrans(ev.getRawX());
                            float ratio = targetTrans / (getScreenWidth() * mScaleValue);
//                            Log.i(TAG, "ratio: " + ratio);
                            if (menuListener != null) {
                                menuListener.transProgressRadio(Float.valueOf(decimalFormat.format(ratio)));
                            }
                            ev.setAction(MotionEvent.ACTION_CANCEL);//达到侧滑条件后，不往里分发事件。让自己下一次开始侧滑
                        }
                    }
                } else if (pressedState == PRESSED_MOVE_HORIZONTAL) {
                    if (mVelocityTracker != null) {
                        mVelocityTracker.addMovement(ev);
                    }
                    if (mUse3D && currentActivityScaleX < 0.95) {
//                        showScrollViewMenu(scrollViewMenu);
                    } else if (currentActivityTransX > 0.05f * getScreenWidth()) {
//                        showScrollViewMenu(scrollViewMenu);
                    }
                    float targetScale = getTargetScale(ev.getRawX());
                    float targetTrans = getTargetTrans(ev.getRawX());
                    float ratio = targetTrans / (getScreenWidth() * mScaleValue);
//                    Log.i(TAG, "ratio: " + ratio);
                    if (menuListener != null) {
                        menuListener.transProgressRadio(Float.valueOf(decimalFormat.format(ratio)));
                    }

                    iv_main_background.setBackgroundColor(ColorUtils.blendARGB(color1, color2, ratio));

                    if (mUse3D) {
                        int angle = scaleDirection == DIRECTION_LEFT ? -ROTATE_Y_ANGLE : ROTATE_Y_ANGLE;
                        angle *= (1 - targetScale) * 2;
                        ViewHelper.setRotationY(viewActivity, angle);
                        ViewHelper.setScaleX(viewActivity, targetScale);
                        ViewHelper.setScaleY(viewActivity, targetScale);
                    } else {
                        ViewHelper.setTranslationX(viewActivity, targetTrans);
                        ViewHelper.setTranslationX(scrollViewLeftMenu, targetTrans * ((1 - mScaleValue) / mScaleValue) - getScreenWidth() * (1 - mScaleValue));
                    }
                    lastRawX = ev.getRawX();
                    return true;
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
                    if (mUse3D) {
//                        Log.i(TAG, isOpened+"currentActivityScaleX: "+currentActivityScaleX);
                        if (currentActivityScaleX > 0.75f) closeMenu();
                        else openMenu(scaleDirection);
                    } else {
                        mVelocityTracker.addMovement(ev);
                        mVelocityTracker.computeCurrentVelocity(1000);
                        float xVelocity = mVelocityTracker.getXVelocity();//如果抬起时速度够快
                        if (currentActivityTransX < slidingBorderValue * getScreenWidth() || Math.abs(xVelocity) > 1000) {
                            closeMenu();
                        } else {
                            openMenu(scaleDirection);
                        }
                    }
                } else {
                    if (mUse3D) {
//                        Log.i(TAG, isOpened+"currentActivityScaleX: "+currentActivityScaleX);
                        if (currentActivityScaleX < 0.75f) openMenu(scaleDirection);
                        else closeMenu();
                    } else {
                        mVelocityTracker.addMovement(ev);
                        mVelocityTracker.computeCurrentVelocity(1000);
                        float xVelocity = mVelocityTracker.getXVelocity();//如果抬起时速度够快
                        if (currentActivityTransX > slidingBorderValue * getScreenWidth() || Math.abs(xVelocity) > 1000) {
                            openMenu(scaleDirection);
                        } else {
                            closeMenu();
                        }
                    }
                }
                releaseVelocityTracker();
                break;
            case MotionEvent.ACTION_CANCEL:
                releaseVelocityTracker();
                break;

        }
        lastRawX = ev.getRawX();
        return super.dispatchTouchEvent(ev);
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
            openMenu(scaleDirection);
        }
    }

    public int getScreenHeight() {
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    public int getScreenWidth() {
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    public void setScaleValue(float scaleValue) {
        this.mScaleValue = scaleValue;
    }

    public void setUse3D(boolean use3D) {
        mUse3D = use3D;
    }

    public interface OnMenuListener {
        void openMenu();

        void closeMenu();

        void transProgressRadio(float ratio);
    }
}
