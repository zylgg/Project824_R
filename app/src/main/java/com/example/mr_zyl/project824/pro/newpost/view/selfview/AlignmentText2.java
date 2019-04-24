package com.example.mr_zyl.project824.pro.newpost.view.selfview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint.FontMetricsInt;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * 定义，自动对齐的textview
 *
 * @author 023
 */
@SuppressLint("AppCompatCustomView")
public class AlignmentText2 extends TextView {
    /**
     * 构造函数
     *
     * @param context
     * @param attrs
     * @param defStyle
     */
    public AlignmentText2(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        if (textLen == null) {
            buffer = new StringBuffer();
            textLen = new ArrayList<CharSequence>();
            lines = 2;
        }

    }

    /**
     * 构造函数
     *
     * @param context
     * @param attrs
     */
    public AlignmentText2(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    /**
     * stringbuffer
     */
    StringBuffer buffer;
    /**
     * 文本内容
     */
    List<CharSequence> textLen;
    /**
     * 首行
     */
    boolean firstFlag = true;
    /**
     * 行数
     */
    private int lines = 2;

    /**
     * 设置文本行数
     *
     * @param lineCount 文本行数
     */
    public void setLines(int lineCount) {
        lines = lineCount;
    }

    /**
     * 文字高
     */
    int textHeight = 0;
    /**
     * paint
     */
    TextPaint paint;

    /**
     * 构造函数
     *
     * @param context
     */
    public AlignmentText2(Context context) {
        super(context);
        init();
    }

    /**
     * 设置文本
     */
    @Override
    public void setText(CharSequence text, BufferType type) {
        firstFlag = true;
        tmpText = text;
        this.type = type;
        super.setText(tmpText, type);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (firstFlag) {
            firstFlag = refreshText(this.getWidth());
        }
    }

    private CharSequence tmpText;
    private BufferType type;

    private boolean refreshText(float textwidth) {
        StringBuffer buffer = new StringBuffer();
        if (!TextUtils.isEmpty(tmpText)) {
            if (textwidth <= 0) {
                super.setText(tmpText, type);
                return true;
            }
            divideText(tmpText, textwidth);
            if (textLen.size() > 0) {
                for (int ii = 0; ii < textLen.size(); ii++) {
                    buffer.append(textLen.get(ii).toString());
                    if (ii < textLen.size() - 1) {
                        buffer.append("\n");
                    }
                }
            }
        }
        super.setText(buffer, type);
        return false;
    }

    /**
     * 组织文本数据
     *
     * @param text2
     * @param width
     */
    private void divideText(CharSequence text2, float width) {
        String text = text2.toString();

        init();
        initTextHeight();
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
            if (lines > 0 && curLen + 1 == lines) {
                if (textLength + tmpLength + dotWidth > width) {
                    buffer.append("...");
                    textLen.add(buffer.toString());
                    return;
                    //textLength = textLength + tmpLength;
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
        firstFlag = false;
        if (buffer.length() > 0) {
            textLen.add(buffer.toString());
        }
    }

    /**
     * 初始化文字行距
     */
    private void initTextHeight() {
        if (paint == null) {
            paint = this.getPaint();
            paint.setColor(getCurrentTextColor());
            paint.setTextSize(this.getTextSize());
            FontMetricsInt fmi = new FontMetricsInt();
            paint.getFontMetricsInt(fmi);
            textHeight = fmi.bottom - fmi.top + 3;
        }
    }


}
