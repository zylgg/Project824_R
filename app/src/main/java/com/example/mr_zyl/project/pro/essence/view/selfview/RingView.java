package com.example.mr_zyl.project.pro.essence.view.selfview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import java.text.DecimalFormat;

/**
 * @author TFHR02
 */
public class RingView extends View {

    public RingView(Context context) {
        super(context);
    }


    public RingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!init) {
            initPaint();
        }
    }

    public RingView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    /**
     * 环的颜色
     */
    private final static int RingColor = Color.parseColor("#72d4fb");

    /**
     * 进度的颜色
     */
    private final static int PecentColor = Color.parseColor("#14b7f8");

    /**
     * 画笔
     */
    private Paint paint;
    /**
     * 是否第一次
     */
    private boolean init = false;
    /**
     * 背景
     */
    private static final int BackGround = Color.parseColor("#FF0000");
//    /** 
//     * 已经完成的颜色 
//     */  
//    private static final int CircleColor = Color.YELLOW;  

    /**
     * 完成扇形角度
     */
    private static final float startAngle = 360;
    /**
     * 扇形中心点X轴
     */
    private float content_X;
    /**
     * 扇形中心点Y轴
     */
    private float content_Y;
    /**
     * 环形外半径
     */
    private float bigRadius;
    /**
     * 环形内半径
     */
    private float smallRadius;
    /**
     * 默认终点角度
     */
    private float SweepAngle = 0;

    /**
     * 控件默认的宽度
     */
    private int DEFAULT_WIDTH = 100;
    /**
     * 控件宽
     */
    private int width = DEFAULT_WIDTH;
    /**
     * 控件高
     */
    private int height;
    /**
     * 文件显示的文本
     */
    private String text ="";
    private static final int TEXTSIZE = 25;
    /**
     * 环宽
     */
    private int ring_width = 10;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(DEFAULT_WIDTH, DEFAULT_WIDTH);
    }

    private void initPaint() {
        setPadding(0, 0, 0, 0);
        paint = new Paint();
        paint.setStyle(Style.FILL);
        paint.setAntiAlias(true);
        paint.setColor(RingColor);//ring的颜色  
        width = DEFAULT_WIDTH;
        height = DEFAULT_WIDTH;
        bigRadius = ((float) width / 2);
        smallRadius = (float) width / 2 - ring_width;
        content_X = (float) width / 2;
        content_Y = (float) height / 2;
        init = true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (SweepAngle <= 0) {
            return;
        }
//        super.onDraw(canvas);  
        paint.setColor(RingColor);//ring的颜色  
        Path path = new Path();
        path.reset();  
        /*画圆   Path.Direction.CCW 逆时针 Path.Direction.CW 顺时针*/
        path.addCircle(content_X, content_Y, bigRadius, Path.Direction.CCW);
        path.close();
        canvas.drawPath(path, paint);

        path.reset();
        paint.setColor(Color.WHITE);
        path.addCircle(content_X, content_Y, smallRadius, Path.Direction.CCW);
        path.close();
        canvas.drawPath(path, paint);
        getSectorClip(canvas, startAngle);


        path.reset();
        paint.setColor(Color.WHITE);
        path.addCircle(content_X, content_Y, smallRadius, Path.Direction.CCW);
        path.close();
        canvas.drawPath(path, paint);

        if (text != null) {
            paint.setColor(Color.GREEN);
            paint.setFakeBoldText(true);
            paint.setTextSize(TEXTSIZE);

            float text_w = paint.measureText(text);//文字的宽
            float text_h = Math.abs(paint.ascent() + paint.descent());//文字的高

            canvas.drawText(text, width / 2 - text_w / 2, height / 2 + text_h / 2, paint);
        }
    }

    /**
     * 返回一个扇形的剪裁区
     *
     * @param canvas     //画笔
     * @param startAngle //起始角度
     */
    private void getSectorClip(Canvas canvas, float startAngle) {
        paint.setColor(PecentColor);//进度的颜色  
        Path path = new Path();
        // 下面是获得一个三角形的剪裁区  
        path.moveTo(content_X, content_Y); // 圆心  
        path.lineTo(
                (float) (content_X + bigRadius * Math.cos(startAngle * Math.PI / 180)), // 起始点角度在圆上对应的横坐标  

                (float) (content_Y + bigRadius * Math.sin(startAngle * Math.PI / 180))); // 起始点角度在圆上对应的纵坐标  
        path.lineTo(
                (float) (content_X + bigRadius * Math.cos(SweepAngle * Math.PI / 180)), // 终点角度在圆上对应的横坐标  

                (float) (content_Y + bigRadius * Math.sin(SweepAngle * Math.PI / 180))); // 终点点角度在圆上对应的纵坐标  
        path.close();
        //设置一个正方形，内切圆
        RectF rectF = new RectF(content_X - bigRadius, content_Y - bigRadius, content_X + bigRadius,
                content_Y + bigRadius);
        // 下面是获得弧形剪裁区的方法  
        path.addArc(rectF, startAngle, SweepAngle - startAngle);
        canvas.drawPath(path, paint);

    }

    /**
     * @param startAngle 开始角度
     */
    public void setAngle(float startAngle) {
        DecimalFormat format = new DecimalFormat("#");
        float text = (startAngle / 360f) * 100;
        SweepAngle = (360 - startAngle);
        setText(format.format(text) + "%");

        invalidate();
    }

    public void setText(String text) {
        this.text = text;
    }
}  