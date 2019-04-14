package com.example.mr_zyl.project824.pro.publish.view.self;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.mr_zyl.project824.R;
import com.example.mr_zyl.project824.pro.publish.animation.KickBackAnimator;
import com.example.mr_zyl.project824.utils.DisplayUtil;
import com.example.mr_zyl.project824.utils.SystemAppUtils;
import com.example.mr_zyl.project824.utils.ToastUtil;

import net.robinx.lib.blur.widget.BlurMode;

public class MoreWindow extends PopupWindow implements OnClickListener {

    private String TAG = MoreWindow.class.getSimpleName();
    Activity mContext;
    private int mWidth;
    private int mHeight;
    private Bitmap mBitmap = null;
    private Bitmap overlay = null;
    private BlurRelativeLayout bmrl_main_poplayout;

    private Handler mHandler = new Handler();
    private MenuOnclickListener menuListener;

    public MoreWindow(Activity context,MenuOnclickListener menuListener) {
        this.mContext = context;
        this.menuListener=menuListener;
    }

    public void init() {
        mWidth = LinearLayout.LayoutParams.MATCH_PARENT;
        mHeight = LinearLayout.LayoutParams.MATCH_PARENT;

        setWidth(mWidth);
        setHeight(DisplayUtil.Height(mContext));
    }

    public void showMoreWindow(View anchor, int bottomMargin) {
        final View view = LayoutInflater.from(mContext).inflate(R.layout.center_music_more_window, null);
        setContentView(view);
        bmrl_main_poplayout= (BlurRelativeLayout) view.findViewById(R.id.bmrl_main_poplayout);
        bmrl_main_poplayout.blurMode(BlurMode.RENDER_SCRIPT).blurRadius(10);
        ImageView close = (ImageView) view.findViewById(R.id.center_music_window_close);
        final RelativeLayout rl_menu_layout = (RelativeLayout) view.findViewById(R.id.rl_menu_layout);
        close.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isShowing()) {
                    closeAnimation(rl_menu_layout);
                }
            }
        });
        showAnimation(rl_menu_layout);
        setBackgroundDrawable(new BitmapDrawable(mContext.getResources(), (Bitmap) null));
//		setBackgroundDrawable(new BitmapDrawable(mContext.getResources(), blur()));
        setOutsideTouchable(true);
        setFocusable(false);
        showAtLocation(anchor, Gravity.BOTTOM, 0, bottomMargin);
        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                destroy();
            }
        });
    }

    private void showAnimation(ViewGroup layout) {
        for (int i = 0; i < layout.getChildCount(); i++) {
            final View child = layout.getChildAt(i);
            if (child.getId() == R.id.center_music_window_close) {
                continue;
            }
            child.setOnClickListener(this);
            child.setVisibility(View.INVISIBLE);
            mHandler.postDelayed(new Runnable() {

                @Override
                public void run() {
                    child.setVisibility(View.VISIBLE);
                    ValueAnimator fadeAnim = ObjectAnimator.ofFloat(child, "translationY", -DisplayUtil.Height(mContext), 0);
                    fadeAnim.setDuration(400);
                    KickBackAnimator kickAnimator = new KickBackAnimator();
                    kickAnimator.setDuration(150);
                    fadeAnim.setEvaluator(kickAnimator);
                    fadeAnim.start();
                }
            }, i * 50);
        }

    }

    private void closeAnimation(ViewGroup layout) {
        for (int i = 0; i < layout.getChildCount(); i++) {
            final View child = layout.getChildAt(i);
            if (child.getId() == R.id.center_music_window_close) {
                continue;
            }
            child.setOnClickListener(this);
            mHandler.postDelayed(new Runnable() {

                @Override
                public void run() {
                    child.setVisibility(View.VISIBLE);
                    ValueAnimator fadeAnim = ObjectAnimator.ofFloat(child, "translationY", 0, DisplayUtil.Height(mContext));
                    fadeAnim.setDuration(400);
                    KickBackAnimator kickAnimator = new KickBackAnimator();
                    kickAnimator.setIs_closeAnimation(true);
                    kickAnimator.setDuration(150);
                    fadeAnim.setEvaluator(kickAnimator);
                    fadeAnim.start();
                    fadeAnim.addListener(new AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            child.setVisibility(View.INVISIBLE);
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {
                        }
                    });
                }
            }, (layout.getChildCount() - i - 1) * 50);

            if (child.getId() == R.id.more_window_language) {
                mHandler.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        dismiss();
                    }
                }, (layout.getChildCount() - i) * 50 + 80);
            }
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.more_window_language:
                break;
            case R.id.more_window_photo:
                menuListener.selectPhoto();
                break;
            case R.id.more_window_take:
                menuListener.selectCamera();
                break;
            case R.id.more_window_signin:
                break;
            case R.id.more_window_auto:
                break;
            case R.id.more_window_more:
                break;
        }
        ToastUtil.showToast(mContext,((TextView)v).getText().toString());
    }

    public void destroy() {
        if (null != overlay) {
            overlay.recycle();
            overlay = null;
        }
        if (null != mBitmap) {
            mBitmap.recycle();
            mBitmap = null;
        }
        bmrl_main_poplayout.clearCatchView();
        System.gc();
    }

    public interface MenuOnclickListener {
        void selectPhoto();
        void selectCamera();
    }

}
