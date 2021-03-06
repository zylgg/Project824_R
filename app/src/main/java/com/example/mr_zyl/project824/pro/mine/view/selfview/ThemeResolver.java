package com.example.mr_zyl.project824.pro.mine.view.selfview;

/**
 * Created by TFHR02 on 2017/9/6.
 */
import android.content.Context;
import android.content.res.TypedArray;
import androidx.annotation.AttrRes;

public class ThemeResolver {

    public static int getColor(Context context, @AttrRes int attr) {
        return getColor(context, attr, 0);
    }

    public static int getColor(Context context, @AttrRes int attr, int defaultColor) {
        TypedArray a = context.getTheme().obtainStyledAttributes(new int[]{attr});
        try {
            return a.getColor(0, defaultColor);
        } finally {
            a.recycle();
        }
    }

}