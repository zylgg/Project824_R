package com.example.mr_zyl.project.pro.base.view.residemenu;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.mr_zyl.project.R;
import com.example.mr_zyl.project.utils.DisplayUtil;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.view.ViewHelper;

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

    private ImageView imageViewShadow;
    public View scrollViewLeftMenu;
    private View scrollViewRightMenu;
    private View scrollViewMenu;
    private Activity activity;
    private TouchDisableView viewActivity;
    private boolean isOpened;
    private float shadowAdjustScaleX;
    private float shadowAdjustScaleY;
    private List<View> ignoredViews;
    private OnMenuListener menuListener;
    private float lastRawX;
    private boolean isInIgnoredView = false;
    private int scaleDirection = DIRECTION_LEFT;
    private int pressedState = PRESSED_DOWN;
    private List<Integer> disabledSwipeDirection = new ArrayList<Integer>();
    private float mScaleValue = 0.5f;

    private boolean mUse3D;
    private static final int ROTATE_Y_ANGLE = 10;

    public ResideMenu(Context context) {
        super(context);
        initViews(context, -1, -1);
    }

    public ResideMenu(Context context, int customLeftMenuId,
                      int customRightMenuId) {
        super(context);
        initViews(context, customLeftMenuId, customRightMenuId);
    }

    private void initViews(Context context, int customLeftMenuId,
                           int customRightMenuId) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.residemenu_custom, this);

        if (customLeftMenuId >= 0) {
            scrollViewLeftMenu = inflater.inflate(customLeftMenuId, this, false);
        } else {
            scrollViewLeftMenu = new View(context);
        }

        if (customRightMenuId >= 0) {
            scrollViewRightMenu = inflater.inflate(customRightMenuId, this, false);
        } else {
            scrollViewRightMenu = new View(context);
        }

        imageViewShadow = (ImageView) findViewById(R.id.iv_shadow);

        RelativeLayout menuHolder = (RelativeLayout) findViewById(R.id.sv_menu_holder);
        menuHolder.addView(scrollViewLeftMenu);
        menuHolder.addView(scrollViewRightMenu);
    }

    /**
     * Set up the activity;
     *
     * @param activity
     */
    public void attachToActivity(Activity activity, View ll_main_content) {
        initValue(activity, ll_main_content);
        setShadowAdjustScaleXByOrientation();
    }

    private void initValue(Activity activity, View ll_main_content) {
        this.activity = activity;
        ignoredViews = new ArrayList<>();
        viewActivity = new TouchDisableView(this.activity);

        ViewGroup parent1 = (ViewGroup) ll_main_content.getParent();
        parent1.removeViewAt(0);

        viewActivity.setContent(ll_main_content);

        addView(viewActivity);

        ViewGroup parent = (ViewGroup) scrollViewLeftMenu.getParent();
        parent.removeView(scrollViewLeftMenu);
        parent.removeView(scrollViewRightMenu);

        parent1.addView(this, 0);
    }

    private void setShadowAdjustScaleXByOrientation() {
        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            shadowAdjustScaleX = 0.034f;
            shadowAdjustScaleY = 0.12f;
        } else if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            shadowAdjustScaleX = 0.06f;
            shadowAdjustScaleY = 0.07f;
        }
    }

    public void setShadowVisible(boolean isVisible) {
        if (isVisible)
            imageViewShadow.setBackgroundResource(R.drawable.shadow);
        else
            imageViewShadow.setBackgroundResource(0);
    }

    public void setSwipeDirectionDisable(int direction) {
        disabledSwipeDirection.clear();
        disabledSwipeDirection.add(direction);
    }

    /**
     * 是不是左边边界
     */
    private boolean isLeftBorder;

    public void setIsLeftBorder(boolean isBorder) {
        this.isLeftBorder = isBorder;
    }

    private boolean isInDisableDirection(int direction) {
        return disabledSwipeDirection.contains(direction);
    }

    private void setScaleDirection(int direction) {

        int screenWidth = getScreenWidth();
        float pivotX;
        float pivotY = getScreenHeight() * 0.5f;

        if (direction == DIRECTION_LEFT) {
            scrollViewMenu = scrollViewLeftMenu;
            pivotX = screenWidth * 1.5f;
        } else {
            scrollViewMenu = scrollViewRightMenu;
            pivotX = screenWidth * -0.5f;
        }

        if (mUse3D){
            ViewHelper.setPivotX(viewActivity, pivotX);
            ViewHelper.setPivotY(viewActivity, pivotY);
        }

        ViewHelper.setPivotX(imageViewShadow, pivotX);
        ViewHelper.setPivotY(imageViewShadow, pivotY);
        scaleDirection = direction;
    }

    public boolean isOpened() {
        return isOpened;
    }

    private OnClickListener viewActivityOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            if (isOpened()) closeMenu(ViewHelper.getTranslationX(viewActivity),0);
        }
    };

    private Animator.AnimatorListener animationListener = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animation) {
            if (isOpened()) {
                showScrollViewMenu(scrollViewMenu);
                if (menuListener != null)
                    menuListener.openMenu();
            }
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            // reset the view;
            if (isOpened()) {
                viewActivity.setTouchDisable(true);
                viewActivity.setOnClickListener(viewActivityOnClickListener);
            } else {
                viewActivity.setTouchDisable(false);
                viewActivity.setOnClickListener(null);
                hideScrollViewMenu(scrollViewLeftMenu);
                hideScrollViewMenu(scrollViewRightMenu);
                if (menuListener != null)
                    menuListener.closeMenu();
            }
        }

        @Override
        public void onAnimationCancel(Animator animation) {

        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }
    };

    private AnimatorSet buildMenuAnimation(View target, float alpha) {

        AnimatorSet alphaAnimation = new AnimatorSet();
        alphaAnimation.playTogether(
                ObjectAnimator.ofFloat(target, "alpha", alpha)
        );

        alphaAnimation.setDuration(250);
        return alphaAnimation;
    }

    public void addIgnoredView(View v) {
        ignoredViews.add(v);
    }

    public void removeIgnoredView(View v) {
        ignoredViews.remove(v);
    }

    public void clearIgnoredViewList() {
        ignoredViews.clear();
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

    private void setScaleDirectionByRawX(float currentRawX) {
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
        targetScale = targetScale < 0.5f ? 0.5f : targetScale;
        return targetScale;
    }

    private float lastActionDownX, lastActionDownY;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        float currentActivityScaleX = ViewHelper.getScaleX(viewActivity);
        if (currentActivityScaleX == 1.0f)
            setScaleDirectionByRawX(ev.getRawX());

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastActionDownX = ev.getX();
                lastActionDownY = ev.getY();
                isInIgnoredView = isInIgnoredView(ev) && !isOpened();
                pressedState = PRESSED_DOWN;
                break;

            case MotionEvent.ACTION_MOVE:
                if (isInIgnoredView || isInDisableDirection(scaleDirection))
                    break;

                if (pressedState != PRESSED_DOWN &&
                        pressedState != PRESSED_MOVE_HORIZONTAL)
                    break;

                int xOffset = (int) (ev.getX() - lastActionDownX);
                int yOffset = (int) (ev.getY() - lastActionDownY);

                if (pressedState == PRESSED_DOWN) {
                    if (yOffset > 25 || yOffset < -25) {
                        pressedState = PRESSED_MOVE_VERTICAL;
                        break;
                    }
                    if (xOffset < -50 || xOffset > 50) {
                        pressedState = PRESSED_MOVE_HORIZONTAL;
                        ev.setAction(MotionEvent.ACTION_CANCEL);
                    }
                } else if (pressedState == PRESSED_MOVE_HORIZONTAL) {
                    if (currentActivityScaleX < 0.95)
                        showScrollViewMenu(scrollViewMenu);

                    float targetScale = getTargetScale(ev.getRawX());
                    if (mUse3D) {
                        int angle = scaleDirection == DIRECTION_LEFT ? -ROTATE_Y_ANGLE : ROTATE_Y_ANGLE;
                        angle *= (1 - targetScale) * 2;
                        ViewHelper.setRotationY(viewActivity, angle);
                        ViewHelper.setScaleX(viewActivity, targetScale);
                        ViewHelper.setScaleY(viewActivity, targetScale);

                        ViewHelper.setScaleX(imageViewShadow, targetScale - shadowAdjustScaleX);
                        ViewHelper.setScaleY(imageViewShadow, targetScale - shadowAdjustScaleY);
                    } else {
                        ViewHelper.setScaleX(imageViewShadow, targetScale + shadowAdjustScaleX);
                        ViewHelper.setScaleY(imageViewShadow, targetScale + shadowAdjustScaleY);
                    }

                    Log.i(TAG, "targetScale: " + targetScale);

                    float target_trans = ev.getRawX() - lastRawX;
                    float left = ViewHelper.getTranslationX(viewActivity);

                    ViewHelper.setTranslationX(viewActivity, left + target_trans);

                    ViewHelper.setAlpha(scrollViewMenu, (1 - targetScale) * 2.0f);

                    lastRawX = ev.getRawX();
                    return true;
                }

                break;

            case MotionEvent.ACTION_UP:

                if (isInIgnoredView) break;
                if (pressedState != PRESSED_MOVE_HORIZONTAL) break;

                pressedState = PRESSED_DONE;

                float startX = ViewHelper.getTranslationX(viewActivity);

                if (isOpened()) {
                    if (currentActivityScaleX > 0.56f)
                        closeMenu(startX,0);
                    else
                        openMenu(scaleDirection,startX,getScreenWidth());
                } else {
                    if (currentActivityScaleX < 0.94f) {
                        openMenu(scaleDirection,startX,getScreenWidth());
                    } else {
                        closeMenu(startX,0);
                    }
                }

                break;

        }
        lastRawX = ev.getRawX();
        return super.dispatchTouchEvent(ev);
    }


    /**
     * Show the menu;
     */
    public void openMenu(int direction,float startX,float endX) {

        setScaleDirection(direction);

        isOpened = true;
        AnimatorSet scaleDown_activity = buildScaleDownAnimation(viewActivity, mScaleValue, mScaleValue,startX,endX);
        AnimatorSet scaleDown_shadow = buildScaleDownAnimation(imageViewShadow,
                mScaleValue + shadowAdjustScaleX, mScaleValue + shadowAdjustScaleY,startX,endX);
        AnimatorSet alpha_menu = buildMenuAnimation(scrollViewMenu, 1.0f);
        scaleDown_shadow.addListener(animationListener);
        scaleDown_activity.playTogether(scaleDown_shadow);
        scaleDown_activity.playTogether(alpha_menu);
        scaleDown_activity.start();
    }

    /**
     * Close the menu;
     */
    public void closeMenu(float startX,float endX) {

        isOpened = false;
        AnimatorSet scaleUp_activity = buildScaleUpAnimation(viewActivity, 1.0f, 1.0f,startX,endX);
        AnimatorSet scaleUp_shadow = buildScaleUpAnimation(imageViewShadow, 1.0f, 1.0f,startX,endX);
        AnimatorSet alpha_menu = buildMenuAnimation(scrollViewMenu, 0.0f);
        scaleUp_activity.addListener(animationListener);
        scaleUp_activity.playTogether(scaleUp_shadow);
        scaleUp_activity.playTogether(alpha_menu);
        scaleUp_activity.start();
    }


    private AnimatorSet buildScaleDownAnimation(View target, float targetScaleX, float targetScaleY,float startX,float endX) {

        AnimatorSet scaleDown = new AnimatorSet();
        if (mUse3D) {
            scaleDown.playTogether(
                    ObjectAnimator.ofFloat(target, "scaleX", targetScaleX),
                    ObjectAnimator.ofFloat(target, "scaleY", targetScaleY)
            );

            int angle = scaleDirection == DIRECTION_LEFT ? -ROTATE_Y_ANGLE : ROTATE_Y_ANGLE;
            scaleDown.playTogether(ObjectAnimator.ofFloat(target, "rotationY", angle));
        }else{
            scaleDown.playTogether(
                    ObjectAnimator.ofFloat(target, "translationX",startX, endX)
            );
        }
        scaleDown.setInterpolator(AnimationUtils.loadInterpolator(activity,
                android.R.anim.decelerate_interpolator));
        scaleDown.setDuration(250);
        return scaleDown;
    }

    private AnimatorSet buildScaleUpAnimation(View target, float targetScaleX, float targetScaleY,float startX,float endX) {

        AnimatorSet scaleUp = new AnimatorSet();
        if (mUse3D) {
            scaleUp.playTogether(
                    ObjectAnimator.ofFloat(target, "scaleX", targetScaleX),
                    ObjectAnimator.ofFloat(target, "scaleY", targetScaleY)
            );
            scaleUp.playTogether(ObjectAnimator.ofFloat(target, "rotationY", 0));
        }else{
            scaleUp.playTogether(
                    ObjectAnimator.ofFloat(target, "translationX",startX, endX)
            );
        }

        scaleUp.setDuration(250);
        return scaleUp;
    }




    public int getScreenHeight() {
        return DisplayUtil.Height(getContext());
    }

    public int getScreenWidth() {
        return DisplayUtil.Width(getContext());
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
    }

    private void showScrollViewMenu(View scrollViewMenu) {
        if (scrollViewMenu != null && scrollViewMenu.getParent() == null) {
            addView(scrollViewMenu);
        }
    }

    private void hideScrollViewMenu(View scrollViewMenu) {
        if (scrollViewMenu != null && scrollViewMenu.getParent() != null) {
            removeView(scrollViewMenu);
        }
    }
}
