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
    private int DEFAULT_VALUE = 60;
    private float default_width = DEFAULT_VALUE;//宽度
    private float default_height = DEFAULT_VALUE;//高度
    private float radius = DEFAULT_VALUE / 2f;//圆环的半径
    private Paint mPaint;//画笔
    private float ring_width = 6f;//环的宽度
    private STATUS PlayStatus=STATUS.pause;//默认为暂停状态
    private float vertical_line_width = ring_width;//竖线宽度

    /**
     * 用于存放播放状态
     */
    public enum STATUS {
        playing, pause;
    }

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
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(themecolor);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeCap(Paint.Cap.ROUND);//设置笔刷的样式 Paint.Cap.Round ,Cap.SQUARE等分别为圆形、方形
        mPaint.setStrokeJoin(Paint.Join.ROUND);//设置结合处的样子，Miter:结合处为锐角， Round:结合处为圆弧：BEVEL：结合处为直线。
        mPaint.setStrokeWidth(ring_width);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
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
                default_height = default_width = size;
                radius = default_width / 2 - ring_width / 2;
                break;
        }
        return size;
    }

    /**
     * 获取播放状态
     * @return
     */
    public STATUS getPlayStatus() {
        return PlayStatus;
    }

    /**
     * 设置播放状态
     * @param playStatus
     */
    public void setPlayStatus(STATUS playStatus) {
        PlayStatus = playStatus;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
        //画外界圆圈
        mPaint.setStrokeWidth(ring_width);
        mPaint.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(default_width / 2, default_height / 2, radius, mPaint);
        //1，ae
        float ae = (float) ((default_width * 2 / 5) * (Math.tan(30 * Math.PI / 180)));
        //2，eo
        float eo = (float) (ae * (Math.tan(30 * Math.PI / 180)));
        //画三角形abc区域
        if (PlayStatus == STATUS.pause) {
            Path path = new Path();
            path.moveTo(default_width / 2 - eo, default_height / 2 - ae);//a
            path.lineTo(default_width / 2 + 2 * eo, default_height / 2);//b
            path.lineTo(default_width / 2 - eo, default_height / 2 + ae);//c
            path.close();
            mPaint.setStyle(Paint.Style.FILL);
            canvas.drawPath(path, mPaint);
        }
        //画两条竖线
        if (PlayStatus == STATUS.playing) {
            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setStrokeWidth(0.1f);
            canvas.drawRect(
                    default_width / 2 * 0.75f - vertical_line_width / 2,
                    default_height / 2 - ae,
                    default_width / 2 * 0.75f + vertical_line_width - vertical_line_width / 2,
                    default_height / 2 + ae, mPaint);//画左边的竖线
            canvas.drawRect(
                    default_width / 2 * 1.25f - vertical_line_width / 2,
                    default_height / 2 - ae,
                    default_width / 2 * 1.25f + vertical_line_width - vertical_line_width / 2,
                    default_height / 2 + ae, mPaint);//画右边的竖线
        }

    }
}
