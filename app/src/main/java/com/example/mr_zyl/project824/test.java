package com.example.mr_zyl.project824;

/**
 * Created by TFHR02 on 2017/6/23.
 */

public class test {

    private final float s = 2.70158f;
    private final float s_close = -4.70158f;
    float mDuration = 400f;

    public Float evaluate(float fraction, Float startValue, Float endValue) {
        float t = mDuration * fraction;
        float b = startValue.floatValue();
        float c = endValue.floatValue() - startValue.floatValue();
        float d = mDuration;
        float result = calculate(t, b, c, d);
        return result;
    }

    public Float calculate(float t, float b, float c, float d) {
        //t越来越接近d
        return c * ((t = t / d - 1f) * t * ((s_close + 1) * t + s_close) + 1) + b;
    }

    public static void main(String[] args) throws java.lang.Exception {
        test t = new test();
        float i = 0f;
        while (i <=  1) {
            System.out.println(t.evaluate(i, 0f, 1774f));
            i = i + (0.1f);
        }
    }
}