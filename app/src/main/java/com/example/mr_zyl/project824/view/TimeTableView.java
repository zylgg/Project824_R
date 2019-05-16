package com.example.mr_zyl.project824.view;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;

import com.example.mr_zyl.project824.R;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by win764-1 on 2016/11/1.
 */

public class TimeTableView extends View {

    private Context mContext;
    private Paint mPaint, mSelectedPaint;
    private float mSecondDegree;
    private float mMinDegree;
    private float mHourDegree;
    private boolean mIsNight;
    private float mTotalSecond;
    private int borderColor;
    private int minScaleColor;
    private int midScaleColor;
    private int maxScaleColor;
    private float minScaleLength;
    private float midScaleLength;
    private float maxScaleLength;
    private int textColor,selectedTextColor;
    private float textSize;
    private int secondPointerColor;
    private int minPointerColor;
    private int hourPointerColor;
    private float secondPointerLength;
    private float minPointerLength;
    private float hourPointerLength;
    private int centerPointColor;
    private float centerPointSize;
    private float borderWidth;
    private float secondPointerSize;
    private float minPointerSize;
    private float hourPointerSize;
    private float centerPointRadiu;
    private String centerPointType;
    private int circleBackground;
    private int parentSize;
    private int sizeMode;
    private boolean isSecondGoSmooth;
    private int sleepTime;
    private int textHeight;
    private int textWight;
    private boolean isDrawText;
    private String secondArray[] = new String[60];
    private String minuteArray[] = new String[60];
    private String hourArray[] = new String[12];
    private Timer mTimer = new Timer();
    private int circleRadius = 0;

    public TimeTableView(Context context) {
        super(context);
        this.mContext = context;
        initPainter();
    }

