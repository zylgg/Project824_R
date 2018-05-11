package com.example.mr_zyl.project824.pro.mine.view.selfview;

import android.app.Activity;
import android.content.Context;
import android.graphics.PixelFormat;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;

import com.example.mr_zyl.project824.pro.mine.view.impl.OnActionClickListener;

import java.lang.reflect.Field;


/**
 * Created by TFHR02 on 2017/9/6.
 */

public class MySnackbarUtils {
    private static Toast mToast;
    private static final String TAG = "MySnackbarUtils";
    private MySnackbar mySnackbarView;
    private Activity context;
    private boolean isCoverStatusBar;


    private MySnackbarUtils() {
    }

    private MySnackbarUtils(Activity context, Params params, boolean isCoverStatusBar) {
        this.context = context;
        this.isCoverStatusBar = isCoverStatusBar;
        this.mySnackbarView = new MySnackbar(context, this);
        this.mySnackbarView.setParams(params);
    }

    public void show() {
        if (mToast == null) {
            mToast = new Toast(context);
        } else {//让新的toast在最上面。
            mToast.cancel();
            mToast = new Toast(context);
        }
        mToast.setView(mySnackbarView);
        mToast.setGravity(mySnackbarView.getLayoutGravity(), 0, 0);
        mToast.setDuration(Toast.LENGTH_LONG);

        try {
            Object mTN = null;
            mTN = getField(mToast, "mTN");
            if (mTN != null) {
                Object mParams = getField(mTN, "mParams");
                if (mParams != null
                        && mParams instanceof WindowManager.LayoutParams) {
                    WindowManager.LayoutParams params = (WindowManager.LayoutParams) mParams;

                    int flag = params.flags =WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                                    | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
//                                    | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                            |WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                            |WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION
                            ;

                    int coverFlag = flag | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
                    params.type=WindowManager.LayoutParams.TYPE_TOAST|WindowManager.LayoutParams.FIRST_APPLICATION_WINDOW;
                    params.flags = mySnackbarView.isCoverStatusBar() ? coverFlag : flag;
                }
            }
        } catch (Exception e) {
            Log.i("error", "T: Exception");
        }

        mToast.show();
    }

    private static Object getField(Object object, String fieldName)
            throws NoSuchFieldException, IllegalAccessException {
        Field field = object.getClass().getDeclaredField(fieldName);
        if (field != null) {
            field.setAccessible(true);
            return field.get(object);
        }
        return null;
    }

    public void dismiss() {
        Log.i(TAG, "dismiss");
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

        public Builder setCoverStatusBar(boolean coverStatusBar) {
            params.isCoverStatusBar = coverStatusBar;
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

        public Builder setActionWithIcon(@DrawableRes int resId, OnActionClickListener onActionClickListener) {
            params.actionIcon = resId;
            params.onActionClickListener = onActionClickListener;
            return this;
        }

        /**
         * 只对 不覆盖状态栏时有效
         *
         * @param layoutGravity
         * @return
         */
        public Builder setLayoutGravity(int layoutGravity) {
            params.layoutGravity = layoutGravity;
            return this;
        }

        public MySnackbarUtils create() {
            MySnackbarUtils cookie = new MySnackbarUtils(context, params, params.isCoverStatusBar);
            return cookie;
        }

        public MySnackbarUtils show() {
            final MySnackbarUtils cookie = create();
            cookie.show();
            return cookie;
        }
    }

    final static class Params {

        boolean isCoverStatusBar;

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