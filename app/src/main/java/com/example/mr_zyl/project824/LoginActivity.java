package com.example.mr_zyl.project824;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import android.widget.TextView;
import android.widget.VideoView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.mr_zyl.project824.bean.UserBean;
import com.example.mr_zyl.project824.pro.base.view.BaseActivity;
import com.example.mr_zyl.project824.utils.StatusBarUtils;
import com.example.mr_zyl.project824.utils.ToastUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.objectbox.Box;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

import static com.example.mr_zyl.project824.BaseApplication.myObjectBox;

public class LoginActivity extends BaseActivity {

    @BindView(R.id.jcv_login_videopic)
    VideoView jcv_login_videopic;
    @BindView(R.id.tiet_login_phone)
    TextInputEditText tiet_login_phone;
    @BindView(R.id.tiet_login_password)
    TextInputEditText tiet_login_password;

    @BindView(R.id.til_login_phone)
    TextInputLayout til_login_phone;
    @BindView(R.id.til_login_password)
    TextInputLayout til_login_password;

    @BindView(R.id.ll_login)
    LinearLayout ll_login;
    @BindView(R.id.rl_login_main)
    RelativeLayout rl_login_main;

    @BindView(R.id.bt_login_main)
    Button bt_login_main;

    private Uri local_video_uri;
    @BindView(R.id.mpb_login_loading)
    MaterialProgressBar mpb_login_loading;

    @BindView(R.id.lav_animation_view)
    LottieAnimationView lav_animation_view;

    /**
     * 弹起软键盘窗体 需要滚动的高度
     */
    private int scrollHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtils.setTransparent(this);
        mpb_login_loading.setVisibility(View.GONE);
        local_video_uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.login);
        initBackVideo(local_video_uri);

        //禁止手机号复制粘贴
        tiet_login_phone.setTextIsSelectable(false);
        tiet_login_phone.setLongClickable(false);

        controlKeyboardLayout(rl_login_main);
    }

    @Override
    protected int initLayoutId() {
        return R.layout.activity_login;
    }


    public void initBackVideo(Uri uri) {
        //播放路径
        jcv_login_videopic.setVideoURI(uri);
        //播放
        jcv_login_videopic.start();
        //循环播放
        jcv_login_videopic.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                jcv_login_videopic.start();
            }
        });
    }

    /**
     * @param scrollToView 被键盘遮挡的scrollToView，滚动root,使scrollToView在root可视区域的底部
     */
    private void controlKeyboardLayout(final View scrollToView) {
        // 注册一个回调函数，当在一个视图树中全局布局发生改变 或者视图树中的某个视图的可视状态发生改变 时调用这个回调函数。
        ll_login.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        Rect rect = new Rect();
                        // 获取root在窗体的可视区域
                        ll_login.getWindowVisibleDisplayFrame(rect);
                        // 当前视图最外层的高度减去现在所看到的视图的最底部的y坐标
                        int rootInvisibleHeight = ll_login.getRootView().getHeight() - rect.bottom;
                        Log.i("tag", "最外层的高度" + ll_login.getRootView().getHeight());
                        // 若rootInvisibleHeight高度大于100，则说明当前视图上移了，说明软键盘弹出了
                        if (rootInvisibleHeight > 100) {
                            //软键盘弹出来的时候
                            int[] location = new int[2];
                            // 获取scrollToView在窗体的坐标
                            scrollToView.getLocationInWindow(location);
                            // 计算root滚动高度，使scrollToView在可见区域的底部
                            scrollHeight += (location[1] + scrollToView.getHeight()) - rect.bottom;
                        } else { // 软键盘没有弹出来的时候
                            scrollHeight = 0;
                        }
                        ll_login.scrollTo(0, scrollHeight);
                    }
                });
    }

    @Override
    protected void onRestart() {
        //返回重新加载
        initBackVideo(local_video_uri);
        super.onRestart();
    }


    @Override
    protected void onStop() {
        //防止锁屏或者弹出的时候，音乐在播放
        jcv_login_videopic.stopPlayback();
        super.onStop();
    }

    boolean isInputPhoneSuccess, isInputPasswordSuccess;

    /**
     * 登录
     *
     * @param V
     */
    public void LoginMain(View V) {
        bt_login_main.setEnabled(false);
        bt_login_main.setText("");
        mpb_login_loading.setVisibility(View.VISIBLE);
        //模拟网络加载
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mpb_login_loading.setVisibility(View.GONE);
                checkLogins();
            }
        }, 1500);

    }

    /**
     * 正确的手机号
     */
    public static final String RightPhone = "13651274057";
    /**
     * 正确的密码
     */
    public static final String RightPass = "zylgg";
    public static final String phoneErrorLog="手机号输入有误";
    public static final String passwordErrorLog="密码输入有误";

    private void checkLogins() {
        //获取缓存的账号信息
        String phone = tiet_login_phone.getText().toString();
        String password = tiet_login_password.getText().toString();
        Box<UserBean> userBeanBox = myObjectBox.boxFor(UserBean.class);
        List<UserBean> all = userBeanBox.getAll();
        if ((all == null || all.size() == 0)) {//没缓存时且输入正确时，保存到数据库
            isInputPhoneSuccess = phone.equals(RightPhone);
            isInputPasswordSuccess = password.equals(RightPass);
            if (isInputPhoneSuccess && isInputPasswordSuccess) {
                UserBean bean = new UserBean();
                bean.setUsername(phone);
                bean.setPassword(password);
                userBeanBox.put(bean);
            }
        } else {//有缓存，判断输入是否正确
            String catch_phone = all.get(0).getUsername();
            String catch_password = all.get(0).getPassword();
            isInputPhoneSuccess = phone.equals(catch_phone);
            isInputPasswordSuccess = password.equals(catch_password);
        }

        if (!isInputPhoneSuccess && isInputPasswordSuccess) {
            til_login_phone.setError(phoneErrorLog);
        } else if (isInputPhoneSuccess && !isInputPasswordSuccess) {
            til_login_password.setError(passwordErrorLog);
        } else if (!isInputPhoneSuccess && !isInputPasswordSuccess) {
            til_login_phone.setError(phoneErrorLog);
            til_login_password.setError(passwordErrorLog);
        }

        if (!isInputPasswordSuccess || !isInputPhoneSuccess) {
            bt_login_main.setEnabled(true);
            bt_login_main.setText("登录");
            return;
        }

        //开始加载成功的动画
        lav_animation_view.setVisibility(View.VISIBLE);
        lav_animation_view.playAnimation();
        lav_animation_view.addAnimatorListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            }
        });
    }

}
