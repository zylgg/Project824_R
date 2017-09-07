package com.example.mr_zyl.project.pro.mine.view.selfview;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mr_zyl.project.R;
import com.example.mr_zyl.project.pro.mine.view.impl.ThemeResolver;

/**
 * Created by TFHR02 on 2017/9/6.
 */
public class MySnackbar extends LinearLayout {

    private Animation slideInAnimation;
    private Animation slideOutAnimation;
    private View contentview;

    private TextView tvTitle;
    private TextView tvMessage;
    private ImageView ivIcon;
    private TextView btnAction;
    private ImageView btnActionWithIcon;
    private long duration = 2000;
    private int layoutGravity = Gravity.BOTTOM;
    private ViewDragHelper viewDragHelper;
    private GestureDetectorCompat detectorCompat;

    private int horizontalDX;


    public MySnackbar(@NonNull final Context context) {
        this(context, null);
    }

    public MySnackbar(@NonNull final Context context, @Nullable final AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MySnackbar(@NonNull final Context context, @Nullable final AttributeSet attrs,
                      final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews(context);
    }

    public int getLayoutGravity() {
        return layoutGravity;
    }

    private void initViews(Context context) {
        setOrientation(LinearLayout.VERTICAL);
        setGravity(Gravity.BOTTOM);
        inflate(getContext(), R.layout.layout_cookie, this);
        contentview = findViewById(R.id.cookie);

        viewDragHelper = ViewDragHelper.create(this, dragCallback);
        detectorCompat = new GestureDetectorCompat(getContext(),
                onGestureListener);

        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvMessage = (TextView) findViewById(R.id.tv_message);
        ivIcon = (ImageView) findViewById(R.id.iv_icon);
        btnAction = (TextView) findViewById(R.id.btn_action);
        btnActionWithIcon = (ImageView) findViewById(R.id.btn_action_with_icon);
        initDefaultStyle(context);
    }

    private GestureDetector.OnGestureListener onGestureListener = new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2,
                                float distanceX, float distanceY) {
            return Math.abs(distanceX) >= Math.abs(distanceY);
        }
    };

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                viewDragHelper.cancel();
                break;
        }
        return viewDragHelper.shouldInterceptTouchEvent(ev) & detectorCompat.onTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        viewDragHelper.processTouchEvent(event);
        return true;
    }

    private ViewDragHelper.Callback dragCallback = new ViewDragHelper.Callback() {

        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return child.getId() == contentview.getId();
        }

        /**
         * 设置滑动偏移量
         */
//        @Override
//        public int getViewHorizontalDragRange(View child) {
//            horizontalDX = child.getWidth();
//            return horizontalDX;
//        }

        /**
         * 控制滚动的范围
         */
        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            if (left < getPaddingLeft()) {
                return getPaddingLeft();
            }

            if (left > getWidth() - child.getMeasuredWidth()) {
                return getWidth() - child.getMeasuredWidth();
            }
            return left;
        }

        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            //两个if主要是让view在ViewGroup中
            if (top < getPaddingTop()) {
                return getPaddingTop();
            }

            if (top > getHeight() - child.getMeasuredHeight()) {
                return getHeight() - child.getMeasuredHeight();
            }

            return top;
        }

        /**
         * 拖拽视图的时候，希望能够同时干一些其他事
         */
//        @Override
//        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
//            float distance = fraction(0.0f * getWidth(), 1.0f * getWidth(), left);
//            ViewCompat.setAlpha(changedView, distance);
//
//            contentview.offsetLeftAndRight(dx);
//            contentview.offsetTopAndBottom(dy);
//
//            invalidate();
//        }

        /**
         * 我们拖拽手势弹起
         //         */
//        @Override
//        public void onViewReleased(View releasedChild, float xvel, float yvel) {
        // 根据速度取判断
