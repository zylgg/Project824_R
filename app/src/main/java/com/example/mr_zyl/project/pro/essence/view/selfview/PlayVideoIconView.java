package com.example.mr_zyl.project.pro.essence.view.selfview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import com.example.mr_zyl.project.R;

/**
 * Created by TFHR02 on 2017/3/27.
 */
public class PlayVideoIconView extends View {
    /**
     * 控件的主题颜色
     */
    private int themecolor = Color.WHITE;
    private int DEFAULT_VALUE =60;
    private float default_width = DEFAULT_VALUE;//宽度
    private float default_height = DEFAULT_VALUE;//高度
    private float radius=DEFAULT_VALUE/2f;
    private Paint mPaint;
    private float ring_width=6f;

    public PlayVideoIconView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttr(context, attrs);
        initPaint();
    }

    /**
     * 初始化自定义属性
     *
     * @param context
     * @param attrs
     */
    private void initAttr(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.PlayVideoIconView);
        int attrsize = array.getIndexCount();
        for (int i = 0; i < attrsize; i++) {
            int index = array.getIndex(i);
            switch (index) {
                case R.styleable.PlayVideoIconView_themecolor:
                    themecolor = array.getColor(index, -1);
                    break;
            }
        }
    }

    /**
     * 初始化画笔
     */
    private void initPaint() {
        mPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(themecolor);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeCap(Paint.Cap.ROUND);//设置笔刷的样式 Paint.Cap.Round ,Cap.SQUARE等分别为圆形、方形
        mPaint.setStrokeJoin(Paint.Join.ROUND);//设置结合处的样子，Miter:结合处为锐角， Round:结合处为圆弧：BEVEL：结合处为直线。
        mPaint.setStrokeWidth(ring_width);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec,heightMeasureSpec);
        setMeasuredDimension(MeasuredSize(widthMeasureSpec), MeasuredSize(heightMeasureSpec));
    }
    /**
     * 测量大小
     *
     * @param Spec
     * @return
     */
    private int MeasuredSize(int Spec) {
        int mode = MeasureSpec.getMode(Spec);
        int size = 0;
        switch (mode) {
            case MeasureSpec.AT_MOST:
            case MeasureSpec.UNSPECIFIED:
                size = DEFAULT_VALUE;
                break;
            case MeasureSpec.EXACTLY:
                size = MeasureSpec.getSize(Spec);
                //重置宽、高、半径
                default_height=default_width=size;
                radius=default_width/2-ring_width/2;
                break;
        }
        return size;
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
        //画外界圆圈
        mPaint.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(default_width/2,default_height/2,radius,mPaint);
        //1，ae
        float ae= (float) ((default_width/2)*(Math.tan(30*Math.PI/180)));
        //2，eo
        float eo= (float) (ae*(Math.tan(30*Math.PI/180)));
        //画三角形abc区域
        Path path=new Path();
        path.moveTo(default_width/2-eo,default_height/2-ae);//a
        path.lineTo(default_width/2+2*eo,default_height/2);//b
        path.lineTo(default_width/2-eo,default_height/2+ae);//c
        path.close();
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawPath(path,mPaint);

    }
}
