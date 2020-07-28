package com.example.mr_zyl.project824.pro.mine.view.selfview;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.util.Log;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.mr_zyl.project824.pro.mine.view.impl.OnActionClickListener;

import java.lang.reflect.Field;


/**
 * Created by TFHR02 on 2017/9/6.
 */

public class MySnackBarUtils {
    private static MySnackBarUtils mySnackBarUtils;
    private static Toast mToast;
    private static final String TAG = "MySnackBarUtils";
    private MySnackBar mySnackBarView;
    private Activity context;

    public static Builder getBuilder(Activity context) {
        return new Builder(context);
    }

    private MySnackBarUtils(Activity context, Params params) {
        this.context = context;
        this.mySnackBarView = new MySnackBar(context, this);
        this.mySnackBarView.setParams(params);
    }

    public void show() {
        if (mToast == null) {
            mToast = new Toast(context);
        } else {//让新的toast在最上面。
            mToast.cancel();
            mToast = new Toast(context);
        }
        mToast.setView(mySnackBarView);
        mToast.setGravity(mySnackBarView.getLayoutGravity(), 0, 0);
        mToast.setDuration(Toast.LENGTH_LONG);

        try {
            Object mTN = null;
            mTN = getField(mToast, "mTN");
            if (mTN != null) {
                Object mParams = getField(mTN, "mParams");
                if (mParams != null
                        && mParams instanceof WindowManager.LayoutParams) {
                    WindowManager.LayoutParams params = (WindowManager.LayoutParams) mParams;

                    int flag = params.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                            | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;

                    int coverFlag = flag | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
                    params.type = WindowManager.LayoutParams.TYPE_TOAST;
                    params.flags = mySnackBarView.isCoverStatusBar() ? coverFlag : flag;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        params.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
                    }
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
            //返回对象obj中表示字段的值
            return field.get(object);
        }
        return null;
    }

    public void dismiss() {
        if (mToast != null) {
            mToast.cancel();
        }
    }

    public static class Builder {

        private Params params = new Params();

        public Activity context;

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

        public Builder setLayoutGravity(int layoutGravity) {
            params.layoutGravity = layoutGravity;
            return this;
        }

        private MySnackBarUtils create() {
            MySnackBarUtils cookie = new MySnackBarUtils(context, params);
            return cookie;
        }

        public MySnackBarUtils show() {
            final MySnackBarUtils cookie = create();
            cookie.show();
            return cookie;
        }
    }

    final static class Params {

        boolean isCoverStatusBar = true;

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