//            if (xvel == 0) {
//                if (releasedChild.getLeft() > releasedChild.getWidth() * 0.5f) {
//                    // 打开状态---打开滑动视图
//                    close();
//                } else {
//                    open();
//                }
//            } else if (xvel > 0) {
//                close();
//            } else {
//                open();
//            }
//            invalidate();
//        }
    };


    public void close() {
        //关闭
        if (viewDragHelper.smoothSlideViewTo(this, this.getLeft(), this.getHeight())) {
            ViewCompat.postInvalidateOnAnimation(this);
//            destroy();
        }
    }

    public void open() {
        //打开
        if (viewDragHelper.smoothSlideViewTo(this, 0, this.getHeight())) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }


    private float fraction(float startValue, float endValue, float value) {
        return (value - startValue) / (endValue - startValue);
    }


    private void initDefaultStyle(Context context) {
        int titleColor = ThemeResolver.getColor(context, R.attr.cookieTitleColor, Color.WHITE);
        int messageColor = ThemeResolver.getColor(context, R.attr.cookieMessageColor, Color.WHITE);
        int actionColor = ThemeResolver.getColor(context, R.attr.cookieActionColor, Color.WHITE);
        int backgroundColor = ThemeResolver.getColor(context, R.attr.cookieBackgroundColor,
                ContextCompat.getColor(context, R.color.default_bg_color));

        tvTitle.setTextColor(titleColor);
        tvMessage.setTextColor(messageColor);
        btnAction.setTextColor(actionColor);
//        contentview.setBackgroundColor(backgroundColor);
        setBackgroundResource(R.drawable.rect_shape);
    }

    public void setParams(final MySnackbarUtils.Params params) {
        if (params != null) {
            duration = params.duration;
            layoutGravity = params.layoutGravity;

            //Icon
            if (params.iconResId != 0) {
                ivIcon.setVisibility(VISIBLE);
                ivIcon.setBackgroundResource(params.iconResId);
            }

            //Title
            if (!TextUtils.isEmpty(params.title)) {
                tvTitle.setVisibility(VISIBLE);
                tvTitle.setText(params.title);
                if (params.titleColor != 0) {
                    tvTitle.setTextColor(ContextCompat.getColor(getContext(), params.titleColor));
                }
            }

            //Message
            if (!TextUtils.isEmpty(params.message)) {
                tvMessage.setVisibility(VISIBLE);
                tvMessage.setText(params.message);
                if (params.messageColor != 0) {
                    tvMessage.setTextColor(ContextCompat.getColor(getContext(), params.messageColor));
                }

                if (TextUtils.isEmpty(params.title)) {
                    LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) tvMessage
                            .getLayoutParams();
                    layoutParams.topMargin = 0;
                }
            }

            //Action
            if ((!TextUtils.isEmpty(params.action) || params.actionIcon != 0)
                    && params.onActionClickListener != null) {
                btnAction.setVisibility(VISIBLE);
                btnAction.setText(params.action);
                btnAction.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        params.onActionClickListener.onClick();
                        dismiss();
                    }
                });

                //Action Color
                if (params.actionColor != 0) {
                    btnAction.setTextColor(ContextCompat.getColor(getContext(), params.actionColor));
                }
            }

            if (params.actionIcon != 0 && params.onActionClickListener != null) {
                btnAction.setVisibility(GONE);
                btnActionWithIcon.setVisibility(VISIBLE);
                btnActionWithIcon.setBackgroundResource(params.actionIcon);
                btnActionWithIcon.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        params.onActionClickListener.onClick();
                        dismiss();
                    }
                });
            }

            //Background
            if (params.backgroundColor != 0) {
                contentview.setBackgroundColor(ContextCompat.getColor(getContext(), params.backgroundColor));
            }

            int padding = getContext().getResources().getDimensionPixelSize(R.dimen.default_padding);
            if (layoutGravity == Gravity.BOTTOM) {
                contentview.setPadding(padding, padding, padding, padding);
            }

            createInAnim();
            createOutAnim();
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (layoutGravity == Gravity.TOP) {
            super.onLayout(changed, l, 0, r, getMeasuredHeight());
        } else {
            super.onLayout(changed, l, t, r, b);
        }
    }

    private void createInAnim() {
        slideInAnimation = AnimationUtils.loadAnimation(getContext(),
                layoutGravity == Gravity.BOTTOM ? R.anim.slide_in_from_bottom : R.anim.slide_in_from_top);
        slideInAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
//                postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        dismiss();
//                    }
//                }, duration);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        setAnimation(slideInAnimation);
    }

    private void createOutAnim() {
        slideOutAnimation = AnimationUtils.loadAnimation(getContext(),
                layoutGravity == Gravity.BOTTOM ? R.anim.slide_out_to_bottom : R.anim.slide_out_to_top);
        slideOutAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                destroy();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void dismiss() {
        startAnimation(slideOutAnimation);
    }

    private void destroy() {
        postDelayed(new Runnable() {
            @Override
            public void run() {
                ViewParent parent = getParent();
                if (parent != null) {
                    MySnackbar.this.clearAnimation();
                    ((ViewGroup) parent).removeView(MySnackbar.this);
                }
            }
        }, 200);
    }

}