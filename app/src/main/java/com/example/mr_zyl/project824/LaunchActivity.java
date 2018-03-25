package com.example.mr_zyl.project824;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mr_zyl.project824.pro.base.view.BaseActivity;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class LaunchActivity extends BaseActivity {

    @BindView(R.id.iv_launch_hai)
    ImageView iv_launch_hai;
    @BindView(R.id.tv_remainingTime)
    TextView tv_remainingTime;

    @Override
    protected int initLayoutId() {
        return R.layout.activity_launch;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);  //全屏
        hideBottomMenu();
        timers.start();
//        ToastUtil.showToast(this,"2*2="+ MathKit.square(2));
        ImageLoader.getInstance().displayImage("drawable://" + R.drawable.hai, iv_launch_hai, new AnimateFirstDisplayListener());
    }

    /**
     * 隐藏底部虚拟按键，且全屏
     */
    private void hideBottomMenu() {
        //隐藏虚拟按键，并且全屏
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT <= 19) { // lower api
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT > 19) {
            //for new api versions.
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    @OnClick(R.id.tv_remainingTime)
    public void onViewClicked() {
        timers.cancel();
        timers=null;
        startActivity(new Intent(LaunchActivity.this, MainActivity.class));
        finish();
    }

    /**
     * 图片加载监听事件
     **/
    private class AnimateFirstDisplayListener extends SimpleImageLoadingListener {

        final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            if (loadedImage != null) {
                ImageView imageView = (ImageView) view;
                boolean firstDisplay = !displayedImages.contains(imageUri);
                if (firstDisplay) {
//                    animate(imageView, 2000); // 设置image隐藏动画500ms
                    displayedImages.add(imageUri); // 将图片uri添加到集合中
                }

            }
        }
    }

    CountDownTimer timers = new CountDownTimer(4000, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            tv_remainingTime.setText("跳过（" + (millisUntilFinished-1000) / 1000 + "）");
        }

        @Override
        public void onFinish() {
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(LaunchActivity.this,
                            MainActivity.class));
                    finish();
                }
            });
        }
    };

    public void animate(View imageView, int durationMillis) {
        if (imageView != null) {
            AlphaAnimation fadeImage = new AlphaAnimation(1f, 1f);
            fadeImage.setDuration(durationMillis);
            fadeImage.setInterpolator(new LinearInterpolator());
            fadeImage.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {

                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            imageView.startAnimation(fadeImage);

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
