package com.example.mr_zyl.project.pro.publish.animation;

import android.animation.TypeEvaluator;

/**
 * 估值器： 用于计算动画从开始到结束的过渡值
 */
public class KickBackAnimator implements TypeEvaluator<Float> {
    public float s = 2.70158f;
    private float mDuration = 0f;
    private final float s_close = -4.70158f;
    private boolean is_closeAnimation;

    public void setDuration(float duration) {
        mDuration = duration;
    }

    public boolean is_closeAnimation() {
        return is_closeAnimation;
    }

    public void setIs_closeAnimation(boolean is_closeAnimation) {
        this.is_closeAnimation = is_closeAnimation;
    }

    /**
     * 动画[结束值]减去[开始值]然后乘以[动画完成度]，再加上[开始值]，就可以得到[当前动画的值]了。
     *
     * @param fraction   代表当前动画的完成度
     * @param startValue 动画的开始值
     * @param endValue   动画的结束值
     * @return
     */
    public Float evaluate(float fraction, Float startValue, Float endValue) {
        if (is_closeAnimation()){
            s=s_close;
        }
        float t = mDuration * fraction;
        float b = startValue.floatValue();
        float c = endValue.floatValue() - startValue.floatValue();
        float d = mDuration;
        float result = calculate(t, b, c, d);
        return result;
    }

    public Float calculate(float t, float b, float c, float d) {
        return c * ((t = t / d - 1f) * t * ((s + 1) * t + s) + 1) + b;
    }
}
