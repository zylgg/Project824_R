package com.example.mr_zyl.project.pro.mine.view.selfview;

/**
 * Created by TFHR02 on 2017/9/6.
 */

import android.app.Activity;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.example.mr_zyl.project.pro.mine.view.impl.OnActionClickListener;

/**
 * MySnackbarUtils is a lightweight library for showing a brief message at the top or bottom of the
 * screen. <p>
 * <pre>
 * new MySnackbarUtils
 *      .Builder(MainActivity.this)
 *      .setTitle("TITLE")
 *      .setMessage("MESSAGE")
 *      .setAction("ACTION", new OnActionClickListener() {})
 *      .show();
 * </pre>
 */
public class MySnackbarUtils {

    private static final String TAG = "MySnackbarUtils";

    private MySnackbar mySnackbarView;
    private Activity context;

    private MySnackbarUtils() {
    }

    private MySnackbarUtils(Activity context, Params params) {
        this.context = context;
        mySnackbarView = new MySnackbar(context);
        mySnackbarView.setParams(params);
    }

    public void show() {
        if (mySnackbarView != null) {
            LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            final ViewGroup decorView = (ViewGroup) context.getWindow().getDecorView();
            final ViewGroup content = (ViewGroup) decorView.findViewById(android.R.id.content);
            if (mySnackbarView.getParent() == null) {
                if (mySnackbarView.getLayoutGravity() == Gravity.BOTTOM) {
                    content.addView(mySnackbarView,layoutParams);
                } else {
                    decorView.addView(mySnackbarView,layoutParams);
                }
            }
        }
    }

    public static class Builder {

        private Params params = new Params();

        public Activity context;

        /**
         * Create a builder for an cookie.
         */
        public Builder(Activity activity) {
            this.context = activity;
        }

        public Builder setIcon(@DrawableRes int iconResId) {
            params.iconResId = iconResId;
            return this;
        }

        public Builder setTitle(String title) {
            params.title = title;
            return this;
        }

        public Builder setTitle(@StringRes int resId) {
            params.title = context.getString(resId);
            return this;
        }

        public Builder setMessage(String message) {
            params.message = message;
            return this;
        }

        public Builder setMessage(@StringRes int resId) {
            params.message = context.getString(resId);
            return this;
        }

        public Builder setDuration(long duration) {
            params.duration = duration;
            return this;
        }

        public Builder setTitleColor(@ColorRes int titleColor) {
            params.titleColor = titleColor;
            return this;
        }

        public Builder setMessageColor(@ColorRes int messageColor) {
            params.messageColor = messageColor;
            return this;
        }

        public Builder setBackgroundColor(@ColorRes int backgroundColor) {
            params.backgroundColor = backgroundColor;
            return this;
        }

        public Builder setActionColor(@ColorRes int actionColor) {
            params.actionColor = actionColor;
            return this;
        }

        public Builder setAction(String action, OnActionClickListener onActionClickListener) {
            params.action = action;
            params.onActionClickListener = onActionClickListener;
            return this;
        }

        public Builder setAction(@StringRes int resId, OnActionClickListener onActionClickListener) {
            params.action = context.getString(resId);
            params.onActionClickListener = onActionClickListener;
            return this;
        }

        public Builder setActionWithIcon(@DrawableRes int resId,
                                         OnActionClickListener onActionClickListener) {
            params.actionIcon = resId;
            params.onActionClickListener = onActionClickListener;
            return this;
        }

        public Builder setLayoutGravity(int layoutGravity) {
            params.layoutGravity = layoutGravity;
            return this;
        }

        public MySnackbarUtils create() {
            MySnackbarUtils cookie = new MySnackbarUtils(context, params);
            return cookie;
        }

        public MySnackbarUtils show() {
            final MySnackbarUtils cookie = create();
            cookie.show();
            return cookie;
        }
    }

    final static class Params {

        public String title;

        public String message;

        public String action;

        public OnActionClickListener onActionClickListener;

        public int iconResId;

        public int backgroundColor;

        public int titleColor;

        public int messageColor;

        public int actionColor;

        public long duration = 2000;

        public int layoutGravity = Gravity.TOP;

        public int actionIcon;
    }

}