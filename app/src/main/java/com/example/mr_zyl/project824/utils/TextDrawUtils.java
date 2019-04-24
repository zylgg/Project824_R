package com.example.mr_zyl.project824.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.mr_zyl.project824.R;

import java.util.ArrayList;
import java.util.List;

public class TextDrawUtils {
    private static CharSequence tmpText;
    /**
     * stringbuffer
     */
    private static StringBuffer buffer;
    /**
     * 文本内容
     */
    private static List<CharSequence> textLen;
    /**
     * 行数
     */
    private static int lines = 2;

    /**
     * 文字第一行标签的总内边距
     */
    private static float firstLineShapePadding_LR_Space = 0;

    public static StringBuffer refreshText(TextPaint paint, CharSequence text, float textwidth, float shapePadding_LR_Space) {
        tmpText = text;
        firstLineShapePadding_LR_Space = shapePadding_LR_Space;
        StringBuffer buffer = new StringBuffer();
        if (!TextUtils.isEmpty(tmpText)) {
            if (textwidth <= 0) {
                return buffer;
            }
            divideText(paint, tmpText, textwidth);
            if (textLen.size() > 0) {
                for (int ii = 0; ii < textLen.size(); ii++) {
                    buffer.append(textLen.get(ii).toString());
                    if (ii < textLen.size() - 1) {
                        buffer.append("\n");
                    }
                }
            }
        }
        return buffer;
    }

    /**
     * 组织文本数据
     *
     * @param text2
     * @param width
     */
    public static void divideText(TextPaint paint, CharSequence text2, float width) {
        String text = text2.toString();
        boolean isAddWidth=false;
        boolean isSubtractWidth=false;
        init();
        textLen.clear();
        buffer.delete(0, buffer.length());

        float textLength = 0f;
        float tmpLength = 0f;
        char firstCharacter;
        char[] textTochars = text.toCharArray();
        char tmpChar;
        float dotWidth = paint.measureText("...");
        int curLen = 0;
        for (int ii = 0; ii < textTochars.length; ii++) {
            tmpChar = textTochars[ii];
            if (tmpChar == '\n') {
                textLen.add(buffer.toString());
                buffer.delete(0, buffer.length());
                curLen++;
                continue;
            }
            tmpLength = paint.measureText(textTochars, ii, 1);
//            if (lines > 0 && curLen + 1 == lines) {
//                if (textLength + tmpLength + dotWidth > width) {
//                    buffer.append("...");
//                    textLen.add(buffer.toString());
//                    return;
//                }
//            }
            //如果第一行包含标签时，宽度加上标签的总内边距
            if (firstLineShapePadding_LR_Space != 0) {
                if (textLen.size() == 0&&!isAddWidth) {
                    isAddWidth=true;
                    width = width + firstLineShapePadding_LR_Space;
                } else if (textLen.size() == 1&&!isSubtractWidth) {
                    isSubtractWidth=true;
                    width = width - firstLineShapePadding_LR_Space;
                }
            }
            if (textLength + tmpLength < width) {
                buffer.append(text.charAt(ii));
                textLength = textLength + tmpLength;
            } else {
                firstCharacter = text.charAt(ii);
                textLen.add(buffer.toString());
                buffer.delete(0, buffer.length());
                buffer.append(firstCharacter);
                textLength = tmpLength;
                curLen++;
            }
        }
        if (buffer.length() > 0) {
            textLen.add(buffer.toString());
        }
    }

    public static void init() {
        if (textLen == null) {
            buffer = new StringBuffer();
            textLen = new ArrayList<CharSequence>();
            lines = 2;
        }
    }


    public static Drawable getTagDrawable(Context context, String tag, int resid) {
        View view = LayoutInflater.from(context).inflate(R.layout.labeled_text_layout, null);
        TextView tv_labeled_text = view.findViewById(R.id.tv_labeled_text);
        tv_labeled_text.setText(tag);
        tv_labeled_text.setBackgroundResource(resid);
        if (resid == R.drawable.labeled_text_shape2) {
            tv_labeled_text.setTextColor(Color.parseColor("#492F14"));
        } else {
            tv_labeled_text.setTextColor(Color.WHITE);
        }
        Drawable drawable = new BitmapDrawable(context.getResources(), convertViewToBitmap(view));
        drawable.setBounds(0, 0, tv_labeled_text.getWidth(), tv_labeled_text.getHeight());
        return drawable;
    }

    public static Bitmap convertViewToBitmap(View view) {
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();
        return bitmap;
    }
}
