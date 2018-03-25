package com.example.mr_zyl.project824.pro.essence.view.selfview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.example.mr_zyl.project824.R;

import java.text.DecimalFormat;


/**
 * @author TFHR02
 */
public class RingView extends View {
    /**
     * 环进度值文本的颜色
     */
    private int ringtextcolor = 0;


    /**
     * 环的颜色
     */
    private final static int RingColor = Color.parseColor("#72d4fb");

    /**
     * 进度的颜色
     */
    private static int PecentColor = Color.parseColor("#14b7f8");

    /**
     * 画笔
     */
    private Paint paint;
    /**
     * 是否第一次
     */
    private boolean init = false;
    /**
     * 完成扇形角度
     */
    private static final float startAngle = 270;
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
    private String text = "";
    private static final int TEXTSIZE = 25;
    /**
     * 环宽
     */
    private int ring_width = 10;

    public RingView(Context context) {
        super(context);
    }

    public RingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(attrs,context);
        if (!init) {
            initPaint();
        }
    }
    private void initAttrs(AttributeSet attrs,Context context){
        TypedArray array = context.obtainStyledAttributes(attrs,R.styleable.RingView);
        int attrsize = array.getIndexCount();
        for (int i = 0; i < attrsize; i++) {
            int arrrid = array.getIndex(i);
            switch (arrrid) {
                case R.styleable.RingView_ringtextcolor:
                    ringtextcolor = array.getColor(arrrid, Color.argb(255, 0, 255, 0));
                    break;
                case R.styleable.RingView_pecentColor:
                    PecentColor=array.getColor(arrrid,Color.argb(255, 0, 255, 0));
                    break;
            }
        }
        array.recycle();
    }
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
        //终点角度小于等于0是不绘制
        if (SweepAngle <= 0) {
            setVisibility(INVISIBLE);
        }else if(SweepAngle==360){
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    setVisibility(INVISIBLE);
                }
            },200);
        }else{
            setVisibility(VISIBLE);
        }
        getSectorClip(canvas);

        //覆盖扇形区域的中间那一个小圆
//        paint.setColor(Color.WHITE);
//        canvas.drawCircle(content_X, content_Y, smallRadius,paint);

        //绘制进度数值
        if (text != null) {
            paint.setColor(ringtextcolor);
            paint.setStyle(Style.FILL);
            paint.setFakeBoldText(true);
            paint.setTextSize(TEXTSIZE);

            float text_w = paint.measureText(text);//文字的宽
            float text_h = Math.abs(paint.ascent() + paint.descent());//文字的高
            //从文字左下角开始绘制
            canvas.drawText(text, width / 2 - text_w / 2, height / 2 + text_h / 2, paint);
        }
    }

    /**
     * 返回一个扇形的剪裁区
     *
     * @param canvas     //画笔
     */
    private void getSectorClip(Canvas canvas) {

        paint.setStrokeWidth(ring_width);
        paint.setStyle(Style.STROKE);
        //设置一个正方形-圆外接正方形
        RectF rectF = new RectF(ring_width/2,ring_width/2,width-ring_width/2,height-ring_width/2);

        Path path = new Path();
        paint.setColor(Color.GRAY);//进度的颜色
        // 下面是获得弧形剪裁区的方法  （参数2，开始角度；参数3，所要扫过的角度）
        path.addArc(rectF, 0, 360);
        canvas.drawPath(path, paint);

        Path path2 = new Path();
        paint.setColor(PecentColor);//进度的颜色
        // 下面是获得弧形剪裁区的方法  （参数2，开始角度；参数3，所要扫过的角度）
        path2.addArc(rectF, startAngle, SweepAngle);
        canvas.drawPath(path2, paint);

    }

    /**
     * @param Angle 角度
     */
    public void setAngle(float Angle) {
        DecimalFormat format = new DecimalFormat("#");
        float text = (Angle / 360f) * 100;
        SweepAngle = Angle;
        setText(format.format(text) + "%");

        invalidate();
    }

    public void setText(String text) {
        this.text = text;
    }
}  