    public TimeTableView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        init(context, attrs);
        initPainter();
    }

    /**
     * 初始化各参数
     *
     * @param context
     * @param attrs
     */
    private void init(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TimeView);
        borderColor = ta.getColor(R.styleable.TimeView_borderColor, Color.BLACK);
        circleBackground = ta.getColor(R.styleable.TimeView_circleBackground, Color.WHITE);
        borderWidth = ta.getDimension(R.styleable.TimeView_borderWidth,
                SizeUtils.dp2px(context, 1));
        minScaleColor = ta.getColor(R.styleable.TimeView_minScaleColor, Color.BLACK);
        midScaleColor = ta.getColor(R.styleable.TimeView_midScaleColor, Color.BLACK);
        maxScaleColor = ta.getColor(R.styleable.TimeView_maxScaleColor, Color.BLACK);
        minScaleLength = ta.getDimension(R.styleable.TimeView_minScaleLength,
                SizeUtils.dp2px(context, 7));
        midScaleLength = ta.getDimension(R.styleable.TimeView_midScaleLength,
                SizeUtils.dp2px(context, 12));
        maxScaleLength = ta.getDimension(R.styleable.TimeView_maxScaleLength,
                SizeUtils.dp2px(context, 14));
        textColor = ta.getColor(R.styleable.TimeView_textColor, Color.BLACK);
        selectedTextColor=ta.getColor(R.styleable.TimeView_selectedTextColor, Color.BLACK);
        textSize = ta.getDimension(R.styleable.TimeView_textSize,
                SizeUtils.dp2px(context, 15));
        isDrawText = ta.getBoolean(R.styleable.TimeView_isDrawText, true);
        secondPointerColor = ta.getColor(R.styleable.TimeView_secondPointerColor, Color.RED);
        minPointerColor = ta.getColor(R.styleable.TimeView_minPointerColor, Color.BLACK);
        hourPointerColor = ta.getColor(R.styleable.TimeView_hourPointerColor, Color.BLACK);
        secondPointerLength = ta.getDimension(R.styleable.TimeView_secondPointerLength,
                SizeUtils.dp2px(context, 80));
        minPointerLength = ta.getDimension(R.styleable.TimeView_minPointerLength,
                SizeUtils.dp2px(context, 50));
        hourPointerLength = ta.getDimension(R.styleable.TimeView_hourPointerLength,
                SizeUtils.dp2px(context, 30));
        secondPointerSize = ta.getDimension(R.styleable.TimeView_secondPointerSize,
                SizeUtils.dp2px(context, 1));
        minPointerSize = ta.getDimension(R.styleable.TimeView_minPointerSize,
                SizeUtils.dp2px(context, 3));
        hourPointerSize = ta.getDimension(R.styleable.TimeView_hourPointerSize,
                SizeUtils.dp2px(context, 5));
        centerPointColor = ta.getColor(R.styleable.TimeView_centerPointColor, Color.BLACK);
        centerPointSize = ta.getDimension(R.styleable.TimeView_centerPointSize,
                SizeUtils.dp2px(context, 5));
        centerPointRadiu = ta.getDimension(R.styleable.TimeView_centerPointRadiu,
                SizeUtils.dp2px(context, 1));
        centerPointType = ta.getString(R.styleable.TimeView_centerPointType);
        isSecondGoSmooth = ta.getBoolean(R.styleable.TimeView_isSecondGoSmooth, false);
        if (isSecondGoSmooth) {
            sleepTime = 50;
        } else {
            sleepTime = 1000;
        }
        ta.recycle();
    }

    /**
     * 初始化画笔
     */
    private void initPainter() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(2);
        //文字选中的画笔
        mSelectedPaint = new Paint();
        mSelectedPaint.setAntiAlias(true);
        mSelectedPaint.setColor(selectedTextColor);
        mSelectedPaint.setStyle(Paint.Style.FILL);
        mSelectedPaint.setStrokeWidth(1);
        mSelectedPaint.setTextSize(textSize);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int size = SizeUtils.measureSize(mContext, widthMeasureSpec);
        int sizeH = SizeUtils.measureSize(mContext, heightMeasureSpec);
        setMeasuredDimension(size, sizeH);
    }


    /**
     * 绘制（时、分、秒）数字之间的间隔
     */
    private static int drawNumberSpace = 0;

    @Override
    protected void onDraw(Canvas canvas) {
        drawNumberSpace = SizeUtils.dp2px(getContext(), 15);
        circleRadius = getWidth() / 2;
        canvas.translate(-getWidth() / 2, 0);
        //画外边框
//        mPaint.setColor(borderColor);
//        mPaint.setStrokeWidth(2);
//        mPaint.setStyle(Paint.Style.STROKE);
//        Path path = new Path();
//        path.lineTo(getWidth(), 0);
//        path.lineTo(getWidth(), getHeight());
//        path.lineTo(0, getHeight());
//        path.close();
//        canvas.drawPath(path, mPaint);
        //外圆边界
        drawCircleOut(canvas);
        //圆背景
//        drawCircleBg(canvas);
        //圆心
        drawCirclePoint(canvas);
        //刻度
//        drawKedu(canvas);
        //画时、分、秒针数字
        drawHourNumber(canvas);
        drawMinuteNumber(canvas);
        drawSecondNumber(canvas);
        //时针
//        drawHourWheel(canvas);
        //分针
//        drawMinuteWheel(canvas);
        //秒针
//        drawSecondWheel(canvas);
    }

    /**
     * 手动设置时间
     *
     * @param hour
     * @param min
     */

    public void setTime(int hour, int min) {
        setTotalSecond(hour * 3600f + min * 60f);
    }

    /**
     * 应用打开初始化时间（例如1：30：30）
     *
     * @param hour
     * @param min
     * @param second
     */
    public void setTime(int hour, int min, int second) {
//        mMinDegree = second * 0.1f + min * 6f;
        if (hour >= 24 || hour < 0 || min >= 60 || min < 0 || second >= 60 || second < 0) {
            Toast.makeText(getContext(), "时间不合法", Toast.LENGTH_SHORT).show();
            return;
        }
        if (hour >= 12) {
            mIsNight = true;
//            mHourDegree = min * 0.5f + (hour - 12) * 30f;
            mHourDegree = (hour + min * 1.0f / 60f + second * 1.0f / 3600f - 12) * 30f;
        } else {
            mIsNight = false;
//            mHourDegree = min * 0.5f + hour * 30f;
            mHourDegree = (hour + min * 1.0f / 60f + second * 1.0f / 3600f) * 30f;
        }
        mMinDegree = (min + second * 1.0f / 60f) * 6f;
//        mSecondDegree = second * 6f;

        for (int i = 0; i < secondArray.length; i++) {
            int currentSecond = second + i;
            int currentMinute = min + i;
            currentSecond = currentSecond > 60 ? currentSecond - 60 : currentSecond;
            currentMinute = currentMinute >= 60 ? currentMinute - 60 : currentMinute;
            secondArray[i] = (currentSecond + "秒");
            minuteArray[i] = (currentMinute + "分");
        }
        for (int i = 0; i < hourArray.length; i++) {
            hour = hour > 12 ? hour - 12 : hour;
            int currentHour = hour + i;
            currentHour = currentHour > 12 ? currentHour - 12 : currentHour;
            hourArray[i] = (currentHour + "时");
        }
        invalidate();
    }

    /**
     * 时钟走起
     */
    public void start() {
        mTimer.schedule(task, 0, sleepTime);
    }

    private TimerTask task = new TimerTask() {
        @Override
        public void run() {
            if (mSecondDegree == 360) {
                mSecondDegree = 0;
            }
            if (mMinDegree == 360) {
                mMinDegree = 0;
            }
            if (mHourDegree == 360) {
                mHourDegree = 0;
                mIsNight = !mIsNight;
            }
            //秒针：1秒（1000ms）走6°，即50ms走0.3°
            mSecondDegree = mSecondDegree + 0.3f * sleepTime / 50;
            //分针：1秒（1000ms）走0.1°，即50ms走0.005°
            mMinDegree = mMinDegree + 0.005f * sleepTime / 50;
            //时针：1秒（1000ms）走1/120°，即50ms走1/2400°
            mHourDegree = mHourDegree + 1.0f * sleepTime / 50 / 2400.0f;
            postInvalidate();

        }
    };


    /**
     * 画小时 数字
     *
     * @param canvas
     */
    private void drawHourNumber(Canvas canvas) {
        if (isDrawText) {
            //旋转圆盘
            Calendar calendar = Calendar.getInstance();
            int minute = calendar.getTime().getMinutes();
            int seconds = calendar.getTime().getSeconds();
            if (minute == 0 && seconds == 0) {
                String firstHourText = hourArray[0];
                for (int i = 0; i < hourArray.length; i++) {
                    if (i <= hourArray.length - 2) {
                        hourArray[i] = hourArray[i + 1];
                    } else {
                        hourArray[i] = firstHourText;
                    }
                }
            }
            canvas.save();
            mPaint.setColor(textColor);
            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setStrokeWidth(1);
            mPaint.setTextSize(textSize);
            canvas.translate(getWidth() / 2, getHeight() / 2);
            int currentDegree = 0;
            for (int i = 0; i < 12; i++) {
                Rect textBound = new Rect();//创建一个矩形
                String text = hourArray[i];
                //将文字装在上面创建的矩形中，即这个矩形就是文字的边框
                mPaint.getTextBounds(text, 0, text.length(), textBound);
                if (currentDegree % 360 == 0) {
                    canvas.drawText(text, circleRadius - getMaxTextWidth("十二点"), textBound.height() / 2, mSelectedPaint);
                } else {
                    canvas.drawText(text, circleRadius - getMaxTextWidth("十二点"), textBound.height() / 2, mPaint);
                }
                canvas.rotate(30);
                currentDegree = currentDegree + 6;
            }
            canvas.restore();
        }
    }

    /**
     * 画分钟 数字
     *
     * @param canvas
     */
    private void drawMinuteNumber(Canvas canvas) {
        if (isDrawText) {
            //旋转圆盘
            Calendar calendar = Calendar.getInstance();
            int seconds = calendar.getTime().getSeconds();
//            Log.i("drawMinuteNumber","seconds:"+seconds);
            if (seconds == 0) {
                String firstMinuteText = minuteArray[0];
                for (int i = 0; i < minuteArray.length; i++) {
                    if (i <= minuteArray.length - 2) {
                        minuteArray[i] = minuteArray[i + 1];
                    } else {
                        minuteArray[i] = firstMinuteText;
                    }
                }
            }
            canvas.save();
            mPaint.setColor(textColor);
            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setStrokeWidth(1);
            mPaint.setTextSize(textSize);
            canvas.translate(getWidth() / 2, getHeight() / 2);
            int currentDegree = 0;
            for (int i = 0; i < minuteArray.length; i++) {
                Rect textBound = new Rect();//创建一个矩形
                String text = minuteArray[i];
                //将文字装在上面创建的矩形中，即这个矩形就是文字的边框
                mPaint.getTextBounds(text, 0, text.length(), textBound);
                if (currentDegree % 360 == 0) {
                    canvas.drawText(text, circleRadius, textBound.height() / 2, mSelectedPaint);
                } else {
                    canvas.drawText(text, circleRadius, textBound.height() / 2, mPaint);
                }
                canvas.rotate(6);
                currentDegree = currentDegree + 6;
            }
            canvas.restore();
        }
    }

    /**
     * 画秒钟 数字
     *
     * @param canvas
     */
    private void drawSecondNumber(Canvas canvas) {
        if (isDrawText) {
            //旋转圆盘
//            Log.i("mSecondDegree",""+mSecondDegree);
            canvas.rotate(360 - mSecondDegree, getWidth() / 2, getHeight() / 2);

            canvas.save();
            mPaint.setColor(textColor);
            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setStrokeWidth(1);
            mPaint.setTextSize(textSize);
            canvas.translate(getWidth() / 2, getHeight() / 2);
            int currentDegree = 0;
            for (int i = 0; i < secondArray.length; i++) {
                Rect textBound = new Rect();//创建一个矩形
                String text = secondArray[i];
                //将文字装在上面创建的矩形中，即这个矩形就是文字的边框
                mPaint.getTextBounds(text, 0, text.length(), textBound);
                if ((currentDegree + 360 - mSecondDegree) % 360 == 0) {
                    canvas.drawText(text, circleRadius + getMaxTextWidth("60秒") + drawNumberSpace, textBound.height() / 2, mSelectedPaint);
                } else {
                    canvas.drawText(text, circleRadius + getMaxTextWidth("60秒") + drawNumberSpace, textBound.height() / 2, mPaint);
                }
                canvas.rotate(6);
                currentDegree = currentDegree + 6;
            }
            canvas.restore();
        }
    }

    private int getMaxTextWidth(String maxWText) {
        mPaint.setColor(textColor);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(1);
        mPaint.setTextSize(textSize);
        Rect textBound = new Rect();//创建一个矩形
        mPaint.getTextBounds(maxWText, 0, maxWText.length(), textBound);
        return textBound.width();
    }

    /**
     * 画秒针
     *
     * @param canvas
     */
    private void drawSecondWheel(Canvas canvas) {
        canvas.save();
        mPaint.setColor(secondPointerColor);
        mPaint.setStrokeWidth(secondPointerSize);
        canvas.rotate(mSecondDegree, getWidth() / 2, getHeight() / 2);
        canvas.drawLine(getWidth() / 2, getHeight() / 2, getWidth() / 2,
                getHeight() / 2 - secondPointerLength, mPaint);
        canvas.restore();
    }

    /**
     * 画分针
     *
     * @param canvas
     */
    private void drawMinuteWheel(Canvas canvas) {
        canvas.save();
        mPaint.setColor(minPointerColor);
        mPaint.setStrokeWidth(minPointerSize);
        canvas.rotate(mMinDegree, getWidth() / 2, getHeight() / 2);
        canvas.drawLine(getWidth() / 2, getHeight() / 2, getWidth() / 2,
                getHeight() / 2 - minPointerLength, mPaint);
        canvas.restore();
    }

    /**
     * 画时针
     *
     * @param canvas
     */
    private void drawHourWheel(Canvas canvas) {
        canvas.save();
        mPaint.setColor(hourPointerColor);
        mPaint.setStrokeWidth(hourPointerSize);
        canvas.rotate(mHourDegree, getWidth() / 2, getHeight() / 2);
        canvas.drawLine(getWidth() / 2, getHeight() / 2, getWidth() / 2,
                getHeight() / 2 - hourPointerLength, mPaint);
        canvas.restore();
    }

    /**
     * 画圆圈
     *
     * @param canvas
     */
    private void drawCircleOut(Canvas canvas) {
        //半径是宽度的1/3
        mPaint.setColor(borderColor);
        mPaint.setStrokeWidth(borderWidth);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setTextSize(textSize);
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, circleRadius, mPaint);

        //绘制水平渐变条
        Rect textBound = new Rect();//创建一个矩形
        String text = "60秒";
        mPaint.getTextBounds(text, 0, text.length(), textBound);
        //画水平线
        mPaint.setColor(Color.parseColor("#88FF4081"));
        mPaint.setStrokeWidth(textBound.height() + SizeUtils.dp2px(getContext(), 4));
        float startX = getWidth() / 2;
        float startY = getHeight() / 2;
        float endX = getWidth() / 2 + circleRadius + textBound.width() + drawNumberSpace + textBound.width();
        float endY = getHeight() / 2;
        LinearGradient llGradient = new LinearGradient(startX, startY, endX, endY, new int[]{0X88FF4081, 0X66FF4081, 0X33FF4081}, new float[]{0.0f, 0.8f, 1.0f}, Shader.TileMode.CLAMP);
        mPaint.setShader(llGradient);
        canvas.drawLine(startX, startY, endX, endY, mPaint);
        mPaint.setShader(null);
    }


    /**
     * 画圆心
     *
     * @param canvas
     */
    private void drawCirclePoint(Canvas canvas) {
        mPaint.setColor(centerPointColor);
        mPaint.setStrokeWidth(centerPointSize);
        if (centerPointType.equals("rect")) {
            canvas.drawPoint(getWidth() / 2, getHeight() / 2, mPaint);
        } else if (centerPointType.equals("circle")) {
            mPaint.setStyle(Paint.Style.FILL);
            canvas.drawCircle(getWidth() / 2, getHeight() / 2, centerPointRadiu, mPaint);
        }
    }


    /**
     * 画刻度
     *
     * @param canvas
     */
    private void drawKedu(Canvas canvas) {
        canvas.save();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(1);
        for (int i = 0; i < 360; i++) {
            if (i % 30 == 0) {//长刻度
                mPaint.setColor(maxScaleColor);
                canvas.drawLine(getWidth() / 2, getHeight() / 2 - (circleRadius - borderWidth / 2),
                        getWidth() / 2, getHeight() / 2 - (circleRadius - borderWidth / 2) + maxScaleLength, mPaint);
            } else if (i % 6 == 0) {//中刻度
                mPaint.setColor(midScaleColor);
                canvas.drawLine(getWidth() / 2, getHeight() / 2 - (circleRadius - borderWidth / 2),
                        getWidth() / 2, getHeight() / 2 - (circleRadius - borderWidth / 2) + midScaleLength, mPaint);
            } else {//短刻度
                mPaint.setColor(minScaleColor);
                canvas.drawLine(getWidth() / 2, getHeight() / 2 - (circleRadius - borderWidth / 2),
                        getWidth() / 2, getHeight() / 2 - (circleRadius - borderWidth / 2) + minScaleLength, mPaint);
            }
            canvas.rotate(1, getWidth() / 2, getHeight() / 2);
        }
        canvas.restore();
    }

    /**
     * 画圆盘背景
     *
     * @param canvas
     */
    private void drawCircleBg(Canvas canvas) {
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(circleBackground);
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, circleRadius - borderWidth / 2, mPaint);
    }

    /**
     * 数字
     *
     * @param canvas
     * @param degree
     * @param text
     * @param paint
     */
    private void drawNum(Canvas canvas, int degree, String text, Paint paint) {
        Rect textBound = new Rect();
        paint.getTextBounds(text, 0, text.length(), textBound);
        canvas.rotate(degree);
//        canvas.translate(0, borderWidth / 2 + maxScaleLength + textHeight * 3f / 4 - circleRadius);
        canvas.translate(0, 50 - circleRadius);//这里的50是坐标中心距离时钟最外边框的距离，当然你可以根据需要适当调节
        canvas.rotate(-degree);
//        canvas.drawText(text,-(textBound.right - textBound.left)/2,
//                (Math.abs(paint.ascent())-Math.abs(paint.descent()))/2,paint);
        canvas.drawText(text, -textBound.width() / 2,
                textBound.height() / 2, paint);
        canvas.rotate(degree);
//        canvas.translate(0, circleRadius - (borderWidth / 2 + maxScaleLength + textHeight * 3f / 4));
        canvas.translate(0, circleRadius - 50);
        canvas.rotate(-degree);
    }


