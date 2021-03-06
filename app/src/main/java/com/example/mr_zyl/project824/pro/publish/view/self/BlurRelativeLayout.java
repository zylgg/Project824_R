package com.example.mr_zyl.project824.pro.publish.view.self;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;

import net.robinx.lib.blur.StackBlur;
import net.robinx.lib.blur.utils.BlurUtils;
import net.robinx.lib.blur.widget.BlurMode;

/**
 * Created by TFHR02 on 2017/9/30.
 */
public class BlurRelativeLayout extends RelativeLayout {

    private int mBlurRadius = 1;

    private Bitmap mScreenBitmap;
    private Bitmap mBitmap;

    private float mOffsetX;
    private float mOffsetY;

    private int mCompressFactor = 8;
    private int BLUR_MODE = BlurMode.NATIVE_PIXELS;

    public BlurRelativeLayout(Context context) {
        super(context);
        init();
    }

    public BlurRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BlurRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                getViewTreeObserver().removeOnPreDrawListener(this);
                blur();
                return true;
            }
        });
    }
    View view;
    private void blur() {
        //screen bitmap
        view = ((Activity) getContext()).getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        mScreenBitmap = view.getDrawingCache();

        //original bitmap
        mBitmap = Bitmap.createBitmap( (getMeasuredWidth()),(getMeasuredHeight()), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(mBitmap);
        canvas.translate(-getLeft()-mOffsetX, -getTop()- BlurUtils.getStatusBarHeight(getContext())-mOffsetY);
        canvas.drawBitmap(mScreenBitmap, 0, 0, null);

        //compress bitmap
        Matrix matrix = new Matrix();
        matrix.postScale(1.0f / mCompressFactor, 1.0f / mCompressFactor);
        mBitmap = Bitmap.createBitmap(mBitmap, 0, 0,
                mBitmap.getWidth(), mBitmap.getHeight(), matrix, true);

        //blur bitmap
        switch (BLUR_MODE) {
            case BlurMode.RENDER_SCRIPT:
                mBitmap = StackBlur.blurRenderScript(getContext(),mBitmap, mBlurRadius, false);
                break;
            case BlurMode.NATIVE_PIXELS:
                mBitmap = StackBlur.blurNativelyPixels(mBitmap, mBlurRadius, false);
                break;
            case BlurMode.NATIVE_BITMAP:
                mBitmap = StackBlur.blurNatively(mBitmap, mBlurRadius, false);
                break;
            case BlurMode.JAVA:
                mBitmap = StackBlur.blurJava(mBitmap, mBlurRadius, false);
                break;
        }


        setBackgroundDrawable(new BitmapDrawable(getResources(), mBitmap));
    }
    public void clearCatchView(){
        view.destroyDrawingCache();
    }

    public BlurRelativeLayout blurRadius(int blurRadius) {
        mBlurRadius = blurRadius;
        return this;
    }

    public BlurRelativeLayout offsetX(float offsetX) {
        mOffsetX = offsetX;
        return this;
    }

    public BlurRelativeLayout offsetY(float offsetY) {
        mOffsetY = offsetY;
        return this;
    }

    public BlurRelativeLayout compressFactor(int compressFactor) {
        mCompressFactor = compressFactor;
        return this;
    }

    public BlurRelativeLayout blurMode(int blurMode) {
        this.BLUR_MODE = blurMode;
        return this;
    }

    public void refresh(){
        invalidate();
    }

    public void update(){
        blur();
    }
}
