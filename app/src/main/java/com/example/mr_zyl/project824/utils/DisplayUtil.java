package com.example.mr_zyl.project824.utils;

import android.content.Context;

/**
 * Created by aaron on 16/8/3.
 */
public class DisplayUtil {

    public static int screenWidthPx; //屏幕宽 px
    public static int screenhightPx; //屏幕高 px
    public static float density;//屏幕密度
    public static int densityDPI;//屏幕密度
    public static float screenWidthDip;//  dp单位
    public static float screenHightDip;//  dp单位


    public static int getDensity(Context context) {
        density = context.getResources().getDisplayMetrics().density;
        return (int)density;
    }
    public static int Width(Context context) {
         screenWidthPx = context.getResources().getDisplayMetrics().widthPixels;
        return screenWidthPx;
    }
    public static int Height(Context context) {
        screenhightPx = context.getResources().getDisplayMetrics().heightPixels;
        return screenhightPx;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     *
     * @param pxValue
     * @return
     */
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     *
     * @param spValue
     * @return
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

}