//------------------------------------------------------------对外开放的方法------------------------------------------------------------------------------------
//------------------------------------------------------------对外开放的方法------------------------------------------------------------------------------------
//------------------------------------------------------------对外开放的方法------------------------------------------------------------------------------------

    /**
     * 获取时间对应的总秒数
     *
     * @return
     */
    public float getTimeTotalSecond() {
        if (mIsNight) {
            mTotalSecond = mHourDegree * 120 + 12 * 3600;
            return mTotalSecond;
        } else {
            mTotalSecond = mHourDegree * 120;
            return mTotalSecond;
        }
    }

    /**
     * 设置总时间（最大值为 24 * 3600）
     *
     * @param mTotalSecond
     */
    public void setTotalSecond(float mTotalSecond) {
        if (mTotalSecond >= (24 * 3600)) {
            this.mTotalSecond = mTotalSecond - 24 * 3600;
        } else {
            this.mTotalSecond = mTotalSecond;
        }
        int hour = (int) (mTotalSecond / 3600);
        int min = (int) ((mTotalSecond - hour * 3600) * 1.0 / 60);
        int second = (int) (mTotalSecond - hour * 3600 - min * 60);
        setTime(hour, min, second);
    }

    /**
     * 获取hour
     *
     * @return
     */
    public int getHour() {
        return (int) (getTimeTotalSecond() / 3600);
    }

    /**
     * 获取Min
     *
     * @return
     */
    public int getMin() {
        return (int) ((getTimeTotalSecond() - getHour() * 3600) / 60);
    }

    /**
     * 获取Second
     *
     * @return
     */
    public int getSecond() {
        return (int) (getTimeTotalSecond() - getHour() * 3600 - getMin() * 60);
    }

}
