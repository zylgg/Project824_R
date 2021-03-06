/**
 * Copyright 2016 andy
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.mr_zyl.project824.pro.mine.view.selfview;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.example.mr_zyl.project824.R;

/**
 */
public class SmileyLoadingView extends View {

    private static final int DEFAULT_WIDHT = 30;
    private static final int DEFAULT_HEIGHT = 30;
    private static final int DEFAULT_PAINT_WIDTH = 5;

    private static final int DEFAULT_ANIM_DURATION = 2000;
    private static final int DEFAULT_PAINT_COLOR = Color.parseColor("#b3d8f3");
    /**
     * 旋转偏移值
     */
    private static final int ROTATE_OFFSET = 90;

    private Paint mArcPaint, mCirclePaint;
    private Path mCirclePath, mArcPath;
    private RectF mRectF;

    private float[] mCenterPos, mLeftEyePos, mRightEyePos;
    private float mStartAngle, mSweepAngle;
    private float mEyeCircleRadius;

    private int mStrokeColor;
    private int mAnimDuration;
    private int mAnimRepeatCount;

    private int mStrokeWidth;
    public boolean mRunning;
    private boolean mStopping;

    private boolean mFirstStep;
    private boolean mShowLeftEye, mShowRightEye;
    private boolean mStopUntilAnimationPerformCompleted;

    private OnAnimPerformCompletedListener mOnAnimPerformCompletedListener;
    private ValueAnimator mValueAnimator;

    public SmileyLoadingView(Context context) {
        this(context, null);
    }

