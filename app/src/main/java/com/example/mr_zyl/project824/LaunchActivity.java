package com.example.mr_zyl.project824;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mr_zyl.project824.bean.UserBean;
import com.example.mr_zyl.project824.pro.base.view.BaseActivity;
import com.example.mr_zyl.project824.pro.mine.view.selfview.MySnackBarUtils;
import com.example.mr_zyl.project824.utils.StatusBarUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.objectbox.Box;

import static com.example.mr_zyl.project824.BaseApplication.myObjectBox;

public class LaunchActivity extends BaseActivity {

    @BindView(R.id.iv_launch_hai)
    ImageView iv_launch_hai;
    @BindView(R.id.tv_remainingTime)
    TextView tv_remainingTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtils.setFullActivity(this);

        timers.start();
//        ToastUtil.showToast(this,"2*2="+ MathKit.square(2));http://oak-goods.oss-cn-zhangjiakou.aliyuncs.com/oak/goods/ce3128cf4f9807c64e0f5510cdd130b2.jpg
        ImageLoader.getInstance().displayImage("drawable://" + R.drawable.hai, iv_launch_hai, new AnimateFirstDisplayListener());
//        String url ="http://oak-goods.oss-cn-zhangjiakou.aliyuncs.com/oak/goods/ce3128cf4f9807c64e0f5510cdd130b2.jpg";
//        ImageLoader.getInstance().displayImage(url, iv_launch_hai, new AnimateFirstDisplayListener());
    }

    @Override
    protected int initLayoutId() {
        return R.layout.activity_launch;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        StatusBarUtils.setFullActivity(this);
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

        if (all == null || all.size() == 0) {
            new MySnackBarUtils.Builder(this).setMessage("账号信息缺失，请重新登录！").show();
            startActivity(loginIntent);
        } else {
            String db_phone = all.get(0).getUsername();
            String db_password = all.get(0).getPassword();
            if (db_phone.equals(LoginActivity.RightPhone) && db_password.equals(LoginActivity.RightPass)) {
                startActivity(mainIntent);
            } else {
                startActivity(loginIntent);
            }
        }
        finish();
    }

}
