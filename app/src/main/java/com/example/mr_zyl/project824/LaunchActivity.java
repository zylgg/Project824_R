package com.example.mr_zyl.project824;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mr_zyl.project824.bean.UserBean;
import com.example.mr_zyl.project824.pro.base.view.BaseActivity;
import com.example.mr_zyl.project824.utils.StatusBarUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.objectbox.Box;

import static com.example.mr_zyl.project824.BaseApplication.myObjectBox;

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
        StatusBarUtils.setFullActivity(this);
        timers.start();
//        ToastUtil.showToast(this,"2*2="+ MathKit.square(2));
        ImageLoader.getInstance().displayImage(
                "drawable://" + R.drawable.hai,
                iv_launch_hai, new AnimateFirstDisplayListener());
    }

    @OnClick(R.id.tv_remainingTime)
    public void onViewClicked() {
        timers.cancel();
        timers = null;
        startNextActivity();
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
            tv_remainingTime.setText("跳过（" + (millisUntilFinished - 1000) / 1000 + "）");
        }

        @Override
        public void onFinish() {
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    startNextActivity();
                }
            });
        }
    };

    /**
     * 下一步
     */
    private void startNextActivity() {
        Intent mainIntent = new Intent(this, MainActivity.class);
        Intent loginIntent = new Intent(this, LoginActivity.class);
        //获取缓存的账号信息
        Box<UserBean> userBeanBox = myObjectBox.boxFor(UserBean.class);
        List<UserBean> all = userBeanBox.getAll();

        if (all==null||all.size()==0){
            startActivity(loginIntent);
        }else{
            String db_phone=all.get(0).getUsername();
            String db_password=all.get(0).getPassword();
            if (db_phone.equals("13651274057")&&db_password.equals("zylgg")){
                startActivity(mainIntent);
            }else{
                startActivity(loginIntent);
            }
        }

        finish();
    }

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