    public SmileyLoadingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SmileyLoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SmileyLoadingView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        // get attrs
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.SmileyLoadingView);
        mStrokeColor = ta.getColor(R.styleable.SmileyLoadingView_strokeColor, DEFAULT_PAINT_COLOR);
        mStrokeWidth = ta.getDimensionPixelSize(R.styleable.SmileyLoadingView_strokeWidth, dp2px(DEFAULT_PAINT_WIDTH));
        mAnimDuration = ta.getInt(R.styleable.SmileyLoadingView_duration, DEFAULT_ANIM_DURATION);
        mAnimRepeatCount = ta.getInt(R.styleable.SmileyLoadingView_animRepeatCount, ValueAnimator.INFINITE);
        ta.recycle();
        //初始化扫过的度数，圆路径，弧的路径
        mSweepAngle = 180; // init sweepAngle, the mouth line sweep angle
        mCirclePath = new Path();
        mArcPath = new Path();
        //初始化画圆路劲的画笔
        mArcPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mArcPaint.setStyle(Paint.Style.STROKE);
        mArcPaint.setStrokeCap(Paint.Cap.ROUND);//设置笔刷的样式 Paint.Cap.Round ,Cap.SQUARE等分别为圆形、方形
        mArcPaint.setStrokeJoin(Paint.Join.ROUND);//设置结合处的样子，Miter:结合处为锐角， Round:结合处为圆弧：BEVEL：结合处为直线。
        mArcPaint.setStrokeWidth(mStrokeWidth);//画的圆的线条粗细值
        mArcPaint.setColor(mStrokeColor);
        //初始化画圆的画笔
        mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCirclePaint.setStyle(Paint.Style.STROKE);
        mCirclePaint.setStrokeCap(Paint.Cap.ROUND);
        mCirclePaint.setStrokeJoin(Paint.Join.ROUND);
        mCirclePaint.setStyle(Paint.Style.FILL);
        mCirclePaint.setColor(mStrokeColor);
        //中心点坐标集合，左眼坐标集合，右眼坐标集合
        mCenterPos = new float[2];
        mLeftEyePos = new float[2];
        mRightEyePos = new float[2];
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(measureWidthSize(widthMeasureSpec), measureHeightSize(heightMeasureSpec));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mRunning) {//动画开始了
            if (mShowLeftEye) {//确定显示左眼
                canvas.drawCircle(mLeftEyePos[0], mLeftEyePos[1], mEyeCircleRadius, mCirclePaint);
            }

            if (mShowRightEye) {//确定显示右眼
                canvas.drawCircle(mRightEyePos[0], mRightEyePos[1], mEyeCircleRadius, mCirclePaint);
            }
//            if (mFirstStep) { //第一次加载
                mArcPath.reset();
                mArcPath.addArc(mRectF, mStartAngle, mSweepAngle); //(圆弧的外接正方形,起始的角度,移动的度数)
                canvas.drawPath(mArcPath, mArcPaint);
//            } else {
//                mArcPath.reset();
//                mArcPath.addArc(mRectF, mStartAngle, mSweepAngle);
//                canvas.drawPath(mArcPath, mArcPaint);
//            }
        } else {
            canvas.drawCircle(mLeftEyePos[0], mLeftEyePos[1], mEyeCircleRadius, mCirclePaint);
            canvas.drawCircle(mRightEyePos[0], mRightEyePos[1], mEyeCircleRadius, mCirclePaint);

            mArcPath.reset();
            mArcPath.addArc(mRectF, mStartAngle, mSweepAngle);
//            mArcPath.addArc(mRectF, 360, 90);
            canvas.drawPath(mArcPath, mArcPaint);
        }
    }

    /**
     * 测量宽度
     *
     * @param measureSpec spec
     * @return width
     */
    private int measureWidthSize(int measureSpec) {
        int defSize = dp2px(DEFAULT_WIDHT);
        int specSize = MeasureSpec.getSize(measureSpec);
        int specMode = MeasureSpec.getMode(measureSpec);

        int result = 0;
        switch (specMode) {
            case MeasureSpec.UNSPECIFIED:
            case MeasureSpec.AT_MOST:
                //最小的值为默认值
                result = Math.min(defSize, specSize);
                break;
            case MeasureSpec.EXACTLY:
                result = specSize;
                break;
        }
        return result;
    }

    /**
     * 测量高度
     *
     * @param measureSpec spec
     * @return height
     */
    private int measureHeightSize(int measureSpec) {
        int defSize = dp2px(DEFAULT_HEIGHT);
        int specSize = MeasureSpec.getSize(measureSpec);
        int specMode = MeasureSpec.getMode(measureSpec);

        int result = 0;
        switch (specMode) {
            case MeasureSpec.UNSPECIFIED:
            case MeasureSpec.AT_MOST:
                //最小值为默认值
                result = Math.min(defSize, specSize);
                break;
            case MeasureSpec.EXACTLY:
                result = specSize;
                break;
        }
        return result;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //计算内边距
        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();

        int width = getWidth();
        int height = getHeight();
        //计算圆心坐标
        mCenterPos[0] = (width - paddingRight + paddingLeft) >> 1;
        mCenterPos[1] = (height - paddingBottom + paddingTop) >> 1;
        //计算圆的半径
        float radiusX = (width - paddingRight - paddingLeft - mStrokeWidth) >> 1;
        float radiusY = (height - paddingTop - paddingBottom - mStrokeWidth) >> 1;
        float radius = Math.min(radiusX, radiusY);
        //计算眼睛的圆图形的半径
        mEyeCircleRadius = mStrokeWidth / 2;

        mRectF = new RectF(paddingLeft + mStrokeWidth, paddingTop + mStrokeWidth,
                width - mStrokeWidth - paddingRight, height - mStrokeWidth - paddingBottom);

        //定义一个圆弧(1,圆弧外接矩形。2，圆弧开始角度。3，转动多少角度（正，顺；负，逆）)
        mArcPath.arcTo(mRectF, 0, 180);

        //PathDirectionCCW   逆时针 PathDirectionCW  顺时针
        mCirclePath.addCircle(mCenterPos[0], mCenterPos[1], radius, Path.Direction.CW);

        //测量路径
        PathMeasure circlePathMeasure = new PathMeasure(mCirclePath, true);
        // 获取指定长度的位置坐标及该点切线值（getLength()：获取Path的长度）
        circlePathMeasure.getPosTan(circlePathMeasure.getLength() / 8 * 5, mLeftEyePos, null);
        circlePathMeasure.getPosTan(circlePathMeasure.getLength() / 8 * 7, mRightEyePos, null);
        //微调，控制眼珠的位置，
        mLeftEyePos[0] += mStrokeWidth >> 2;
        mLeftEyePos[1] += mStrokeWidth >> 1;
        mRightEyePos[0] -= mStrokeWidth >> 2;
        mRightEyePos[1] += mStrokeWidth >> 1;
    }

    /**
     * 当view从窗口分离时触发
     */
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mValueAnimator != null && mValueAnimator.isRunning()) {
            mValueAnimator.end();
        }
    }

    /**
     * 设置画笔透明度
     *
     * @param alpha alpha
     */
    public void setPaintAlpha(int alpha) {
        mArcPaint.setAlpha(alpha);
        mCirclePaint.setAlpha(alpha);
        invalidate();
    }

    /**
     * Set paint stroke color
     *
     * @param color color
     */
    public void setStrokeColor(int color) {
        mStrokeColor = color;
        invalidate();
    }

    /**
     * Set paint stroke width
     *
     * @param width px
     */
    public void setStrokeWidth(int width) {
        mStrokeWidth = width;
    }

    /**
     * Set animation running duration
     *
     * @param duration duration
     */
    public void setAnimDuration(int duration) {
        mAnimDuration = duration;
    }

    /**
     * Set animation repeat count, ValueAnimator.INFINITE(-1) means cycle
     *
     * @param repeatCount repeat count
     */
    public void setAnimRepeatCount(int repeatCount) {
        mAnimRepeatCount = repeatCount;
    }

    public void start(int animRepeatCount) {
        if (mRunning) {//如果正在运行则不执行
            return;
        }
        //设置无限循环
        mAnimRepeatCount = animRepeatCount;
        //默认第一次加载
        mFirstStep = true;

        mValueAnimator = ValueAnimator.ofFloat(ROTATE_OFFSET, 720.0f + 2 * ROTATE_OFFSET);//从90度，要转2圈在加90度
        mValueAnimator.setDuration(mAnimDuration);
        mValueAnimator.setInterpolator(new LinearInterpolator());
        mValueAnimator.setRepeatCount(mAnimRepeatCount);
        mValueAnimator.setRepeatMode(ValueAnimator.RESTART);//从头开始动画
        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (!animation.isRunning()) {
                    return;
                }
                float animatedValue = (float) animation.getAnimatedValue();
                update(animatedValue);
            }
        });
        mValueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                mRunning = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mRunning = false;
                mStopping = false;
                if (mOnAnimPerformCompletedListener != null) {
                    mOnAnimPerformCompletedListener.onCompleted();
                }
                ToReset();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mRunning = false;
                mStopping = false;
                if (mOnAnimPerformCompletedListener != null) {
                    mOnAnimPerformCompletedListener.onCompleted();
                }
                ToReset();
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                if (mStopUntilAnimationPerformCompleted) {
                    animation.cancel();
                    mStopUntilAnimationPerformCompleted = false;
                }
            }
        });
        mValueAnimator.start();
    }

    private void update(float angle) {
        mFirstStep = angle / 360.0f <= 1;//是否不到一圈，即[0~360]度
        if (mFirstStep) {//不到一圈
            mShowLeftEye = angle % 360 > 225.0f;//取值区间[225,360n) n为圈数
            mShowRightEye = angle % 360 > 315.0f;//取值区间[315n,360n) n为圈数

            //设置弧扫描角时的步骤是：第一，设定值0.1f类似一个圆
            mSweepAngle = 0.1f;
            mStartAngle = angle;
        } else {
            //开始角度经过眼睛的时候不显示，
            mShowLeftEye = (angle / 360.0f <= 2) && angle % 360 <= 225.0f;//[0+360n,225+360n];n>=1
            mShowRightEye = (angle / 360.0f <= 2) && angle % 360 <= 315.0f;//[0+360n,315+360n];n>=1
            if (angle >= (720.0f + ROTATE_OFFSET)) {//[90+720,180+720]//（最后一个）90度形成微笑收尾动作。
                mStartAngle = angle - (720.0f + ROTATE_OFFSET);
                mSweepAngle = ROTATE_OFFSET - mStartAngle;
            } else {
                mStartAngle = (angle / 360.0f <= 1.625) ? 0 : angle - mSweepAngle - 360;
                //乘以3/5是因为要在angle=720.0f + ROTATE_OFFSET时保持终点坐标为ROTATE_OFFSET，是为下一个（最后一个）90度形成微笑收尾动作。
                mSweepAngle = (angle / 360.0f <= 1.625) ? angle % 360 : 225 - (angle - 225 - 360) / 5 * 3;
            }
        }
        invalidate();
    }

    /**
     * smile
     *
     * @param angle
     */
    public void smile(float angle) {
        update(angle);
    }

    /**
     * Start animation
     */
    public void startSmile() {
        start(ValueAnimator.INFINITE);//无限循环
    }

    /**
     * Stop animation
     */
    public void stopSmile() {
        stopSmile(true);
    }

    /**
     * 是否在动画结束后停止，还是立即停止（false立即停止，true动画结束后停止）
     *
     * @param stopUntilAnimationPerformCompleted boolean
     */
    public void stopSmile(boolean stopUntilAnimationPerformCompleted) {
        //三种可能：1，已经设置动画结束后停止就不再执行；2，没有设置动画不执行；3，动画没有运行了就不再执行
        if (mStopping || mValueAnimator == null || !mValueAnimator.isRunning()) {
            return;
        }
        mStopping = stopUntilAnimationPerformCompleted;

        mStopUntilAnimationPerformCompleted = stopUntilAnimationPerformCompleted;
        if (mValueAnimator != null && mValueAnimator.isRunning()) {
            if (!stopUntilAnimationPerformCompleted) {
                mValueAnimator.end();
            }
        } else {
            mStopping = false;
            if (mOnAnimPerformCompletedListener != null) {
                mOnAnimPerformCompletedListener.onCompleted();
            }
        }
    }

    /**
     * set status changed listener
     *
     * @param l OnStatusChangedListener
     */
    public void setOnAnimPerformCompletedListener(OnAnimPerformCompletedListener l) {
        mOnAnimPerformCompletedListener = l;
    }

    /**
     * reset UI
     */
    private void ToReset() {
        mStartAngle = 0;
        mSweepAngle = 180;
        invalidate();
    }

    /**
     * dp to px
     *
     * @param dpValue dp
     * @return px
     */
    private int dp2px(float dpValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * Callback
     */
    public interface OnAnimPerformCompletedListener {
        void onCompleted();
    }
}
