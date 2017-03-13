package com.example.mr_zyl.project;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

public class LaunchActivity extends Activity {

    ImageView iv_hai;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

//        ToastUtil.showToast(this,"2*2="+ MathKit.square(2));
        iv_hai = (ImageView) findViewById(R.id.iv_hai);
        //初始化补间动画
        AlphaAnimation alphaA = new AlphaAnimation(0, 1f);
        ScaleAnimation scaleA = new ScaleAnimation(0.8f, 1f, 0.8f, 1f, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        AnimationSet setA = new AnimationSet(true);
        setA.addAnimation(alphaA);
//        setA.addAnimation(scaleA);
        setA.setDuration(2000);
        setA.setFillAfter(true);
        setA.setInterpolator(new LinearInterpolator());//插值器-匀速
        //设置动画监听
        setA.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(LaunchActivity.this, MainActivity.class));
                        finish();
                    }
                });
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        //给imageview设置动画
        iv_hai.startAnimation(setA);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
