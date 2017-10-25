package com.example.mr_zyl.project.pro.mine.view.selfview;

/**
 * Created by Houxy on 2016/8/2.
 */

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.v4.graphics.ColorUtils;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mr_zyl.project.R;
import com.example.mr_zyl.project.utils.DensityUtil;
import com.example.mr_zyl.project.utils.SystemAppUtils;

import de.hdodenhof.circleimageview.CircleImageView;


public class SlideView extends LinearLayout implements AppBarLayout.OnOffsetChangedListener {

    private static final String TAG = "SlideView";
    /**
     * 基础控件
     */
    private CircleImageView avatarView;
    /**
     * 文本控件
     */
    private TextView titleView;

    /**
     * 折叠时距离左边的距离
     */
    private float collapsedPadding;
    /**
     * 布局最开始距离左侧的padding
     */
    private float expandedPadding;

    /**
     * 展开时图片控件的大小
     */
    private float expandedImageSize;
    /**
     * 折叠是图片控件的大小
     */
    private float collapsedImageSize;

    /**
     * 值 是否已计算
     */
    private boolean valuesCalculatedAlready = false;
    private Toolbar toolbar;
    private AppBarLayout appBarLayout;
    /**
     * 固定在顶上的toolbar的高度
     */
    private float toolBarHeight;
    /**
     * appbar的最大移动距离
     */
    private float maxOffset;
    /**
     * 整个控件展开时的高度
     */
    private float expandedTheHeight;
    /**
     * 控件外面距离底部距离
     */
    private float marginbottomoffset;

    /**
     * 折叠时文本大小
     */
    private float collapsed_text_size;
    /**
     * 展开时文本大小
     */
    private float expanded_text_size;

    public SlideView(Context context) {
        this(context, null);
        setOrientation(HORIZONTAL);
    }

    public SlideView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(HORIZONTAL);

        final Resources resources = getResources();
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.test);
        try {
            collapsedPadding = a.getDimension(R.styleable.test_collapsedPadding, resources.getDimension(R.dimen.default_collapsed_padding));
            expandedPadding = a.getDimension(R.styleable.test_expandedPadding, resources.getDimension(R.dimen.default_expanded_padding));

            collapsedImageSize = a.getDimension(R.styleable.test_collapsedImageSize, resources.getDimension(R.dimen.default_collapsed_image_size));
            collapsed_text_size=a.getDimension(R.styleable.test_collapsed_text_size,resources.getDimension(R.dimen.default_collapsed_text_size));
            expanded_text_size=a.getDimension(R.styleable.test_expanded_text_size,resources.getDimension(R.dimen.default_expanded_text_size));

            marginbottomoffset = a.getDimension(R.styleable.test_marginbottomoffset, 0);
        } finally {
            a.recycle();
        }
    }

    //此方法在onDraw方法之前调用，也就是view还没有画出来的时候，可以在此方法中去执行一些初始化的操作
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        findViews();
        if (!isInEditMode()) {
            appBarLayout.addOnOffsetChangedListener(this);
            //除去design23.0.0 的 coordinatorlayout事件分发的bug
//            appBarLayout.setOnTouchListener(new OnTouchListener() {
//                @Override
//                public boolean onTouch(View v, MotionEvent event) {
//                    return true;
//                }
//            });
        } else {
            setExpandedValuesForEditMode();
        }
    }

    //初始化布局-----------------------------------------------开始
    private void findViews() {
        appBarLayout = findParentAppBarLayout();
        toolbar = findSiblingToolbar();
        avatarView = findAvatar();
        titleView = findTitle();
    }

    @NonNull
    private AppBarLayout findParentAppBarLayout() {
        ViewParent parent = this.getParent();
        if (parent instanceof AppBarLayout) {
            return ((AppBarLayout) parent);
        } else if (parent.getParent() instanceof AppBarLayout) {
            return ((AppBarLayout) parent.getParent());
        } else if (parent.getParent().getParent() instanceof AppBarLayout) {
            return ((AppBarLayout) parent.getParent().getParent());
        } else {
            throw new IllegalStateException("Must be inside an AppBarLayout");
        }
    }

    @NonNull //使用@NonNull注解修饰的参数不能为null
    private Toolbar findSiblingToolbar() {
        ViewGroup parent = ((ViewGroup) this.getParent());
        for (int i = 0, c = parent.getChildCount(); i < c; i++) {
            View child = parent.getChildAt(i);
            if (child instanceof Toolbar) {
                return (Toolbar) child;
            }
        }
        throw new IllegalStateException("No toolbar found as sibling");
    }


    private CircleImageView findAvatar() {
        CircleImageView avatar = null;
        if (getChildAt(0) instanceof CircleImageView)
            avatar = (CircleImageView) getChildAt(0);
        return avatar;
    }

    private TextView findTitle() {
        TextView title = null;
        if (getChildAt(0) instanceof TextView) {
            title = (TextView) getChildAt(0);
        }
        return title;
    }
    //初始化布局-----------------------------------------------结束

    private void setExpandedValuesForEditMode() {
        calculateValues();
        updateViews(1f, 0);
    }


    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int offset) {
        if (!valuesCalculatedAlready) {
            calculateValues();
            valuesCalculatedAlready = true;
        }
        float expandedPercentage = 1 - (-offset / maxOffset);
        updateViews(expandedPercentage, offset);
        if (onratiolistener != null) {
            onratiolistener.onRatioChanged(1 - expandedPercentage);
        }
    }

    /**
     * 计算值
     */
    private void calculateValues() {
        expandedTheHeight = this.getHeight();
//        Log.i(TAG, "expandedTheHeight: " + expandedTheHeight);
        toolBarHeight = toolbar.getHeight();
        if (avatarView != null) {
            expandedImageSize = avatarView.getHeight();
        }
        //计算滑动范围（appbar高-toolbar高-状态栏-最下面的布局高）

        maxOffset = appBarLayout.getHeight() - toolBarHeight - (isStatusBarView()?SystemAppUtils.getStatusHeight(getContext()):0)- DensityUtil.getpxByDimensize(getContext(),R.dimen.y120);
        //使控件处在居中的位置
        if (avatarView != null) {
            expandedPadding = (appBarLayout.getWidth() - expandedImageSize) / 2;
        } else if (titleView != null) {
            expandedPadding = (appBarLayout.getWidth() - titleView.getWidth()) / 2;
        }
    }
    public boolean isStatusBarView() {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {//如果系统不支持透明状态栏
            return false;
        } else {
            return true;
        }
    }

    /**
     * @param expandedPercentage 膨胀率1-0（越上滑，值越小）
     * @param currentOffset
     */
    private void updateViews(float expandedPercentage, int currentOffset) {
        //expandedPercentage 1-0  inversePercentage 0-1
        //collapsed 折叠时的
        // expanded  展开时的，初始的
//        Log.i(TAG, "expandedPercentage: " + expandedPercentage);
//        Log.i(TAG, "currentOffset: " + currentOffset);
        float inversePercentage = 1 - expandedPercentage;

        //括号里的是：滑动控件最开始的位置（滑动的高-控件的高+toolbar高-距离底部的距离）。
        float translation = -currentOffset + (maxOffset - expandedTheHeight + toolBarHeight - marginbottomoffset) * expandedPercentage;

        float currHeight = 0;
        if (avatarView != null) {
            currHeight = toolBarHeight + (expandedTheHeight - toolBarHeight) * expandedPercentage;
        } else if (titleView != null) {
            currHeight = toolBarHeight + (expandedTheHeight - toolBarHeight) * expandedPercentage;
        }
        float currentPadding = expandedPadding - (expandedPadding - collapsedPadding) * inversePercentage;

        float currentImageSize = collapsedImageSize - (collapsedImageSize - expandedImageSize) * expandedPercentage;
        //移动布局
        setContainerOffset(translation);
        //设置整个高度
//        Log.i(TAG, "currHeight: " + currHeight);
        setContainerHeight((int) currHeight);
        //设置布局内边距
        setPadding((int) currentPadding);
        //缩放子viewImageView的大小
        setAvatarSize((int) currentImageSize);
        if (avatarView != null) {
            avatarView.setBorderColor(ColorUtils.blendARGB(Color.WHITE, getResources().getColor(R.color.green), inversePercentage));
        } else if (titleView != null) {
            //颜色过渡
            titleView.setTextColor(ColorUtils.blendARGB(Color.WHITE, getResources().getColor(R.color.green), inversePercentage));
            //字体大小过度
            titleView.setTextSize(TypedValue.COMPLEX_UNIT_PX,expanded_text_size+(collapsed_text_size-expanded_text_size)*inversePercentage);
        }
    }

    private void setContainerOffset(float translation) {
        this.setTranslationY(translation);
    }

    private void setContainerHeight(int currHeight) {
        this.getLayoutParams().height = currHeight;
    }

    private void setPadding(int currentPadding) {
        this.setPadding(currentPadding, 0, 0, 0);
    }

    private void setAvatarSize(int currentImageSize) {
        if (avatarView != null) {
            avatarView.getLayoutParams().height = currentImageSize;
            avatarView.getLayoutParams().width = currentImageSize;
        }
    }

    private onRatioChangedListener onratiolistener;

    public void setOnRatiolistener(onRatioChangedListener ratiolistener) {
        this.onratiolistener = ratiolistener;
    }

    public interface onRatioChangedListener {
        void onRatioChanged(float expandedPercentage);
    }
}